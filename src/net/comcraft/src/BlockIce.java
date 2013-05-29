package net.comcraft.src;

public class BlockIce extends Block {

    public BlockIce(int id, int index) {
        super(id, index);
    }
    
    public boolean isNormal() {
        return false;
    }
    
    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return world.getBlockID(x, y, z) == 0 || (!Block.blocksList[world.getBlockID(x, y, z)].isNormal() && world.getBlockID(x, y, z) != blockID);
    }
    
    public boolean doesBlockDestroyGrass() {
        return false;
    }
}
