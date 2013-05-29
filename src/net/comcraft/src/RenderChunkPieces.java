package net.comcraft.src;

public class RenderChunkPieces extends Thread {

    private World world;
    private Chunk chunk;
    public ChunkPiece[][][] chunkPieces;
    private boolean[][][] isBlockVisibile;

    public RenderChunkPieces(World world, Chunk chunk) {
        this.chunk = chunk;
        this.world = world;
    }

    public void regenereatePieces() {
        chunkPieces = null;
        chunkPieces = new ChunkPiece[4][32][4];
        isBlockVisibile = getIsBlockVisibile();

        for (int z = 0; z < 4; ++z) {
            for (int y = 0; y < 32; ++y) {
                for (int x = 0; x < 4; ++x) {
                    if (chunkPieces[x][y][z] == null && isBlockVisibile[x][y][z]) {
                        generateMegablock(x, y, z);
                    }
                }
            }
        }
    }

    private void generateMegablock(int x, int y, int z) {
        Block block = Block.blocksList[chunk.getBlockID(x, y, z)];

        int xSize = 0;
        int ySize = 0;
        int zSize = 0;

        if (block.canBePieced()) {
            xSize = getSizeX(x, y, z);
            ySize = getSizeY(x, y, z, xSize);
            zSize = getSizeZ(x, y, z, xSize, ySize);
        }

        fillPieces(x, y, z, xSize, ySize, zSize, block);
    }

    private void fillPieces(int x, int y, int z, int xSize, int ySize, int zSize, Block block) {
        ChunkPiece chunkPiece = new ChunkPiece((chunk.xPos << 2) + x, y, (chunk.zPos << 2) + z, xSize, ySize, zSize, block);

        for (int zI = 0; zI <= zSize; ++zI) {
            for (int yI = 0; yI <= ySize; ++yI) {
                for (int xI = 0; xI <= xSize; ++xI) {
                    chunkPieces[x + xI][y + yI][z + zI] = chunkPiece;
                }
            }
        }
    }

    private int getSizeZ(int x, int y, int z, final int xSize, final int ySize) {
        int zSize = -1;

        int firstId = chunk.getBlockID(x, y, z);

        for (int zI = 0; z + zI < 4; ++zI) {
            if (chunk.getBlockID(x, y, z + zI) != firstId || !isBlockVisibile[x][y][z + zI] || chunkPieces[x][y][z + zI] != null) {
                zSize = zI - 1;
                break;
            }
            if (getSizeY(x, y, z + zI, xSize) < ySize) {
                zSize = zI - 1;
                break;
            }
            if (z + zI == 3) {
                zSize = zI;
            }
        }

        return zSize;
    }

    private int getSizeY(int x, int y, int z, final int xSize) {
        int ySize = -1;

        int firstID = chunk.getBlockID(x, y, z);

        for (int yI = 0; yI < 4 && y + yI < 32; ++yI) {
            if (chunk.getBlockID(x, y + yI, z) != firstID || !isBlockVisibile[x][y + yI][z] || chunkPieces[x][y + yI][z] != null) {
                ySize = yI - 1;
                break;
            }
            if (getSizeX(x, y + yI, z) < xSize) {
                ySize = yI - 1;
                break;
            }
            if (yI == 3 || y + yI == 31 || !Block.blocksList[firstID].canBePiecedVertically()) {
                ySize = yI;
                break;
            }
        }

        return ySize;
    }

    private int getSizeX(int x, int y, int z) {
        int xSize = -1;

        int firstId = chunk.getBlockID(x, y, z);

        for (int xI = 0; x + xI < 4; ++xI) {
            if (chunk.getBlockID(x + xI, y, z) != firstId || !isBlockVisibile[x + xI][y][z] || chunkPieces[x + xI][y][z] != null) {
                xSize = xI - 1;
                break;
            }
            if (x + xI == 3) {
                xSize = xI;
            }
        }

        return xSize;
    }

    private boolean[][][] getIsBlockVisibile() {
        boolean[][][] blockTab = new boolean[4][32][4];

        for (int z = 0; z < 4; ++z) {
            for (int y = 0; y < 32; ++y) {
                for (int x = 0; x < 4; ++x) {
                    int id = chunk.getBlockID(x, y, z);

                    if (id == 0) {
                        continue;
                    }

                    Block block = Block.blocksList[id];

                    if (block.shouldSideBeRendered(world, ((chunk.xPos << 2) + x) + 1, y, ((chunk.zPos << 2) + z), 0)) {
                        blockTab[x][y][z] = true;
                        continue;
                    }
                    if (block.shouldSideBeRendered(world, ((chunk.xPos << 2) + x) - 1, y, ((chunk.zPos << 2) + z), 0)) {
                        blockTab[x][y][z] = true;
                        continue;
                    }
                    if (block.shouldSideBeRendered(world, ((chunk.xPos << 2) + x), y + 1, ((chunk.zPos << 2) + z), 0)) {
                        blockTab[x][y][z] = true;
                        continue;
                    }
                    if (block.shouldSideBeRendered(world, ((chunk.xPos << 2) + x), y - 1, ((chunk.zPos << 2) + z), 0)) {
                        blockTab[x][y][z] = true;
                        continue;
                    }
                    if (block.shouldSideBeRendered(world, ((chunk.xPos << 2) + x), y, ((chunk.zPos << 2) + z) + 1, 0)) {
                        blockTab[x][y][z] = true;
                        continue;
                    }
                    if (block.shouldSideBeRendered(world, ((chunk.xPos << 2) + x), y, ((chunk.zPos << 2) + z) - 1, 0)) {
                        blockTab[x][y][z] = true;
                        continue;
                    }
                }
            }
        }

        return blockTab;
    }
}
