/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.util.Random;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockTNT extends Block {

    private int indexSides;
    private int indexTop;
    private int indexBottom;
    private int indexSidesExp;
    private int indexTopExp;
    private int indexBottomExp;
    private int boomRadius;
    private int boomTime;
    private Random rand = new Random();

    public BlockTNT(int id, int indexSides, int indexTop, int indexBottom, int indexSidesExp, int indexTopExp, int indexBottomExp, int boomRange, int boomTime) {
        super(id);

        this.indexSides = indexSides;
        this.indexTop = indexTop;
        this.indexBottom = indexBottom;
        this.indexSidesExp = indexSidesExp;
        this.indexTopExp = indexTopExp;
        this.indexBottomExp = indexBottomExp;
        this.boomRadius = boomRange;
        this.boomTime = boomTime;
    }

    public int[] getUsedTexturesList() {
        return new int[] { indexSides, indexTop, indexBottom, indexSidesExp, indexTopExp, indexBottomExp };
    }

    public int getRenderType() {
        return 0;
    }

    public boolean canBePieced() {
        return false;
    }

    public boolean canBePiecedVertically() {
        return false;
    }

    private void addBoomEfects(World world, int x, int y, int z) {
        int particles = rand.nextInt(3 * boomRadius) + 10 * boomRadius;

        for (int n = 0; n < particles; ++n) {
            float xD = rand.nextFloat() * (boomRadius * 2 + 1) - (boomRadius * 2 + 1) / 2f + x;
            float yD = rand.nextFloat() * (boomRadius * 2 - 1) - (boomRadius * 2 - 1) / 2f + y;
            float zD = rand.nextFloat() * (boomRadius * 2 + 1) - (boomRadius * 2 + 1) / 2f + z;

            world.cc.render.renderEffects.addEffect(new EffectExplosion(world.cc, "misc/explosion.png", xD * 10, yD * 10, zD * 10));
        }
    }

    private void tntBooom(World world, int x, int y, int z) {
        world.cc.vibrate(200);

        world.setBlockID(x, y, z, 0);

        boolean waterFound = false;

        for (int zI = -1; zI <= 1; ++zI) {
            for (int yI = -1; yI <= 1; ++yI) {
                for (int xI = -1; xI <= 1; ++xI) {
                    if (world.getBlockID(x + xI, y + yI, z + zI) == Block.getBlock("water").blockID) {
                        waterFound = true;
                    }
                }
            }
        }

        if (!waterFound) {
            for (int zI = -boomRadius; zI <= boomRadius; ++zI) {
                for (int yI = -boomRadius; yI <= boomRadius; ++yI) {
                    for (int xI = -boomRadius; xI <= boomRadius; ++xI) {
                        if ((zI * zI + yI * yI * xI * xI) > boomRadius * boomRadius) {
                            continue;
                        }

                        if (world.getBlockID(x + xI, y + yI, z + zI) == Block.getBlock("tnt").blockID
                                || world.getBlockID(x + xI, y + yI, z + zI) == Block.getBlock("tntWeak").blockID
                                || world.getBlockID(x + xI, y + yI, z + zI) == Block.getBlock("tntStrong").blockID) {
                            Block.blocksList[blockID].blockActivated(world, x + xI, y + yI, z + zI, null, null);
                        } else {
                            world.setBlockID(x + xI, y + yI, z + zI, 0);
                        }
                    }
                }
            }
        }

        addBoomEfects(world, x, y, z);
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, InvItemStack itemStack) {
        if (itemStack != null && itemStack.itemID != InvItem.detonator.shiftedIndex) {
            return false;
        }

        int time = 1;

        if (entityplayer == null) {
            time = boomTime;
        }

        world.setBlockMetadata(x, y, z, time);

        world.getWorldUpdater().addBlockUpdatable(x, y, z);

        return true;
    }

    public void tickBlock(World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);

        if (metadata >= boomTime) {
            tntBooom(world, x, y, z);

            world.getWorldUpdater().removeBlockUpdatable(x, y, z);
        } else {
            world.setBlockMetadata(x, y, z, metadata + 1);
        }
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        if (world != null && world.getBlockMetadata(x, y, z) > 0) {
            if (side == 4) {
                return indexTopExp;
            } else if (side == 5) {
                return indexBottomExp;
            } else {
                return indexSidesExp;
            }
        } else {
            if (side == 4) {
                return indexTop;
            } else if (side == 5) {
                return indexBottom;
            } else {
                return indexSides;
            }
        }
    }
}