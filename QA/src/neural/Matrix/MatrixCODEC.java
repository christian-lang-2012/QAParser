package neural.Matrix;

import neural.FeedForwardLayer;
import neural.FeedforwardNetwork;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 */
public class MatrixCODEC {

    public static void arrayToNetwork(Double array[],
                                      FeedforwardNetwork network) {

        // copy data to array
        int index = 0;

        for (final FeedForwardLayer layer : network.getLayers()) {

            // now the weight matrix(if it exists)
            if (layer.getNext() != null) {
                index = layer.getMatrix().fromPackedArray(array, index);
            }
        }
    }

    public static Double[] networkToArray(FeedforwardNetwork network) {
        int size = 0;

        // first determine size
        for (final FeedForwardLayer layer : network.getLayers()) {
            // count the size of the weight matrix
            if (layer.hasMatrix()) {
                size += layer.getMatrixSize();
            }
        }

        // allocate an array to hold
        final Double result[] = new Double[size];

        // copy data to array
        int index = 0;

        for (final FeedForwardLayer layer : network.getLayers()) {

            // now the weight matrix(if it exists)
            if (layer.getNext() != null) {

                final Double matrix[] = layer.getMatrix().toPackedArray();
                for (int i = 0; i < matrix.length; i++) {
                    result[index++] = matrix[i];
                }
            }
        }

        return result;
    }
}
