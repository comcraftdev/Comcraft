/*
 * Copyright (C) 2013 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.util.Random;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockTreePlant extends BlockFlower {

    public BlockTreePlant(int id, int index) {
        super(id, index);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        world.getWorldUpdater().removeBlockUpdatable(x, y, z);
    }

    private void setWheatMetadata(World world, int x, int y, int z, int metadata) {
        world.setBlockMetadata(x, y, z, metadata - 128);
    }

    private int getWheatMetadata(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) + 128;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        setWheatMetadata(world, x, y, z, 0);

        world.getWorldUpdater().addBlockUpdatable(x, y, z);
    }

    public void tickBlock(World world, int x, int y, int z) {
        int currentTick = getWheatMetadata(world, x, y, z);

        if (world.isAirBlock(x, y + 1, z) && world.isAirBlock(x, y + 2, z) && world.isAirBlock(x, y + 3, z)) {
            ++currentTick;
        }

        if (currentTick >= 100) {
            world.getWorldUpdater().removeBlockUpdatable(x, y, z);

            placeTree(world, x, y, z);
        }

        if (currentTick <= 255) {
            setWheatMetadata(world, x, y, z, currentTick);
        }
    }

    private void placeTree(World world, int x, int y, int z) {
        world.setBlockID(x, y, z, Block.wood.blockID);

        for (int n = 1; n <= 3; ++n) {
            if (!world.isAirBlock(x, y + n, z)) {
                return;
            }

            world.setBlockID(x, y + n, z, Block.wood.blockID);
        }

        Random random = new Random();

        for (int n = 2; n <= 5; ++n) {
            for (int b = -1; b <= 1; ++b) {
                for (int a = -1; a <= 1; ++a) {
                    if (world.isAirBlock(x + a, y + n, z + b) && !(random.nextInt(6 - n) == 0)) {
                        world.setBlockID(x + a, y + n, z + b, Block.leaves.blockID);
                    }
                }
            }
        }
        
        world.setBlockID(x, y + 4, z, Block.leaves.blockID);
    }

    public boolean isUpdatableBlock() {
        return true;
    }
}