package recognition.filter;

import java.io.IOException;
import java.util.LinkedList;

import recognition.features.InputFeatureCombiner;
import recognition.strokes.StrokeList;
import trainingdata.character.ReadTrainingCharacters;

public class Filter {
	
	public int[] results_0_0_0_0_0 = new int[10]; {
		results_0_0_0_0_0[0] = 5;
		results_0_0_0_0_0[1] = 6;
		results_0_0_0_0_0[2] = 11;
		results_0_0_0_0_0[3] = 13;
		results_0_0_0_0_0[4] = 14;
		results_0_0_0_0_0[5] = 15;
		results_0_0_0_0_0[6] = 18;
		results_0_0_0_0_0[7] = 22;
		results_0_0_0_0_0[8] = 23;
		results_0_0_0_0_0[9] = 24;
	}
	public String string_0_0_0_0_0 = "0.0.0.0.0";
	
	public Filter() {
		results_0_0_0_0_0 = new int[10]; {
			results_0_0_0_0_0[0] = 5;
			results_0_0_0_0_0[1] = 6;
			results_0_0_0_0_0[2] = 11;
			results_0_0_0_0_0[3] = 13;
			results_0_0_0_0_0[4] = 14;
			results_0_0_0_0_0[5] = 15;
			results_0_0_0_0_0[6] = 18;
			results_0_0_0_0_0[7] = 22;
			results_0_0_0_0_0[8] = 23;
			results_0_0_0_0_0[9] = 24;
		}
		string_0_0_0_0_0 = "0.0.0.0.0";
	}
	
	public int[][] filter(StrokeList character) {
		int[] to_remove_up = new int[8];
			to_remove_up[0] = 4;
			to_remove_up[1] = 6;
			to_remove_up[2] = 11;
			to_remove_up[3] = 12;
			to_remove_up[4] = 14;
			to_remove_up[5] = 15;
			to_remove_up[6] = 16;
			to_remove_up[7] = 23;
			
		int[] to_remove_down = new int[7];
			to_remove_down[0] = 1;
			to_remove_down[1] = 3;
			to_remove_down[2] = 7;
			to_remove_down[3] = 9;
			to_remove_down[4] = 10;
			to_remove_down[5] = 19;
			to_remove_down[6] = 21;
			
		int[] to_remove_left = new int[1];
			to_remove_left[0]  = 0;
		int[] to_remove_right = new int[2];
			to_remove_right[0] = 2;
			to_remove_right[1] = 5;
			
		int[] to_remove_yint_not2 = new int[1];
			to_remove_yint_not2[0] = 2;
		
		int[] to_remove_yint_not3 = new int[2];
			to_remove_yint_not3[0] = 4;
			to_remove_yint_not3[1] = 17;
			
		int[] to_remove_yint_not1 = new int[5];
			to_remove_yint_not1[0] = 7;
			to_remove_yint_not1[1] = 12;
			to_remove_yint_not1[2] = 16;
			to_remove_yint_not1[3] = 19;
			to_remove_yint_not1[4] = 20;
		
		int[] to_remove_xint_not1 = new int[4];
			to_remove_xint_not1[0] = 2;
			to_remove_xint_not1[1] = 4;
			to_remove_xint_not1[2] = 9;
			to_remove_xint_not1[3] = 17;
			
		int[] to_remove_xint_not2 = new int[3];
			to_remove_xint_not2[0] = 13;
			to_remove_xint_not2[1] = 19;
			to_remove_xint_not2[2] = 20;
		
		LinkedList<Integer> possible_chars = new LinkedList<Integer>();
		for (int i=0; i < 25; i++) {
			if (i != 8) {
				possible_chars.add(i);
			}
		}
		
		
		boolean[] features = new boolean[6];
		features[4] = true; 
		features[5] = true;
		
		InputFeatureCombiner inputs = new InputFeatureCombiner(features, character);
		double[] input_vector = inputs.get_input_vector();
		for (int i=0; i < input_vector.length; i++) {
			System.out.println(i + ": " + input_vector[i]);
			
			
		}
		
		int[] path = new int[5];
		path[0] = 0;
		
		if (input_vector[0] > 0) { // up
			path[1] =1;
			possible_chars = remove_chars(to_remove_up, possible_chars);
		}
		if (input_vector[0] <= 0) { // down
			path[1] = 0;
			possible_chars = remove_chars(to_remove_down, possible_chars);
		}
		if (input_vector[1] > 0) { // right
			path[2] = 1;
			possible_chars = remove_chars(to_remove_right, possible_chars);
		}
		if (input_vector[1] <= 0) { // left
			path[2] = 0;
			possible_chars = remove_chars(to_remove_left, possible_chars);

		}
		if (input_vector[2] != 1 ) {
			possible_chars = remove_chars(to_remove_yint_not1, possible_chars);
			
		}
		if (input_vector[2] != 2) {
			possible_chars = remove_chars(to_remove_yint_not2, possible_chars);
		}
		if (input_vector[2] != 3) {
			possible_chars = remove_chars(to_remove_yint_not3, possible_chars);
		}
		
		if (input_vector[2] != 1) {
			if (input_vector[2] != 2) {
				if (input_vector[2] != 3) {
					path[3] = 6;
				} else {
					path[3] = 3;
				}
			} else {
				if (input_vector[2] != 3) {
					path[3] = 4;
				} //else {
					//path[3] = 0;
				//}
			}
		} else if (input_vector[2] != 2) {
			if (input_vector[2] != 3) {
				path[3] = 5;
			} //else {
				//path[3] = 1;
			//}
		}
		
		if (input_vector[3] != 1) {
			possible_chars = remove_chars(to_remove_xint_not1, possible_chars);
		} 
		if (input_vector[3] != 2) {
			possible_chars = remove_chars(to_remove_xint_not2, possible_chars);
		}
		if (input_vector[3] != 1) {
			if (input_vector[3] != 2) {
				path[4] = 2;
			} else {
				path[4] = 0;
			}
		} else {
			path[4] = 1;
		}
		
		int[][] returner = new int[2][];
		returner[0] = path;
		
		returner[1] = new int[possible_chars.size()];
		for (int i=0; i < possible_chars.size(); i++) {
			returner[1][i] = possible_chars.get(i);
		}
		return returner;
		
	}
	
	
	public int[] filter(int[] path) {
		int[] to_remove_up = new int[8];
		to_remove_up[0] = 4;
		to_remove_up[1] = 6;
		to_remove_up[2] = 11;
		to_remove_up[3] = 12;
		to_remove_up[4] = 14;
		to_remove_up[5] = 15;
		to_remove_up[6] = 16;
		to_remove_up[7] = 23;
		
	int[] to_remove_down = new int[7];
		to_remove_down[0] = 1;
		to_remove_down[1] = 3;
		to_remove_down[2] = 7;
		to_remove_down[3] = 9;
		to_remove_down[4] = 10;
		to_remove_down[5] = 19;
		to_remove_down[6] = 21;
		
	int[] to_remove_left = new int[1];
		to_remove_left[0]  = 0;
	int[] to_remove_right = new int[2];
		to_remove_right[0] = 2;
		to_remove_right[1] = 5;
		
	int[] to_remove_yint_not2 = new int[1];
		to_remove_yint_not2[0] = 2;
	
	int[] to_remove_yint_not3 = new int[2];
		to_remove_yint_not3[0] = 4;
		to_remove_yint_not3[1] = 17;
		
	int[] to_remove_yint_not1 = new int[5];
		to_remove_yint_not1[0] = 7;
		to_remove_yint_not1[1] = 12;
		to_remove_yint_not1[2] = 16;
		to_remove_yint_not1[3] = 19;
		to_remove_yint_not1[4] = 20;
	
	int[] to_remove_xint_not1 = new int[4];
		to_remove_xint_not1[0] = 2;
		to_remove_xint_not1[1] = 4;
		to_remove_xint_not1[2] = 9;
		to_remove_xint_not1[3] = 17;
		
	int[] to_remove_xint_not2 = new int[3];
		to_remove_xint_not2[0] = 13;
		to_remove_xint_not2[1] = 19;
		to_remove_xint_not2[2] = 20;
		
		LinkedList<Integer> possible_chars = new LinkedList<Integer>();
		for (int i=0; i < 25; i++) {
			if (i != 8) {
				possible_chars.add(i);
			}
		}
		
		
		
		if (path[1] == 1) { // up
			possible_chars = remove_chars(to_remove_up, possible_chars);

		}
		if (path[1] == 0) { // down
			possible_chars = remove_chars(to_remove_down, possible_chars);
		}
		if (path[2] == 1) { // right
			possible_chars = remove_chars(to_remove_right, possible_chars);
		}
		if (path[2] == 0) { // left
			possible_chars = remove_chars(to_remove_left, possible_chars);

		}
		if (path[3] == 3 || path[3] == 4 || path[3] == 6 ) {
			possible_chars = remove_chars(to_remove_yint_not1, possible_chars);
			
		}
		if (path[3] == 3 || path[3] == 5 || path[3] == 6) {
			possible_chars = remove_chars(to_remove_yint_not2, possible_chars);
		}
		if (path[3] == 4 || path[3] == 5 || path[3] == 6) {
			possible_chars = remove_chars(to_remove_yint_not3, possible_chars);
		}
		if (path[4] == 0 || path[4] == 2) {
			possible_chars = remove_chars(to_remove_xint_not1, possible_chars);
		}
		if (path[4] == 1 || path[4] == 2) {
			possible_chars = remove_chars(to_remove_xint_not2, possible_chars);

		}

		
		int[] returner = new int[possible_chars.size()];
		
		for (int i=0; i < possible_chars.size(); i++) {
			returner[i] = possible_chars.get(i);
		}
		return returner;
		
	}
	
	
	public LinkedList<Integer> remove_chars(int[] to_remove, LinkedList<Integer> possible_chars) {
		int counter =0;

		
		int start_size = possible_chars.size();
		for (int i=0; i < start_size; i++) {
			
			int this_char = possible_chars.get(counter);
			for (int j=0; j < to_remove.length; j++) {
				if (this_char == to_remove[j]) {
					possible_chars.remove(counter);
					counter--;
					break;
				}
			}
			counter++;
			
		}
		return possible_chars;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		//ReadTrainingCharacters.combine_filtered_files((new Filter()).results_0_0_0_0_0, true, "0.0.0.0.0.tr");
		//ReadTrainingCharacters.combine_filtered_files((new Filter()).results_0_0_0_0_0, false, "0.0.0.0.0.ts");
		
		
		
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
		Filter f = new Filter();
		for (int j=0; j < paths.length; j++) {
		for (int i=0; i < paths[0].length; i++) {
			System.out.print(paths[j][i] + ", ");
		}
		System.out.println();
		}
		for (int i=0; i < paths.length; i++) {
			
			possible_chars_per_path[i] = f.filter(paths[i]);
			
		}
		
		
		String s = paths[0][0] + "." + paths[0][1] + "." + paths[0][2] + "." + paths[0][3] + "." + paths[0][4] + "."; 
		String sts = s + "ts";
		String str = s + "tr";
		
		//ReadTrainingCharacters.combine_filtered_files(possible_chars_per_path[0], true, str);
		ReadTrainingCharacters.combine_filtered_files(possible_chars_per_path[0], false, sts);
		
		for (int i=1; i < possible_chars_per_path.length; i++) {
			//ReadTrainingCharacters.combine_filtered_files(possible_chars_per_path[i], true, paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4] + ".tr");
			ReadTrainingCharacters.combine_filtered_files(possible_chars_per_path[i], false, paths[i][0] + "." + paths[i][1] + "." + paths[i][2] + "." + paths[i][3] + "." + paths[i][4] + ".ts");
		}
		
		
	}
}
