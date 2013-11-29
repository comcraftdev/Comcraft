package net.comcraft.src;

import javax.microedition.lcdui.Graphics;

import net.comcraft.client.Comcraft;

public class GuiServerMessage extends GuiScreen {

    private String text;
    private ServerGame sg;
    private boolean discon;

    public GuiServerMessage(GuiScreen parentScreen, ServerGame sg) {
        super(parentScreen);
        this.sg = sg;
        discon = false;
    }

    public void setText(String msg) {
        text = msg;
    }

    protected void customDrawScreen() {
        cc.g.setColor(255, 255, 255);
        drawStringInLines(cc.g, text, 10, (Comcraft.screenHeight / 2) - GuiButtonSmall.getButtonHeight() - 15, Comcraft.screenWidth - 10 * 2, Graphics.TOP | Graphics.LEFT);
    }

    protected void initGui() {
        elementsList.addElement(new GuiButtonSmall(cc, 0, (Comcraft.screenWidth / 2) - (GuiButtonSmall.getButtonWidth() / 2), (Comcraft.screenHeight / 2), "Back"));
        buttonSetCancel();
    }

    public void buttonSetCancel() {
        ((GuiButtonSmall) elementsList.elementAt(0)).displayString = cc.langBundle.getText("GuiServerMessage.cancel");
        discon = true;
    }

    public void buttonSetBack() {
        ((GuiButtonSmall) elementsList.elementAt(0)).displayString = cc.langBundle.getText("GuiServerMessage.back");
        discon = false;
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (discon) {
            sg.disconnect();
        }
        backToParentScreen();
    }
}
