package net.comcraft.src;

public class GuiInGameTextBox extends GuiTextBox {

    public GuiInGameTextBox(GuiTextBoxHost parent, String string, int textFieldType, int maxLength) {
        super(parent, string, textFieldType, maxLength);
    }

    public void drawDefaultBackground() {
    }

    protected void backToParentScreen() {
        cc.isGamePaused = false;
        cc.displayGuiScreen(null);
    }
}
