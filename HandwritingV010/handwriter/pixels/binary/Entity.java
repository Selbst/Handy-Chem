package pixels.binary;

import java.util.Iterator;
import java.util.LinkedList;

/*
 * An entity is essentially a connected set of coordinates that is not connected to anything else.
 * 
 * i.e. the dot of an 'i' is an entity, as is the line below the dot.  A 'g' is a single entity.
 * 
 * feature extraction must occur before image compression.
 */
public class Entity {
	public LinkedList<SimpleCoordinate> coordinates;
	public int height; public int width; public int maxRow; public int minRow; public int minColumn; public int maxColumn;
	public double northernDensity=0; public double southernDensity=0; public double westernDensity=0; public double easternDensity=0;
	public double[] relativeDensities = new double[4]; public double ratioHtoW;
	public int[][] image; public int[][] compressedImage; public int ID;
	int numPixels;
	public int[][] relativeImage;
	public Entity(LinkedList<SimpleCoordinate> list) {
		coordinates = list;
		this.setFeatures();
	}
	public void setFeatures() {
		findMinMax();
		height = maxRow - minRow +1; width = maxColumn - minColumn +1;  ratioHtoW = height/width;
		setImage();
		numPixels = coordinates.size();
		findDensities();
		
		
	}
	public void findMinMax() {
		Iterator<SimpleCoordinate> iterator = coordinates.iterator();
		SimpleCoordinate thisCoordinate; thisCoordinate = iterator.next();
		maxRow = thisCoordinate.row; minRow = maxRow;
		maxColumn = thisCoordinate.column; minColumn = maxColumn;
		while (iterator.hasNext()) {
			thisCoordinate = iterator.next();
			if (thisCoordinate.row > maxRow) {
				maxRow = thisCoordinate.row;
			}
			if (thisCoordinate.row < minRow) {
				minRow = thisCoordinate.row;
			}
			if (thisCoordinate.column > maxColumn) {
				maxColumn = thisCoordinate.column;
			}
			if (thisCoordinate.column < minColumn) {
				minColumn = thisCoordinate.column;
			}
		}
	}
	public void findDensities() {
		int equator = (int)(height/2) + minRow;
		int primeMeridian = (int)(width/2) + minColumn;
		int northernPixels = 0; int southernPixels = 0; int westernPixels = 0; int easternPixels = 0;

		Iterator<SimpleCoordinate> iterator = coordinates.iterator(); SimpleCoordinate thisCoordinate;
		while (iterator.hasNext()) {
			thisCoordinate = iterator.next();
			if (thisCoordinate.row < equator) {
				northernPixels++;
			} else if (thisCoordinate.row > equator) {
				southernPixels++;
			}
			if (thisCoordinate.column < primeMeridian) {
				westernPixels++;
			} else if (thisCoordinate.column > primeMeridian) {
				easternPixels++;
			}
		}
		northernDensity = northernPixels/(double)numPixels; southernDensity = southernPixels/(double)numPixels; westernDensity = westernPixels/(double)numPixels; easternDensity = easternPixels/(double)numPixels;
		double totalDensity = northernDensity + southernDensity + westernDensity + easternDensity; 
		relativeDensities[0] = northernDensity/totalDensity; relativeDensities[1] = southernDensity/totalDensity; relativeDensities[2] = westernDensity/totalDensity; relativeDensities[3] = easternDensity/totalDensity;
		
		
	}
	public LinkedList<SimpleCoordinate> copyCoordinates() {
		LinkedList<SimpleCoordinate> copy = new LinkedList<SimpleCoordinate>();
		Iterator<SimpleCoordinate> iterator = coordinates.iterator();
		SimpleCoordinate thisCoordinate;
		while(iterator.hasNext()) {
			thisCoordinate = iterator.next();
			copy.add(new SimpleCoordinate(thisCoordinate.row, thisCoordinate.column));
		}
		
		return copy;
	}
	public void setImage() {
		image = new int[height][width]; 
		Iterator<SimpleCoordinate> iterator = coordinates.iterator(); SimpleCoordinate thisCoordinate;
		while (iterator.hasNext()) {
			thisCoordinate = iterator.next();
			image[thisCoordinate.row-minRow][thisCoordinate.column-minColumn] = 1;
		}
	}

	
	
}
