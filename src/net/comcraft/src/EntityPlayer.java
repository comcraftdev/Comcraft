/*
 * Copyright (C) 2013 Piotr Wójcik
 * 
 * This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.m3g.Transform;
import net.comcraft.client.Comcraft;

public class EntityPlayer {

    private final float maxSpeed = 3f;
    private Comcraft cc;
    private Vec3D vVec;
    public InventoryPlayer inventory;
    public float xPos;
    public float yPos;
    public float zPos;
    public float rotationYaw = 225;
    public float rotationPitch = 340;
    private float aspect;
    private float h;
    private float w;
    private AxisAlignedBB boundingBox;

    public EntityPlayer(Comcraft cc) {
        this.cc = cc;
        inventory = new InventoryPlayer();
        vVec = new Vec3D();
        xPos = 32;
        yPos = 15;
        zPos = 32;
        aspect = (float) Comcraft.screenWidth / Comcraft.screenHeight;
        h = 2 * (float) Math.tan(Math.toRadians(cc.settings.fov / 2));
        w = aspect * h;
        boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }

    public void setPlayerOnWorldCenter(int worldSize) {
        xPos = worldSize * 4 / 2;
        yPos = 15;
        zPos = worldSize * 4 / 2;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public void moveEntity(int strafe, int up, int forw) {
        float dt = cc.dt;

        if (dt > 0.15f) {
            dt = 0.15f;
        }

        if (vVec.lengthVector() < maxSpeed) {
            Vec3D forward = getLookForw();
            Vec3D right = getLookRight();

            Vec3D newVec = new Vec3D();

            newVec.x += (forward.x * forw + right.x * strafe);
            newVec.y += (up);
            newVec.z += (forward.z * forw + right.z * strafe);

            newVec = newVec.normalize();

            newVec = newVec.crossProduct((1.5f + 9) * dt);

            vVec = vVec.addVector(newVec);
        }
    }

    public void onLivingUpdate(float dt) {
        updateEntityMove(dt);
    }

    private boolean collidesWithWorld(float xPosNew, float yPosNew, float zPosNew) {
        int xP = (int) xPosNew;
        int yP = (int) yPosNew;
        int zP = (int) zPosNew;

        for (int z = -1; z <= 1; ++z) {
            for (int y = -1; y <= 1; ++y) {
                for (int x = -1; x <= 1; ++x) {
                    Block block = cc.world.getBlock(xP + x, yP + y, zP + z);

                    if (block != null && block.collidesWithPlayer()) {
                        AxisAlignedBB blockBoundingBox = cc.world.getBlockBoundingBox(xP + x, yP + y, zP + z);

                        if (blockBoundingBox != null && boundingBox.collidesWith(blockBoundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void updateEntityMove(float dt) {
        if (dt > 0.15f) {
            dt = 0.15f;
        }

        float xPosNew = xPos + vVec.x * dt;
        float yPosNew = yPos - vVec.y * dt;
        float zPosNew = zPos + vVec.z * dt;

        boundingBox.setBounds(xPosNew - 0.2f, yPosNew - 0.2f, zPosNew - 0.2f, xPosNew + 0.2f, yPosNew + 0.2f, zPosNew + 0.2f);

        if (!collidesWithWorld(xPosNew, yPosNew, zPosNew)) {
            xPos = xPosNew;
            yPos = yPosNew;
            zPos = zPosNew;
        } else {
            vVec.setComponents(0, 0, 0);
        }


        Vec3D slowVec = new Vec3D(vVec);
        slowVec = slowVec.normalize();
        slowVec = slowVec.crossProduct(9f * dt);

        Vec3D prevVec = new Vec3D(vVec);

        vVec = slowVec.subtractVector(vVec);

        if (MathHelper.isOpositeSign(vVec.x, prevVec.x)) {
            vVec.setComponents(0, 0, 0);
        } else if (MathHelper.isOpositeSign(vVec.y, prevVec.y)) {
            vVec.setComponents(0, 0, 0);
        } else if (MathHelper.isOpositeSign(vVec.z, prevVec.z)) {
            vVec.setComponents(0, 0, 0);
        }
    }

    public Transform getPlayerTransform() {
        Transform transform = new Transform();
        transform.postTranslate(xPos * 10, yPos * 10, zPos * 10);
        transform.postRotate(rotationYaw, 0, 1, 0);
        transform.postRotate(rotationPitch, 1, 0, 0);

        if (Touch.isTouchSupported()) {
            transform.postRotate(90, 0, 0, 1);
        }

        return transform;
    }

    public Vec3D getLookForw() {
        Vec3D vec3D = getLook();
        vec3D.y = 0;

        return vec3D.normalize();
    }

    public Vec3D getLookRight() {
        return getLook().crossProduct(new Vec3D(0, 1, 0)).normalize();
    }

    public Vec3D getLook() {
        float cosX = (float) Math.cos(Math.toRadians(rotationPitch));
        float lookX = (float) Math.sin(Math.toRadians(rotationYaw)) * cosX;
        float lookY = (float) Math.sin(Math.toRadians(rotationPitch));
        float lookZ = (float) Math.cos(Math.toRadians(rotationYaw)) * cosX;

        return new Vec3D(lookX, lookY, lookZ).normalize();
    }

    public Vec3D getLook(int x, int y) {
        Vec3D dir1 = getLook();
        dir1.normalize();

        Vec3D dir1c = new Vec3D(dir1);
        Vec3D right_dir = getLookRight();

        Vec3D up_dir = dir1c.crossProduct(right_dir);
        up_dir.normalize();

        Vec3D center = new Vec3D(((float) y / Comcraft.screenHeight - 0.5f) * h, (0.5f - (float) x / Comcraft.screenWidth) * w, 0);
        Vec3D centerWS = (right_dir.crossProduct(center.x)).addVector((up_dir.crossProduct(center.y)));
        Vec3D dir2 = (dir1.addVector(centerWS));
        dir2.normalize();

        return dir2;
    }

    public Vec3D getPosition() {
        return new Vec3D(xPos, yPos, zPos);
    }

    public void rotate(float rotationPitch, float rotationYaw) {
        this.rotationYaw -= rotationYaw;
        this.rotationPitch += rotationPitch;

        if (rotationPitch > 85 && rotationPitch < 180) {
            this.rotationPitch = 85;
        }

        if (this.rotationPitch > 360 + 85) {
            this.rotationPitch = 360 + 85;
        }

        if (this.rotationPitch < 360 - 85 && this.rotationPitch > 180) {
            this.rotationPitch = 360 - 85;
        }

        if (this.rotationYaw > 360) {
            this.rotationYaw = this.rotationYaw % 360;
        } else if (this.rotationYaw < 0) {
            this.rotationYaw = 360 + this.rotationYaw;
        }
    }

    public RayObjectPosition rayTrace(float reachDistance) {
        return cc.world.rayTraceBlocks(getPosition(), getLook(), reachDistance);
    }

    public RayObjectPosition rayTrace(float reachDistance, int x, int y) {
        return cc.world.rayTraceBlocks(getPosition(), getLook(x, y), reachDistance);
    }

    public void loadFromDataInputStream(DataInputStream dataInputStream, float worldVersion) throws IOException {
        xPos = dataInputStream.readFloat();
        yPos = dataInputStream.readFloat();
        zPos = dataInputStream.readFloat();
        rotationPitch = dataInputStream.readFloat();
        rotationYaw = dataInputStream.readFloat();

        if (worldVersion == 2f) {
            return;
        }

        int fastSlotSize = dataInputStream.readInt();

        for (int n = 0; n < fastSlotSize; ++n) {
            int id = dataInputStream.readInt();

            inventory.setItemStackAt(n, new InvItemStack(id));
        }
    }

    public void writeToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(xPos);
        dataOutputStream.writeFloat(yPos);
        dataOutputStream.writeFloat(zPos);
        dataOutputStream.writeFloat(rotationPitch);
        dataOutputStream.writeFloat(rotationYaw);

        //from CC 0.6 (worldVersion 3)

        dataOutputStream.writeInt(inventory.getFastSlotSize());

        for (int n = 0; n < inventory.getFastSlotSize(); ++n) {
            dataOutputStream.writeInt(inventory.getItemStackAt(n).itemID);
        }
    }
}
