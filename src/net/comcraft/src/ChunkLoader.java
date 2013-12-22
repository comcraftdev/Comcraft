package net.comcraft.src;

import java.util.Hashtable;

public interface ChunkLoader {
    void onChunkLoaderEnd();

    Chunk loadChunk(int x, int z);

    void saveChunks(Hashtable chunksMap, LoadingScreen loadingScreen);
}
