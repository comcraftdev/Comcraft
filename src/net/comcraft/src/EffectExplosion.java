/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Sprite3D;
import net.comcraft.client.Comcraft;

/**
 *
 * @author Piotr Wójcik
 */
public class EffectExplosion extends Effect {

    private static Appearance appearance;
    private static Sprite3D[] explosionSprites3D = new Sprite3D[16];
    private Random rand = new Random();

    public EffectExplosion(Comcraft cc, String path, float x, float y, float z) {
        super(cc, x, y, z);

        fullTime = (int) (rand.nextFloat() * 500 + 500);
        aliveTime = fullTime;

        if (appearance == null) {
            initEffect(path);
        }
    }

    private void initEffect(String path) {
        appearance = new Appearance();

        Image image = cc.textureProvider.getImage(path);

        int width = image.getWidth() / 4;
        int height = image.getHeight() / 4;

        for (int yI = 0; yI < 4; ++yI) {
            for (int xI = 0; xI < 4; ++xI) {
                int trans = rand.nextInt(5);

                if (trans == 0) {
                    trans = Sprite.TRANS_NONE;
                } else if (trans == 1) {
                    trans = Sprite.TRANS_ROT180;
                } else if (trans == 2) {
                    trans = Sprite.TRANS_ROT90;
                } else if (trans == 3) {
                    trans = Sprite.TRANS_ROT270;
                } else {
                    trans = Sprite.TRANS_MIRROR;
                }

                Image frame = Image.createImage(image, xI * width, yI * height, width, height, trans);

                Image2D image2D = new Image2D(Image2D.RGBA, frame);

                explosionSprites3D[yI * 4 + xI] = new Sprite3D(true, image2D, appearance);
            }
        }
    }

    public void renderEffect() {
        int frame = 15 - (int) ((float) aliveTime / fullTime * 16f);

        if (frame > 15) {
            frame = 15;
        }

        g3d.render(explosionSprites3D[frame], getTransform());
    }

    public boolean isAlive() {
        return aliveTime > 0;
    }

    public void tickEffect(int tickTime) {
        aliveTime -= tickTime;
    }
}