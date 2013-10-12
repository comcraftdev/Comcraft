package net.comcraft.src;

import javax.microedition.lcdui.Graphics;

import net.comcraft.client.Comcraft;

public class GuiYesNo extends GuiScreen {

    private String displayedString;
    private GuiYesNoHost guiYesNoHost;

    public GuiYesNo(GuiYesNoHost guiYesNoHost, String displayedString) {
        super((GuiScreen) guiYesNoHost);

        this.guiYesNoHost = guiYesNoHost;
        this.displayedString = displayedString;
    }

    protected void customDrawScreen() {
        cc.g.setColor(0xFFFFFF);
        drawStringWithShadow(cc.g, displayedString, Comcraft.screenWidth / 2, 20, Graphics.TOP | Graphics.HCENTER);
    }

    protected void initGui() {
        int centerX = (Comcraft.screenWidth - GuiButton.getButtonWidth()) / 2;

        int startY = 60;

        elementsList.addElement(new GuiButton(cc, 0, centerX, startY, cc.langBundle.getText("GuiYesNo.buttonYes")));
        elementsList.addElement(new GuiButton(cc, 1, centerX, startY + GuiButton.getButtonHeight() + 10, cc.langBundle.getText("GuiYesNo.buttonNo")));
        elementsList.addElement(new GuiButton(cc, 2, centerX, startY + (GuiButton.getButtonHeight() + 10) * 2, cc.langBundle.getText("GuiYesNo.buttonIDontKnow")).setEnabled(false));
    }

    public void onScreenClosed() {
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 0) {
            backToParentScreen();
            guiYesNoHost.guiYesNoAction(true);
        } else if (guiButton.getId() == 1) {
            backToParentScreen();
            guiYesNoHost.guiYesNoAction(false);
        }
    }
}
