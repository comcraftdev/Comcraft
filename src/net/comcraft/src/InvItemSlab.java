package net.comcraft.src;

public class InvItemSlab extends InvItemBlock {

    private Block doubleSlab;

    public InvItemSlab(int id, Block doubleSlab) {
        super(id);

        this.doubleSlab = doubleSlab;
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
            Block block = Block.blocksList[blockID];

            if (world.getBlockID(x, y, z) == blockID && world.setBlockAndMetadataN(x, y, z, doubleSlab.blockID, 0)) {
                if (world.getBlockID(x, y, z) == blockID) {
                    Block.blocksList[blockID].onBlockPlaced(world, x, y, z, side);
                    Block.blocksList[blockID].onBlockPlacedBy(world, x, y, z, entityPlayer);
                }

                itemStack.stackSize--;
            } else if (world.setBlockAndMetadataN(x, y, z, blockID, 0)) {
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
}
