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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class SaveInfo implements LevelInfo {

    private String savePath;

    public SaveInfo(String savePath) {
        this.savePath = savePath;
    }

    public ChunkLoader getChunkLoader(World world) {
        return new LocalChunkLoader(world, savePath);
    }
    
    public String getSavePath() {
        return savePath;
    }

    public WorldInfo loadWorldInfo(EntityPlayer player) {
        try {
            FileConnection file = (FileConnection) Connector.open(savePath + "level.info", Connector.READ);
            
            DataInputStream dataInputStream = file.openDataInputStream();
            
            WorldInfo worldInfo = new WorldInfo();
            worldInfo.loadWorldInfo(dataInputStream, player);
            
            dataInputStream.close();
            file.close();
            
            return worldInfo;
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public void saveWorldInfo(WorldInfo worldInfo, EntityPlayer player) {
        try {
            FileConnection file = (FileConnection) Connector.open(savePath + "level.info", Connector.READ_WRITE);
            
            if (!file.exists()) {
                file.create();
            }
            
            DataOutputStream dataOutputStream = file.openDataOutputStream();
            
            worldInfo.writeWorldInfo(dataOutputStream, player);
            
            dataOutputStream.close();
            file.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }
}
