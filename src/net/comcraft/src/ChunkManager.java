package net.comcraft.src;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import net.comcraft.client.Comcraft;

public class ChunkManager implements Runnable {

    public final Comcraft cc;
    private Hashtable chunksMap;
    private ChunkLoader chunkLoader;
    private ChunkEmpty emptyChunk;
    private Vector chunksQueue;
    private boolean isAlive;
    private long lastUnusedChunksCheck;
    private Chunk[][] chunksTable;
    private World world;

    public ChunkManager(Comcraft cc, ChunkLoader chunkLoader, World world) {
        this.cc = cc;
        this.world = world;
        this.chunkLoader = chunkLoader;
        chunksMap = new Hashtable(1024);
        emptyChunk = new ChunkEmpty();
        chunksQueue = new Vector(256);
        chunksTable = new Chunk[world.worldSize][world.worldSize];
        startThread();
    }

    public void releaseUnusedChunks() {
        if (cc.currentTime - lastUnusedChunksCheck < 3500) {
            return;
        }

        int size = cc.settings.renderDistance * 2 + 3;
        int startX = (((int) (cc.player.xPos)) >> 2) - cc.settings.renderDistance - 1;
        int startZ = (((int) (cc.player.zPos)) >> 2) - cc.settings.renderDistance - 1;

        Enumeration enumeration = chunksMap.keys();

        while (enumeration.hasMoreElements()) {
            Integer id = (Integer) enumeration.nextElement();

            int x = getDecodedChunkX(id, world.worldSize);
            int z = getDecodedChunkZ(id, world.worldSize);

            if (!(x >= startX && x <= startX + size && z >= startZ && z <= startZ + size)) {
                Chunk chunk = (Chunk) chunksMap.get(id);

                if (!chunk.isEdited()) {
                    chunksMap.remove(id);
                }
            }
        }
        
        lastUnusedChunksCheck = cc.currentTime;
    }

    private void startThread() {
        isAlive = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public Chunk getChunk(int x, int z) {
        if (x < 0 || x >= world.worldSize || z < 0 || z >= world.worldSize) {
            return emptyChunk;
        }

        Chunk chunk = chunksTable[x][z];

        if (chunk == null) {
            chunk = (Chunk) chunksMap.get(getChunkID(x, z, world.worldSize));

            if (chunk == null) {
                chunk = loadChunk(x, z);
            }

            if (!chunk.isEmptyChunk()) {
                chunksTable[x][z] = chunk;
            }
        }

        return chunk;
    }

    public Chunk loadChunk(int x, int z) {
        if (x < 0 || x >= world.worldSize || z < 0 || z >= world.worldSize) {
            return emptyChunk;
        }

        Integer id = getChunkID(x, z, world.worldSize);

        if (!chunksQueue.contains(id)) {
            chunksQueue.addElement(id);
        }

        return emptyChunk;
    }

    public static Integer getChunkID(int x, int z, int worldSize) {
        return new Integer(x + z * worldSize);
    }

    public static int getDecodedChunkX(Integer chunkId, int worldSize) {
        int id = chunkId.intValue();

        return id % worldSize;
    }

    public static int getDecodedChunkZ(Integer chunkId, int worldSize) {
        int id = chunkId.intValue();

        return (id - getDecodedChunkX(chunkId, worldSize)) / worldSize;
    }

    public void saveAllChunks(LoadingScreen loadingScreen) {
        if (!chunksQueue.isEmpty()) {
            chunksQueue.removeAllElements();
        }

        chunkLoader.saveChunks(chunksMap, loadingScreen);
    }

    public void onChunkProviderEnd() {
        chunkLoader.onChunkLoaderEnd();
        isAlive = false;
    }

    public int getLoadedChunksNum() {
        return chunksMap.size();
    }

    public int getChunksQueueNum() {
        return chunksQueue.size();
    }

    private Integer getTheClosestChunk() {
        Integer closest = (Integer) chunksQueue.firstElement();
        
        for (int n = 1; n < chunksQueue.size(); ++n) {
            Integer c = (Integer) chunksQueue.elementAt(n);
            
            if (c.intValue() < closest.intValue()) {
                closest = c;
            }
        }
        
        return closest;
    }
    
    public void run() {
        while (isAlive) {
            if (!chunksQueue.isEmpty()) {
                try {
                    Integer integer = getTheClosestChunk();

                    int x = getDecodedChunkX(integer, world.worldSize);
                    int z = getDecodedChunkZ(integer, world.worldSize);

                    Chunk chunk = chunkLoader.loadChunk(x, z);
                    chunksMap.put(getChunkID(x, z, world.worldSize), chunk);

                    chunksQueue.removeElement(integer);
                } catch (OutOfMemoryError error) {
                    if (!cc.settings.ignoreOutOfMemory) {
                        throw error;
                    }
                }
            } else {
                try {
                    Thread.sleep(750);
                } catch (InterruptedException ex) {
                    //#debug
//#                     ex.printStackTrace();
                    throw new ComcraftException(ex);
                }
            }
        }
    }
}
