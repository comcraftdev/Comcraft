package net.comcraft.src;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.comcraft.client.Comcraft;

public abstract class GuiScreen extends GuiElement {

    protected Comcraft cc;
    protected Vector elementsList;
    protected GuiButton selectedButton;
    protected GuiScreen parentScreen;
    private boolean isInitialized;

    public GuiScreen(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        elementsList = new Vector(5);
    }

    protected abstract void customDrawScreen();

    public boolean getSkipRender() {
        return true;
    }

    protected void preScreenRender() {
    }

    protected void postScreenRender() {
    }

    public final void drawScreen() {
        preScreenRender();

        drawDefaultBackground();

        customDrawScreen();

        for (int i = 0; i < elementsList.size(); ++i) {
            GuiButton guiButton = (GuiButton) elementsList.elementAt(i);
            guiButton.drawButton(selectedButton);
        }

        postScreenRender();
    }

    protected abstract void initGui();

    public void initGuiScreen(Comcraft cc) {
        this.cc = cc;
        elementsList.removeAllElements();

        initGui();
        initTouchOrKeyboard();

        isInitialized = true;
    }

    protected void initTouchOrKeyboard() {
        if (Touch.isTouchSupported()) {
            selectedButton = null;
        } else {
            GuiButton guiButton = getFirstAvailableButton();

            if (guiButton != null) {
                selectedButton = guiButton;
            } else {
                selectedButton = null;
            }
        }
    }

    public void onScreenDisplay() {
    }

    public void onScreenClosed() {
    }

    protected void drawDefaultBackground() {
        drawBackground(false);
    }

    protected void drawDarkBackground() {
        drawBackground(true);
    }

    private void drawBackground(boolean dark) {
        Image backgroundImage = cc.textureProvider.getImage("gui/background.png");
        Image blackImage = null;

        if (dark) {
            blackImage = cc.textureProvider.getImage("gui/black.png");
        }

        int rows = Comcraft.screenWidth / backgroundImage.getWidth() + 1;
        int cols = Comcraft.screenHeight / backgroundImage.getHeight() + 1;

        for (int y = 0; y < cols; ++y) {
            for (int x = 0; x < rows; ++x) {
                cc.g.drawImage(backgroundImage, x * backgroundImage.getWidth(), y * backgroundImage.getHeight(), Graphics.TOP | Graphics.LEFT);

                if (dark) {
                    cc.g.drawImage(blackImage, x * blackImage.getWidth(), y * blackImage.getHeight(), Graphics.TOP | Graphics.LEFT);
                }
            }
        }
    }

    protected void touchClicked(int x, int y) {
        for (int i = 0; i < elementsList.size(); ++i) {
            GuiButton guiButton = (GuiButton) elementsList.elementAt(i);

            if (guiButton.checkPoint(x, y)) {
                selectedButton = guiButton;
                handleGuiAction(guiButton);
            }
        }
    }

    protected void touchMovedOrUp(int x, int y) {
        if (selectedButton != null && Touch.isTouchSupported()) {
            selectedButton = null;
        }
    }

    protected abstract void handleGuiAction(GuiButton guiButton);

    protected void handleKeyboardInput() {
        if (Keyboard.wasButtonDown(Keyboard.KEY_DOWN) || Keyboard.wasButtonDown(Keyboard.KEY_S)) {
            selectedButton = getNextAvailableButton();
        }
        if (Keyboard.wasButtonDown(Keyboard.KEY_UP) || Keyboard.wasButtonDown(Keyboard.KEY_W)) {
            selectedButton = getPreviousAvailableButton();
        }
        if (Keyboard.wasButtonDown(Keyboard.KEY_FIRE) || Keyboard.wasButtonDown(Keyboard.KEY_SOFT_LEFT) || Keyboard.wasButtonDown(Keyboard.KEY_SOFT_RIGHT)) {
            if (selectedButton != null && selectedButton.enabled) {
                handleGuiAction(selectedButton);
            }
        }
    }

    protected void handleTouchInput() {
        if (!Touch.isPressed() && Touch.wasPressed()) {
            touchClicked(Touch.getX(), Touch.getY());
        } else {
            touchMovedOrUp(Touch.getX(), Touch.getY());
        }
    }

    protected void customHandleInput() {
    }

    public void handleInput() {
        customHandleInput();
        handleTouchInput();
        handleKeyboardInput();
    }

    protected final GuiButton getButton(int id) {
        for (int i = 0; i < elementsList.size(); ++i) {
            GuiButton guiButton = (GuiButton) elementsList.elementAt(i);

            if (guiButton.getId() == id) {
                return guiButton;
            }
        }

        return null;
    }

    protected GuiButtonOnOff getButtonOnOff(int id) {
        return (GuiButtonOnOff) getButton(id);
    }

    protected GuiButtonSelect getButtonSelect(int id) {
        return (GuiButtonSelect) getButton(id);
    }

    protected final GuiButton getNextAvailableButton() {
        if (selectedButton == null) {
            return getLastAvailableButton();
        }

        for (int i = elementsList.indexOf(selectedButton) + 1; i < elementsList.size(); ++i) {
            GuiButton guiButton = (GuiButton) elementsList.elementAt(i);

            if (guiButton.enabled) {
                return guiButton;
            }
        }

        return getFirstAvailableButton();
    }

    protected final GuiButton getPreviousAvailableButton() {
        if (selectedButton == null) {
            return getFirstAvailableButton();
        }

        for (int i = elementsList.indexOf(selectedButton) - 1; i >= 0; --i) {
            GuiButton guiButton = (GuiButton) elementsList.elementAt(i);

            if (guiButton.enabled) {
                return guiButton;
            }
        }

        return getLastAvailableButton();
    }

    protected final GuiButton getFirstAvailableButton() {
        for (int i = 0; i < elementsList.size(); ++i) {
            GuiButton guiButton = (GuiButton) elementsList.elementAt(i);

            if (guiButton.enabled) {
                return guiButton;
            }
        }

        return null;
    }

    protected final GuiButton getLastAvailableButton() {
        for (int i = elementsList.size() - 1; i >= 0; --i) {
            GuiButton guiButton = (GuiButton) elementsList.elementAt(i);

            if (guiButton.enabled) {
                return guiButton;
            }
        }

        return null;
    }

    protected void backToParentScreen() {
        if (parentScreen == null) {
            parentScreen = new GuiMainMenu();
        }

        cc.displayGuiScreen(parentScreen);
    }

    protected void drawTitle(String title) {
        cc.g.setColor(255, 255, 255);
        drawStringWithShadow(cc.g, title, Comcraft.screenWidth / 2, 3, Graphics.TOP | Graphics.HCENTER);
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    public final boolean isScreenInitialized() {
        return isInitialized;
    }
}
