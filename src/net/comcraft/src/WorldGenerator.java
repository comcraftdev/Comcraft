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

public class WorldGenerator {

    private Comcraft cc;
    private String worldName;
    private WorldSaveType worldSaveType;
    private long seed;
    private SaveInfo saveHandler;
    private ChunkLoader chunkLoader;
    private int worldSize; //in chunks
    private boolean isFlat;
    private int flatLevel;
    private ChunkGenerator chunkGenerator;
    private boolean generateTrees;

    public WorldGenerator(Comcraft cc, String name, int worldSize, boolean isFlat, int flatLevel, boolean generateTrees) {
        this.cc = cc;
        worldName = name;
        saveHandler = cc.worldLoader.getSaveLoader(worldName);
        this.worldSize = worldSize;
        this.isFlat = isFlat;
        this.flatLevel = flatLevel;
        this.generateTrees = generateTrees;

        try {
            FileConnection file = (FileConnection) Connector.open(saveHandler.getSavePath(), Connector.READ_WRITE);

            if (!file.exists()) {
                file.mkdir();
            }

            file.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }

        chunkLoader = saveHandler.getChunkLoader(null);
    }

    public WorldSaveType generateAndSaveWorld() {
        cc.loadingScreen.displayLoadingScreen(cc.langBundle.getText("WorldGenereator.generatingWorld"));

        seed = System.currentTimeMillis();

        if (isFlat) {
            chunkGenerator = new ChunkGeneratorFlat(seed, flatLevel);
        } else {
            chunkGenerator = new ChunkGeneratorNormal(seed, generateTrees);
        }
        ModGlobals.event.runEvent("World.Generate", new Object[] { new Boolean(isFlat), chunkGenerator });

        worldSaveType = new WorldSaveType(saveHandler.getSavePath());

        writeWorldInfo();
        writeWorldData();

        return worldSaveType;
    }

    private void writeWorldInfo() {
        EntityPlayer player = new EntityPlayer(cc);
        player.setPlayerOnWorldCenter(worldSize);

        WorldInfo worldInfo = new WorldInfo();
        worldInfo.setWorldInfo(player, worldSize);

        saveHandler.saveWorldInfo(worldInfo, player);
    }

    private void writeWorldData() {
        chunkLoader.startSavingBlockStorage();

        for (int z = 0; z < worldSize; ++z) {
            for (int x = 0; x < worldSize; ++x) {
                chunkLoader.saveBlockStorage(chunkGenerator.generateChunk(x, z));
            }

            cc.loadingScreen.setProgress((float) z / (worldSize - 1));
        }

        chunkLoader.endSavingBlockStorage();

        chunkLoader.onChunkLoaderEnd();
    }
}
