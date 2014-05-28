package Util;

import Config.Config;
import neural.FeedForwardLayer;
import neural.FeedforwardNetwork;
import neural.activation.ActivationFunction;
import neural.activation.ActivationSigmoid;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/15/14
 */
public class NetworkUtil {
    public static FeedforwardNetwork createNetwork(){
        ActivationFunction threshold = new ActivationSigmoid();
        FeedforwardNetwork network = new FeedforwardNetwork();
        network.addLayer(new FeedForwardLayer(threshold, Config.INPUT_SIZE));
        network.addLayer(new FeedForwardLayer(threshold, Config.NEURONS_HIDDEN_1));
        if(Config.NEURONS_HIDDEN_2 > 0){
            network.addLayer((new FeedForwardLayer(threshold, Config.NEURONS_HIDDEN_2)));
        }

        network.addLayer(new FeedForwardLayer(threshold, Config.OUTPUT_SIZE));
        network.reset();
        return network;
    }
}
