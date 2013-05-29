/*
 * Class is used to chceck collisions with blocks.
 */

package net.comcraft.src;

public final class AxisAlignedBB {

    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;

    public AxisAlignedBB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public static AxisAlignedBB getBoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AxisAlignedBB setBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public boolean collidesWith(AxisAlignedBB axisAlignedBB) {
        Vec3D[] vecTab = new Vec3D[8];
        
        vecTab[0] = new Vec3D(minX, minY, minZ);
        vecTab[1] = new Vec3D(maxX, minY, minZ);
        vecTab[2] = new Vec3D(minX, maxY, minZ);
        vecTab[3] = new Vec3D(minX, minY, maxZ);
        vecTab[4] = new Vec3D(maxX, maxY, minZ);
        vecTab[5] = new Vec3D(minX, maxY, maxZ);
        vecTab[6] = new Vec3D(maxX, minY, maxZ);
        vecTab[7] = new Vec3D(maxX, maxY, maxZ);
        
        for (int n = 0; n < vecTab.length; ++n) {
            if (axisAlignedBB.isVecInside(vecTab[n])) {
                return true;
            }
        }
        
        return false;
    }

    public boolean isVecInside(Vec3D vec3D) {
        return minX <= vec3D.x && maxX >= vec3D.x && minY <= vec3D.y && maxY >= vec3D.y && minZ <= vec3D.z && maxZ >= vec3D.z; 
    }
    
//    public boolean collidesWithWorld(World world) {
//        if (world.getBlockID((int) minX, (int) minY, (int) minZ) != 0) {
//            return true;
//        }
//        if (world.getBlockID((int) maxX, (int) minY, (int) minZ) != 0) {
//            return true;
//        }
//        if (world.getBlockID((int) minX, (int) maxY, (int) minZ) != 0) {
//            return true;
//        }
//        if (world.getBlockID((int) minX, (int) minY, (int) maxZ) != 0) {
//            return true;
//        }
//        if (world.getBlockID((int) maxX, (int) minY, (int) maxZ) != 0) {
//            return true;
//        }
//        if (world.getBlockID((int) minX, (int) maxY, (int) maxZ) != 0) {
//            return true;
//        }
//        if (world.getBlockID((int) maxX, (int) maxY, (int) minZ) != 0) {
//            return true;
//        }
//        if (world.getBlockID((int) maxX, (int) maxY, (int) maxZ) != 0) {
//            return true;
//        }
//        
//        return false;
//    }
}
