package net.comcraft.src;

public class Chunk {

    public final int xPos;
    public final int zPos;
    private ChunkStorage[] blockStorageArray;
    private World world;
    private boolean needsRegenereate;
    private RenderChunkPieces renderChunk;
    private boolean isEdited;

    public Chunk(World world, int chunkX, int chunkZ) {
        this.world = world;
        xPos = chunkX;
        zPos = chunkZ;
        blockStorageArray = new ChunkStorage[8];

        needsRegenereate = true;

        renderChunk = new RenderChunkPieces(world, this);
    }

    public void scanChunk() {
        for (int z = 0; z < 4; ++z) {
            for (int y = 0; y < 32; ++y) {
                for (int x = 0; x < 4; ++x) {
                    if (Block.blocksList[getBlockID(x, y, z)] != null && Block.blocksList[getBlockID(x, y, z)].isUpdatableBlock()) {
                        world.getWorldUpdater().addBlockUpdatable(x + (xPos << 2), y, z + (zPos << 2));
                    }
                }
            }
        }
    }

    public boolean isEdited() {
        return isEdited;
    }

    public RenderChunkPieces getRenderChunk() {
        if (xPos - 1 >= 0 && world.getChunkFromChunkCoords(xPos - 1, zPos).isEmptyChunk()) {
            return null;
        }
        if (xPos + 1 < world.worldSize && world.getChunkFromChunkCoords(xPos + 1, zPos).isEmptyChunk()) {
            return null;
        }
        if (zPos - 1 >= 0 && world.getChunkFromChunkCoords(xPos, zPos - 1).isEmptyChunk()) {
            return null;
        }
        if (zPos + 1 < world.worldSize && world.getChunkFromChunkCoords(xPos, zPos + 1).isEmptyChunk()) {
            return null;
        }

        if (needsRegenereate) {
            renderChunk.regenereatePieces();
            needsRegenereate = false;
        }

        return renderChunk;
    }

    public ChunkStorage[] getBlockStorageArray() {
        return blockStorageArray;
    }

    public void setBlockStorageArray(ChunkStorage[] blockStorage) {
        blockStorageArray = blockStorage;
    }

    public int getBlockID(int x, int y, int z) {
        return blockStorageArray[y >> 2].getBlockID(x, y & 3, z);
    }

    public boolean setBlockID(int x, int y, int z, int id) {
        return setBlockIDWithMetadata(x, y, z, id, 0);
    }

    public int getBlockMetadata(int x, int y, int z) {
        return blockStorageArray[y >> 2].getBlockMetadata(x, y & 3, z);
    }

    public boolean setBlockMetadata(int x, int y, int z, int metadata) {
        isEdited = true;

        ChunkStorage blockStorage = blockStorageArray[y >> 2];

        if (blockStorage == null) {
            return false;
        }

        int i = blockStorage.getBlockMetadata(x, y & 3, z);

        if (i == metadata) {
            return false;
        }

        blockStorage.setBlockMetadata(x, y & 3, z, metadata);

        return true;
    }

    public boolean setBlockIDWithMetadata(int x, int y, int z, int id, int metadata) {
        isEdited = true;
        needsRegenereate = true;

        if (x == 0) {
            world.getChunkFromChunkCoords(xPos - 1, zPos).needsRegenereate = true;
        } else if (x == 3) {
            world.getChunkFromChunkCoords(xPos + 1, zPos).needsRegenereate = true;
        }

        if (z == 0) {
            world.getChunkFromChunkCoords(xPos, zPos - 1).needsRegenereate = true;
        } else if (z == 3) {
            world.getChunkFromChunkCoords(xPos, zPos + 1).needsRegenereate = true;
        }

        int lastID = getBlockID(x, y, z);

        ChunkStorage blockStorage = blockStorageArray[y >> 2];

        if (blockStorage == null) {
            if (id == 0) {
                return false;
            }

            blockStorage = blockStorageArray[y >> 2] = new ChunkStorage();
        }

        if (world.chunkProvider.isServerGame) {
            world.cc.serverLoader.game.blockChanged(this, x, y, z, id, metadata);
        }

        blockStorage.setBlockID(x, y & 3, z, id);

        if (lastID != 0) {
            Block.blocksList[lastID].onBlockRemoval(world, (xPos << 2) + x, y, (zPos << 2) + z);
        }

        blockStorage.setBlockMetadata(x, y & 3, z, metadata);

        if (id != 0) {
            Block.blocksList[id].onBlockAdded(world, (xPos << 2) + x, y, (zPos << 2) + z);
        }

        return true;
    }

    public boolean isEmptyChunk() {
        return false;
    }
}
