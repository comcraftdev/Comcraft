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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import net.comcraft.client.Comcraft;

public class CanvasComcraft extends GameCanvas {

    public volatile boolean visibile;
    public CanvasComcraft() {
        super(false);
        setFullScreenMode(true);

        Touch.setSupportTouch(hasPointerEvents());

        visibile = false;
    }

    public void setComcraft(Comcraft cc) {
    }

    protected void showNotify() {
        visibile = true;
    }

    protected void hideNotify() {
        visibile = false;
    }

    public Graphics getGraphics() {
        return super.getGraphics();
    }

    public void pointerPressed(int x, int y) {
        Touch.pointerPressed(x, y);
    }

    public void pointerDragged(int x, int y) {
        int nX = x;
        int nY = y;
        
        Touch.pointerDragged(nX, nY);
    }

    public void pointerReleased(int x, int y) {
        int nX = x;
        int nY = y;
        
        Touch.pointerReleased(nX, nY);
    }

    public void keyPressed(int key) {
        Keyboard.keyPressed(key);
    }

    public void keyRepeated(int key) {
        Keyboard.keyRepeated(key);
    }

    public void keyReleased(int key) {
        Keyboard.keyReleased(key);
    }
}
