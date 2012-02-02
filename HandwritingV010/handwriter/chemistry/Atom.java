package chemistry;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Iterator;
import java.util.LinkedList;

import auto_grader.Print_Area;

import structural_synthesis.AtomSkeleton;

public class Atom {
	private String name; int electrons;
	private AtomSkeleton skeleton;
	private LinkedList<Bond> bonds;
	private int position_x; // # of atoms to the right or the left of the first.
	private int position_y; // # of atoms above or below the first.
	
	public Atom() {
		bonds = new LinkedList<Bond>();
	}
	public Atom(String name) {
		this.name=name;
		bonds = new LinkedList<Bond>();
	}
	public Atom(AtomSkeleton skeleton) {
		this.skeleton = skeleton;
		name = "unknown";
		bonds = new LinkedList<Bond>();
	}
	
	
	public void add_bond(Bond b) {

		bonds.add(b);
	}
	public AtomSkeleton get_skeleton() {
		return skeleton;
	}
	
	public void set_bond_directions() {
		Iterator<Bond> bond_iterator = bonds.iterator();
		Bond this_bond;
		while (bond_iterator.hasNext()) {
			this_bond = bond_iterator.next();
			
			this_bond.set_direction(this);
			
			this_bond.next.set_bond_directions();
		}
	}
	
	public void set_dimensions(boolean first, Bond bond, int previous_x, int previous_y) {
		
		if (first && bond == null) {
			position_x = 0;
			position_y = 0;
		} else {
			//System.out.println("bond direction: " + bond.direction);
			if (bond.direction == 0) { // north
				position_y = previous_y +1;
				position_x = previous_x;
			} else if (bond.direction == 1) { // east
				position_y = previous_y;
				position_x = previous_x + 1;
			} else if (bond.direction == 2) { // south
				position_y = previous_y - 1;
				position_x = previous_x;
			} else if (bond.direction == 3) { // west
				position_y = previous_y;
				position_x = previous_x - 1;
			} else if (bond.direction == 4) { // NE
				position_y = previous_y +1;
				position_x = previous_x +1;
			} else if (bond.direction == 5) { // SE
				position_y = previous_y - 1;
				position_x = previous_x +1;
			} else if (bond.direction == 6) { // SW
				position_y = previous_y -1;
				position_x = previous_x - 1;
			} else if (bond.direction == 7) { // NW
				position_y = previous_y + 1;
				position_x = previous_x -1;
			}		
		}		
		//System.out.println("position: (" + position_x + ", " + position_y + ")");
		Iterator<Bond> bond_iterator = bonds.iterator();
		Bond this_bond;
		while (bond_iterator.hasNext()) {
			this_bond = bond_iterator.next();
			this_bond.next.set_dimensions(false, this_bond, position_x, position_y);
		}
	}
	
	public String toString() {
		String s = "";
		s += "atom key: " + skeleton.key + ", atom name: " + name;
		Iterator<Bond> bond_iterator = bonds.iterator();
		Bond this_bond;
		int counter =0; 
		while (bond_iterator.hasNext())	{
			this_bond = bond_iterator.next();
			s += "\natom " + skeleton.key + ":   bond " + counter +  ", type " + this_bond.type + ": " ;
			s += "\n        " + this_bond.next.toString();			
			
			counter++;
		}
		
		return s;
	}
	public String toStringWithPositions() {
		String s = "";
		s += "atom key: " + skeleton.key + "(" + position_x + ", " + position_y + ") , atom name: " + name;
		
		Iterator<Bond> bond_iterator = bonds.iterator();
		Bond this_bond;
		int counter =0; 
		while (bond_iterator.hasNext())	{
			this_bond = bond_iterator.next();
			s += "\natom " + skeleton.key + "(" + position_x + ", " + position_y + "):   bond " + counter +  ", type " + this_bond.type + ": " ;
			s += "\n        " + this_bond.next.toStringWithPositions();			
			
			counter++;
		}
		
		return s;
	}
	
	public int[] get_borders(int[] max_borders) {
		// borders[0] to the left
		// borders[1] to the right
		// borders[2] below
		// borders[3 above
		
		
		
		if (position_x < max_borders[0]) {
			max_borders[0] = position_x;
		} else if (position_x > max_borders[1]) {
			max_borders[1] = position_x;
		}
		
		if (position_y < max_borders[2]) {
			max_borders[2] = position_y;
		} else if (position_y > max_borders[3]) {
			max_borders[3] = position_y;
		}
		
		Iterator<Bond> bond_iterator = bonds.iterator();
		Bond this_bond;
		int[] next_atoms_borders;
		
		while (bond_iterator.hasNext()) {
			this_bond = bond_iterator.next();
			next_atoms_borders = this_bond.next.get_borders(max_borders);
			
			for (int i=0; i < max_borders.length; i++) {
				if (Math.abs(next_atoms_borders[i]) > Math.abs(max_borders[i])) {
					max_borders[i] = next_atoms_borders[i];
				}
			}
		}
		return max_borders;
	}
	
	
	public int get_position_x() {
		return position_x;
	}
	public int get_position_y() {
		return position_y;
	}
	public void print(Font font, int bondLength, int atomLength, int atoms_above, int atoms_to_left, double mid_height_atom, double mid_width_atom, int center_panel_x, int center_panel_y, Graphics2D g2d, Print_Area print_area) {
		
		//System.out.println("bond length: " + bondLength + ", atom length:  " + atomLength + ", atoms above: " + atoms_above 
		//		+ ", atoms to left: " + atoms_to_left + ", mid y atom: " + mid_height_atom + ", mid x atom: " + mid_width_atom
		//		+ ", center panel: (" + center_panel_x + ", " + center_panel_y + ")");
		double from_center_atom_x = position_x - mid_width_atom;
		double from_center_atom_y = position_y - mid_height_atom;
		
		
		int y = (int) (-1*from_center_atom_y*atomLength + center_panel_y + font.getSize()/2);
		int x = (int) (from_center_atom_x*atomLength + center_panel_x - 10);
		
		//System.out.println("y range: " + Math.abs(atoms_above - position_y) + " ... " + y);
		//System.out.println("x range: " + Math.abs(atoms_to_left - position_x) + " ... " + x);
		
		
		print_area.atoms_x.add(x);
		print_area.atoms_y.add(y);
		print_area.atoms_to_draw.add("" + skeleton.key);
		print_area.paintComponent(print_area.getGraphics());
		
		Iterator<Bond> bond_iterator = bonds.iterator();
		Bond this_bond;
		
		
		//int center_of_this_atom_x = x + atomLength/2;
		//int center_of_this_atom_y = y + atomLength/2;
			
		int center_of_this_atom_x = x + (int)(bondLength*1.2);
		int center_of_this_atom_y = y - (int)(font.getSize()*.4);
		
		/*
		 * TODO allow for dynamic adjusting of length, so that bonds can be properly formatted.
		 */
		int left_atom_length = 2;
		
		while (bond_iterator.hasNext()) {
			this_bond = bond_iterator.next();
			this_bond.print(font, bondLength, atomLength, g2d, center_of_this_atom_x, center_of_this_atom_y, print_area, left_atom_length);
			this_bond.next.print(font, bondLength, atomLength, atoms_above, atoms_to_left, mid_height_atom, mid_width_atom, center_panel_x, center_panel_y, g2d, print_area);
			
			
			
			
		}		
	}

	
}
