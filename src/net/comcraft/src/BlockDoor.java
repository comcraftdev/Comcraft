/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockDoor extends Block {

    private int indexTop;
    private int indexBottom;

    public BlockDoor(int id, int indexTop, int indexBottom) {
        super(id);

        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
    }

    public boolean doesBlockDestroyGrass() {
        return false;
    }

    public boolean canBePieced() {
        return false;
    }

    public boolean canBePiecedVertically() {
        return false;
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return true;
    }

    public boolean isNormal() {
        return false;
    }

    public int[] getUsedTexturesList() {
        return new int[]{indexTop, indexBottom};
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (world == null) {
            return indexTop;
        }

        if (getDoorSide(world, x, y, z) == 4) {
            return indexBottom;
        } else {
            return indexTop;
        }
    }

    public Transform getBlockTransform(World world, int x, int y, int z, Transform transform, int side) {
        if (world == null) {
            return transform;
        }

        Transform blockTransform = new Transform();
        blockTransform.set(transform);

        if (isDoorOpended(world, x, y, z)) {
            if (getDoorSide(world, x, y, z) == 5) {
                y -= 1;
            }
            
            if ((!world.isAirBlock(x - 1, y, z) && !world.isAirBlock(x + 1, y, z)) || world.getBlockID(x - 1, y, z) == this.blockID) {
                blockTransform.postRotate(90, 0, 1, 0);
                blockTransform.postTranslate(-10, 0, 5);
            } else if (world.getBlockID(x + 1, y, z) == this.blockID) {
                blockTransform.postRotate(90, 0, 1, 0);
                blockTransform.postTranslate(-10, 0, -5);
            } else if (world.getBlockID(x, y, z + 1) == this.blockID) {
                blockTransform.postTranslate(0, 0, -5);
            } else {
                blockTransform.postTranslate(0, 0, 5);
            }
            
            if (getDoorSide(world, x, y, z) == 5) {
                y += 1;
            }
        } else {
            if (getDoorSide(world, x, y, z) == 5) {
                y -= 1;
            }
            
            if ((!world.isAirBlock(x - 1, y, z) && !world.isAirBlock(x + 1, y, z)) || world.getBlockID(x - 1, y, z) == this.blockID || world.getBlockID(x + 1, y, z) == this.blockID) {
            } else {
                blockTransform.postRotate(90, 0, 1, 0);
                blockTransform.postTranslate(-10, 0, 0);
            }
            
            if (getDoorSide(world, x, y, z) == 5) {
                y += 1;
            }
        }

        return blockTransform;
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer) {
        int side = getDoorSide(world, x, y, z);

        boolean value = !isDoorOpended(world, x, y, z);

        world.setBlockMetadata(x, y, z, getDoorMetadataToSave(side, value));

        if (side == 4) {
            world.setBlockMetadata(x, y + 1, z, getDoorMetadataToSave(getDoorSide(world, x, y + 1, z), value));
        } else if (side == 5) {
            world.setBlockMetadata(x, y - 1, z, getDoorMetadataToSave(getDoorSide(world, x, y - 1, z), value));
        }

        return true;
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        int side = getDoorSide(world, x, y, z);

        if (side == 4) {
            world.setBlockID(x, y + 1, z, 0);
        } else if (side == 5) {
            world.setBlockID(x, y - 1, z, 0);
        }
    }

    private int getDoorMetadataToSave(int side, boolean opended) {
        return side | (opended ? 16 : 0);
    }

    private int getDoorSide(World world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) & 15);
    }

    private boolean isDoorOpended(World world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) >> 4) == 1;
    }

    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        if (super.canPlaceBlockAt(world, x, y - 1, z)) {
            world.setBlockMetadata(x, y, z, getDoorMetadataToSave(5, false));

            world.setBlockAndMetadata(x, y - 1, z, blockID, getDoorMetadataToSave(4, false));
        } else if (super.canPlaceBlockAt(world, x, y + 1, z)) {
            world.setBlockMetadata(x, y, z, getDoorMetadataToSave(4, false));

            world.setBlockAndMetadata(x, y + 1, z, blockID, getDoorMetadataToSave(5, false));
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        if (isDoorOpended(world, x, y, z)) {
            if (getDoorSide(world, x, y, z) == 5) {
                y -= 1;
            }
            
            if ((!world.isAirBlock(x - 1, y, z) && !world.isAirBlock(x + 1, y, z)) || world.getBlockID(x - 1, y, z) == this.blockID) {
                return AxisAlignedBB.getBoundingBox(x + 0.8f, y, z, x + 1, y + 1, z + 1);
            } else if (world.getBlockID(x + 1, y, z) == this.blockID) {
                return AxisAlignedBB.getBoundingBox(x, y, z, x + 0.2f, y + 1, z + 1);
            } else if (world.getBlockID(x, y, z + 1) == this.blockID) {
                return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 0.2f);
            } else {
                return AxisAlignedBB.getBoundingBox(x, y, z + 0.8f, x + 1, y + 1, z + 1);
            }
        } else {
            return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 1f, z + 1f);
        }
    }

    public int getRenderType() {
        return 5;
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        return ModelDoor.vertexBuffer;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelDoor.indexBuffer;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (!super.canPlaceBlockAt(world, x, y, z)) {
            return false;
        }

        if (super.canPlaceBlockAt(world, x, y - 1, z)) {
            return true;
        }
        if (super.canPlaceBlockAt(world, x, y + 1, z)) {
            return true;
        }

        return false;
    }
}