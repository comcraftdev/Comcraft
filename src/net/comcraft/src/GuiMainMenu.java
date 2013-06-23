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

import java.util.Calendar;
import javax.microedition.lcdui.Graphics;
import net.comcraft.client.Comcraft;

public class GuiMainMenu extends GuiScreen implements GuiYesNoHost {

    private String currentHelloWord;

    public GuiMainMenu() {
        super(null);
    }

    public void onScreenDisplay() {
        Touch.resetTouch();
        Keyboard.resetKeyboard();
    }

    public void customDrawScreen() {
        cc.g.drawImage(cc.textureProvider.getImage("gui/comcraft_logo.png"), Comcraft.screenWidth / 2, 10, Graphics.TOP | Graphics.HCENTER);

        cc.g.setColor(0x007FFF);
        drawStringWithShadow(cc.g, currentHelloWord, 10, 13 + cc.textureProvider.getImage("gui/comcraft_logo.png").getHeight(), Graphics.TOP | Graphics.LEFT);

        cc.g.setColor(240, 240, 240);
        drawStringWithShadow(cc.g, "Copyright " + "Piotr Wójcik" + ".", Comcraft.screenWidth - 2, Comcraft.screenHeight - 2 - cc.g.getFont().getHeight() - 2, Graphics.BOTTOM | Graphics.RIGHT);
        drawStringWithShadow(cc.g, "Do not distribute!", Comcraft.screenWidth - 2, Comcraft.screenHeight - 2, Graphics.BOTTOM | Graphics.RIGHT);
    }

    protected void initGui() {
        currentHelloWord = getHelloWorld();

        int centerX = (Comcraft.screenWidth - GuiButton.getButtonWidth()) / 2;

        int logoHeight = cc.textureProvider.getImage("gui/comcraft_logo.png").getHeight();
        int startY = 10 + logoHeight + 30;

        elementsList.addElement(new GuiButton(cc, 0, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 0, cc.langBundle.getText("GuiMainMenu.buttonPlay")));
        elementsList.addElement(new GuiButton(cc, 1, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 1, cc.langBundle.getText("GuiMainMenu.buttonTexturepacks")));
        elementsList.addElement(new GuiButton(cc, 2, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 2, cc.langBundle.getText("GuiMainMenu.buttonOptions")));
        elementsList.addElement(new GuiButton(cc, 3, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 3, cc.langBundle.getText("GuiMainMenu.buttonInfo")));
        elementsList.addElement(new GuiButton(cc, 4, centerX, startY + (GuiButton.getButtonHeight() + (int) (GuiButton.getButtonHeight() * 0.35f)) * 4, cc.langBundle.getText("GuiMainMenu.buttonQuit")));
    }

    private String getHelloWorld() {
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY && calendar.get(Calendar.DAY_OF_MONTH) == 10) {
            return "Happy birthday Queader!";
        } else if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && (calendar.get(Calendar.DAY_OF_MONTH) == 24 || calendar.get(Calendar.DAY_OF_MONTH) == 25 || calendar.get(Calendar.DAY_OF_MONTH) == 26)) {
            return "Merry Christmas!";
        } else if (calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DAY_OF_MONTH) == 29) {
            return "Happy birthday Michał!";
        } else if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DAY_OF_MONTH) == 27) {
            return "Happy birthday Simon!";
        }

        return cc.helloWords.getRandomWord();
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.id == 0) {
            cc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (guiButton.id == 1) {
            cc.displayGuiScreen(new GuiSelectTexturepack(this));
        } else if (guiButton.id == 2) {
            cc.displayGuiScreen(new GuiOptions(this));
        } else if (guiButton.id == 4) {
            cc.displayGuiScreen(new GuiYesNo(this, cc.langBundle.getText("GuiMainMenu.quitAction.confirmationText")));
        } else if (guiButton.id == 3) {
            cc.displayGuiScreen(new GuiInfo(this));
        } else if (guiButton.id == 5) {
        }
    }

    public void guiYesNoAction(boolean value) {
        if (value) {
            cc.displayGuiScreen(null);
            cc.shutdown();
        }
    }
}
