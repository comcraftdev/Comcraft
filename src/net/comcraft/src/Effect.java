/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Transform;
import net.comcraft.client.Comcraft;

/**
 *
 * @author Piotr Wójcik
 */
public abstract class Effect {

    protected Comcraft cc;
    protected Graphics3D g3d;
    private Transform transform;
    protected float x;
    protected float y;
    protected float z;
    protected int aliveTime;
    protected int fullTime;

    public Effect(Comcraft cc, float x, float y, float z) {
        this.cc = cc;
        g3d = Graphics3D.getInstance();
        
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public abstract void renderEffect();

    public abstract void tickEffect(int tickTime);

    public abstract boolean isAlive();

    public final Transform getTransform() {
        transform = new Transform();
        
        transform.postTranslate(x, y, z);
        transform.postScale(10, 10, 10);

        return transform;
    }
}