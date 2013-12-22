/*
 * Copyright (C) 2013 Piotr Wójcik
 *
 */
package net.comcraft.src;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockDirt extends Block {

    public BlockDirt(int id, int index) {
        super(id, index);
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, InvItemStack itemStack) {
        if (itemStack.itemID == InvItem.seeds.shiftedIndex) {
            world.setBlockID(x, y, z, Block.getBlock("grass").blockID);
            
            return true;
        }

        return false;
    }
}