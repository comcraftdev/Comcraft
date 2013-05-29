package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class ChunkLoader {

    private World world;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private FileConnection file;
    private int currenPosition;
    private boolean readWriteFlag;
    private boolean isAlive;

    public ChunkLoader(World world, String path) {
        this.world = world;
        isAlive = true;

        try {
            openChunkLoaderStreams(path);
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException("ChunkLoader <init>", ex);
        }
    }

    private void openChunkLoaderStreams(String path) throws IOException {
        file = (FileConnection) Connector.open(path + "world.data", Connector.READ_WRITE);

        if (!file.exists()) {
            file.create();
        }
    }

    public void onChunkLoaderEnd() {
        isAlive = false;
        
        while (readWriteFlag) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                //#debug
//#                 ex.printStackTrace();
            }
        }
        
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }

            if (dataOutputStream != null) {
                dataOutputStream.close();
            }

            file.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException("onChunkLoaderEnd", ex);
        }
    }

    public Chunk loadChunk(int x, int z) {
        return readChunkFromFile(x, z);
    }

    public void saveChunks(Hashtable chunksList, LoadingScreen loadingScreen) {
        writeChunksToFile(chunksList, loadingScreen);
    }

    private Chunk readChunkFromFile(int x, int z) {
        if (!isAlive) {
            return null;
        }
        
        readWriteFlag = true;
        
        int bytesToSkip = (x + z * world.worldSize) * 1024;

        if (dataInputStream == null || currenPosition > bytesToSkip) {
            try {
                if (dataInputStream != null) {
                    dataInputStream.close();
                }

                dataInputStream = file.openDataInputStream();
                currenPosition = 0;
            } catch (IOException ex) {
                //#debug
//#             ex.printStackTrace();
//            throw new ComcraftException("(load) open inputStream exception(x: " + x + " z: " + z + ")", ex);
            }
        }

        try {
            dataInputStream.skipBytes(bytesToSkip - currenPosition);
            
            currenPosition = bytesToSkip + 1024;
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
//            throw new ComcraftException("(load) skip dataInputStream exception(x: " + x + " z: " + z + " skippedBytes: " + (x + z * World.worldSize) * 1024 + ")", ex);
        }

        Chunk chunk = new Chunk(world, x, z);

        ChunkStorage[] blockStorage = new ChunkStorage[8];

        for (int i = 0; i < blockStorage.length; ++i) {
            blockStorage[i] = new ChunkStorage();

            byte[] id = new byte[64];
            byte[] metadata = new byte[64];

            try {
                dataInputStream.read(id);
                dataInputStream.read(metadata);
            } catch (IOException ex) {
                //#debug
//#                 ex.printStackTrace();
//                throw new ComcraftException("(load) reading blockStorage exception(x: " + x + " z: " + z + " i: " + i + ")", ex);
            }

            blockStorage[i].setBlockIDArray(id);
            blockStorage[i].setBlockMetadataArray(metadata);

            blockStorage[i].initBlockStorage();
        }

        chunk.setBlockStorageArray(blockStorage);

        chunk.scanChunk();

        readWriteFlag = false;
        
        return chunk;
    }

    private void writeChunksToFile(Hashtable chunksList, LoadingScreen loadingScreen) {
        if (!isAlive) {
            return;
        }
        
        readWriteFlag = true;
        
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }

            dataInputStream = file.openDataInputStream();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
//            throw new ComcraftException("(save) open inputStream exception", ex);
        }

        startSavingBlockStorage();

        for (int z = 0; z < world.worldSize; ++z) {
            for (int x = 0; x < world.worldSize; ++x) {
                Chunk chunk = (Chunk) chunksList.get(ChunkManager.getChunkID(x, z, world.worldSize));

                if (chunk != null) {
                    try {
                        dataInputStream.skipBytes(1024);
                    } catch (IOException ex) {
                        //#debug
//#                         ex.printStackTrace();
//                        throw new ComcraftException("(save) skip dataInputStream exception(x: " + x + " z: " + z + ")", ex);
                    }

                    ChunkStorage[] blockStorage = chunk.getBlockStorageArray();

                    saveBlockStorage(blockStorage);
                } else {
                    byte[] data = new byte[1024];

                    try {
                        dataInputStream.read(data);
                        dataOutputStream.write(data);
                    } catch (IOException ex) {
                        //#debug
//#                         ex.printStackTrace();
//                        throw new ComcraftException("(save) reading blockStorage exception(x: " + x + " z: " + z + ")", ex);
                    }
                }
            }

            if (loadingScreen != null) {
                loadingScreen.setProgress((float) z / (world.worldSize - 1));
            }
        }

        endSavingBlockStorage();

        try {
            dataInputStream.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
//            throw new ComcraftException("(save) close inputStream exception", ex);
        }

        dataInputStream = null;
        
        readWriteFlag = false;
    }

    public void startSavingBlockStorage() {
        try {
            dataOutputStream = file.openDataOutputStream();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException("startSavingBlockStorage", ex);
        }
    }

    public void saveBlockStorage(ChunkStorage[] blockStorage) {
        if (dataOutputStream == null) {
            return;
        }
        
        for (int i = 0; i < 8; ++i) {
            try {
                dataOutputStream.write(blockStorage[i].getBlockIDArray());
                dataOutputStream.write(blockStorage[i].getBlockMetadataArray());
            } catch (IOException ex) {
                //#debug
//#                 ex.printStackTrace();
                throw new ComcraftException("saveBlockStorage", ex);
            }
        }
    }

    public void endSavingBlockStorage() {
        try {
            dataOutputStream.close();
            dataOutputStream = null;
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException("endSavingBlockStorage", ex);
        }
    }
}
