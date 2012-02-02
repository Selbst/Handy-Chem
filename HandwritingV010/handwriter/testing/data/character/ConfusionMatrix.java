package testing.data.character;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import neuralnetwork.strokenetwork.StrokeNetwork;

import recognition.features.TrainingSet;
import recognition.filter.Filter;
import recognition.neuralnet_builders.FullyConnectedNNBuilder;
import recognition.strokes.StrokeList;
import testing.TestCharacterNetwork;
import trainingdata.character.ReadTrainingCharacters;
import util.CombinationGenerator;

public class ConfusionMatrix {
	static boolean debug = false;
	public static int[][] find_partition(int[][] c_matrix) {
		
		int[] indices;
		
		int k = (int) c_matrix.length/2;
		CombinationGenerator x = new CombinationGenerator (c_matrix.length, k);
		
		int[] set_A = new int[k];
		int[] set_B = new int[c_matrix.length-k];
		
		int max = -1;
		int[] max_set_A = new int[k];
		int[] max_set_B = new int[c_matrix.length-k];
		
		
		while (x.hasMore ()) {
		  indices = x.getNext ();
		  for (int i = 0; i < indices.length; i++) {
		    set_A[i] = indices[i];
		  }
		  
		  set_B = find_complement(set_A, c_matrix.length);
		  int confusion = find_confusion(c_matrix, set_A, set_B);

		  if (confusion > max) {
			  max = confusion;
			  max_set_A = set_A.clone();
			  max_set_B = set_B.clone();
		  }
		  
		}
		System.out.println(max);
		
		
		int[][] return_set = new int[2][];
		return_set[0] = max_set_A;
		return_set[1] = max_set_B;
		
		return return_set;
	}
	
	public static int[] find_complement(int[] set_A, int total) {
		int[] set_B = new int[total - set_A.length];
		
		boolean[] in_A = new boolean[total];
		for (int i=0; i < set_A.length; i++) {
			in_A[set_A[i]] = true;
			if (debug) {System.out.print(set_A[i] + ", ");}
		}
		
		int counter =0;

		if (debug) {System.out.println();}
		for (int i=0; i < in_A.length; i++) {
			if (in_A[i] == false) {
				set_B[counter] = i;
				counter++;
				if (debug) {
					System.out.print(i +", ");
				}
			}
			
		}
		
		if (debug) {
			System.out.println();
			for (int i=0; i < set_B.length; i++) {
				System.out.print(set_B[i] + ", ");
			}
		}
		return set_B;
	}
	
	public static int find_confusion(int[][] c_matrix, int[] set_A, int[] set_B) {
		int confusion = 0;
		
		for (int i=0; i < set_A.length; i++) {
			for (int j=0; j < set_A.length; j++) {
				confusion += c_matrix[set_A[i]][set_A[j]];
			}
		}
		//System.out.println(set_A.length + ", " + set_B.length);
		for (int i=0; i < set_B.length; i++) {
			for (int j=0; j < set_B.length; j++) {
				confusion += c_matrix[set_B[i]][set_B[j]];
			}
		}
		//System.out.println(confusion);
		return confusion;
		

	}
	
	
	public static void main(String[] args) throws IOException {

		
		/*
		int[][] c_matrix = new int[25][25];
		int[] false_negatives = new int[25];
		
		LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/all_6.ts");
		Iterator<StrokeList> it = chars.iterator();

		boolean[] toggleFeatures = new boolean[4]; 
		toggleFeatures[0] = true;
		toggleFeatures[1] = false;
		toggleFeatures[2] = false;
		toggleFeatures[3] = true;
		
		TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
		StrokeNetwork classifier;
		
		double threshold = 0.3;
		
		for (int i=0; i < 25; i++) {
			classifier = FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/grid/2/0_" + i + ".1.0.0.1.__10.net");
			
			for (int j=0; j < trainer.inputs.length; j++) {
				double output = FullyConnectedNNBuilder.getOutput(trainer.inputs[j], classifier);
				
				if (output > threshold) {
					if (trainer.classifications[j] != i) {
						c_matrix[i][trainer.classifications[j]]++;
					}
				} else {
					if (trainer.classifications[j] == i) {
						false_negatives[i]++;
					}
				}
			}
		}
		
		
		for (int i=0; i < 25; i++) {
			for (int j=0; j < 25; j++) {
				System.out.print(c_matrix[i][j] +", ");
			}
			System.out.println();
		}
		
		int[][] partition = find_partition(c_matrix);
		
		
		for (int i=0; i < partition.length; i++) {
			System.out.println();
			System.out.println("Group " + i + ": ");

			for (int j=0; j < partition[i].length; j++) {
				System.out.print(partition[i][j] + ", ");
				
			}
			
		}
		
		System.out.println(find_confusion(c_matrix, partition[0], partition[1]));
		debug = true;
		find_complement(partition[0]);
		
		
		
		StrokeNetwork[] classifiers = new StrokeNetwork[25];
		for (int i=0; i < 25; i++) {
			classifiers[i] = FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/grid/2/0_" + i + ".1.0.0.1.__10.net");
		}
		
		double[] outputs = new double[25];
		
		
		
		int incorrect =0;
		int total =0;
		for (int j=0; j < trainer.inputs.length; j++) {
			for (int i=0; i < 25; i++) {
				outputs[i] = FullyConnectedNNBuilder.getOutput(trainer.inputs[j], classifiers[i]);
			}
			
			int max_classifier = TestCharacterNetwork.findMax(outputs);
			//boolean classified_set_A = in_set(partition, max_classifier);
			boolean classified_set_A = classify(outputs, partition);
			boolean actual = in_set(partition, trainer.classifications[j]);
			
			if (classified_set_A == actual) {
				
			} else {
				incorrect++;
			}
			total++;
			
			
		}
		
		System.out.println("total incorrect: " + incorrect +" out of " + total);
		
		*/
		/*
		incorrect=0;
		StrokeNetwork partition_classifier = FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/grid/left0/0_all.1.0.0.1.__10.net");
		for (int j=0; j < trainer.inputs.length; j++) {
			double output = FullyConnectedNNBuilder.getOutput(trainer.inputs[j], partition_classifier);
			System.out.println("classifying: " + trainer.classifications[j] + ", " + output);
			if (in_set(partition, trainer.classifications[j])) {
				if (output > 0.3) {
				} else {
					incorrect++;
				}
				
			} else {
				if (output > 0.3) {
					incorrect++;
				} else {
					
				}
			}
			
		}

		System.out.println("total incorrect: " + incorrect +" out of " + total);

		*/
		Filter f = new Filter();

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
		for (int i=0; i < paths.length; i++) {
			
			possible_chars_per_path[i] = f.filter(paths[i]);
			if (i < 1) { // 0 to 1

		
		
				int[][] c_matrix = new int[possible_chars_per_path[i].length][possible_chars_per_path[i].length];
				int[] false_negatives = new int[possible_chars_per_path[i].length];
				
				LinkedList<StrokeList> chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/0.0.0.3.0.ts");
				Iterator<StrokeList> it = chars.iterator();
		
				boolean[] toggleFeatures = new boolean[4]; 
				toggleFeatures[0] = true;
				toggleFeatures[1] = false;
				toggleFeatures[2] = false;
				toggleFeatures[3] = false;
				
				TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
				StrokeNetwork classifier;
				
				double threshold = 0.0;
				
				for (int z=0; z < possible_chars_per_path[i].length; z++) {
					System.out.println("z ==" + z + ": " + "trainingcharacters/classifiers/filtered/" + possible_chars_per_path[i][z] + "_0_0.0.0.3.0.1.0.0.0.__10.net");
					classifier = FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + possible_chars_per_path[i][z] + "_0_0.0.0.3.0.1.0.0.0.__10.net");
					
					for (int j=0; j < trainer.inputs.length; j++) {
						double output = FullyConnectedNNBuilder.getOutput(trainer.inputs[j], classifier);
						
						if (output > threshold) {
							if (get_index(possible_chars_per_path[i], trainer.classifications[j]) != z) {
								c_matrix[z][get_index(possible_chars_per_path[i], trainer.classifications[j])]++;
							} else {
							}
						} else {
							if (get_index(possible_chars_per_path[i], trainer.classifications[j]) == z) {
								false_negatives[z]++;
							}
						}
					}
				}
				
				
				for (int z=0; z < possible_chars_per_path[i].length; z++) {
					for (int j=0; j < possible_chars_per_path[i].length; j++) {
						System.out.print(c_matrix[z][j] +", ");
					}
					System.out.println();
				}
				
				//System.out.println(c_matrix.length +", "  + c_matrix[0].length);
				int[][] partition = find_partition(c_matrix);
				
				
				for (int z=0; z < partition.length; z++) {
					System.out.println();
					System.out.println("Group " + z + ": ");
		
					for (int j=0; j < partition[z].length; j++) {
						System.out.print(partition[z][j] + ", ");
						
					}
					System.out.println();
					
				}
				
				System.out.println(find_confusion(c_matrix, partition[0], partition[1]));
				debug = true;
				find_complement(partition[0], c_matrix.length);
				
				
				
				StrokeNetwork[] classifiers = new StrokeNetwork[possible_chars_per_path[i].length];
				for (int z=0; z < possible_chars_per_path[i].length; z++) {
					classifiers[z] = FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + possible_chars_per_path[i][z] + "_0_0.0.0.3.0.1.0.0.0.__10.net");
				}
				
				double[] outputs = new double[possible_chars_per_path[i].length];
				
				
				chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/1/filtered/0.0.0.3.0.ts");

				
				trainer = new TrainingSet(chars, toggleFeatures);
				int incorrect =0;
				int total =0;
				for (int j=0; j < trainer.inputs.length; j++) {
					for (int z=0; z < possible_chars_per_path[i].length; z++) {
						outputs[z] = FullyConnectedNNBuilder.getOutput(trainer.inputs[j], classifiers[z]);
					}
					
					int max_classifier = TestCharacterNetwork.findMax(outputs);
					//boolean classified_set_A = in_set(partition, max_classifier);
					boolean classified_set_A = classify(outputs, partition);
					boolean actual = in_set(partition, get_index(possible_chars_per_path[i], trainer.classifications[j]));
					
					if (classified_set_A == actual) {
						
					} else {
						incorrect++;
					}
					total++;
					
					
				}
				
				System.out.println("total incorrect: " + incorrect +" out of " + total);
			}
		}
	}
			
	
	/*
	 *  classifies character as group which receives the most votes.
	 * 
	 */
	public static boolean classify(double[] outputs, int[][] partition) {
		double threshold = 0.0;
		int A_counter = 0;
		int B_counter = 0;
		
		for (int i=0; i < outputs.length; i++) {
			if (outputs[i] > threshold) {
				if (in_set(partition, i)) {
					A_counter++;
				} else {
					B_counter++;
				}
			}
		}
		if (A_counter > B_counter) {
			return true;
			
		} else {
			return false;
		}
		
		
	}
	
	public static boolean in_set(int[][] partition, int k) {
		for (int i=0; i < partition[0].length; i++) {
			if (partition[0][i] == k) {
				return true;
			}
		}
		return false;
		
	}
	
	public static int get_index(int[] arr, int x) {
		for (int i=0; i < arr.length; i++) {
			if (arr[i] == x) {
				return i;
			}
		}
		return -1;
	}
	
	
	
	
}
