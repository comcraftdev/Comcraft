/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comcraft.src;

import javax.microedition.lcdui.Image;

/**
 *
 * @author Piotr Wójcik (Queader)
 */
public class GuiButtonArea extends GuiButton {

    private int width;
    private int height;

    public GuiButtonArea(GuiScreen guiIngame, int id, int x, int y, int width, int height) {
        super(null, id, x, y, null);

        this.width = width;
        this.height = height;
        
        drawButton = false;
    }

    public void drawButton(GuiButton selectedButton) {
    }

    protected Image getButtonImage() {
        return null;
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }
}