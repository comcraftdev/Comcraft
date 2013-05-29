/*
 * Copyright (C) 2013 Piotr Wójcik
 *
 */

package net.comcraft.src;

/**
 *
 * @author Piotr Wójcik
 */
public class InvItemHammer extends InvItem {

    public InvItemHammer(int id, int index) {
        super(id, index);
    }
    
    public boolean onItemUse(InvItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side) {
        if (world.isAirBlock(x, y, z)) {
            return false;
        }
        
        world.setBlockID(x, y, z, 0);
        
        return true;
    }
}