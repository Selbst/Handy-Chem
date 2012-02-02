package structural_synthesis;

import java.util.LinkedList;

import recognition.strokes.StrokeList;

public class AtomSkeleton {

	LinkedList<StrokeList> entities_in_atom;
	public int minX, minY, maxX, maxY, height, width, key; 

	public AtomSkeleton() {
		entities_in_atom = new LinkedList<StrokeList>();
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;
	}
	public void add(StrokeList entity) {
		entities_in_atom.add(entity);
		if (entity.maxX > maxX) {
			maxX = entity.maxX;
		}
		if (entity.maxY > maxY) {
			maxY = entity.maxY;
		}
		if (entity.minX < minX) {
			minX = entity.minX;
		}
		if (entity.minY < minY) {
			minY = entity.minY;
		}
		
	}
	
	public String toString() {
		return "key: " + key;
	}
	
	
}
