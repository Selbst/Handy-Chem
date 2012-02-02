package pixels.binary;

import java.util.Iterator;
import java.util.LinkedList;


public class PixelToEntity {
	public int[][] entities;  // entities[i] specifies which entity. entities[0][0] specifies top left corner, 1-top right, 2-bottom left, 3-bottom right.
	public int height; public int width;
	Image entireImage;
	public int[][] alreadyMapped;
	public LinkedList<Entity> entityImages = new LinkedList<Entity>();
	
	
	public LinkedList<SimpleCoordinate> nextCoordinates = new LinkedList<SimpleCoordinate>();
	public LinkedList<SimpleCoordinate> thisEntityCoordinates = new LinkedList<SimpleCoordinate>();
	
	/*
	 * Takes an image and stores all of its entities in LinkedList<Entity> entityImages
	 * 
	 */
	public void mapEntities(Image image) {
		height = image.height; width= image.width;
		entireImage = image;
		alreadyMapped = new int[image.height][image.width];
		System.out.println(width);
		for (int i=0; i < image.height; i++) { // row by column.
			for (int j=0; j < image.width; j++) {
				if (image.binaryImage[i][j] == 0) {
					alreadyMapped[i][j] = 1;
				}
				else if (alreadyMapped[i][j] == 0) { // pixel not yet mapped.
					beginPropagation(i, j); // map the entire entity with a single blow!
					nextCoordinates.clear();
					entityImages.add(new Entity((LinkedList<SimpleCoordinate>) thisEntityCoordinates.clone()));
					thisEntityCoordinates.clear();
				}
			}
		}
	}
	public void beginPropagation(int rowStart, int colStart) {
		thisEntityCoordinates.add(new SimpleCoordinate(rowStart, colStart, true));
		alreadyMapped[rowStart][colStart] = 1;
		findSurroundingPixels(rowStart, colStart);
		filterAlreadyMapped();
	}
	
	public boolean pixelValidity(int row, int column) { // just check whether pixel is off the page.
		if (row < 0 || row >= height || column < 0 || column >= width) {return false;}
		else {return true;}
	}
	
	public void findSurroundingPixels(int row, int column) { // filters all surrounding pixels that are off page out. stores them in nextCoordinates.
		if (pixelValidity(row-1, column)) { // up.
			nextCoordinates.add(new SimpleCoordinate(row-1, column, false));
		}
		if (pixelValidity(row+1, column)) { // down
			nextCoordinates.add(new SimpleCoordinate(row+1, column, false));
		}
		if (pixelValidity(row, column-1)) { // left
			nextCoordinates.add(new SimpleCoordinate(row, column-1, false));
		}
		if (pixelValidity(row, column+1)) { // right
			nextCoordinates.add(new SimpleCoordinate(row, column+1, false));
		}
		if (pixelValidity(row-1, column-1)) { // up and left
			nextCoordinates.add(new SimpleCoordinate(row-1, column-1, false));
		}
		if (pixelValidity(row-1, column+1)) { // up and right
			nextCoordinates.add(new SimpleCoordinate(row-1, column+1, false));
		}
		if (pixelValidity(row+1, column-1)) { // down and left
			nextCoordinates.add(new SimpleCoordinate(row+1, column-1, false));
		}
		if (pixelValidity(row+1, column+1)) { // down and right
			nextCoordinates.add(new SimpleCoordinate(row+1, column+1, false));
		}
	}
	public void filterAlreadyMapped() {
		Iterator<SimpleCoordinate> iterator = nextCoordinates.iterator(); int counter = 0;
		SimpleCoordinate thisCoord;
		while (iterator.hasNext()) {
			thisCoord = iterator.next();
			if (alreadyMapped[thisCoord.row][thisCoord.column] == 0) {
				if (entireImage.binaryImage[thisCoord.row][thisCoord.column] == 1) {
						iterator.remove();
						beginPropagation(thisCoord.row, thisCoord.column);
						iterator = nextCoordinates.iterator();
				} else { 
					alreadyMapped[thisCoord.row][thisCoord.column] = 1;
					iterator.remove();
					//nextCoordinates.remove(counter);
					
				}
			} else { 
				iterator.remove();

				/*nextCoordinates.remove(counter);
				iterator = nextCoordinates.iterator();
				for (int i=0; i < counter; i++) {
					iterator.next();
				} counter--;*/ 
			}
			counter++;
		}
	}
	
	
	public static void main (String[] args) {
		int[][] image = new int[10][30];
		for (int i=0; i < image.length; i++) {
			for (int j=0; j < image[0].length; j++) {
				if (i != 2) {
					image[i][j] = 1;
				}
				System.out.print(image[i][j]);
			}
			System.out.println();
		}
		Image Image = new Image(image);
		PixelToEntity holder = new PixelToEntity();
		holder.mapEntities(Image);
		Iterator<Entity> entityIterator = holder.entityImages.iterator();
		Iterator<SimpleCoordinate> coordinateIterator;
		SimpleCoordinate thisCoordinate;
		Entity thisEntity;
		while (entityIterator.hasNext()) {
			System.out.println("new entity:");
			thisEntity = entityIterator.next();
			coordinateIterator = thisEntity.coordinates.iterator();
			System.out.println("height: " + thisEntity.height + ", width: " + thisEntity.width + "\nmaxRow: " + thisEntity.maxRow + ", maxColumn: " + thisEntity.maxColumn + "\nminRow: " + thisEntity.minRow + ", minColumn: " + thisEntity.minColumn);
			System.out.println("relativeDensities: ");
			System.out.println("        " + thisEntity.relativeDensities[0] + ", " + thisEntity.relativeDensities[1] + ", " + thisEntity.relativeDensities[2] + ", " + thisEntity.relativeDensities[3]);
			while (coordinateIterator.hasNext()) {
				thisCoordinate = (SimpleCoordinate) coordinateIterator.next();
				System.out.println("row: " + thisCoordinate.row + ", column: " + thisCoordinate.column);
			}
			System.out.println();
		}
		
		
		
		
	}
	
	
}
