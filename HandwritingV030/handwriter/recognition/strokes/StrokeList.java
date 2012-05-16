package recognition.strokes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import pixels.binary.Entity;

/*
 * Stores the list of all strokes made (in order).
 * 
 * TODO: map strokes to entities.
 */
public class StrokeList {
	private LinkedList<Stroke> strokes; public LinkedList<Stroke> getStrokes() {return strokes;}
	public int minX, minY, maxX, maxY, height, width; 
	
	boolean marked = false;
	/*
	 * id = -3: dot
	 * id = -2: bond
	 */
	public int id;
	public int entity_id;
	
	
	public StrokeList() {
		strokes = new LinkedList<Stroke>();
	}
	public StrokeList(Stroke stroke) {
		strokes = new LinkedList<Stroke>();
		strokes.add(stroke);
	}
	public StrokeList(LinkedList<Stroke> strokes) {
		this.strokes = strokes;
	}
	public StrokeList(LinkedList<Stroke> strokes, int id) {
		this.strokes = strokes;
		this.id = id;
	}
	public void addStroke(Stroke stroke) {
		strokes.add(stroke);
	}
	

	public void assignEntities(LinkedList<Entity> entities) {
		Iterator<Stroke> strokeIterator = strokes.iterator(); Stroke thisStroke;
		Iterator<Entity> entityIterator; Entity thisEntity;
		while (strokeIterator.hasNext()) {
			thisStroke = strokeIterator.next();
			entityIterator = entities.iterator();
			
			while (entityIterator.hasNext()) {
				thisEntity = entityIterator.next();
				if (thisStroke.getMinY() >= thisEntity.minRow && thisStroke.getMaxY() <= thisEntity.maxRow && 
						thisStroke.getMinX() >= thisEntity.minColumn && thisStroke.getMaxX() <= thisEntity.maxColumn) {
					// TODO check coordinates.
					thisStroke.entity = thisEntity;
				} // clearly not part of the entity.
			}
		}
	}
	public void find_dot_dimensions() {
		Stroke s = strokes.getFirst();
		LinkedList<Coordinate> cs = s.getCoordinates();
		Coordinate c = cs.getFirst();
		minX = c.getX(); maxX = c.getX();
		minY = c.getY();
		minY = c.getY();
		
	}
	public void setCharacterRelativeCoordinates() {
		Iterator<Stroke> strokeIterator = strokes.iterator(); Stroke thisStroke;
		if (strokeIterator.hasNext()) {
			thisStroke = strokeIterator.next();
			minX = thisStroke.getMinX(); minY = thisStroke.getMinY(); maxX = thisStroke.getMaxX(); maxY = thisStroke.getMaxY();
		}
		while (strokeIterator.hasNext()) {
			thisStroke = strokeIterator.next();
			if (thisStroke.getMinX() < minX) {
				minX = thisStroke.getMinX();
			}
			if (thisStroke.getMinY() < minY) {
				minY = thisStroke.getMinY();
			}
			if (thisStroke.getMaxX() > maxX) {
				maxX = thisStroke.getMaxX();
			}
			if (thisStroke.getMaxY() > maxY) {
				maxY = thisStroke.getMaxY();
			}
		}
		height = maxY - minY;
		width = maxX - minX;
		
		strokeIterator = strokes.iterator(); Iterator<Coordinate> coordinateIterator; Coordinate thisCoordinate;
		LinkedList<Coordinate> charRelativeCoordinates;
		while (strokeIterator.hasNext()) {
			thisStroke = strokeIterator.next();
			coordinateIterator = thisStroke.getCoordinates().iterator();
			charRelativeCoordinates = new LinkedList<Coordinate>();
			while (coordinateIterator.hasNext()) {
				thisCoordinate = coordinateIterator.next();
				charRelativeCoordinates.add(new Coordinate(thisCoordinate.getX() - minX, thisCoordinate.getY() - minY));
			}
			thisStroke.setCharacterRelativeCoordinate(charRelativeCoordinates);
		}
		
		
	}
	
	public static void to_file(StrokeList character, String output_file) {
		
		try {  // add training character
			
			//fstream = new FileWriter("trainingcharacters/choose_from_all/" + file.getText(), true);
			
			FileWriter fstream = new FileWriter(output_file, true);

			BufferedWriter out = new BufferedWriter(fstream);
			out.write("char\n");								//          	"char"
			out.write(character.id + "\n"); // 				<id#>
			out.write(character.getStrokes().size()+ "\n");  // 		<# of strokes>
			
			// write strokes to file.
			Iterator<Stroke> strokeIterator = character.getStrokes().iterator(); Iterator<Coordinate> coordinateIterator;
			Stroke thisStroke; Coordinate thisCoordinate;
			while (strokeIterator.hasNext()) {
				thisStroke = strokeIterator.next();
				out.write(thisStroke.getMinX() + "\n");                 //		<min x of stroke>
				out.write(thisStroke.getMinY()  + "\n");				//		<min y of stroke>
				out.write(thisStroke.getMaxX()  + "\n");				// 		<max x of stroke>
				out.write(thisStroke.getMaxY()  + "\n");				// 		<max y of stroke>
				out.write(thisStroke.getCoordinates().size() + "\n"); //		<stroke i # of coordinates>
				out.write(thisStroke.metaCoordinates.size() + "\n");   	//		<stroke i # of meta coords>
				coordinateIterator = thisStroke.getCoordinates().iterator();
				while (coordinateIterator.hasNext()) {
					thisCoordinate = coordinateIterator.next();
					out.write(thisCoordinate.getX() + "\n");			//		<x coord>
					out.write(thisCoordinate.getY() + "\n");			//      <y coord>
					out.write(thisCoordinate.getT() + "\n");			// 		<time coord>
				}

				coordinateIterator = thisStroke.metaCoordinates.iterator();
				while (coordinateIterator.hasNext()) {
					thisCoordinate = coordinateIterator.next();
					String a = "areas : ";

					double[] areas = thisCoordinate.getAreas();
					a += areas[0] +", ";
					a += areas[1] + ", ";
					a += areas[2] + ", ";
					a += areas[3];
					System.out.println(areas);
					out.write(thisCoordinate.getXAvg() + "\n");			//		<x average of meta coord>
					out.write(thisCoordinate.getYAvg() + "\n");			// 		<y average of meta coord>
					out.write(areas[0] + "\n");							//		
					out.write(areas[1] + "\n");
					out.write(areas[2] + "\n");
					out.write(areas[3] + "\n");

				}
			}					
			
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public String toString() {
		String returnString = ""; boolean returnNullString = false;
		Iterator<Stroke> strokeIterator = null; Stroke strokePointer = null;
		if (strokes != null) {
			strokeIterator = strokes.iterator();
		} else {returnNullString=true;}
		
		if (!returnNullString) {
			int strokeNum = 0;
			while (strokeIterator.hasNext()) {
				strokePointer = strokeIterator.next();
				returnString += "Stroke " + strokeNum + ": ";
				returnString += "\n" + strokePointer.toString();
				returnString += "\n" + strokePointer.metaToString();
				strokeNum++;
			}
		}
		return returnString;
	}
	public StrokeList copy() {
		StrokeList s2 = new StrokeList();
		for (Stroke s : strokes) {
			s2.addStroke(s);
		}		
		s2.entity_id = entity_id;
		s2.minX = minX;
		s2.minY = minY;
		s2.maxX = maxX;
		s2.maxY = maxY;
		s2.height = height;
		s2.width = width;
		s2.id = id;
		s2.marked = marked;
		
		return s2;
		
	}
}
