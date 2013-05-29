package net.comcraft.src;

public class BlockSidesTopBottom extends Block {

    private int indexSide;
    private int indexTop;
    private int indexBottom;
    
    public BlockSidesTopBottom(int id, int indexSide, int indexTop, int indexBottom) {
        super(id);
        
        this.indexSide = indexSide;
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
    }
    
    public int[] getUsedTexturesList() {
        return new int[] {indexSide, indexTop, indexBottom};
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (side == 4) {
            return indexTop;
        } else if (side == 5) {
            return indexBottom;
        } else {
            return indexSide;
        }
    }
}
