package recognition.features;

import java.util.Iterator;

import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;



public class Grid {
	private double[][][] gridFeatures3x3 = new double[3][3][5];
	private double[][][] gridFeatures2x2 = new double[2][2][4];
	public Grid() {
		for (int i=0; i < 3; i++) {
			for (int j=0; j < 3; j++) {
				for (int z=0; z < 5; z++) {
					gridFeatures3x3[i][j][z] =0;
				}
			}
		}
	}
	public Grid(StrokeList character) {
		for (int i=0; i < 3; i++) {
			for (int j=0; j < 3; j++) {
				for (int z=0; z < 5; z++) {
					gridFeatures3x3[i][j][z] =0;
				}
			}
		}
		setFeatures(character);
	}
	public double[][][] getFeatures3x3() {
		return gridFeatures3x3;
	}
	public double[] getFlattenedFeatures() {
		double[] output = new double[gridFeatures3x3.length*gridFeatures3x3[0].length*(gridFeatures3x3[0][0].length-1)];
		for (int i=0; i < gridFeatures3x3.length; i++) {
			for (int j=0; j < gridFeatures3x3[i].length; j++) {
				for (int z =0; z < gridFeatures3x3[i][j].length-1; z++) {
					output[gridFeatures3x3.length*i + gridFeatures3x3[i].length*j + z] = gridFeatures3x3[i][j][z];
				}
			}
		}
		return output;
	}
	public double[] getNetworkInputs(StrokeList character) {
		setFeatures(character);
		return getFlattenedFeatures();
	}
	
	public void setFeatures(StrokeList character) {
		Stroke test = character.getStrokes().getFirst();
		//System.out.println(test.toString());
		//System.out.println(test.metaToString());

		double topHorizontal = character.minY + character.height*.333333333333;
		double bottomHorizontal = character.minY + character.height*.66666666667;
		double leftVertical = character.minX + character.width*.333333333333;
		double rightVertical = character.minX + character.width*.666666666667;
		
		//System.out.println("top horizontal: " + topHorizontal + ", bottom horizontal: " + bottomHorizontal + ", " + "left vertical: "
					 //+ leftVertical + ", right vertical: " + rightVertical);
		
		double[] areas;
		int xIndex; int yIndex;
		boolean[][][] countStrokes = new boolean[character.getStrokes().size()][3][3]; // tracks how many unique strokes go through a grid.
		int[][] countCoordinates = new int[3][3]; // counts how many coordinates have been placed in grid so averaging can be done.
		int counter = 0; double xAvg, yAvg;
		//System.out.println("min x: " + character.minX);
		//System.out.println("min y: " + character.minY);
		
		Iterator<Stroke> strokeIterator = character.getStrokes().iterator(); Stroke thisStroke;
		Iterator<Coordinate> coordinateIterator; Coordinate thisCoordinate;
		while (strokeIterator.hasNext()) {
			thisStroke = strokeIterator.next();
			coordinateIterator = thisStroke.metaCoordinates.iterator();
			while (coordinateIterator.hasNext()) {
				thisCoordinate = coordinateIterator.next();
				xAvg = thisCoordinate.getXAvg();
				yAvg = thisCoordinate.getYAvg();
				if (!(thisCoordinate.getXAvg() == 0 && thisCoordinate.getYAvg() == 0)) {
					//System.out.println("x avg: " + xAvg);
					//System.out.println("y avg: " + yAvg);
					if (xAvg > rightVertical) {
						xIndex = 2; // rightmost
					} else if (xAvg > leftVertical) {
						xIndex = 1; // center
					} else { xIndex = 0;}
					if (yAvg > bottomHorizontal) {
						yIndex = 2; // bottommost
					} else if (yAvg > topHorizontal) {
						yIndex = 1; // center
					} else { yIndex = 0;}
					areas = thisCoordinate.getAreas();
				
					for (int i=0; i < 4; i++) {
						gridFeatures3x3[xIndex][yIndex][i] += areas[i];
					}	
					countCoordinates[xIndex][yIndex]++;
				
					countStrokes[counter][xIndex][yIndex] = true;
				}
			}
			counter++;
			
		}
		//System.out.println("coordinates[0][0]: " + countCoordinates[0][0]);
		
		// take average.
		for (int i=0; i < 3; i++) {
			for (int j=0; j < 3; j++) {
				for (int z=0; z < 4; z++) {
					if (countCoordinates[i][j] != 0) {
						gridFeatures3x3[i][j][z] = gridFeatures3x3[i][j][z] / countCoordinates[i][j];
						gridFeatures3x3[i][j][z] = gridFeatures3x3[i][j][z] / 100;
					}
				}
			}
		}
		
		// count unique strokes in each grid.
		for (int i=0; i < character.getStrokes().size(); i++) {
			for (int j=0; j < 3; j++) {
				for (int z =0; z < 3; z++) {
					if (countStrokes[i][j][z]) {
						gridFeatures3x3[j][z][4]++;
					}
				}
			}
		}
		
		
		
	}
	
	
	public String toString() {
		
		String s = "";
		for (int i=0; i < 3; i++) {
			for (int j=0; j < 3; j++) {
				s += "[";
				for (int z =0; z < 5; z++) {
					s += gridFeatures3x3[i][j][z] +", ";
				}
				s += "]";
			}
			s += "\n";
			s += "-----------\n"; 
		}
		return s;
		
	}
	

}
