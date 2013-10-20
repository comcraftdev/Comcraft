package net.comcraft.src;

import java.util.Random;

public class BlockEmoticon extends Block {

    private int indexFront;
    private int indexSide;
    private int indexTop;
    private int indexBottom;
    protected int[] indexActivated;
    private Random random;

    public BlockEmoticon(int id, int indexFront, int indexSide, int indexTop, int indexBottom, int[] indexActivated) {
        super(id);

        this.indexFront = indexFront;
        this.indexSide = indexSide;
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
        this.indexActivated = indexActivated;
        random = new Random();
    }

    public int[] getUsedTexturesList() {
        int[] tex = {indexFront, indexSide, indexTop, indexBottom};

        int[] list = new int[tex.length + indexActivated.length];

        for (int n = 0; n < list.length; ++n) {
            if (n < tex.length) {
                list[n] = tex[n];
            } else {
                list[n] = indexActivated[n - tex.length];
            }
        }

        return list;
    }

    protected int getActivatedTextureIndex(World world, int x, int y, int z) {
        if (world == null) {
            return 0;
        }

        return (world.getBlockMetadata(x, y, z) >> 3);
    }

    private int getSide(World world, int x, int y, int z) {
        if (world == null) {
            return 0;
        }

        return world.getBlockMetadata(x, y, z) & 3;
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, InvItemStack itemStack) {
        int index = getNextTextureIndex(world, x, y, z);
        
        world.setBlockMetadata(x, y, z, getMetadataToSave(getSide(world, x, y, z), true, index));

        return true;
    }

    protected int getNextTextureIndex(World world, int x, int y, int z) {
        int nextIndex;
        
        do {
            nextIndex = random.nextInt(indexActivated.length);
        } while (nextIndex == getActivatedTextureIndex(world, x, y, z));
        
        return nextIndex;
    }

    public boolean canBePieced() {
        return false;
    }

    public int getRenderType() {
        return 0;
    }

    private boolean isActivated(World world, int x, int y, int z) {
        if (world == null) {
            return false;
        }

        return ((world.getBlockMetadata(x, y, z) >> 2) & 1) == 1;
    }

    private int getMetadataToSave(int side, boolean activated, int index) {
        int metadata = 0;

        int act = activated ? 1 : 0;

        metadata = metadata | side;
        metadata = metadata | (act << 2);
        metadata = metadata | (index << 3);

        return metadata;
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (side == getSide(world, x, y, z)) {
            if (isActivated(world, x, y, z)) {
                return indexActivated[getActivatedTextureIndex(world, x, y, z)];
            }
            return indexFront;
        } else if (side == 4) {
            return indexTop;
        } else if (side == 5) {
            return indexBottom;
        } else {
            return indexSide;
        }
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityPlayer entityPlayer) {
        int side;

        if (entityPlayer.rotationYaw >= 45 && entityPlayer.rotationYaw <= 135) {
            side = 3;
        } else if (entityPlayer.rotationYaw >= 135 && entityPlayer.rotationYaw <= 225) { //ok
            side = 1;
        } else if (entityPlayer.rotationYaw >= 225 && entityPlayer.rotationYaw <= 315) { //ok
            side = 2;
        } else {
            side = 0;
        }

        world.setBlockMetadata(x, y, z, getMetadataToSave(side, isActivatedByDefault(), 0));
    }
    
    protected boolean isActivatedByDefault() {
        return false;
    }
}