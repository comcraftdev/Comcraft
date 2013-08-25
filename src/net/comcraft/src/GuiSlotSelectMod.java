package net.comcraft.src;

import javax.microedition.lcdui.Graphics;

import net.comcraft.client.Comcraft;

public class GuiSlotSelectMod extends GuiSlot {

    public GuiSlotSelectMod(GuiSelectMod guiScreenSlotHost) {
        super(guiScreenSlotHost);
    }

    protected void initSlotSize() {
        xPos = 0;
        slotScreenWidth = Comcraft.screenWidth;
        slotWidth = Comcraft.screenWidth - 10 * 2;

        yPos = 3 + cc.g.getFont().getHeight() + 3;
        slotScreenHeight = Comcraft.screenHeight - yPos - 5 * 4 - GuiButtonSmall.getButtonHeight() * 2;

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

        Mod mod = (Mod) guiScreenSlotHost.getElementsList().elementAt(i);

        if (mod == null) {
            return;
        }
        int interval = (slotHeight - cc.g.getFont().getHeight() * 2) / 3;
        if (mod.isRunning()) {
            cc.g.setColor(255,255,255);
            drawStringWithShadow(cc.g, mod.getModName(), (Comcraft.screenWidth - slotWidth) / 2 + 10, y + interval, Graphics.TOP | Graphics.LEFT);
            cc.g.setColor(200,200,200);
            drawStringWithShadow(cc.g, mod.getModDescription(), (Comcraft.screenWidth - slotWidth) / 2 + 15, y + interval*2+ cc.g.getFont().getHeight(), Graphics.TOP | Graphics.LEFT);
        }
        else {
            cc.g.setColor(110, 110, 110);
            drawStringWithShadow(cc.g, mod.getModName(), (Comcraft.screenWidth - slotWidth) / 2 + 10, y + interval, Graphics.TOP | Graphics.LEFT);
            cc.g.setColor(0x8B0000);
            drawStringWithShadow(cc.g, "Errors were encountered", (Comcraft.screenWidth - slotWidth) / 2 + 15, y + interval*2+ cc.g.getFont().getHeight(), Graphics.TOP | Graphics.LEFT);
        }
    }

}