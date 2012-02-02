package structural_synthesis;

import java.util.Iterator;
import java.util.LinkedList;

import line.Line;

import recognition.strokes.StrokeList;

public class Bond_Connect {

	
	public static StrokeList[] connect_to_atoms(StrokeList bond, LinkedList<StrokeList> entities, Line bond_line, double avg_length) {
		
		int step_size;
		
		double slope = bond_line.getSlope();
		double y_intercept = bond_line.getYIntercept();
		
		Iterator<StrokeList> entity_iterator;
		StrokeList[] atoms = new StrokeList[2];
		
		if (bond.height > bond.width) { // travel alone y
			step_size = bond.height/10;
			double x;			
			StrokeList this_entity;
			//extend down
			int y = bond.maxY;
			
			outerloop:
			for (int i=0; i < 60; i++) {
				y += step_size;
				
				x = (y - y_intercept) / slope;
				
				entity_iterator = entities.iterator();
				while (entity_iterator.hasNext()) {
					this_entity = entity_iterator.next();
					
					if (point_inside_rectangle(this_entity, x, y)) {
						atoms[0] = this_entity;
						//System.out.println("found 1st");

						break outerloop;
					}
				}
			}
			
			// extend up
			y = bond.minY;
			outerloop:
			for (int i=0; i < 60; i++) {
				y -= step_size;
				x = (y - y_intercept) / slope;
				entity_iterator = entities.iterator();
				while (entity_iterator.hasNext()) {
					this_entity = entity_iterator.next();
					if (point_inside_rectangle(this_entity, x, y)) {
						atoms[1] = this_entity;
						//System.out.println("found 2nd");

						break outerloop;
					}
				}
			}
			
			
		} else { // travel along x
			//System.out.println("travelling along x");
			
			step_size = bond.width/10;
			double y;			
			StrokeList this_entity;
			
			
			//extend left
			int x = bond.minX;
			outerloop:
			for (int i=0; i < 60; i++) {
				
				x -= step_size;
				
				y = x*slope + y_intercept;
				
				//System.out.println("(" + x + ", " + y + ")");
				
				entity_iterator = entities.iterator();
				while (entity_iterator.hasNext()) {
					this_entity = entity_iterator.next();
					
					if (point_inside_rectangle(this_entity, x, y)) {
						atoms[0] = this_entity;
						//System.out.println("found 1st");

						break outerloop;
					}
				}
			}
			
			//extend right
			x = bond.maxX;
			outerloop:
			for (int i=0; i < 60; i++) {
				x += step_size;
				
				y = x*slope + y_intercept;
				//System.out.println("(" + x + ", " + y + ")");

				entity_iterator = entities.iterator();
				while (entity_iterator.hasNext()) {
					this_entity = entity_iterator.next();
					
					if (point_inside_rectangle(this_entity, x, y)) {
						atoms[1] = this_entity;
						//System.out.println("found 2nd");
						break outerloop;
					}
				}
			}
			
			
			
			
		}
		
		
		
		
		
		return atoms;
	}
	public static AtomSkeleton[] connect(StrokeList bond, LinkedList<AtomSkeleton> entities, Line bond_line) {
		
		int step_size;
		
		double slope = bond_line.getSlope();
		double y_intercept = bond_line.getYIntercept();
		
		Iterator<AtomSkeleton> entity_iterator;
		AtomSkeleton[] atoms = new AtomSkeleton[2];
		
		if (bond.height > bond.width) { // travel along y
			step_size = bond.height/10;
			double x;			
			AtomSkeleton this_entity;
			//extend down
			int y = bond.maxY;
			
			outerloop:
			for (int i=0; i < 60; i++) {
				y += step_size;
				
				x = (y - y_intercept) / slope;
				
				entity_iterator = entities.iterator();
				while (entity_iterator.hasNext()) {
					this_entity = entity_iterator.next();
					
					if (point_inside_rectangle(this_entity, x, y)) {
						atoms[0] = this_entity;
						//System.out.println("found 1st");

						break outerloop;
					}
				}
			}
			
			// extend up
			y = bond.minY;
			outerloop:
			for (int i=0; i < 60; i++) {
				y -= step_size;
				x = (y - y_intercept) / slope;
				entity_iterator = entities.iterator();
				while (entity_iterator.hasNext()) {
					this_entity = entity_iterator.next();
					if (point_inside_rectangle(this_entity, x, y)) {
						atoms[1] = this_entity;
						//System.out.println("found 2nd");

						break outerloop;
					}
				}
			}
			
			
		} else { // travel along x
			//System.out.println("travelling along x");
			
			step_size = bond.width/10;
			double y;			
			AtomSkeleton this_entity;
			
			
			//extend left
			int x = bond.minX;
			outerloop:
			for (int i=0; i < 60; i++) {
				
				x -= step_size;
				
				y = x*slope + y_intercept;
				
				//System.out.println("(" + x + ", " + y + ")");
				
				entity_iterator = entities.iterator();
				while (entity_iterator.hasNext()) {
					this_entity = entity_iterator.next();
					//System.out.println(this_entity.minX + " - " + this_entity.maxX + ", " + this_entity.minY + " - " + this_entity.maxY);
					if (point_inside_rectangle(this_entity, x, y)) {
						atoms[0] = this_entity;
						//System.out.println("found 1st");

						break outerloop;
					}
				}
			}
			
			//extend right
			x = bond.maxX;
			outerloop:
			for (int i=0; i < 60; i++) {
				x += step_size;
				
				y = x*slope + y_intercept;
				//System.out.println("(" + x + ", " + y + ")");

				entity_iterator = entities.iterator();
				while (entity_iterator.hasNext()) {
					this_entity = entity_iterator.next();
					//System.out.println(this_entity.minX + " - " + this_entity.maxX + ", " + this_entity.minY + " - " + this_entity.maxY);

					if (point_inside_rectangle(this_entity, x, y)) {
						atoms[1] = this_entity;
						//System.out.println("found 2nd");
						break outerloop;
					}
				}
			}
			
			
			
			
		}
		
		
		
		
		return atoms;
	}
	
	public static boolean point_inside_rectangle(StrokeList rectangle, double x, int y) {
		
		if (x < rectangle.maxX && x > rectangle.minX && y < rectangle.maxY && y > rectangle.minY) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean point_inside_rectangle(StrokeList rectangle, int x, double y) {
		
		if (x < rectangle.maxX && x > rectangle.minX && y < rectangle.maxY && y > rectangle.minY) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean point_inside_rectangle(AtomSkeleton rectangle, double x, int y) {
		
		if (x < rectangle.maxX && x > rectangle.minX && y < rectangle.maxY && y > rectangle.minY) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean point_inside_rectangle(AtomSkeleton rectangle, int x, double y) {
		
		if (x < rectangle.maxX && x > rectangle.minX && y < rectangle.maxY && y > rectangle.minY) {
			return true;
		} else {
			return false;
		}
	}
	
}
