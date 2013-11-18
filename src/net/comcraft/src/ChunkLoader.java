package net.comcraft.src;

import java.util.Hashtable;

public interface ChunkLoader {

    void startSavingBlockStorage();

    void saveBlockStorage(ChunkStorage[] generateChunk);

    void onChunkLoaderEnd();

    Chunk loadChunk(int x, int z);

    void saveChunks(Hashtable chunksMap, LoadingScreen loadingScreen);

    void endSavingBlockStorage();

}
