/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */

package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockChristmasTree extends Block {
    
    private int indexTop;
    private int indexBottom;
    
    public BlockChristmasTree(int id, int indexTop, int indexBottom) {
        super(id);
        
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
    }
    
    public boolean canBePieced() {
        return false;
    }

    public boolean canBePiecedVertically() {
        return false;
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return true;
    }

    public boolean isNormal() {
        return false;
    }

    public int[] getUsedTexturesList() {
        return new int[]{indexTop, indexBottom};
    }

    /*
     * 0 bottom, 1 top
     */
    private int getChristrmasTreeSide(World world, int x, int y, int z) {
        if (world == null) {
            return 0;
        }
        
        return world.getBlockMetadata(x, y, z);
    }
    
    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        if (super.canPlaceBlockAt(world, x, y - 1, z)) {
            world.setBlockMetadata(x, y, z, 1);

            world.setBlockAndMetadata(x, y - 1, z, blockID, 0);
        } else if (super.canPlaceBlockAt(world, x, y + 1, z)) {
            world.setBlockMetadata(x, y, z, 0);

            world.setBlockAndMetadata(x, y + 1, z, blockID, 1);
        }
    }
    
    public VertexBuffer[][][][] getBlockVertexBuffer(World world, int x, int y, int z) {
        return ModelChristmasTree.vertexBuffer[getChristrmasTreeSide(world, x, y, z)];
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelChristmasTree.indexBuffer;
    }
    
    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (world == null) {
            return indexTop;
        }

        if (getChristrmasTreeSide(world, x, y, z) == 0) {
            return indexBottom;
        } else {
            return indexTop;
        }
    }
    
    public void onBlockRemoval(World world, int x, int y, int z) {
        int side = getChristrmasTreeSide(world, x, y, z);

        if (side == 0) {
            world.setBlockID(x, y + 1, z, 0);
        } else if (side == 1) {
            world.setBlockID(x, y - 1, z, 0);
        }
    }
    
    public int getRenderType() {
        return 0;
    }
    
}