/*
 * Copyright (C) 2013 Piotr Wójcik
 *
 */

package net.comcraft.src;

public class BlockAnimal extends Block {
    
    private int indexFront;
    private int indexBack;
    private int indexSides;
    private int indexTop;
    private int indexBottom;
    
    public BlockAnimal(int id, int indexFront, int indexBack, int indexSides, int indexTop, int indexBottom) {
        super(id);
        
        this.indexFront = indexFront;
        this.indexBack = indexBack;
        this.indexSides = indexSides;
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
    }

    public int[] getUsedTexturesList() {
        return new int[]{indexFront, indexBack, indexSides, indexTop, indexBottom};
    }
    
    public int getRenderType() {
        return 0;
    }
    
    public boolean canBePieced() {
        return false;
    }
    
    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (world == null && side == 0) {
            return indexFront;
        }
        
        if (world == null && side == 4) {
            return indexTop;
        }
        
        if (world == null) {
            return indexSides;
        }
        
        int blockSide = world.getBlockMetadata(x, y, z);
        
        if (side == blockSide) {
            return indexFront;
        }
        if (side == 4) {
            return indexTop;
        }
        if (side == 5) {
            return indexBottom;
        }
        if ((side == 0 && blockSide == 1) || (side == 1 && blockSide == 0) || (side == 2 && blockSide == 3) || (side == 3 && blockSide == 2)) {
            return indexBack;
        }
        
        return indexSides;
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