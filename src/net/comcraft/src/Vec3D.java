package net.comcraft.src;

public final class Vec3D {

    public float x;
    public float y;
    public float z;

    public Vec3D() {
    }

    public Vec3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(Vec3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3D setComponents(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3D subtractVector(Vec3D vec3D) {
        return new Vec3D(vec3D.x - x, vec3D.y - y, vec3D.z - z);
    }

    public Vec3D normalize() {
        float length = lengthVector();

        if (length < 0.0001f) {
            return new Vec3D(0, 0, 0);
        } else {
            return new Vec3D(x / length, y / length, z / length);
        }
    }

    public double dotProduct(Vec3D vec3D) {
        return x * vec3D.x + y * vec3D.y + z * vec3D.z;
    }

    public Vec3D crossProduct(Vec3D vec3D) {
        return new Vec3D(y * vec3D.z - z * vec3D.y, z * vec3D.x - x * vec3D.z, x * vec3D.y - y * vec3D.x);
    }

    public Vec3D crossProduct(float t) {
        return new Vec3D(x * t, y * t, z * t);
    }

    public Vec3D addVector(float x, float y, float z) {
        return new Vec3D(x + x, y + y, z + z);
    }

    public Vec3D addVector(Vec3D vec) {
        return new Vec3D(x + vec.x, y + vec.y, z + vec.z);
    }

    public float distanceTo(Vec3D vec3D) {
        float f = vec3D.x - x;
        float f1 = vec3D.y - y;
        float f2 = vec3D.z - z;
        return (float) Math.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public double squareDistanceTo(Vec3D vec3D) {
        float f = vec3D.x - x;
        float f1 = vec3D.y - y;
        float f2 = vec3D.z - z;
        return f * f + f1 * f1 + f2 * f2;
    }

    public float lengthVector() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    public void copy(Vec3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }
    
    public float innerProduct(Vec3D vec) {
        return (x * vec.x + y * vec.y + z * vec.z);
    }
    
    public Vec3D subtractVector() {
        Vec3D res = new Vec3D();
        
        res.x = -x;
        res.y = -y;
        res.z = -z;
        
        return res;
    }
}
