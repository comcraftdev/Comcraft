package net.comcraft.src;

public class MathHelper {

    static final double sq2p1 = 2.414213562373095048802e0;
    static final double sq2m1 = .414213562373095048802e0;
    static final double p4 = .161536412982230228262e2;
    static final double p3 = .26842548195503973794141e3;
    static final double p2 = .11530293515404850115428136e4;
    static final double p1 = .178040631643319697105464587e4;
    static final double p0 = .89678597403663861959987488e3;
    static final double q4 = .5895697050844462222791e2;
    static final double q3 = .536265374031215315104235e3;
    static final double q2 = .16667838148816337184521798e4;
    static final double q1 = .207933497444540981287275926e4;
    static final double q0 = .89678597403663861962481162e3;
    static final double PIO2 = 1.5707963267948966135E0;
    static final double nan = (0.0 / 0.0);

    public static boolean isOpositeSign(float a, float b) {
        return (a < 0 && b > 0) || (a > 0 && b < 0);
    }

    public static double getRadians(int x, int y) {
        if (x == 0) {
            if (y > 0) {
                return Math.PI / 2;
            }

            if (y < 0) {
                return -Math.PI / 2;
            }

            return 0;
        }

        return atan(y / x) + (x < 0 ? Math.PI : 0);
    }

    // reduce
    private static double mxatan(double arg) {
        double argsq, value;

        argsq = arg * arg;
        value = ((((p4 * argsq + p3) * argsq + p2) * argsq + p1) * argsq + p0);
        value = value / (((((argsq + q4) * argsq + q3) * argsq + q2) * argsq + q1) * argsq + q0);
        return value * arg;
    }

    // reduce
    private static double msatan(double arg) {
        if (arg < sq2m1) {
            return mxatan(arg);
        }
        if (arg > sq2p1) {
            return PIO2 - mxatan(1 / arg);
        }
        return PIO2 / 2 + mxatan((arg - 1) / (arg + 1));
    }

    // implementation of atan
    public static double atan(double arg) {
        if (arg > 0) {
            return msatan(arg);
        }
        return -msatan(-arg);
    }

    // implementation of atan2
    public static double atan2(double arg1, double arg2) {
        if (arg1 + arg2 == arg1) {
            if (arg1 >= 0) {
                return PIO2;
            }
            return -PIO2;
        }
        arg1 = atan(arg1 / arg2);
        if (arg2 < 0) {
            if (arg1 <= 0) {
                return arg1 + Math.PI;
            }
            return arg1 - Math.PI;
        }
        return arg1;

    }

    // implementation of asin
    public static double asin(double arg) {
        double temp;
        int sign;

        sign = 0;
        if (arg < 0) {
            arg = -arg;
            sign++;
        }
        if (arg > 1) {
            return nan;
        }
        temp = Math.sqrt(1 - arg * arg);
        if (arg > 0.7) {
            temp = PIO2 - atan(temp / arg);
        } else {
            temp = atan(arg / temp);
        }
        if (sign > 0) {
            temp = -temp;
        }
        return temp;
    }

    // implementation of acos
    public static double acos(double arg) {
        if (arg > 1 || arg < -1) {
            return nan;
        }
        return PIO2 - asin(arg);
    }

    public static double pow(double x, double y) {
        return powTaylor(x, y);
    }

    public static double powSqrt(double x, double y) {
        int den = 1024, num = (int) (y * den), iterations = 10;
        double n = Double.MAX_VALUE;

        while (n >= Double.MAX_VALUE && iterations > 1) {
            n = x;

            for (int i = 1; i < num; i++) {
                n *= x;
            }

            if (n >= Double.MAX_VALUE) {
                iterations--;
                den = (int) (den / 2);
                num = (int) (y * den);
            }
        }

        for (int i = 0; i < iterations; i++) {
            n = Math.sqrt(n);
        }

        return n;
    }

    public static double powDecay(double x, double y) {
        int num, den = 1001, s = 0;
        double n = x, z = Double.MAX_VALUE;

        for (int i = 1; i < s; i++) {
            n *= x;
        }

        while (z >= Double.MAX_VALUE) {
            den -= 1;
            num = (int) (y * den);
            s = (num / den) + 1;

            z = x;
            for (int i = 1; i < num; i++) {
                z *= x;
            }
        }

        while (n > 0) {
            double a = n;

            for (int i = 1; i < den; i++) {
                a *= n;
            }

            if ((a - z) < .00001 || (z - a) > .00001) {
                return n;
            }

            n *= .9999;
        }

        return -1.0;
    }

    public static double powTaylor(double a, double b) {
        boolean gt1 = (Math.sqrt((a - 1) * (a - 1)) <= 1) ? false : true;
        int oc = -1, iter = 30;
        double p = a, x, x2, sumX, sumY;

        if ((b - Math.floor(b)) == 0) {
            for (int i = 1; i < b; i++) {
                p *= a;
            }
            return p;
        }

        x = (gt1) ? (a / (a - 1)) : (a - 1);
        sumX = (gt1) ? (1 / x) : x;

        for (int i = 2; i < iter; i++) {
            p = x;
            for (int j = 1; j < i; j++) {
                p *= x;
            }

            double xTemp = (gt1) ? (1 / (i * p)) : (p / i);

            sumX = (gt1) ? (sumX + xTemp) : (sumX + (xTemp * oc));

            oc *= -1;
        }

        x2 = b * sumX;
        sumY = 1 + x2;

        for (int i = 2; i <= iter; i++) {
            p = x2;
            for (int j = 1; j < i; j++) {
                p *= x2;
            }

            int yTemp = 2;
            for (int j = i; j > 2; j--) {
                yTemp *= j;
            }

            sumY += p / yTemp;
        }

        return sumY;
    }
}
