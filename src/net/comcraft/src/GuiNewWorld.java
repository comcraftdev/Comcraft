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

public class GuiNewWorld extends GuiScreen {

    private GuiScreen guiMainMenu;

    public GuiNewWorld(GuiScreen parentScreen, GuiScreen guiMainMenu) {
        super(parentScreen);

        this.guiMainMenu = guiMainMenu;
    }

    protected void customDrawScreen() {
    }

    protected void initGui() {
        int centerX = (Comcraft.screenWidth - GuiButton.getButtonWidth()) / 2;
        int startY = 30;

        elementsList.addElement(new GuiButton(cc, 0, centerX, startY, cc.langBundle.getText("GuiNewWorld.buttonCreate")));

        elementsList.addElement(new GuiButtonSelect(cc, 2, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 2, cc.langBundle.getText("GuiNewWorld.worldType"), new int[]{0, 1}, new String[]{cc.langBundle.getText("GuiNewWorld.worldType.normal"), cc.langBundle.getText("GuiNewWorld.worldType.flat")}));
        elementsList.addElement(new GuiButtonSelect(cc, 4, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 3, cc.langBundle.getText("GuiNewWorld.size"), new int[]{16, 32, 64, 256}, new String[]{cc.langBundle.getText("GuiNewWorld.size.normal"), cc.langBundle.getText("GuiNewWorld.size.big"), cc.langBundle.getText("GuiNewWorld.size.bigger"), cc.langBundle.getText("GuiNewWorld.size.biggest")}));
        
        elementsList.addElement(new GuiButtonSelect(cc, 3, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 4, cc.langBundle.getText("GuiNewWorld.level"), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12 (default)", "13", "14", "15"}).setCurrentValue(12).setEnabled(false));

//        elementsList.addElement(new GuiButtonOnOff(cc, 5, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 5, cc.langBundle.getText("GuiNewWorld.generateTrees")).setValue(false).setEnabled(true));
        
        elementsList.addElement(new GuiButton(cc, 1, centerX, startY + (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6) * 6, cc.langBundle.getText("GuiNewWorld.buttonClose")).setEnabled(parentScreen != null));//Close
    }

    public void onScreenClosed() {
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 0) {
            generateWorld();
            cc.displayGuiScreen(new GuiSelectWorld(guiMainMenu));
        } else if (guiButton.getId() == 1) {
            backToParentScreen();
        } else if (guiButton.getId() == 2) {
            GuiButtonSelect guiButtonSelect = getButtonSelect(guiButton.getId());
            guiButtonSelect.nextValue();
            
            if (guiButtonSelect.getCurrentValue() == 1) {
                getButton(3).setEnabled(true);
//                getButton(5).setEnabled(false);
            } else {
                getButton(3).setEnabled(false);
//                getButton(5).setEnabled(true);
            }
        } else if (guiButton.getId() == 3) {
            GuiButtonSelect guiButtonSelect = getButtonSelect(guiButton.getId());
            guiButtonSelect.nextValue();
        } else if (guiButton.getId() == 4) {
            GuiButtonSelect guiButtonSelect = getButtonSelect(guiButton.getId());
            guiButtonSelect.nextValue();
        } else if (guiButton.getId() == 5) {
            boolean value = !getButtonOnOff(guiButton.getId()).getValue();
            getButtonOnOff(guiButton.getId()).setValue(value);
        } 
    }

    private boolean isWorldWithTheSameName(String name) {
        for (int i = 0; i < cc.worldLoader.getWorldList().size(); ++i) {
            WorldSaveType worldSaveType = (WorldSaveType) cc.worldLoader.getWorldList().elementAt(i);

            if (worldSaveType.getWorldName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void generateWorld() {
        String worldName;

        for (int i = 1;; ++i) {
            String name = "World " + i;

            if (!isWorldWithTheSameName(name)) {
                worldName = name;
                break;
            }
        }

        int worldSize = getButtonSelect(4).getCurrentValue();

        boolean isFlat = getButtonSelect(2).getCurrentValue() == 1;
        int flatLevel = getButtonSelect(3).getCurrentValue();
        
//        boolean generateTrees = getButtonOnOff(5).getValue();

        WorldGenerator worldGenerator = new WorldGenerator(cc, worldName, worldSize, isFlat, flatLevel, false);
        worldGenerator.generateAndSaveWorld();
    }
}
