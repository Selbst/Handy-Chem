package testing.data.character;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import neuralnetwork.strokenetwork.StrokeNetwork;

import recognition.features.TrainingSet;
import recognition.filter.Filter_Lowercase;
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
		Filter_Lowercase f = new Filter_Lowercase();

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
		/*
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
						classifiers[z] = FullyConnectedNNBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + possible_chars_per_path[z] + "_0_0.0.0.3.0.1.0.0.0.__10.net");
					}
				

				
				double[][] outputs = new double[2][possible_chars_per_path[i].length];
				
				
				chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/filtered/0.0.0.3.0.ts");

				
				trainer = new TrainingSet(chars, toggleFeatures);
				int incorrect =0;
				int total =0;
				for (int j=0; j < trainer.inputs.length; j++) {
					for (int q=0; q < 2; q++) {
						
					
						for (int z=0; z < possible_chars_per_path[i].length; z++) {
							outputs[q][z] = FullyConnectedNNBuilder.getOutput(trainer.inputs[j], classifiers[q][z]);
						}
					}
					
					
					
					int max_classifier = TestCharacterNetwork.findMax(outputs[0]);
					//boolean classified_set_A = in_set(partition, max_classifier);
					boolean classified_set_A = classify(outputs[0], partition);
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
		*/
		
		
		
		
		int[][][] partitions = new int[paths.length][][];
		int incorrect_sum=0;
		int total_sum =0;
		for (int i=41; i < paths.length; i++) {
			
			possible_chars_per_path[i] = f.filter(paths[i]);
			
			
			System.out.println("trainingcharacters/filtered/" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4] + ".tr");
			System.out.println("testingcharacters/filtered/" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4] + ".ts");
			// 0 to 1
				StrokeNetwork[] classifier = new StrokeNetwork[possible_chars_per_path[i].length];
				for (int j=0; j < possible_chars_per_path[i].length; j++) {
											
						String nn_file = "trainingcharacters/classifiers/filtered/" + possible_chars_per_path[i][j] + "__2_" + paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4]   + ".1.0.0.0.__" + "10" + ".net";
						classifier[j] = FullyConnectedNNBuilder.fileToNetwork(nn_file);

				}
				
				
				LinkedList<StrokeList> testing_chars = new LinkedList<StrokeList>();

				
				
				for (int j=0; j < 7; j++) {
					for (int index=0; index < possible_chars_per_path[i].length; index++) {
						testing_chars.addAll(ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/" + j + "_" + possible_chars_per_path[i][index] + ".ts"));
					}
				}
				boolean[] toggleFeatures = new boolean[4]; 
				toggleFeatures[0] = true;
				toggleFeatures[1] = false;
				toggleFeatures[2] = false;
				toggleFeatures[3] = false;				
				TrainingSet trainer = new TrainingSet(testing_chars, toggleFeatures);
				testing_chars.clear();
				int incorrect =0;
				int total =0;
				
				
				int[][] c_matrix = new int[possible_chars_per_path[i].length][possible_chars_per_path[i].length];

				double threshold = 0.2;
				for (int z=0; z < possible_chars_per_path[i].length; z++) {

					for (int j=0; j < trainer.inputs.length; j++) {
						double output = FullyConnectedNNBuilder.getOutput(trainer.inputs[j], classifier[z]);
						
						if (output > threshold) {
							
							if (get_index(possible_chars_per_path[i], trainer.classifications[j]) != z) {
								c_matrix[z][get_index(possible_chars_per_path[i], trainer.classifications[j])]++;
							} else {
							}
						} 
					}
				}
				
				if (i ==0) {
					for (int q =0; q < possible_chars_per_path[i].length; q++) {
						System.out.println(possible_chars_per_path[i][q]);
					}
				}
				int[][] partition = find_partition(c_matrix);
				partitions[i] = partition;
				for (int q=0; q < 2; q++) {
					for (int k=0; k < partitions[i][q].length; k++) {
						//System.out.println(partition[q][k]);
						partitions[i][q][k] = possible_chars_per_path[i][partition[q][k]];
						//System.out.println(partitions[i][q][k]);

					}
				}
				
				

				
				for (int z=0; z < partition.length; z++) {
					System.out.println();
					System.out.println("Group " + z + ": ");
		
					for (int j=0; j < partition[z].length; j++) {
						System.out.print(partition[z][j] + ", ");
						
					}
					System.out.println();
					
				}
				
				incorrect =0;
				total =0;
				for (int j=0; j < trainer.inputs.length; j++) {
						
						double[] outputs = new double[possible_chars_per_path[i].length];
						for (int z=0; z < possible_chars_per_path[i].length; z++) {
							outputs[z] = FullyConnectedNNBuilder.getOutput(trainer.inputs[j], classifier[z]);
						}
						boolean classified_set_A = classify(outputs, partition);
						boolean actual = in_set(partition, get_index(possible_chars_per_path[i], trainer.classifications[j]));
						
						if (classified_set_A == actual) {
							
						} else {
							incorrect++;
						}
						total++;
				}
				System.out.println(incorrect + ", " + total);
				incorrect_sum += incorrect;
				total_sum += total;
					
			
		}
		int[][] paths2 = new int[paths.length*2][];
		for (int q=0; q < paths.length; q++) {
			for (int z=0; z < 2; z++)	 {

				int[] path = new int[6];


				path[0] = paths[q][0];
				path[1] = paths[q][1];
				path[2] = paths[q][2];
				path[3] = paths[q][3];
				path[4] = paths[q][4];
				path[5] = z;
				paths2[q*2+z] = path;	
			}
		}
		int[][] possible_chars_per_path2 = new int[possible_chars_per_path.length*2][];
		for (int i=0; i < possible_chars_per_path.length; i++) {
				possible_chars_per_path2[i*2] = partitions[i][0];
				possible_chars_per_path2[i*2+1] = partitions[i][1];
			
		}
		//FileWriter fstream = new FileWriter("trainingcharacters/filtered/level_2/characters_index", true);
		//BufferedWriter out = new BufferedWriter(fstream);
		

		System.out.println(incorrect_sum + ", " + total_sum);
		for (int i=0; i < possible_chars_per_path2.length; i++) {
			System.out.println("length" + possible_chars_per_path2[i].length);
			int[] filtered_options = new int[possible_chars_per_path2[i].length];
			//out.write("branch\n");
			for (int j=0; j < possible_chars_per_path2[i].length; j++) {
				System.out.print(", " + possible_chars_per_path2[i][j]);
				filtered_options[j] = possible_chars_per_path2[i][j];
				//out.write("" + possible_chars_per_path2[i][j] + "\n");
				
			}	
		}
		//out.close();
		
	}
	//public static int[][] get_possible_chars
	
	
	/*
	 *  classifies character as group which receives the most votes.
	 * 
	 */
	public static boolean classify(double[] outputs, int[][] partition) {
		double threshold = 0.0;
		int A_counter = 0;
		int B_counter = 0;
		
		/*
		for (int i=0; i < outputs.length; i++) {
			if (in_set(partition, i)) {
				A_counter += outputs[i];
			} else {
				B_counter += outputs[i];
			}
			
		}
		*/
		
		
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
	public static boolean classify(double[][] outputs, int[][] partition) {
		double threshold = 0.0;
		int A_counter = 0;
		int B_counter = 0;
		
		for (int z=0; z < outputs.length; z++) {
			for (int i=0; i < outputs[z].length; i++) {
				if (outputs[z][i] > threshold) {
					if (in_set(partition, i)) {
						A_counter++;
					} else {
						B_counter++;
					}
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

	
	public static void get_partition(int level) {
		
		
		
		if (level == 1) {
			
		} else if (level == 2) {
			
		}
	}
	
	
	
	
	
}
