package net.comcraft.src;

import java.util.Vector;

public class GuiSelectServer extends GuiScreenSlotHost implements GuiYesNoHost {

    private String[] selectedServer;

    public GuiSelectServer(GuiScreen parentScreen) {
        super(parentScreen);
        guiSlot = new GuiSlotSelectServer(this);
    }

    protected void customDrawScreen() {
        guiSlot.drawScreen();
        drawTitle(cc.langBundle.getText("GuiSelectServer.title"));
    }

    private void selectServer(String[] serverinfo) {
        String ip = serverinfo[1];
        // Launch Server Game
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 0) {
            selectServer(selectedServer);
        } else if (guiButton.getId() == 1) {
            cc.displayGuiScreen(new GuiEditServer(this, null, null, true));
        } else if (guiButton.getId() == 2) {
            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectWorld.deleteWorld.confirmationText")));
        } else if (guiButton.getId() == 3) {
            backToParentScreen();
        } else if (guiButton.getId() == 4) {
            cc.displayGuiScreen(new GuiEditServer(this, selectedServer[0], selectedServer[1], false));
        }
    }

    protected void initGuiSlotCustom() {
        addButton(cc.langBundle.getText("GuiSelectServer.buttonConnect"), false, 2, 0);
        addButton(cc.langBundle.getText("GuiSelectServer.buttonAdd"), true, 1, 0);
        addButton(cc.langBundle.getText("GuiSelectWorld.buttonDelete"), false, 2, 1);
        addButton(cc.langBundle.getText("GuiSelectWorld.buttonClose"), parentScreen != null, 0, 1);
        addButton(cc.langBundle.getText("GuiSelectServer.buttonEdit"), false, 1, 1);
        elementClicked(0);

    }

    public void onScreenClosed() {
    }

    public void elementClicked(int id) {
        if (id >= getElementsList().size() || id < 0) {
            getButton(0).setEnabled(false);
            getButton(2).setEnabled(false);
            getButton(4).setEnabled(false);
            selectedServer = null;
            return;
        }

        selectedServer = (String[]) getElementsList().elementAt(id);

        getButton(0).setEnabled(true);
        getButton(2).setEnabled(true);
        getButton(4).setEnabled(true);
    }

    public Vector getElementsList() {
        return new Vector(0);
    }

    public void guiYesNoAction(boolean value) {
        if (value) {
            // Delete Server
            guiSlot.resetSlot();
            if (!getButton(2).enabled) {
                selectedButton = getNextAvailableButton();
            }
        }
    }

}
