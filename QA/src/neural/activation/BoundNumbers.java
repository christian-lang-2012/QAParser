package neural.activation;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 */
public class BoundNumbers {

    public static double TOO_SMALL = -1.0E20;

    public static double TOO_BIG = 1.0E20;

    public static double bound(double d) {
        if (d < TOO_SMALL) {
            return TOO_SMALL;
        } else if (d > TOO_BIG) {
            return TOO_BIG;
        } else {
            return d;
        }
    }

    public static double exp(double d) {
        return bound(Math.exp(d));
    }
}
