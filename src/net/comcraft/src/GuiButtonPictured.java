/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comcraft.src;

import javax.microedition.lcdui.Image;
import net.comcraft.client.Comcraft;

/**
 *
 * @author Piotr Wójcik (Queader)
 */
public class GuiButtonPictured extends GuiButton {

    protected GuiScreen guiScreen;
    protected int transform;
    protected String imageName;

    public GuiButtonPictured(Comcraft cc, GuiScreen guiIngame, int id, int x, int y, String imageName, int transform) {
        super(cc, id, x, y);
        this.transform = transform;
        this.imageName = imageName;
        this.guiScreen = guiIngame;

        initButtonSprite();
    }

    protected Image getButtonImage() {
        Image image = cc.textureProvider.getImage(imageName);
        return Image.createImage(image, 0, 0, image.getWidth(), image.getHeight(), transform);
    }

    protected int getHooverState(boolean flag) {
        byte byte0 = 0;

        if (flag || !enabled) {
            byte0 = 1;
        }

        return byte0;
    }

    protected void drawButtonString(boolean flag, int y) {
        //There is no String, only Image! (String == null)
    }
    
    public boolean checkPoint(int x, int y) {
        return enabled && drawButton && x > xPos && y > yPos && x <= xPos + getWidth() && y <= yPos + getHeight();
    }

    protected int getWidth() {
        return getButtonWidth();
    }

    protected int getHeight() {
        return getButtonHeight();
    }

    public static int getButtonWidth() {
        return 50;
    }

    public static int getButtonHeight() {
        return 50;
    }
}