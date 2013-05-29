package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import net.comcraft.client.Comcraft;

public class GuiSlotSelectPath extends GuiSlot {

    private Image fileImage = null;
    private Image folderImage = null;

    public GuiSlotSelectPath(GuiSelectPath guiSelectPath) {
        super(guiSelectPath);
    }

    protected void initSlotSize() {
        xPos = 0;
        slotScreenWidth = cc.screenWidth;
        slotWidth = cc.screenWidth - 10 * 2;

        yPos = 3 + cc.g.getFont().getHeight() + 3;
        slotScreenHeight = cc.screenHeight - yPos - 5 * 3 - GuiButtonSmall.getButtonHeight() * 2;

        if (Comcraft.getScreenHeight() == 320) {
            slotHeight = 30;
        } else if (Comcraft.getScreenHeight() == 240) {
            slotHeight = 30;
        } else if (Comcraft.getScreenHeight() == 220) {
            slotHeight = 30;
        } else if (Comcraft.getScreenHeight() == 400) {
            slotHeight = 30;
        } else if (Comcraft.getScreenHeight() == 480) {
            slotHeight = 40;
        } else if (Comcraft.getScreenHeight() == 640) {
            slotHeight = 60;
        } else if (Comcraft.getScreenHeight() == 800) {
            slotHeight = 60;
        }
    }

    private void initFileAndFolderImage() {
        Image baseImage = cc.textureProvider.getImage("gui/file_icon.png");

        int size = baseImage.getHeight();

        fileImage = Image.createImage(baseImage, 0, 0, size, size, Sprite.TRANS_NONE);
        folderImage = Image.createImage(baseImage, size, 0, size, size, Sprite.TRANS_NONE);
    }

    protected void initSlot() {
        initFileAndFolderImage();
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

        String slotString = (String) guiScreenSlotHost.getElementsList().elementAt(i);

        if (FileSystemHelper.isDirectory(slotString)) {
            drawSlotTypeImage(folderImage, y);
        } else {
            drawSlotTypeImage(fileImage, y);
        }

        drawStringWithShadow(cc.g, FileSystemHelper.getLastPathName(slotString), (cc.screenWidth - slotWidth) / 2 + folderImage.getWidth() + 10, y + slotHeight / 2 - cc.g.getFont().getHeight() / 2, Graphics.TOP | Graphics.LEFT);
    }

    private void drawSlotTypeImage(Image slotImage, int y) {
        cc.g.drawImage(slotImage, (cc.screenWidth - slotWidth) / 2 + 5, y + (slotHeight - slotImage.getHeight()) / 2, Graphics.TOP | Graphics.LEFT);
    }
}
