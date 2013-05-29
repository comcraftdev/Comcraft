package net.comcraft.src;

public class BlockFurnance extends Block {

    private int indexFront;
    private int indexSide;
    private int indexTop;
    private int indexBottom;
    private int indexActivated;

    public BlockFurnance(int id, int indexFront, int indexSide, int indexTop, int indexBottom, int indexActivated) {
        super(id);

        this.indexFront = indexFront;
        this.indexSide = indexSide;
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
        this.indexActivated = indexActivated;
    }
    
    public int[] getUsedTexturesList() {
        return new int[] {indexFront, indexSide, indexTop, indexBottom, indexActivated};
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer) {
        changeFurnanceState(world, x, y, z);

        return true;
    }

    public boolean canBePieced() {
        return false;
    }

    public int getRenderType() {
        return 0;
    }

    private boolean isFurnanceActivated(World world, int x, int y, int z) {
        if (world == null) {
            return false;
        }
        
        return (world.getBlockMetadata(x, y, z) >> 4) == 1;
    }

    private void changeFurnanceState(World world, int x, int y, int z) {
        int prevMetadata = world.getBlockMetadata(x, y, z);
        
        int side = prevMetadata & 15;
        int state = (prevMetadata >> 4) == 1 ? 0 : 1;
        
        world.setBlockMetadata(x, y, z, side | (state << 4));
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if ((world != null && side == (world.getBlockMetadata(x, y, z) & 15)) || (world == null && side == 0)) {
            if (isFurnanceActivated(world, x, y, z)) {
                return indexActivated;
            }
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