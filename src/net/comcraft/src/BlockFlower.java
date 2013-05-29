package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik (Queader)
 */
public class BlockFlower extends Block {

    protected BlockFlower(int id, int index) {
        super(id, index);
    }

    public boolean collidesWithPlayer() {
        return false;
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
}