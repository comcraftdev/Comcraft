package net.comcraft.src;

public class ChunkStorage {

    private byte[] blockIDArray;
    private byte[] blockMetadataArray;
    public boolean containsBlocks;
    public boolean containsNonOpaqueBlocks;

    public ChunkStorage() {
        blockIDArray = new byte[64];
        blockMetadataArray = new byte[64];
    }

    public int getBlockID(int x, int y, int z) {
        return (int) blockIDArray[x + (y << 2) + (z << 4)] & 0xFF;
    }

    public void setBlockID(int x, int y, int z, int id) {
        if (id != 0) {
            containsBlocks = true;
            
            if (!Block.blocksList[id].isNormal()) {
                containsNonOpaqueBlocks = true;
            }
        }

        blockIDArray[x + (y << 2) + (z << 4)] = (byte) id;
    }

    public int getBlockMetadata(int x, int y, int z) {
        return (int) blockMetadataArray[x + (y << 2) + (z << 4)];
    }

    public void setBlockMetadata(int x, int y, int z, int metadata) {
        blockMetadataArray[x + (y << 2) + (z << 4)] = (byte) metadata;
    }

    public byte[] getBlockIDArray() {
        return blockIDArray;
    }

    public void setBlockIDArray(byte[] data) {
        blockIDArray = data;
    }

    public byte[] getBlockMetadataArray() {
        return blockMetadataArray;
    }

    public void setBlockMetadataArray(byte[] data) {
        blockMetadataArray = data;
    }

    public void initBlockStorage() {
        containsBlocks = false;
        containsNonOpaqueBlocks = false;

        for (int i = 0; i < 4 * 4 * 4; ++i) {
            if (blockIDArray[i] != 0) {
                containsBlocks = true;
                
                if (!Block.blocksList[(blockIDArray[i] & 0xFF)].isNormal()) {
                    containsNonOpaqueBlocks = true;
                    return;
                }
            }
        }
    }
}
