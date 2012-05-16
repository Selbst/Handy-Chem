package chemistry;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.AttributedString;
import java.util.Iterator;
import java.util.LinkedList;

import dictionary.Lookup_Char;

import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;

import auto_grader.Print_Area;

import recognition.Compare_Classifiers;
import recognition.features.TrainingSet;
import recognition.filter.Filter_Lowercase;
import recognition.filter.Filter_Uppercase;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;
import structural_synthesis.AtomSkeleton;
import structural_synthesis.Synthesize_Molecule;

public class Atom {
	private String name; int electrons;
	private AtomSkeleton skeleton;
	private LinkedList<Bond> bonds;
	private int position_x; // # of atoms to the right or the left of the first.
	private int position_y; // # of atoms above or below the first.
	private int[] letter_0; // letter_0[0] = letter of alphabet 0 - 25. letter_0[1] = strokelist index in skeleton.
	private int[] letter_1;
	private int[] letter_2;
	public String symbol;
	public int position_y_java;
	
	boolean mark;
	
	
	private Integer[] electrodes;
	public void set_position_x(int x) {
		position_x = x;
	}
	public void set_position_y(int y) {
		position_y = y;
		position_y_java = -y;
	}
	public void set_position_y_java(int y) {
		position_y_java = y;
	}
	public void set_position_y_java() {
		position_y_java = -position_y;
	}
	public int get_position_y_java() {
		return position_y_java;
	}
	
	
	public Atom() {
		bonds = new LinkedList<Bond>();
		symbol = "";
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
	
	public void classify_atom() throws NumberFormatException, IOException {
		classify_letter(0);
		classify_letter(1);
		//System.out.println("classified as: " + letter_1[0]);
	}
	public void classify_letter(int index) throws NumberFormatException, IOException {
		boolean debug_classification = true;
		//skeleton.entities_in_atom;
		//entities.getFirst().getStrokes().getFirst().checkIfDot();
		StrokeList character;
		Filter_Lowercase f_lowercase = new Filter_Lowercase();
		Filter_Uppercase f_uppercase = new Filter_Uppercase();

		f_lowercase.set_classifier_branches();
		f_uppercase.set_classifier_branches();
		if (index == 0) { // first letter, uppercase
			
			int char_index = get_leftmost_char(-1, -1);
		
			letter_0 = new int[2];
			letter_0[1] = char_index;
			letter_0[0] = -1;
			character = skeleton.entities_in_atom.get(char_index);
			//System.out.println("capital letter: "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
			
			
			int[][] filtered_result = f_uppercase.filter(character);
			String path = "";
			for (int i=0; i < filtered_result[0].length; i++) {
				path += filtered_result[0][i] + "." ;
			}
			//System.out.println("filter uppercase path: " + path);
			boolean[] toggleFeatures = new boolean[4]; 
			toggleFeatures[0] = true; // 3x3
			toggleFeatures[1] = false; // 2x2
			toggleFeatures[2] = false;
			toggleFeatures[3] = false;
			double[] inputs_3x3;
			
			LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
			chars.add(character);
			
			TrainingSet trainer = new TrainingSet(chars, toggleFeatures);
			inputs_3x3 = trainer.inputs[0];
			toggleFeatures[0] = false; // 3x3
			toggleFeatures[1] = true; // 2x2
			toggleFeatures[2] = false;
			toggleFeatures[3] = false;
			double[] inputs_2x2;
			
			trainer = new TrainingSet(chars, toggleFeatures);
			inputs_2x2 = trainer.inputs[0];
			
			
			StrokeNetwork CvsH = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/caps/filtered/vs/2vs7_0.net");
			StrokeNetwork CvsO = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/caps/filtered/vs/2vs14_0.net");
			StrokeNetwork HvsO = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/caps/filtered/vs/7vs14_0.net");
			StrokeNetwork CvsS = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/caps/filtered/vs/2vs18_0.net");
			StrokeNetwork HvsS = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/caps/filtered/vs/7vs18_0.net");
			StrokeNetwork OvsS = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/caps/filtered/vs/14vs18_0.net");

			double cvh = StrokeNetworkBuilder.getOutput(inputs_2x2, CvsH);
			double cvo = StrokeNetworkBuilder.getOutput(inputs_2x2, CvsO);
			double hvo = StrokeNetworkBuilder.getOutput(inputs_2x2, HvsO);
			double cvs = StrokeNetworkBuilder.getOutput(inputs_2x2, CvsS);
			double hvs = StrokeNetworkBuilder.getOutput(inputs_2x2, HvsS);
			double ovs = StrokeNetworkBuilder.getOutput(inputs_2x2, OvsS);

			System.out.println("cvh: " + cvh  + ", cvo: " + cvo + ", hvo: " + hvo + ", cvs: " + cvs + ", hvs: " + hvs + ", ovs: " + ovs);
			
			if (cvh > 0 && cvo > 0 && cvs > 0) {
				symbol = "C";
			} else if (cvh < 0 && hvo > 0 && hvs > 0){
				symbol = "H";
			} else if (hvo < 0 && ovs > 0){
				symbol = "O";
			} else {
				symbol = "S";
			}
			

		} else if (index == 1) { // second letter, lowercase
			//character = skeleton.entities_in_atom.get(get_leftmost_char(letter_0[1], -1));
			letter_1 = new int[2];
			//System.out.println(letter_0[1]);
			//System.out.println(get_leftmost_char(letter_0[1], -1));
			if (get_leftmost_char(letter_0[1], -1) == -1) {
				letter_1[0] = -1;
			} else {
				character = skeleton.entities_in_atom.get(get_leftmost_char(letter_0[1], -1));
				if (Synthesize_Molecule.is_bond(character)) {
					Iterator<StrokeList> entities_iterator = skeleton.entities_in_atom.iterator();
					StrokeList this_entity;
					boolean is_i = false;
					for (int i=0; i < skeleton.entities_in_atom.size(); i++) {
						this_entity = entities_iterator.next();
						Stroke first_stroke = this_entity.getStrokes().getFirst();
						if (first_stroke.checkIfDot()) {
							//System.out.println("dot check");
							//if (first_stroke.getMaxY() > (this_entity.minY + 10)) {
								//System.out.println("pass 1");
								//System.out.println(first_stroke.getMaxX() + ", " + (character.minX - 20) + ", " + first_stroke.getMaxX() + ", " + (character.maxX - 20));
								if (first_stroke.getMaxX() > (character.minX - 30) && first_stroke.getMinX() < (character.maxX + 30)) {
									//System.out.println("pass 2");

									is_i = true;
									first_stroke.is_i = true;
								}
							//}
						}
					}
					if (is_i) {
						letter_1[0] = 8;
						symbol += Lookup_Char.lookup(letter_1[0], false);
						//System.out.println("symbol 2: " + symbol);

					} else {
						if (character.getStrokes().size() == 1) {
							letter_1[0] = 11;
							symbol += Lookup_Char.lookup(letter_1[0], false);

						} else {
							System.out.println("lowercase letter (1): "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
							
							/*
							 * 
							 * 
							 */
							int[][] filtered_result = f_lowercase.filter(character);
							String path = "";
							for (int i=0; i < filtered_result[0].length; i++) {
								path += filtered_result[0][i] + "." ;
							}
							if (debug_classification)
								System.out.println("path: " + path);
							int branch_index = filtered_result[0][1]*2*4*3 + filtered_result[0][2]*4*3 + (filtered_result[0][3]-3)*3 + filtered_result[0][4];
							//System.out.println("branch index" + branch_index);
							int[] left_chars = f_lowercase.first_branch_chars[branch_index*2];
							int[] right_chars = f_lowercase.first_branch_chars[branch_index*2 + 1];
							
							StrokeNetwork[] classifiers = new StrokeNetwork[filtered_result[1].length];
							for (int i=0; i < filtered_result[1].length; i++) {
								//System.out.println("char : " + filtered_result[1][i]);
								classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + filtered_result[1][i] + "__" + "2" + "_" + path + "1.0.0.0.__10.net");
							}
							for (int i=0; i < left_chars.length; i++) {
								if (debug_classification)
									System.out.println("left char (0): " + left_chars[i]);
							}
							for (int i=0; i < right_chars.length; i++) {
								if (debug_classification)
									System.out.println("right char (0): " + right_chars[i]);
							}
							boolean branch_right = Compare_Classifiers.compare_branches(character, left_chars, right_chars, classifiers, filtered_result[1]);
							int classification =-1;
							if (branch_right) {
								path += "1.";
								classifiers = new StrokeNetwork[right_chars.length];
								if (debug_classification)
									System.out.println("went right");
				
								for (int i=0; i < right_chars.length; i++) {
									classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + right_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
								}
								classification = Compare_Classifiers.get_classification(character, classifiers, right_chars);
								if (debug_classification)
									System.out.println("classification: " + classification);
				
								
							} else {
								path += "0.";
								if (debug_classification)
									System.out.println("went left");
								classifiers = new StrokeNetwork[left_chars.length];
				
				
								for (int i=0; i < left_chars.length; i++) {
									classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + left_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
								}
								if (debug_classification)
									System.out.println("classification: "  + classification);
								classification = Compare_Classifiers.get_classification(character, classifiers, left_chars);
				
							}
							
							letter_1[0] = Lookup_Char.convert_lowercase_index("" + classification);
							symbol += Lookup_Char.lookup(letter_1[0], false);
						}
					}
				} else {
					System.out.println("lowercase letter(2): "  + character.minX + ", " + character.maxX + ", " + character.minY + ", " + character.maxY);
		
					/*
					 * 
					 * 
					 */
					int[][] filtered_result = f_lowercase.filter(character);
					String path = "";
					for (int i=0; i < filtered_result[0].length; i++) {
						path += filtered_result[0][i] + "." ;
					}
					if (debug_classification)
						System.out.println("path: " + path);
					int branch_index = filtered_result[0][1]*2*4*3 + filtered_result[0][2]*4*3 + (filtered_result[0][3]-3)*3 + filtered_result[0][4];
					//System.out.println("branch index" + branch_index);
					int[] left_chars = f_lowercase.first_branch_chars[branch_index*2];
					int[] right_chars = f_lowercase.first_branch_chars[branch_index*2 + 1];
					
					StrokeNetwork[] classifiers = new StrokeNetwork[filtered_result[1].length];
					for (int i=0; i < filtered_result[1].length; i++) {
						//System.out.println("char : " + filtered_result[1][i]);
						classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/" + filtered_result[1][i] + "__" + "2" + "_" + path + "1.0.0.0.__10.net");
					}
					for (int i=0; i < left_chars.length; i++) {
						if (debug_classification)
							System.out.println("left char (0): " + left_chars[i]);
					}
					for (int i=0; i < right_chars.length; i++) {
						if (debug_classification)
							System.out.println("right char (0): " + right_chars[i]);
					}
					boolean branch_right = Compare_Classifiers.compare_branches(character, left_chars, right_chars, classifiers, filtered_result[1]);
					int classification =-1;
					if (branch_right) {
						path += "1.";
						classifiers = new StrokeNetwork[right_chars.length];
						if (debug_classification)
							System.out.println("went right");
		
						for (int i=0; i < right_chars.length; i++) {
							classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + right_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
						}
						classification = Compare_Classifiers.get_classification(character, classifiers, right_chars);
						if (debug_classification)
							System.out.println("classification: " + classification);
		
						
					} else {
						path += "0.";
						if (debug_classification)
							System.out.println("went left");
						classifiers = new StrokeNetwork[left_chars.length];
		
		
						for (int i=0; i < left_chars.length; i++) {
							classifiers[i] = StrokeNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/filtered/level_2/" + left_chars[i] + "_2_" + path + "1.0.0.0.__10.net");
						}
						if (debug_classification)
							System.out.println("classification: "  + classification);
						classification = Compare_Classifiers.get_classification(character, classifiers, left_chars);
		
					}
					
					letter_1[0] = Lookup_Char.convert_lowercase_index("" + classification);
					symbol += Lookup_Char.lookup(letter_1[0], false);
				}
			}

		} else if (index == 2) { // third letter, lowercase
			character = skeleton.entities_in_atom.get(get_leftmost_char(letter_0[1], letter_1[1]));
		}
		
	}
	public int get_leftmost_char(int skip1, int skip2) { // use -1, -1 to skip nothing.
		Iterator<StrokeList> iterate_chars = skeleton.entities_in_atom.iterator();
		StrokeList this_char;
		int min_x = Integer.MAX_VALUE;
		int min_index = -1;
		for (int i=0; i < skeleton.entities_in_atom.size(); i++) {
			this_char = iterate_chars.next();
			if (i != skip1 && i != skip2) {
				if (!this_char.getStrokes().getFirst().checkIfDot()) {
					if (this_char.minX < min_x) {
						min_x = this_char.minX;
						min_index = i;
					}
				}
			}
		}
		return min_index;
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
		position_y_java = -1*position_y;
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
		System.out.println("symbol: " + symbol);
		print_area.atoms_to_draw.add(symbol);
		

		
		
		//int center_of_this_atom_x = x + atomLength/2;
		//int center_of_this_atom_y = y + atomLength/2;
			
		int center_of_this_atom_x = x + (int)(bondLength*1.2);
		int center_of_this_atom_y = y - (int)(font.getSize()*.4);
		
		electrodes = new Integer[0];
		set_electrons();
		//System.out.println("Number of electrons: " + electrodes.length);
		for (int k = 0; k < electrodes.length; k++) {
			//System.out.println("electron " + k + ": " + electrodes[k]);
		}
		
		print_area.electrons_to_draw.add(electrodes);

		print_area.paintComponent(print_area.getGraphics());

		/*
		 * TODO allow for dynamic adjusting of length, so that bonds can be properly formatted.
		 */
		int left_atom_length = 2;
		
		
		Iterator<Bond> bond_iterator = bonds.iterator();
		Bond this_bond;
		while (bond_iterator.hasNext()) {
			this_bond = bond_iterator.next();
			this_bond.print(font, bondLength, atomLength, g2d, center_of_this_atom_x, center_of_this_atom_y, print_area, left_atom_length);
			this_bond.next.print(font, bondLength, atomLength, atoms_above, atoms_to_left, mid_height_atom, mid_width_atom, center_panel_x, center_panel_y, g2d, print_area);
		}		
		
		
	}
	public void set_characters() throws NumberFormatException, IOException {
		classify_atom();
		Iterator<Bond> bond_iterator = bonds.iterator();
		for (int i=0; i < bonds.size(); i++) {
			bond_iterator.next().next.set_characters();
		}
	}
	private void set_electrons() {
		/*
		 * 				[0][1][2]
		 * 				[7]   [3]
		 *              [6][5][4]
		 * 
		 */
		int height = skeleton.maxY - skeleton.minY;
		int width = skeleton.maxX - skeleton.minX;
		System.out.println("height/width: " + height + ", " + width + ":: corners: " + skeleton.minX + ", " + skeleton.minY + " ;; " + skeleton.maxX + ", " +  skeleton.maxY);
		int y_0 = height/4 + skeleton.minY; // top 3rd
		int y_2 = skeleton.maxY - height/4; // bottom 3rd
		int x_0 = width/4 + skeleton.minX; // left 3rd
		int x_2 = skeleton.maxX - width/4; // right 3rd
		
		
		LinkedList<Integer> electrons = new LinkedList<Integer>();
		
		for (StrokeList s : skeleton.entities_in_atom) {
			if (s.id != 8) {
				if (s.getStrokes().getFirst().checkIfDot()) {
					//System.out.println("electron position: " + s.maxX + ", " + s.maxY + ": " + x_2 + ", " +  y_2 + " :: " + x_0 + ", " + y_0);
					if (s.maxY <= y_0) {
						if (s.maxX <= x_0) { // top left
							electrons.add(0);
						} else if (s.maxX >= x_2) { // top right
							electrons.add(2);
						} else {
							electrons.add(1);  // top middle
						}
					} else  if (s.maxY >= y_2) {
						if (s.maxX <= x_0) { // bottom left
							electrons.add(6);
						} else if (s.maxX >= x_2) { // bottom right
							electrons.add(4);
						} else { // bottom middle
							electrons.add(5);
							//System.out.println("electron position: " + s.maxX + ", " + s.maxY + ": " + x_2 + ", " +  y_2);
						}
					} else {
						if (s.maxX <= x_0) { // middle left
							electrons.add(7);
						} else if (s.maxX > x_2) { // middle right
							electrons.add(3);
						}
					}
					
					int[] electrons_array = new int[electrons.size()];
					int index=0;
					for (Integer e : electrons) {
						electrons_array[index] = e;
						index++;
					}
					
					// find pairs
					LinkedList<Integer> electron_pairs = new LinkedList<Integer>();
					for (int i=0; i < electrons_array.length; i++) {
						if (electrons_array[i] != -1) {
							for (int j=0; j < electrons_array.length; j++) {
								if (j != i) {
									if (electrons_array[i] == electrons_array[j]) {
										electron_pairs.add(electrons_array[i]);
										electrons_array[i] = -1;
										electrons_array[j] = -1;
										break;
									}
								}
							}
						}
					}
					electrodes = new Integer[electron_pairs.size()];
					this.electrons = electron_pairs.size()*2;
					index=0;
					for (int i : electron_pairs) {
						electrodes[index] = i;
						index++;
					}
				}
			}
		}
		if (electrodes.length == 0) {
			electrodes = new Integer[1];
			electrodes[0] = -1;
		}
	}
	
	protected LinkedList<Bond> get_bonds() {
		return bonds;
	}

	protected Atom copy() {
		Atom a = new Atom();
		a.electrons = electrons;
		a.electrodes = electrodes;
		a.letter_0 = letter_0;
		a.letter_1 = letter_1;
		a.letter_2 = letter_2;
		a.name = name;
		a.position_x = position_x;
		a.position_y = position_y;
		a.symbol = symbol;
		a.mark = mark;
		a.position_y_java = position_y_java;

		return a;
	}

	
}
