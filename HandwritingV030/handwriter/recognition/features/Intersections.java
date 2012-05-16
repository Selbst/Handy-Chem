package recognition.features;

import java.util.Iterator;
import java.util.LinkedList;

import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;

public class Intersections {
	public final int[][] expected_intersections_lowercase = new int[25][2]; { // -1 denotes unknown. 
	
		// 0  = equator
		// 1  = prime meridian
		expected_intersections_lowercase[0][0] = -1;
		expected_intersections_lowercase[0][1] = -1;
	
		expected_intersections_lowercase[1][0] = -1;
		expected_intersections_lowercase[1][1] = 2;
		
		expected_intersections_lowercase[2][0] = 1;
		expected_intersections_lowercase[2][1] = 2;
	
		expected_intersections_lowercase[3][0] = -1;
		expected_intersections_lowercase[3][1] = 2;
		
		expected_intersections_lowercase[4][0] = 1;
		expected_intersections_lowercase[4][1] = 3;
		
		expected_intersections_lowercase[5][0] = -1;
		expected_intersections_lowercase[5][1] = -1;
		
		expected_intersections_lowercase[6][0] = -1;
		expected_intersections_lowercase[6][1] = -1;
		
		expected_intersections_lowercase[7][0] = -1;
		expected_intersections_lowercase[7][1] = 1;
		
		expected_intersections_lowercase[8][0] = -1;
		expected_intersections_lowercase[8][1] = -1;
		
		expected_intersections_lowercase[9][0] = 1;
		expected_intersections_lowercase[9][1] = -1;
		
		expected_intersections_lowercase[10][0] = -1;
		expected_intersections_lowercase[10][1] = 2;
		
		expected_intersections_lowercase[11][0] = -1;
		expected_intersections_lowercase[11][1] = -1;
		
		expected_intersections_lowercase[12][0] = -1;
		expected_intersections_lowercase[12][1] = 1;
		
		expected_intersections_lowercase[13][0] = 2;
		expected_intersections_lowercase[13][1] = 2;
		
		expected_intersections_lowercase[14][0] = -1;
		expected_intersections_lowercase[14][1] = 2;
		
		expected_intersections_lowercase[15][0] = -1;
		expected_intersections_lowercase[15][1] = 2;
		
		expected_intersections_lowercase[16][0] = 1;
		expected_intersections_lowercase[16][1] = 1;
		
		expected_intersections_lowercase[17][0] = 1;
		expected_intersections_lowercase[17][1] = 3;
		
		expected_intersections_lowercase[18][0] = -1;
		expected_intersections_lowercase[18][1] = -1;
		
		expected_intersections_lowercase[19][0] = 2;
		expected_intersections_lowercase[19][1] = 1;
		
		expected_intersections_lowercase[20][0] = 2;
		expected_intersections_lowercase[20][1] = 1;
		
		expected_intersections_lowercase[21][0] = -1;
		expected_intersections_lowercase[21][1] = -1;
		
		expected_intersections_lowercase[22][0] = -1;
		expected_intersections_lowercase[22][1] = -1;
		
		
		expected_intersections_lowercase[23][0] = -1;
		expected_intersections_lowercase[23][1] = -1;
		
		expected_intersections_lowercase[24][0] = -1;
		expected_intersections_lowercase[24][1] = -1;
		
	}
	public final int[][] expected_intersections_uppercase = new int[26][2]; { // -1 denotes unknown. 
		
		// 0  = equator
		// 1  = prime meridian
		expected_intersections_uppercase[0][0] = -1;
		expected_intersections_uppercase[0][1] = 2;
	
		expected_intersections_uppercase[1][0] = 2;
		expected_intersections_uppercase[1][1] = 3;
		
		expected_intersections_uppercase[2][0] = 1;
		expected_intersections_uppercase[2][1] = 2;
	
		expected_intersections_uppercase[3][0] = 2;
		expected_intersections_uppercase[3][1] = 2;
		
		expected_intersections_uppercase[4][0] = -1;
		expected_intersections_uppercase[4][1] = -1;
		
		expected_intersections_uppercase[5][0] = -1;
		expected_intersections_uppercase[5][1] = -1;
		
		expected_intersections_uppercase[6][0] = -1;
		expected_intersections_uppercase[6][1] = -1;
		
		expected_intersections_uppercase[7][0] = -1;
		expected_intersections_uppercase[7][1] = 1;
		
		expected_intersections_uppercase[8][0] = 1;
		expected_intersections_uppercase[8][1] = -1;
		
		expected_intersections_uppercase[9][0] = 1;
		expected_intersections_uppercase[9][1] = -1;
		
		expected_intersections_uppercase[10][0] = -1;
		expected_intersections_uppercase[10][1] = 2;
		
		expected_intersections_uppercase[11][0] = 1;
		expected_intersections_uppercase[11][1] = 1;
		
		expected_intersections_uppercase[12][0] = -1;
		expected_intersections_uppercase[12][1] = 1;
		
		expected_intersections_uppercase[13][0] = 3;
		expected_intersections_uppercase[13][1] = 1;
		
		expected_intersections_uppercase[14][0] = 2;
		expected_intersections_uppercase[14][1] = 2;
		
		expected_intersections_uppercase[15][0] = -1;
		expected_intersections_uppercase[15][1] = 2;
		
		expected_intersections_uppercase[16][0] = 2;
		expected_intersections_uppercase[16][1] = -1;
		
		expected_intersections_uppercase[17][0] = 2;
		expected_intersections_uppercase[17][1] = -1;
		
		expected_intersections_uppercase[18][0] = 1;
		expected_intersections_uppercase[18][1] = 3;
		
		expected_intersections_uppercase[19][0] = 1;
		expected_intersections_uppercase[19][1] = -1;
		
		expected_intersections_uppercase[20][0] = -1;
		expected_intersections_uppercase[20][1] = 1;
		
		expected_intersections_uppercase[21][0] = 2;
		expected_intersections_uppercase[21][1] = 1;
		
		expected_intersections_uppercase[22][0] = -1;
		expected_intersections_uppercase[22][1] = 1;
		
		
		expected_intersections_uppercase[23][0] = -1;
		expected_intersections_uppercase[23][1] = -1;
		
		expected_intersections_uppercase[24][0] = -1;
		expected_intersections_uppercase[24][1] = -1;
		
		expected_intersections_uppercase[25][0] = 1;
		expected_intersections_uppercase[25][1] = 3;
		
	}
	public int[][] get_expected_lowercase() {
		return expected_intersections_lowercase;
	}
	public int[][] get_expected_uppercase() {
		return expected_intersections_uppercase;
	}
		
	
	
	public static int[] get_intersection(StrokeList character) {
		double prime_meridian = (character.minX + character.maxX) /  2.0;
		double equator = (character.minY + character.maxY) /  2.0;
		LinkedList<Integer> meridian_crosses = new LinkedList<Integer>(); // y coordinates of points in character that fall on prime_meridian.
		LinkedList<Integer> equator_crosses = new LinkedList<Integer>(); // x coordinates of points in character that fall on equator. 
		
		
		int threshold_x = character.width/5;
		int threshold_y = character.height/8;
		
		Iterator<Stroke> stroke_iterator = character.getStrokes().iterator();
		Iterator<Coordinate> coord_iterator;
		Coordinate this_coord;
		while (stroke_iterator.hasNext()) {
			coord_iterator = stroke_iterator.next().getCoordinates().iterator();
			while (coord_iterator.hasNext()) {
				this_coord = coord_iterator.next();
				
				
				if (this_coord.getX() < prime_meridian +1 && this_coord.getX() > prime_meridian -1) { // crosses prime meridian.
					
					Iterator<Integer> prev_merid_crosses_iterator = meridian_crosses.iterator();

					boolean proximity_prev_merid = false;

					while (prev_merid_crosses_iterator.hasNext()) {
						int prev_y = prev_merid_crosses_iterator.next();
						if (this_coord.getY() < prev_y + threshold_y && this_coord.getY() > prev_y - threshold_y) {
							proximity_prev_merid = true;
							break;
						}
					}
					
					if (!proximity_prev_merid) meridian_crosses.add(this_coord.getY());
					
				}
				
				if (this_coord.getY() < equator + 1 && this_coord.getY() > equator -1) {
					Iterator<Integer> prev_equator_crosses_iterator = equator_crosses.iterator();
					boolean proximity_prev_equator = false;

					while (prev_equator_crosses_iterator.hasNext()) {
						int prev_x = prev_equator_crosses_iterator.next();
						
						
						if (this_coord.getX() < prev_x + threshold_x && this_coord.getX() > prev_x - threshold_x) {
							proximity_prev_equator = true;
							break;
						}
					}
					if (!proximity_prev_equator) equator_crosses.add(this_coord.getX());
				}

			}
		}
		
		int[] intersections = new int[2];
		intersections[0] = meridian_crosses.size();
		//System.out.println("meridian: " + meridian_crosses.size());
		intersections[1] = equator_crosses.size();
		
		
		return intersections;
		
	}
}
