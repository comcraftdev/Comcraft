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
import net.comcraft.client.Comcraft;

public class Touch {

    private static final int minUntouchTime = 100;
    private static int touchX = -1;
    private static int touchY = -1;
    private static int lastTouchX = -1;
    private static int lastTouchY = -1;
    private static boolean isPressed = false;
    private static boolean isDragged = false;
    private static boolean wasPressed = false;
    private static boolean wasUnpressed = false;
    private static boolean wasDragged = false;
    private static boolean supportTouch = false;
    private static long touchDownTime;
    private static long touchPressTime;
    private static boolean inputHandled = false;
    private static Vector typeQueue;
    private static Vector valueXQueue;
    private static Vector valueYQueue;
    private static Vector timeQueue;
    private static long lastUnpressTime;
    private static boolean touchIgnored;
    private static boolean inverted;

    private Touch() {
    }

    static {
        typeQueue = new Vector(50);
        valueXQueue = new Vector(50);
        valueYQueue = new Vector(50);
        timeQueue = new Vector(50);
        inverted = false;
    }

    public static void setInvesion(boolean flag) {
        inverted = flag;
    }

    public static boolean isQueueEmpty() {
        return typeQueue.isEmpty() || valueXQueue.isEmpty() || valueYQueue.isEmpty() || timeQueue.isEmpty();
    }

    public static int getNextEvent() {
        if (typeQueue.isEmpty()) {
            return -1;
        }

        Integer integer = (Integer) typeQueue.firstElement();
        return integer.intValue();
    }

    public static void pointerPressed(int x, int y) {
        if (System.currentTimeMillis() - lastUnpressTime > minUntouchTime) {
            typeQueue.addElement(new Integer(0));
            valueXQueue.addElement(new Integer(x));
            valueYQueue.addElement(new Integer(y));
            timeQueue.addElement(new Long(System.currentTimeMillis()));
        } else {
            touchIgnored = true;
        }
    }

    public static void pointerDragged(int x, int y) {
        if (!touchIgnored) {
            typeQueue.addElement(new Integer(1));
            valueXQueue.addElement(new Integer(x));
            valueYQueue.addElement(new Integer(y));
            timeQueue.addElement(new Long(0));
        }
    }

    public static void pointerReleased(int x, int y) {
        lastUnpressTime = System.currentTimeMillis();

        if (!touchIgnored) {
            typeQueue.addElement(new Integer(2));
            valueXQueue.addElement(new Integer(x));
            valueYQueue.addElement(new Integer(y));
            timeQueue.addElement(new Long(System.currentTimeMillis()));
        } else {
            touchIgnored = false;
        }
    }

    public static void tickTouch() {
        if (isQueueEmpty()) {
            return;
        }

        Integer typeInteger = (Integer) typeQueue.firstElement();
        Integer valueXInteger = (Integer) valueXQueue.firstElement();
        Integer valueYInteger = (Integer) valueYQueue.firstElement();
        Long timeLong = (Long) timeQueue.firstElement();

        typeQueue.removeElementAt(0);
        valueXQueue.removeElementAt(0);
        valueYQueue.removeElementAt(0);
        timeQueue.removeElementAt(0);

        int type = typeInteger.intValue();
        int valueX = valueXInteger.intValue();
        int valueY = valueYInteger.intValue();
        long time = timeLong.longValue();

        actionType(type, valueX, valueY, time);
    }

    private static void actionType(int type, int x, int y, long time) {
        if (type == 0) {
            touchDown(x, y, time);
        } else if (type == 1) {
            touchMoved(x, y);
        } else {
            touchUp(x, y, time);
        }
    }

    private static void touchDown(int x, int y, long time) {
        touchDownTime = time;
        touchX = x;
        touchY = y;
        lastTouchX = x;
        lastTouchY = y;
        isPressed = true;
        wasDragged = false;
        wasPressed = false;
        inputHandled = false;
    }

    private static void touchUp(int x, int y, long time) {
        touchPressTime = time - touchDownTime;
        isPressed = false;
        isDragged = false;
        wasPressed = true;
        wasUnpressed = true;
        lastTouchX = -1;
        lastTouchY = -1;
    }

    private static void touchMoved(int x, int y) {
        if (lastTouchX == -1) {
            lastTouchX = touchX;
            lastTouchY = touchY;
        }

        touchX = x;
        touchY = y;

        if (Math.abs(lastTouchX - touchX) > Comcraft.getScreenHeight() / 20 || Math.abs(lastTouchY - touchY) > Comcraft.getScreenHeight() / 20) {
            isDragged = true;
            wasDragged = true;
        }
    }

    public static void setInputHandled() {
        inputHandled = true;
    }

    public static void setInputHandled(boolean flag) {
        inputHandled = flag;
    }

    public static boolean isInputHandled() {
        return inputHandled;
    }

    private static int getPcX(int x, int y) {
        return y;
    }

    private static int getPcY(int x, int y) {
        return Comcraft.getScreenWidth() - x;
    }

    public static int getX() {
        return !inverted ? touchX : getPcX(touchX, touchY);
    }

    public static int getY() {
        return !inverted ? touchY : getPcY(touchX, touchY);
    }

    public static int getTouchXDifference() {
        int difference = lastTouchX - touchX;

        lastTouchX = touchX;

        return difference;
    }

    public static int getTouchYDifference() {
        int difference = lastTouchY - touchY;

        lastTouchY = touchY;

        return difference;
    }

    public static boolean isPressed() {
        return isPressed;
    }

    public static boolean isDragged() {
        return isDragged;
    }

    public static boolean wasPressed() {
        if (wasPressed) {
            wasPressed = false;
            return true;
        } else {
            return false;
        }
    }

    public static boolean wasUnpressed() {
        if (wasUnpressed) {
            wasUnpressed = false;
            return true;
        } else {
            return false;
        }
    }

    public static boolean wasDragged() {
        return wasDragged;
    }

    public static void setSupportTouch(boolean flag) {
        supportTouch = flag;
    }

    public static boolean isTouchSupported() {
        return supportTouch;
    }

    public static int getTouchPressDtTime(long currentTime) {
        if (isPressed) {
            return (int) (System.currentTimeMillis() - touchDownTime);
        }
        return (int) (touchPressTime);
    }

    public static void resetTouch() {
        isPressed = false;
        isDragged = false;
        wasPressed = false;
        wasUnpressed = false;
        inverted = false;

        typeQueue.removeAllElements();
        valueXQueue.removeAllElements();
        valueYQueue.removeAllElements();
        timeQueue.removeAllElements();
    }
}
