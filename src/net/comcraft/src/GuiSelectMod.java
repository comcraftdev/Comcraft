package net.comcraft.src;

import java.util.Vector;

public final class GuiSelectMod extends GuiScreenSlotHost {
    private Mod selectedmod;

    public GuiSelectMod(GuiScreen parentScreen) {
        super(parentScreen);
        guiSlot = new GuiSlotSelectMod(this);
    }

    protected void initGuiSlotCustom() {
        addButton(cc.langBundle.getText("GuiSelectMod.seeInfo"), false, 0, 0);
        addButton(cc.langBundle.getText("GuiOptions.buttonBack"), parentScreen != null, 0, 1);
        addButton(cc.langBundle.getText("GuiSelectMod.enable"), false, 1, 0);
        addButton(cc.langBundle.getText("GuiSelectMod.disable"), false, 1, 1);
        elementClicked(0);
    }

    public void onScreenClosed() {
    }

    public void elementClicked(int id) {
        getButton(2).setEnabled(false);
        getButton(3).setEnabled(false);
        if (id >= getElementsList().size() || id < 0) {
            getButton(0).setEnabled(false);
            return;
        }
        getButton(0).setEnabled(true);
        Mod mod = (Mod) getElementsList().elementAt(id);
        selectedmod = mod;
        if (mod.enabled) {
            getButton(3).setEnabled(true);
        } else {
            getButton(2).setEnabled(true);

        }
    }

    public Vector getElementsList() {
        return cc.modLoader.ListMods();
    }

    protected void customDrawScreen() {
        guiSlot.drawScreen();
        drawTitle(cc.langBundle.getText("GuiMainMenu.buttonMods"));
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }
        if (guiButton.id == 0) {
            // Info
            String text = selectedmod.getModInfo();
            cc.displayGuiScreen(new GuiModInfo(this, text));
        } else if (guiButton.id == 1) {
            // Back
            backToParentScreen();
        } else if (guiButton.id == 2) {
            // Enable
            selectedmod.enable();
            getButton(2).setEnabled(false);
            getButton(3).setEnabled(true);
        } else if (guiButton.id == 3) {
            // Disable
            selectedmod.disable();
            getButton(3).setEnabled(false);
            getButton(2).setEnabled(true);
        }
    }
}
