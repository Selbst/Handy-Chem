package structural_synthesis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import line.Line;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;
import chemistry.Atom;
import chemistry.Bond;
import chemistry.Molecule;
import contiguities.Contiguities;

public class Synthesize_Molecule {

	@SuppressWarnings("unchecked")
	public static Molecule synthesize(StrokeList drawing) {
		Molecule the_molecule = new Molecule();
		
		drawing.setCharacterRelativeCoordinates();
		
		LinkedList<StrokeList> entities = Contiguities.get_entities(drawing);
		
		
		
		ArrayList<Object> split_entities = find_bonds(entities);
		
		
		
		LinkedList<StrokeList> bonds;
		LinkedList<StrokeList> not_bonds;
		LinkedList<Line> bond_lines;
		
		bonds = (LinkedList<StrokeList> )split_entities.get(0);
		not_bonds = (LinkedList<StrokeList> )split_entities.get(2);
		bond_lines = (LinkedList<Line>) split_entities.get(1);
		
		
		
		split_entities = find_dots(not_bonds);
		LinkedList<StrokeList> dots;
		LinkedList<StrokeList> letters;
		
		dots = (LinkedList<StrokeList>)split_entities.get(0);
		letters = (LinkedList<StrokeList>)split_entities.get(1);
		
		
		
		
		
		LinkedList<AtomSkeleton> atoms;
		
		atoms = find_atoms(dots, letters, bonds, bond_lines);
		//System.out.println("# of atoms: " + atoms.size());
		
		Iterator<StrokeList> bonds_iterator = bonds.iterator();
		Iterator<Line> lines_iterator = bond_lines.iterator();
		AtomSkeleton[][] bonded_atoms = new AtomSkeleton[bonds.size()][2];
		
		int counter =0;
		while (bonds_iterator.hasNext()) {
			//System.out.println("bond " + counter);
			bonded_atoms[counter] = Bond_Connect.connect(bonds_iterator.next(), atoms, lines_iterator.next());
			
			
			//System.out.println("bond " + counter + ": " + bonded_atoms[counter][0].key + " with " + bonded_atoms[counter][0].entities_in_atom.size() + ", " + bonded_atoms[counter][1].key  + " with " + bonded_atoms[counter][1].entities_in_atom.size());
			counter++;
		}
		
		the_molecule = connect_molecule(bonded_atoms, bond_lines);
		                                                
		
		return the_molecule;
		
		
		
		
		
	}
	
	public static Molecule connect_molecule(AtomSkeleton[][] bonded_atoms, LinkedList<Line> bond_lines) {
		
		int[] count_bond_type = new int[bonded_atoms.length];
		int[] bond_pointers = new int[bonded_atoms.length];

		for (int i=0; i < count_bond_type.length; i++) {
			count_bond_type[i] = 1;
			bond_pointers[i] = i;
		}
		
		
		/*
		 * combine bonds pointing to same atoms into double/triple bonds.
		 */
		int count_bonds = 0;
		for (int i=0; i < bonded_atoms.length; i++) {
			if (count_bond_type[i] != 0) { // bond already collapsed.
				for (int j=i+1; j < bonded_atoms.length; j++) {
					
					if ((bonded_atoms[i][0] == bonded_atoms[j][0] && bonded_atoms[i][1] == bonded_atoms[j][1])
							|| (bonded_atoms[i][1] == bonded_atoms[j][0] && bonded_atoms[i][0] == bonded_atoms[j][1])) { // bonds connect same atoms
						bond_pointers[j] = bond_pointers[i];
						count_bond_type[bond_pointers[i]]++;
						count_bond_type[j] = 0;
					}
				}		
			} else {
				count_bonds++;
			}
		}
		
		LinkedList<AtomSkeleton[]> adjusted_bond_atoms = new LinkedList<AtomSkeleton[]>();
		int counter =0;
		LinkedList<Integer> adjusted_bond_types = new LinkedList<Integer>();
		for (int i=0; i < bonded_atoms.length; i++) {
			if (count_bond_type[i] != 0) {
				adjusted_bond_atoms.add(bonded_atoms[i]);
				adjusted_bond_types.add(new Integer(count_bond_type[i]));
				counter++;
			} else {
				bond_lines.remove(counter);
			}
		}
		
		if (adjusted_bond_atoms.size() > 0) {
			Atom first_atom = new Atom(adjusted_bond_atoms.getFirst()[0]);
			
			Molecule molecule = new Molecule(first_atom);

			
			
			for (int i=0; i < count_bond_type.length; i++) {
				//System.out.println("bond " + i + ": " + count_bond_type[i]);
			}
			
			connect_atoms(first_atom, adjusted_bond_atoms, bond_lines, adjusted_bond_types);
			//System.out.println(molecule.toString());
			return molecule;

		} else {
			Molecule molecule = new Molecule();
			return molecule;
			
		}
	}
	
	public static void connect_atoms(Atom atom_to_branch, LinkedList<AtomSkeleton[]> bonded_atoms, LinkedList<Line> bond_lines, LinkedList<Integer> bond_types) {
		

		AtomSkeleton[] this_bond;
		Atom next_atom;
		Bond bond;
		int this_bond_type;
		Line this_bond_line;
		int counter = 0;
	
		
		while (counter < bonded_atoms.size()) {
			this_bond = bonded_atoms.get(counter);
			this_bond_type = bond_types.get(counter);
			this_bond_line = bond_lines.get(counter);
			if (atom_to_branch.get_skeleton().key == this_bond[0].key) {
				next_atom = new Atom(this_bond[1]);
				bond = new Bond(next_atom, this_bond_type, this_bond_line.getSlope());
				atom_to_branch.add_bond(bond);
				bonded_atoms.remove(counter);
				bond_types.remove(counter);
				bond_lines.remove(counter);
				connect_atoms(next_atom, bonded_atoms, bond_lines, bond_types);
				
			} else if (atom_to_branch.get_skeleton().key == this_bond[1].key) {
				next_atom = new Atom(this_bond[0]);
				bond = new Bond(next_atom, this_bond_type, this_bond_line.getSlope());
				atom_to_branch.add_bond(bond);
				bonded_atoms.remove(counter);
				bond_types.remove(counter);
				bond_lines.remove(counter);
				connect_atoms(next_atom, bonded_atoms, bond_lines, bond_types);
				
			} else {
				counter++;
			}
		}
		
		
	}
	
	public static LinkedList<AtomSkeleton> find_atoms(LinkedList<StrokeList> dots, LinkedList<StrokeList> letters, LinkedList<StrokeList> bonds, LinkedList<Line> bond_lines) {
		LinkedList<AtomSkeleton> atoms = new LinkedList<AtomSkeleton>(); 
		
		
		AtomSkeleton this_atom = new AtomSkeleton();

		StrokeList leftmost = find_leftmost(letters);
		this_atom.add(leftmost);

		int counter =0;
		this_atom.key = counter;
		counter++;
		while (letters.size() > 0) {
			leftmost = find_to_right(leftmost, letters, bonds, bond_lines);
			if (leftmost.id != -4) {
				this_atom.add(leftmost);
				leftmost = find_to_right(leftmost, letters, bonds, bond_lines);
				if (leftmost.id != -4) {
					this_atom.add(leftmost);
				} else {
					atoms.add(this_atom);
					
					if (letters.size() > 0) {
						this_atom = new AtomSkeleton();
						this_atom.key = counter;
						counter++;

						leftmost = find_leftmost(letters);
						this_atom.add(leftmost);
						if (letters.size() == 0) {
							//this_atom.add(leftmost);
							leftmost = find_to_right(leftmost, letters, bonds, bond_lines);
							if (leftmost.id == -2) {
								this_atom.add(leftmost);
							}

							atoms.add(this_atom);
						}						
					}
				}
			} else {
				atoms.add(this_atom);
				if (letters.size() > 0) {
					this_atom = new AtomSkeleton();
					this_atom.key = counter;
					counter++;
					
					leftmost = find_leftmost(letters);
					this_atom.add(leftmost);
					if (letters.size() == 0) {
						//this_atom.add(leftmost);
						leftmost = find_to_right(leftmost, letters, bonds, bond_lines);
						if (leftmost.id == -2) {
							this_atom.add(leftmost);
						}
						atoms.add(this_atom);
						
					}

				}
			}
		}
		
		System.out.println("# of atoms: " + atoms.size());
		System.out.println("# of bonds: " + bonds.size());
		System.out.println("# of dots: " + dots.size());
		
		Iterator<StrokeList> iterator = dots.iterator();
		StrokeList this_dot;
		counter=0;
		while (iterator.hasNext()) {
			this_dot = iterator.next();
			this_dot.getStrokes().getFirst().setMaxesAndMins();
			this_dot.setCharacterRelativeCoordinates();
			//this_dot.find_dot_dimensions();
			//System.out.println("coords size: " + this_dot.getStrokes().getFirst().getCoordinates().size());
			//System.out.println("closest atom to dot " + counter + ": " + find_closest_atom(this_dot, atoms).key );
			find_closest_atom(this_dot, atoms).add(this_dot);
			counter++;
		}
		
		
		
		return atoms;
	}
	public static AtomSkeleton find_closest_atom(StrokeList dot, LinkedList<AtomSkeleton> atoms) {
		
		Iterator<AtomSkeleton> iterator = atoms.iterator();
		AtomSkeleton closest = null;
		int min_distance = Integer.MAX_VALUE;
		AtomSkeleton this_atom;
		while (iterator.hasNext()) {
			this_atom = iterator.next();
			int distance = find_distance(dot, this_atom);
			//System.out.println("distance to atom " + this_atom.key + ": " + distance);

			if (distance < min_distance) {
				closest = this_atom;
				min_distance = distance;
			}
		}
		return closest;
		
	}
	
	public static int find_distance(StrokeList dot, AtomSkeleton atom) {
		int min_x = Integer.MAX_VALUE;
		int min_y = Integer.MAX_VALUE;
		//System.out.println("" + dot.minX + ", " + dot.maxX + ", " + dot.minY + ", " + dot.maxY + " -- " + 
		//		atom.minX + ", " + atom.maxX + ", " + atom.minY + ", " + atom.maxY);
		if (dot.minX > atom.minX && dot.maxX < atom.maxX && dot.minY > atom.minY && dot.maxY < atom.maxY) {
			return 0;
		}
		
		int x1 = Math.abs(dot.minX - atom.minX);
		int x2 = Math.abs(dot.minX - atom.maxX);
		int x3 = Math.abs(dot.maxX - atom.minX);
		int x4 = Math.abs(dot.maxX - atom.maxX);
		
		int y1 = Math.abs(dot.minY - atom.minY);
		int y2 = Math.abs(dot.minY - atom.maxY);
		int y3 = Math.abs(dot.maxY - atom.minY);
		int y4 = Math.abs(dot.maxY - atom.maxY);
		
		if (x1 < x2 && x1 < x3 && x1 < x4) {
			min_x = x1;
		} else if (x2 < x3 && x2 < x4) {
			min_x = x2;
		} else if (x3 < x4) {
			min_x = x3;
		} else {
			min_x = x4;
		}
		if (y1 < y2 && y1 < y3 && y1 < y4) {
			min_y = y1;
		} else if (y2 < y3 && y2 < y4) {
			min_y = y2;
		} else if (y3 < y4) {
			min_y = y3;
		} else {
			min_y = y4;
		}
		
		if (min_x > min_y) {
			return min_x;
		} else {
			return min_y;
		}
		
		
	}
	
	public static StrokeList find_leftmost(LinkedList<StrokeList> letters) {
		
		int min_x = Integer.MAX_VALUE; 
		StrokeList leftmost_letter = new StrokeList();
		StrokeList this_letter;
		Iterator<StrokeList> letters_iterator = letters.iterator();
		
		int min_index = -1;
		int this_min_x;
		int index = 0;
		while (letters_iterator.hasNext()) {
			this_letter = letters_iterator.next();
			this_min_x = this_letter.minX;
			
			if (this_min_x < min_x) {
				leftmost_letter = this_letter;
				min_x = this_min_x;
				min_index = index;
			}
			index++;
		}
		letters.remove(min_index);
		return leftmost_letter;
	}
	
	public static StrokeList find_to_right(StrokeList left, LinkedList<StrokeList> letters, LinkedList<StrokeList> bonds, LinkedList<Line> bond_lines) {
		
		Iterator<StrokeList> iterator = letters.iterator();
		int letter_x = left.maxX;
		
		StrokeList this_entity;
		int min_x = Integer.MAX_VALUE;
		StrokeList closest_in_line = null;
		int closest_index =-1;
		int index =0;
		while (iterator.hasNext()) {
			this_entity = iterator.next();
			if (check_in_line(left.minY, left.maxY, this_entity.minY, this_entity.maxY)) {
				if (this_entity.minX > left.maxX && this_entity.minX < min_x) {
					min_x = this_entity.minX;
					closest_in_line = this_entity;
					closest_index = index;
				}
			}
			index++;
		}
		
		iterator = bonds.iterator();
		index =0;
		while (iterator.hasNext()) {
			this_entity = iterator.next();
			if (check_in_line(left.minY, left.maxY, this_entity.minY, this_entity.maxY)) {
				if (this_entity.minX > left.maxX && this_entity.minX < min_x) {
					min_x = this_entity.minX;
					closest_in_line = this_entity;
					closest_index = index;
				}
			}
			index++;
		}
		
		if (closest_in_line == null) { // nothing to the right.
			//System.out.println("nothing");
			return (new StrokeList(null, -4));
		}
		
		if (closest_in_line.id == -2) { // closest was classified as bond.
			//System.out.println("bond");
			Line this_bond_line = bond_lines.get(closest_index);
			
			if (Math.abs(this_bond_line.getSlope()) > 5) { // vertical line means letter
				bonds.remove(closest_index);
				bond_lines.remove(closest_index);
				return closest_in_line; // return vertical line.
			} else {
				
				return (new StrokeList(null, -4)); // return -4 to indicate that it is the end of the atom.
			}
		} else { 
			//System.out.println("letter");
			letters.remove(closest_index);
			return closest_in_line;
		}
		
	}
	
	public static boolean check_in_line(int minY, int maxY, int minY2, int maxY2) {
		
		if (minY < minY2) {
			if (minY2 < maxY) { // case 1
				return true;
			}
		}
		
		if (minY > minY2) {
			if (maxY2 < maxY && maxY2 > minY) { // case 2
				return true;
			} else if (maxY2 > maxY) { // case 3
				return true;
			}
			
			
		}
		return false;
		
		
		
	}
	
	
	public static ArrayList<Object> find_dots(LinkedList<StrokeList> entities) {
		LinkedList<StrokeList> dots = new LinkedList<StrokeList>();
		LinkedList<StrokeList> not_dots = new LinkedList<StrokeList>();
		
		Iterator<StrokeList> entities_iterator = entities.iterator();
		
		StrokeList this_entity;
		while (entities_iterator.hasNext()) {
			this_entity = entities_iterator.next();
			if (this_entity.getStrokes().size() == 1) {
				if (this_entity.getStrokes().getFirst().checkIfDot()) {
					this_entity.id = -3;
					dots.add(this_entity);
				} else {
					not_dots.add(this_entity);
				}
			} else {
				not_dots.add(this_entity);
			}
		}
		
		
		ArrayList<Object> return_entities = new ArrayList<Object>(2);
		return_entities.add(dots);
		return_entities.add(not_dots);
		return return_entities;
	}
	
	public static ArrayList<Object> find_bonds(LinkedList<StrokeList> entities) {
		Iterator<StrokeList> entities_iterator = entities.iterator();
		StrokeList this_entity;
		Line this_bond = null;
		LinkedList<StrokeList> bonds = new LinkedList<StrokeList>();
		LinkedList<StrokeList> not_bonds = new LinkedList<StrokeList>();
		LinkedList<Line> bond_lines = new LinkedList<Line>();
		
		int index = 0;
		while (entities_iterator.hasNext()) {
			
			this_entity = entities_iterator.next();
			this_entity.setCharacterRelativeCoordinates();
			//System.out.println("entity " + index + " size: " + this_entity.getStrokes().size());
			boolean bond = false;
			if (this_entity.getStrokes().size() == 1) {
				
				bond = false;
				if (this_entity.getStrokes().getFirst().checkIfDot()) {
					
				} else {
					Stroke this_stroke = this_entity.getStrokes().getFirst();
					this_bond = Line.regressionLine(this_stroke.getCoordinates());
					this_bond.setClassification();
					
					double error = Line.findMeanError(this_stroke.getCoordinates(), this_bond);
					double avg_dim = (this_stroke.getStrokeHeight() + this_stroke.getStrokeWidth())/2;
					double ratio = error/avg_dim; // 0.1 = threshold, > 0.1 not straight line.
					
					if (ratio < 0.1) {
						bond = true;
					} else if (Math.abs(ratio / this_bond.getSlope()) < 0.1) {
						bond = true;
					}
				}

				
				
			}
			if (bond) {
				bonds.add(this_entity);
				bond_lines.add(this_bond);
				this_entity.id = -2;
			} else {
				not_bonds.add(this_entity);
			}
			index++;
		}		
		
		ArrayList<Object> return_entities = new ArrayList<Object>();
		return_entities.add(bonds);
		return_entities.add(bond_lines);
		return_entities.add(not_bonds);
		
		return return_entities;
	}	
		
	
	
	
}
