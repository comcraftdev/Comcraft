package net.comcraft.src;

import javax.microedition.lcdui.Graphics;

import net.comcraft.client.Comcraft;

public class GuiSlotSelectServer extends GuiSlot {

    public GuiSlotSelectServer(GuiScreenSlotHost guiScreenSlotHost) {
        super(guiScreenSlotHost);
    }

    protected void initSlot() {

    }

    protected void initSlotSize() {
        xPos = 0;
        slotScreenWidth = Comcraft.screenWidth;
        slotWidth = Comcraft.screenWidth - 10 * 2;

        yPos = 3 + cc.g.getFont().getHeight() + 3;
        slotScreenHeight = Comcraft.screenHeight - yPos - 5 * 4 - GuiButtonSmall.getButtonHeight() * 3;

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

    protected void drawSlot(int i) {
        int y = getSlotY(i);

        if (!isSlotVisibile(y)) {
            return;
        }

        if (selectedElement == i) {
            drawSelectedSlotFrame(y);
        }

        cc.g.setColor(255, 255, 255);

        String[] serverinfo = (String[]) guiScreenSlotHost.getElementsList().elementAt(i);

        int interval = (slotHeight - cc.g.getFont().getHeight() * 2) / 3;
        drawStringWithShadow(cc.g, serverinfo[0], (Comcraft.screenWidth - slotWidth) / 2 + 10, y + interval, Graphics.TOP | Graphics.LEFT);
        drawStringWithShadow(cc.g, serverinfo[1], (Comcraft.screenWidth - slotWidth) / 2 + 15, y + interval * 2 + cc.g.getFont().getHeight(), Graphics.TOP | Graphics.LEFT);
    }

}
