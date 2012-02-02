package testing;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;

import recognition.features.BinaryGridNetworkTrainer;
import recognition.features.Grid;
import recognition.features.GridNetworkBuilder;
import recognition.features.GridNetworkTrainer;
import recognition.features.TrainingSet;
import recognition.features.training.EvolveWithParams;
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
	public static void binaryTest1(StrokeNetwork classifier, int classifierID) throws IOException {
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-z_3.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/choose_from_all/4_all.tr");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/all_6.ts");
		LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/0.0.0.0.0.ts");
		//LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/filtered/0.0.0.0.0.tr");


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
			fitness += EvolveWithParams.get1Fitness(trainer.inputs[i], trainer.classifications[i], classifier, classifierID);
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
	public static void main(String[] args) throws NumberFormatException, IOException {
		//test3(GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/test3.net"));
		//System.out.println(FullyConnectedNNBuilder.toString(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testera0.net")));

		
		binaryTest1(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/22_0_0.0.0.3.0.1.0.0.0.__10.net"), 22);
		
		String testingset = "testingcharacters/filtered/0.0.0.0.0.ts";
		LinkedList<StrokeList> testingChars = ReadTrainingCharacters.getCharsFromFile(testingset);
		
		boolean[] toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = true; // 3x3
		toggleFeatures[1] = false; // 2x2
		toggleFeatures[2] = false;
		toggleFeatures[3] = false;
		TrainingSet testSet = new TrainingSet(testingChars, toggleFeatures);
		
		double bestNetFitness = 0.0;
		StrokeNetwork bestNetModel = FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/11_0_0.0.0.0.0.1.0.0.0.__10.net");

		for (int j=0; j < testSet.inputs.length; j++) {
			bestNetFitness += EvolveWithParams.get1FitnessFromTestSet(testSet.inputs[j], testSet.classifications[j], bestNetModel, 1);
		}
		System.out.println(bestNetFitness);
		//System.out.println(FullyConnectedNNBuilder.toString(FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testeraa10.net")));
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
