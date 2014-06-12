package Errors;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 *
 * Custom error for when unwanted behavior happens in the neural network
 */
public class NeuralNetworkError extends RuntimeException {
    public NeuralNetworkError(String message){
        super(message);
    }
}
