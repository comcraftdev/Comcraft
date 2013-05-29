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

import javax.microedition.m3g.Transform;
import javax.microedition.m3g.VertexBuffer;

public class ChunkPiece {

    private Transform transform;
    private int xSize;
    private int ySize;
    private int zSize;
    private long lastTick;
    private boolean[] doneSides;
    private Block block;

    public ChunkPiece(int xPos, int yPos, int zPos, int xSize, int ySize, int zSize, Block block) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;

        this.block = block;
        
        transform = new Transform();
        transform.postTranslate(xPos * 10, yPos * 10, zPos * 10);
    }
    
    public int getSizeX() {
        return xSize;
    }
    
    public int getSizeY() {
        return ySize;
    }
    
    public int getSizeZ() {
        return zSize;
    }

    public VertexBuffer getVertexBuffer(World world, int side) {
        return block.getBlockVertexBufferPieced(world, 0, 0, 0)[xSize][ySize][zSize][side];
    }

    public Transform getTransform() {
        return transform;
    }

    public boolean needsRender(long currentTick, int side) {
        if (currentTick > lastTick) {
            doneSides = new boolean[6];
            doneSides[side] = true;

            lastTick = currentTick;

            return true;
        } else if (!doneSides[side]) {
            doneSides[side] = true;

            return true;
        } else {
            return false;
        }
    }
}
