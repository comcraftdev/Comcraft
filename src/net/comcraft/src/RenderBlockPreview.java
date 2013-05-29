package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.m3g.*;

public final class RenderBlockPreview {

    private RenderBlocks renderBlock;
    private Image[] blockPreviewImages;

    public RenderBlockPreview(RenderBlocks renderBlock) {
        this.renderBlock = renderBlock;
        blockPreviewImages = new Image[256];
    }

    public Image getBlockPreviewImage(int id) {
        return blockPreviewImages[id];
    }

    public void releaseRenderBlockPreview() {
        blockPreviewImages = null;
    }
    
    public void reloadRenderBlockPreview() {
        Camera camera = new Camera();
        camera.setPerspective(50, 1, 1, 200);

        Transform blockTrans = new Transform();
        blockTrans.postTranslate(-10 * 0.625f, -10 * 0.35f, -10);
        blockTrans.postRotate(45, 0, 1, 0);
        blockTrans.postRotate(25, 1, 0, 1);

        Transform camTrans = new Transform();
        camTrans.postTranslate(0, 0, 18);

        Background background = new Background();
        background.setColor(0xFFFFFF);

        Light light = new Light();
        light.setMode(Light.DIRECTIONAL);
        light.setIntensity(2.0f);

        for (int i = 0; i < 256; ++i) {
            blockPreviewImages[i] = Image.createImage(GuiButtonMoveControl.getButtonWidth(), GuiButtonMoveControl.getButtonHeight());

            if (Block.blocksList[i] != null) {
                Graphics g = blockPreviewImages[i].getGraphics();
                Graphics3D g3d = Graphics3D.getInstance();
                g3d.bindTarget(g, true, Graphics3D.ANTIALIAS);
                g3d.resetLights();
                g3d.addLight(light, camTrans);
                g3d.clear(background);
                g3d.setCamera(camera, camTrans);
                renderBlock.renderBlockAllFaces(Block.blocksList[i], 0, 0, 0, blockTrans);
                g3d.releaseTarget();
                
                if (Touch.isTouchSupported()) {
                    Image image = Image.createImage(blockPreviewImages[i], 0, 0, GuiButtonMoveControl.getButtonWidth(), GuiButtonMoveControl.getButtonHeight(), Sprite.TRANS_ROT90);
                    blockPreviewImages[i] = image;
                }
            }
        }
    }
}
