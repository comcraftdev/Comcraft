package net.comcraft.src;

import java.util.Vector;
import javax.microedition.m3g.*;
import net.comcraft.client.Comcraft;

public final class RenderBlocks {

    private final Comcraft cc;
    private Graphics3D g3D;
    private Appearance[] appearanceList;
    private Fog fog;
    private boolean renderAllFaces;
    private Vector cullingOffTextures;

    public RenderBlocks(Comcraft cc) {
        this.cc = cc;
        fog = new Fog();
        g3D = Graphics3D.getInstance();

        cullingOffTextures = new Vector(20);

        addCullingOffTextures(Block.getBlock("redFlower").getUsedTexturesList());
        addCullingOffTextures(Block.getBlock("yellowFlower").getUsedTexturesList());
        addCullingOffTextures(Block.getBlock("treePlant").getUsedTexturesList());
        addCullingOffTextures(Block.getBlock("toadstool").getUsedTexturesList());
        addCullingOffTextures(Block.getBlock("mushroom").getUsedTexturesList());
        addCullingOffTextures(Block.getBlock("wheat").getUsedTexturesList());
    }

    private void addCullingOffTextures(int[] list) {
        for (int n = 0; n < list.length; ++n) {
            Integer i = new Integer(list[n]);

            cullingOffTextures.addElement(i);
        }
    }

    public void releaseRenderBlock() {
        appearanceList = null;
    }

    public void reloadRenderBlock() {
        appearanceList = new Appearance[512];

        Material material = new Material();
        material.setVertexColorTrackingEnable(false);
//        material.setColor(Material.AMBIENT, 0x00FF0000);

        CompositingMode compositingMode = new CompositingMode();
        compositingMode.setBlending(CompositingMode.ALPHA);
        compositingMode.setAlphaThreshold(0.1f);

        PolygonMode polygonModeNormal = new PolygonMode();
        polygonModeNormal.setPerspectiveCorrectionEnable(cc.settings.fancyGraphics);
        polygonModeNormal.setShading(PolygonMode.SHADE_FLAT);
        polygonModeNormal.setCulling(PolygonMode.CULL_BACK);
        polygonModeNormal.setLocalCameraLightingEnable(false);
        polygonModeNormal.setTwoSidedLightingEnable(false);

        PolygonMode polygonModeCullingOff = new PolygonMode();
        polygonModeCullingOff.setPerspectiveCorrectionEnable(cc.settings.fancyGraphics);
        polygonModeCullingOff.setShading(PolygonMode.SHADE_FLAT);
        polygonModeCullingOff.setCulling(PolygonMode.CULL_NONE);
        polygonModeCullingOff.setLocalCameraLightingEnable(false);
        polygonModeCullingOff.setTwoSidedLightingEnable(false);

        setFogColor(0xB0E0E6);
        setFogDistance(130);

        for (int i = 0; i < 512; ++i) {
            appearanceList[i] = new Appearance();
            appearanceList[i].setTexture(0, cc.textureProvider.getTerrainTexture(i));
            appearanceList[i].setMaterial(material);
            appearanceList[i].setCompositingMode(compositingMode);

            if (cc.settings.fog) {
                appearanceList[i].setFog(fog);
            }

            if (cullingOffTextures.contains(new Integer(i))) {
                appearanceList[i].setPolygonMode(polygonModeCullingOff);
            } else {
                appearanceList[i].setPolygonMode(polygonModeNormal);
            }
        }
    }

    public void setFogColor(int color) {
        fog.setColor(color);
    }

    public void setFogDistance(float distance) {
        fog.setLinear(distance - cc.settings.renderDistance * 5, distance);
    }

    public void renderBlockAllFaces(Block block, int x, int y, int z, Transform transform) {
        renderAllFaces = true;
        renderBlockByRenderType(block, x, y, z, transform, null, null);
        renderAllFaces = false;
    }

    public void renderBlockByRenderType(Block block, int x, int y, int z, Transform transform, EntityPlayer player, ChunkPiece chunkPiece) {
        int i = block.getRenderType();

        if (i == 0 || ((i == 1 || i == 4) && chunkPiece == null)) {
            renderStandardBlock(block, x, y, z, transform, player);
        } else if (i == 1) {
            renderPiecedBlock(block, x, y, z, transform, player, chunkPiece);
        } else if (i == 2) {
            renderFlowerBlock(block, x, y, z, transform);
        } else if (i == 3) {
            renderLiquidBlock(block, x, y, z, transform, player);
        } else if (i == 4) {
            renderCarpetBlock(block, x, y, z, transform, player, chunkPiece);
        } else if (i == 5) {
            renderTorchBlock(block, x, y, z, transform, player);
        } else if (i == 6) {
            renderModelBlock(block, x, y, z, transform);
        } else if (i == 7) {
            renderStairsBlock(block, x, y, z, transform, player);
        } else if (i == 8) {
            renderFenceBlock(block, x, y, z, transform, player);
        } else if (i == 9) {
            renderBedBlock(block, x, y, z, transform, player);
        }
    }

    private void renderFenceBlock(Block block, int x, int y, int z, Transform transform, EntityPlayer player) {
        for (int n = 0; n < 12; ++n) {
            if (block.shouldSideBeRendered(cc.world, x, y, z, n) || (renderAllFaces && n < 6)) {
                renderFenceFace(block, x, y, z, n, transform);
            }
        }
    }

    private void renderFenceFace(Block block, int x, int y, int z, int side, Transform transform) {
        VertexBuffer[] blockVertexBuffer = block.getBlockVertexBufferSided(cc.world, x, y, z);

        Transform transform1 = block.getBlockTransform(cc.world, x, y, z, transform, side);

        if (blockVertexBuffer != null) {
            g3D.render(blockVertexBuffer[side % 6], block.getBlockIndexBuffer(), appearanceList[block.getBlockTexture(cc.world, x, y, z, side)], transform1);
        }
    }

    private void renderStairsBlock(Block block, int x, int y, int z, Transform transform, EntityPlayer player) {
        renderTransformedFace(block, x, y, z, 0, transform);
        renderTransformedFace(block, x, y, z, 1, transform);
        renderTransformedFace(block, x, y, z, 2, transform);
        renderTransformedFace(block, x, y, z, 3, transform);

        if (renderAllFaces || (player.yPos > (y + 0.5f))) {
            renderTransformedFace(block, x, y, z, 4, transform);
        }
        if (renderAllFaces || (player.yPos < (y) && block.shouldSideBeRendered(cc.world, x, y - 1, z, 5))) {
            renderTransformedFace(block, x, y, z, 5, transform);
        }

        renderTransformedFace(block, x, y, z, 6, transform);
        renderTransformedFace(block, x, y, z, 7, transform);
        renderTransformedFace(block, x, y, z, 8, transform);
        renderTransformedFace(block, x, y, z, 9, transform);

        if (renderAllFaces || (player.yPos > (y + 1) && block.shouldSideBeRendered(cc.world, x, y + 1, z, 10))) {
            renderTransformedFace(block, x, y, z, 10, transform);
        }
    }

    private void renderModelBlock(Block block, int x, int y, int z, Transform transform) {
        Node node = block.getBlockModel(cc.world, x, y, z);

        if (node != null) {
            g3D.render(node, transform);
        }
    }

    private void renderTorchBlock(Block block, int x, int y, int z, Transform transform, EntityPlayer player) {
        if (renderAllFaces || (block.shouldSideBeRendered(cc.world, x, y, z + 1, 0))) {
            renderTransformedFace(block, x, y, z, 0, transform);
        }
        if (renderAllFaces || (block.shouldSideBeRendered(cc.world, x, y, z - 1, 1))) {
            renderTransformedFace(block, x, y, z, 1, transform);
        }
        if (renderAllFaces || (block.shouldSideBeRendered(cc.world, x - 1, y, z, 2))) {
            renderTransformedFace(block, x, y, z, 2, transform);
        }
        if (renderAllFaces || (block.shouldSideBeRendered(cc.world, x + 1, y, z, 3))) {
            renderTransformedFace(block, x, y, z, 3, transform);
        }
        if (renderAllFaces || (block.shouldSideBeRendered(cc.world, x, y + 1, z, 4))) {
            renderTransformedFace(block, x, y, z, 4, transform);
        }
        if (renderAllFaces || (block.shouldSideBeRendered(cc.world, x, y - 1, z, 5))) {
            renderTransformedFace(block, x, y, z, 5, transform);
        }
    }

    private void renderTransformedFace(Block block, int x, int y, int z, int side, Transform transform) {
        VertexBuffer[] blockVertexBuffer = block.getBlockVertexBufferSided(cc.world, x, y, z);

        Transform transform1 = block.getBlockTransform(cc.world, x, y, z, transform, side);

        if (blockVertexBuffer != null) {
            g3D.render(blockVertexBuffer[side], block.getBlockIndexBuffer(), appearanceList[block.getBlockTexture(cc.world, x, y, z, side)], transform1);
        }
    }

    private void renderCarpetBlock(Block block, int x, int y, int z, Transform transform, EntityPlayer player, ChunkPiece chunkPiece) {
        if (renderAllFaces || (player.zPos > (z + 0.5f) && block.shouldSideBeRendered(cc.world, x, y, z + 1, 0))) {
            renderPiecedFace(block, x, y, z, 0, chunkPiece, transform);
        }
        if (renderAllFaces || (player.zPos < (z + 0.5f) && block.shouldSideBeRendered(cc.world, x, y, z - 1, 1))) {
            renderPiecedFace(block, x, y, z, 1, chunkPiece, transform);
        }
        if (renderAllFaces || (player.xPos < (x + 0.5f) && block.shouldSideBeRendered(cc.world, x - 1, y, z, 2))) {
            renderPiecedFace(block, x, y, z, 2, chunkPiece, transform);
        }
        if (renderAllFaces || (player.xPos > (x + 0.5f) && block.shouldSideBeRendered(cc.world, x + 1, y, z, 3))) {
            renderPiecedFace(block, x, y, z, 3, chunkPiece, transform);
        }
        if (renderAllFaces || (player.yPos > (y + 0.1f) && block.shouldSideBeRendered(cc.world, x, y + 1, z, 4))) {
            renderPiecedFace(block, x, y, z, 4, chunkPiece, transform);
        }
        if (renderAllFaces || (player.yPos < (y + 0.0f) && block.shouldSideBeRendered(cc.world, x, y - 1, z, 5))) {
            renderPiecedFace(block, x, y, z, 5, chunkPiece, transform);
        }
    }

    private void renderFlowerBlock(Block block, int x, int y, int z, Transform transform) {
        renderStandardFace(block, x, y, z, 0, transform);
        renderStandardFace(block, x, y, z, 1, transform);
    }

    private void renderBedBlock(Block block, int x, int y, int z, Transform transform, EntityPlayer player) {
        if (renderAllFaces || (player.zPos > (z + 1f) && block.shouldSideBeRendered(cc.world, x, y, z + 1, 0))) {
            renderStandardFace(block, x, y, z, 0, transform);
        }
        if (renderAllFaces || (player.zPos < (z + 0f) && block.shouldSideBeRendered(cc.world, x, y, z - 1, 1))) {
            renderStandardFace(block, x, y, z, 1, transform);
        }
        if (renderAllFaces || (player.xPos < (x + 0f) && block.shouldSideBeRendered(cc.world, x - 1, y, z, 2))) {
            renderStandardFace(block, x, y, z, 2, transform);
        }
        if (renderAllFaces || (player.xPos > (x + 1f) && block.shouldSideBeRendered(cc.world, x + 1, y, z, 3))) {
            renderStandardFace(block, x, y, z, 3, transform);
        }
        if (renderAllFaces || (player.yPos > (y + 0.5f) && block.shouldSideBeRendered(cc.world, x, y + 1, z, 4))) {
            renderStandardFace(block, x, y, z, 4, transform);
        }
        if (renderAllFaces || (player.yPos < (y + 0f) && block.shouldSideBeRendered(cc.world, x, y - 1, z, 5))) {
            renderStandardFace(block, x, y, z, 5, transform);
        }
    }

    private void renderStandardBlock(Block block, int x, int y, int z, Transform transform, EntityPlayer player) {
        if (renderAllFaces || (player.zPos > (z + 1f) && block.shouldSideBeRendered(cc.world, x, y, z + 1, 0))) {
            renderStandardFace(block, x, y, z, 0, transform);
        }
        if (renderAllFaces || (player.zPos < (z + 0f) && block.shouldSideBeRendered(cc.world, x, y, z - 1, 1))) {
            renderStandardFace(block, x, y, z, 1, transform);
        }
        if (renderAllFaces || (player.xPos < (x + 0f) && block.shouldSideBeRendered(cc.world, x - 1, y, z, 2))) {
            renderStandardFace(block, x, y, z, 2, transform);
        }
        if (renderAllFaces || (player.xPos > (x + 1f) && block.shouldSideBeRendered(cc.world, x + 1, y, z, 3))) {
            renderStandardFace(block, x, y, z, 3, transform);
        }
        if (renderAllFaces || (player.yPos > (y + 1f) && block.shouldSideBeRendered(cc.world, x, y + 1, z, 4))) {
            renderStandardFace(block, x, y, z, 4, transform);
        }
        if (renderAllFaces || (player.yPos < (y + 0f) && block.shouldSideBeRendered(cc.world, x, y - 1, z, 5))) {
            renderStandardFace(block, x, y, z, 5, transform);
        }
    }

    private void renderLiquidBlock(Block block, int x, int y, int z, Transform transform, EntityPlayer player) {
        if (renderAllFaces || (player.zPos > (z + 1f) && block.shouldSideBeRendered(cc.world, x, y, z + 1, 0))) {
            renderStandardFace(block, x, y, z, 0, transform);
        }
        if (renderAllFaces || (player.zPos < (z + 0f) && block.shouldSideBeRendered(cc.world, x, y, z - 1, 1))) {
            renderStandardFace(block, x, y, z, 1, transform);
        }
        if (renderAllFaces || (player.xPos < (x + 0f) && block.shouldSideBeRendered(cc.world, x - 1, y, z, 2))) {
            renderStandardFace(block, x, y, z, 2, transform);
        }
        if (renderAllFaces || (player.xPos > (x + 1f) && block.shouldSideBeRendered(cc.world, x + 1, y, z, 3))) {
            renderStandardFace(block, x, y, z, 3, transform);
        }
        if (renderAllFaces || (player.yPos > (y + 0f) && block.shouldSideBeRendered(cc.world, x, y + 1, z, 4))) {
            renderStandardFace(block, x, y, z, 4, transform);
        }
        if (renderAllFaces || (player.yPos < (y + 0f) && block.shouldSideBeRendered(cc.world, x, y - 1, z, 5))) {
            renderStandardFace(block, x, y, z, 5, transform);
        }
    }

    private void renderStandardFace(Block block, int x, int y, int z, int side, Transform transform) {
        VertexBuffer[] blockVertexBuffer = block.getBlockVertexBufferSided(cc.world, x, y, z);

        if (blockVertexBuffer != null) {
            g3D.render(blockVertexBuffer[side], block.getBlockIndexBuffer(), appearanceList[block.getBlockTexture(cc.world, x, y, z, side)], transform);
        }
    }

    private void renderPiecedBlock(Block block, int x, int y, int z, Transform transform, EntityPlayer player, ChunkPiece chunkPiece) {
        if (renderAllFaces || (player.zPos > (z + 1f) && block.shouldSideBeRendered(cc.world, x, y, z + 1, 0))) {
            renderPiecedFace(block, x, y, z, 0, chunkPiece, transform);
        }
        if (renderAllFaces || (player.zPos < (z + 0f) && block.shouldSideBeRendered(cc.world, x, y, z - 1, 1))) {
            renderPiecedFace(block, x, y, z, 1, chunkPiece, transform);
        }
        if (renderAllFaces || (player.xPos < (x + 0f) && block.shouldSideBeRendered(cc.world, x - 1, y, z, 2))) {
            renderPiecedFace(block, x, y, z, 2, chunkPiece, transform);
        }
        if (renderAllFaces || (player.xPos > (x + 1f) && block.shouldSideBeRendered(cc.world, x + 1, y, z, 3))) {
            renderPiecedFace(block, x, y, z, 3, chunkPiece, transform);
        }
        if (renderAllFaces || (player.yPos > (y + 1f) && block.shouldSideBeRendered(cc.world, x, y + 1, z, 4))) {
            renderPiecedFace(block, x, y, z, 4, chunkPiece, transform);
        }
        if (renderAllFaces || (player.yPos < (y + 0f) && block.shouldSideBeRendered(cc.world, x, y - 1, z, 5))) {
            renderPiecedFace(block, x, y, z, 5, chunkPiece, transform);
        }
    }

    private void renderPiecedFace(Block block, int x, int y, int z, int side, ChunkPiece chunkPiece, Transform transform) {
        if (chunkPiece.needsRender(cc.render.currentTick, side)) {
            g3D.render(chunkPiece.getVertexBuffer(cc.world, side), block.getBlockIndexBuffer(), appearanceList[block.getBlockTexture(cc.world, x, y, z, side)], transform);
        }
    }
}
