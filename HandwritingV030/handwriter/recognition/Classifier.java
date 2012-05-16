package recognition;

import neuralnetwork.strokenetwork.StrokeNetwork;

public class Classifier {
	int[] filter_path;
	int[][] input_sizes;
	StrokeNetwork[][] neural_nets;
	int layer;
	int classifier_index;
	
	public Classifier(int layer, int classifier_index) {
		this.layer = layer;
		this.classifier_index = classifier_index;
		
		if (layer == 1) {
			if (classifier_index == 0) {
				filter_path = new int[5];
					filter_path[0] = 0;
					filter_path[1] = 0;
					filter_path[2] = 0;
					filter_path[3] = 3;
					filter_path[4] = 0;
					
			} else if (classifier_index == 1) {
				filter_path = new int[5];
					filter_path[0] = 0;
					filter_path[1] = 0;
					filter_path[2] = 0;
					filter_path[3] = 3;
					filter_path[4] = 1;
			} else if (classifier_index == 2) {
				filter_path = new int[5];
					filter_path[0] = 0;
					filter_path[1] = 0;
					filter_path[2] = 0;
					filter_path[3] = 3;
					filter_path[4] = 2;
			} else if (classifier_index == 3) {
				filter_path = new int[5];
					filter_path[0] = 0;
					filter_path[1] = 0;
					filter_path[2] = 0;
					filter_path[3] = 4;
					filter_path[4] = 0;
			} else if (classifier_index == 4) {
				filter_path = new int[5];
					filter_path[0] = 0;
					filter_path[1] = 0;
					filter_path[2] = 0;
					filter_path[3] = 4;
					filter_path[4] = 1;	
			} else if (classifier_index == 5) {
				filter_path = new int[5];
					filter_path[0] = 0;
					filter_path[1] = 0;
					filter_path[2] = 0;
					filter_path[3] = 4;
					filter_path[4] = 2;	
			} else if (classifier_index == 6) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 0;	
			} else if (classifier_index == 7) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 1;	
			} else if (classifier_index == 8) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 2;	
			} else if (classifier_index == 9) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 0;	
			} else if (classifier_index == 10) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 1;	
			} else if (classifier_index == 11) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 2;	
			} else if (classifier_index == 12) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 0;	
			} else if (classifier_index == 13) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 1;	
			} else if (classifier_index == 14) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 2;	
			} else if (classifier_index == 15) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 0;	
			} else if (classifier_index == 16) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 1;	
			} else if (classifier_index == 17) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 2;	
			} else if (classifier_index == 18) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 0;	
			} else if (classifier_index == 19) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 1;	
			} else if (classifier_index == 20) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 2;	
			} else if (classifier_index == 21) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 0;	
			} else if (classifier_index == 22) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 1;	
			} else if (classifier_index == 23) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 2;	
			} else if (classifier_index == 24) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 0;	
			} else if (classifier_index == 25) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 1;	
			} else if (classifier_index == 26) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 2;	
			} else if (classifier_index == 27) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 0;	
			} else if (classifier_index == 28) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 1;	
			} else if (classifier_index == 29) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 2;	
			} else if (classifier_index == 30) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 0;	
			} else if (classifier_index == 31) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 1;	
			} else if (classifier_index == 32) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 2;	
			} else if (classifier_index == 33) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 0;	
			} else if (classifier_index == 34) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 1;	
			} else if (classifier_index == 35) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 2;	
			} else if (classifier_index == 36) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 0;	
			} else if (classifier_index == 37) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 1;	
			} else if (classifier_index == 38) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 2;	
			} else if (classifier_index == 39) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 0;	
			} else if (classifier_index == 40) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 1;	
			} else if (classifier_index == 41) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 2;	
			} else if (classifier_index == 42) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 0;	
			} else if (classifier_index == 43) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 1;	
			} else if (classifier_index == 44) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 2;	
			} else if (classifier_index == 45) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 0;	
			} else if (classifier_index == 46) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 1;	
			} else if (classifier_index == 47) {
				filter_path = new int[5];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 2;	
			}	
		} else if (layer == 2) {
			if (classifier_index == 0) { 
				filter_path = new int[6];
					filter_path[0] = 0;
					filter_path[1] = 0;
					filter_path[2] = 0;
					filter_path[3] = 3;
					filter_path[4] = 0;
					filter_path[5] = 0;
			} else if (classifier_index == 1) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 2) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 3) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 4) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 5) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 6) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 7) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 8) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 9) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 10) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 11) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 12) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 13) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 14) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 15) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 16) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 17) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 18) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 19) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 20) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 21) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 22) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 23) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 24) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 25) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 26) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 27) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 28) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 29) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 30) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 31) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 32) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 33) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 34) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 35) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 36) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 37) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 38) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 39) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 40) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 41) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 42) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 43) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 44) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 45) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 46) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 47) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 0;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 48) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 49) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 50) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 51) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 52) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 53) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 3;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 54) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 55) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 56) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 57) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 58) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 59) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 4;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 60) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 61) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 62) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 63) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 64) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 65) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 5;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 66) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 67) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 68) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 69) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 70) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 71) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 0;
				filter_path[3] = 6;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 72) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 73) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 74) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 75) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 76) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 77) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 3;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 78) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 79) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 80) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 81) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 82) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 83) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 4;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 84) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 85) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 86) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 87) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 88) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 89) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 5;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			} else if (classifier_index == 90) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 0;
				filter_path[5] = 0;				
			} else if (classifier_index == 91) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 0;
				filter_path[5] = 1;				
			} else if (classifier_index == 92) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 1;
				filter_path[5] = 0;				
			} else if (classifier_index == 93) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 1;
				filter_path[5] = 1;				
			} else if (classifier_index == 94) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 2;
				filter_path[5] = 0;				
			} else if (classifier_index == 95) {	
				filter_path = new int[6];
				filter_path[0] = 0;
				filter_path[1] = 1;
				filter_path[2] = 1;
				filter_path[3] = 6;
				filter_path[4] = 2;
				filter_path[5] = 1;				
			}
		
		}
	}
	public int get_next_classification() {
		return 0;
	}
	
	public int get_next_branch() {
		return 0;
	}
	
	
	
}
