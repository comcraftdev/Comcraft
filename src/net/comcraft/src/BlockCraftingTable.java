/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */

package net.comcraft.src;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockCraftingTable extends Block {
    
    private int indexSide1;
    private int indexSide2;
    private int indexTop;
    private int indexBottom;
    
    public BlockCraftingTable(int id, int indexSide1, int indexSide2, int indexTop, int indexBottom) {
        super (id, 0);
        
        this.indexSide1 = indexSide1;
        this.indexSide2 = indexSide2;
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
    }
    
    public int[] getUsedTexturesList() {
        return new int[] {indexTop, indexBottom, indexSide1, indexSide2};
    }
    
    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (side == 4) {
            return indexTop;
        } else if (side == 5) {
            return indexBottom;
        } else if (side == 0 || side == 2) {
            return indexSide1;
        } else {
            return indexSide2;
        }
    }
}