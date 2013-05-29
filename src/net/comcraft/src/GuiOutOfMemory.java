package net.comcraft.src;

import javax.microedition.lcdui.Graphics;

public class GuiOutOfMemory extends GuiScreen {

    private String errorClassName;
    private String errorMessage;

    public GuiOutOfMemory(OutOfMemoryError ex) {
        super(null);

        errorClassName = ex.getClass().getName();
        errorMessage = ex.getMessage();
    }

    public void customDrawScreen() {
        cc.g.setColor(255, 255, 255);
        drawStringInLines(cc.g, "Error: " + errorClassName, 10, 10, cc.screenWidth - 10 * 2, Graphics.TOP | Graphics.LEFT);

        cc.g.setColor(215, 215, 215);
        drawStringInLines(cc.g, "Error message: " + errorMessage, 15, 40 + cc.g.getFont().getHeight() * 3, cc.screenWidth - 15 * 2, Graphics.TOP | Graphics.LEFT);
    }

    protected void initGui() {
//        elementsList.addElement(new GuiButtonOnOff(cc, 0, (cc.screenWidth - GuiButton.getButtonWidth()) / 2, cc.screenHeight - (10 + GuiButton.getButtonHeight()) * 2, "Ignore OutOfMemory: ").setValue(cc.settings.ignoreOutOfMemory));
        elementsList.addElement(new GuiButton(cc, 0, (cc.screenWidth - GuiButton.getButtonWidth()) / 2, cc.screenHeight - 10 - GuiButton.getButtonHeight(), "Quit"));
    
        cc.settings.sounds = false;
    }

    public void onScreenClosed() {
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (guiButton.getId() == 1) {
            cc.settings.ignoreOutOfMemory = !cc.settings.ignoreOutOfMemory;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.ignoreOutOfMemory);
        } else if (guiButton.getId() == 0) {
            cc.shutdown();
        }
    }
}
