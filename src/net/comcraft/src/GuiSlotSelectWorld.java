package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import net.comcraft.client.Comcraft;

public class GuiSlotSelectWorld extends GuiSlot {

    public GuiSlotSelectWorld(GuiSelectWorld guiWorldList) {
        super(guiWorldList);
    }

    protected void initSlot() {
    }

    protected void initSlotSize() {
        xPos = 0;
        slotScreenWidth = cc.screenWidth;
        slotWidth = cc.screenWidth - 10 * 2;

        yPos = 3 + cc.g.getFont().getHeight() + 3;
        slotScreenHeight = cc.screenHeight - yPos - 5 * 4 - GuiButtonSmall.getButtonHeight() * 3;

        if (Comcraft.getScreenHeight() == 320) {
            slotHeight = 30;
        } else if (Comcraft.getScreenHeight() == 240) {
            slotHeight = 30;
        } else if (Comcraft.getScreenHeight() == 220) {
            slotHeight = 30;
        }  else if (Comcraft.getScreenHeight() == 400) {
            slotHeight = 30;
        } else if (Comcraft.getScreenHeight() == 480) {
            slotHeight = 40;
        } else if (Comcraft.getScreenHeight() == 640) {
            slotHeight = 60;
        } else if (Comcraft.getScreenHeight() == 800) {
            slotHeight = 60;
        }
    }

    protected void drawSlot(int i) {
        int y = getSlotY(i);

        if (!isSlotVisibile(y)) {
            return;
        }

        if (selectedElement == i) {
            drawSelectedSlotFrame(y);
        }

        cc.g.setColor(255, 255, 255);

        WorldSaveType worldInfo = (WorldSaveType) guiScreenSlotHost.getElementsList().elementAt(i);

        drawStringWithShadow(cc.g, worldInfo.getWorldName(), (cc.screenWidth - slotWidth) / 2 + 10, y + slotHeight / 2 - cc.g.getFont().getHeight() / 2, Graphics.TOP | Graphics.LEFT);
    }
}
