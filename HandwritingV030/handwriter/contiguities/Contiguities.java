package contiguities;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;


public class Contiguities {
	
	public static LinkedList<StrokeList> get_entities(StrokeList all_strokes) {
		
		Iterator<Stroke> stroke_iterator = all_strokes.getStrokes().iterator();
		
		int length =0;
		int counter =0;
		while (stroke_iterator.hasNext()) {
			Stroke this_stroke = stroke_iterator.next();
			length += this_stroke.getStrokeHeight();
			length += this_stroke.getStrokeWidth();
			counter += 2;
		}
		int tolerance = length / (counter*4);
		
		System.out.println("tolerance: " + tolerance);
		
		LinkedList<StrokeList> entities = new LinkedList<StrokeList>();
		
		Queue<Stroke> unmarked_strokes = all_strokes.getStrokes();
		int entity_counter = 0;
		
		while (!unmarked_strokes.isEmpty()) {
			LinkedList<Stroke> this_entity = new LinkedList<Stroke>();
			this_entity.add(unmarked_strokes.poll()); // start new entity.
			
			stroke_connect(this_entity, unmarked_strokes, 0, tolerance); // try to connect other strokes.
			entities.add(new StrokeList(this_entity, entity_counter));
			entity_counter++;
		}
		
		
		
		return entities;
	}
	
	private static boolean possible_contiguity(int max_x1, int min_x1, int max_y1, int min_y1, int max_x2, int min_x2, int max_y2, int min_y2, int tolerance) {
		boolean y_possible = false;
		boolean x_possible = false;
				
		if (min_y1 - tolerance < min_y2 
				&& min_y2 < max_y1 + tolerance) {
			y_possible = true;

		} else if (min_y1 - tolerance < max_y2 
				&& max_y2 < max_y1 + tolerance) {
			y_possible = true;

		} else if (min_y2 < min_y1
				&& max_y2 > max_y1) {
			y_possible = true;

		}
		
		if (min_x1 - tolerance < min_x2
				&& min_x2 < max_x1 + tolerance) {
			x_possible = true;

		} else if (min_x1 - tolerance < max_x2 
				&& max_x2 < max_x1 + tolerance) {
			x_possible = true;

		} else if (min_x2 < min_x1
				&& max_x2 > max_x1) {
			x_possible = true;

		}
		
		if (x_possible && y_possible) {
			return true;
		} else {
			return false;
		}
	}
	
	private static void stroke_connect(LinkedList<Stroke> this_entity, Queue<Stroke> unmarked_strokes, int entity_index, int tolerance) {		
		Stroke stroke_to_match = this_entity.get(entity_index);
		
		Stroke this_stroke;
		
		LinkedList<Integer> added_stroke_ids =  new LinkedList<Integer>();
		
		int counter =0;
		int end = unmarked_strokes.size();
		while (counter < end) {
			this_stroke = unmarked_strokes.poll();
			if (proximitize(stroke_to_match, this_stroke, tolerance)) {
				this_entity.add(this_stroke);
				added_stroke_ids.add(counter);
			} else {
				unmarked_strokes.add(this_stroke);
			}
			counter++;

		}
		
		while (!added_stroke_ids.isEmpty()) {
			unmarked_strokes.remove(added_stroke_ids.poll());
		}
		if (this_entity.size() > entity_index+1) {  // at least 1 stroke was added
			stroke_connect(this_entity, unmarked_strokes, entity_index+1, tolerance);
		} 

	}
	
	private static boolean proximitize(Stroke s1, Stroke s2, int tolerance) {
				
		if (possible_contiguity(s1.getMaxX(), s1.getMinX(), s1.getMaxY(), s1.getMinY(), s2.getMaxX(), s2.getMinX(), s2.getMaxY(), s2.getMinY(), tolerance)) {
			Iterator<Coordinate> s1_coords_iterator = s1.getCoordinates().iterator();
			Iterator<Coordinate> s2_coords_iterator;
			Coordinate this_s1_coord;
			Coordinate this_s2_coord;
			int this_s1_x;
			int this_s2_x;
			int this_s1_y;
			int this_s2_y;
			

			
			while (s1_coords_iterator.hasNext()) {
				this_s1_coord = s1_coords_iterator.next();
				this_s1_x = this_s1_coord.getX();
				this_s1_y = this_s1_coord.getY();
				
				s2_coords_iterator = s2.getCoordinates().iterator();
				while (s2_coords_iterator.hasNext()) {
					this_s2_coord = s2_coords_iterator.next();
					this_s2_x = this_s2_coord.getX();
					this_s2_y = this_s2_coord.getY();

					if ((Math.abs(this_s2_x - this_s1_x) < tolerance) &&  (Math.abs(this_s2_y - this_s1_y) < tolerance)) {
						return true;
					}
				}
			}			
		} 
		return false;
	}
	
	
	
	public static void main(String[] args) {
		Stroke s1 = new Stroke();
		s1.add(new Coordinate(0, 0));
		for (int i=1; i < 15; i++) {
			s1.add(new Coordinate(i, i));
		}
		Stroke s2 = new Stroke();
		
		for (int i=50; i < 60; i++) {
			s2.add(new Coordinate(63, i));
		}
		
		System.out.println(proximitize(s1, s2, 50));
		/*
		Stroke s3 = new Stroke();
		s3.add(new Coordinate(500, 500));
		LinkedList<Stroke> l1 = new LinkedList<Stroke>();
		l1.add(s2);
		l1.add(s3);
		
		LinkedList<Stroke> le = new LinkedList<Stroke>();
		le.add(s1);
		stroke_connect(le, l1, 0);
		
		for (int i=0; i < le.size(); i++) {
			System.out.println(i);
		}
		*/
	}
	
}
