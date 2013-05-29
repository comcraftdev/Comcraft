/*
 * Copyright (C) 2013 Piotr Wójcik
 * 
 * This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package net.comcraft.src;

import net.comcraft.client.Comcraft;

public class PlayerManagerImp extends PlayerManager {

    private int counter;

    public PlayerManagerImp(Comcraft cc) {
        super(cc);
    }

    public static void clickBlockCreative(Comcraft cc, PlayerManager playerController, int x, int y, int z, int side) {
        playerController.onPlayerDestroyBlock(x, y, z, side);
    }

    public void clickBlock(int x, int y, int z, int side) {
        cc.vibrate(50);

        clickBlockCreative(cc, this, x, y, z, side);
        counter = 5;
    }

    public void onPlayerDamageBlock(int x, int y, int z, int side) {
        counter--;

        if (counter <= 0) {
            counter = 5;
            clickBlockCreative(cc, this, x, y, z, side);
        }
    }

    public void resetBlockRemoving() {
    }

    public float getBlockReachDistance() {
        return 18f;
    }

    public boolean onPlayerRightClick(EntityPlayer entityplayer, World world, InvItemStack itemstack, int x, int y, int z, int side) {
        int i = world.getBlockID(x, y, z);

        if (i > 0 && i != 255 && Block.blocksList[i].blockActivated(world, x, y, z, entityplayer, itemstack)) {
            return true;
        }

        if (itemstack == null) {
            return false;
        } else {
            cc.vibrate(50);

            int damage = itemstack.getItemDamage();
            int stackSize = itemstack.stackSize;
            boolean flag = itemstack.useItem(entityplayer, world, x, y, z, side);
            itemstack.setItemDamage(damage);
            itemstack.stackSize = stackSize;
            return flag;
        }
    }
}
