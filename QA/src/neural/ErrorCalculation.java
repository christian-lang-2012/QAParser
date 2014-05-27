package neural;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 */
public class ErrorCalculation {
    private double globalError;
    private int setSize;

    public double calculateRMS(){
        double err = Math.sqrt(this.globalError / this.setSize);
        return err;
    }

    public void reset(){
        this.globalError = 0;
        this.setSize = 0;
    }

    public void updateError(double actual[], double ideal[]){
        for(int i = 0; i < actual.length; i++){
            double delta = ideal[i] - actual[i];
            this.globalError += delta * delta;
        }
        this.setSize += ideal.length;
    }
}
