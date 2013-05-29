package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotrek
 */
public class BlockStairs extends Block {

    public BlockStairs(int id, int index) {
        super(id, index);
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        if (side == 5 || side == 10) {
            return !world.isBlockNormal(x, y, z);
        }
        
        return true;
    }

    public boolean canBePiecedVertically() {
        return false;
    }

    public boolean canBePieced() {
        return false;
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        return ModelStairs.vertexBuffer;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelPieceSlab.indexBuffer;
    }

    public boolean isNormal() {
        return false;
    }

    public int getRenderType() {
        return 7;
    }

    public Transform getBlockTransform(World world, int x, int y, int z, Transform transform, int s) {
        if (world == null) {
            return transform;
        }

        Transform blockTransform = new Transform();
        blockTransform.set(transform);

        int side = world.getBlockMetadata(x, y, z);

        if (side == 3) {
            blockTransform.postRotate(90 * 3, 0, 1, 0);
            blockTransform.postTranslate(0, 0, -10);
        } else if (side == 0) {
            blockTransform.postRotate(90 * 2, 0, 1, 0);
            blockTransform.postTranslate(-10, 0, -10);
        } else if (side == 1) {
            blockTransform.postRotate(90 * 0, 0, 1, 0);
        } else if (side == 2) {
            blockTransform.postRotate(90 * 1, 0, 1, 0);
            blockTransform.postTranslate(-10, 0, 0);
        }

        return blockTransform;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityPlayer entityPlayer) {
        int metadata;

        if (entityPlayer.rotationYaw >= 45 && entityPlayer.rotationYaw <= 135) {
            metadata = 3;
        } else if (entityPlayer.rotationYaw >= 135 && entityPlayer.rotationYaw <= 225) { //ok
            metadata = 1;
        } else if (entityPlayer.rotationYaw >= 225 && entityPlayer.rotationYaw <= 315) { //ok
            metadata = 2;
        } else {
            metadata = 0;
        }

        world.setBlockMetadata(x, y, z, metadata);
    }
}
