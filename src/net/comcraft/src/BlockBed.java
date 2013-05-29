/*
 * Copyright (C) 2013 Piotr Wójcik
 *
 */
package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockBed extends Block {

    private int indexFrontTop;
    private int indexBackTop;
    private int indexBackBack;
    private int indexFrontFront;
    private int indexBackSides;
    private int indexFrontSides;
    private int indexBackSidesInverted;
    private int indexFrontSidesInverted;

    public BlockBed(int id, int indexFrontTop, int indexBackTop, int indexBackBack, int indexFrontFront, int indexBackSides, int indexFrontSides, int indexBackSidesInverted, int indexFrontSidesInverted) {
        super(id);

        this.indexFrontTop = indexFrontTop;
        this.indexBackTop = indexBackTop;
        this.indexBackBack = indexBackBack;
        this.indexFrontFront = indexFrontFront;
        this.indexBackSides = indexBackSides;
        this.indexFrontSides = indexFrontSides;
        this.indexFrontSidesInverted = indexFrontSidesInverted;
        this.indexBackSidesInverted = indexBackSidesInverted;
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return !world.isBlockNormal(x, y, z) && world.getBlock(x, y, z) != this;
    }
    
    public int[] getUsedTexturesList() {
        return new int[]{indexFrontTop, indexBackTop, indexBackBack, indexFrontFront, indexBackSides, indexFrontSides, indexFrontSidesInverted, indexBackSidesInverted};
    }

    public int getRenderType() {
        return 9;
    }

    public boolean isNormal() {
        return false;
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        return ModelPieceSlab.vertexBuffer[0][0][0];
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelPieceSlab.indexBuffer;
    }

    public boolean canBePiecedVertically() {
        return false;
    }

    public boolean canBePieced() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 0.5f, z + 1f);
    }

    private int getBedMetadataToSave(int side, boolean head) {
        int h = head ? 1 : 0;

        int n = side;

        return n | (h << 3);
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (world == null) {
            return indexFrontTop;
        }

        int bedSide = getBedSide(world, x, y, z);

        if (isBedHead(world, x, y, z)) {
            if (side == bedSide) {
                return indexFrontFront;
            }

            if (side == 4 || side == 5) {
                return indexFrontTop;
            }

            if (bedSide == 1 && (side == 2)) {
                return indexFrontSidesInverted;
            }
            if (bedSide == 3 && (side == 1)) {
                return indexFrontSidesInverted;
            }
            if (bedSide == 2 && (side == 0)) {
                return indexFrontSidesInverted;
            }
            if (bedSide == 0 && (side == 3)) {
                return indexFrontSidesInverted;
            }

            return indexFrontSides;
        } else {
            if (side == bedSide) {
                return indexBackBack;
            }

            if (side == 4 || side == 5) {
                return indexBackTop;
            }

            if (bedSide == 0 && (side == 2)) {
                return indexBackSidesInverted;
            }
            if (bedSide == 2 && (side == 1)) {
                return indexBackSidesInverted;
            }
            if (bedSide == 3 && (side == 0)) {
                return indexBackSidesInverted;
            }
            if (bedSide == 1 && (side == 3)) {
                return indexBackSidesInverted;
            }

            return indexBackSides;
        }
    }

    private void destroyBlockBed(World world, int x, int y, int z) {
        int side = getBedSide(world, x, y, z);

        world.setBlockID(x, y, z, 0);

        int nX = x;
        int nZ = z;

        if (side == 3) {
            --nX;
        } else if (side == 1) {
            ++nZ;
        } else if (side == 2) {
            ++nX;
        } else {
            --nZ;
        }

        if (world.getBlockID(nX, y, nZ) == this.blockID) {
            world.setBlockID(nX, y, nZ, 0);
        }
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        if (world.isAirBlock(x, y - 1, z)) {
            destroyBlockBed(world, x, y, z);
        }
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        destroyBlockBed(world, x, y, z);
    }

    private int getBedSide(World world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) & 3);
    }

    private boolean isBedHead(World world, int x, int y, int z) {
        return ((world.getBlockMetadata(x, y, z) >> 3)) == 1;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityPlayer entityPlayer) {
        int side;

        if (entityPlayer.rotationYaw >= 45 && entityPlayer.rotationYaw <= 135) {
            side = 3; //x zmniejsza
        } else if (entityPlayer.rotationYaw >= 135 && entityPlayer.rotationYaw <= 225) { //ok
            side = 1; //z zwieksza
        } else if (entityPlayer.rotationYaw >= 225 && entityPlayer.rotationYaw <= 315) { //ok
            side = 2; //x zwieksza
        } else {
            side = 0; //z zmniejsza
        }

        world.setBlockMetadata(x, y, z, getBedMetadataToSave(side, true));

        int nX = x;
        int nZ = z;

        if (side == 3) {
            --nX;
        } else if (side == 1) {
            ++nZ;
        } else if (side == 2) {
            ++nX;
        } else {
            --nZ;
        }

        if (!super.canPlaceBlockAt(world, nX, y, nZ)) {
            world.setBlockID(x, y, z, 0);
            return;
        }

        int nSide;

        if (side == 3) {
            nSide = 2;
        } else if (side == 1) {
            nSide = 0;
        } else if (side == 2) {
            nSide = 3;
        } else {
            nSide = 1;
        }

        world.setBlockAndMetadata(nX, y, nZ, this.blockID, getBedMetadataToSave(nSide, false));
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (!super.canPlaceBlockAt(world, x, y, z)) {
            return false;
        }

        if (super.canPlaceBlockAt(world, x + 1, y, z)) {
            return true;
        }
        if (super.canPlaceBlockAt(world, x - 1, y, z)) {
            return true;
        }
        if (super.canPlaceBlockAt(world, x, y, z + 1)) {
            return true;
        }
        if (super.canPlaceBlockAt(world, x, y, z - 1)) {
            return true;
        }

        return false;
    }
}