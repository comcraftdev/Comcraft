/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comcraft.src;

/**
 *
 * @author Piotrek
 */
public class BlockGrass extends BlockSidesTopBottom {

    public BlockGrass(int id, int indexSide, int indexTop, int indexBottom) {
        super(id, indexSide, indexTop, indexBottom);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        if (world.getBlockID(x, y + 1, z) != 0 && world.getBlock(x, y + 1, z).doesBlockDestroyGrass()) {
            world.setBlockID(x, y, z, Block.getBlock("dirt").blockID);
        }
    }
}
