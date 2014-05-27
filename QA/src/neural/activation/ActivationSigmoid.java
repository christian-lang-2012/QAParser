
package neural.activation;

/**
 * ActivationSigmoid: The sigmoid activation function takes on a
 * sigmoidal shape.  Only positive numbers are generated.  Do not
 * use this activation function if negative number output is desired.
 */
public class ActivationSigmoid implements ActivationFunction {

	public double activationFunction(double d) {
		return 1.0 / (1 + BoundNumbers.exp(-1.0 * d));
	}

	public double derivativeFunction(double d) {
		return d*(1.0-d);
	}

}
