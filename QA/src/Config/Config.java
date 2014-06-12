package Config;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 *
 * This is a configuration file for the Neural Network.
 * It contains the default input, output sizes, the default hidden layer neuron count,
 * and acceptable error range.
 */
public class Config {

    public final static int INPUT_SIZE = 10;
    public final static int OUTPUT_SIZE = 1;
    public final static int NEURONS_HIDDEN_1 = 20;
    public final static int NEURONS_HIDDEN_2 = 0;
    public final static double ACCEPTABLE_ERROR = 0.01;
    public final static int MINIMUM_WORDS_PRESENT = 3;

    public static final String FILENAME_GOOD_TRAINING_TEXT = "goodPracticeData.txt";
    public static final String FILENAME_BAD_TRAINING_TEXT = "badPracticeData.txt";
    public static final String FILENAME_COMMON_WORDS = "common.csv";

}
