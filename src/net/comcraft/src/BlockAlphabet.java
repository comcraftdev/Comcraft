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

public class BlockAlphabet extends BlockEmoticon {
    
    public BlockAlphabet(int id, int indexFront, int indexSide, int indexTop, int indexBottom, int[] indexActivated) {
        super(id, indexFront, indexSide, indexTop, indexBottom, indexActivated);
    }
    
    protected int getNextTextureIndex(World world, int x, int y, int z) {
        int nextIndex = getActivatedTextureIndex(world, x, y, z) + 1;
        
        if (nextIndex >= indexActivated.length) {
            nextIndex = 0;
        }
        
        return nextIndex;
    }
    
    protected boolean isActivatedByDefault() {
        return true;
    }

}