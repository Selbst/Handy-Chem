package recognition.features;

import java.util.Iterator;

import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;

public class Barycenter {

	public static double[] get_mass_centers(StrokeList character) {
		
		double x_sum = 0;
		double y_sum = 0;
		double coord_counter = 0;
		double centers[] = new double[4];
		
		int[] coords = new int[2];
		int equator = (character.minY + character.maxY)/2;
		int prime_meridian = (character.minX + character.maxX)/2;
		//System.out.println("prime meridian: " + prime_meridian);
		
		Iterator<Stroke> strokes_iterator = character.getStrokes().iterator();
		
		Coordinate this_coordinate;
		Iterator<Coordinate> coordinate_iterator;
		
		while (strokes_iterator.hasNext()) {
			
			coordinate_iterator = strokes_iterator.next().getCoordinates().iterator();
			while (coordinate_iterator.hasNext()) {
				this_coordinate = coordinate_iterator.next();
				x_sum += this_coordinate.getX() - character.minX;
				y_sum += this_coordinate.getY() - character.minY;
				if (this_coordinate.getY() > equator) {
					coords[0]++; // up
				} else {
					coords[0]--; // down
				}
				if (this_coordinate.getX() > prime_meridian) {
					coords[1]++; // right
					
				} else {
					coords[1]--; // left
				}
				coord_counter++;
			}
		}
		centers[0] = x_sum / coord_counter;
		centers[1] = y_sum / coord_counter;
		centers[2] = coords[0];
		centers[3] = coords[1];
		
		return centers;
		
	}
}
