package chemistry;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import auto_grader.Print_Area;

public class Molecule {
	private Atom first;
	
	
	private int[][] bonds; // adjacency matrix
	private int[] atoms;
	
	private int atoms_to_left; // (of first)
	private int atoms_to_right;
	private int atoms_above;
	private int atoms_below;
	private int height;
	private int width;
	int min_y;
	int min_x;
	
	public void set_height(int h) {
		height = h;
	}
	public void set_width(int w) {
		width = w;
	}
	
	private String name;
	public Molecule() {
	}
	public Molecule(String name) {
		this.name=name;
	}
	public Molecule(Atom first) {
		this.first = first;
		height=0;
		width=0;
	}
	protected Atom get_first() {
		return first;
	}
	
	
	public int[] set_dimensions() {
		set_bond_directions();
		first.set_dimensions(true, null, 0, 0);
		int[] dimensions = new int[4];
		dimensions[0] = 0; dimensions[1] = 0; dimensions[2] = 0; dimensions[3] = 0;
		
		
		dimensions = first.get_borders(dimensions);
		atoms_to_left = dimensions[0];
		atoms_to_right = dimensions[1];
		atoms_below = dimensions[2];
		atoms_above = dimensions[3];
		
		
		height = Math.abs(atoms_below) + atoms_above + 1;
		width = Math.abs(atoms_to_left) + atoms_to_right + 1;
		
		return dimensions;
	}
	public void set_characters() throws NumberFormatException, IOException {
		first.set_characters();
	}
	private void set_bond_directions() {
		first.set_bond_directions();
	}
	
	
	public int get_height() {
		if (height == 0 || width == 0) {
			Atom this_atom = first;
			LinkedList<Atom> atom_queue = new LinkedList<Atom>();
			
			boolean marked = !this_atom.mark;
			LinkedList<Object[][]> relative_positions = new LinkedList<Object[][]>();
			
			atom_queue.add(this_atom);
			int min_x = Integer.MAX_VALUE;
			int max_x = Integer.MIN_VALUE;
			int min_y = Integer.MAX_VALUE;
			int max_y = Integer.MIN_VALUE;
			while (!atom_queue.isEmpty()) {
				this_atom = atom_queue.poll();
				int x = this_atom.get_position_x();
				int y = this_atom.get_position_y();
				if (min_x > x) {
					min_x = x;
				}
				if (max_x < x) {
					max_x = x;
				}
				if (min_y > y) {
					min_y = y;
				}
				if (max_y < y) {
					max_y = y;
				}
				this_atom.mark = marked;
				
				Iterator<Bond> bonds = this_atom.get_bonds().iterator();
				while (bonds.hasNext()) {
					Bond b = bonds.next();
					if (b.next.mark != marked) {
						atom_queue.add(b.next);
					}
				}
			}
			height = max_y - min_y + 1;
			width = max_x - min_x + 1;
		}
		return height;
	}
	public int get_width() {
		if (height == 0 || width == 0) {
			Atom this_atom = first;
			LinkedList<Atom> atom_queue = new LinkedList<Atom>();
			
			boolean marked = !this_atom.mark;
			LinkedList<Object[][]> relative_positions = new LinkedList<Object[][]>();
			
			atom_queue.add(this_atom);
			int min_x = Integer.MAX_VALUE;
			int max_x = Integer.MIN_VALUE;
			int min_y = Integer.MAX_VALUE;
			int max_y = Integer.MIN_VALUE;
			while (!atom_queue.isEmpty()) {
				this_atom = atom_queue.poll();
				int x = this_atom.get_position_x();
				int y = this_atom.get_position_y();
				if (min_x > x) {
					min_x = x;
				}
				if (max_x < x) {
					max_x = x;
				}
				if (min_y > y) {
					min_y = y;
				}
				if (max_y < y) {
					max_y = y;
				}
				this_atom.mark = marked;
				
				Iterator<Bond> bonds = this_atom.get_bonds().iterator();
				while (bonds.hasNext()) {
					Bond b = bonds.next();
					if (b.next.mark != marked) {
						atom_queue.add(b.next);
					}
				}
			}
			height = max_y - min_y + 1;
			width = max_x - min_x + 1;
		}
		return width;
	}

	
	public String toString() {
		String s = "Molecule:\n";
		s += first.toString();
		
		return s;
	}
	public String toStringWithPositions() {
		String s = "Molecule:\n";
		s += first.toStringWithPositions();
		return s;
	}
	public static void main(String[] args) {
		Molecule mol = new Molecule("d");
		System.out.println(mol.name);
	}
	public void print(Font font, int bond_length, int atom_length, Graphics2D g2d, Print_Area print_area) {
		print_area.initialize_drawing();
		double mid_height_atom = atoms_below + (atoms_above - atoms_below)/2.0;
		double mid_width_atom = atoms_to_left + (atoms_to_right - atoms_to_left)/2.0;
		int center_x = print_area.get_width()/2;
		int center_y = print_area.get_height()/2;
		first.print(font, bond_length, atom_length, atoms_above, atoms_to_left, mid_height_atom, mid_width_atom, center_x, center_y, g2d, print_area);
		System.out.println(toStringWithPositions());
	}
	
}
