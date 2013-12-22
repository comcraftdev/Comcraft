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

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.comcraft.client.Comcraft;

public final class ComcraftFileSystem {

    private String comcraftPath;

    public ComcraftFileSystem(Comcraft cc, String path) {
        comcraftPath = path;
    }

    public void initComcraftFileSystem() {
        if (!isAvailable()) {
            return;
        }

        initFolder(comcraftPath + "comcraft/");
        initFolder(comcraftPath + "comcraft/" + "mods/");
        initFolder(comcraftPath + "comcraft/" + "saves/");
        initFolder(comcraftPath + "comcraft/" + "texturepacks/");
        initFolder(comcraftPath + "comcraft/" + "music/");
        initFolder(comcraftPath + "comcraft/" + "screenshots/");
    }

    private void initFolder(String path) {
        try {
            FileConnection fileConnection = (FileConnection) Connector.open(path, Connector.READ_WRITE);

            if (!fileConnection.exists()) {
                fileConnection.mkdir();
            }

            fileConnection.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public String getPathToFolder(String folder) {
        return comcraftPath + "comcraft/" + folder;
    }

    public String getComcraftPath() {
        return isAvailable() ? comcraftPath : "";
    }

    public boolean isAvailable() {
        if (comcraftPath == null || comcraftPath.equals("")) {
            return false;
        }

        return true;
    }
}
