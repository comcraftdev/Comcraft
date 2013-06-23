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


/*
 * Class is currently not used.
 */

package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import net.comcraft.client.Comcraft;

public class RenderPC {

    private Comcraft cc;
    private Image screenImage;
    private Graphics gLast;
    private boolean isBinded;

    public RenderPC(Comcraft cc) {
        this.cc = cc;
        screenImage = Image.createImage(640, 640);
    }

    public boolean isBinded() {
        return isBinded;
    }
    
    public void bindRenderPC() {
        isBinded = true;
        gLast = cc.g;
        cc.g = screenImage.getGraphics();
        cc.g.setClip(0, 0, Comcraft.screenWidth, Comcraft.screenHeight);
    }

    public void releaseRenderPC() {
        isBinded = false;
        cc.g = gLast;
        cc.g.setClip(0, 0, Comcraft.screenHeight, Comcraft.screenWidth);
        cc.g.drawRegion(screenImage, 0, 0, screenImage.getWidth(), screenImage.getHeight(), Sprite.TRANS_ROT270, 0, Comcraft.screenWidth - Comcraft.screenHeight, Graphics.TOP | Graphics.LEFT);
    }
}
