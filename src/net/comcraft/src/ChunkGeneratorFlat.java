package net.comcraft.src;

import java.util.Calendar;
import java.util.Random;

public class ChunkGeneratorFlat extends ChunkGenerator {

    private int level;
    private Random random;
    
    public ChunkGeneratorFlat(long seed, int level) {
        super(seed);
        
        this.level = level;
        random = new Random(seed);
    }

    public ChunkStorage[] generateChunk(int chunkX, int chunkZ) {
        int blockGrassId = Block.grass.blockID;

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && (calendar.get(Calendar.DAY_OF_MONTH) == 24 || calendar.get(Calendar.DAY_OF_MONTH) == 25 || calendar.get(Calendar.DAY_OF_MONTH) == 26)) {
            blockGrassId = Block.snow.blockID;
        }
        
        boolean isHalloweenToday = calendar.get(Calendar.MONTH) == Calendar.OCTOBER && calendar.get(Calendar.DAY_OF_MONTH) == 31;

        ChunkStorage[] blockStorage = new ChunkStorage[8];

        for (int i = 0; i < 8; ++i) {
            blockStorage[i] = new ChunkStorage();
        }

        for (int y = 0; y < 32; ++y) {
            ChunkStorage blockStorageTemp = blockStorage[y >> 2];

            for (int z = 0; z < 4; ++z) {
                for (int x = 0; x < 4; ++x) {
                    int id = 0;

                    if (y == 0) {
                        id = Block.bedrock.blockID;
                    } else if (y >= 1 && y <= 3) {
                        id = Block.stone.blockID;
                    } else if (y >= 4 && y <= 10) {
                        int i = random.nextInt(y - 3);

                        if (i == 0) {
                            id = Block.stone.blockID;
                        } else {
                            id = Block.dirt.blockID;
                        }
                    }
                    
                    if (y < level - 1 && y >= 11) {
                        id = Block.dirt.blockID;
                    }
                    if (y == level - 1) {
                        id = Block.dirt.blockID;
                    }
                    if (y == level) {
                        id = Block.grass.blockID;
                    }
                    if (y > level) {
                        id = 0;
                    }
                    
                    if (y == level + 1 && isHalloweenToday && random.nextInt(30) == 0) {
                        id = Block.pumpkin.blockID;
                    }

                    blockStorageTemp.setBlockID(x, y & 3, z, id);
                }
            }
        }

        return blockStorage;
    }

    private void genereateTree(ChunkStorage[] blocksStorage, Random rand, int x, int y, int z) {
        for (int i = 0; i < 6; ++i) {
            blocksStorage[(i + y) >> 2].setBlockID(x, (y + i) & 3, z, Block.wood.blockID);
        }

        for (int i = 2; i < 6; ++i) {
            for (int zT = -1; zT <= 1; ++zT) {
                for (int xT = -1; xT <= 1; ++xT) {
                    if (rand.nextInt(5) == 0) {
                        continue;
                    }

                    blocksStorage[(i + y) >> 2].setBlockID(x + xT, (y + i) & 3, z + zT, Block.leaves.blockID);
                }
            }
        }

        blocksStorage[(6 + y) >> 2].setBlockID(x, (y + 6) & 3, z, Block.leaves.blockID);
    }
}
