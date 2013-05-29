package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import net.comcraft.client.Comcraft;

public class GuiSlotSelectTexturepack extends GuiSlot {

    public GuiSlotSelectTexturepack(GuiSelectTexturepack guiScreenSlotHost) {
        super(guiScreenSlotHost);
    }

    protected void initSlotSize() {
        //TODORES: wielkość slotu

        xPos = 0;
        slotScreenWidth = cc.screenWidth;
        slotWidth = cc.screenWidth - 10 * 2;

        yPos = 3 + cc.g.getFont().getHeight() + 3;
        slotScreenHeight = cc.screenHeight - yPos - 5 * 2 - GuiButtonSmall.getButtonHeight();

        if (Comcraft.getScreenHeight() == 320) {
            slotHeight = 50;
        } else if (Comcraft.getScreenHeight() == 240) {
            slotHeight = 50;
        } else if (Comcraft.getScreenHeight() == 220) {
            slotHeight = 50;
        } else if (Comcraft.getScreenHeight() == 400) {
            slotHeight = 50;
        } else if (Comcraft.getScreenHeight() == 480) {
            slotHeight = 60;
        } else if (Comcraft.getScreenHeight() == 640) {
            slotHeight = 70;
        } else if (Comcraft.getScreenHeight() == 800) {
            slotHeight = 70;
        }
    }

    protected void initSlot() {
    }

    protected void drawSlot(int i) {
        int y = getSlotY(i);

        if (!isSlotVisibile(y)) {
            return;
        }

        if (selectedElement == i) {
            drawSelectedSlotFrame(y);
        }

        int interval = (slotHeight - cc.g.getFont().getHeight() * 2) / 3;

        TexturePack texturePack = (TexturePack) guiScreenSlotHost.getElementsList().elementAt(i);

        if (texturePack == null) {
            return;
        }

        if (texturePack.isTexturePackSupported()) {
            cc.g.setColor(255, 255, 255);
            drawStringWithShadow(cc.g, texturePack.getTexturePackName(), (cc.screenWidth - slotWidth) / 2 + 10, y + interval, Graphics.TOP | Graphics.LEFT);

            cc.g.setColor(200, 200, 200);
            drawStringWithShadow(cc.g, texturePack.getTexturePackDescription(), (cc.screenWidth - slotWidth) / 2 + 15, y + interval * 2 + cc.g.getFont().getHeight(), Graphics.TOP | Graphics.LEFT);
        } else if (texturePack.isThereAnyError()) {
            cc.g.setColor(110, 110, 110);
            drawStringWithShadow(cc.g, texturePack.getTexturePackName(), (cc.screenWidth - slotWidth) / 2 + 10, y + interval, Graphics.TOP | Graphics.LEFT);

            cc.g.setColor(0x8B0000);
            drawStringWithShadow(cc.g, texturePack.getErrorMessage(), (cc.screenWidth - slotWidth) / 2 + 15, y + interval * 2 + cc.g.getFont().getHeight(), Graphics.TOP | Graphics.LEFT);
        } else {
            cc.g.setColor(110, 110, 110);
            drawStringWithShadow(cc.g, texturePack.getTexturePackName(), (cc.screenWidth - slotWidth) / 2 + 10, y + interval, Graphics.TOP | Graphics.LEFT);

            cc.g.setColor(0x8B0000);
            drawStringWithShadow(cc.g, cc.langBundle.getText("GuiSlotSelectTexturepack.invalidResolutionText"), (cc.screenWidth - slotWidth) / 2 + 15, y + interval * 2 + cc.g.getFont().getHeight(), Graphics.TOP | Graphics.LEFT);
        }
    }
}
