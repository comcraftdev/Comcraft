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

import java.util.Vector;

public class Keyboard {

    public static final int KEY_UP = -1;
    public static final int KEY_DOWN = -2;
    public static final int KEY_LEFT = -3;
    public static final int KEY_RIGHT = -4;
    public static final int KEY_FIRE = -5;
    public static final int KEY_SOFT_LEFT = -6;
    public static final int KEY_SOFT_RIGHT = -7;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM2 = 50;
    public static final int KEY_NUM3 = 51;
    public static final int KEY_NUM4 = 52;
    public static final int KEY_NUM5 = 53;
    public static final int KEY_NUM6 = 54;
    public static final int KEY_NUM7 = 55;
    public static final int KEY_NUM8 = 56;
    public static final int KEY_NUM9 = 57;
    public static final int KEY_NUM0 = 48;
    public static final int KEY_POUND = 35;
    public static final int KEY_STAR = 42;
    public static final int KEY_W = (char) 'w';
    public static final int KEY_S = (char) 's';
    public static final int KEY_A = (char) 'a';
    public static final int KEY_D = (char) 'd';
    public static final int KEY_Q = (char) 'q';
    public static final int KEY_E = (char) 'e';
    public static final int KEY_R = (char) 'r';
    public static final int KEY_I = (char) 'i';
    public static final int KEY_J = (char) 'j';
    public static final int KEY_K = (char) 'k';
    public static final int KEY_L = (char) 'l';
    public static final int KEY_F = (char) 'f';
    public static final int KEY_O = (char) 'o';
    public static final int KEY_U = (char) 'u';
    public static final int KEY_T = (char) 't';
    public static final int KEY_Y = (char) 'y';
    private static int actKey = -999;
    private static int lastKey = -999;
    private static boolean isPressed = false;
    private static boolean wasPressed = false;
    private static long buttonDownTime;
    private static long buttonPressTime;
    private static Vector typeQueue;
    private static Vector valueQueue;
    private static Vector timeQueue;
    private static boolean hasAnyKeyBeenPressed = false;

    private Keyboard() {
    }

    static {
        typeQueue = new Vector(50);
        valueQueue = new Vector(50);
        timeQueue = new Vector(50);
    }

    public static boolean hasAnyKeyBeenPressed() {
        return hasAnyKeyBeenPressed;
    }

    public static int getCurrentKey() {
        return actKey;
    }

    public static int getLastKey() {
        return lastKey;
    }

    public static boolean isQueueEmpty() {
        return typeQueue.isEmpty() || valueQueue.isEmpty() || timeQueue.isEmpty();
    }

    public static void keyPressed(int key) {
        typeQueue.addElement(new Integer(0));
        valueQueue.addElement(new Integer(key));
        timeQueue.addElement(new Long(System.currentTimeMillis()));
        
        hasAnyKeyBeenPressed = true;
    }

    public static void keyRepeated(int key) {
        typeQueue.addElement(new Integer(1));
        valueQueue.addElement(new Integer(key));
        timeQueue.addElement(new Long(0));
    }

    public static void keyReleased(int key) {
        typeQueue.addElement(new Integer(2));
        valueQueue.addElement(new Integer(key));
        timeQueue.addElement(new Long(System.currentTimeMillis()));
    }

    public static void tickKeyboard() {
        if (isQueueEmpty()) {
            return;
        }

        Integer typeInteger = (Integer) typeQueue.firstElement();
        Integer valueInteger = (Integer) valueQueue.firstElement();
        Long timeLong = (Long) timeQueue.firstElement();

        typeQueue.removeElementAt(0);
        valueQueue.removeElementAt(0);
        timeQueue.removeElementAt(0);

        int type = typeInteger.intValue();
        int value = valueInteger.intValue();
        long time = timeLong.longValue();

        if (type == 0) {
            keyDown(value, time);
        } else if (type == 1) {
            keyHold(value);
        } else {
            keyUp(value, time);
        }
    }

    private static void keyDown(int key, long time) {
        buttonDownTime = time;
        actKey = key;
        isPressed = true;
        wasPressed = false;
    }

    private static void keyUp(int key, long time) {
        buttonPressTime = time - buttonDownTime;
        actKey = -999;
        lastKey = key;
        isPressed = false;
        wasPressed = true;
    }

    private static void keyHold(int key) {
    }

    public static boolean isButtonDown(int key) {
        return isPressed && isTheSameKey(key, actKey);
    }

    public static boolean wasButtonDown(int key) {
        if (wasPressed && isTheSameKey(key, lastKey)) {
            wasPressed = false;
            return true;
        } else {
            return false;
        }
    }

    private static boolean isTheSameKey(int actKey, int lastKey) {
        if (actKey == lastKey) {
            return true;
        }

        if (actKey >= 97 && actKey <= 122) {
            return actKey - 32 == lastKey;
        }

        return false;
    }

    public static int getButtonPressDtTime(long currentTime) {
        if (isPressed) {
            return (int) (System.currentTimeMillis() - buttonDownTime);
        }
        return (int) (buttonPressTime);
    }

    public static void resetKeyboard() {
        actKey = -999;
        lastKey = -999;
        isPressed = false;
        wasPressed = false;

        typeQueue.removeAllElements();
        valueQueue.removeAllElements();
        timeQueue.removeAllElements();
    }
}
