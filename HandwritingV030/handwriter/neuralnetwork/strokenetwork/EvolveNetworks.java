package neuralnetwork.strokenetwork;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import recognition.TrainingElement;
import recognition.TrainingSet;

public class EvolveNetworks {
	TrainingSet training;
	double[] fitnesses;
	
	
	public static void buildAndEvolved(String trainingset, int numInputs, int populationSize, int numGenerations, int numToSelect, 
											int largeMutationsPerSelected, double largeMutationFactor, int smallMutationsPerSelected, 
											double smallMutationFactor,  int numChildrenToBreed, int numToChoose, String fileOutput) throws NumberFormatException, IOException {
		Vector<StrokeNetwork> netpopulation = new Vector<StrokeNetwork>();
		EvolveNetworks learn = new EvolveNetworks(); learn.training = new TrainingSet(trainingset, numInputs);
		System.out.println(learn.training.toString());
		StrokeNetwork thisnet;
		for (int i=0; i < populationSize; i++) {
			thisnet =  StrokeNetworkBuilder.strokeNetworkBuilder2(true, 80, 0.5, 1);
			netpopulation.add(thisnet);
		}
		double[] fitnesses = getFitnesses(learn.training, netpopulation);
		//for (int i=0; i < fitnesses.length; i++) {
			//System.out.println("fitness " + i + ": " + fitnesses[i]);
		//}
		for (int i=0; i < numGenerations; i++) {
			System.out.println("generation " + i);
			netpopulation = evolvePopulation(netpopulation, fitnesses, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
					smallMutationFactor, numChildrenToBreed, numGenerations);
			fitnesses = getFitnesses(learn.training, netpopulation);
		}

		//for (int i=0; i < fitnesses.length; i++) {
			//System.out.println("fitness " + i + ": " + fitnesses[i]);
		//}
		
		int[] mostFit = findMostFit(netpopulation, fitnesses, numToSelect);
		//for (int i=0; i < mostFit.length; i++) {
			//System.out.println(fitnesses[mostFit[i]]);
		//}
		Random rand = new Random();
		StrokeNetwork[] theChosen = new StrokeNetwork[numToChoose]; 
		for (int i=0; i < theChosen.length; i++) {
			theChosen[i] = netpopulation.get(mostFit[rand.nextInt(mostFit.length)]);
			
			System.out.println(fitnesses[mostFit[rand.nextInt(mostFit.length)]]);
			StrokeNetworkBuilder.networkToFile(fileOutput + i, theChosen[i]);
		}		
		//Iterator<StrokeNetwork> iterator = netpopulation.listIterator();
		//while (iterator.hasNext()) {
		//	System.out.println(StrokeNetworkBuilder2.toString(iterator.next()));
		//}
		
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
																// numInputs,   populationSize,     numGenerations,    numToSelect,          largeMutationsPerSelected
		//buildAndEvolved("stroketrainingset/stroke0/set5.tr",         80,        10000,                500,                 100,                        40, 
																// largeMutationFactor,    smallMutationsPerSelected,    smallmMutationFactor,     numChildrenToBreed
																//	0.1,			         40,                          0.005,                     2000,
																// numToChoose,    fileOutput
															//		5,           "networkmodels/stroke0/test");
		
		int numInputs = 80; int populationSize = 10000; int numGenerations = 500; int numToSelect = 100; int largeMutationsPerSelected = 40; 
		double largeMutationFactor = 0.1; int smallMutationsPerSelected =40; double smallMutationFactor = 0.005; int numChildrenToBreed = 2000; 
		int numToChoose = 1;
		
		for (int i=1; i < 9; i++) {
			for (int j=0; j < 5; j++) {
				System.out.println("STROKE------------ " + i + "---MODEL-------" + j);
				buildAndEvolved("stroketrainingset/stroke" + i +"/set1.tr", numInputs, populationSize, numGenerations, numToSelect, largeMutationsPerSelected, largeMutationFactor, smallMutationsPerSelected,
									smallMutationFactor, numChildrenToBreed, numToChoose, "networkmodels/stroke" + i + "/model" + j);
			}
		}
		System.out.println("finished");
		
		//evolved
		
	}
	
	public static double[] getFitnesses(TrainingSet training, Vector<StrokeNetwork> networks) {
		double[] fitnesses = new double[networks.size()];
		for (int i=0; i < fitnesses.length; i++) {
				Iterator<TrainingElement> iterator = training.trainingelements.listIterator();
				StrokeNetwork network = networks.get(i); fitnesses[i] = 0;
				while (iterator.hasNext()) {
					TrainingElement next = iterator.next();
					fitnesses[i] += get1Fitness(next, network);
				}
		}
		return fitnesses;
	}
	public static double get1Fitness(TrainingElement trainer, StrokeNetwork network) {
		double output =0;
		double[] input = trainer.inputs;
		output = StrokeNetworkBuilder.getOutput(input, network);
		if (trainer.classification == 1) {
			return output;
		} else {
			return -1*output;
		}
	}
	
	public static Vector<StrokeNetwork> evolvePopulation(Vector<StrokeNetwork> netpopulation, double[] fitnesses, int numToSelect, int largeMutationsPerSelected, 
															double largeMutationFactor, int smallMutationsPerSelected, double smallMutationFactor, 
															int numChildrenToBreed, int numGenerations) {
		
		Vector<StrokeNetwork> newpopulation = new Vector<StrokeNetwork>();
		int[] mostFit = findMostFit(netpopulation, fitnesses, numToSelect);

		Vector<StrokeNetwork> mostfitnets = new Vector<StrokeNetwork>(); StrokeNetwork thisnet;
		for (int i=0; i < mostFit.length; i++) {
			thisnet = netpopulation.get(mostFit[i]);
			mostfitnets.add(thisnet);
		}
		newpopulation.addAll(mostfitnets);
		newpopulation.addAll(mutateAll(mostfitnets, largeMutationFactor, largeMutationsPerSelected, 1));

		newpopulation.addAll(mutateAll(mostfitnets, smallMutationFactor, smallMutationsPerSelected, 0));	
		//newpopulation.addAll(breed(mostfitnets, smallMutationFactor, numChildrenToBreed));
		
		return newpopulation;
	}

	public static Vector<StrokeNetwork> mutateAll(Vector<StrokeNetwork> parents, double mutationFactor, int numMutations, int allocateParent) {
		Vector<StrokeNetwork> mutations = new Vector<StrokeNetwork>();
		Iterator<StrokeNetwork> iterator = parents.listIterator();
		int counter = 0;
		while (iterator.hasNext()) {
			mutations.addAll(mutate1(iterator.next(), mutationFactor, numMutations, allocateParent));
			counter++;
		}
		return mutations;
	}
	public static Vector<StrokeNetwork> mutate1(StrokeNetwork parent, double mutationFactor, int numMutations, int allocateParent) {
		Vector<StrokeNetwork> mutations = new Vector<StrokeNetwork>();
		for (int i=0; i < numMutations-allocateParent; i++) {
			mutations.add(get1Mutation(parent, mutationFactor));
		}
		return mutations;
	}
	public static StrokeNetwork get1Mutation(StrokeNetwork parent, double mutationFactor) {
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
					} else {
						mutation.weights[k][h][z] = parent.weights[k][h][z] - rand*mutationFactor;
					}
				}
			}
		}
		for (int kk=0; kk < mutation.thresholds.length; kk++) {
			for (int hh=0; hh < mutation.thresholds[kk].length; hh++) {
				rand = Math.random();
				if (Math.random() < 0.5) {
					mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] + rand*mutationFactor;
				} else {
					mutation.thresholds[kk][hh] = parent.thresholds[kk][hh] - rand*mutationFactor;
				}
			}
		}
		return mutation;
	}
	public static int[] findMostFit(Vector<StrokeNetwork> netpopulation, double[] fitnesses, int numToSelect) {
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

	
}
