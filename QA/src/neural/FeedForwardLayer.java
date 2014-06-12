package neural;

import Errors.NeuralNetworkError;
import neural.Matrix.Matrix;
import neural.Matrix.MatrixMath;
import neural.activation.ActivationFunction;
import neural.activation.ActivationSigmoid;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 *
 * A feedforward neural network layer
 */
public class FeedForwardLayer {
    private double fire[];

    private Matrix matrix;

    private FeedForwardLayer next;

    private FeedForwardLayer previous;

    private ActivationFunction activationFunction;

    public FeedForwardLayer(ActivationFunction thresholdFunction, int neuronCount){
        this.fire = new double[neuronCount];
        this.activationFunction = thresholdFunction;
    }

    public FeedForwardLayer(int neuronCount){
        this(new ActivationSigmoid(), neuronCount);
    }

    public FeedForwardLayer cloneStructure(){
        return  new FeedForwardLayer(this.activationFunction, this.getNeuronCount());
    }

    public double[] computeOutputs(double pattern[]){
        int i;
        if(pattern != null){
            for(i = 0; i < getNeuronCount(); i++){
                setFire(i, pattern[i]);
            }
        }

        Matrix inputMatrix = createInputMatrix(this.fire);

        for (i = 0; i < this.next.getNeuronCount(); i++) {
            final Matrix col = this.matrix.getCol(i);
            final double sum = MatrixMath.dotProduct(col, inputMatrix);

            this.next.setFire(i, this.activationFunction.activationFunction(sum));
        }

        return this.fire;
    }

    private Matrix createInputMatrix(double pattern[]){
        Matrix result = new Matrix(1, pattern.length +1);
        for(int i = 0; i < pattern.length; i++){
            result.set(0, i, pattern[i]);
        }
        return result;
    }

    public double[] getFire(){
        return this.fire;
    }

    public Matrix getMatrix(){
        return this.matrix;
    }

    public int getMatrixSize(){
        if(this.matrix == null){
          return 0;
        }
        else{
            return this.matrix.size();
        }
    }

    public int getNeuronCount(){
        return this.fire.length;
    }

    public FeedForwardLayer getNext(){
        return this.next;
    }

    public FeedForwardLayer getPrevious(){
        return this.previous;
    }

    public boolean hasMatrix(){
        return this.matrix != null;
    }

    public boolean isHidden(){
        return ((this.next !=null) && (this.previous != null));
    }

    public boolean isInput(){
        return (this.previous == null);
    }

    public boolean isOutput(){
        return this.next == null;
    }


    public void prune(int neuron) {
        // delete a row on this matrix
        if (this.matrix != null) {
            setMatrix(MatrixMath.deleteRow(this.matrix, neuron));
        }

        // delete a column on the previous
        final FeedForwardLayer previous = this.getPrevious();
        if (previous != null) {
            if (previous.getMatrix() != null) {
                previous.setMatrix(MatrixMath.deleteCol(previous.getMatrix(),
                        neuron));
            }
        }

    }

    public void reset() {

        if (this.matrix != null) {
            this.matrix.ramdomize(-1, 1);
        }

    }

    public void setFire(int index, double f) {
        this.fire[index] = f;
    }

    public void setMatrix(Matrix m1) {
        if (m1.getRows() < 2) {
            throw new NeuralNetworkError(
                    "Weight matrix includes threshold values, and must have at least 2 rows.");
        }

        if (m1 != null) {
            this.fire = new double[matrix.getRows() - 1];
        }

        this.matrix = m1;

    }


    public void setNext(FeedForwardLayer next) {
        this.next = next;
        // add one to the neuron count to provide a threshold value in row 0
        this.matrix = new Matrix(this.getNeuronCount() + 1, next
                .getNeuronCount());
    }

    public void setPrevious(FeedForwardLayer previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("[FeedforwardLayer: Neuron Count=");
        result.append(getNeuronCount());
        result.append("]");
        return result.toString();
    }

    public ActivationFunction getActivationFunction() {
        return this.activationFunction;
    }


}
