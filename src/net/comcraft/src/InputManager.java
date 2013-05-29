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

public class InputManager {

    private Comcraft cc;
    private float touchSensitivity;
    
    public InputManager(Comcraft cc) {
        this.cc = cc;
        touchSensitivity = 1f / (Comcraft.screenHeight / 80);
    }

    private void handleTouchInput() {
        if (Touch.isInputHandled()) {
            return;
        }

        if (Touch.isDragged()) {
            rotatePlayerTouch(Touch.getTouchXDifference(), Touch.getTouchYDifference());
        } else if (Touch.isPressed() && Touch.getTouchPressDtTime(cc.currentTime) > 800 && Touch.getNextEvent() != 2) {
            cc.onClick(0, cc.player.rayTrace(cc.playerManager.getBlockReachDistance(), Touch.getX(), Touch.getY()));
            Touch.resetTouch();
        } else if (Touch.wasPressed() && !Touch.wasDragged() && Touch.getTouchPressDtTime(cc.currentTime) <= 800) {
            cc.onClick(1, cc.player.rayTrace(cc.playerManager.getBlockReachDistance(), Touch.getX(), Touch.getY()));
            Touch.resetTouch();
        }
    }

    private void rotatePlayerTouch(int xDifference, int yDifference) {
        cc.player.rotate(xDifference * touchSensitivity, yDifference * touchSensitivity);
    }

    private void rotatePlayerKeyboard(int rotationPitch, int rotationYaw) {
        float dt = cc.dt;
        
        if (dt > 0.3f) {
            dt = 0.3f;
        }
        
        float rotation = 45f * dt;

        cc.player.rotate(rotation * rotationPitch, rotation * rotationYaw);
    }

    private void handleKeyboardInput() {
        if (Keyboard.isButtonDown(Keyboard.KEY_DOWN) || Keyboard.isButtonDown(Keyboard.KEY_K)) {
            rotatePlayerKeyboard(-1, 0);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_UP) || Keyboard.isButtonDown(Keyboard.KEY_I)) {
            rotatePlayerKeyboard(1, 0);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_LEFT) || Keyboard.isButtonDown(Keyboard.KEY_J)) {
            rotatePlayerKeyboard(0, -1);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_RIGHT) || Keyboard.isButtonDown(Keyboard.KEY_L)) {
            rotatePlayerKeyboard(0, 1);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_NUM2) || Keyboard.isButtonDown(Keyboard.KEY_W)) {
            cc.player.moveEntity(0, 0, -1);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_NUM8) || Keyboard.isButtonDown(Keyboard.KEY_S)) {
            cc.player.moveEntity(0, 0, 1);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_NUM4) || Keyboard.isButtonDown(Keyboard.KEY_A)) {
            cc.player.moveEntity(1, 0, 0);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_NUM6) || Keyboard.isButtonDown(Keyboard.KEY_D)) {
            cc.player.moveEntity(-1, 0, 0);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_NUM1) || Keyboard.isButtonDown(Keyboard.KEY_Q)) {
            cc.player.moveEntity(0, 1, 0);
        } else if (Keyboard.isButtonDown(Keyboard.KEY_NUM3) || Keyboard.isButtonDown(Keyboard.KEY_E)) {
            cc.player.moveEntity(0, -1, 0);
        } else if (((!Keyboard.isButtonDown(Keyboard.KEY_NUM5) && Keyboard.wasButtonDown(Keyboard.KEY_NUM5)) || (!Keyboard.isButtonDown(Keyboard.KEY_F) && Keyboard.wasButtonDown(Keyboard.KEY_F)) || (!Keyboard.isButtonDown(Keyboard.KEY_FIRE) && Keyboard.wasButtonDown(Keyboard.KEY_FIRE))) && Keyboard.getButtonPressDtTime(cc.currentTime) <= 400) {
            cc.onClick(1, cc.player.rayTrace(cc.playerManager.getBlockReachDistance()));
            Keyboard.resetKeyboard();
        } else if ((Keyboard.isButtonDown(Keyboard.KEY_NUM5) || Keyboard.isButtonDown(Keyboard.KEY_F) || Keyboard.isButtonDown(Keyboard.KEY_FIRE)) && Keyboard.getButtonPressDtTime(cc.currentTime) > 400) {
            cc.onClick(0, cc.player.rayTrace(cc.playerManager.getBlockReachDistance()));
            Keyboard.resetKeyboard();
        } else if (Keyboard.wasButtonDown(Keyboard.KEY_NUM0) || Keyboard.wasButtonDown(Keyboard.KEY_T)) {
            cc.endWorld();
        } else if (Keyboard.wasButtonDown(Keyboard.KEY_NUM7) || Keyboard.wasButtonDown(Keyboard.KEY_U)) {
            if (cc.player.inventory.getSelectedElementNum() > 0) {
                cc.player.inventory.setSelectedElement(cc.player.inventory.getSelectedElementNum() - 1);
            } else {
                cc.player.inventory.setSelectedElement(cc.player.inventory.getFastSlotSize() - 1);
            }
        } else if (Keyboard.wasButtonDown(Keyboard.KEY_NUM9) || Keyboard.wasButtonDown(Keyboard.KEY_O)) {
            if (cc.player.inventory.getSelectedElementNum() < cc.player.inventory.getFastSlotSize() - 1) {
                cc.player.inventory.setSelectedElement(cc.player.inventory.getSelectedElementNum() + 1);
            } else {
                cc.player.inventory.setSelectedElement(0);
            }
        }
    }

    public void handleInput() {
        if (cc.world == null || cc.currentScreen != null) {
            return;
        }

        handleKeyboardInput();
        handleTouchInput();
    }
}
