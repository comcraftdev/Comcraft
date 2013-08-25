package net.comcraft.src;

import java.util.Vector;

public final class GuiSelectMod extends GuiScreenSlotHost {
    private Mod selectedmod;
    public GuiSelectMod(GuiScreen parentScreen) {
        super(parentScreen);
        guiSlot = new GuiSlotSelectMod(this);
    }

    protected void initGuiSlotCustom() {
        addButton("See Info", false, 0, 0);
        addButton(cc.langBundle.getText("GuiOptions.buttonBack"),parentScreen!=null, 0, 1);
        addButton("Enable",false, 1, 0);
        addButton("Disable", false, 1, 1);
        elementClicked(0);
    }

    public void onScreenClosed() {
    }

    public void elementClicked(int id) {
        if (id >= getElementsList().size() || id < 0) {
            getButton(0).setEnabled(false);
            getButton(2).setEnabled(false);
            getButton(3).setEnabled(false);
            return;
        }
        getButton(0).setEnabled(true);
        Mod mod = (Mod) getElementsList().elementAt(id);
        selectedmod=mod;
        if (mod.isRunning()) {
            if (mod.enabled) {
                getButton(3).setEnabled(true);
            }
            else {
                getButton(2).setEnabled(true);
            }
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
            //Info
            String text = selectedmod.getModInfo();
            cc.displayGuiScreen(new GuiModInfo(this, text));
        } else if (guiButton.id == 1) {
            //Back
            backToParentScreen();
        } else if (guiButton.id == 2) {
            //Enable
            selectedmod.enabled=true;
            getButton(2).setEnabled(false);
            getButton(3).setEnabled(true);
        } else if (guiButton.id == 3) {
            //Disable
            selectedmod.enabled=false;
            getButton(3).setEnabled(false);
            getButton(2).setEnabled(true);
        }
    }
}
