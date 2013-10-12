/*
 * Copyright (C) 2013 Piotr Wójcik
 * 
 * This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.comcraft.src;

import java.util.Calendar;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.Graphics3D;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

import net.comcraft.client.Comcraft;

public final class Render {
    private class Background extends JsObject {
        private static final int ID_SET_COLOR = 100;
        private static final int ID_GET_COLOR = 101;
        public javax.microedition.m3g.Background bg;

        public Background() {
            super(OBJECT_PROTOTYPE);
            bg = new javax.microedition.m3g.Background();
            addNative("setColor", ID_SET_COLOR, 1);
            addNative("getColor", ID_GET_COLOR, 0);
        }

        public void setColor(int c) {
            bg.setColor(c);
        }

        public int getColor() {
            return bg.getColor();
        }

        public void evalNative(int id, JsArray stack, int sp, int parCount) {
            switch (id) {
            case ID_SET_COLOR:
                setColor(stack.getInt(sp + 2));
                break;
            case ID_GET_COLOR:
                stack.setInt(sp, getColor());
                break;
            default:
                super.evalNative(id, stack, sp, parCount);
            }
        }

    }

    private final Comcraft cc;
    private Background background;
    private Camera camera;
    public Graphics3D g3D;
    public RenderBlocks renderBlock;
    public RenderBlockPreview renderBlockPreview;
    private Vector currentChunksList;
    private Frustum frustum;
    private static final Vec3D upVec = new Vec3D(0, 1, 0);
    public long currentTick;
    public RenderEffects renderEffects;
    private Image targetGraphicsImage;
    public Render(Comcraft cc) {
        this.cc = cc;
        g3D = Graphics3D.getInstance();

        currentTick = 1;

        renderEffects = new RenderEffects(cc);

        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 21) {
        }
    }

    public void initRender() {
        renderBlock = new RenderBlocks(cc);
        renderBlockPreview = new RenderBlockPreview(renderBlock);

        background = new Background();
        background.setColor(0xB0E0E6);
        ModGlobals.event.runEvent("Render.Init", new Object[] { background });
//        background.setColor(0x191970);

        camera = new Camera();
        reloadCamera();

        currentChunksList = new Vector(1024);

        frustum = new Frustum();
        reloadFrustum();

        reloadTargetGraphics();
    }

    public void reloadTargetGraphics() {
        if (cc.settings.resolutionScale == 1) {
        } else {
            targetGraphicsImage = Image.createImage(Comcraft.getScreenWidth() / cc.settings.resolutionScale, Comcraft.getScreenHeight() / cc.settings.resolutionScale);
            targetGraphicsImage.getGraphics();
        }
    }

    public Image getScreenshot() {
        Image image = Image.createImage(Comcraft.getScreenWidth() * cc.settings.screenshotResolution / 2, Comcraft.getScreenHeight() * cc.settings.screenshotResolution / 2);

        Graphics g2 = image.getGraphics();

        Graphics g = cc.g;

        cc.g = g2;

        renderGame();

        cc.g = g;

        if (Touch.isTouchSupported()) {
            return Image.createImage(image, 0, 0, image.getWidth(), image.getHeight(), Sprite.TRANS_ROT270);
        }

        return image;
    }

    public void reloadFrustum() {
        frustum.setCamInternals(cc.settings.fov + 3 + cc.settings.renderDistance * 6f, (float) Comcraft.screenWidth / Comcraft.screenHeight, 1f, 800.0f);
    }

    public void reloadCamera() {
        camera.setPerspective(cc.settings.fov, (float) Comcraft.screenWidth / Comcraft.screenHeight, 1f, 800.0f);
    }

    public void releaseRender() {
        renderBlock.releaseRenderBlock();
        renderBlockPreview.releaseRenderBlockPreview();
    }

    public void reloadRender() {
        renderBlock.reloadRenderBlock();
        renderBlockPreview.reloadRenderBlockPreview();
    }

    public void renderAll() {
        clearScreen();

        if (!cc.isGamePaused) {
            if (cc.currentScreen == null || (!cc.currentScreen.getSkipRender())) {
                renderGame();
            }

            if (cc.settings.renderGui) {
                cc.guiIngame.drawIngameGui();
            }
        }

        if (cc.currentScreen != null) {
            cc.currentScreen.drawScreen();
        }

        if (cc.settings.showFps) {
            drawFPS();
        }
        if (cc.settings.debugInfo) {
            drawDebugInfo();
        }

        cc.flushGraphics();
    }

    private void drawDebugInfo() {
        if (cc.world == null) {
            return;
        }

        int startX = Comcraft.screenWidth / 4;

        cc.g.setColor(0, 0, 0);
        cc.g.drawString("x: " + cc.player.xPos, startX, 3, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("y: " + cc.player.yPos, startX, 3 + (cc.g.getFont().getHeight() + 3), Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("z: " + cc.player.zPos, startX, 3 + (cc.g.getFont().getHeight() + 3) * 2, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("rotation yaw: " + cc.player.rotationYaw, startX, 3 + (cc.g.getFont().getHeight() + 3) * 3, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("rotation pitch: " + cc.player.rotationPitch, startX, 3 + (cc.g.getFont().getHeight() + 3) * 4, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("key: " + Keyboard.getCurrentKey(), startX, 3 + (cc.g.getFont().getHeight() + 3) * 10, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("last key: " + Keyboard.getLastKey(), startX, 3 + (cc.g.getFont().getHeight() + 3) * 11, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("loaded chunks: " + cc.world.chunkProvider.getLoadedChunksNum(), startX, 3 + (cc.g.getFont().getHeight() + 3) * 5, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("chunks queue: " + cc.world.chunkProvider.getChunksQueueNum(), startX, 3 + (cc.g.getFont().getHeight() + 3) * 6, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("free RAM: " + Runtime.getRuntime().freeMemory() / 1048576f + " MB", startX, 3 + (cc.g.getFont().getHeight() + 3) * 7, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("used RAM: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576f + " MB", startX, 3 + (cc.g.getFont().getHeight() + 3) * 8, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("total RAM: " + Runtime.getRuntime().totalMemory() / 1048576f + " MB", startX, 3 + (cc.g.getFont().getHeight() + 3) * 9, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("touch x: " + Touch.getX(), startX, 3 + (cc.g.getFont().getHeight() + 3) * 12, Graphics.TOP | Graphics.LEFT);
        cc.g.drawString("touch y: " + Touch.getY(), startX, 3 + (cc.g.getFont().getHeight() + 3) * 13, Graphics.TOP | Graphics.LEFT);
    }

    private void renderGame() {
        if (cc.settings.fog) {
            renderBlock.setFogDistance((cc.settings.renderDistance) * 40);
        }

        updateFrustum();

        g3D.bindTarget(cc.g, true, cc.settings.antialiasing ? Graphics3D.ANTIALIAS : 0);

        g3D.clear(background.bg);
        g3D.setCamera(camera, cc.player.getPlayerTransform());

        try {
            renderWorld();

            renderEffects.renderEffects();
        } catch (OutOfMemoryError error) {
            if (!cc.settings.ignoreOutOfMemory) {
                throw error;
            }
        } finally {
            g3D.releaseTarget();
        }
    }

    private void updateFrustum() {
        Vec3D posVec = cc.player.getPosition();
        Vec3D lookTemp = cc.player.getLook();
        Vec3D lookVec = new Vec3D(posVec.x - lookTemp.x, posVec.y + lookTemp.y, posVec.z - lookTemp.z);

        frustum.setCamDef(posVec, lookVec, upVec);
    }

    private void renderWorld() {
        cc.world.updateCurrentChunksList(currentChunksList);

        for (int i = 0; i < currentChunksList.size(); ++i) {
            renderChunkA((Chunk) currentChunksList.elementAt(i), true);
        }
        for (int i = 0; i < currentChunksList.size(); ++i) {
            renderChunkA((Chunk) currentChunksList.elementAt(i), false);
        }

        ++currentTick;
    }

    private void renderChunkA(Chunk chunk, boolean renderOpaque) {
        if (chunk == null || chunk.isEmptyChunk()) {
            return;
        }

        RenderChunkPieces renderChunk = chunk.getRenderChunk();

        if (renderChunk == null) {
            return;
        }

        int xChunkCenter = (chunk.xPos << 2) + 2;
        int zChunkCenter = (chunk.zPos << 2) + 2;

        for (int i = 0; i < 8; ++i) {
            ChunkStorage blockStorage = chunk.getBlockStorageArray()[i];

            int yChunkCenter = (i << 2) + 2;

            if (blockStorage == null || !blockStorage.containsBlocks || (renderOpaque == false && !blockStorage.containsNonOpaqueBlocks) || frustum.sphereInFrustum(new Vec3D(xChunkCenter, yChunkCenter, zChunkCenter), 3.5f) == Frustum.OUTSIDE) {
                continue;
            }

            for (int z = 0; z < 4; ++z) {
                for (int y = 0; y < 4; ++y) {
                    for (int x = 0; x < 4; ++x) {
                        Block block = Block.blocksList[blockStorage.getBlockID(x, y, z)];

                        if (block == null || block.isNormal() != renderOpaque) {
                            continue;
                        }

                        ChunkPiece chunkPiece = renderChunk.chunkPieces[x][(i << 2) + y][z];

                        if (chunkPiece != null) {
                            renderBlock.renderBlockByRenderType(block, (chunk.xPos << 2) + x, (i << 2) + y, (chunk.zPos << 2) + z, chunkPiece.getTransform(), cc.player, chunkPiece);
                        }
                    }
                }
            }
        }
    }

    
    private void clearScreen() {
        cc.g.setColor(background.getColor());
        cc.g.fillRect(0, 0, Comcraft.screenWidth, Comcraft.screenHeight);
    }

    private void drawFPS() {
        cc.g.setColor(0, 0, 0);
        cc.g.drawString("" + (int) (1 / cc.dt), 3, 3, Graphics.TOP | Graphics.LEFT);
    }
}
