package chemistry;

import java.awt.Font;
import java.awt.Graphics2D;

import auto_grader.Print_Area;

public class Bond {
	Atom next;
	int type;
	double slope;
	int angle;
	
	/*
	 * Clockwise, improving precision after every 12 hours or 2pi or 360 degrees 
	 * 
	 * 0 - N
	 * 1 - E
	 * 2 - S
	 * 3 - W
	 * 4 - NE
	 * 5 - SE
	 * 6 - SW
	 * 7 - NW
	 * 
	 * 
	 */
	int direction; 
	
	public void set_direction(Atom prev) {
		/*
		 * -1 =  south, 1 = north, or 0 = even
		 */
		int vertical = -10;
			if (prev.get_skeleton().maxY < next.get_skeleton().minY) {
				vertical = -1;
			} else if (next.get_skeleton().maxY < prev.get_skeleton().minY) {
				vertical = 1;
			} else {
				vertical = 0;
			}

		
		
		
		/*
		 * -1 = west, 1 = east, or 0 = even
		 */
		int horizontal = -10;
			if (prev.get_skeleton().maxX < next.get_skeleton().minX) {
				horizontal = 1;
			} else if (next.get_skeleton().maxX < prev.get_skeleton().minX) {
				horizontal = -1;
			} else {
				horizontal = 0;
			}
		
		if (horizontal == 0 && vertical == 1) {
			direction = 0;
		} else if (horizontal == 1 && vertical == 0) {
			direction = 1;
		} else if (horizontal == 0 & vertical == -1) {
			direction = 2;
		} else if (horizontal == -1 && vertical == 0) {
			direction = 3;
		} else if (horizontal == 1 && vertical == 1) {
			direction = 4;
		} else if (horizontal == 1 && vertical == -1) {
			direction = 5;
		} else if (horizontal == -1 && vertical == -1) {
			direction = 6;
		} else if (horizontal == -1 && vertical == 1) {
			direction = 7;
		} else {
			direction = -1;
		}
	}
	
	public Bond(Atom next, int type) {
		this.next = next; this.type = type;
	}
	public Bond(Atom next, int type, double slope) {
		this.next = next; this.type = type;
	}

	public void print(Font font, int bondLength, int atomLength,
			Graphics2D g2d, int center_of_prev_atom_x, int center_of_prev_atom_y, Print_Area print_area, int prev_atom_length) {

		int[] coords = new int[4];
		int[] coords2 = new int[4];
		int[] coords3 = new int[4];
		int x1=0;
		int x2=0;
		int y1=0;
		int y2=0;
		
		if (direction == 0) {
			y1 = (int) -(bondLength*1.3);
			y2 = (int) (-bondLength*2.2);
		} else if (direction == 1) {
			x1 = (int) (bondLength*1.3);
			x2 = (int) (bondLength*2.2);
		} else if (direction == 2) {
			y1 = (int) (bondLength*1.3);
			y2 = (int) (bondLength*2.2);
		} else if (direction == 3) {
			x1 = (int) (-bondLength*1.3);
			x2 = (int) (-bondLength*2.2);
		} else if (direction == 4) {
			x1 = (int) (bondLength*1.3);
			x2 = (int) (bondLength*2.2);
			y1 = (int) (-bondLength*1.3);
			y2 = (int) (-bondLength*2.2);
		} else if (direction == 5) {
			x1 = (int) (bondLength*1.3);
			x2 = (int) (bondLength*2.2);
			y1 = (int) (bondLength*1.3);
			y2 = (int) (bondLength*2.2);
		} else if (direction == 6) {
			x1 = (int) (-bondLength*1.3);
			x2 = (int) (-bondLength*2.2);
			y1 = (int) (bondLength*1.3);
			y2 = (int) (bondLength*2.2);
		} else if (direction == 7) {
			x1 = (int) (-bondLength*1.3);
			x2 = (int) (-bondLength*2.2);
			y1 = (int) (-bondLength*1.3);
			y2 = (int) (-bondLength*2.2);
		}
		
		if (type == 1) {
			coords[0] = center_of_prev_atom_x + x1;
			coords[1] = center_of_prev_atom_y + y1;
			coords[2] = center_of_prev_atom_x + x2;
			coords[3] = center_of_prev_atom_y + y2;
			//System.out.println("center x: " + center_of_prev_atom_x + ", center y: " + center_of_prev_atom_y);
			print_area.bonds.add(coords);

		}
		if (type == 2) {
			if (direction == 0 || direction == 2) {
				coords[0] = center_of_prev_atom_x + x1 + bondLength/4;
				coords[2] = center_of_prev_atom_x + x2 + bondLength/4;
				
				coords[1] = center_of_prev_atom_y + y1;
				coords[3] = center_of_prev_atom_y + y2;
				
				coords2[0] = center_of_prev_atom_x + x1 - bondLength/4;
				coords2[2] = center_of_prev_atom_x + x2 - bondLength/4;
				
				coords2[1] = center_of_prev_atom_y + y1;
				coords2[3] = center_of_prev_atom_y + y2;
			} else if (direction == 1 || direction == 3) {
				coords[1] = center_of_prev_atom_y + y1 + bondLength/4;
				coords[3] = center_of_prev_atom_y + y2 + bondLength/4;
				
				coords[0] = center_of_prev_atom_x + x1;
				coords[2] = center_of_prev_atom_x + x2;
				
				coords2[1] = center_of_prev_atom_y + y1 - bondLength/4;
				coords2[3] = center_of_prev_atom_y + y2 - bondLength/4;
				
				coords2[0] = center_of_prev_atom_x + x1;
				coords2[2] = center_of_prev_atom_x + x2;
			} else if (direction == 4 || direction == 6 || direction == 5 || direction == 7) {
				coords[0] = center_of_prev_atom_x + x1 + bondLength/4;
				coords[2] = center_of_prev_atom_x + x2 + bondLength/4;
				
				coords[1] = center_of_prev_atom_y + y1 + bondLength/4;
				coords[3] = center_of_prev_atom_y + y2 + bondLength/4;
				
				coords2[0] = center_of_prev_atom_x + x1 - bondLength/4;
				coords2[2] = center_of_prev_atom_x + x2 - bondLength/4;
				
				coords2[1] = center_of_prev_atom_y + y1 - bondLength/4;
				coords2[3] = center_of_prev_atom_y + y2 - bondLength/4;
			}
			
			print_area.bonds.add(coords);
			print_area.bonds.add(coords2);
		} else if (type == 3) {
			if (direction == 0 || direction == 2) {
				coords[0] = center_of_prev_atom_x + x1 + bondLength/3;
				coords[2] = center_of_prev_atom_x + x2 + bondLength/3;
				
				coords[1] = center_of_prev_atom_y + y1;
				coords[3] = center_of_prev_atom_y + y2;
				
				coords2[0] = center_of_prev_atom_x + x1 - bondLength/3;
				coords2[2] = center_of_prev_atom_x + x2 - bondLength/3;
				
				coords2[1] = center_of_prev_atom_y + y1;
				coords2[3] = center_of_prev_atom_y + y2;
			} else if (direction == 1 || direction == 3) {
				coords[1] = center_of_prev_atom_y + y1 + bondLength/3;
				coords[3] = center_of_prev_atom_y + y2 + bondLength/3;
				
				coords[0] = center_of_prev_atom_x + x1;
				coords[2] = center_of_prev_atom_x + x2;
				
				coords2[1] = center_of_prev_atom_y + y1 - bondLength/3;
				coords2[3] = center_of_prev_atom_y + y2 - bondLength/3;
				
				coords2[0] = center_of_prev_atom_x + x1;
				coords2[2] = center_of_prev_atom_x + x2;
			} else if (direction == 4 || direction == 6 || direction == 5 || direction == 7) {
				coords[0] = center_of_prev_atom_x + x1 + bondLength/3;
				coords[2] = center_of_prev_atom_x + x2 + bondLength/3;
				
				coords[1] = center_of_prev_atom_y + y1 + bondLength/3;
				coords[3] = center_of_prev_atom_y + y2 + bondLength/3;
				
				coords2[0] = center_of_prev_atom_x + x1 - bondLength/3;
				coords2[2] = center_of_prev_atom_x + x2 - bondLength/3;
				
				coords2[1] = center_of_prev_atom_y + y1 - bondLength/3;
				coords2[3] = center_of_prev_atom_y + y2 - bondLength/3;
			}
			
			coords3[0] = center_of_prev_atom_x + x1;
			coords3[1] = center_of_prev_atom_y + y1;
			coords3[2] = center_of_prev_atom_x + x2;
			coords3[3] = center_of_prev_atom_y + y2;
			
			print_area.bonds.add(coords);
			print_area.bonds.add(coords2);
			print_area.bonds.add(coords3);

			
		}
		

		
		
		
		
	}
}
