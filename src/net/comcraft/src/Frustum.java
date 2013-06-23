package net.comcraft.src;

public class Frustum {

    private final static short TOP = 0;
    private final static short BOTTOM = 1;
    private final static short LEFT = 2;
    private final static short RIGHT = 3;
    private final static short NEARP = 4;
    private final static short FARP = 5;
    public final static short OUTSIDE = 0;
    public final static short INTERSECT = 1;
    public final static short INSIDE = 2;
    private final static float HALF_ANG2RAD = (float) (3.14159265358979323846 / 360.0);
    private Plane[] pl = new Plane[6];
    private Vec3D ntl, ntr, nbl, nbr, ftl, fbl, fbr, X, Y, Z, camPos;
    private float nearD, farD, ratio, angle;
    private float sphereFactorX, sphereFactorY;
    private double tang;
    private double nw, nh, fw, fh;

    public Frustum() {
        for (int x = 0; x < 6; ++x) {
            pl[x] = new Plane();
        }
    }

    public void setCamInternals(float angle, float ratio, float nearD, float farD) {
        this.ratio = ratio;
        this.angle = angle * HALF_ANG2RAD;
        this.nearD = nearD;
        this.farD = farD;

        tang = Math.tan(this.angle);
        sphereFactorY = (float) (1.0 / Math.cos(this.angle));

        float anglex = (float) (MathHelper.atan(tang * ratio));
        sphereFactorX = (float) (1.0 / Math.cos(anglex));

        nh = nearD * tang;
        nw = nh * ratio;

        fh = farD * tang;
        fw = fh * ratio;
    }

    public void setCamDef(Vec3D p, Vec3D l, Vec3D u) {
        Vec3D nc, fc;

        camPos = p;

        Z = p.subtractVector(l);
        Z.normalize();

        X = u.crossProduct(Z);
        X.normalize();

        Y = Z.crossProduct(X);

        nc = p.subtractVector(Z.crossProduct(nearD));
        fc = p.subtractVector(Z.crossProduct(farD));

        ntl = nc.addVector(Y.crossProduct((float) nh)).subtractVector(X.crossProduct((float) nw));
        ntr = nc.addVector(Y.crossProduct((float) nh)).addVector(X.crossProduct((float) nw));
        nbl = nc.subtractVector(Y.crossProduct((float) nh)).subtractVector(X.crossProduct((float) nw));
        nbr = nc.subtractVector(Y.crossProduct((float) nh)).addVector(X.crossProduct((float) nw));

        ftl = fc.addVector(Y.crossProduct((float) fh)).subtractVector(X.crossProduct((float) fw));
        fbr = fc.subtractVector(Y.crossProduct((float) fh)).addVector(X.crossProduct((float) fw));
        fc.addVector(Y.crossProduct((float) fh)).addVector(X.crossProduct((float) fw));
        fbl = fc.subtractVector(Y.crossProduct((float) fh)).subtractVector(X.crossProduct((float) fw));

        pl[TOP].set3Points(ntr, ntl, ftl);
        pl[BOTTOM].set3Points(nbl, nbr, fbr);
        pl[LEFT].set3Points(ntl, nbl, fbl);
        pl[RIGHT].set3Points(nbr, ntr, fbr);
        pl[NEARP].setNormalAndPoint(Z.subtractVector(), nc);
        pl[FARP].setNormalAndPoint(Z, fc);

        Vec3D aux, normal;

        aux = (nc.addVector(Y.crossProduct((float) nh))).subtractVector(p);
        normal = aux.crossProduct(X);
        pl[TOP].setNormalAndPoint(normal, nc.addVector(Y.crossProduct((float) nh)));

        aux = (nc.subtractVector(Y.crossProduct((float) nh))).subtractVector(p);
        normal = X.crossProduct(aux);
        pl[BOTTOM].setNormalAndPoint(normal, nc.subtractVector(Y.crossProduct((float) nh)));

        aux = (nc.subtractVector(X.crossProduct((float) nw))).subtractVector(p);
        normal = aux.crossProduct(Y);
        pl[LEFT].setNormalAndPoint(normal, nc.subtractVector(X.crossProduct((float) nw)));

        aux = (nc.addVector(X.crossProduct((float) nw))).subtractVector(p);
        normal = Y.crossProduct(aux);
        pl[RIGHT].setNormalAndPoint(normal, nc.addVector(X.crossProduct((float) nw)));
    }

    public short pointInFrustum(Vec3D p) {
        float pcz, pcx, pcy, aux;

        Vec3D v = p.subtractVector(camPos);

        pcz = v.innerProduct(Z.subtractVector());
        if (pcz > farD || pcz < nearD) {
            return (OUTSIDE);
        }

        pcy = v.innerProduct(Y);
        aux = pcz * (float) tang;
        if (pcy > aux || pcy < -aux) {
            return (OUTSIDE);
        }

        pcx = v.innerProduct(X);
        aux = aux * ratio;
        if (pcx > aux || pcx < -aux) {
            return (OUTSIDE);
        }

        return (INSIDE);
    }

    public int sphereInFrustum(Vec3D p, float radius) {
        float d1, d2;
        float az, ax, ay, zz1, zz2;
        int result = INSIDE;

        Vec3D v = p.subtractVector(camPos);

        az = v.innerProduct(Z.subtractVector());

        if (az > farD + radius || az < nearD - radius) {
            return (OUTSIDE);
        }

        ax = v.innerProduct(X);
        zz1 = az * (float) tang * ratio;
        d1 = sphereFactorX * radius;

        if (ax > zz1 + d1 || ax < -zz1 - d1) {
            return (OUTSIDE);
        }

        ay = v.innerProduct(Y);
        zz2 = az * (float) tang;
        d2 = sphereFactorY * radius;

        if (ay > zz2 + d2 || ay < -zz2 - d2) {
            return (OUTSIDE);
        }
        if (az > farD - radius || az < nearD + radius) {
            result = INTERSECT;
        }
        if (ay > zz2 - d2 || ay < -zz2 + d2) {
            result = INTERSECT;
        }
        if (ax > zz1 - d1 || ax < -zz1 + d1) {
            result = INTERSECT;
        }

        return (result);
    }

    public int boxInFrustum(AABox b) {
        int result = INSIDE;
        
        for (int i = 0; i < 6; i++) {
            if (pl[i].distance(b.getVertexP(pl[i].normal)) < 0) {
                return OUTSIDE;
            }
            else if (pl[i].distance(b.getVertexN(pl[i].normal)) < 0) {
                result = INTERSECT;
            }
        }
        return (result);
    }
    
    public int chunkInFrustum(Vec3D vec0, int x, int y, int z) {
        int number = 10;
        
        Vec3D[] corners = new Vec3D[(number + 1) * 4];
        
        for (int i = 0; i <= number; ++i) {
            corners[(i * 4)] = new Vec3D(vec0.x + x, vec0.y + y * ((float) i / number), vec0.z);
            corners[(i * 4) + 1] = new Vec3D(vec0.x, vec0.y + y * ((float) i / number), vec0.z + z);
            corners[(i * 4) + 2] = new Vec3D(vec0.x, vec0.y + y * ((float) i / number), vec0.z);
            corners[(i * 4) + 3] = new Vec3D(vec0.x + x, vec0.y + y * ((float) i / number), vec0.z + z);
        }
        
        for (int i = 0; i < corners.length; ++i) {
            if (pointInFrustum(corners[i]) != OUTSIDE) {
                return INSIDE;
            }
        }
        
        return OUTSIDE;
    }
}
