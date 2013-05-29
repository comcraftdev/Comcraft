package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.VertexBuffer;

public class BlockSlab extends Block {

    private int indexSides;
    private int indexTopDown;

    public BlockSlab(int id, int indexSides, int indexTopDown) {
        super(id, -1);

        this.indexSides = indexSides;
        this.indexTopDown = indexTopDown;
    }
    
    public BlockSlab(int id, int index) {
        this(id, index, index);
    }

    public int[] getUsedTexturesList() {
        return new int[] {indexSides, indexTopDown};
    }
    
    public boolean canBePiecedVertically() {
        return false;
    }
    
    public VertexBuffer[][][][] getBlockVertexBufferPieced(World world, int x, int y, int z) {
        return ModelPieceSlab.vertexBuffer;
    }
    
    public IndexBuffer getBlockIndexBuffer() {
        return ModelPieceSlab.indexBuffer;
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (side > 3) {
            return indexTopDown;
        } else {
            return indexSides;
        }
    }

    public boolean isNormal() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 0.5f, z + 1f);
    }
    
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int i = world.getBlockID(x, y, z);
        return i == blockID || i == 0;
    }
    
    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return !world.isBlockNormal(x, y, z) || side == 4;
    }
}
