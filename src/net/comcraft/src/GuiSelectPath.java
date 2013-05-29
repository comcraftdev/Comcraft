package net.comcraft.src;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class GuiSelectPath extends GuiScreenSlotHost implements GuiYesNoHost {

    private String selectedPath;
    private FileBrowser fileBrowser;
    private String memoryCardRoot;

    public GuiSelectPath(GuiScreen parentScreen) {
        super(parentScreen);
        guiSlot = new GuiSlotSelectPath(this);
        fileBrowser = new FileBrowser();
    }

    public void customDrawScreen() {
        guiSlot.drawScreen();

        cc.g.setColor(255, 255, 255);
        drawStringWithShadow(cc.g, FileSystemHelper.getPathWithoutPrefix(fileBrowser.getCurrentPath()), 5, 3, Graphics.TOP | Graphics.LEFT);
    }

    protected void initGuiSlotCustom() {
        if (!cc.settings.getComcraftFileSystem().isAvailable()) {
            fileBrowser.openDirectory("file:///");

            checkForMemoryCard();
        } else {
            fileBrowser.openDirectory(cc.settings.getComcraftFileSystem().getComcraftPath());
        }

        elementsList.addElement(new GuiButtonSmall(cc, 0, 5, guiSlot.getSlotEndPosY() + 5, cc.langBundle.getText("GuiSelectPath.buttonOpen")).setEnabled(false));
        elementsList.addElement(new GuiButtonSmall(cc, 2, 5, guiSlot.getSlotEndPosY() + 5 + GuiButtonSmall.getButtonHeight() + 5, cc.langBundle.getText("GuiSelectPath.buttonSetAsRoot")).setEnabled(fileBrowser.canWriteCurrentDirectory()));
        elementsList.addElement(new GuiButtonSmall(cc, 1, cc.screenWidth - 5 - GuiButtonSmall.getButtonWidth(), guiSlot.getSlotEndPosY() + 5, cc.langBundle.getText("GuiSelectPath.buttonBack")).setEnabled(!fileBrowser.getCurrentPath().equals("file:///")));
        elementsList.addElement(new GuiButtonSmall(cc, 3, cc.screenWidth - 5 - GuiButtonSmall.getButtonWidth(), guiSlot.getSlotEndPosY() + 5 + GuiButtonSmall.getButtonHeight() + 5, cc.langBundle.getText("GuiSelectPath.buttonClose")).setEnabled(parentScreen != null));

        elementClicked(0);
    }

    private void checkForMemoryCard() {
        Vector roots = getElementsList();

        if (roots.contains("file:///Mmc/")) {
            memoryCardRoot = "file:///Mmc/";

            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectPatch.useMMC.confirmationText")));
        } else if (roots.contains("file:///MemoryCard/")) {
            memoryCardRoot = "file:///MemoryCard/";

            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectPatch.useMMC.confirmationText")));
        } else if (roots.contains("file:///e/")) {
            memoryCardRoot = "file:///e/";

            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectPatch.useMMC.confirmationText")));
        } else if (roots.contains("file:///e:/")) {
            memoryCardRoot = "file:///e:/";

            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectPatch.useMMC.confirmationText")));
        } else if (roots.contains("file:///E/")) {
            memoryCardRoot = "file:///E/";

            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectPatch.useMMC.confirmationText")));
        } else if (roots.contains("file:///E:/")) {
            memoryCardRoot = "file:///E:/";

            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectPatch.useMMC.confirmationText")));
        } else if (roots.contains("file:///M:/")) {
            memoryCardRoot = "file:///M:/";

            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiSelectPatch.useMMC.confirmationText")));
        }
    }

    public void elementClicked(int id) {
        if (id >= getElementsList().size() || id < 0) {
            getButton(0).setEnabled(false);
            selectedPath = "";
            return;
        }

        String path = (String) getElementsList().elementAt(id);
        selectedPath = path;

        getButton(0).setEnabled(FileSystemHelper.isDirectory(selectedPath));
    }

    private void reset() {
        if (getElementsList().isEmpty() || Touch.isTouchSupported()) {
            selectedPath = "";
        } else {
            selectedPath = (String) getElementsList().firstElement();
        }

        guiSlot.setHandleInput(true);
        selectedButton = null;
        guiSlot.resetSlot();
    }

    private void checkCurrentFolder() {
        getButton(0).setEnabled(FileSystemHelper.isDirectory(selectedPath));
        getButton(1).setEnabled(!fileBrowser.getCurrentPath().equals("file:///"));
        getButton(2).setEnabled(fileBrowser.canWriteCurrentDirectory());
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 0) {
            fileBrowser.openDirectory(selectedPath);
            reset();
            checkCurrentFolder();
        } else if (guiButton.getId() == 1) {
            fileBrowser.openPreviousFolder();
            reset();
            checkCurrentFolder();
        } else if (guiButton.getId() == 2) {
            if (setAsRoot()) {
                backToParentScreen();
            }
        } else if (guiButton.getId() == 3) {
            backToParentScreen();
        }
    }

    private boolean setAsRoot() {
        ComcraftFileSystem comcraftFileSystem;

        try {
            comcraftFileSystem = new ComcraftFileSystem(cc, fileBrowser.getCurrentPath());
        } catch (SecurityException ex) {
            //#debug
//#             ex.printStackTrace();
            
            return false;
        }

        cc.settings.setComcraftFileSystem(comcraftFileSystem);
        return true;
    }

    public Vector getElementsList() {
        return fileBrowser.getCurrentElements();
    }

    public void onScreenClosed() {
    }

    public void guiYesNoAction(boolean value) {
        if (value) {
            if (memoryCardRoot == null) {
                return;
            }

            fileBrowser.openDirectory(memoryCardRoot);

            setAsRoot();

            cc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
