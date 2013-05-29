
package net.comcraft.src;

public class GuiLoadingScreen extends GuiScreen {

    public GuiLoadingScreen() {
        super(null);
    }
    
    protected void customDrawScreen() {
    }

    protected void initGui() {
        elementsList.addElement(new GuiButtonSmall(cc, 0, 3, cc.screenHeight - 3 - GuiButtonSmall.getButtonHeight(), "loading..."));
    }

    public void onScreenClosed() {
        Touch.resetTouch();
        Keyboard.resetKeyboard();
    }

    protected void handleGuiAction(GuiButton guiButton) {
    }
}
