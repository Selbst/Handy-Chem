package testing;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;

import recognition.features.Grid;
import recognition.features.GridNetworkBuilder;
import recognition.features.GridNetworkTrainer;
import recognition.features.TrainingSet;
import recognition.features.training.EvolveWithParams;
import recognition.filter.Filter_Lowercase;
import recognition.filter.Filter_Uppercase;
import recognition.neuralnet_builders.FullyConnectedNNBuilder;
import recognition.strokes.StrokeList;
import trainingdata.character.ReadTrainingCharacters;

public class TestCharacterNetwork {
	public static void test1() throws IOException {
		LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-e_0.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-e_0.tr");

		Iterator<StrokeList> it = chars.iterator();
		StrokeList thisChar;
		Grid grid;
		StrokeNetwork classifier = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/test1.net");
		double fitnessTotal = 0;
		int desiredClass;
		double[] output;
		double[][] alloutputs = new double[chars.size()][5];
		int correct =0;
		int wrong = 0;
		int[] classifications = new int[chars.size()];
		int counter =0;
		while (it.hasNext()) {
			thisChar = it.next();
			desiredClass = thisChar.id;
			grid = new Grid(thisChar);
			output = GridNetworkBuilder.getOutputArray(grid.getFlattenedFeatures(), classifier);
			alloutputs[counter] = output;
			int classifiedAs = findMax(output);
			classifications[counter] = classifiedAs;
			counter++;
		
			if (classifiedAs == desiredClass) {
				System.out.println("---------------------------" + desiredClass);
				correct++;
			} else {
				wrong++;
			}
			fitnessTotal += GridNetworkTrainer.get1Fitness(grid.getFlattenedFeatures(), desiredClass, classifier);
			System.out.println("fitness: " + GridNetworkTrainer.get1Fitness(grid.getFlattenedFeatures(), desiredClass, classifier));
		}
		for (int i=0; i < 5; i++) {
			System.out.print(alloutputs[0][i] + ", ");
		}
		for (int i=0; i < classifications.length; i++) {
			System.out.print(classifications[i] + ", ");
		}System.out.println();
		System.out.println("number correct: " + correct);
		System.out.println("number wrong: " + wrong);
		System.out.println(fitnessTotal);
	}
	
	public static void test2() throws IOException {
		LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/l_a-e_0.ts");
		Iterator<StrokeList> it = chars.iterator();
		StrokeList thisChar;
		Grid grid;
		
		StrokeNetwork classifier = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/test2.net");
		System.out.println(GridNetworkBuilder.toString(classifier));
		int correct =0;
		int wrong = 0;
		int desiredClass;
		double[] output;
		double[][] outputs = new double[chars.size()][6];
		for (int i=0; i < 8; i++) {
			classifier = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/test" + i + ".net");
			it = chars.iterator();
			
			int counter =0;
			while (it.hasNext()) {
				thisChar = it.next();
				desiredClass = thisChar.id;
				System.out.println("desired class: " + desiredClass);
				grid = new Grid(thisChar);
				output = GridNetworkBuilder.getOutputArray(grid.getFlattenedFeatures(), classifier);
				for (int j=0; j < output.length; j++ ) {
					outputs[counter][j] += output[j];
				}
				outputs[counter][5] = desiredClass;
				counter++;
			}
		}
		
		for (int i=0; i < outputs.length; i++) {
			if (findMax(outputs[i]) == outputs[i][5]) {
				System.out.println(outputs[i][5]);
				correct++;
			} else { wrong++;
				System.out.println(outputs[i][5]);
			}
		}
		System.out.println("number correct: " + correct);
		System.out.println("number wrong: " + wrong);
		
	}
	
	public static void test3(StrokeNetwork classifier) throws IOException {
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-e_0.tr");
		LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/l_a-e_0.ts");
		TrainingSet trainer = new TrainingSet(chars, 69);
		
		double fitness = 0;
		
		fitness = 0;
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += GridNetworkTrainer.get1Fitness(trainer.inputs[i], trainer.classifications[i], classifier);
		}
		double fitness2 = 0;
		int correct=0, wrong = 0;
		double[] output = new double[5];
		int[] classifiedAs = new int[trainer.classifications.length];
		for (int i=0; i < trainer.inputs.length; i++) {
			output = GridNetworkBuilder.getOutputArray(trainer.inputs[i], classifier);
			if (findMax(output) == trainer.classifications[i]) {
				correct++;
				fitness2 += 10;
			} else {
				wrong++;
				fitness2 -= 10;
			}
			classifiedAs[i] = findMax(output);
		}
		
		
		System.out.println("test3 fitness: " + fitness + ", " + fitness2);
		System.out.println("number correct: " + correct);
		System.out.println("number wrong: " + wrong);
		for (int i=0; i < classifiedAs.length; i++) {
			System.out.println(classifiedAs[i] + " vs. " + trainer.classifications[i]);
		}
	}
	public static void binaryTest1(StrokeNetwork classifier, int classifierID, int num_possible) throws IOException {
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-z_3.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/choose_from_all/4_all.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/all_6.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/0.0.1.4.0.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/filtered/0.0.0.0.0.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/classifiers/temp/0vs13.ts");
		LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/vs/0vs5.ts");



		boolean[] toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = true;
		toggleFeatures[1] = false;
		toggleFeatures[2] = false;
		toggleFeatures[3] = false;
		
		TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
		
		double fitness = 0;
		int correctPositive =0, falsePositive=0, correctNegative=0, falseNegative=0;
		
		fitness = 0;
		
		
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1Fitness(trainer.inputs[i], trainer.classifications[i], classifier, classifierID, num_possible);
			System.out.println(fitness);
		}
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1FitnessFromTestSet(trainer.inputs[i], trainer.classifications[i], classifier, classifierID);
		}
		
		System.out.println("fitness: " + fitness);
		double output;
		for (int i=0; i < trainer.inputs.length; i++) {
			output = FullyConnectedNNBuilder.getOutput(trainer.inputs[i], classifier);
			System.out.println("classifies " + trainer.classifications[i] + " with output: " + output);
			if (classifierID == trainer.classifications[i]) {
				if (output > 0.0) {
					correctPositive++;
				} else {
					falseNegative++;
				}
			} else {
				if (output > 0.0) {
					falsePositive++;
				} else {
					correctNegative++;
				}
			}
		}
		System.out.println("correct positive: " + correctPositive);
		System.out.println("false negative: " + falseNegative);
		System.out.println("false positive: " + falsePositive);
		System.out.println("correct negative: " + correctNegative);
		
	}

	public static void binaryTest2(StrokeNetwork classifier, int classifierID) throws IOException {
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-z_3.tr");
		LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/l_a-z_0.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/a-test.ts");


		
		TrainingSet trainer = new TrainingSet(chars, 53);
		
		double fitness = 0;
		int correctPositive =0, falsePositive=0, correctNegative=0, falseNegative=0;
		
		fitness = 0;
		
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1FitnessFromTestSet(trainer.inputs[i], trainer.classifications[i], classifier, classifierID);
		}
		
		System.out.println("fitness: " + fitness);
		double output;
		for (int i=0; i < trainer.inputs.length; i++) {
			output = FullyConnectedNNBuilder.getOutput(trainer.inputs[i], classifier);
			System.out.println("classifies " + trainer.classifications[i] + " with output: " + output);
			if (classifierID == trainer.classifications[i]) {
				if (output > 0.0) {
					correctPositive++;
				} else {
					falseNegative++;
				}
			} else {
				if (output > 0.0) {
					falsePositive++;
				} else {
					correctNegative++;
				}
			}
		}
		System.out.println("correct positive: " + correctPositive);
		System.out.println("false negative: " + falseNegative);
		System.out.println("false positive: " + falsePositive);
		System.out.println("correct negative: " + correctNegative);
		
	}
	public static void binaryTest3(StrokeNetwork classifier, int classifierID, int num_possible, int[] chars_to_choose, boolean[] toggleFeatures) throws IOException {
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-z_3.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/choose_from_all/4_all.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/all_6.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/0.0.1.4.0.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/filtered/0.0.0.0.0.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/classifiers/temp/0vs13.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/vs/0vs5.ts");

		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		for (int i=0; i < chars_to_choose.length; i++) {
			for (int j=0; j < 8; j++) {
				chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/" + j + "_" + chars_to_choose[i] + ".ts"));
			}
		}

		
		TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
		chars.clear();
		double fitness = 0;
		int correctPositive =0, falsePositive=0, correctNegative=0, falseNegative=0;
		
		fitness = 0;
		
		
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1Fitness(trainer.inputs[i], trainer.classifications[i], classifier, classifierID, num_possible);
			System.out.println(fitness);
		}
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1FitnessFromTestSet(trainer.inputs[i], trainer.classifications[i], classifier, classifierID);
		}
		
		System.out.println("fitness: " + fitness);
		double output;
		for (int i=0; i < trainer.inputs.length; i++) {
			output = FullyConnectedNNBuilder.getOutput(trainer.inputs[i], classifier);
			System.out.println("classifies " + trainer.classifications[i] + " with output: " + output);
			if (classifierID == trainer.classifications[i]) {
				if (output > 0.0) {
					correctPositive++;
				} else {
					falseNegative++;
				}
			} else {
				if (output > 0.0) {
					falsePositive++;
				} else {
					correctNegative++;
				}
			}
		}
		System.out.println("correct positive: " + correctPositive);
		System.out.println("false negative: " + falseNegative);
		System.out.println("false positive: " + falsePositive);
		System.out.println("correct negative: " + correctNegative);
		
	}
	public static void binaryTest3_validation(StrokeNetwork classifier, int classifierID, int num_possible, int[] chars_to_choose, boolean[] toggleFeatures) throws IOException {
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-z_3.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/choose_from_all/4_all.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/all_6.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/0.0.1.4.0.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/filtered/0.0.0.0.0.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/classifiers/temp/0vs13.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/vs/0vs5.ts");

		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		for (int i=0; i < chars_to_choose.length; i++) {
			for (int j=0; j < 2; j++) {
				chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/1/" + j + "_" + chars_to_choose[i] + ".ts"));
			}
		}

		
		TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
		chars.clear();
		
		double fitness = 0;
		int correctPositive =0, falsePositive=0, correctNegative=0, falseNegative=0;
		
		fitness = 0;
		
		
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1Fitness(trainer.inputs[i], trainer.classifications[i], classifier, classifierID, num_possible);
			System.out.println(fitness);
		}
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1FitnessFromTestSet(trainer.inputs[i], trainer.classifications[i], classifier, classifierID);
		}
		
		System.out.println("fitness: " + fitness);
		double output;
		for (int i=0; i < trainer.inputs.length; i++) {
			output = FullyConnectedNNBuilder.getOutput(trainer.inputs[i], classifier);
			System.out.println("classifies " + trainer.classifications[i] + " with output: " + output);
			if (classifierID == trainer.classifications[i]) {
				if (output > 0.0) {
					correctPositive++;
				} else {
					falseNegative++;
				}
			} else {
				if (output > 0.0) {
					falsePositive++;
				} else {
					correctNegative++;
				}
			}
		}
		System.out.println("correct positive: " + correctPositive);
		System.out.println("false negative: " + falseNegative);
		System.out.println("false positive: " + falsePositive);
		System.out.println("correct negative: " + correctNegative);
		
	}
	
	public static void binaryTest3_caps(StrokeNetwork classifier, int classifierID, int num_possible, int[] chars_to_choose, boolean[] toggleFeatures) throws IOException {

		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		for (int i=0; i < chars_to_choose.length; i++) {
			for (int j=0; j < 7; j++) {
				chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/cap_" + j + "_" + chars_to_choose[i] + ".ts"));
			}
		}

		
		TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
		chars.clear();
		double fitness = 0;
		int correctPositive =0, falsePositive=0, correctNegative=0, falseNegative=0;
		
		fitness = 0;
		
		
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1Fitness(trainer.inputs[i], trainer.classifications[i], classifier, classifierID, num_possible);
			System.out.println(fitness);
		}
		
		for (int i=0; i < trainer.inputs.length; i++) {
			fitness += EvolveWithParams.get1FitnessFromTestSet(trainer.inputs[i], trainer.classifications[i], classifier, classifierID);
		}
		
		System.out.println("fitness: " + fitness);
		double output;
		for (int i=0; i < trainer.inputs.length; i++) {
			output = FullyConnectedNNBuilder.getOutput(trainer.inputs[i], classifier);
			System.out.println("classifies " + trainer.classifications[i] + " with output: " + output);
			if (classifierID == trainer.classifications[i]) {
				if (output > 0.0) {
					correctPositive++;
				} else {
					falseNegative++;
				}
			} else {
				if (output > 0.0) {
					falsePositive++;
				} else {
					correctNegative++;
				}
			}
		}
		System.out.println("correct positive: " + correctPositive);
		System.out.println("false negative: " + falseNegative);
		System.out.println("false positive: " + falsePositive);
		System.out.println("correct negative: " + correctNegative);
		
	}
	
	public static double binaryTest4_vs(StrokeNetwork classifier, double[][] inputs, int[] classifications, int low) throws IOException {
		double correct = 0;
		double total = 0;
		
		for (int i=0; i < inputs.length; i++) {
			if (StrokeNetworkBuilder.getOutput(inputs[i], classifier) > 0) {
				if (classifications[i] == low) {
					correct++;
				}
			} else {
				if (classifications[i] != low) {
					correct++;
				}
			}
			total++;
		}
		double score = correct / total;
		return score;
				
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		//test3(GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/test3.net"));
		//System.out.println(FullyConnectedNNBuilder.toString(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testera0.net")));

		
		//binaryTest1(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/15_1_0.0.1.4.0.1.0.0.0.__10.net"), 15, 5);
		//binaryTest1(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/0vs5_1.net"), 0, 2);
		
		int[] chars_to_choose = new int[2];
		chars_to_choose[0] = 1;
		chars_to_choose[1] = 10;	
		
		boolean[] toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = false;
		toggleFeatures[1] = true;
		toggleFeatures[2] = false;
		toggleFeatures[3] = false;
		binaryTest3(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/1vs10_0.net"), 1, 2, chars_to_choose, toggleFeatures);

		chars_to_choose = new int[12];
		chars_to_choose[0] = 0;
		chars_to_choose[1] = 1;
		chars_to_choose[2] = 3;
		chars_to_choose[3] = 4;
		chars_to_choose[4] = 5;
		chars_to_choose[5] = 9;
		chars_to_choose[6] = 10;
		chars_to_choose[7] = 14;
		chars_to_choose[8] = 15;
		chars_to_choose[9] = 17;
		chars_to_choose[10] = 23;
		chars_to_choose[11] = 24;
		
		toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = true;
		toggleFeatures[1] = false;
		toggleFeatures[2] = false;
		toggleFeatures[3] = false;
		
		//binaryTest3_caps(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/caps/filtered/23__1_0.0.0.3.1.1.0.0.0.__10.net"), 23, 12, chars_to_choose, toggleFeatures);
		
		
		chars_to_choose = new int[10];
		chars_to_choose[0] = 5;
		chars_to_choose[1] = 6;
		chars_to_choose[2] = 11;
		chars_to_choose[3] = 13;
		chars_to_choose[4] = 14;
		chars_to_choose[5] = 15;
		chars_to_choose[6] = 18;
		chars_to_choose[7] = 22;
		chars_to_choose[8] = 23;
		chars_to_choose[9] = 24;
		
		//binaryTest3(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/5_2_0.0.0.3.0.1.0.0.0.__10.net"), 5, 10, chars_to_choose, toggleFeatures);

		
		//binaryTest3_validation(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/5_2_0.0.0.3.0.1.0.0.0.__10.net"), 5, 10, chars_to_choose, toggleFeatures);
		//binaryTest3(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/5_1_0.0.0.3.0.1.0.0.0.__10.net"), 5, 10, chars_to_choose, toggleFeatures);
		chars_to_choose = new int[2];
		chars_to_choose[0] = 2;
		chars_to_choose[1] = 14;
		toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = false;
		toggleFeatures[1] = true;
		toggleFeatures[2] = false;
		toggleFeatures[3] = false;
		
		//binaryTest3_caps(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/caps/filtered/vs/2vs14_0.net"), 2, 2, chars_to_choose, toggleFeatures);
		
		/*
		String testingset = "testingcharacters/filtered/0.0.0.0.0.ts";
		LinkedList<StrokeList> testingChars = ReadTrainingCharacters.getCharsFromFile(testingset);
		

		TrainingSet testSet = new TrainingSet(testingChars, toggleFeatures);
		
		double bestNetFitness = 0.0;
		StrokeNetwork bestNetModel = FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/11_0_0.0.0.0.0.1.0.0.0.__10.net");

		for (int j=0; j < testSet.inputs.length; j++) {
			bestNetFitness += EvolveWithParams.get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetModel, 1);
		}
		System.out.println(bestNetFitness);
		//System.out.println(FullyConnectedNNBuilder.toString(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testeraa10.net")));
		 
		*/
		
		// COMPARE DIFFERENT CLASSIFIERS.
		/*
		int[][] paths = new int[48][];
		int[][] possible_chars_per_path = new int[48][];
		
		for (int i=0; i < 2; i++) {
			for (int j=0; j < 2; j++) {
				for (int z = 3; z < 7; z++) {
					for (int q = 0; q < 3; q++) {
						int[] path = new int[5];
						path[0] = 0;
						path[1] = i;
						path[2] = j;
						path[3] = z;
						path[4] = q;
						System.out.println("" + (i*2*4*3 + j*4*3 + (z-3)*3 +q));
						paths[i*2*4*3 + j*4*3 + (z-3)*3 +q] = path;
					}
				}
			}
		}
		String[] path_strings = new String[48];
		
		for (int z=0; z < path_strings.length; z++) {
			path_strings[z] = paths[z][0] + "." + paths[z][1] + "." + paths[z][2] + "." + paths[z][3] + "." + paths[z][4] + "."; 
		}

		Filter_Lowercase f = new Filter_Lowercase();
		double[][][] success_rate = new double[paths.length][][];

		for (int i=0; i < paths.length; i++) {
			
			possible_chars_per_path[i] = f.filter(paths[i]);
			LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
			for (int k=0; k < possible_chars_per_path[i].length; k++) {
				for (int j=0; j < 8; j++) {
					chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/" + j + "_" + possible_chars_per_path[i][k] + ".ts"));
				}
			}
			for (int k=0; k < possible_chars_per_path[i].length; k++) {
				for (int j=0; j < 2; j++) {
					chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/1/" + j + "_" + possible_chars_per_path[i][k] + ".ts"));
				}
			}
			
			double[][] path_success_rate = new double[possible_chars_per_path[i].length][2];
			
			for (int k=0; k < possible_chars_per_path[i].length; k++) {
				try {
					StrokeNetwork classifier = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + possible_chars_per_path[i][k] + "__" + "2" + "_" + paths[i] + "1.0.0.0.__10.net");

				} catch(Error e) {
					path_success_rate[k][0] = 0;
				}
				try {
					
				} catch(Error e) {
					path_success_rate[k][1] = 0;
				}
			}
			chars.clear();
		}		
		*/
		
		boolean[] features_2x2 = new boolean[4]; 
		features_2x2[0] = false;
		features_2x2[1] = true;
		features_2x2[2] = false;
		features_2x2[3] = false;
		
		boolean[] features_3x3 = new boolean[4]; 
		features_3x3[0] = true;
		features_3x3[1] = false;
		features_3x3[2] = false;
		features_3x3[3] = false;
		
		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		double[][][] success_rates = new double[24][][];
		boolean[][] best_net_3x3 = new boolean[25][25];
		for (int i=0; i < 24; i++) {
			double[][] success_rates_i = new double[24 - i][2];
			for (int j=i+1; j < 25; j++) {
					for (int k=0; k < 8; k++) {
						chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/" + k + "_" + i + ".ts"));
						chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/" + k + "_" + j + ".ts"));
					}
					for (int k=0; k < 2; k++) {
						chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/1/" + k + "_" + i + ".ts"));
						chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/1/" + k + "_" + j + ".ts"));
					}
				
				
				TrainingSet trainer = new TrainingSet(chars, features_2x2);
				double[][] inputs_2x2 = trainer.inputs;
				trainer = new TrainingSet(chars, features_3x3);
				double[][] inputs_3x3 = trainer.inputs;
				int[] classifications = trainer.classifications;
				chars.clear();
								
				StrokeNetwork classifier_2x2 = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + i + "vs" + j + "_0.net");
				
				try {
					StrokeNetwork classifier_3x3 = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + i + "vs" + j + "_1.net");
					success_rates_i[j - (i + 1)][1] = binaryTest4_vs(classifier_3x3, inputs_3x3, classifications, i);	
				} catch (Exception e) {
					success_rates_i[j - (i + 1)][1] = 0;
				}

				success_rates_i[j - (i + 1)][0] = binaryTest4_vs(classifier_2x2, inputs_2x2, classifications, i);
				//System.out.println(i + " vs " + j);
				//System.out.println("2x2: " + success_rates_i[j - (i + 1)][0]);
				//System.out.println("3x3: " + success_rates_i[j - (i + 1)][1] + "\n");
				
				if ((success_rates_i[j - (i + 1)][1] - success_rates_i[j - (i + 1)][0]) > 0.1) {
					best_net_3x3[i][j] = true;
					System.out.println("best_net[" + i + "][" + j + "] = true;");
				}
			}
			success_rates[i] = success_rates_i;
		}
		
		
		
		
		
	
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
	
}
