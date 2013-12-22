package net.comcraft.src;

public interface LevelInfo {

    WorldInfo loadWorldInfo(EntityPlayer player);

    void saveWorldInfo(WorldInfo worldInfo, EntityPlayer player);

    ChunkLoader getChunkLoader(World world);

}
