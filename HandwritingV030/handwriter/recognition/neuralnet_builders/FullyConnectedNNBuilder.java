package recognition.neuralnet_builders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import neuralnetwork.strokenetwork.StrokeNetwork;

public class FullyConnectedNNBuilder {
	
	/**
	 * builds a random neural network  with a few parameters.
	 * 
	 * @param weightsweight, thresholdsweight
	 * 	
	 * 
	 */
	public static StrokeNetwork networkBuilder(boolean random, int inputSizer, double weightsweight, double thresholdsweight, int[] hiddenLayerSizes)
	{
		// count input layer?
		
		StrokeNetwork net = new StrokeNetwork();
		net.inputSize = inputSizer;
		Random rand = new Random();
		
		net.numlayers = hiddenLayerSizes.length +1; 		  // number of layers between 3 and 4
		
		net.layersizes = new int[net.numlayers];
		for (int i=0; i < net.numlayers-1; i++) { 
			net.layersizes[i] = hiddenLayerSizes[i];
		} net.layersizes[net.numlayers-1] = 1;
		
		net.weights = new double[net.numlayers][][];
		for (int i=0; i < net.numlayers; i++) {
			if (i==0) {
				net.weights[i] = new double[net.layersizes[i]][inputSizer];
			}
			else {
				net.weights[i] = new double[net.layersizes[i]][net.layersizes[i-1]];
			}
		}
		net.thresholds = new double[net.numlayers][];
		for (int i=0; i < net.numlayers; i++) {
			net.thresholds[i] = new double[net.layersizes[i]];
		}
		setRandomWeights(weightsweight, net); setRandomThresholds(thresholdsweight, net);
		return net;
	}
	
	
	
	
	public static void setRandomWeights(double weightweight, StrokeNetwork net) {
		for (int i=0; i < net.weights.length; i++) {
			for (int j=0; j < net.weights[i].length; j++) {
				if (i ==0) {
					for (int z=0; z < net.weights[i][j].length; z++) {
						net.weights[i][j][z] = Math.random()*weightweight;
					}
				}
				else {
					for (int z=0; z < net.weights[i][j].length; z++) {
						net.weights[i][j][z] = Math.random()*weightweight;
					}
				}
			}
		}
	}
	
	public static void setRandomThresholds(double threshweight, StrokeNetwork net) {
		for (int i=0; i < net.thresholds.length; i++) {
			for (int j=0; j < net.thresholds[i].length; j++) {
				net.thresholds[i][j] = Math.random()*threshweight;
			}
		}
	}
	
	/*
	 * returns output from a neural network for given input.
	 */
	public static double getOutput(double[] input, StrokeNetwork net) {

		double[][] outputs = new double[net.numlayers][];
		for (int kk = 0; kk < net.numlayers; kk++) {
			outputs[kk] = new double[net.layersizes[kk]];
		}
		for (int i=0; i < net.weights[0].length; i++) { // loop through input layer neurons
			outputs[0][i] = sigmoid(getActivation(input, net.weights[0][i]));
			
		}
		
		for (int zz=1; zz < net.weights.length; zz++) { // loop through layers
			for (int qq=0; qq < net.weights[zz].length; qq++) { // loop through layer neurons
				outputs[zz][qq] = sigmoid(getActivation(outputs[zz-1], net.weights[zz][qq]));
			}
			
		}		
		
		return outputs[net.numlayers-1][0];
	}
	public static double[] getOutputArray(double[] input, StrokeNetwork net) {
		double[][] outputs = new double[net.numlayers][];
		for (int kk =0; kk < net.numlayers; kk++) {
			outputs[kk] = new double[net.layersizes[kk]];
		}
		//System.out.println(net.weights);
		for (int i=0; i < net.weights[0].length; i++) {
			outputs[0][i] = sigmoid(getActivation(input, net.weights[0][i]));
		}
		for (int zz=1; zz < net.weights.length; zz++) {
			for (int qq=0; qq < net.weights[zz].length; qq++) {
				outputs[zz][qq] = sigmoid(getActivation(outputs[zz-1], net.weights[zz][qq]));
			}
		}
		return outputs[net.numlayers-1];
	}
	
	public static double sigmoid(double activation) {
		return (1 / (1 + (Math.pow(2.71828183, (-1*activation)))))-.5;
	}
	public static double getActivation(double[] inputs, double[] weights) {
		double activation=0;
		for (int i =0; i < inputs.length; i++) {
			activation += inputs[i]*weights[i];
		}
		return activation;
	}
	
	public static String toString(StrokeNetwork net) {
		String s = "";
		
		s += "number of layers: " + net.numlayers +"\n";
		for (int i=0; i < net.numlayers; i++) {
			s += " layer " + i + ":\n";
			s += "  number of neurons: " + net.weights[i].length + "\n";
			for (int j=0; j < net.weights[i].length; j++) {
				s += "    neuron " + j + ":\n";
				s += "     number of weights " + net.weights[i][j].length +"\n";
				for (int z=0; z < net.weights[i][j].length; z++) {
					s += "        weight: " + net.weights[i][j][z];
				}
				s += "\n";
				s += 	 "        threshold: " + net.thresholds[i][j];
			}
		}
		return s;
	}
	
	/*
	 * Stores a network to a file
	 * 
	 * has template:
	 * [input size]
	 * [number of layers]
	 * [layer0 size]
	 * [layer0 weights
	 * .
	 * .
	 * ]
	 * [layer0 thresholds
	 * .
	 * .
	 * ]
	 * [layer1 size]
	 * [layer1 weights
	 * .
	 * .
	 * ]
	 * [layer1 thresholds
	 * .
	 * .
	 * ]
	 * .
	 * .
	 * .
	 * [layer n size]
	 * [layer n weights
	 * .
	 * .
	 * ]
	 * [layer n thresholds
	 * .
	 * .
	 * ]
	 * 
	 */
	public static void networkToFile(String filename, StrokeNetwork net) {
		FileWriter fstream = null;
		try {
			fstream = new FileWriter(filename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(net.inputSize + "\n");
			out.write(net.numlayers + "\n");
			for (int i=0; i < net.numlayers; i++) {
				out.write(net.layersizes[i] + "\n");
				for (int j=0; j < net.weights[i].length; j++) {
					for (int z=0; z < net.weights[i][j].length; z++) {
						out.write(net.weights[i][j][z] + "\n");
					}
				}
				for (int jj=0; jj < net.thresholds[i].length; jj++) {
					out.write(net.thresholds[i][jj] + "\n");
				}
			}
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/*
	 * reads a file and creates the StrokeNetwork it describes.
	 * 
	 */
	public static StrokeNetwork fileToNetwork(String filename) throws NumberFormatException, IOException {
		StrokeNetwork net = new StrokeNetwork();
		
		try {
			BufferedReader reader  = new BufferedReader(new FileReader(filename));
			net.inputSize = Integer.valueOf(reader.readLine());
			net.numlayers = Integer.valueOf(reader.readLine());
			net.layersizes = new int[net.numlayers];
			net.weights = new double[net.numlayers][][];
			net.thresholds = new double[net.numlayers][];
			
			for (int i=0; i < net.numlayers; i++) {
				net.layersizes[i] = Integer.valueOf(reader.readLine());
				net.thresholds[i] = new double[net.layersizes[i]];				
				if (i ==0) {
					net.weights[i] = new double[net.layersizes[i]][net.inputSize];
					for (int j=0; j < net.weights[i].length; j++) { // first layer
						for (int z=0; z < net.weights[i][j].length; z++){
							net.weights[i][j][z] = Double.valueOf(reader.readLine());
						}
					} 
				} else {
					net.weights[i] = new double[net.layersizes[i]][net.layersizes[i-1]];					
					for (int qq=0; qq < net.weights[i].length; qq++) {
						for (int pp=0; pp < net.weights[i][qq].length; pp++) {
							net.weights[i][qq][pp] = Double.valueOf(reader.readLine());
						}
					}
				}
				for (int jj=0; jj < net.thresholds[i].length; jj++) {
					net.thresholds[i][jj] = Double.valueOf(reader.readLine());
				}
			}	
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return net;
	}
	
	public static void main(String[] args) {
		int[] ls = new int[1];
		ls[0] = 50;
		StrokeNetwork net = networkBuilder(true, 69, 0.5, 1, ls);
		double[] input = new double[69];
		for (int i=0; i < 69; i++) {
			input[i] = Math.random();
		}
		long time = System.currentTimeMillis();
		
		for (int i=0; i < 100000; i++) {
			getOutput(input, net);
		}
		System.out.println(FullyConnectedNNBuilder.getOutput(input, net));
		
		System.out.println(System.currentTimeMillis() - time);
		
	}
	
}
