
package neural.activation;

import Errors.NeuralNetworkError;

/**
 * ActivationLinear: The Linear layer is really not an activation function 
 * at all.  The input is simply passed on, unmodified, to the output.
 * This activation function is primarily theoretical and of little actual
 * use.  Usually an activation function that scales between 0 and 1 or
 * -1 and 1 should be used.
 *
 */
public class ActivationLinear implements ActivationFunction {

	public double activationFunction(double d) {
		return d;
	}

	public double derivativeFunction(double d) {
		throw new NeuralNetworkError("Can't use the linear activation function where a derivative is required.");
	}

}
