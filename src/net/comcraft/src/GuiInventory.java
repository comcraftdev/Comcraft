package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import net.comcraft.client.Comcraft;

public class GuiInventory extends GuiScreen implements GuiYesNoHost {

    private GuiContainerInventory guiContainer;
    private Image inventoryImage;
    private int xInv;
    private int yInv;

    public GuiInventory() {
        super(null);
    }

    protected void drawDefaultBackground() {
    }

    protected void customDrawScreen() {
        cc.g.drawImage(inventoryImage, xInv, yInv, Graphics.TOP | Graphics.LEFT);

        guiContainer.drawContainer();
        
        if (!Touch.isTouchSupported()) {
            cc.g.setColor(0xFFFFFF);
            drawStringWithShadow(cc.g, cc.langBundle.getText("Ingame.quickMenuInfo"), 3, Comcraft.screenHeight - 3, Graphics.LEFT | Graphics.BASELINE);
        }
    }

    protected void initGui() {
        inventoryImage = cc.textureProvider.getImage("gui/inventory_" + (Touch.isTouchSupported() ? "landscape.png" : "normal.png"));

        if (Touch.isTouchSupported()) {
            xInv = (Comcraft.screenWidth - 3 - GuiButtonMoveControl.getButtonWidth() - 3) / 2 - inventoryImage.getWidth() / 2;
            yInv = (Comcraft.screenHeight) / 2 - inventoryImage.getHeight() / 2;

            elementsList.addElement(new GuiButtonPictured(cc, this, 0, Comcraft.screenWidth - GuiButtonPictured.getButtonWidth() - 3, Comcraft.screenHeight - GuiButtonPictured.getButtonHeight() - 3, "gui/button_exit.png", Sprite.TRANS_ROT90));
            elementsList.addElement(new GuiButtonPictured(cc, this, 1, Comcraft.screenWidth - GuiButtonPictured.getButtonWidth() - 3, 3, "gui/button_quick_menu.png", Sprite.TRANS_ROT90));
        } else {
            xInv = (Comcraft.screenWidth) / 2 - inventoryImage.getWidth() / 2;
            yInv = (Comcraft.screenHeight - 3 - GuiButtonMoveControl.getButtonHeight() - 3) / 2 - inventoryImage.getHeight() / 2 + 3 + GuiButtonMoveControl.getButtonHeight() + 3;
        }

        guiContainer = new GuiContainerInventory(cc, this, xInv + 5, yInv + 5);
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 0) {
            touchMovedOrUp(Touch.getX(), Touch.getY());
            cc.endWorld();
        } else if (guiButton.getId() == 1) {
            touchMovedOrUp(Touch.getX(), Touch.getY());
            cc.displayGuiScreen(new GuiQuickMenu());
        }
    }

    protected void handleTouchInput() {
        if (!Touch.isInputHandled()) {
            super.handleTouchInput();
        }
    }

    protected void handleKeyboardInput() {
    }

    public void customHandleInput() {
        guiContainer.handleInput();
    }

    public void clickedItemStack(InvItemStack itemStack) {
        cc.player.inventory.setItemStackAt(cc.player.inventory.getSelectedElementNum(), itemStack);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void guiYesNoAction(boolean value) {
        if (value) {
            cc.endWorld();
        }
    }
}
