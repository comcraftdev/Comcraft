/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.util.Vector;
import net.comcraft.client.Comcraft;

/**
 *
 * @author Piotr Wójcik
 */
public final class RenderEffects {

    private Comcraft cc;
    private Vector effectsList = new Vector(50);

    public RenderEffects(Comcraft cc) {
        this.cc = cc;
    }

    public void addEffect(Effect effect) {
        effectsList.addElement(effect);
    }

    public void tickEffects() {
        for (int n = 0; n < effectsList.size(); ++n) {
            Effect effect = (Effect) effectsList.elementAt(n);

            effect.tickEffect(cc.tickTime);

            if (!effect.isAlive()) {
                effectsList.removeElementAt(n);
                --n;
            }
        }
    }

    public void renderEffects() {
        for (int n = 0; n < effectsList.size(); ++n) {
            Effect effect = (Effect) effectsList.elementAt(n);

            effect.renderEffect();
        }
    }
}