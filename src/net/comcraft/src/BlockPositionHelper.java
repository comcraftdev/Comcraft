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

/**
 *
 * @author Piotr Wójcik (Queader)
 */

/*
 * This is one of the classes I started coding from :)
 */

public class BlockPositionHelper {

    public int x;
    public int y;
    public int z;
    public Block block;

    public BlockPositionHelper(int x, int y, int z, Block block) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }

    public BlockPositionHelper(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Object object) {
        BlockPositionHelper blockPosition = (BlockPositionHelper) object;

        if (blockPosition.x == x && blockPosition.y == y && blockPosition.z == z) {
            return true;
        }
        return false;
    }
}