/*
 * Copyright (C) 2013 Piotr Wójcik
 * 
 * This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.comcraft.src;

import net.comcraft.client.Comcraft;

public class GuiInfo extends GuiScreen {

    public GuiInfo(GuiScreen parentScreen) {
        super(parentScreen);
    }

    protected void customDrawScreen() {
    }

    protected void initGui() {
        final int smallInterval = (int) (GuiButton.getButtonHeight() * 0.1f);
        final int bigInterval = (int) (GuiButton.getButtonHeight() * 0.3f);

        int centerX = (Comcraft.screenWidth - GuiButton.getButtonWidth()) / 2;

        int y = 3;

        /*
         * Look! It's important!
         * 
         * Feel free to add your name here, but you shouldn't remove existing authors. It would be very unfair!
         */
        
        elementsList.addElement(new GuiButton(cc, 0, centerX, y, cc.langBundle.getText("GuiInfo.labelCreator")));
        y += GuiButton.getButtonHeight() + smallInterval;
        elementsList.addElement(new GuiButton(cc, 1, centerX, y, "Piotr Wójcik"));

        y += GuiButton.getButtonHeight() + bigInterval;

        elementsList.addElement(new GuiButton(cc, 5, centerX, y, cc.langBundle.getText("GuiInfo.labelMusic")));
        y += GuiButton.getButtonHeight() + smallInterval;
        elementsList.addElement(new GuiButton(cc, 6, centerX, y, "Bartłomiej Bemowski"));

        y += GuiButton.getButtonHeight() + bigInterval;

        elementsList.addElement(new GuiButton(cc, 2, centerX, y, cc.langBundle.getText("GuiInfo.labelSpecialThanks")));
        y += GuiButton.getButtonHeight() + smallInterval;
        elementsList.addElement(new GuiButton(cc, 3, centerX, y, "Michał Silent"));
        y += GuiButton.getButtonHeight() + smallInterval;
        elementsList.addElement(new GuiButton(cc, 4, centerX, y, "Simon Game"));

        y += GuiButton.getButtonHeight() + bigInterval;

        elementsList.addElement(new GuiButton(cc, 7, centerX, y, cc.langBundle.getText("GuiInfo.labelVersion")));
        y += GuiButton.getButtonHeight() + smallInterval;
        elementsList.addElement(new GuiButton(cc, 8, centerX, y, "Comcraft 1.0"));
        y += GuiButton.getButtonHeight() + smallInterval;
        elementsList.addElement(new GuiButton(cc, 8, centerX, y, "Mod Loader " + ModLoader.version));

        y += GuiButton.getButtonHeight() + bigInterval;

        elementsList.addElement(new GuiButton(cc, 9, centerX, y, "Available RAM:"));
        y += GuiButton.getButtonHeight() + smallInterval;
        elementsList.addElement(new GuiButton(cc, 10, centerX, y, Runtime.getRuntime().totalMemory() / 1048576f + " MB"));
    }

    public void onScreenClosed() {
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        backToParentScreen();
    }
}
