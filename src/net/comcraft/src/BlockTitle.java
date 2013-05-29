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
public class BlockTitle extends Block {

    public BlockTitle(int id, int index) {
        super(id, index);
    }
    
    public int getRenderType() {
        return 4;
    }

    public VertexBuffer[][][][] getBlockVertexBufferPieced(World world, int x, int y, int z) {
        return ModelPieceCarpet.vertexBuffer;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelPieceCarpet.indexBuffer;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 0.3f, z + 1f);
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return !world.isBlockNormal(x, y, z) || side == 4;
    }

    public boolean isNormal() {
        return false;
    }

    public boolean canBePiecedVertically() {
        return false;
    }
}