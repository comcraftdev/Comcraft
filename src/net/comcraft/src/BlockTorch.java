/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockTorch extends Block {

    public BlockTorch(int id, int index) {
        super(id, index);
    }
    
    public boolean collidesWithPlayer() {
        return false;
    }
    
    public boolean doesBlockDestroyGrass() {
        return false;
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

    public Transform getBlockTransform(World world, int x, int y, int z, Transform transform, int s) {
        if (world == null) {
            return transform;
        }

        Transform blockTransform = new Transform();
        blockTransform.set(transform);

        int side = world.getBlockMetadata(x, y, z);

        if (side < 4) {
            if (side == 0) {
                blockTransform.postTranslate(0, 5, -4);

                blockTransform.postRotate(30, 1, 0, 0);
            } else if (side == 1) {
                blockTransform.postTranslate(0, 0, 6);

                blockTransform.postRotate(-30, 1, 0, 0);
            } else if (side == 2) {
                blockTransform.postTranslate(-4, 5, 0);

                blockTransform.postRotate(-30, 0, 0, 1);
            } else if (side == 3) {
                blockTransform.postTranslate(6, 0, 0);

                blockTransform.postRotate(30, 0, 0, 1);
            }
        }

        return blockTransform;
    }

    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        world.setBlockMetadata(x, y, z, side);
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        if (side == 5) {
            return false;
        } else {
            return true;
        }
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        return ModelTorch.vertexBuffer;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelTorch.indexBuffer;
    }

    public int getRenderType() {
        return 5;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 1f, z + 1f);
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, InvItemStack itemStack) {
        return true;
    }

    public boolean isNormal() {
        return false;
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        int side = world.getBlockMetadata(x, y, z);
        
        int tX = x;
        int tY = y;
        int tZ = z;
        
        if (side == 0) {
            --z;
        } else if (side == 1) {
            ++z;
        } else if (side == 2) {
            --x;
        } else if (side == 3) {
            ++x;
        } else if (side == 4) {
            --y;
        } else if (side == 5) {
            --y;
        }
        
        if (world.isAirBlock(x, y, z)) {
            world.setBlockID(tX, tY, tZ, 0);
        }
    }
    
}