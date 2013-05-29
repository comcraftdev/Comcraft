package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import net.comcraft.client.Comcraft;

public class GuiQuickMenu extends GuiScreen {

    private boolean isLandscape;
    private Image tempImage;
    private Graphics tempGraphics;
    private Graphics ccGraphics;

    public GuiQuickMenu() {
        super(null);
    }

    public void onScreenDisplay() {
        Touch.setInvesion(true);

        checkLoadAllChunksButton();
    }

    public void onScreenClosed() {
        Touch.setInvesion(false);
    }

    protected void drawDefaultBackground() {
        cc.g.setColor(0xB0E0E6);
        cc.g.fillRect(0, 0, Comcraft.screenHeight, Comcraft.screenHeight);
    }

    protected void customDrawScreen() {
    }

    protected void preScreenRender() {
        if (!isLandscape) {
            return;
        }

        ccGraphics = cc.g;
        cc.g = tempGraphics;
    }

    protected void postScreenRender() {
        if (!isLandscape) {
            return;
        }

        cc.g = ccGraphics;

        Image rotImage = Image.createImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight(), Sprite.TRANS_ROT90);
        cc.g.drawImage(rotImage, 0, 0, Graphics.TOP | Graphics.LEFT);
    }

    protected void initGui() {
        isLandscape = Touch.isTouchSupported();

        if (isLandscape) {
            tempImage = Image.createImage(Comcraft.getScreenHeight(), Comcraft.getScreenWidth());
            tempGraphics = tempImage.getGraphics();
        }

        int startY = 5;
        int centerX;

        if (isLandscape) {
            centerX = (Comcraft.getScreenHeight() - GuiButton.getButtonWidth()) / 2;
        } else {
            centerX = (Comcraft.getScreenWidth() - GuiButton.getButtonWidth()) / 2;
        }

        elementsList.addElement(new GuiButton(cc, 0, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 0, cc.langBundle.getText("GuiQuickMenu.resume")));

        elementsList.addElement(new GuiButtonOnOff(cc, 3, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 1, cc.langBundle.getText("GuiOptions.buttonSound")).setValue(cc.settings.sounds));
        elementsList.addElement(new GuiButtonOnOff(cc, 4, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 2, cc.langBundle.getText("GuiOptions.buttonVibrations")).setValue(cc.settings.vibrations));

        elementsList.addElement(new GuiButtonOnOff(cc, 2, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 3, cc.langBundle.getText("GuiQuickMenu.screenshotMode")).setValue(cc.settings.screenshotMode));
        elementsList.addElement(new GuiButton(cc, 5, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 4, cc.langBundle.getText("GuiQuickMenu.loadWholeWorld")));

        elementsList.addElement(new GuiButton(cc, 1, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 5, cc.langBundle.getText("GuiQuickMenu.mainMenu")));
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 0) {
            cc.displayGuiScreen(null);
        } else if (guiButton.getId() == 1) {
            cc.endWorld();
        } else if (guiButton.getId() == 2) {
            cc.settings.screenshotMode = !cc.settings.screenshotMode;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.screenshotMode);
            cc.guiIngame.updateScreenshotButton();
        } else if (guiButton.getId() == 3) {
            cc.settings.sounds = !cc.settings.sounds;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.sounds);
        } else if (guiButton.getId() == 4) {
            cc.settings.vibrations = !cc.settings.vibrations;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.vibrations);
        } else if (guiButton.getId() == 5) {
            cc.world.loadAllChunks();
            checkLoadAllChunksButton();
        }
    }

    private void checkLoadAllChunksButton() {
        if (cc.world.chunkProvider.getLoadedChunksNum() + cc.world.chunkProvider.getChunksQueueNum() < cc.world.worldSize * cc.world.worldSize) {
            getButton(5).setEnabled(true);
        } else {
            getButton(5).setEnabled(false);
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void handleKeyboardInput() {
        if (Keyboard.wasButtonDown(Keyboard.KEY_POUND) || Keyboard.wasButtonDown(Keyboard.KEY_Y)) {
            cc.displayGuiScreen(null);
        }
        
        super.handleKeyboardInput();
    }
}
