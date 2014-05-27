package Errors;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 */
public class MatrixError extends RuntimeException {
    public MatrixError(String message){
        super(message);
    }
}
