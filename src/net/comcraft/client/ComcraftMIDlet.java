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

package net.comcraft.client;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import net.comcraft.src.CanvasComcraft;

public class ComcraftMIDlet extends MIDlet {

    private CanvasComcraft ccCanvas;
    private Comcraft cc;
    private Thread ccThread;
    public Display display;

    public ComcraftMIDlet() {
        display = null;
        ccThread = null;
        ccCanvas = new CanvasComcraft();
        cc = new Comcraft(this, ccCanvas);
    }

    public void openUrl(String url) {
        try {
            boolean flag = this.platformRequest(url);
            
            if (flag) {
                shutdown();
            }
        } catch (ConnectionNotFoundException ex) {
            //#debug
//#             ex.printStackTrace();
        }
    }
    
    private void startMainThread() {
        ccThread = new Thread(cc, "Comcraft main thread");
        ccThread.start();
    }

    public void startApp() {
        display = Display.getDisplay(this);

        display.setCurrent(ccCanvas);

        if (ccThread == null) {
            startMainThread();
        }

        if (cc != null) {
            cc.isGamePaused = false;
        }
    }

    public void pauseApp() {
        if (cc != null) {
            cc.isGamePaused = true;
        }
    }

    public void destroyApp(boolean unconditional) {
        shutdown();
    }

    public void shutdown() {
        cc.shutdown();
    }
}
