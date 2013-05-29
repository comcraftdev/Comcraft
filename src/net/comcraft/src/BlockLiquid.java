package net.comcraft.src;

import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockLiquid extends Block {

    private int waterFall;

    public BlockLiquid(int id, int textureIndex) {
        super(id, textureIndex);

        waterFall = 1;
    }

    public boolean doesBlockDestroyGrass() {
        return false;
    }
    
    public boolean isReplaceableBlock() {
        return true;
    }

    public BlockLiquid setWaterFall(int waterFall) {
        this.waterFall = waterFall;

        return this;
    }

    public boolean isUpdatableBlock() {
        return true;
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return world.getBlockID(x, y, z) == 0 || (world.getBlockID(x, y, z) != 0 && side == 4) || !Block.blocksList[world.getBlockID(x, y, z)].isNormal() || (world.getBlockID(x, y, z) == blockID && getWaterHeightFromMetadata(world, x, y, z) != 8);
    }

    public boolean isNormal() {
        return false;
    }

    public int getRenderType() {
        return 3;
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        int height = getWaterHeightFromMetadata(world, x, y, z) - 1;

        if (height < 0) {
            return null;
        }

        return ModelLiquid.vertexBuffer[height];
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    }

    private int getWaterHeightFromMetadata(World world, int x, int y, int z) {
        if (world == null) {
            return 6;
        }

        return (world.getBlockMetadata(x, y, z) >> 4) & 15;
    }

    private int getWaterHeightFromWorldAndSide(World world, int x, int y, int z, int side) {
        if (world == null) {
            return 6;
        }

        if (side == 7) {
            return 8;
        } else {
            if (side == 1) {
                ++z;
            } else if (side == 2) {
                --z;
            } else if (side == 3) {
                ++x;
            } else if (side == 4) {
                --x;
            } else if (side == 5) {
                ++y;
            } else if (side == 6) {
                --y;
            } else {
                return 0;
            }

            if (world.getBlockID(x, y, z) != blockID) {
                return 0;
            }

            int height;

            if (side == 5) {
                return 8;
            } else {
                height = getWaterHeightFromWorld(world, x, y, z) - waterFall;
            }

            if (height < 0) {
                height = 0;
            }

            return height;
        }
    }

    private int getWaterHeightFromWorld(World world, int x, int y, int z) {
        if (world == null) {
            return 6;
        }

        return getWaterHeightFromWorldAndSide(world, x, y, z, world.getBlockMetadata(x, y, z) & 15);
    }

    private int getWaterMetadataToSaveFromWorld(World world, int x, int y, int z, int side) {
        int waterHeight = getWaterHeightFromWorldAndSide(world, x, y, z, side);

        return side | (waterHeight << 4);
    }

    private int getWaterMetadataToSaveFromMetadata(World world, int x, int y, int z, int height) {
        int side = world.getBlockMetadata(x, y, z) & 15;

        return side | (height << 4);
    }

    private int getWaterMetadataToSave(int side, int height) {
        return (side | (height << 4));
    }

    private int getWaterSide(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) & 15;
    }

    public void tickBlock(World world, int x, int y, int z) {
        if (getWaterHeightFromWorld(world, x, y, z) == 0 && getWaterHeightFromMetadata(world, x, y, z) > 0) {
            int newHeight = getWaterHeightFromMetadata(world, x, y, z) - 3;

            if (newHeight > 0) {
                world.setBlockMetadata(x, y, z, getWaterMetadataToSaveFromMetadata(world, x, y, z, newHeight));
            } else {
                world.setBlockID(x, y, z, 0);
            }

            return;
        }
        
        System.out.println("metadata: " + getWaterHeightFromMetadata(world, x, y, z));
        System.out.println("world: " + getWaterHeightFromWorld(world, x, y, z));

        if (getWaterHeightFromMetadata(world, x, y, z) - waterFall == 0) {
            return;
        } 
        if (getWaterHeightFromMetadata(world, x, y, z) <= 0) {
            world.setBlockID(x, y, z, 0);
            return;
        }

        checkForFusion(world, x, y, z);
        
        if (world.getBlockID(x, y - 1, z) == 0 || world.getBlockID(x, y - 1, z) == blockID) {
            world.setBlockAndMetadata(x, y - 1, z, blockID, getWaterMetadataToSaveFromWorld(world, x, y - 1, z, 5));
        }

        if (world.getBlockID(x, y - 1, z) != 0 && world.getBlockID(x, y - 1, z) != blockID) {
            if (world.getBlockID(x - 1, y, z) == 0 || (world.getBlockID(x - 1, y, z) == blockID && getWaterHeightFromMetadata(world, x - 1, y, z) < getWaterHeightFromWorld(world, x, y, z) && getWaterSide(world, x, y, z) != 4)) {
                world.setBlockAndMetadata(x - 1, y, z, blockID, getWaterMetadataToSaveFromWorld(world, x - 1, y, z, 3));
            }
            if (world.getBlockID(x + 1, y, z) == 0 || (world.getBlockID(x + 1, y, z) == blockID && getWaterHeightFromMetadata(world, x + 1, y, z) < getWaterHeightFromWorld(world, x, y, z) && getWaterSide(world, x, y, z) != 3)) {
                world.setBlockAndMetadata(x + 1, y, z, blockID, getWaterMetadataToSaveFromWorld(world, x + 1, y, z, 4));
            }
            if (world.getBlockID(x, y, z - 1) == 0 || (world.getBlockID(x, y, z - 1) == blockID && getWaterHeightFromMetadata(world, x, y, z - 1) < getWaterHeightFromWorld(world, x, y, z) && getWaterSide(world, x, y, z) != 2)) {
                world.setBlockAndMetadata(x, y, z - 1, blockID, getWaterMetadataToSaveFromWorld(world, x, y, z - 1, 1));
            }
            if (world.getBlockID(x, y, z + 1) == 0 || (world.getBlockID(x, y, z + 1) == blockID && getWaterHeightFromMetadata(world, x, y, z + 1) < getWaterHeightFromWorld(world, x, y, z) && getWaterSide(world, x, y, z) != 1)) {
                world.setBlockAndMetadata(x, y, z + 1, blockID, getWaterMetadataToSaveFromWorld(world, x, y, z + 1, 2));
            }
        }
    }

    private void checkForFusion(World world, int x, int y, int z) {
        Block currentBlock = Block.blocksList[world.getBlockID(x, y, z)];
        Block oppositeBlock = (currentBlock == Block.water ? Block.lava : Block.water);

        int height = getWaterHeightFromMetadata(world, x, y, z);

        if (Block.blocksList[world.getBlockID(x + 1, y, z)] == oppositeBlock && height > 1) {
            if (height < 7) {
                world.setBlockID(x + 1, y, z, Block.cobblestone.blockID);
            } else {
                world.setBlockID(x + 1, y, z, Block.obsidian.blockID);
            }
        }
        if (Block.blocksList[world.getBlockID(x - 1, y, z)] == oppositeBlock && height > 1) {
            if (height < 7) {
                world.setBlockID(x - 1, y, z, Block.cobblestone.blockID);
            } else {
                world.setBlockID(x - 1, y, z, Block.obsidian.blockID);
            }
        }
        if (Block.blocksList[world.getBlockID(x, y - 1, z)] == oppositeBlock && height > 1) {
            if (height < 7) {
                world.setBlockID(x, y - 1, z, Block.cobblestone.blockID);
            } else {
                world.setBlockID(x, y - 1, z, Block.obsidian.blockID);
            }
        }
        if (Block.blocksList[world.getBlockID(x, y, z + 1)] == oppositeBlock && height > 1) {
            if (height < 7) {
                world.setBlockID(x, y, z + 1, Block.cobblestone.blockID);
            } else {
                world.setBlockID(x, y, z + 1, Block.obsidian.blockID);
            }
        }
        if (Block.blocksList[world.getBlockID(x, y, z - 1)] == oppositeBlock && height > 1) {
            if (height < 7) {
                world.setBlockID(x, y, z - 1, Block.cobblestone.blockID);
            } else {
                world.setBlockID(x, y, z - 1, Block.obsidian.blockID);
            }
        }
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        world.getWorldUpdater().removeBlockUpdatable(x, y, z);
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        world.getWorldUpdater().addBlockUpdatable(x, y, z);
    }

    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        world.setBlockMetadata(x, y, z, getWaterMetadataToSave(7, 8));
    }

    public boolean canBePieced() {
        return false;
    }
}