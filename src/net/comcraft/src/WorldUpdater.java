package net.comcraft.src;

import java.util.Vector;

/**
 *
 * @author Piotr Wójcik (Queader)
 */
public final class WorldUpdater {

    public static final int tickTime = 800;
    private World world;
    private Vector updatableBlocksList;
    private int outstandingTime;

    public WorldUpdater(World world) {
        this.world = world;

        updatableBlocksList = new Vector(512);
    }

    public void addBlockUpdatable(int x, int y, int z) {
        updatableBlocksList.addElement(new BlockPositionHelper(x, y, z));
    }

    public void removeBlockUpdatable(int x, int y, int z) {
        updatableBlocksList.removeElement(new BlockPositionHelper(x, y, z));
    }

    public void updateWorld(int dt) {
        int time = dt + outstandingTime;

        int counts = time / tickTime;

        for (int i = 0; i < counts; ++i) {
            time -= tickTime;

            int prevSize = updatableBlocksList.size();

            for (int n = 0; n < prevSize && n < updatableBlocksList.size(); ++n) {
                BlockPositionHelper blockPosition = (BlockPositionHelper) updatableBlocksList.elementAt(n);

                Block block = Block.blocksList[world.getBlockID(blockPosition.x, blockPosition.y, blockPosition.z)];

                if (block != null) {
                    block.tickBlock(world, blockPosition.x, blockPosition.y, blockPosition.z);
                }
            }
        }

        outstandingTime = time;
    }
}