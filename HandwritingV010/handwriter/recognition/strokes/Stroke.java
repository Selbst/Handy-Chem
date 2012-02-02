package recognition.strokes;
import java.util.Iterator;
import java.util.LinkedList;

import pixels.binary.Entity;

import line.FuzzyLineClassifier;
import line.Line;
import line.LineConnect;



/*
 * 	A stroke begins when the "pen" is pressed down, and ends when it is lifted back up.
 * 
 * 	Stroke data:
 * 		relative coordinates
 * 		absolute coordinates
 * 		start point
 * 		end point
 * 		duration
 * 		center point
 * 		height
 * 		width
 * 		classification
 * 		meta-coordinates (fuzzy line-segmenting)
 * 
 * 		
 */
public class Stroke {
	private LinkedList<Coordinate> coordinates; public LinkedList<Coordinate> getCoordinates(){return coordinates;}
	private int minY=-1; private int minX=-1; private int maxY=-1; private int maxX=-1;
	public int getMinY() {return minY;} 
	public int getMinX(){return minX;} 
	public int getMaxY() {return maxY;} 
	public int getMaxX() {return maxX;}
	public void setMinY(int m) {minY = m;} 
	public void setMinX(int m) { minX = m;} 
	public void setMaxY(int m) { maxY = m;} 
	public void setMaxX(int m) { maxX = m;}
	boolean strokeTerminate = false; 
	public void setStrokeTerminate() {strokeTerminate = true;}
	private Coordinate centerPoint = null; 
	public Coordinate getCenterPoint() {return centerPoint;}
	private int strokeHeight;
	private int strokeWidth;
	public int getStrokeHeight(){return strokeHeight;} 
	public int getStrokeWidth(){return strokeWidth;}
	public void setHeight(int h) {strokeHeight = h;} 
	public void setWidth(int w) {strokeWidth = w;}
	private long strokeStartTime; 
	private long strokeEndTime; 
	private long strokeDuration; 
	public long getStrokeDuration() {return strokeDuration;}
	private LinkedList<Coordinate> relativeCoordinates; 
	public LinkedList<Coordinate> getRelativeCoordinates(){return relativeCoordinates;} 
	public void setRelativeCoordinates(LinkedList<Coordinate> coordinates) {relativeCoordinates = coordinates;}
	private int ID=-1; public int getID() {return ID;} public void setID(int id) {ID=id;}
	
	public LinkedList<Coordinate> metaCoordinates;
	
	private LinkedList<Coordinate> characterRelativeCoordinates; 
	public LinkedList<Coordinate> getCharacterRelativeCoordinates(){return characterRelativeCoordinates;} 
	public void setCharacterRelativeCoordinate(LinkedList<Coordinate> charCoords) {
		characterRelativeCoordinates = charCoords;
	}
	
	public Entity entity;
	
	private int numSegmentsPostFill = 20;
	
	private boolean isDot = false; public boolean checkIfDot() {return isDot;}
	
	public Stroke (){
		coordinates = new LinkedList<Coordinate>();
		strokeStartTime = System.currentTimeMillis();
	}
	public Stroke(int x, int y) {
		coordinates = new LinkedList<Coordinate>();
		coordinates.add(new Coordinate(x,y));
		strokeStartTime = System.currentTimeMillis();
	}
	public void addCoordinate(int x, int y) {
		coordinates.add(new Coordinate(x,y));
	}
	public void addCoordinate(int x, int y, long t) {
		coordinates.add(new Coordinate(x, y, t));
	}
	public void add(Coordinate coordinate) {
		coordinates.addFirst(coordinate);
	}
	public Coordinate getCoordinatesHead() {
		if (coordinates != null) {return coordinates.getFirst();}
		else {return null;}
	}
	public boolean hasNext() {
		return false;
	}
	public void terminate() {
		strokeTerminate = true;
		//System.out.println("coords 1: " + coordinates.size());
		strokeEndTime = System.currentTimeMillis();
		strokeDuration = strokeEndTime - strokeStartTime;
		fill_coordinates();
		//System.out.println("coords 2: " + coordinates.size());

		setMaxesAndMins(); // needs to go first
		findCenterPoint();
		findHeightAndWidth();
		makeRelativeCoordinates();
		segment();
		setMetaCoordinates();
		//System.out.println("coords 3: " + coordinates.size());

	}
	public void setMaxesAndMins() {
		Iterator<Coordinate> coordinateIterator = null;
		Coordinate coordinatePointer = null;
		int currentMaxY = -1; int currentMaxX =-1; int currentMinY=-1; int currentMinX=-1; int currentY; int currentX;
		if (strokeTerminate) {
			coordinateIterator = coordinates.iterator();
			while (coordinateIterator.hasNext()) {
				coordinatePointer = coordinateIterator.next();
				currentY = coordinatePointer.getY(); currentX = coordinatePointer.getX();
				if (currentMaxY == -1) {
					currentMaxY = currentMinY = coordinatePointer.getY();
					currentMaxX = currentMinX = coordinatePointer.getX();
				}
				else {
					if (currentY > currentMaxY) {currentMaxY = currentY;} else if (currentY < currentMinY) {currentMinY = currentY;}
					if (currentX > currentMaxX) {currentMaxX = currentX;} else if (currentX < currentMinX) {currentMinX = currentX;}
				}
			}
			minY = currentMinY; minX = currentMinX; maxY = currentMaxY; maxX = currentMaxX;
		}
	}
	public void findCenterPoint() {if (minY != -1) {centerPoint = new Coordinate((int)(maxX + minX)/2,(int)(maxY+minY)/2 );}	}
	public void findHeightAndWidth() {if (minY != -1) {strokeHeight = maxY - minY; strokeWidth = maxX - minX;}
	}
	public void makeRelativeCoordinates() {
		if (strokeTerminate) {
			//System.out.println("stroke terminate");
			Iterator<Coordinate> coordinateIterator = coordinates.iterator();
			Coordinate coordinatePointer; Coordinate relativeCoordinate;
			relativeCoordinates = new LinkedList<Coordinate>();	
			while (coordinateIterator.hasNext()) {
				coordinatePointer = coordinateIterator.next();
				relativeCoordinate = new Coordinate(coordinatePointer.getX() - minX, coordinatePointer.getY() - minY);
				relativeCoordinates.add(relativeCoordinate);
			}
		}
	}
	public int getNumSegments(int strokeSize) {
		int numSegments;
		if (strokeHeight < 10 && strokeWidth < 10) { // classify as a dot.
			isDot = true;
			return 1;
		} else if (strokeSize/numSegmentsPostFill < 5) {
			if (strokeSize/(numSegmentsPostFill/2) < 5) {
				if (strokeSize/5 < (numSegmentsPostFill/4)) {numSegments = 2;}
				else {numSegments = strokeSize/5;}
			} else {numSegments =10;}
		}else {numSegments=20;}
		return numSegments;
	}
	public void segment() {
		
		int numSegments = getNumSegments(relativeCoordinates.size());
		
		if (!isDot) {
			Iterator<Coordinate> coordinateIterator = relativeCoordinates.listIterator();
			Coordinate coordinatePointer;
			LinkedList<Coordinate> tempStroke = new LinkedList<Coordinate>();
			Line oddLine = null; Line evenLine = null; boolean parity =false; boolean enoughCoordinates = true; boolean first = true; double deviations[];
			int iterator = -1; int strokeSize = relativeCoordinates.size();  int numCoordsInStroke = strokeSize/numSegments;
			int countMetaCoords =0; double xAvg=0, yAvg=0;
		
		
			while (iterator < numSegments-1) { if (parity) {parity=false;} else {parity=true;} //true=odd false=even
				iterator++;
				for (int i=0; i < numCoordsInStroke; i++) {
					if (coordinateIterator.hasNext()) {
						coordinatePointer = coordinateIterator.next();
						xAvg = xAvg + coordinatePointer.getX() + minX;
						yAvg = yAvg + coordinatePointer.getY() + minY;
						tempStroke.add(coordinatePointer);
					}
					else { enoughCoordinates = false;}
				}
				xAvg = xAvg / numCoordsInStroke;
				yAvg = yAvg / numCoordsInStroke;
				if (enoughCoordinates) {
					if (parity) {
						oddLine = Line.regressionLine(tempStroke);
						oddLine.setClassification();
						if (!first) {
							deviations =oddLine.getClassification().compareLines(evenLine.getClassification());
							Coordinate metaCoord = new Coordinate(-1,-1, xAvg, yAvg);
							metaCoord.setAreas(oddLine.getClassification().getAreas());
							metaCoord.setDeviations(deviations);
							relativeCoordinates.add(iterator*numCoordsInStroke, metaCoord);
							countMetaCoords++;
							coordinateIterator = relativeCoordinates.listIterator(iterator*numCoordsInStroke + countMetaCoords);
						}
						else {
							Coordinate metaCoord = new Coordinate(-2,-2, xAvg, yAvg);
							metaCoord.setAreas(oddLine.getClassification().getAreas());
							relativeCoordinates.add(iterator*numCoordsInStroke, metaCoord);
							countMetaCoords++;
							coordinateIterator = relativeCoordinates.listIterator(iterator*numCoordsInStroke + countMetaCoords);

						}
					} else {
						evenLine = Line.regressionLine(tempStroke);
						evenLine.setClassification();
						deviations = evenLine.getClassification().compareLines(oddLine.getClassification());
						Coordinate metaCoord = new Coordinate(-1,-1, xAvg, yAvg);
						metaCoord.setAreas(evenLine.getClassification().getAreas());
						metaCoord.setDeviations(deviations);
						relativeCoordinates.add(iterator*numCoordsInStroke, metaCoord);
						countMetaCoords++;
						coordinateIterator = relativeCoordinates.listIterator(iterator*numCoordsInStroke + countMetaCoords);

					}
				} 
				tempStroke.clear();
				first = false; xAvg =0; yAvg = 0;
			}
		} else { System.out.println("is dot");}
		
		
	}
	/*
	 * meta Coordinates have relative coordinates and fuzzy elementary perceptual codes.
	 * 
	 */
	public void setMetaCoordinates() {
		Iterator<Coordinate> coordinateIterator = relativeCoordinates.listIterator();
		Coordinate coordinatePointer; LinkedList<Coordinate> tempList = new LinkedList<Coordinate>();
		while (coordinateIterator.hasNext()) {
			coordinatePointer = coordinateIterator.next();
			if (coordinatePointer.getX() == -1 || coordinatePointer.getX() == -2) {
				tempList.add(coordinatePointer);
			}
		}
		metaCoordinates = tempList;
		//fillMetaCoordinates();

	}
	
	/*
	 * takes sparse strokes and smoothes their elementary perceptual code classifications.
	 * 
	 */
	public void fillMetaCoordinates() {
		
		int size = metaCoordinates.size();
		int numFillsPerCoord = (int) numSegmentsPostFill/size-1; 
		int remainderFills = numSegmentsPostFill%size;
		
		LinkedList<Coordinate> pre = (LinkedList<Coordinate>) metaCoordinates.clone(); metaCoordinates.clear(); int index = 0;
		if (numFillsPerCoord != 0) {
			Iterator<Coordinate> iteratePre = pre.iterator(); Coordinate thisCoord;
			while (iteratePre.hasNext()) {
				thisCoord = (Coordinate) iteratePre.next();
				metaCoordinates.add(thisCoord);
				for (int i=0; i < numFillsPerCoord; i++) {
					metaCoordinates.add(thisCoord.copy());
					//System.out.println(index);
					index++;
				}
			}
			for (int i=0; i < remainderFills; i++) {
				metaCoordinates.add(pre.getLast().copy());
				//System.out.println(index);
				index++;
			}
			
		}
		
			
		
	}
	
	
	public String toString() {
		String returnString = ""; boolean returnNullString = false;
		Iterator<Coordinate> coordinateIterator = null; Coordinate coordinatePointer = null;
		if (coordinates != null) {
			coordinateIterator = coordinates.iterator();
			if (strokeTerminate) {
				returnString += "height: " + strokeHeight + "\nwidth: "+ strokeWidth + "\ncenter point: "+centerPoint.getX() + "," + centerPoint.getY();
				returnString += "\nstroke duration: " + strokeDuration;
			}
		} else {returnNullString=true;}
		if (!returnNullString) {
			int coordNum = 0;
			while (coordinateIterator.hasNext()) {
				coordinatePointer = coordinateIterator.next();
				returnString += ", "+coordinatePointer.toString();
				coordNum++;
			}
			returnString += "\n";
		}
		return returnString;
	}
	public String relativeToString() {
		String returnString = ""; boolean returnNullString = false;
		Iterator<Coordinate> coordinateIterator = null; Coordinate coordinatePointer = null;
		if (relativeCoordinates != null) {
			coordinateIterator = relativeCoordinates.iterator();
			if (strokeTerminate) {
				returnString += "height: " + strokeHeight + "\nwidth: "+ strokeWidth + "\ncenter point: "+centerPoint.getX() + "," + centerPoint.getY();
				returnString += "\nstroke duration: " + strokeDuration;
			}
		} else {returnNullString=true;}
		if (!returnNullString) {
			int coordNum = 0;
			while (coordinateIterator.hasNext()) {
				coordinatePointer = coordinateIterator.next();
				returnString += "\n"+coordinatePointer.toString();
				coordNum++;
			}
			returnString += "\n";
		}
		return returnString;
	}
	public String metaToString() {
		String returnString = "";
		Iterator<Coordinate> coordinateIterator; Coordinate coordinatePointer;
		if (metaCoordinates != null) {
			coordinateIterator = metaCoordinates.iterator();
			int coordNum = 0;
			while (coordinateIterator.hasNext()) {
				coordinatePointer = coordinateIterator.next();
				returnString += "\ncoordinate " + coordNum + ": " + coordinatePointer.metaCoordToString();
				coordNum++;
			}
			returnString += "\n";
		}
		return returnString;
	}
	
	public void fill_coordinates() {
		LinkedList<Coordinate> new_coordinates = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> coord_iterator = coordinates.iterator();
		Coordinate this_coord;
		Coordinate next_coord = coord_iterator.next();
		while (coord_iterator.hasNext()) {
			this_coord = next_coord;
			next_coord = coord_iterator.next();
			new_coordinates.add(this_coord);
			new_coordinates.addAll(LineConnect.connect(this_coord.getX(), this_coord.getY(), next_coord.getX(), next_coord.getY()));
			
			new_coordinates.add(next_coord);
		}
		
		if (coordinates.size() > 1) {
			coordinates = new_coordinates;
		}
		
	}
	
	
	public static void main(String[] args) {
		
		Stroke s = new Stroke();
		for (int i=0; i < 20; i++) {
			s.addCoordinate(i, i*5);
		}
		s.terminate();
		
		System.out.println(s.metaToString());
		System.out.println(s.coordinates.size());
		
	}
	
	
}
