/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.util.Random;
import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockPresent extends Block {

    private Random rand;
    private boolean boomable;
    
    public BlockPresent(int id, int index, boolean boomable) {
        super(id, index);
        
        this.boomable = boomable;
        rand = new Random();
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

    public VertexBuffer[][][][] getBlockVertexBuffer(World world, int x, int y, int z) {
        return ModelPresent.vertexBuffer;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelPresent.indexBuffer;
    }

    public boolean isNormal() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x + 0.2f, y + 0f, z + 0.2f, x + 0.8f, y + 0.5f, z + 0.8f);
    }

    private void addBoomEfects(World world, int x, int y, int z) {
        int particles = rand.nextInt(10) + 30;

        for (int n = 0; n < particles; ++n) {
            float xD = rand.nextFloat() * 7 - 3.5f + x;
            float yD = rand.nextFloat() * 5 - 2.5f + y;
            float zD = rand.nextFloat() * 7 - 3.5f + z;

            world.cc.render.renderEffects.addEffect(new EffectExplosion(world.cc, "misc/explosion.png", xD * 10, yD * 10, zD * 10));
        }
    }
    
    private void presentBoom(World world, int x, int y, int z) {
        world.cc.vibrate(200);

        world.setBlockID(x, y, z, 0);

        addBoomEfects(world, x, y, z);
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer) {
        if (!boomable) {
            return false;
        }
        
        presentBoom(world, x, y, z);

        return true;
    }
}