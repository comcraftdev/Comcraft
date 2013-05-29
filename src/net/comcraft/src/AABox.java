/*
 * Class is used by FrustumCulling
 */

package net.comcraft.src;

public final class AABox {

    public Vec3D corner = new Vec3D();
    public float x, y, z;

    public AABox(Vec3D corner, float x, float y, float z) {
        setBox(corner, x, y, z);
    }

    public AABox() {
        corner.x = 0;
        corner.y = 0;
        corner.z = 0;

        x = 1.0f;
        y = 1.0f;
        z = 1.0f;
    }

    public void setBox(Vec3D corner, float x, float y, float z) {
        this.corner.copy(corner);

        if (x < 0.0) {
            x = -x;
            this.corner.x -= x;
        }
        if (y < 0.0) {
            y = -y;
            this.corner.y -= y;
        }
        if (z < 0.0) {
            z = -z;
            this.corner.z -= z;
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D getVertexP(Vec3D normal) {
        Vec3D res = corner;

        if (normal.x > 0) {
            res.x += x;
        }

        if (normal.y > 0) {
            res.y += y;
        }

        if (normal.z > 0) {
            res.z += z;
        }

        return (res);
    }

    public Vec3D getVertexN(Vec3D normal) {
        Vec3D res = corner;

        if (normal.x < 0) {
            res.x += x;
        }

        if (normal.y < 0) {
            res.y += y;
        }

        if (normal.z < 0) {
            res.z += z;
        }

        return (res);
    }
}
