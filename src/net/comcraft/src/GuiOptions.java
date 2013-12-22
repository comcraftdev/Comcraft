package net.comcraft.src;

import javax.microedition.m3g.Graphics3D;

import net.comcraft.client.Comcraft;

public class GuiOptions extends GuiScreen {

    public GuiOptions(GuiScreen parentScreen) {
        super(parentScreen);
    }

    public void customDrawScreen() {
    }

    protected void initGui() {
        int centerX = (Comcraft.screenWidth - GuiButton.getButtonWidth()) / 2;
        int startY = 5;

        String distShort = cc.langBundle.getText("GuiOptions.buttonRenderDistance.short");
        String distNormal = cc.langBundle.getText("GuiOptions.buttonRenderDistance.normal");
        String distFar = cc.langBundle.getText("GuiOptions.buttonRenderDistance.far");
        String distFar2 = cc.langBundle.getText("GuiOptions.buttonRenderDistance.farx2");
        String distFar3 = cc.langBundle.getText("GuiOptions.buttonRenderDistance.farx3");
        String distFar4 = cc.langBundle.getText("GuiOptions.buttonRenderDistance.farx4");

        Boolean antialiasingB = (Boolean) (Graphics3D.getProperties().get("supportAntialiasing"));

        elementsList.addElement(new GuiButtonOnOff(cc, 0, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 0, cc.langBundle.getText("GuiOptions.buttonSound")).setValue(cc.settings.sounds));
        elementsList.addElement(new GuiButtonOnOff(cc, 1, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 1, cc.langBundle.getText("GuiOptions.buttonVibrations")).setValue(cc.settings.vibrations));
        elementsList.addElement(new GuiButtonSelect(cc, 2, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 2, cc.langBundle.getText("GuiOptions.buttonRenderDistance"), new int[]{0, 1, 2, 5, 7, 9}, new String[]{distShort, distNormal, distFar, distFar2, distFar3, distFar4}).setCurrentValue(cc.settings.renderDistance));
        elementsList.addElement(new GuiButtonOnOff(cc, 3, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 3, cc.langBundle.getText("GuiOptions.buttonAntialiasing")).setValue(cc.settings.antialiasing).setEnabled(antialiasingB.booleanValue()));
        elementsList.addElement(new GuiButtonOnOff(cc, 4, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 4, cc.langBundle.getText("GuiOptions.buttonShowFPS")).setValue(cc.settings.showFps));
        elementsList.addElement(new GuiButton(cc, 5, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 5, cc.langBundle.getText("GuiOptions.buttonChangeRoot")));
//        elementsList.addElement(new GuiButtonSelect(cc, 6, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 6, cc.langBundle.getText("GuiOptions.buttonAutosave"), new int[]{-1, 5 * 60 * 1000, 10 * 60 * 1000, 15 * 60 * 1000}, new String[]{cc.langBundle.getText("GuiOptions.buttonAutosave.off"), cc.langBundle.getText("GuiOptions.buttonAutosave.5min"), cc.langBundle.getText("GuiOptions.buttonAutosave.10min"), cc.langBundle.getText("GuiOptions.buttonAutosave.15min")}).setCurrentValue(cc.settings.autosaveTime));
        elementsList.addElement(new GuiButton(cc, 9, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 6, "Language"));
        elementsList.addElement(new GuiButton(cc, 7, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 7, cc.langBundle.getText("GuiOptions.buttonMore")));

        elementsList.addElement(new GuiButton(cc, 8, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 8, cc.langBundle.getText("GuiOptions.buttonBack")).setEnabled(parentScreen != null));
    }

    public void onScreenClosed() {
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 5) {
            cc.displayGuiScreen(new GuiSelectPath(this));
        } else if (guiButton.getId() == 4) {
            cc.settings.showFps = !cc.settings.showFps;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.showFps);
        } else if (guiButton.getId() == 2) {
            GuiButtonSelect guiButtonSelect = getButtonSelect(2);
            guiButtonSelect.nextValue();
            cc.settings.renderDistance = guiButtonSelect.getCurrentValue();
            cc.render.reloadFrustum();
        } else if (guiButton.getId() == 8) {
            backToParentScreen();
        } else if (guiButton.getId() == 6) {
            GuiButtonSelect guiButtonSelect = getButtonSelect(guiButton.getId());
            guiButtonSelect.nextValue();
            cc.settings.autosaveTime = guiButtonSelect.getCurrentValue();
        } else if (guiButton.getId() == 7) {
            cc.displayGuiScreen(new GuiOptionsMore(this));
        } else if (guiButton.getId() == 3) {
            cc.settings.antialiasing = !cc.settings.antialiasing;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.antialiasing);
        } else if (guiButton.getId() == 0) {
            cc.settings.sounds = !cc.settings.sounds;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.sounds);
        } else if (guiButton.getId() == 1) {
            cc.settings.vibrations = !cc.settings.vibrations;
            getButtonOnOff(guiButton.getId()).setValue(cc.settings.vibrations);
        } else if (guiButton.getId() == 9) {
            cc.displayGuiScreen(new GuiSelectLanguage(this));
        }
    }
}
