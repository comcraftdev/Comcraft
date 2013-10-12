package net.comcraft.src;

public class InvItemBlock extends InvItem {

    protected int blockID;

    public InvItemBlock(int id) {
        super(id);
        blockID = id + 256;
    }
    
    public InvItemBlock(int id, int index) {
        super(id, index);
        blockID = id + 256;
    }

    public String getItemUsingSound() {
        return Block.blocksList[blockID].getBlockPlacingSound();
    }
    
    public int getIconIndex() {
        return iconIndex == -1 ? blockID + 512 : iconIndex;
    }
    
    public boolean onItemUse(InvItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side) {
        if (side == 0) {
            ++z;
        } else if (side == 1) {
            --z;
        } else if (side == 2) {
            ++x;
        } else if (side == 3) {
            --x;
        } else if (side == 4) {
            ++y;
        } else if (side == 5) {
            --y;
        }

        if (itemStack.stackSize == 0) {
            return false;
        }
        if (world.canBlockBePlacedAt(blockID, x, y, z, side)) {
            if (world.setBlockAndMetadataN(x, y, z, blockID, 0)) {
                if (world.getBlockID(x, y, z) == blockID) {
                    Block.blocksList[blockID].onBlockPlaced(world, x, y, z, side);
                    Block.blocksList[blockID].onBlockPlacedBy(world, x, y, z, entityPlayer);
                }

                itemStack.stackSize--;
            }
            return true;
        } else {
            return false;
        }
    }

    public String getItemName() {
        return Block.blocksList[blockID].getBlockName();
    }
}
