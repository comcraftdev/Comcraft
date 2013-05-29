/*
 * Copyright (C) 2013 Piotr Wójcik
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
public class BlockFence extends Block {

    public BlockFence(int id, int index) {
        super(id, index);
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
        if (world == null) {
            if (side < 6) {
                return true;
            }
            return false;
        }

        if (side > 5) {
            if (world.getBlockID(x, y, z - 1) == this.blockID || world.getBlockID(x, y, z + 1) == this.blockID) {
                return true;
            }
            return false;
        }

        if (world.getBlockID(x - 1, y, z) == this.blockID || world.getBlockID(x + 1, y, z) == this.blockID) {
            return true;
        } else if (!(world.getBlockID(x, y, z - 1) == this.blockID || world.getBlockID(x, y, z + 1) == this.blockID)) {
            return true;
        }

        return false;
    }

    public int getRenderType() {
        return 8;
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        if (world == null) {
            return ModelFence.vertexBufferFull;
        }

        if (world.getBlockID(x, y, z - 1) == this.blockID || world.getBlockID(x, y, z + 1) == this.blockID || world.getBlockID(x - 1, y, z) == this.blockID || world.getBlockID(x + 1, y, z) == this.blockID) {
            return ModelFence.vertexBufferFull;
        }

        return ModelFence.vertexBufferSmall;
    }

    public Transform getBlockTransform(World world, int x, int y, int z, Transform transform, int side) {
        if (world == null) {
            return transform;
        }

        Transform blockTransform = new Transform();
        blockTransform.set(transform);

        if (side > 5 && (world.getBlockID(x, y, z - 1) == this.blockID || world.getBlockID(x, y, z + 1) == this.blockID)) {
            blockTransform.postRotate(90, 0, 1, 0);
            blockTransform.postTranslate(-10, 0, 0);
        }

        return blockTransform;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelFence.indexBuffer;
    }

    public boolean isNormal() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        if (world.getBlockID(x, y, z - 1) == this.blockID || world.getBlockID(x, y, z + 1) == this.blockID) {
            if (world.getBlockID(x - 1, y, z) == this.blockID || world.getBlockID(x + 1, y, z) == this.blockID) {
                return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 1f, z + 1f);
            }

            return AxisAlignedBB.getBoundingBox(x + 0.3f, y, z, x + 0.7f, y + 1f, z + 1f);
        }

        if (world.getBlockID(x - 1, y, z) == this.blockID || world.getBlockID(x + 1, y, z) == this.blockID) {
            return AxisAlignedBB.getBoundingBox(x, y, z + 0.3f, x + 1f, y + 1f, z + 0.7f);
        }
        
        return AxisAlignedBB.getBoundingBox(x + 0.3f, y, z + 0.3f, x + 0.7f, y + 1f, z + 0.7f);
    }
}