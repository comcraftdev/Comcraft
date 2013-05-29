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

public abstract class PlayerManager {

    protected Comcraft cc;

    public PlayerManager(Comcraft cc) {
        this.cc = cc;
    }

    public abstract void clickBlock(int x, int y, int z, int side);

    public boolean onPlayerDestroyBlock(int x, int y, int z, int side) {
        World world = cc.world;
        Block block = Block.blocksList[world.getBlockID(x, y, z)];

        if (block == null) {
            return false;
        }
        
        return world.setBlockN(x, y, z, 0);
    }

    public abstract void onPlayerDamageBlock(int x, int y, int z, int side);

    public abstract void resetBlockRemoving();

    public abstract float getBlockReachDistance();

    public void updateController() {
    }

    public abstract boolean onPlayerRightClick(EntityPlayer entityplayer, World world, InvItemStack itemstack, int x, int y, int z, int side);

    public EntityPlayer createPlayer() {
        return new EntityPlayer(cc);
    }
}
