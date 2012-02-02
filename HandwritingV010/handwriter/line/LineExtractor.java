package line;
import java.util.Iterator;
import java.util.LinkedList;



import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;

public class LineExtractor {

	public Line Extract(Stroke stroke) {
		LinkedList<Coordinate> relativeCoordinates = stroke.getRelativeCoordinates();
		int strokeWidth = stroke.getStrokeWidth(); int strokeHeight = stroke.getStrokeWidth();
		relativeCoordinates = removeDuplicateCoordinates(relativeCoordinates);
		return null;
	}
	public static LinkedList<Coordinate> removeDuplicateCoordinates(LinkedList<Coordinate> coordinates) {
		Iterator<Coordinate> coordinateIterator = coordinates.iterator();
		LinkedList<Coordinate> newCoordinates = new LinkedList<Coordinate>();
		Iterator<Coordinate> newCoordinatesIterator = newCoordinates.iterator();
		int oldx; int oldy; int newx; int newy;
		Coordinate oldCoordinate; Coordinate newCoordinate; boolean isDuplicate = false;
		while (coordinateIterator.hasNext()) {
			oldCoordinate = coordinateIterator.next(); oldx = oldCoordinate.getX(); oldy = oldCoordinate.getY();
			while (newCoordinatesIterator.hasNext()) {
				newCoordinate = newCoordinatesIterator.next(); newx = newCoordinate.getX(); newy = newCoordinate.getY();
				if (newx == oldx && newy == oldy && newx != -1 && oldx != -1) {isDuplicate=true;}
			}
			if (!isDuplicate) {newCoordinates.add(oldCoordinate);} isDuplicate=false;
			newCoordinatesIterator = newCoordinates.iterator();
		}
		return newCoordinates;
	}
}
