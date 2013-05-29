package net.comcraft.src;

import javax.microedition.lcdui.Image;
import net.comcraft.client.Comcraft;

public class GuiButtonSmall extends GuiButton {

    public GuiButtonSmall(Comcraft cc, int id, int posX, int posY, String displayString) {
        super(cc, id, posX, posY, displayString);
    }

    protected Image getButtonImage() {
        return cc.textureProvider.getImage("gui/button_small.png");
    }

    protected int getWidth() {
        return getButtonWidth();
    }

    protected int getHeight() {
        return getButtonHeight();
    }

    public static int getButtonWidth() {
        if (Comcraft.getScreenWidth() == 240) {
            return 110;
        } else if (Comcraft.getScreenWidth() == 320) {
            return 150;
        } else if (Comcraft.getScreenWidth() == 360) {
            return 170;
        } else if (Comcraft.getScreenWidth() == 480) {
            return 230;
        } else if (Comcraft.getScreenWidth() == 176) {
            return 75;
        }
        
        return -1;
    }

    public static int getButtonHeight() {
        if (Comcraft.getScreenWidth() == 240) {
           return 25;  
        } else if (Comcraft.getScreenWidth() == 360) {
            return 35;
        } else if (Comcraft.getScreenWidth() == 320 && Comcraft.getScreenHeight() == 240) {
            return 20;
        } else if (Comcraft.getScreenWidth() == 320) {
            return 30;
        } else if (Comcraft.getScreenWidth() == 480) {
            return 35;
        } else if (Comcraft.getScreenWidth() == 176) {
            return 20;
        }
        
        return -1;
    }
}
