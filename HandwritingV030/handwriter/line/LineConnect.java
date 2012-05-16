package line;

import java.util.LinkedList;

import java.util.Iterator;
import recognition.strokes.Coordinate;

/*
 * Connects two disjointed points with a line, 
 * 
 * smooths out characters when stylus is dragged more quickly than swing can detect.
 * 
 * 
 */
public class LineConnect {

	public static LinkedList<Coordinate> connect(int x1, int y1, int x2, int y2) {
		double slope = getSlope(x1, y1, x2, y2);
		double invSlope = 1/slope;
		LinkedList<Coordinate> coordinates1 = new LinkedList<Coordinate>();
		LinkedList<Coordinate> coordinates2 = new LinkedList<Coordinate>();
		//System.out.println(x1 + "," + y1 + " to " + x2 + "," +y2 + "; slope = " + slope);
		if (Math.abs(slope) != Double.POSITIVE_INFINITY) {
			coordinates1 = iterate(x1, y1, y1, slope, x2, coordinates1, false); // iterate x.
		}
		if (Math.abs(invSlope) != Double.POSITIVE_INFINITY) {
			coordinates1.addAll(iterate(y1, x1, x1, invSlope, y2, coordinates2, true)); // iterate y.
		}
		return coordinates1;
	}
	
	/*
	 * xNow   -- the x coordinate iterator
	 * yNow   -- the real number value of y during the iterations.
	 * yStart -- tracks the integer coordinate boundaries that yNow lies within.
	 * 
	 */
	public static LinkedList<Coordinate> iterate(int xNow, double yNow, int yStart, double slope, int lastX, LinkedList<Coordinate> coordinates, boolean iterateY) {
		if (xNow == lastX) { // end.
			return coordinates;
		} else if (lastX - xNow > 0){ // move right.
			xNow++;
			yNow = yNow + slope;
		} else { // move left
			xNow--;
			yNow = yNow - slope;
		}
			
		if (Math.abs(yNow - yStart) < 1) { // didn't change vertical coordinate
			if (iterateY) {
				coordinates.add(new Coordinate(yStart, xNow));

			} else {
				coordinates.add(new Coordinate(xNow, yStart));
			}
			return iterate(xNow, yNow, yStart, slope, lastX, coordinates, iterateY);	
		} else if (yNow - yStart >= 1) { // increased vertical coordinate
				yStart = yStart + (int)Math.floor(yNow-yStart);
				if (Math.abs(yNow - yStart) == 0 && iterateY) {
					return iterate(xNow, yNow, yStart, slope, lastX, coordinates, iterateY);
				}
				if (iterateY) {
					coordinates.add(new Coordinate(yStart, xNow));	

				} else {
					coordinates.add(new Coordinate(xNow, yStart));	
				}
				return iterate(xNow, yNow, yStart, slope, lastX, coordinates, iterateY);
					
		} else { // decreased vertical coordinate
				yStart = yStart + (int) Math.nextUp(yNow - yStart);
				if (Math.abs(yNow - yStart) == 0 && iterateY) {
					return iterate(xNow, yNow, yStart, slope, lastX, coordinates, iterateY);
				}
				if (iterateY) {
					coordinates.add(new Coordinate(yStart, xNow));		
				} else {
					coordinates.add(new Coordinate(xNow, yStart));		
				}
				return iterate(xNow, yNow, yStart, slope, lastX, coordinates, iterateY);
		}
	}
	

	
	
	
	
    public static double getSlope(double x1, double y1, double x2, double y2) {
    	return (y2-y1)/(x2-x1);
    }
    
    
    public static void main(String[] args) {
    	Iterator<Coordinate> i = connect(500, 138, 507, 152).iterator();
    	while (i.hasNext()) {
    		System.out.println(i.next().toString());
    	}
    }
    
    
}
