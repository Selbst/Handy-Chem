package testing.data.character;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;
import recognition.Compare_Classifiers;
import recognition.Compare_Classifiers2;
import recognition.features.TrainingSet;
import recognition.filter.Filter_Lowercase;
import recognition.filter.Filter_Uppercase;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;
import structural_synthesis.Synthesize_Molecule;
import trainingdata.character.ReadTrainingCharacters;
import dictionary.Lookup_Char;

public class Testing_Tree {
	static boolean debug_classification = false;
	
	private static int get_label(StrokeList character) throws NumberFormatException, IOException {
		Filter_Lowercase f_lowercase = new Filter_Lowercase();
		Filter_Uppercase f_uppercase = new Filter_Uppercase();

		f_lowercase.set_classifier_branches();
		f_uppercase.set_classifier_branches();
		
		
			int[] letter_1 = new int[1];
				if (Synthesize_Molecule.is_bond(character)) {
					if (character.getStrokes().size() == 1) {
							letter_1[0] = 8;
					} else {
							System.out.println("lowercase letter (1): "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
							
							/*
							 * 
							 * 
							 */
							int[][] filtered_result = f_lowercase.filter(character);
							String path = "";
							for (int i=0; i < filtered_result[0].length; i++) {
								path += filtered_result[0][i] + "." ;
							}
							if (debug_classification)
								System.out.println("path: " + path);
							int branch_index = filtered_result[0][1]*2*4*3 + filtered_result[0][2]*4*3 + (filtered_result[0][3]-3)*3 + filtered_result[0][4];
							//System.out.println("branch index" + branch_index);
							int[] left_chars = f_lowercase.first_branch_chars[branch_index*2];
							int[] right_chars = f_lowercase.first_branch_chars[branch_index*2 + 1];
							
							StrokeNetwork[] classifiers = new StrokeNetwork[filtered_result[1].length];
							for (int i=0; i < filtered_result[1].length; i++) {
								//System.out.println("char : " + filtered_result[1][i]);
								classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + filtered_result[1][i] + "__" + "2" + "_" + path + "1.0.0.0.__10.net");
							}
							for (int i=0; i < left_chars.length; i++) {
								if (debug_classification)
									System.out.println("left char (0): " + left_chars[i]);
							}
							for (int i=0; i < right_chars.length; i++) {
								if (debug_classification)
									System.out.println("right char (0): " + right_chars[i]);
							}
							boolean branch_right = Compare_Classifiers.compare_branches(character, left_chars, right_chars, classifiers, filtered_result[1]);
							int classification =-1;
							if (branch_right) {
								path += "1.";
								classifiers = new StrokeNetwork[right_chars.length];
								if (debug_classification)
									System.out.println("went right");
				
								for (int i=0; i < right_chars.length; i++) {
									classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + right_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
								}
								classification = Compare_Classifiers.get_classification(character, classifiers, right_chars);
								if (debug_classification)
									System.out.println("classification: " + classification);	
							} else {
								path += "0.";
								if (debug_classification)
									System.out.println("went left");
								classifiers = new StrokeNetwork[left_chars.length];
				
				
								for (int i=0; i < left_chars.length; i++) {
									classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + left_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
								}
								if (debug_classification)
									System.out.println("classification: "  + classification);
								classification = Compare_Classifiers.get_classification(character, classifiers, left_chars);
				
							}
							letter_1[0] = classification;
					}
				} else {
					System.out.println("lowercase letter(2): "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
		
					/*
					 * 
					 * 
					 */
					int[][] filtered_result = f_lowercase.filter(character);
					String path = "";
					for (int i=0; i < filtered_result[0].length; i++) {
						path += filtered_result[0][i] + "." ;
					}
					if (debug_classification)
						System.out.println("path: " + path);
					int branch_index = filtered_result[0][1]*2*4*3 + filtered_result[0][2]*4*3 + (filtered_result[0][3]-3)*3 + filtered_result[0][4];
					//System.out.println("branch index" + branch_index);
					int[] left_chars = f_lowercase.first_branch_chars[branch_index*2];
					int[] right_chars = f_lowercase.first_branch_chars[branch_index*2 + 1];
					
					StrokeNetwork[] classifiers = new StrokeNetwork[filtered_result[1].length];
					for (int i=0; i < filtered_result[1].length; i++) {
						//System.out.println("char : " + filtered_result[1][i]);
						classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + filtered_result[1][i] + "__" + "2" + "_" + path + "1.0.0.0.__10.net");
					}
					for (int i=0; i < left_chars.length; i++) {
						if (debug_classification)
							System.out.println("left char (0): " + left_chars[i]);
					}
					for (int i=0; i < right_chars.length; i++) {
						if (debug_classification)
							System.out.println("right char (0): " + right_chars[i]);
					}
					boolean branch_right = Compare_Classifiers.compare_branches(character, left_chars, right_chars, classifiers, filtered_result[1]);
					int classification =-1;
					if (branch_right) {
						path += "1.";
						classifiers = new StrokeNetwork[right_chars.length];
						if (debug_classification)
							System.out.println("went right");
		
						for (int i=0; i < right_chars.length; i++) {
							classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + right_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
						}
						classification = Compare_Classifiers.get_classification(character, classifiers, right_chars);
						if (debug_classification)
							System.out.println("classification: " + classification);
		
						
					} else {
						path += "0.";
						if (debug_classification)
							System.out.println("went left");
						classifiers = new StrokeNetwork[left_chars.length];
		
		
						for (int i=0; i < left_chars.length; i++) {
							classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + left_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
						}
						if (debug_classification)
							System.out.println("classification: "  + classification);
						classification = Compare_Classifiers.get_classification(character, classifiers, left_chars);
					}
					
					letter_1[0] = classification;
				}
		return letter_1[0];
	}
	private static int get_label2(StrokeList character) throws NumberFormatException, IOException {
		Filter_Lowercase f_lowercase = new Filter_Lowercase();
		Filter_Uppercase f_uppercase = new Filter_Uppercase();

		f_lowercase.set_classifier_branches();
		f_uppercase.set_classifier_branches();
		
		
			int[] letter_1 = new int[1];
				if (Synthesize_Molecule.is_bond(character)) {
					if (character.getStrokes().size() == 1) {
							letter_1[0] = 8;
					} else {
							System.out.println("lowercase letter (1): "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
							
							/*
							 * 
							 * 
							 */
							int[][] filtered_result = f_lowercase.filter(character);
							String path = "";
							for (int i=0; i < filtered_result[0].length; i++) {
								path += filtered_result[0][i] + "." ;
							}
							if (debug_classification)
								System.out.println("path: " + path);
							int branch_index = filtered_result[0][1]*2*4*3 + filtered_result[0][2]*4*3 + (filtered_result[0][3]-3)*3 + filtered_result[0][4];
							//System.out.println("branch index" + branch_index);
							int[] left_chars = f_lowercase.first_branch_chars[branch_index*2];
							int[] right_chars = f_lowercase.first_branch_chars[branch_index*2 + 1];
							
							StrokeNetwork[] classifiers = new StrokeNetwork[filtered_result[1].length];
							for (int i=0; i < filtered_result[1].length; i++) {
								//System.out.println("char : " + filtered_result[1][i]);
								classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + filtered_result[1][i] + "__" + "2" + "_" + path + "1.0.0.0.__10.net");
							}
							for (int i=0; i < left_chars.length; i++) {
								if (debug_classification)
									System.out.println("left char (0): " + left_chars[i]);
							}
							for (int i=0; i < right_chars.length; i++) {
								if (debug_classification)
									System.out.println("right char (0): " + right_chars[i]);
							}
							boolean branch_right = Compare_Classifiers2.compare_branches(character, left_chars, right_chars, classifiers, filtered_result[1]);
							int classification =-1;
							if (branch_right) {
								path += "1.";
								classifiers = new StrokeNetwork[right_chars.length];
								if (debug_classification)
									System.out.println("went right");
				
								for (int i=0; i < right_chars.length; i++) {
									classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + right_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
								}
								classification = Compare_Classifiers2.get_classification(character, classifiers, right_chars);
								if (debug_classification)
									System.out.println("classification: " + classification);	
							} else {
								path += "0.";
								if (debug_classification)
									System.out.println("went left");
								classifiers = new StrokeNetwork[left_chars.length];
				
				
								for (int i=0; i < left_chars.length; i++) {
									classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + left_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
								}
								if (debug_classification)
									System.out.println("classification: "  + classification);
								classification = Compare_Classifiers2.get_classification(character, classifiers, left_chars);
				
							}
							letter_1[0] = classification;
					}
				} else {
					System.out.println("lowercase letter(2): "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
		
					/*
					 * 
					 * 
					 */
					int[][] filtered_result = f_lowercase.filter(character);
					String path = "";
					for (int i=0; i < filtered_result[0].length; i++) {
						path += filtered_result[0][i] + "." ;
					}
					if (debug_classification)
						System.out.println("path: " + path);
					int branch_index = filtered_result[0][1]*2*4*3 + filtered_result[0][2]*4*3 + (filtered_result[0][3]-3)*3 + filtered_result[0][4];
					//System.out.println("branch index" + branch_index);
					int[] left_chars = f_lowercase.first_branch_chars[branch_index*2];
					int[] right_chars = f_lowercase.first_branch_chars[branch_index*2 + 1];
					
					StrokeNetwork[] classifiers = new StrokeNetwork[filtered_result[1].length];
					for (int i=0; i < filtered_result[1].length; i++) {
						//System.out.println("char : " + filtered_result[1][i]);
						classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + filtered_result[1][i] + "__" + "2" + "_" + path + "1.0.0.0.__10.net");
					}
					for (int i=0; i < left_chars.length; i++) {
						if (debug_classification)
							System.out.println("left char (0): " + left_chars[i]);
					}
					for (int i=0; i < right_chars.length; i++) {
						if (debug_classification)
							System.out.println("right char (0): " + right_chars[i]);
					}
					boolean branch_right = Compare_Classifiers2.compare_branches(character, left_chars, right_chars, classifiers, filtered_result[1]);
					int classification =-1;
					if (branch_right) {
						path += "1.";
						classifiers = new StrokeNetwork[right_chars.length];
						if (debug_classification)
							System.out.println("went right");
		
						for (int i=0; i < right_chars.length; i++) {
							classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + right_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
						}
						classification = Compare_Classifiers2.get_classification(character, classifiers, right_chars);
						if (debug_classification)
							System.out.println("classification: " + classification);
		
						
					} else {
						path += "0.";
						if (debug_classification)
							System.out.println("went left");
						classifiers = new StrokeNetwork[left_chars.length];
		
		
						for (int i=0; i < left_chars.length; i++) {
							classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + left_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
						}
						if (debug_classification)
							System.out.println("classification: "  + classification);
						classification = Compare_Classifiers2.get_classification(character, classifiers, left_chars);
					}
					
					letter_1[0] = classification;
				}
		return letter_1[0];
	}
	private static int get_label3(StrokeList character) throws NumberFormatException, IOException {
		
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
		
		int[] win_count = new int[25];
		for (int i=0; i < 24; i++) {
			for (int j=i+1; j < 25; j++) {
				if (!(i == 1 && j == 4)) {
					double output = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + i + "vs" + j + "_0.net"));
					if (output > 0) {
						win_count[i]++;
					} else {
						win_count[j]++;
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
		
		
		return maxdex;
	}
	private static int get_label4(StrokeList character) throws NumberFormatException, IOException {
		Filter_Lowercase f_lowercase = new Filter_Lowercase();
		Filter_Uppercase f_uppercase = new Filter_Uppercase();

		f_lowercase.set_classifier_branches();
		f_uppercase.set_classifier_branches();
		int[] left_chars = f_lowercase.first_branch_chars[0];
		int[] right_chars = f_lowercase.first_branch_chars[1];
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
		

		double[] left_chars_output = new double[left_chars.length];
		for (int i=0; i < left_chars.length; i++) {
			left_chars_output[i] = StrokeNetworkBuilder.getOutput(inputs_3x3, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + left_chars[i] + "__" + "2" + "_" + "0.0.0.3.0.1.0.0.0.__10.net"));
		}
		double[] right_chars_output = new double[right_chars.length];
		for (int i=0; i < right_chars.length; i++) {
			right_chars_output[i] = StrokeNetworkBuilder.getOutput(inputs_3x3, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + right_chars[i] + "__" + "2" + "_" + "0.0.0.3.0.1.0.0.0.__10.net"));
		}
		
		boolean right_max = false;
		int max_index = 0;
		double max_val = left_chars_output[0];
		for (int i=0; i < left_chars_output.length; i++) {
			if (left_chars_output[i] > max_val) {
				max_val = left_chars_output[i];
				max_index = i;
			}
		}
		for (int i=0; i < right_chars_output.length; i++) {
			if (right_chars_output[i] > max_val) {
				max_val = right_chars_output[i];
				max_index = i;
				right_max = true;
			}
		}
		if (right_max) {
			System.out.println(right_chars[max_index]);
			return right_chars[max_index];
		} else {
			System.out.println(left_chars[max_index]);
			return left_chars[max_index];
		}		
	}
	public static void main(String[] args) throws IOException {
		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		
		int[][] confusion_matrix = new int[25][25];
		
		Filter_Lowercase f_lowercase = new Filter_Lowercase();
		Filter_Uppercase f_uppercase = new Filter_Uppercase();

		f_lowercase.set_classifier_branches();
		f_uppercase.set_classifier_branches();
		int[] left_chars = f_lowercase.first_branch_chars[0];
		
		left_chars = new int[25];
		for (int i=0; i < left_chars.length; i++) {
			left_chars[i] = i;
		}
		
		//int[] right_chars = f_lowercase.first_branch_chars[1];
		for (int i=0; i < left_chars.length; i++) {
			System.out.println(left_chars[i]);
		}
		//for (int i=0; i < right_chars.length; i++) {
		//	System.out.println(right_chars[i]);
		//}		
		
		/*
		for (int i=0; i < 25; i++) {
			for (int j=0; j < 7; j++) {
				chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/" + j + "_" + i + ".ts");
				for (StrokeList Char : chars) {
					int classification = get_label5(Char);
					confusion_matrix[i][classification]++;
				}
				chars.clear();
			}
		}
		*/
		
		for (int i=0; i < left_chars.length; i++) {
			for (int j=0; j < 7; j++) {
				chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/" + j + "_" + left_chars[i] + ".ts");
				for (StrokeList Char : chars) {
					int classification = get_label(Char);
					confusion_matrix[left_chars[i]][classification]++;
				}
				chars.clear();
			}
		}
		
		for (int i=0; i < left_chars.length; i++) {
			for (int j=0; j < 2; j++) {
				chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/1/" + j + "_" + left_chars[i] + ".ts");
				for (StrokeList Char : chars) {
					int classification = get_label(Char);
					confusion_matrix[left_chars[i]][classification]++;
				}
				chars.clear();
			}
		}
		
		/*
		for (int i=0; i < right_chars.length; i++) {
			for (int j=0; j < 7; j++) {
				chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/" + j + "_" + right_chars[i] + ".ts");
				for (StrokeList Char : chars) {
					int classification = get_label4(Char);
					confusion_matrix[right_chars[i]][classification]++;
				}
				chars.clear();
			}
		}
		
		for (int i=0; i < right_chars.length; i++) {
			for (int j=0; j < 2; j++) {
				chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/1/" + j + "_" + right_chars[i] + ".ts");
				for (StrokeList Char : chars) {
					int classification = get_label4(Char);
					confusion_matrix[right_chars[i]][classification]++;
				}
				chars.clear();
			}
		}
		*/
		
		for (int i=0; i < confusion_matrix.length; i++) {
			for (int j=0; j < confusion_matrix[0].length; j++) {
				System.out.print(j + "-" + confusion_matrix[j][i] + ", ");
			}
			System.out.println();
		}
		
		
		int[][][] maxes = new int[25][6][2];
		for (int i =0; i < maxes.length; i++) {
			for (int j=0; j < maxes[i].length; j++) {
				maxes[i][j][0] = Integer.MIN_VALUE;
				maxes[i][j][1] = Integer.MIN_VALUE;
			}
		}
		for (int i=0; i < confusion_matrix.length; i++) {
			for (int j=0; j < confusion_matrix[0].length; j++) {
				if (i != j) {
					maxes[i] = update_maxes(maxes[i], confusion_matrix[j][i], j);
				}
			}
		}
		
		
		for (int i=0; i < maxes.length; i++) {
			System.out.print(i + ": ");
			for (int j=0; j < maxes[i].length; j++) {
				System.out.print(maxes[i][j][1] + "-" + maxes[i][j][0] + ", ");
			}
			System.out.println();
		}
		
		
		for (int i=0; i < maxes.length; i++) {
			for (int j=0; j < maxes[i].length; j++) {
				System.out.println("max_confusions[" + i + "][" + j + "] = " + maxes[i][j][1] + ";");
			}
		}
		
		int correct = 0;
		int wrong = 0;
		for (int i=0; i < confusion_matrix.length; i++) {
			for (int j=0; j < confusion_matrix.length; j++) {
				if (i == j) {
					correct += confusion_matrix[i][j];
				} else {
					wrong += confusion_matrix[i][j];
				}
			}
		}
		System.out.println("correct: " + correct);
		System.out.println("wrong: " + wrong);
		
		System.out.println((double)(correct / (correct + wrong)));
		  
		
		/*
		for (int i=0; i < confusion_matrix.length; i++) {
			for (int j=0; j < confusion_matrix[0].length; j++) {
				System.out.println("confusion_matrix[" + i + "][" + j + "] = " + confusion_matrix[i][j] + ";");
			}
		}
		*/
	}
	
	private static int get_label5(StrokeList character) throws NumberFormatException, IOException {
		Filter_Lowercase f_lowercase = new Filter_Lowercase();
		Filter_Uppercase f_uppercase = new Filter_Uppercase();

		f_lowercase.set_classifier_branches();
		f_uppercase.set_classifier_branches();
		
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
		
			int[] letter_1 = new int[1];
				if (Synthesize_Molecule.is_bond(character)) {
					if (character.getStrokes().size() == 1) {
							letter_1[0] = 8;
					} else {
							System.out.println("lowercase letter (1): "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
							
							/*
							 * 
							 * 
							 */
							int[][] filtered_result = f_lowercase.filter(character);
							String path = "";
							for (int i=0; i < filtered_result[0].length; i++) {
								path += filtered_result[0][i] + "." ;
							}
							if (debug_classification)
								System.out.println("path: " + path);
							int branch_index = filtered_result[0][1]*2*4*3 + filtered_result[0][2]*4*3 + (filtered_result[0][3]-3)*3 + filtered_result[0][4];
							//System.out.println("branch index" + branch_index);
							int[] left_chars = f_lowercase.first_branch_chars[branch_index*2];
							int[] right_chars = f_lowercase.first_branch_chars[branch_index*2 + 1];
							
							for (int i=0; i < filtered_result[1].length; i++) {
								System.out.print(filtered_result[1][i] + ", ");
							}
							System.out.println();
							

							
							int[] win_count = new int[25];

							for (int i=0; i < filtered_result[1].length-1; i++) {
								for (int j=i+1; j < filtered_result[1].length; j++) {
									if (!(i == 1 && j == 4)) {
										if (filtered_result[1][i] < filtered_result[1][j]) {
											double output = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + filtered_result[1][i] + "vs" + filtered_result[1][j] + "_0.net"));
											if (output > 0) {
												win_count[filtered_result[1][i]]++;
											} else {
												win_count[filtered_result[1][j]]++;
											}
										} else {
											double output = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + filtered_result[1][j] + "vs" + filtered_result[1][i] + "_0.net"));
											if (output > 0) {
												win_count[filtered_result[1][j]]++;
											} else {
												win_count[filtered_result[1][i]]++;
											}
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
							
							for (int i=0; i < win_count.length; i++) {
								System.out.println(i + " - " + win_count[i]);
							}
							System.out.println("winner: " +  maxdex);
							
							
							return maxdex;
							
					}
				} else {
					System.out.println("lowercase letter(2): "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
		
					/*
					 * 
					 * 
					 */
					int[][] filtered_result = f_lowercase.filter(character);
					int[] win_count = new int[25];
					for (int i=0; i < filtered_result[1].length; i++) {
						System.out.print(filtered_result[1][i] + ", ");
					}
					System.out.println();

					for (int i=0; i < filtered_result[1].length-1; i++) {
						for (int j=i+1; j < filtered_result[1].length; j++) {
							if (!(i == 1 && j == 4)) {
								if (filtered_result[1][i] < filtered_result[1][j]) {
									double output = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + filtered_result[1][i] + "vs" + filtered_result[1][j] + "_0.net"));
									if (output > 0) {
										win_count[i]++;
									} else {
										win_count[j]++;
									}
								} else {
									double output = StrokeNetworkBuilder.getOutput(inputs_2x2, StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/vs/" + filtered_result[1][j] + "vs" + filtered_result[1][i] + "_0.net"));
									if (output > 0) {
										win_count[j]++;
									} else {
										win_count[i]++;
									}
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
					
					
					return maxdex;
				}
		return letter_1[0];
	}	
	
	private static int[][] update_maxes(int[][] maxes, int update, int update_index) {
		int min_index = 0;
		for (int i=1; i < maxes.length; i++) {
			if (maxes[i][0] < maxes[min_index][0]) {
				min_index = i;
			}
		}
		if (maxes[min_index][0] < update) {
			maxes[min_index][0] = update;
			maxes[min_index][1] = update_index;
		}
		
		
		return maxes;
	}
}
