package recognition.features;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import recognition.strokes.StrokeList;
import trainingdata.character.ReadTrainingCharacters;

public class TrainingSet {
	public double[][] inputs;
	public int[] classifications;
	public double[] heights;
	public double[] widths;
	
	public TrainingSet(LinkedList<StrokeList> characters, boolean[] featureToggle) {
		inputs = new double[characters.size()][];
		classifications = new int[characters.size()];
		StrokeList character;
		InputFeatureCombiner features;
		
		Iterator<StrokeList> iterateCharacters = characters.iterator();
		int counter = 0;
		while (iterateCharacters.hasNext()) {
			character = iterateCharacters.next();
			features = new InputFeatureCombiner(featureToggle, character);
			inputs[counter] = features.inputVector;
			classifications[counter] = character.id;
			//heights[counter] = character.maxY - character.minY;
			//widths[counter] = character.maxX - character.minX;
			
			counter++;
		}
		
		
		
		
		
		
	}
	
	
	
	
	public TrainingSet(LinkedList<StrokeList> characters, int inputSize) {
		inputs = new double[characters.size()][inputSize];
		classifications = new int[characters.size()];
		StrokeList character;
		Grid grid = new Grid();
		Iterator<StrokeList> iterateCharacters = characters.iterator();
		int counter =0;
		while (iterateCharacters.hasNext()) {
			character = iterateCharacters.next();
			inputs[counter] = grid.getNetworkInputs(character);
			classifications[counter] = character.id;
			counter++;
		}
		
	}
	public TrainingSet(StrokeList character, boolean[] featureToggle) {
		inputs = new double[1][];
		classifications = new int[1];
		InputFeatureCombiner features;
		

			features = new InputFeatureCombiner(featureToggle, character);
			inputs[0] = features.inputVector;
			classifications[0] = -1;
		
	}

	
	public String toString() {
		String s = "";
		for (int i=0; i < inputs.length; i++) {
			s += "\ntraining element: " + i + ", desired classification: " +  classifications[i] + "\n   ";
			for (int j=0; j < inputs[i].length; j++) {
				s += inputs[i][j] + ", ";
			}
		}
		return s;
	}
	public static void countTrainingCharacters(LinkedList<StrokeList> characters, int numClassifications) {
		StrokeList character;
		Iterator<StrokeList> iterateCharacters = characters.iterator();
		int counter =0; int[] classCounter = new int[numClassifications];
		while (iterateCharacters.hasNext()) {
			character = iterateCharacters.next();
			classCounter[character.id]++;
			
			
			
			
			
			counter++;
		}
		for (int i=0; i < classCounter.length; i++) {
			System.out.println("class " + i + " counted " + classCounter[i] + " times");
		}
		System.out.println("total: " + counter);
	}
	
	public static void main(String[] args) throws IOException {
		//TrainingSet ts = new TrainingSet(ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-e_0.tr"), 45);
		//System.out.println(ts.toString());
		
		//TrainingSet ts2 = new TrainingSet(ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-e_1.tr").getFirst(), 36);
		//System.out.println(ts2.toString());
		
		//countTrainingCharacters(ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-e_0-1.tr"), 5);
		boolean[] features = new boolean[4];
			features[0] = true;
			features[1] = true;
			features[2] = true;
			features[3] = true;
		TrainingSet ts3 = new TrainingSet(ReadTrainingCharacters.getCharsFromFile("trainingcharacters/l_a-e_0.tr"), features);
		System.out.println(ts3.toString());
	}
}
