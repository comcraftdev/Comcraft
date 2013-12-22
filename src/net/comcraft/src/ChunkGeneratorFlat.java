package net.comcraft.src;

import java.util.Calendar;

public class ChunkGeneratorFlat extends ChunkGenerator {

    private int level;

    public ChunkGeneratorFlat(long seed, int level) {
        super(seed);
        this.level = level;
    }

    public ChunkStorage[] generateChunk(int chunkX, int chunkZ) {
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && (calendar.get(Calendar.DAY_OF_MONTH) == 24 || calendar.get(Calendar.DAY_OF_MONTH) == 25 || calendar.get(Calendar.DAY_OF_MONTH) == 26)) {
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
                        id = Block.getBlock("bedrock").blockID;
                    } else if (y >= 1 && y <= 3) {
                        id = Block.getBlock("stone").blockID;
                    } else if (y >= 4 && y <= 10) {
                        int i = random.nextInt(y - 3);

                        if (i == 0) {
                            id = Block.getBlock("stone").blockID;
                        } else {
                            id = Block.getBlock("dirt").blockID;
                        }
                    }
                    
                    if (y < level - 1 && y >= 11) {
                        id = Block.getBlock("dirt").blockID;
                    }
                    if (y == level - 1) {
                        id = Block.getBlock("dirt").blockID;
                    }
                    if (y == level) {
                        id = Block.getBlock("grass").blockID;
                    }
                    if (y > level) {
                        id = 0;
                    }
                    
                    if (y == level + 1 && isHalloweenToday && random.nextInt(30) == 0) {
                        id = Block.getBlock("pumpkin").blockID;
                    }
                    id = runModGens(id,-1,level, x,y,z, chunkX,chunkZ);
                    blockStorageTemp.setBlockID(x, y & 3, z, id);
                }
            }
        }

        return blockStorage;
    }
}
