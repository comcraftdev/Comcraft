package net.comcraft.src;

import javax.microedition.lcdui.Image;
import net.comcraft.client.Comcraft;

public class GuiButtonMoveControl extends GuiButtonPictured {

    public GuiButtonMoveControl(Comcraft cc, GuiScreen guiIngame, int id, int x, int y, String imageName, int transform) {
        super(cc, guiIngame, id, x, y, imageName, transform);
    }

    protected Image getButtonImage() {
        Image image = cc.textureProvider.getImage(imageName);
        return Image.createImage(image, 0, 0, image.getWidth(), image.getHeight(), transform);
    }

    protected int getHooverState(boolean flag) {
        if (flag && !Touch.isDragged()) {
            guiScreen.handleGuiAction(this);
        }

        byte byte0 = 0;

        if (flag || !enabled) {
            byte0 = 1;
        }

        return byte0;
    }

    public static int getButtonWidth() {
        return 50;
    }

    public static int getButtonHeight() {
        return 50;
    }
}
