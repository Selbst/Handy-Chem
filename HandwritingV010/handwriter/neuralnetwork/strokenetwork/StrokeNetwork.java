package neuralnetwork.strokenetwork;

/*
 * Simple description of a neural network
 * 
 * use method StrokeNetworkBuilder2.getOutputs() to find output values for a given input.
 * 
 */
public class StrokeNetwork {
	public int numlayers; public int[] layersizes;
	public double[][][] weights; // weights[layer][neuron][weight]
	public int inputSize;
	public double[][] thresholds;
	double fitness;
	
}
