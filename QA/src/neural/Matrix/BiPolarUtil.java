package neural.Matrix;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 */
public class BiPolarUtil {
    public static double bipolar2double(final boolean b) {
        if (b) {
            return 1;
        } else {
            return -1;
        }
    }

    public static double[] bipolar2double(final boolean b[]) {
        final double[] result = new double[b.length];

        for (int i = 0; i < b.length; i++) {
            result[i] = bipolar2double(b[i]);
        }

        return result;
    }

    public static double[][] bipolar2double(final boolean b[][]) {
        final double[][] result = new double[b.length][b[0].length];

        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[0].length; col++) {
                result[row][col] = bipolar2double(b[row][col]);
            }
        }

        return result;
    }

    public static boolean double2bipolar(final double d) {
        if (d > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean[] double2bipolar(final double d[]) {
        final boolean[] result = new boolean[d.length];

        for (int i = 0; i < d.length; i++) {
            result[i] = double2bipolar(d[i]);
        }

        return result;
    }

    public static boolean[][] double2bipolar(final double d[][]) {
        final boolean[][] result = new boolean[d.length][d[0].length];

        for (int row = 0; row < d.length; row++) {
            for (int col = 0; col < d[0].length; col++) {
                result[row][col] = double2bipolar(d[row][col]);
            }
        }

        return result;
    }

    public static double normalizeBinary(final double d) {
        if (d > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static double toBinary(final double d) {
        return (d + 1) / 2.0;
    }

    public static double toBiPolar(final double d) {
        return (2 * normalizeBinary(d)) - 1;
    }

    public static double toNormalizedBinary(final double d) {
        return normalizeBinary(toBinary(d));
    }
}
