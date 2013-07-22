package net.comcraft.src;

import java.util.Vector;
import net.comcraft.client.Comcraft;

public final class GuiSelectMod extends GuiScreenSlotHost {

    public GuiSelectMod(GuiScreen parentScreen) {
        super(parentScreen);
        guiSlot = new GuiSlotSelectMod(this);
    }

    protected void initGuiSlotCustom() {

        elementsList.addElement(new GuiButtonSmall(cc, 0, 5, Comcraft.screenHeight - 5 - GuiButtonSmall.getButtonHeight(), cc.langBundle.getText("GuiScreen.buttonSelect")).setEnabled(false));
        elementsList.addElement(new GuiButtonSmall(cc, 1, Comcraft.screenWidth - 5 - GuiButtonSmall.getButtonWidth(), Comcraft.screenHeight - 5 - GuiButtonSmall.getButtonHeight(), cc.langBundle.getText("GuiScreen.buttonClose")).setEnabled(parentScreen != null));

        elementClicked(0);
    }

    public void onScreenClosed() {
    }

    public void elementClicked(int id) {
        if (id >= getElementsList().size() || id < 0) {
            getButton(0).setEnabled(false);
            return;
        }

    }

    public Vector getElementsList() {
        return cc.modLoader.ListMods();
    }

    protected void customDrawScreen() {
        guiSlot.drawScreen();

        drawTitle("Mods");
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.id == 0) {
            backToParentScreen();
        } else if (guiButton.id == 1) {
            backToParentScreen();
        }
    }
}
