/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

/**
 *
 * @author Piotr Wójcik
 */
public class GuiOptionsMore extends GuiScreen {

    public GuiOptionsMore(GuiOptions parentScreen) {
        super(parentScreen);
    }

    protected void customDrawScreen() {
    }

    protected void initGui() {
        int centerX = (cc.screenWidth - GuiButton.getButtonWidth()) / 2;
        int startY = 5;

        elementsList.addElement(new GuiButtonSelect(cc, 0, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 0, cc.langBundle.getText("GuiOptions.buttonFOV"), new int[]{65, 90, 120, 160}, new String[]{"65°", cc.langBundle.getText("GuiOptions.buttonFOV.normal"), "120°", "160°"}).setCurrentValue((int) cc.settings.fov));
        elementsList.addElement(new GuiButtonOnOff(cc, 1, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 1, cc.langBundle.getText("GuiOptions.buttonFog")).setValue(cc.settings.fog));
        elementsList.addElement(new GuiButtonOnOff(cc, 2, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 2, cc.langBundle.getText("GuiOptions.buttonMemorySaving")).setValue(cc.settings.memorySaveMode));
        elementsList.addElement(new GuiButtonOnOff(cc, 3, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 3, cc.langBundle.getText("GuiOptions.buttonShowGui")).setValue(cc.settings.renderGui));
        elementsList.addElement(new GuiButtonOnOff(cc, 4, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 4, cc.langBundle.getText("GuiOptions.buttonDebugInfo")).setValue(cc.settings.debugInfo));
        elementsList.addElement(new GuiButtonOnOff(cc, 5, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 5, cc.langBundle.getText("GuiOptions.buttonIgnoreOOM")).setValue(cc.settings.ignoreOutOfMemory));

        //        elementsList.addElement(new GuiButtonOnOff(cc, 8, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 7, cc.langBundle.getText("GuiOptions.buttonScreenshot")).setValue(cc.settings.screenshotMode));
        elementsList.addElement(new GuiButtonSelect(cc, 9, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 6, cc.langBundle.getText("GuiOptions.buttonScreenshotResolution"), new int[]{1, 2, 4}, new String[]{cc.langBundle.getText("GuiOptions.buttonScreenshotResolution.low"), cc.langBundle.getText("GuiOptions.buttonScreenshotResolution.normal"), cc.langBundle.getText("GuiOptions.buttonScreenshotResolution.high")}).setCurrentValue(cc.settings.screenshotResolution));

        elementsList.addElement(new GuiButton(cc, 7, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 8, cc.langBundle.getText("GuiOptions.buttonBack")).setEnabled(parentScreen != null));
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 7) {
            backToParentScreen();
        } else if (guiButton.getId() == 4) {
            cc.settings.debugInfo = !cc.settings.debugInfo;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.debugInfo);
        } else if (guiButton.getId() == 3) {
            cc.settings.renderGui = !cc.settings.renderGui;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.renderGui);
        } else if (guiButton.getId() == 2) {
            cc.settings.memorySaveMode = !cc.settings.memorySaveMode;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.memorySaveMode);
        } else if (guiButton.getId() == 5) {
            cc.settings.ignoreOutOfMemory = !cc.settings.ignoreOutOfMemory;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.ignoreOutOfMemory);
        } else if (guiButton.getId() == 1) {
            cc.settings.fog = !cc.settings.fog;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.fog);
            cc.render.renderBlock.reloadRenderBlock();
        } else if (guiButton.getId() == 0) {
            GuiButtonSelect guiButtonSelect = getButtonSelect(guiButton.getId());
            guiButtonSelect.nextValue();
            cc.settings.fov = guiButtonSelect.getCurrentValue();
            cc.render.reloadFrustum();
            cc.render.reloadCamera();
        } else if (guiButton.getId() == 8) {
            cc.settings.screenshotMode = !cc.settings.screenshotMode;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.screenshotMode);
        } else if (guiButton.getId() == 9) {
            GuiButtonSelect guiButtonSelect = getButtonSelect(guiButton.getId());
            guiButtonSelect.nextValue();
            cc.settings.screenshotResolution = guiButtonSelect.getCurrentValue();
        }
    }
}