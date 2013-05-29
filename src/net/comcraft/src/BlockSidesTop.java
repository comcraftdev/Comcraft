package net.comcraft.src;

public class BlockSidesTop extends Block {

    private int indexSides;
    private int indexTopBottom;

    public BlockSidesTop(int id, int indexSides, int indexTopDown) {
        super(id);

        this.indexSides = indexSides;
        this.indexTopBottom = indexTopDown;
    }

    public int[] getUsedTexturesList() {
        return new int[] {indexSides, indexTopBottom};
    }
    
    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (side > 3) {
            return indexTopBottom;
        } else {
            return indexSides;
        }
    }
}
