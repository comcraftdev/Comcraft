package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WorldInfo {

    /*
     * worldVersion - in the past spawnX 
     * 
     * 2 - CC 0.5
     * 3 - CC 0.6
     */
    private float worldVersion;
    private float spawnX;
    private float spawnY;
    private float spawnZ;
    private int worldSize;

    public WorldInfo() {
        worldVersion = 3;
    }
    
    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldInfo(EntityPlayer player, int worldSize) {
        spawnX = player.xPos;
        spawnY = player.yPos;
        spawnZ = player.zPos;

        this.worldSize = worldSize;
    }

    public void writeWorldInfo(DataOutputStream dataOutputStream, EntityPlayer player) throws IOException {
        dataOutputStream.writeFloat(worldVersion);

        dataOutputStream.writeInt(worldSize);
        
        dataOutputStream.writeFloat(spawnX);
        dataOutputStream.writeFloat(spawnY);
        dataOutputStream.writeFloat(spawnZ);
        player.writeToDataOutputStream(dataOutputStream);
    }

    public void loadWorldInfo(DataInputStream dataInputStream, EntityPlayer player) throws IOException {
        worldVersion = dataInputStream.readFloat();

        if (worldVersion == 2f) {
            spawnY = dataInputStream.readFloat();
            spawnZ = dataInputStream.readFloat();
            player.loadFromDataInputStream(dataInputStream, worldVersion);
            
            spawnX = 10;
            spawnY = 20;
            spawnZ = 10;
            worldVersion = 3;
            worldSize = 16;
        } else {
            worldSize = dataInputStream.readInt();
            
            spawnX = dataInputStream.readFloat();
            spawnY = dataInputStream.readFloat();
            spawnZ = dataInputStream.readFloat();
            
            player.loadFromDataInputStream(dataInputStream, worldVersion);
        }
    }
}
