
package neural.activation;

import java.io.Serializable;

/**
 * ActivationFunction: This interface allows various 
 * activation functions to be used with the feedforward
 * neural network.  Activation functions are applied
 * to the output from each layer of a neural network.
 * Activation functions scale the output into the
 * desired range.
 */
public interface ActivationFunction extends Serializable {

	public double activationFunction(double d);

	public double derivativeFunction(double d);
}
