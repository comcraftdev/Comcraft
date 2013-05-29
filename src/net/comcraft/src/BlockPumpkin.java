package net.comcraft.src;

public class BlockPumpkin extends Block {

    private int indexFront;
    private int indexSide;
    private int indexTop;
    private int indexBottom;
    
    public BlockPumpkin(int id, int indexFront, int indexSide, int indexTop, int indexBottom) {
        super(id);
        
        this.indexFront = indexFront;
        this.indexSide = indexSide;
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
    }

    public int[] getUsedTexturesList() {
        return new int[] {indexFront, indexSide, indexTop, indexBottom};
    }
    
    public boolean canBePieced() {
        return false;
    }

    public int getRenderType() {
        return 0;
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if ((world != null && side == world.getBlockMetadata(x, y, z)) || (world == null && side == 0)) {
            return indexFront;
        } else if (side == 4) {
            return indexTop;
        } else if (side == 5) {
            return indexBottom;
        } else {
            return indexSide;
        }
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
