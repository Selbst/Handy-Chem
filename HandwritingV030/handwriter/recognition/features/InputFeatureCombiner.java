package recognition.features;

import java.io.IOException;
import java.util.LinkedList;

import recognition.features.grid.Grid2;
import recognition.strokes.StrokeList;
import trainingdata.character.ReadTrainingCharacters;

public class InputFeatureCombiner {
	double[] gridFeatures3x3;
	double[] gridFeatures2x2;
	double[] diagonalPartition;
	int[] lineIntersections;
	double[] pixelDensities;
	double heightToWidthRatio; int heightWidthToggle;
	double[] pixelDistanceFromCenter;
	
	int centersToggle;
	
	double above;
	double right;
	double[] inputVector;
	
	int meridian_intersections;
	int equator_intersections;
	
	int intersectionsToggle;
	public double[] get_input_vector() {
		return inputVector;
	}
	public InputFeatureCombiner(boolean[] featureToggle, StrokeList character) {
		Grid2 gridFeatures = new Grid2(character);
		
		if (!featureToggle[0]) {
			gridFeatures3x3 = new double[0];
		} else {
			gridFeatures3x3 = gridFeatures.getFlattenedFeatures3x3();
		}
		if (!featureToggle[1]) {
			gridFeatures2x2 = new double[0];
		} else {
			gridFeatures2x2 = gridFeatures.getFlattenedFeatures2x2();
		}
		if (!featureToggle[2]) {
			diagonalPartition = new double[0];
		} else {
			diagonalPartition = gridFeatures.getFlattenedFeaturesDiagonal();
		}
		if (!featureToggle[3]) {
			heightWidthToggle = 0;
		} else {
			heightWidthToggle = 1;
			heightToWidthRatio = Grid2.getHeightWidthRatio(character);
		} 
		if (featureToggle.length > 4) {
			if (!featureToggle[4]) {
				centersToggle = 0;
			} else {
				
				
				centersToggle = 2;
				double[] centers = Barycenter.get_mass_centers(character);

				above =  centers[2];
				right =  centers[3];
				
			}
		}
		if (featureToggle.length > 5) {
			if (!featureToggle[5]) {
				intersectionsToggle = 0;
			} else {
				intersectionsToggle = 2;
				int[] intersections = Intersections.get_intersection(character);
				meridian_intersections = intersections[0];
				equator_intersections = intersections[1];
			}
		}

		
		
		inputVector = new double[gridFeatures3x3.length + gridFeatures2x2.length + diagonalPartition.length + heightWidthToggle + centersToggle + intersectionsToggle];
		
		int counter = 0;
		for (int i=0; i < gridFeatures3x3.length; i++) {
			inputVector[counter] = gridFeatures3x3[i];
			counter++;
		}
		for (int i=0; i < gridFeatures2x2.length; i++) {
			inputVector[counter] = gridFeatures2x2[i];
			counter++;
		} 
		for (int i=0; i < diagonalPartition.length; i++) {
			inputVector[counter] = diagonalPartition[i];
			counter++;
		}
		if (featureToggle[3] == true) {
			inputVector[counter] = heightToWidthRatio;
			counter++;
		}
		if (featureToggle.length > 4) {
			//System.out.println(center_mass_x);

			if (featureToggle[4]) {
				inputVector[counter] = above;
				counter++;
				inputVector[counter] = right;
				counter++;
			}
		}
		if (featureToggle.length > 5) {
			if (featureToggle[5]) {
				inputVector[counter] = meridian_intersections;
				counter++;
				inputVector[counter] = equator_intersections;
				counter++;

			}
		}
	}
	
	public static int getVectorSize(boolean[] toggleFeatures) {
		int[] numInputsPerFeature = new int[5]; 
			numInputsPerFeature[0] = 36; numInputsPerFeature[1] = 16;	numInputsPerFeature[2] = 16; numInputsPerFeature[3] = 2;
			numInputsPerFeature[4] = 2;
		int size =0;
		for (int i=0; i < toggleFeatures.length; i++) {
			if (toggleFeatures[i]) {
				size += numInputsPerFeature[i];
			}
		}
		return size;
	}
	
	public static void main(String[] args) throws IOException {
		boolean[] toggleFeatures = new boolean[6];
			toggleFeatures[0] = true;
			toggleFeatures[1] = false;
			toggleFeatures[2] = false;
			toggleFeatures[3] = true;
			toggleFeatures[4] = true;
			toggleFeatures[5] = true;
			
		String trainingset = "trainingcharacters/choose_from_all/7_all.tr";
		LinkedList<StrokeList> trainingChars = ReadTrainingCharacters.getCharsFromFile(trainingset);

		TrainingSet trainer = new TrainingSet(trainingChars, toggleFeatures);
		
		double[][] inputs = trainer.inputs;
	
		int[] count_right = new int[25];
		int[] count_above = new int[25];
		int[] count_below = new int[25];
		int[] count_left = new int[25];
		
		for (int i=0; i < inputs.length; i++) {
			if (inputs[i][37] > 0) {
				count_above[trainer.classifications[i]]++;
			} else if (inputs[i][37] < 0) {
				count_below[trainer.classifications[i]]++;
			}
			if (inputs[i][38] > 0) {
				count_right[trainer.classifications[i]]++;
			} else if (inputs[i][38] < 0) {
				count_left[trainer.classifications[i]]++;
			}
		}
		
		
		for (int i=0; i < count_below.length; i++) {
			System.out.println(i + ":  down: " + count_below[i] + ", up: " + count_above[i]);
		}
		
		trainer = new TrainingSet(trainingChars, toggleFeatures);
		
		inputs = trainer.inputs;
		
		int[][] intersections_correct_count = new int[25][2];
		Intersections k = new Intersections();
		int[][] expected = k.get_expected_lowercase();
		for (int i=0; i < trainer.inputs.length; i++) {
			if (trainer.inputs[i][39] == expected[trainer.classifications[i]][1]) {
				intersections_correct_count[trainer.classifications[i]][0]++;
			}
			if (trainer.inputs[i][40] == expected[trainer.classifications[i]][0]) {
				intersections_correct_count[trainer.classifications[i]][1]++;
			}
		}
		for (int i=0; i < intersections_correct_count.length; i++) {
			System.out.println(i + ": " + "(" + intersections_correct_count[i][0] + ", " + intersections_correct_count[i][1] + ")");
		}
		
		
		trainingset = "trainingcharacters/choose_from_all/7_all.tr";
		trainingChars.clear();
				
		for (int j = 0; j < 26; j++) {
			for (int i=0; i < 4; i++) {
				trainingChars.addAll(ReadTrainingCharacters.getCharsFromFile("trainingcharacters/choose_from_all/cap_" + i  + "_" + j + ".tr"));
				
				if ( i != 3) {
					trainingChars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/cap_" + i  + "_" + j + ".ts"));
				}
			}		
		}
		trainer = new TrainingSet(trainingChars, toggleFeatures);
		inputs = trainer.inputs;
		
		intersections_correct_count = new int[26][2];
		k = new Intersections();
		expected = k.get_expected_uppercase();
		for (int i=0; i < trainer.inputs.length; i++) {
			if (trainer.inputs[i][39] == expected[trainer.classifications[i]][1]) {
				intersections_correct_count[trainer.classifications[i]][0]++;
			}
			if (trainer.inputs[i][40] == expected[trainer.classifications[i]][0]) {
				intersections_correct_count[trainer.classifications[i]][1]++;
			}
		}
		for (int i=0; i < intersections_correct_count.length; i++) {
			System.out.println(i + ": " + "(merid= " + intersections_correct_count[i][0] + ", equat=" + intersections_correct_count[i][1] + ")");
		}
		
		count_right = new int[26];
		count_above = new int[26];
		count_below = new int[26];
		count_left = new int[26];
		
		for (int i=0; i < inputs.length; i++) {
			if (inputs[i][37] > 0) {
				count_above[trainer.classifications[i]]++;
			} else if (inputs[i][37] < 0) {
				count_below[trainer.classifications[i]]++;
			}
			if (inputs[i][38] > 0) {
				count_right[trainer.classifications[i]]++;
			} else if (inputs[i][38] < 0) {
				count_left[trainer.classifications[i]]++;
			}
		}
		
		
		for (int i=0; i < count_below.length; i++) {
			System.out.println(i + ":  down: " + count_below[i] + ", up: " + count_above[i]);
		}
		for (int i=0; i < count_below.length; i++) {
			System.out.println(i + ":  right: " + count_right[i] + ", left: " + count_left[i]);
		}
	}

	
}
