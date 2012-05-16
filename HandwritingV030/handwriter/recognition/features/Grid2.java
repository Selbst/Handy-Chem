package recognition.features;

import java.util.Iterator;

import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;



public class Grid2 {
	private double[][][] gridFeatures3x3 = new double[3][3][4];
	private double[][][] gridFeatures2x2 = new double[2][2][4];
	double ratioHeightToWidth;

	
	public Grid2() {
		for (int i=0; i < 3; i++) {
			for (int j=0; j < 3; j++) {
				for (int z=0; z < 4; z++) {
					gridFeatures3x3[i][j][z] =0;
				}
			}
		}
	}
	public Grid2(StrokeList character) {
		for (int i=0; i < 3; i++) {
			for (int j=0; j < 3; j++) {
				for (int z=0; z < 4; z++) {
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
		int grid3Elements = gridFeatures3x3.length*gridFeatures3x3[0].length*(gridFeatures3x3[0][0].length);
		int grid2Elements = gridFeatures2x2.length*gridFeatures2x2[0].length*(gridFeatures2x2[0][0].length);
		double[] output = new double[grid3Elements + grid2Elements +1];
		for (int i=0; i < gridFeatures3x3.length; i++) {
			for (int j=0; j < gridFeatures3x3[i].length; j++) {
				for (int z =0; z < gridFeatures3x3[i][j].length; z++) {
					output[gridFeatures3x3.length*i + gridFeatures3x3[i].length*j + z] = gridFeatures3x3[i][j][z];
				}
			}
		}
		for (int i=0; i < gridFeatures2x2.length; i++) {
			for (int j=0; j < gridFeatures2x2[i].length; j++) {
				for (int z=0; z < gridFeatures2x2[i][j].length; z++) {
					output[grid3Elements + gridFeatures2x2.length*i + gridFeatures2x2[i].length*j + z] = gridFeatures2x2[i][j][z];
				}
			}
		}
		output[output.length-1] = ratioHeightToWidth;
		return output;
	}
	public double[] getFlattenedFeatures2x2() {
		
		int grid2Elements = gridFeatures2x2.length*gridFeatures2x2[0].length*(gridFeatures2x2[0][0].length);

		double[] output = new double[grid2Elements];

		for (int i=0; i < gridFeatures2x2.length; i++) {
			for (int j=0; j < gridFeatures2x2[i].length; j++) {
				for (int z=0; z < gridFeatures2x2[i][j].length; z++) {
					output[gridFeatures2x2.length*i + gridFeatures2x2[i].length*j + z] = gridFeatures2x2[i][j][z];
				}
			}
		}
		return output;
	}
	
	public double[] getFlattenedFeatures3x3() {
		
		int grid3Elements = gridFeatures3x3.length*gridFeatures3x3[0].length*(gridFeatures3x3[0][0].length);

		double[] output = new double[grid3Elements];

		for (int i=0; i < gridFeatures3x3.length; i++) {
			for (int j=0; j < gridFeatures3x3[i].length; j++) {
				for (int z=0; z < gridFeatures3x3[i][j].length; z++) {
					output[gridFeatures3x3.length*i + gridFeatures3x3[i].length*j + z] = gridFeatures3x3[i][j][z];
				}
			}
		}
		return output;
	}
	
	
	public double[] getNetworkInputs(StrokeList character) {
		setFeatures(character);
		setHeightWidthRatio(character);
		return getFlattenedFeatures();
	}
	
	public void setFeatures(StrokeList character) {
		Stroke test = character.getStrokes().getFirst();

		double topHorizontal = character.minY + character.height*.333333333333;
		double bottomHorizontal = character.minY + character.height*.66666666667;
		double leftVertical = character.minX + character.width*.333333333333;
		double rightVertical = character.minX + character.width*.666666666667;
		
		double equator = character.minY + character.height*.5;
		double primeMeridian = character.minX + character.width*.5;
		
		double[] areas;
		int xIndex; int yIndex, xIndex2x2, yIndex2x2;
		int[][] countCoordinates = new int[3][3]; // counts how many coordinates have been placed in grid so averaging can be done.
		int[][] countCoordinates2x2 = new int[2][2];
		
		double xAvg, yAvg;
		
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
				
					if (xAvg > primeMeridian) {
						xIndex = 1; // rightmost
					} else { xIndex = 0;}
					if (yAvg > equator) {
						yIndex = 1; // bottommost
					} else { yIndex = 0;}
					areas = thisCoordinate.getAreas();
				
					for (int i=0; i < 4; i++) {
						gridFeatures2x2[xIndex][yIndex][i] += areas[i];
					}	
					countCoordinates2x2[xIndex][yIndex]++;
					
					for (int i=0; i < 4; i++) {
						gridFeatures3x3[xIndex][yIndex][i] += areas[i];
					}	
					countCoordinates[xIndex][yIndex]++;
				
				}
			}
			
		}
		
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
		for (int i=0; i < 2; i++) {
			for (int j=0; j < 2; j++) {
				for (int z=0; z < 4; z++) {
					if (countCoordinates[i][j] != 0) {
						gridFeatures2x2[i][j][z] = gridFeatures2x2[i][j][z] / countCoordinates2x2[i][j];
						gridFeatures2x2[i][j][z] = gridFeatures2x2[i][j][z] / 100;
					}
				}
			}
		}
	}
	
	public void setHeightWidthRatio(StrokeList character) {
		ratioHeightToWidth = character.height/character.width;
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
