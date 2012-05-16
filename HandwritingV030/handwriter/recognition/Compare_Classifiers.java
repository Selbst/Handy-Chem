package recognition;

import java.io.IOException;
import java.util.LinkedList;

import recognition.features.TrainingSet;
import recognition.neuralnet_builders.FullyConnectedNNBuilder;
import recognition.strokes.StrokeList;
import testing.TestCharacterNetwork;
import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;

public class Compare_Classifiers {

	/*
	 * return false (left branch)
	 * return true (right branch)
	 */
	public static boolean compare_branches(StrokeList character, int[] left_chars, int[] right_chars, StrokeNetwork[] classifiers, int[] possible_chars) {
		boolean[] toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = true; // 3x3
		toggleFeatures[1] = false; // 2x2
		toggleFeatures[2] = false;
		toggleFeatures[3] = false;
		double[] inputs;
		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		chars.add(character);
		
		TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
		inputs = trainer.inputs[0];
		double left_counter = 0;
		double right_counter = 0;
		double output;
		double left_output =0;
		for (int i=0; i < classifiers.length; i++) {
			output = StrokeNetworkBuilder.getOutput(inputs, classifiers[i]);
			System.out.print("classifier " + possible_chars[i] + ": " + output);
			
			if (!find(left_chars, right_chars, possible_chars[i])) {
				left_counter += output;
				System.out.println(", L");
			} else {
				right_counter += output;
				System.out.println(", R");
			}
			/*
			if (output > 0.2) {
				if (!find(left_chars, right_chars, possible_chars[i])) {
					left_counter++;
				} else {
					right_counter++;
				}
			}
			*/
			
		}
		left_counter = left_counter / left_chars.length;
		right_counter = right_counter / right_chars.length;
		if (left_counter >= right_counter) {
			return false;
		} else {
			return true;
		}
		
	}
	public static boolean find(int[] left_chars, int[] right_chars, int char_index) {
		for (int i=0; i < left_chars.length; i++) {
			if (char_index == left_chars[i]) {
				return false;
			}
		}
		return true;
		
	}
	public static int get_classification(StrokeList character, StrokeNetwork[] classifiers, int[] possible_chars) throws NumberFormatException, IOException {
		boolean[] toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = true; // 3x3
		toggleFeatures[1] = false; // 2x2
		toggleFeatures[2] = false;
		toggleFeatures[3] = false;
		double[] inputs_3x3;
		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		chars.add(character);
		
		TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
		inputs_3x3 = trainer.inputs[0];
		toggleFeatures[0] = false;
		toggleFeatures[1] = true;
		TrainingSet trainer2 = new TrainingSet(chars, toggleFeatures);
		double[] inputs_2x2 = trainer2.inputs[0];
		
		double max_output = Double.NEGATIVE_INFINITY;
		int max_index = -1;
		double second_max_output = Double.NEGATIVE_INFINITY;
		int second_max_index = -1;
		for (int i=0; i < classifiers.length; i++) {
			double output = StrokeNetworkBuilder.getOutput(inputs_3x3, classifiers[i]);
			System.out.println("classification 2: " + output);
			if (output > max_output) {
				second_max_output = max_output;
				second_max_index = max_index;
				
				max_output = output;

				max_index = i;
				//System.out.println("0: " + max_index + ", " + max_output + "; " + second_max_output + ", " + second_max_index);
				
			} else if (output > second_max_output) {
				second_max_output = output;
				second_max_index = i;
				//System.out.println("1: " + max_index + ", " + max_output + "; " + second_max_output + ", " + second_max_index);

			}
		}
		System.out.println("possible chars: " + possible_chars.length);

		/*
		 * last decision.
		 */
		if (possible_chars[second_max_index] < possible_chars[max_index]) {
			int temp = max_index;
			max_index = second_max_index;
			second_max_index = temp;
		}
		
		boolean[][] best_net = new boolean[25][25];
		best_net[11][20] = true;
		best_net[13][20] = true;
		best_net[13][21] = true;
		best_net[18][23] = true;
		
		double final_decision = 0;
		System.out.println(possible_chars[max_index] + " vs " +  possible_chars[second_max_index]);
		if (best_net[possible_chars[max_index]][possible_chars[second_max_index]]) {
			final_decision = StrokeNetworkBuilder.getOutput(inputs_3x3, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + possible_chars[max_index] + "vs" + possible_chars[second_max_index] + "_1.net"));
		} else {
			final_decision = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + possible_chars[max_index] + "vs" + possible_chars[second_max_index] + "_0.net"));
		}
		System.out.println(final_decision);
		
		
		
		
		
		
		int[][] max_confusions = new int[25][3];
		max_confusions[0][0] = 6;
		max_confusions[0][1] = 22;
		max_confusions[0][2] = 3;
		max_confusions[1][0] = 10;
		max_confusions[1][1] = 17;
		max_confusions[1][2] = 7;
		max_confusions[2][0] = 5;
		max_confusions[2][1] = 14;
		max_confusions[2][2] = 24;
		max_confusions[3][0] = 21;
		max_confusions[3][1] = 9;
		max_confusions[3][2] = 19;
		max_confusions[4][0] = 6;
		max_confusions[4][1] = 15;
		max_confusions[4][2] = 2;
		max_confusions[5][0] = 16;
		max_confusions[5][1] = 14;
		max_confusions[5][2] = 12;
		max_confusions[6][0] = 11;
		max_confusions[6][1] = 23;
		max_confusions[6][2] = 15;
		max_confusions[7][0] = 11;
		max_confusions[7][1] = 1;
		max_confusions[7][2] = 22;
		max_confusions[8][0] = 3;
		max_confusions[8][1] = 14;
		max_confusions[8][2] = 6;
		max_confusions[9][0] = 18;
		max_confusions[9][1] = 3;
		max_confusions[9][2] = 23;
		max_confusions[10][0] = 22;
		max_confusions[10][1] = 1;
		max_confusions[10][2] = 7;
		max_confusions[11][0] = 6;
		max_confusions[11][1] = 12;
		max_confusions[11][2] = 14;
		max_confusions[12][0] = 9;
		max_confusions[12][1] = 1;
		max_confusions[12][2] = 11;
		max_confusions[13][0] = 0;
		max_confusions[13][1] = 12;
		max_confusions[13][2] = 14;
		max_confusions[14][0] = 4;
		max_confusions[14][1] = 16;
		max_confusions[14][2] = 12;
		max_confusions[15][0] = 14;
		max_confusions[15][1] = 23;
		max_confusions[15][2] = 4;
		max_confusions[16][0] = 20;
		max_confusions[16][1] = 7;
		max_confusions[16][2] = 11;
		max_confusions[17][0] = 4;
		max_confusions[17][1] = 24;
		max_confusions[17][2] = 14;
		max_confusions[18][0] = 15;
		max_confusions[18][1] = 16;
		max_confusions[18][2] = 24;
		max_confusions[19][0] = 20;
		max_confusions[19][1] = 21;
		max_confusions[19][2] = 2;
		max_confusions[20][0] = 19;
		max_confusions[20][1] = 21;
		max_confusions[20][2] = 2;
		max_confusions[21][0] = 10;
		max_confusions[21][1] = 20;
		max_confusions[21][2] = 19;
		max_confusions[22][0] = 0;
		max_confusions[22][1] = 4;
		max_confusions[22][2] = 10;
		max_confusions[23][0] = 10;
		max_confusions[23][1] = 11;
		max_confusions[23][2] = 6;
		max_confusions[24][0] = 18;
		max_confusions[24][1] = 1;
		max_confusions[24][2] = 2;
		
		int decision_index = -1;
		
		if (final_decision > 0) {
			decision_index =  possible_chars[max_index];
		} else {
			decision_index = possible_chars[second_max_index];
		}


		
		int[] win_count = new int[4];
		int decision_index_holder = decision_index;
		for (int i=-1; i < max_confusions[decision_index].length-3; i++) {
			for (int j=i+1; j < max_confusions[decision_index].length-2; j++) {
				if (i != -1) {
					decision_index = max_confusions[decision_index][i];
				}
				
				if (decision_index < max_confusions[decision_index][j]) {
					if (best_net[decision_index][max_confusions[decision_index][j]]) {
						final_decision = StrokeNetworkBuilder.getOutput(inputs_3x3, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + decision_index + "vs" + max_confusions[decision_index][j] + "_1.net"));
					} else {
						final_decision = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + decision_index + "vs" + max_confusions[decision_index][j] + "_0.net"));
					}
					if (final_decision > 0) {
						win_count[i+1]++;
					} else {
						win_count[j+1]++;
					}
				} else {
					if (best_net[max_confusions[decision_index][j]][decision_index]) {
						final_decision = StrokeNetworkBuilder.getOutput(inputs_3x3, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + max_confusions[decision_index][j] + "vs" + decision_index + "_1.net"));
					} else {
						final_decision = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + max_confusions[decision_index][j] + "vs" + decision_index + "_0.net"));
					}
					if (final_decision > 0) {
						win_count[j+1]++;
					} else {
						win_count[i+1]++;
					}
				}
			}
		}
		int max_wins = win_count[0];
		int maxdex = 0;
		for (int i=1; i < win_count.length; i++) {
			if (win_count[i] > max_wins) {
				maxdex = i;
				max_wins = win_count[i];
			}
		}
		if (maxdex == 0) {
			return decision_index_holder;
		} else {
			return max_confusions[decision_index][maxdex-1];
		}
		
		//return decision_index;
		
		/*
		if (final_decision > 0) {
			if (possible_chars[max_index] != 0) {
				final_decision = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + 0 + "vs" + possible_chars[max_index] + "_0.net"));
				System.out.println("a? " + final_decision);

				if (final_decision > 0.1) {
					return 0;
				} 
			} else {
				final_decision = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + 0 + "vs" + 3 + "_0.net"));

				System.out.println("a vs d: " + final_decision);
				if (final_decision > 0) {
					return 0;
				} else {
					return 3;
				}
			}
			if (possible_chars[max_index] == 1) {
				final_decision = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + 1 + "vs" + 10 + "_0.net"));
				if (final_decision < 0) {
					return 10;
				}
			}
			return possible_chars[max_index];
		} else {
			final_decision = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + 0 + "vs" + possible_chars[second_max_index] + "_0.net"));
			System.out.println("a? " + final_decision);
			if (final_decision > 0) {
				return 0;
			} else {
				if (possible_chars[second_max_index] == 1) {
					final_decision = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + 1 + "vs" + 10 + "_0.net"));
					if (final_decision < 0) {
						return 10;
					}
				}
				return possible_chars[second_max_index];
			}
		}
		*/
	}
	
}
