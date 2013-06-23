package net.comcraft.src;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.TextField;

import net.comcraft.client.Comcraft;

public class GuiSelectWorld extends GuiScreenSlotHost implements GuiYesNoHost, GuiTextBoxHost {

    private WorldSaveType selectedWorld;
    public int maxWorlds;

    public GuiSelectWorld(GuiScreen parentScreen) {
        super(parentScreen);
        guiSlot = new GuiSlotSelectWorld(this);
    }

    protected void initGuiSlotCustom() {
        maxWorlds = 666;
        
        cc.worldLoader.updateAvailableWorldList();

        elementsList.addElement(new GuiButtonSmall(cc, 0, 5, guiSlot.getSlotEndPosY() + 5, cc.langBundle.getText("GuiSelectWorld.buttonPlay")).setEnabled(false));
        elementsList.addElement(new GuiButtonSmall(cc, 1, 5, guiSlot.getSlotEndPosY() + 5 + GuiButtonSmall.getButtonHeight() + 5, cc.langBundle.getText("GuiSelectWorld.buttonNew")).setEnabled(getElementsList().size() < maxWorlds));
        elementsList.addElement(new GuiButtonSmall(cc, 2, Comcraft.screenWidth - 5 - GuiButtonSmall.getButtonWidth(), guiSlot.getSlotEndPosY() + 5, cc.langBundle.getText("GuiSelectWorld.buttonDelete")).setEnabled(false));
        elementsList.addElement(new GuiButtonSmall(cc, 4, Comcraft.screenWidth - 5 - GuiButtonSmall.getButtonWidth(), guiSlot.getSlotEndPosY() + 5 + GuiButtonSmall.getButtonHeight() + 5, cc.langBundle.getText("GuiSelectWorld.buttonRename")).setEnabled(false));
        elementsList.addElement(new GuiButtonSmall(cc, 3, Comcraft.screenWidth - 5 - GuiButtonSmall.getButtonWidth(), guiSlot.getSlotEndPosY() + 5 + (GuiButtonSmall.getButtonHeight() + 5) * 2, cc.langBundle.getText("GuiSelectWorld.buttonClose")).setEnabled(parentScreen != null));

        elementClicked(0);
    }

    public void onScreenClosed() {
    }

    public void elementClicked(int id) {
        if (id >= getElementsList().size() || id < 0) {
            getButton(0).setEnabled(false);
            getButton(2).setEnabled(false);
            getButton(4).setEnabled(false);
            selectedWorld = null;
            return;
        }

        selectedWorld = (WorldSaveType) getElementsList().elementAt(id);

        getButton(0).setEnabled(true);
        getButton(2).setEnabled(true);
        getButton(4).setEnabled(true);
    }

    public Vector getElementsList() {
        Vector worldList = cc.worldLoader.getWorldList();
        
        if (worldList.size() > maxWorlds) {
            worldList.setSize(maxWorlds);
        }
        
        return worldList;
    }

    protected void customDrawScreen() {
        guiSlot.drawScreen();

        drawTitle(cc.langBundle.getText("GuiSelectWorld.title"));
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 0) {
            selectWorld(selectedWorld);
        } else if (guiButton.getId() == 1) {
            cc.displayGuiScreen(new GuiNewWorld(this, parentScreen));
            guiButton.setEnabled(getElementsList().size() < maxWorlds);
        } else if (guiButton.getId() == 2) {
            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectWorld.deleteWorld.confirmationText")));
        } else if (guiButton.getId() == 3) {
            backToParentScreen();
        } else if (guiButton.getId() == 4) {
            cc.displayGuiScreen(new GuiTextBox(this, selectedWorld.getWorldName(), TextField.ANY, 64));
        }
    }

    private void renameWorld(WorldSaveType worldInfo, String newName) {
        if (newName == null || newName.equals("")) {
            return;
        }

        try {
            FileConnection fileConnection = (FileConnection) Connector.open(worldInfo.getWorldPath(), Connector.READ_WRITE);

            fileConnection.rename(newName);

            fileConnection.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
        }
    }

    private void selectWorld(WorldSaveType world) {
        cc.displayGuiScreen(null);

        cc.playerManager = new PlayerManagerImp(cc);

        cc.startWorld(world);
    }

    public void guiYesNoAction(boolean value) {
        if (value) {
            cc.worldLoader.deleteWorld(selectedWorld);
            cc.worldLoader.updateAvailableWorldList();
            guiSlot.resetSlot();

            if (!getButton(2).enabled) {
                selectedButton = getNextAvailableButton();
            }

            getButton(1).setEnabled(getElementsList().size() < maxWorlds);
        }
    }

    public void guiTextBoxAction(String textBoxString) {
        renameWorld(selectedWorld, textBoxString);

        cc.worldLoader.updateAvailableWorldList();
    }
}
