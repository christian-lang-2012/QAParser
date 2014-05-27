
package neural.activation;

/**
 * ActivationTANH: The hyperbolic tangent activation function takes the
 * curved shape of the hyperbolic tangent.  This activation function produces
 * both positive and negative output.  Use this activation function if 
 * both negative and positive output is desired.
 *
 */
public class ActivationTANH implements ActivationFunction {

	private static final long serialVersionUID = 9121998892720207643L;

	public double activationFunction(double d) {
		final double result = (BoundNumbers.exp(d * 2.0)-1.0)/(BoundNumbers.exp(d * 2.0)+1.0);
		return result;
	}

	public double derivativeFunction(double d) {
		return( 1.0-Math.pow(activationFunction(d), 2.0) );
	}

}
