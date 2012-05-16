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
import recognition.neuralnet_builders.FullyConnectedNNBuilder;
import recognition.strokes.StrokeList;
import trainingdata.character.ReadTrainingCharacters;

public class Genetic {

	
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

		int start_i = Integer.parseInt(args[0]);
		int end_i = Integer.parseInt(args[1]);
		int num_to_stop = Integer.parseInt(args[2]);
		
		args = new String[2];
		
		args[0] = "1";
		args[1] = "10";
		
		
		int numInputs = 36; int populationSize = 10000; int numGenerations = 10000; int numToSelect = 100; int largeMutationsPerSelected = 40; 
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

			Filter_Lowercase f = new Filter_Lowercase();
			
			
			//buildAndEvolved("trainingcharacters/choose_from_all/0_all.tr", numInputs, populationSize, numGenerations, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
					//smallMutationFactor, numChildrenToBreed, numToChoose, "trainingcharacters/classifiers/grid/2/0_1" + inputFeatures + layerSizes[0] + ".net", 1, layerSizes, toggleFeatures, "testingcharacters/l_a-z_0.ts", 9.5, 250);
			/*
			for (int i=7; i < 8; i++) {
				buildAndEvolved("trainingcharacters/left0/0_all.tr", numInputs, populationSize, numGenerations, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
						smallMutationFactor, numChildrenToBreed, numToChoose, "trainingcharacters/classifiers/grid/left0/0_" + i  + inputFeatures + layerSizes[0] + ".net", i, layerSizes, toggleFeatures, "testingcharacters/0/left0/0_all.ts", 9.5, 250);
			}
			*/
			/*
			adapt("trainingcharacters/filtered/" + f.string_0_0_0_0_0 + ".tr", numInputs, populationSize, numGenerations, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
					smallMutationFactor, numChildrenToBreed, numToChoose, "trainingcharacters/classifiers/filtered/" + f.results_0_0_0_0_0[0] + "_2_" + f.string_0_0_0_0_0   + inputFeatures + layerSizes[0] + ".net", f.results_0_0_0_0_0[0], layerSizes, toggleFeatures, "testingcharacters/filtered/" + f.string_0_0_0_0_0 + ".ts", 9.5, 500,
					"trainingcharacters/classifiers/filtered/" + f.results_0_0_0_0_0[0] + "_0_" + f.string_0_0_0_0_0   + inputFeatures + layerSizes[0] + ".net");
			
			*/
			/*
			System.out.println("trainingcharacters/filtered/" + f.string_0_0_0_0_0 + ".tr");
			System.out.println("trainingcharacters/classifiers/filtered/" + f.results_0_0_0_0_0[0] + "_1_" + f.string_0_0_0_0_0   + inputFeatures + layerSizes[0] + ".net");
			System.out.println("testingcharacters/filtered/" + f.string_0_0_0_0_0 + ".ts");
			for (int i=2; i < 3; i++) {
				buildAndEvolved("trainingcharacters/filtered/" + f.string_0_0_0_0_0 + ".tr", numInputs, populationSize, numGenerations, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
						smallMutationFactor, numChildrenToBreed, numToChoose, "trainingcharacters/classifiers/filtered/" + f.results_0_0_0_0_0[i] + "_0_" + f.string_0_0_0_0_0   + inputFeatures + layerSizes[0] + ".net", f.results_0_0_0_0_0[i], layerSizes, toggleFeatures, "testingcharacters/filtered/" + f.string_0_0_0_0_0 + ".ts", 9.5, 250);
			}
			*/
			
			/*
			int feature = 5;
			buildAndEvolved("trainingcharacters/filtered/" + f.string_0_0_0_0_0 + ".tr", numInputs, populationSize, numGenerations, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
					smallMutationFactor, numChildrenToBreed, numToChoose, "trainingcharacters/classifiers/filtered/" + feature + "_0_" + f.string_0_0_0_0_0   + inputFeatures + layerSizes[0] + ".net", 5, layerSizes, toggleFeatures, "testingcharacters/filtered/" + f.string_0_0_0_0_0 + ".ts", 9.5, 250);
					*/
			
			
			int[][] paths = new int[96][];
			int[][] possible_chars_per_path = new int[96][];
			
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
			
			/*
			 * code for training second level of neural nets.
			 * 
			 */
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
			
			/*
			 */
			
			
			for (int i=0; i < paths.length; i++) {
				
				//possible_chars_per_path[i] = f.filter(paths[i]);
				
				
				System.out.println("trainingcharacters/filtered/" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4] + "." + paths[i][5]+ ".tr");
				System.out.println("testingcharacters/filtered/" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4]+ "." + paths[i][5]+ ".ts");
				if (i >= start_i && i <= end_i) { // 0 to 1
					
					System.out.println("number of chars: " + possible_chars_per_path[i].length);
					for (int j=0; j < possible_chars_per_path[i].length; j++) {
						System.out.println("i = " + i);
						System.out.println("j = " + j);
						System.out.println("character: " + possible_chars_per_path[i][j]);
						buildAndEvolved("trainingcharacters/filtered/level_2/" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4] + "." + paths[i][5] + ".tr", numInputs, populationSize, numGenerations, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
								smallMutationFactor, numChildrenToBreed, numToChoose, "trainingcharacters/classifiers/filtered/level_2/" + possible_chars_per_path[i][j] + "_1_" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4]+ "." + paths[i][5]   + inputFeatures + layerSizes[0] + ".net", possible_chars_per_path[i][j], layerSizes, toggleFeatures, "testingcharacters/filtered/level_2/" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4] +  "." + paths[i][5] + ".ts",
								9.5, num_to_stop, possible_chars_per_path[i].length);
					}
				}
				
			}
			
			
			
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
			return output;


		} else {

			//return -0.0416666667*output;
			//return -1.08333333333*output;
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
		
		newpopulation.addAll(mate_population(mostfitnets));
		newpopulation = mutate_population(newpopulation, 1.0, numGenerationsSinceImproved);
		//newpopulation.addAll(mostfitnets);
		//newpopulation.addAll(mutateAll(mostfitnets, largeMutationFactor, largeMutationsPerSelected, 1, numGenerationsSinceImproved));
		//newpopulation.addAll(mutateAll(mostfitnets, smallMutationFactor, smallMutationsPerSelected, 0, numGenerationsSinceImproved));	
		
		return newpopulation;
	}
	public static Vector<StrokeNetwork> mutate_population(Vector<StrokeNetwork> population, double mutationFactor, int numGenerationsSinceImproved) {
		Iterator<StrokeNetwork> pop_iterate = population.iterator();
		StrokeNetwork this_net;
		Vector<StrokeNetwork> mutated = new Vector<StrokeNetwork>();
		for (int i=0; i < population.size(); i++) {
			this_net = pop_iterate.next();
			mutated.add(get1Mutation(this_net, mutationFactor, numGenerationsSinceImproved));
		}
		return mutated;
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
						//mutation.weights[k][h][z] = parent.weights[k][h][z] + rand;
					} else {
						mutation.weights[k][h][z] = parent.weights[k][h][z] - rand*mutationFactor;
						//mutation.weights[k][h][z] = parent.weights[k][h][z] - rand;
					}
				}
			}
		}
		for (int kk=0; kk < mutation.thresholds.length; kk++) {
			for (int hh=0; hh < mutation.thresholds[kk].length; hh++) {
				rand = Math.random();
				if (Math.random() < 0.5) {
					mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] + rand*mutationFactor;
					//mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] + rand;
					} else {
					mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] - rand*mutationFactor;
					//mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] - rand;
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
	
	
	
	public static Vector<StrokeNetwork> mate_population(Vector<StrokeNetwork> parents) {
		Vector<StrokeNetwork> babies = new Vector<StrokeNetwork>();
		for (int i=0; i < parents.size(); i++) {
			StrokeNetwork mommy = parents.get(i);
			for (int j=0; j < parents.size(); j++) {
				if (i == j) {
					babies.add(mommy);
				} else {
					StrokeNetwork daddy = parents.get(j);
					babies.add(make_baby(mommy, daddy));
				}
				
			}
		}
		return babies;
		
	}
	public static StrokeNetwork make_baby(StrokeNetwork mommy, StrokeNetwork daddy) {
		StrokeNetwork baby = new StrokeNetwork();
		
		
		
		int network_volume = mommy.inputSize*mommy.layersizes[0];
		for (int i=1; i < mommy.layersizes.length; i++) {
			network_volume += mommy.layersizes[i-1]*mommy.layersizes[i];
		}
		int thresholds_volume = mommy.layersizes[0];
		for (int i=1; i < mommy.layersizes.length; i++) {
			thresholds_volume += mommy.layersizes[i];
		}
		
		double x = Math.random();
		int splice_weights = (int) Math.floor(x*network_volume);
		int splice_thresholds = (int) Math.floor(x*thresholds_volume);
		//System.out.println(thresholds_volume + ", " + network_volume + ", " + splice_weights + ", " + splice_thresholds );
		int splice_final_weights = (int)Math.floor(x*mommy.layersizes[mommy.layersizes.length-2]);
	
		baby.inputSize = mommy.inputSize;
		baby.layersizes = mommy.layersizes;
		baby.numlayers = mommy.numlayers;
		
		baby.weights = mommy.weights;
		baby.thresholds = mommy.thresholds;
		
		int count_weights = 0;
		int count_thresh = 0;
		int count_final_weights = 0;
		for (int i=0; i < baby.weights.length; i++) {
			//System.out.println("1: " + baby.weights.length);
			for (int j=0; j < baby.weights[i].length; j++) {
				//System.out.println("2: " + baby.weights[i].length);
				for (int k=0; k < baby.weights[i][j].length; k++) {
					//System.out.println("3: " + baby.weights[i][j].length);
					if (i == baby.weights.length-1) {
						count_final_weights++;
						if (count_final_weights > splice_final_weights) {
							baby.weights[i][j][k] = daddy.weights[i][j][k];
						}
					} else {
						count_weights++;
						if (count_weights > splice_weights) {
							baby.weights[i][j][k] = daddy.weights[i][j][k];
						}
					}
				}
				
			}
		}
		for (int i=0; i < baby.thresholds.length; i++) {
			for (int j=0; j < baby.thresholds[i].length; j++) {
				count_thresh++;
				if (count_thresh > splice_thresholds) {
					baby.thresholds[i][j] = daddy.thresholds[i][j];
				}
			}
		}
		if (Math.random() < 0.5) {
			baby.thresholds[baby.thresholds.length-1][0] = daddy.thresholds[daddy.thresholds.length-1][0];
		} else {
			baby.thresholds[baby.thresholds.length-1][0] = mommy.thresholds[mommy.thresholds.length-1][0];

		}
		return baby;
		
	}
	
}

