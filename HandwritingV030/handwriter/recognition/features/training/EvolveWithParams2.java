package recognition.features.training;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;
import recognition.features.InputFeatureCombiner;
import recognition.features.TrainingSet;
import recognition.filter.Filter_Lowercase;
import recognition.filter.Filter_Uppercase;
import recognition.neuralnet_builders.FullyConnectedNNBuilder;
import recognition.strokes.StrokeList;
import trainingdata.Aggregate_Char_Files;
import trainingdata.character.ReadTrainingCharacters;

public class EvolveWithParams2 {

	
	public static void buildAndEvolved(String trainingset, int numInputs, int populationSize, int numGenerations, int numToSelect, 
			int largeMutationsPerSelected, double largeMutationFactor, int smallMutationsPerSelected, 
			double smallMutationFactor,  int numChildrenToBreed, int numToChoose, String fileOutput, int featureToClassify, 
			int[] hiddenLayerSizes, boolean[] toggleFeatures, String testingset, double maxThreshold,
			int maxOverfitGenerations, int num_possible)
			
			throws NumberFormatException, IOException {


	Vector<StrokeNetwork> netpopulation = new Vector<StrokeNetwork>();
	LinkedList<StrokeList> trainingChars = ReadTrainingCharacters.getCharsFromFile(trainingset);

	TrainingSet trainer = new TrainingSet(trainingChars, toggleFeatures);
	System.out.println("training set size: " + trainer.inputs.length);

	StrokeNetwork thisnet;


	for (int i=0; i < populationSize; i++) {
		thisnet =  FullyConnectedNNBuilder.networkBuilder(true, numInputs, 0.5, 1, hiddenLayerSizes);
		netpopulation.add(thisnet);
	}
	double[] fitnesses = getFitnesses(trainer, netpopulation, featureToClassify, num_possible);
	long time; long time2;

	LinkedList<StrokeList> testingChars = ReadTrainingCharacters.getCharsFromFile(testingset);
	TrainingSet testSet = new TrainingSet(testingChars, toggleFeatures);
	
	System.out.println("testing set size: " + testSet.inputs.length);

	StrokeNetwork bestNetModel = (StrokeNetwork) netpopulation.get(0);
	int bestNet=0; double bestNetFitness =0;

	double bestNetTestedFitness = Integer.MIN_VALUE;
	StrokeNetwork bestNetTestedModel = null;

	int numGenerationsSinceImproved = 0;

	for (int i=0; i < numGenerations; i++) {
		System.out.println("generation " + i);
		time = System.currentTimeMillis();
		netpopulation = evolvePopulation(netpopulation, fitnesses, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
										smallMutationFactor, numChildrenToBreed, numGenerations, numGenerationsSinceImproved);
		time2 = System.currentTimeMillis();
		System.out.println("mutation took: " + (time2 - time));
		fitnesses = getFitnesses(trainer, netpopulation, featureToClassify, num_possible);
		System.out.println("getting fitness took: " + (System.currentTimeMillis() - time2));
		System.out.println("took: " + (System.currentTimeMillis() - time) + ", fitness: " + fitnesses[0]);


		bestNet = findMax(fitnesses);
		bestNetModel = (StrokeNetwork)netpopulation.get(bestNet);


		for (int j=0; j < testSet.inputs.length; j++) {
			bestNetFitness += get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetModel, featureToClassify);
			//System.out.println(bestNetFitness);
		}
		System.out.println("test fitness: " + bestNetFitness);
		System.out.println(bestNetTestedFitness);
		System.out.println(numGenerationsSinceImproved);
		if (bestNetFitness > (bestNetTestedFitness+0.01)) {
			numGenerationsSinceImproved = 0;
			bestNetTestedModel = bestNetModel;
			bestNetTestedFitness = bestNetFitness;

		} 	else {
			numGenerationsSinceImproved++;
		}

		if (numGenerationsSinceImproved > maxOverfitGenerations) {
			break;
		}
			bestNetFitness = 0;

	}

	double trainFit = 0;
	for (int j=0; j < trainer.inputs.length; j++) {
		trainFit += get1Fitness(trainer.inputs[j], trainer.classifications[j], bestNetTestedModel, featureToClassify, num_possible);
	}

	double testFit = 0;
	for (int j=0; j < testSet.inputs.length; j++) {
		testFit += get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetTestedModel, featureToClassify);
	}
		System.out.println("most fit (training): " + trainFit);
		System.out.println("most fit (testing): " + testFit);

		FullyConnectedNNBuilder.networkToFile(fileOutput, bestNetTestedModel);

	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		// numInputs,   populationSize,     numGenerations,    numToSelect,          largeMutationsPerSelected
		// buildAndEvolved("stroketrainingset/stroke0/set5.tr",         80,        10000,                500,                 100,                        40, 
		// largeMutationFactor,    smallMutationsPerSelected,    smallmMutationFactor,     numChildrenToBreed
		//	0.1,			         40,                          0.005,                     2000,
		// numToChoose,    fileOutput
		//		5,           "networkmodels/stroke0/test");
		int start_i = 0;
		int end_i = 0;
		int num_to_stop = 200;
		args = new String[2];
		
		args[0] = "1";
		args[1] = "10";
		
		
		int numInputs = -1; int populationSize = 10000; int numGenerations = 10000; int numToSelect = 100; int largeMutationsPerSelected = 40; 
		double largeMutationFactor = 4; int smallMutationsPerSelected =40; double smallMutationFactor = 1; int numChildrenToBreed = 2000; 
		int numToChoose = 1;
		boolean[] toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = true; // 3x3
		toggleFeatures[1] = false; // 2x2
		toggleFeatures[2] = false;
		toggleFeatures[3] = false;

		numInputs = InputFeatureCombiner.getVectorSize(toggleFeatures);
		String inputFeatures = ".";
		for (int i=0; i < toggleFeatures.length; i++) {
			if (toggleFeatures[i]) {
				inputFeatures += "1.";
			} else {
				inputFeatures += "0.";
			}
		} inputFeatures += "__";

		// # hidden layers
		int[] layerSizes = new int[Integer.parseInt(args[0])];

		// # neurons layer 1
		layerSizes[0] = Integer.parseInt(args[1]);




			System.out.println(layerSizes.length + " hidden layer, size: " + layerSizes[0]);			
			
			
			
			int[][] paths = new int[96][];
			
			for (int i=0; i < 2; i++) {
				for (int j=0; j < 2; j++) {
					for (int z = 3; z < 7; z++) {
						for (int q = 0; q < 3; q++) {
							for (int x=0; x < 2; x++) {
							int[] path = new int[6];
							path[0] = 0;
							path[1] = i;
							path[2] = j;
							path[3] = z;
							path[4] = q;
							path[5] = x;
							System.out.println("" + (i*2*4*3*2 + j*4*3*2 + (z-3)*3*2 +q*2 + x));
							paths[i*2*4*3*2 + j*4*3*2 + (z-3)*3*2 +q*2 +x] = path;
							}
						}
					}
				}
			}
			Filter_Uppercase f = new Filter_Uppercase();
			for (int i=0; i < paths.length; i++) {
				int[] chars = f.filter(paths[i]);
				
				LinkedList<StrokeList> training_chars = new LinkedList<StrokeList>();
				LinkedList<StrokeList> testing_chars = new LinkedList<StrokeList>();
				for (int j=0; j < chars.length; j++) {
					for (int train_set = 0; train_set < 7; train_set++) {
						training_chars.addAll(ReadTrainingCharacters.getCharsFromFile("trainingcharacters/choose_from_all/cap_" + (train_set) + "_" + chars[j] + ".tr"));
					}					
					for (int test_set = 0; test_set < 4; test_set++) {
						testing_chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/cap_" + (test_set) + "_"  + chars[j] + ".ts"));
					}
				}
				
				TrainingSet trainer = new TrainingSet(training_chars, toggleFeatures);
				TrainingSet testing = new TrainingSet(testing_chars, toggleFeatures);
				training_chars.clear();
				testing_chars.clear();
				
				for (int j=0; j < chars.length; j++) {
					for (int train_set = 7; train_set < 12; train_set++) {
						training_chars.addAll(ReadTrainingCharacters.getCharsFromFile("trainingcharacters/choose_from_all/cap_" + (train_set) + "_" + chars[j] + ".tr"));
					}					
					for (int test_set = 4; test_set < 7; test_set++) {
						testing_chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/cap_" + (test_set) + "_"  + chars[j] + ".ts"));
					}
				}
				TrainingSet trainer2 = new TrainingSet(training_chars, toggleFeatures);
				TrainingSet testing2 = new TrainingSet(testing_chars, toggleFeatures);
				training_chars.clear();
				testing_chars.clear();
				trainer.add_training_set(trainer2);
				testing.add_training_set(testing2);
				
				for (int j=0; j < chars.length; j++) {
					System.out.print("character "  + chars[j] + " out of: ");
					for (int k=0; k < chars.length; k++) {
						System.out.print(chars[k] + ", ");
					}
					System.out.println();
					
					evolve_nn_2(trainer,  testing, numInputs, populationSize, numGenerations, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
							smallMutationFactor, numChildrenToBreed, numToChoose, "trainingcharacters/classifiers/caps/filtered/" + chars[j] + "_" + "_1_" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4]+ "." + paths[i][5]   + inputFeatures + layerSizes[0] + ".net", chars[j], layerSizes, toggleFeatures,
							9.5, 300, chars.length);
						
				}
				
			}
				
			
			/*
			 * code for training second level of neural nets.
			 * 
			 */
			
			/*
			BufferedReader read_chars = new BufferedReader(new FileReader("trainingcharacters/filtered/level_2/characters_index"));
			String in;
			int index=-1;
			ArrayList<Integer> these_chars = new ArrayList<Integer>(20);
			while ((in = read_chars.readLine()) != null) {
				if (in.equals("branch")) {
					if (index != -1) {
						int[] possible_chars = new int[these_chars.size()];
						for (int q=0; q < these_chars.size(); q++) {
							possible_chars[q] = these_chars.get(q);
						}
						these_chars.clear();
						possible_chars_per_path[index] = possible_chars;
					}
					index++;
				} else {
					these_chars.add(Integer.parseInt(in));
				}
			}
			int[] possible_chars = new int[these_chars.size()];
			for (int q=0; q < these_chars.size(); q++) {
				possible_chars[q] = these_chars.get(q);
			}
			these_chars.clear();
			possible_chars_per_path[index] = possible_chars;
			 */
			

			
			
	}
	
	public static double[] getFitnesses(TrainingSet training, Vector<StrokeNetwork> networks, int featureToClassify, int num_possible) throws IOException {
		double[] fitnesses = new double[networks.size()];
		for (int i=0; i < fitnesses.length; i++) {
				int numTrainElements = training.classifications.length;
				StrokeNetwork network = (StrokeNetwork) networks.get(i); fitnesses[i] = 0;
				for (int j=0; j < numTrainElements; j++) {
					fitnesses[i] += get1Fitness(training.inputs[j], training.classifications[j], network, featureToClassify, num_possible);
					
				}

		}
		return fitnesses;
	}
	// return difference between output of desired classification, and output of highest classification (not desired).
	public static double get1Fitness(double[] inputs, int classification, StrokeNetwork network, int featureToClassify, int num_possible) {
		double 	output = FullyConnectedNNBuilder.getOutput(inputs, network);
		
		if (classification == featureToClassify) {
		/*if (classification == 0 || classification == 1 || classification == 3 || classification == 7 || classification == 8 || classification == 9 
				|| classification == 10 || classification == 11 || classification == 19 || classification == 20 || classification == 21
				|| classification == 22 || classification == 23) {
				*/
			//System.out.println(output);
			return output;


		} else {

			//return -0.0416666667*output;
			//return -1.08333333333*output;
			//System.out.println(-1.0*(1.0/(num_possible-1.0))*output);

			return -1.0*(1.0/(num_possible-1.0))*output;
		}
		
	}
	public static double get1FitnessFromTestSet(double[] inputs, int classification, StrokeNetwork network, int featureToClassify) {
		double 	output = FullyConnectedNNBuilder.getOutput(inputs, network);
		
		if (classification == featureToClassify) {
			/*
		}
		if (classification == 0 || classification == 1 || classification == 3 || classification == 7 || classification == 8 || classification == 9 
				|| classification == 10 || classification == 11 || classification == 19 || classification == 20 || classification == 21
				|| classification == 22 || classification == 23) {
			*/
			/*
			if (output > 0) {
				return 1.0;
			} else {
				return -1.0;
			}*/
			return output;
		} else {
			/*
			if (output < 0) {
				return 1.0;
			} else {
				return -1.0;
			}
			*/
			return -1*output;
		}
		
	}		
	
	public static Vector<StrokeNetwork> evolvePopulation(Vector<StrokeNetwork> netpopulation, double[] fitnesses, int numToSelect, int largeMutationsPerSelected, 
															double largeMutationFactor, int smallMutationsPerSelected, double smallMutationFactor, 
															int numChildrenToBreed, int numGenerations, int numGenerationsSinceImproved) {
		
		Vector<StrokeNetwork> newpopulation = new Vector<StrokeNetwork>();
		int[] mostFit = findMostFit(netpopulation, fitnesses, numToSelect);

		Vector<StrokeNetwork> mostfitnets = new Vector<StrokeNetwork>(); StrokeNetwork thisnet;
		for (int i=0; i < mostFit.length; i++) {
			thisnet = netpopulation.get(mostFit[i]);
			mostfitnets.add(thisnet);
		}
		netpopulation.clear();
		
		newpopulation.addAll(mostfitnets);
		newpopulation.addAll(mutateAll(mostfitnets, largeMutationFactor, largeMutationsPerSelected, 1, numGenerationsSinceImproved));
		newpopulation.addAll(mutateAll(mostfitnets, smallMutationFactor, smallMutationsPerSelected, 0, numGenerationsSinceImproved));	
		
		return newpopulation;
	}

	public static Vector<StrokeNetwork> mutateAll(Vector<StrokeNetwork> parents, double mutationFactor, int numMutations, int allocateParent, int numGenerationsSinceImproved) {
		Vector<StrokeNetwork> mutations = new Vector<StrokeNetwork>();
		Iterator<StrokeNetwork> iterator = parents.listIterator();
		int counter = 0;
		while (iterator.hasNext()) {
			mutations.addAll(mutate1(iterator.next(), mutationFactor, numMutations, allocateParent, numGenerationsSinceImproved));
			counter++;
		}
		return mutations;
	}
	public static Vector<StrokeNetwork> mutate1(StrokeNetwork parent, double mutationFactor, int numMutations, int allocateParent, int numGenerationsSinceImproved) {
		Vector<StrokeNetwork> mutations = new Vector<StrokeNetwork>();
		for (int i=0; i < numMutations-allocateParent; i++) {
			mutations.add(get1Mutation(parent, mutationFactor, numGenerationsSinceImproved));
		}
		return mutations;
	}
	public static StrokeNetwork get1Mutation(StrokeNetwork parent, double mutationFactor, int numGenerationsSinceImproved) {
		StrokeNetwork mutation = new StrokeNetwork();
		mutation.numlayers = parent.numlayers; mutation.inputSize = parent.inputSize; mutation.layersizes = parent.layersizes;
		mutation.weights = new double[parent.weights.length][][];
			for (int i=0; i < mutation.weights.length; i++) {
				mutation.weights[i] = new double[parent.weights[i].length][];
				for (int j=0; j < mutation.weights[i].length; j++) {
					mutation.weights[i][j] = new double[parent.weights[i][j].length];
				}                                                 
			}
		mutation.thresholds = new double[parent.thresholds.length][];
			for (int ii=0; ii < mutation.thresholds.length; ii++) {
				mutation.thresholds[ii] = new double[parent.thresholds[ii].length];
			}
		
		double rand;
		for (int k =0; k < mutation.weights.length; k++) {
			for (int h=0; h < mutation.weights[k].length; h++) {
				for (int z=0; z < mutation.weights[k][h].length; z++) {
					rand = Math.random();
					if (Math.random() < 0.5) {
						mutation.weights[k][h][z] = parent.weights[k][h][z] + rand*mutationFactor;
						mutation.weights[k][h][z] = parent.weights[k][h][z] + rand;
					} else {
						mutation.weights[k][h][z] = parent.weights[k][h][z] - rand*mutationFactor;
						mutation.weights[k][h][z] = parent.weights[k][h][z] - rand;
					}
				}
			}
		}
		for (int kk=0; kk < mutation.thresholds.length; kk++) {
			for (int hh=0; hh < mutation.thresholds[kk].length; hh++) {
				rand = Math.random();
				if (Math.random() < 0.5) {
					mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] + rand*mutationFactor;
					mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] + rand;
					} else {
					mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] - rand*mutationFactor;
					mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] - rand;
				}
			}
		}
		return mutation;
	}
	public static int[] findMostFit(Vector<StrokeNetwork> netpopulation, double[] fitnesses, int numToSelect) {
		//String s = "";
		//for (int i=0; i < fitnesses.length; i++) {
		//	s += fitnesses[i] +", ";
		//}
		//System.out.println(s);
		int[] selected = new int[numToSelect];
		double[] selectedFitnesses = new double[numToSelect];
			for (int i=0; i < selectedFitnesses.length; i++) {
				selectedFitnesses[i] = Integer.MIN_VALUE;
			}
			for (int i=0; i < selected.length; i++) {
				selected[i] = -1;
			}
		double thisFitness;
		for (int k=0; k < fitnesses.length; k++) {
			thisFitness = fitnesses[k];
			for (int h=0; h < numToSelect; h++) {
				if (thisFitness > selectedFitnesses[h]) {
					selected[h] = k; 
					selectedFitnesses[h] = thisFitness;
					break;
				}
			}
		}
		return selected;
	}
	public static int findMax(double[] array) {
		double maxVal = array[0];
		int maxIndex = 0;
		for (int i=1; i < array.length; i++) {
			if (array[i] > maxVal) {
				maxVal = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
		
	}
	
	public static void adapt(String trainingset, int numInputs, int populationSize, int numGenerations, int numToSelect, 
			int largeMutationsPerSelected, double largeMutationFactor, int smallMutationsPerSelected, 
			double smallMutationFactor,  int numChildrenToBreed, int numToChoose, String fileOutput, int featureToClassify, 
			int[] hiddenLayerSizes, boolean[] toggleFeatures, String testingset, double maxThreshold,
			int maxOverfitGenerations, String classifier_file, int num_possible)
			
			throws NumberFormatException, IOException {
	
		Vector<StrokeNetwork> netpopulation = new Vector<StrokeNetwork>();
		StrokeNetwork pre_net = FullyConnectedNNBuilder.fileToNetwork(classifier_file);
		for (int i=0; i < populationSize; i++) {
			netpopulation.add(StrokeNetworkBuilder.copy_stroke_network(pre_net));
		}
		LinkedList<StrokeList> trainingChars = ReadTrainingCharacters.getCharsFromFile(trainingset);

		TrainingSet trainer = new TrainingSet(trainingChars, toggleFeatures);

		double[] fitnesses = getFitnesses(trainer, netpopulation, featureToClassify, num_possible);
		long time; long time2;

		LinkedList<StrokeList> testingChars = ReadTrainingCharacters.getCharsFromFile(testingset);
		TrainingSet testSet = new TrainingSet(testingChars, toggleFeatures);

		StrokeNetwork bestNetModel = (StrokeNetwork) netpopulation.get(0);
		int bestNet=0; double bestNetFitness =0;

		double bestNetTestedFitness = Integer.MIN_VALUE;
		StrokeNetwork bestNetTestedModel = null;

		int numGenerationsSinceImproved = 0;

		for (int i=0; i < numGenerations; i++) {
			System.out.println("generation " + i);
			time = System.currentTimeMillis();
			netpopulation = evolvePopulation(netpopulation, fitnesses, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
											smallMutationFactor, numChildrenToBreed, numGenerations, numGenerationsSinceImproved);
			time2 = System.currentTimeMillis();
			System.out.println("mutation took: " + (time2 - time));
			fitnesses = getFitnesses(trainer, netpopulation, featureToClassify, num_possible);
			System.out.println("getting fitness took: " + (System.currentTimeMillis() - time2));
			System.out.println("took: " + (System.currentTimeMillis() - time) + ", fitness: " + fitnesses[0]);


			bestNet = findMax(fitnesses);
			bestNetModel = (StrokeNetwork)netpopulation.get(bestNet);


			for (int j=0; j < testSet.inputs.length; j++) {
				bestNetFitness += get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetModel, featureToClassify);
			}
			System.out.println("test fitness: " + bestNetFitness);
			System.out.println(bestNetTestedFitness);
			System.out.println(numGenerationsSinceImproved);
			if (bestNetFitness > bestNetTestedFitness) {
				numGenerationsSinceImproved = 0;
				bestNetTestedModel = bestNetModel;
				bestNetTestedFitness = bestNetFitness;

			} 	else {
				numGenerationsSinceImproved++;
			}

			if (numGenerationsSinceImproved > maxOverfitGenerations) {
				break;
			}
				bestNetFitness = 0;

		}

		double trainFit = 0;
		for (int j=0; j < trainer.inputs.length; j++) {
			trainFit += get1Fitness(trainer.inputs[j], trainer.classifications[j], bestNetTestedModel, featureToClassify, num_possible);
		}

		double testFit = 0;
		for (int j=0; j < testSet.inputs.length; j++) {
			testFit += get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetTestedModel, featureToClassify);
		}
			System.out.println("most fit (training): " + trainFit);
			System.out.println("most fit (testing): " + testFit);

			FullyConnectedNNBuilder.networkToFile(fileOutput, bestNetTestedModel);

	}
	public static void evolve_nn(LinkedList<StrokeList> trainingChars, LinkedList<StrokeList> testingChars, int numInputs, int populationSize, int numGenerations, int numToSelect, 
			int largeMutationsPerSelected, double largeMutationFactor, int smallMutationsPerSelected, 
			double smallMutationFactor,  int numChildrenToBreed, int numToChoose, String fileOutput, int featureToClassify, 
			int[] hiddenLayerSizes, boolean[] toggleFeatures, double maxThreshold,
			int maxOverfitGenerations, int num_possible)
			
			throws NumberFormatException, IOException {


	Vector<StrokeNetwork> netpopulation = new Vector<StrokeNetwork>();

	TrainingSet trainer = new TrainingSet(trainingChars, toggleFeatures);
	System.out.println("training set size: " + trainer.inputs.length);

	StrokeNetwork thisnet;


	for (int i=0; i < populationSize; i++) {
		thisnet =  FullyConnectedNNBuilder.networkBuilder(true, numInputs, 0.5, 1, hiddenLayerSizes);
		netpopulation.add(thisnet);
	}
	double[] fitnesses = getFitnesses(trainer, netpopulation, featureToClassify, num_possible);
	long time; long time2;

	TrainingSet testSet = new TrainingSet(testingChars, toggleFeatures);
	
	System.out.println("testing set size: " + testSet.inputs.length);

	StrokeNetwork bestNetModel = (StrokeNetwork) netpopulation.get(0);
	int bestNet=0; double bestNetFitness =0;

	double bestNetTestedFitness = Integer.MIN_VALUE;
	StrokeNetwork bestNetTestedModel = null;

	int numGenerationsSinceImproved = 0;

	for (int i=0; i < numGenerations; i++) {
		System.out.println("generation " + i);
		time = System.currentTimeMillis();
		netpopulation = evolvePopulation(netpopulation, fitnesses, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
										smallMutationFactor, numChildrenToBreed, numGenerations, numGenerationsSinceImproved);
		time2 = System.currentTimeMillis();
		System.out.println("mutation took: " + (time2 - time));
		fitnesses = getFitnesses(trainer, netpopulation, featureToClassify, num_possible);
		System.out.println("getting fitness took: " + (System.currentTimeMillis() - time2));
		System.out.println("took: " + (System.currentTimeMillis() - time) + ", fitness: " + fitnesses[0]);


		bestNet = findMax(fitnesses);
		bestNetModel = (StrokeNetwork)netpopulation.get(bestNet);


		for (int j=0; j < testSet.inputs.length; j++) {
			bestNetFitness += get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetModel, featureToClassify);
			//System.out.println(bestNetFitness);
		}
		System.out.println("test fitness: " + bestNetFitness);
		System.out.println(bestNetTestedFitness);
		System.out.println(numGenerationsSinceImproved);
		if (bestNetFitness > (bestNetTestedFitness+0.01)) {
			numGenerationsSinceImproved = 0;
			bestNetTestedModel = bestNetModel;
			bestNetTestedFitness = bestNetFitness;

		} 	else {
			numGenerationsSinceImproved++;
		}

		if (numGenerationsSinceImproved > maxOverfitGenerations) {
			break;
		}
			bestNetFitness = 0;

	}

	double trainFit = 0;
	for (int j=0; j < trainer.inputs.length; j++) {
		trainFit += get1Fitness(trainer.inputs[j], trainer.classifications[j], bestNetTestedModel, featureToClassify, num_possible);
	}

	double testFit = 0;
	for (int j=0; j < testSet.inputs.length; j++) {
		testFit += get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetTestedModel, featureToClassify);
	}
		System.out.println("most fit (training): " + trainFit);
		System.out.println("most fit (testing): " + testFit);

		FullyConnectedNNBuilder.networkToFile(fileOutput, bestNetTestedModel);

	}
	
	public static void evolve_nn_2(TrainingSet trainer, TrainingSet testSet, int numInputs, int populationSize, int numGenerations, int numToSelect, 
			int largeMutationsPerSelected, double largeMutationFactor, int smallMutationsPerSelected, 
			double smallMutationFactor,  int numChildrenToBreed, int numToChoose, String fileOutput, int featureToClassify, 
			int[] hiddenLayerSizes, boolean[] toggleFeatures, double maxThreshold,
			int maxOverfitGenerations, int num_possible)
			
			throws NumberFormatException, IOException {


	Vector<StrokeNetwork> netpopulation = new Vector<StrokeNetwork>();

	System.out.println("training set size: " + trainer.inputs.length);

	StrokeNetwork thisnet;


	for (int i=0; i < populationSize; i++) {
		thisnet =  FullyConnectedNNBuilder.networkBuilder(true, numInputs, 0.5, 1, hiddenLayerSizes);
		netpopulation.add(thisnet);
	}
	double[] fitnesses = getFitnesses(trainer, netpopulation, featureToClassify, num_possible);
	long time; long time2;

	
	System.out.println("testing set size: " + testSet.inputs.length);

	StrokeNetwork bestNetModel = (StrokeNetwork) netpopulation.get(0);
	int bestNet=0; double bestNetFitness =0;

	double bestNetTestedFitness = Integer.MIN_VALUE;
	StrokeNetwork bestNetTestedModel = null;

	int numGenerationsSinceImproved = 0;

	for (int i=0; i < numGenerations; i++) {
		System.out.println("generation " + i);
		time = System.currentTimeMillis();
		netpopulation = evolvePopulation(netpopulation, fitnesses, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
										smallMutationFactor, numChildrenToBreed, numGenerations, numGenerationsSinceImproved);
		time2 = System.currentTimeMillis();
		System.out.println("mutation took: " + (time2 - time));
		fitnesses = getFitnesses(trainer, netpopulation, featureToClassify, num_possible);
		System.out.println("getting fitness took: " + (System.currentTimeMillis() - time2));
		System.out.println("took: " + (System.currentTimeMillis() - time) + ", fitness: " + fitnesses[0]);


		bestNet = findMax(fitnesses);
		bestNetModel = (StrokeNetwork)netpopulation.get(bestNet);


		for (int j=0; j < testSet.inputs.length; j++) {
			bestNetFitness += get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetModel, featureToClassify);
			//System.out.println(bestNetFitness);
		}
		System.out.println("test fitness: " + bestNetFitness);
		System.out.println(bestNetTestedFitness);
		System.out.println(numGenerationsSinceImproved);
		if (bestNetFitness > (bestNetTestedFitness+0.01)) {
			numGenerationsSinceImproved = 0;
			bestNetTestedModel = bestNetModel;
			bestNetTestedFitness = bestNetFitness;

		} 	else {
			numGenerationsSinceImproved++;
		}

		if (numGenerationsSinceImproved > maxOverfitGenerations) {
			break;
		}
			bestNetFitness = 0;

	}

	double trainFit = 0;
	for (int j=0; j < trainer.inputs.length; j++) {
		trainFit += get1Fitness(trainer.inputs[j], trainer.classifications[j], bestNetTestedModel, featureToClassify, num_possible);
	}

	double testFit = 0;
	for (int j=0; j < testSet.inputs.length; j++) {
		testFit += get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetTestedModel, featureToClassify);
	}
		System.out.println("most fit (training): " + trainFit);
		System.out.println("most fit (testing): " + testFit);

		FullyConnectedNNBuilder.networkToFile(fileOutput, bestNetTestedModel);

	}
	
	
}
