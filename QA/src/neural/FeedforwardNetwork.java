package neural;

import Errors.NeuralNetworkError;
import neural.Matrix.MatrixCODEC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 *
 * Self-explanatory
 */
public class FeedforwardNetwork {

    protected FeedForwardLayer inputLayer;

    protected FeedForwardLayer outputLayer;

    protected List<FeedForwardLayer> layers = new ArrayList<FeedForwardLayer>();

    public FeedforwardNetwork() {
    }

    public void addLayer(FeedForwardLayer layer) {
        // setup the forward and back pointer
        if (this.outputLayer != null) {
            layer.setPrevious(this.outputLayer);
            this.outputLayer.setNext(layer);
        }

        // update the inputLayer and outputLayer variables
        if (this.layers.size() == 0) {
            this.inputLayer = this.outputLayer = layer;
        } else {
            this.outputLayer = layer;
        }

        // add the new layer to the list
        this.layers.add(layer);
    }

    public double calculateError(double input[][], double ideal[][])
            throws NeuralNetworkError {
        ErrorCalculation errorCalculation = new ErrorCalculation();

        for (int i = 0; i < ideal.length; i++) {
            computeOutputs(input[i]);
            errorCalculation.updateError(this.outputLayer.getFire(),
                    ideal[i]);
        }
        return (errorCalculation.calculateRMS());
    }

    public int calculateNeuronCount() {
        int result = 0;
        for (FeedForwardLayer layer : this.layers) {
            result += layer.getNeuronCount();
        }
        return result;
    }

    @Override
    public Object clone() {
        FeedforwardNetwork result = cloneStructure();
        Double copy[] = MatrixCODEC.networkToArray(this);
        MatrixCODEC.arrayToNetwork(copy, result);
        return result;
    }

    public FeedforwardNetwork cloneStructure() {
        FeedforwardNetwork result = new FeedforwardNetwork();

        for (FeedForwardLayer layer : this.layers) {
            FeedForwardLayer clonedLayer = new FeedForwardLayer(layer
                    .getNeuronCount());
            result.addLayer(clonedLayer);
        }

        return result;
    }

    public double[] computeOutputs(double input[]) {

        if (input.length != this.inputLayer.getNeuronCount()) {
            throw new NeuralNetworkError(
                    "Size mismatch: Can't compute outputs for input: size ="
                            + input.length + " for input layer: size ="
                            + this.inputLayer.getNeuronCount());
        }

        for (FeedForwardLayer layer : this.layers) {
            if (layer.isInput()) {
                layer.computeOutputs(input);
            } else if (layer.isHidden()) {
                layer.computeOutputs(null);
            }
        }

        return this.outputLayer.getFire();
    }

    public boolean equals(FeedforwardNetwork other) {
        Iterator<FeedForwardLayer> otherLayers = other.getLayers().iterator();

        for (FeedForwardLayer layer : this.getLayers()) {
            FeedForwardLayer otherLayer = otherLayers.next();

            if (layer.getNeuronCount() != otherLayer.getNeuronCount()) {
                return false;
            }

            // make sure they either both have or do not have
            // a weight matrix.
            if ((layer.getMatrix() == null) && (otherLayer.getMatrix() != null)) {
                return false;
            }

            if ((layer.getMatrix() != null) && (otherLayer.getMatrix() == null)) {
                return false;
            }

            // if they both have a matrix, then compare the matrices
            if ((layer.getMatrix() != null) && (otherLayer.getMatrix() != null)) {
                if (!layer.getMatrix().equals(otherLayer.getMatrix())) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getHiddenLayerCount() {
        return this.layers.size() - 2;
    }

    public Collection<FeedForwardLayer> getHiddenLayers() {
        Collection<FeedForwardLayer> result = new ArrayList<FeedForwardLayer>();
        for (FeedForwardLayer layer : this.layers) {
            if (layer.isHidden()) {
                result.add(layer);
            }
        }
        return result;
    }

    public FeedForwardLayer getInputLayer() {
        return this.inputLayer;
    }

    public List<FeedForwardLayer> getLayers() {
        return this.layers;
    }

    public FeedForwardLayer getOutputLayer() {
        return this.outputLayer;
    }

    public int getWeightMatrixSize() {
        int result = 0;
        for (FeedForwardLayer layer : this.layers) {
            result += layer.getMatrixSize();
        }
        return result;
    }

    public void reset() {
        for (FeedForwardLayer layer : this.layers) {
            layer.reset();
        }
    }
}
