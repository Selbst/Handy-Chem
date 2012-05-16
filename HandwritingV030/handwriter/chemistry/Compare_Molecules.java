package chemistry;

import java.util.Iterator;
import java.util.LinkedList;

public class Compare_Molecules {


	public static int compare(Molecule m1, Molecule m2) {
		
		if (structural_isomer(m1, m2)) {
			return 1;
		}
		
		return 0;
		
	}
	public static boolean rotation_isomer(Molecule m1, Molecule m2) {
		
		String[][] atoms_m1 = get_atom_matrix(m1);
		LinkedList<Integer[]> bonds_m1 = get_bond_list(m1);
		LinkedList<Integer[]> electrons_m1 = get_electron_list(m1);
		
		String[][] atoms_m2 = get_atom_matrix(m2);
		LinkedList<Integer[]> bonds_m2 = get_bond_list(m2);
		LinkedList<Integer[]> electrons_m2 = get_electron_list(m2);
		
		
		
		for (int j=0; j < 8; j++) {
			if (compare_atoms(atoms_m1, atoms_m2)) {
				System.out.println("atoms ok");
				if (compare_bonds(bonds_m1, bonds_m2)) {
					System.out.println("bonds ok");
					if  (compare_electrons(electrons_m1, electrons_m2)) {
						return true;
					}
				}
			}
			m2 = rotate(m2);
			atoms_m2 = get_atom_matrix(m2);
			bonds_m2 = get_bond_list(m2);
			electrons_m2 = get_electron_list(m2);
			/*
			for (int i=0; i < atoms_m2.length; i++) {
				for (int q=0; q < atoms_m2[i].length; q++) {
					System.out.print(atoms_m2[i][q] + ", ");
				}
				System.out.println();
			}
			System.out.println();
			System.out.println("meth1: ");
			for (Integer[] b : bonds_m1) {
				for (int z : b) {
					System.out.print(z + ", ");
				}
				System.out.println();
			}
			System.out.println("meth2: ");
			for (Integer[] b : bonds_m2) {
				for (int z : b) {
					System.out.print(z + ", ");
				}
				System.out.println();
			}
			*/
			
		}
		return false;
	}
	private static Molecule rotate(Molecule m) { 

		Atom pivot = m.get_first();
		Atom rotated_pivot = pivot.copy();
		
		Molecule rotated_m = new Molecule(rotated_pivot);
		
		LinkedList<Atom[]> atom_queue = new LinkedList<Atom[]>();
		
		boolean marked = !pivot.mark;
		
		Atom[] this_atom = new Atom[2];
		this_atom[0] = pivot;
		this_atom[1] = rotated_pivot;
		atom_queue.add(this_atom);
		while (!atom_queue.isEmpty()) {
			this_atom = atom_queue.poll();
			
			this_atom[0].mark = marked;
			this_atom[1].mark = marked;
			
			Iterator<Bond> bonds = this_atom[0].get_bonds().iterator();
			while (bonds.hasNext()) {
				Bond b = bonds.next();

				
				if (b.next.mark != marked) {
					Atom[] next_atom = new Atom[2];
					next_atom[0] = b.next;
					next_atom[1] = b.next.copy();
					b.prev = this_atom[1];
					Bond b2 = new Bond(this_atom[1]);
					b2.type = b.type;
					this_atom[1].add_bond(b2);
					if (b.direction == 0) {
						b2.direction = 4;
						next_atom[1].set_position_x(this_atom[1].get_position_x() + 1);
						next_atom[1].set_position_y_java(this_atom[1].get_position_y_java() - 1);
					} else if (b.direction == 4) {
						b2.direction = 1;
						next_atom[1].set_position_x(this_atom[1].get_position_x() + 1);
						next_atom[1].set_position_y_java(this_atom[1].get_position_y_java() + 0);
					} else if (b.direction == 1) {
						b2.direction = 5;
						next_atom[1].set_position_x(this_atom[1].get_position_x() + 1);
						next_atom[1].set_position_y_java(this_atom[1].get_position_y_java() + 1);
					} else if (b.direction == 5) {
						b2.direction = 2;
						next_atom[1].set_position_x(this_atom[1].get_position_x() + 0);
						next_atom[1].set_position_y_java(this_atom[1].get_position_y_java() + 1);
					} else if (b.direction == 2) {
						b2.direction = 6;
						next_atom[1].set_position_x(this_atom[1].get_position_x() - 1);
						next_atom[1].set_position_y_java(this_atom[1].get_position_y_java() + 1);
					} else if (b.direction == 6) {
						b2.direction = 3;
						next_atom[1].set_position_x(this_atom[1].get_position_x() - 1);
						next_atom[1].set_position_y_java(this_atom[1].get_position_y_java() + 0);
					} else if (b.direction == 3) {
						b2.direction = 7;
						next_atom[1].set_position_x(this_atom[1].get_position_x() - 1);
						next_atom[1].set_position_y_java(this_atom[1].get_position_y_java() - 1);
					} else if (b.direction == 7) {
						b2.direction = 0;
						next_atom[1].set_position_x(this_atom[1].get_position_x() + 0);
						next_atom[1].set_position_y_java(this_atom[1].get_position_y_java() - 1);
					}
					b2.next = next_atom[1];
					atom_queue.add(next_atom);
				}
			}
		}

		return rotated_m;
	}
	
	public static boolean structural_isomer(Molecule m1, Molecule m2) {
		return false;
	}
	public static boolean stereoisomer(Molecule m1, Molecule m2) {
		return false;
	}
	
	private static boolean compare_atoms(String[][] a1, String[][] a2) {
		System.out.println("comparison: ");
		if (a1.length != a2.length) {
			return false;
		}
		if (a1[0].length != a2[0].length) {
			return false;
		}
		try {
			for (int i=0; i < a1.length; i++) {
				for (int j=0; j < a1[i].length; j++) {
					if (a1[i][j] == null) {
						a1[i][j] = " ";
					}
					if (a2[i][j] == null) {
						a2[i][j] = " ";
					}
					if (!a1[i][j].equals(a2[i][j])) {
						return false;
					}
				}
			}
		} catch(IndexOutOfBoundsException e) {
			return false;
		} catch(NullPointerException e) {
			return false;
		}
		return true;
	}
	private static boolean compare_bonds(LinkedList<Integer[]> b1, LinkedList<Integer[]> b2) {
		for (Integer[] this_bond : b1) {
			for (int i : this_bond) {
				System.out.print(i + ", ");
			}
			System.out.println();
		}
		for (Integer[] this_bond : b2) {
			for (int i : this_bond) {
				System.out.print(i + ", ");
			}
			System.out.println();
		}
		for (Integer[] this_bond : b1) {
			boolean found_one = false;
			for (Integer[] compare_bond : b2) {
				if (compare_1bond(this_bond, compare_bond)) {
					System.out.print("match found for: ");
					for (int i : this_bond) {
						System.out.print(i + ", ");
					}
					System.out.println();
					for (int i : compare_bond) {
						System.out.print(i + ", ");
					}
					found_one = true;
				}
			}
			if (!found_one) {
				return false;
			}
 		}
		
		return true;
	}
	private static boolean compare_1bond(Integer[] b1, Integer[] b2) {
		if (b1[4] != b2[4]) {
			return false;
		}
		if (b1[5] == 0) {
			if (b2[5] == 0) {
				for (int i=0; i < 4; i++) {
					if (b1[i] != b2[i]) {
						System.out.println("00: " + b1[i] + ", " + b2[i]);
						return false;
					}
				}
			} else if (b2[5] == 2) {
				if (b1[0] != b2[2] || b1[1] != b2[3] || b1[2] != b2[0] || b1[3] != b2[1]) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (b1[5] == 1) {
			if (b2[5] == 1) {
				for (int i=0; i < 4; i++) {
					if (b1[i] != b2[i]) {
						System.out.println("11: " + b1[i] + ", " + b2[i]);
						return false;
					}
				}
			} else if (b2[5] == 3) {
				if (b1[0] != b2[2] || b1[1] != b2[3] || b1[2] != b2[0] || b1[3] != b2[1]) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (b1[5] == 2) {
			if (b2[5] == 2) {
				for (int i=0; i < 4; i++) {
					if (b1[i] != b2[i]) {
						System.out.println("22: " + b1[i] + ", " + b2[i]);
						return false;
					}
				}
			} else if (b2[5] == 0) {
				if (b1[0] != b2[2] || b1[1] != b2[3] || b1[2] != b2[0] || b1[3] != b2[1]) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (b1[5] == 3) {
			if (b2[5] == 3) {
				for (int i=0; i < 4; i++) {
					if (b1[i] != b2[i]) {
						System.out.println("33: " + b1[i] + ", " + b2[i]);
						return false;
					}
				}
			} else if (b2[5] == 1) {
				if (b1[0] != b2[2] || b1[1] != b2[3] || b1[2] != b2[0] || b1[3] != b2[1]) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (b1[5] == 4) {
			if (b2[5] == 4) {
				for (int i=0; i < 4; i++) {
					if (b1[i] != b2[i]) {
						System.out.println("44: " + b1[i] + ", " + b2[i]);
						return false;
					}
				}
			} else if (b2[5] == 6) {
				if (b1[0] != b2[2] || b1[1] != b2[3] || b1[2] != b2[0] || b1[3] != b2[1]) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (b1[5] == 5) {
			if (b2[5] == 5) {
				for (int i=0; i < 4; i++) {
					if (b1[i] != b2[i]) {
						System.out.println("55: " + b1[i] + ", " + b2[i]);
						return false;
					}
				}
			} else if (b2[5] == 7) {
				if (b1[0] != b2[2] || b1[1] != b2[3] || b1[2] != b2[0] || b1[3] != b2[1]) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (b1[5] == 6) {
			if (b2[5] == 6) {
				for (int i=0; i < 4; i++) {
					if (b1[i] != b2[i]) {
						System.out.println("66: " + b1[i] + ", " + b2[i]);
						return false;
					}
				}
			} else if (b2[5] == 4) {
				if (b1[0] != b2[2] || b1[1] != b2[3] || b1[2] != b2[0] || b1[3] != b2[1]) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (b1[5] == 7) {
			if (b2[5] == 7) {
				for (int i=0; i < 4; i++) {
					if (b1[i] != b2[i]) {
						System.out.println("77: " + b1[i] + ", " + b2[i]);
						return false;
					}
				}
			} else if (b2[5] == 5) {
				if (b1[0] != b2[2] || b1[1] != b2[3] || b1[2] != b2[0] || b1[3] != b2[1]) {
					return false;
				}
			} else {
				return false;
			}
		}
		System.out.println("bonds " + b1[5] + ", " + b2[5]);
		return true;
	}
	private static boolean compare_electrons(LinkedList<Integer[]> e1, LinkedList<Integer[]> e2) {
		return compare_bonds(e1, e2);
	}
	private static LinkedList<Integer[]> get_electron_list(Molecule m) {
		Atom this_atom = m.get_first();
		LinkedList<Atom> atom_queue = new LinkedList<Atom>();
		
		boolean marked = !this_atom.mark;
		LinkedList<Integer[]> electron_list = new LinkedList<Integer[]>();
		
		atom_queue.add(this_atom);
		while (!atom_queue.isEmpty()) {
			this_atom = atom_queue.poll();
			this_atom.mark = marked;
			
			Integer[] electron_data = new Integer[3];
			electron_data[0] = this_atom.get_position_x() - m.min_x;
			electron_data[1] = this_atom.get_position_y_java() - m.min_y;
			electron_data[2] = this_atom.electrons;
			
			Iterator<Bond> bonds = this_atom.get_bonds().iterator();
			while (bonds.hasNext()) {
				Bond b = bonds.next();
				if (b.next.mark != marked) {
					atom_queue.add(b.next);
				}
			}
		}
		return electron_list;
	}
	private static LinkedList<Integer[]> get_bond_list(Molecule m) {
		Atom this_atom = m.get_first();
		LinkedList<Atom> atom_queue = new LinkedList<Atom>();
		
		boolean marked = !this_atom.mark;
		LinkedList<Integer[]> bond_list = new LinkedList<Integer[]>();
		
		atom_queue.add(this_atom);
		while (!atom_queue.isEmpty()) {
			this_atom = atom_queue.poll();
			this_atom.mark = marked;
			Iterator<Bond> bonds = this_atom.get_bonds().iterator();
			while (bonds.hasNext()) {
				Bond b = bonds.next();
				Integer[] b_data = new Integer[6];
				b_data[0] = b.prev.get_position_x() - m.min_x;
				b_data[1] = b.prev.get_position_y_java() - m.min_y;
				b_data[2] = b.next.get_position_x() - m.min_x;
				b_data[3] = b.next.get_position_y_java() - m.min_y;
				b_data[4] = b.type;
				b_data[5] = b.direction;
				bond_list.add(b_data);
				if (b.next.mark != marked) {
					atom_queue.add(b.next);
				}
			}
		}
		return bond_list;
	}
	private static String[][] get_atom_matrix(Molecule m) {
		String[][] a = new String[m.get_width()][m.get_height()];
		Atom this_atom = m.get_first();
		LinkedList<Atom> atom_queue = new LinkedList<Atom>();
		
		boolean marked = !this_atom.mark;
		LinkedList<Object[][]> relative_positions = new LinkedList<Object[][]>();
		
		atom_queue.add(this_atom);
		while (!atom_queue.isEmpty()) {
			this_atom = atom_queue.poll();
			Integer[] this_atom_pos = new Integer[2];
			
			this_atom_pos[0] = this_atom.get_position_x();
			
			
			//System.out.println(this_atom.symbol + ": " + this_atom.get_position_x() + ", " + this_atom.get_position_y_java());
			
			this_atom_pos[1] = this_atom.get_position_y_java();
			Object[][] o = new Object[2][];
			o[0] = new Object[1];
			o[0][0] = this_atom.symbol;
			o[1] = this_atom_pos;
			relative_positions.add(o);
			this_atom.mark = marked;
			
			Iterator<Bond> bonds = this_atom.get_bonds().iterator();
			while (bonds.hasNext()) {
				Bond b = bonds.next();
				if (b.next.mark != marked) {
					atom_queue.add(b.next);
				}
			}
		}
		
		int x_min =Integer.MAX_VALUE;
		int y_min = Integer.MAX_VALUE;
		for (Object[][] atom : relative_positions) {
			int x = (int)(Integer)atom[1][0];
			int y = (int)(Integer)atom[1][1];
			if (x < x_min) {
				x_min = x;				
			}
			if (y < y_min) {
				y_min = y;
			}
		}
		m.min_x = x_min;
		m.min_y = y_min;
		//System.out.println(m.min_y + ", " + m.min_x);
		for (Object[][] atom : relative_positions) {
			//System.out.println((String)atom[0][0] + ": " + ((Integer)atom[1][0] - x_min) + ", " + ((Integer)atom[1][1] - y_min)); 
			a[(Integer)atom[1][0] - x_min][(Integer)atom[1][1] - y_min] = (String)atom[0][0];
		}
		return a;
	}
	
	public static void main(String[] args) {
		Atom first = new Atom();
		first.symbol = "Cl";
		first.set_position_x(1);
		first.set_position_x(1);
		Molecule m = new Molecule(first);
		m.set_height(2);
		m.set_width(2);
		Atom first2 = new Atom();
		first2.symbol = "H";
		first2.set_position_x(2);
		first2.set_position_x(2);
		Bond b1 = new Bond(first, 2, first2);
		b1.next = first2;
		first.add_bond(b1);
		
		Atom a2 = new Atom();
		a2.symbol = "Cl";
		a2.set_position_x(1);
		a2.set_position_x(2);
		Molecule m2 = new Molecule(a2);
		m2.set_height(1);
		m2.set_width(1);
		
		System.out.println(compare_atoms(get_atom_matrix(m), get_atom_matrix(m2)));
		System.out.println(compare_bonds(get_bond_list(m), get_bond_list(m2)));
		System.out.println(rotation_isomer(m, m2));
		
		
		
		Atom c1 = new Atom();
		c1.symbol = "C";
		c1.set_position_x(0);
		c1.set_position_y_java(0);
		Molecule meth1 =  new Molecule(c1);
		meth1.set_height(3);
		meth1.set_width(3);
		Atom h1 = new Atom();
		h1.symbol = "H";
		h1.set_position_x(1);
		h1.set_position_y_java(-1);
		Bond beth1 = new Bond(h1, 4, c1, true);
		beth1.next = h1;
		c1.add_bond(beth1);
		Atom h2 = new Atom();
		h2.symbol = "H";
		h2.set_position_x(1);
		h2.set_position_y_java(1);
		Bond beth2 = new Bond(h1, 5, c1, true);
		beth2.next = h2;
		c1.add_bond(beth2);
		Atom h3 = new Atom();
		h3.symbol = "H";
		h3.set_position_x(-1);
		h3.set_position_y_java(1);
		Bond beth3 = new Bond(h3, 6, c1, true);
		beth3.next = h3;
		c1.add_bond(beth3);
		Atom h4 = new Atom();
		h4.symbol = "H";
		h4.set_position_x(-1);
		h4.set_position_y_java(-1);
		Bond beth4 = new Bond(h4, 7, c1, true);
		beth4.next = h4;
		c1.add_bond(beth4);
		
		Atom c2 = new Atom();
		c2.symbol = "C";
		c2.set_position_x(0);
		c2.set_position_y_java(0);
		Molecule meth2 =  new Molecule(c2);
		meth2.set_height(3);
		meth2.set_width(3);
		Atom hh1 = new Atom();
		hh1.symbol = "H";
		hh1.set_position_x(1);
		hh1.set_position_y_java(0);
		Bond bbeth1 = new Bond(hh1, 1, c2, true);
		bbeth1.next = hh1;
		c2.add_bond(bbeth1);
		Atom hh2 = new Atom();
		hh2.symbol = "H";
		hh2.set_position_x(0);
		hh2.set_position_y_java(1);
		Bond bbeth2 = new Bond(hh2, 2, c2, true);
		bbeth2.next = hh2;
		c2.add_bond(bbeth2);
		Atom hh3 = new Atom();
		hh3.symbol = "H";
		hh3.set_position_x(-1);
		hh3.set_position_y_java(0);
		Bond bbeth3 = new Bond(hh3, 3, c2, true);
		bbeth3.next = hh3;
		c2.add_bond(bbeth3);
		Atom hh4 = new Atom();
		hh4.symbol = "H";
		hh4.set_position_x(0);
		hh4.set_position_y_java(-1);
		Bond bbeth4 = new Bond(hh4, 0, c2, true);
		bbeth4.next = hh4;
		c2.add_bond(bbeth4);
		
		System.out.println(rotation_isomer(meth1, meth2));
		
		
	}
}
