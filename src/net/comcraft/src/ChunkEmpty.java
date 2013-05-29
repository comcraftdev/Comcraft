package net.comcraft.src;

public class ChunkEmpty extends Chunk {

    public ChunkEmpty() {
        super(null, 0, 0);
    }

    public RenderChunkPieces getRenderChunk() {
        return null;
    }

    public ChunkStorage[] getBlockStorageArray() {
        return null;
    }

    public void setBlockStorageArray(ChunkStorage[] blockStorage) {
    }

    public boolean setBlockID(int x, int y, int z, int id) {
        return false;
    }

    public boolean setBlockIDWithMetadata(int x, int y, int z, int id, int metadata) {
        return false;
    }

    public int getBlockID(int localX, int localY, int localZ) {
        return 1;
    }

    public int getBlockMetadata(int localX, int localY, int localZ) {
        return 0;
    }

    public boolean isEmptyChunk() {
        return true;
    }
}
