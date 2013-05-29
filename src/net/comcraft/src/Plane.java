package net.comcraft.src;

public final class Plane {

    Vec3D normal = new Vec3D();
    Vec3D point = new Vec3D();
    float d;

    public Plane() {
    }

    public Plane(Vec3D v1, Vec3D v2, Vec3D v3) {
        set3Points(v1, v2, v3);
    }

    public void set3Points(Vec3D v1, Vec3D v2, Vec3D v3) {
        Vec3D aux1, aux2;

        aux1 = v1.subtractVector(v2);

        aux2 = v3.subtractVector(v2);

        normal = aux2.crossProduct(aux1);
        normal.normalize();

        point.copy(v2);

        d = -(normal.innerProduct(point));
    }

    public void setNormalAndPoint(Vec3D normal, Vec3D point) {
        this.normal.copy(normal);
        this.normal.normalize();
        d = -(this.normal.innerProduct(point));
    }

    public void setCoefficients(float a, float b, float c, float d) {
        normal.setComponents(a, b, c);

        float l = normal.lengthVector();

        normal.setComponents(a / l, b / l, c / l);

        this.d = d / l;
    }

    public float distance(Vec3D p) {
        return (d + normal.innerProduct(p));
    }
}
