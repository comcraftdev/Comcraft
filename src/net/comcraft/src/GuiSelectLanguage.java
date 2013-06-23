/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.util.Vector;

import net.comcraft.client.Comcraft;

/**
 *
 * @author Piotr Wójcik
 */
public class GuiSelectLanguage extends GuiScreenSlotHost {

    private Vector languagesList = new Vector();
    private LanguageSet selectetLanguage;

    public GuiSelectLanguage(GuiScreen parentScreen) {
        super(parentScreen);
        guiSlot = new GuiSlotSelectLanguage(this);

        initLanguagesList();
    }

    private void initLanguagesList() {
        languagesList.addElement(new LanguageSet("English", "/lang/en.lng"));
        languagesList.addElement(new LanguageSet("Deutsch", "/lang/de.lng"));
        languagesList.addElement(new LanguageSet("Español", "/lang/es.lng"));
        languagesList.addElement(new LanguageSet("Hrvatski (Hrvatska)", "/lang/hr-HR.lng"));
        languagesList.addElement(new LanguageSet("Polski", "/lang/pl-PL.lng"));
        languagesList.addElement(new LanguageSet("Português (Brasil)", "/lang/pt-BR.lng"));
        languagesList.addElement(new LanguageSet("Pусский", "/lang/ru.lng"));
        languagesList.addElement(new LanguageSet("Український", "/lang/uk.lng"));
    }

    protected void initGuiSlotCustom() {
        elementsList.addElement(new GuiButtonSmall(cc, 0, 5, Comcraft.screenHeight - 5 - GuiButtonSmall.getButtonHeight(), "Select").setEnabled(false));
        elementsList.addElement(new GuiButtonSmall(cc, 1, Comcraft.screenWidth - 5 - GuiButtonSmall.getButtonWidth(), Comcraft.screenHeight - 5 - GuiButtonSmall.getButtonHeight(), "Close").setEnabled(parentScreen != null));

        elementClicked(0);
    }

    public void onScreenClosed() {
    }

    public void elementClicked(int id) {
        if (id >= getElementsList().size() || id < 0) {
            getButton(0).setEnabled(false);
            selectetLanguage = null;
            return;
        }

        selectetLanguage = (LanguageSet) getElementsList().elementAt(id);

        getButton(0).setEnabled(true);
    }

    public Vector getElementsList() {
        return languagesList;
    }

    protected void customDrawScreen() {
        guiSlot.drawScreen();

        drawTitle("Select Language");
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.id == 0) {
            cc.langBundle.loadBundle(selectetLanguage.getPatch());
            cc.settings.language = selectetLanguage.getPatch();

            cc.displayGuiScreen(new GuiMainMenu());
        } else if (guiButton.id == 1) {
            backToParentScreen();
        }
    }
}