package net.comcraft.src;

public class RayObjectPosition {

    public static final int TILE = 0;
    public static final int ENTITY = 1;
    public int typeOfHit;
    public int blockX;
    public int blockY;
    public int blockZ;
    public int sideHit;
    public Vec3D hitVec;
    public Vec3D lastHitVec;

    public RayObjectPosition(int x, int y, int z, int side, Vec3D vec3D) {
        typeOfHit = TILE;
        blockX = x;
        blockY = y;
        blockZ = z;
        sideHit = side;
        hitVec = vec3D;
    }
}
