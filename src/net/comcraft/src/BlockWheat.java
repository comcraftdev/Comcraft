/*
 * Copyright (C) 2013 Piotr Wójcik
 *
 */
package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockWheat extends Block {

    private int[] indices;

    public BlockWheat(int id, int[] indices) {
        super(id);

        this.indices = indices;
    }

    public boolean doesBlockDestroyGrass() {
        return false;
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return world.getBlockID(x, y, z) == 0 || !Block.blocksList[world.getBlockID(x, y, z)].isNormal();
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        return ModelFlower.vertexBuffer;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelFlower.indexBuffer;
    }

    public boolean canBePieced() {
        return false;
    }

    public boolean isNormal() {
        return false;
    }

    public int getRenderType() {
        return 2;
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

        ++currentTick;

        if (currentTick >= 255) {
            world.getWorldUpdater().removeBlockUpdatable(x, y, z);
        }

        if (currentTick <= 255) {
            setWheatMetadata(world, x, y, z, currentTick);
        }
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (world == null) {
            return indices[7];
        }

        int currentTick = getWheatMetadata(world, x, y, z);

        return indices[(int) ((indices.length - 1) / 255f * currentTick)];
    }

    public int[] getUsedTexturesList() {
        return indices;
    }

    public boolean isUpdatableBlock() {
        return true;
    }
    
    public boolean collidesWithPlayer() {
        return false;
    }
}