package auto_grader;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import auto_grader.screen_panels.Electron_Margin_Finder;

import chemistry.Molecule;
import dictionary.Lookup_Char;

public class Print_Area extends JPanel {
	private Molecule molecule;
	private int panel_height;
	private int panel_width;
	private int molecule_height;
	private int molecule_width;
	private int font_size;
	private int electron_margin;
	private int electron_diameter;
	
	public LinkedList<String> atoms_to_draw;
	public boolean drawing;
	public LinkedList<Integer> atoms_x;
	public LinkedList<Integer> atoms_y;
	public LinkedList<int[]> bonds;
	public LinkedList<Integer[]> electrons_to_draw;
	private boolean erase_all;
	
	public int correct =0;
	
	
	
	public Print_Area() {
		setBackground(new Color(240, 230, 230));
        this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        drawing = false;
	}
	public void initialize_drawing() {
		atoms_to_draw = new LinkedList<String>();
		atoms_x = new LinkedList<Integer>();
		atoms_y = new LinkedList<Integer>();
		bonds = new LinkedList<int[]>();
		electrons_to_draw = new LinkedList<Integer[]>();

	}
	public void clear_molecule() {
		if (atoms_to_draw != null) {
			atoms_to_draw.clear();
		}
		if (atoms_x != null) {
			atoms_x.clear();
		}
		if (atoms_y != null) {atoms_y.clear();
		
		}
		if (bonds != null) {
			bonds.clear();
		}
		
		erase_all = true;
		paintComponent(getGraphics());
		erase_all = false;
	}
	public void print_molecule(Molecule molecule) {
		this.molecule = molecule;
  		
        panel_height = this.getHeight();
        panel_width = this.getWidth();
        
  		molecule_height = molecule.get_height();
  		molecule_width = molecule.get_width();
  		drawing = false;
		paintComponent(getGraphics());
	}
	
	  public void paintComponent(Graphics g) {
		  super.paintComponent(g);
		  	if (erase_all) {
			    Graphics2D g2d = (Graphics2D) g;
			    g2d.setColor(new Color(240, 230, 230));
			    g2d.fillRect(0, 0, panel_width-1, panel_height-1);
		  	}
		  	if (drawing) {
			    Graphics2D g2d = (Graphics2D) g;
			    Font font = new Font("Serif", Font.PLAIN, font_size);

			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			    
			    AttributedString as1;
			    System.out.println("to draw: " + atoms_to_draw.size());
			    Iterator<String> to_draw_iter = atoms_to_draw.iterator();
			    Iterator<Integer> atoms_x_iter = atoms_x.iterator();
			    Iterator<Integer> atoms_y_iter = atoms_y.iterator();
			    
			    Iterator<Integer[]> electrons_iter = null;
			    try {
			    	electrons_iter = electrons_to_draw.iterator();
			    } catch (NullPointerException e) {
			    	//e.printStackTrace();
			    } catch (NoSuchElementException e) {
			    	
			    }
			    String atom_string = "";
			    while (to_draw_iter.hasNext()) {
			    	//as1 = new AttributedString(to_draw_iter.next() + "A");
			    	atom_string = to_draw_iter.next();
			    	as1 = new AttributedString(atom_string);
			    	
			    	int x = atoms_x_iter.next();
			    	int y = atoms_y_iter.next();
			    	
				    as1.addAttribute(TextAttribute.FONT, font);
			    	g2d.drawString(as1.getIterator(), x, y);
			    	
			    	Integer[] electrons = new Integer[0];
			    	try {
			    		electrons = electrons_iter.next();
			    	} catch (NullPointerException e) {
			    		//e.printStackTrace();
			    	} catch (NoSuchElementException e) {
			    		
			    	}

			    	for (int k =0; k < electrons.length; k++) {
			    		int el_y;
			    		int el_x;
			    		if (electrons[k] == 0) {
			    			el_y = y + font_size + 2*electron_margin;
					    	g2d.drawOval(x, y, electron_diameter, electron_diameter);
			    		} else if (electrons[k] == 1) {
			    			el_y = (y - (int)(0.75*font_size)) - 3*electron_margin;
			    			if (atom_string.length() == 1) {
				    			el_x = x + Electron_Margin_Finder.get_margin_top_bottom(atom_string)*electron_margin;
						    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
						    	el_x = el_x + 2*electron_margin;
						    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
			    			} else if (atom_string.length() == 2) {
				    			el_x = x + Electron_Margin_Finder.get_margin_top_bottom(atom_string)*electron_margin;
						    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
						    	el_x = el_x + 2*electron_margin;
						    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
			    			}
			    		} else if (electrons[k] == 3) {
			    			if (atom_string.length() == 1) {
			    				el_x = x + Electron_Margin_Finder.get_margin_right(atom_string)*electron_margin;
			    			} else if (atom_string.length() == 2) {
			    				el_x = x + Electron_Margin_Finder.get_margin_right(atom_string)*electron_margin;
			    			} else {
			    				el_x = x + 28*electron_margin;
			    			}
			    			el_y = y - 5*electron_margin;
					    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);

			    			el_y = el_y + 2*electron_margin;
					    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
					    	
			    		} else if (electrons[k] == 5) {
			    			el_y = y + 3*electron_margin;
			    			if (atom_string.length() == 1) {
				    			el_x = x + Electron_Margin_Finder.get_margin_top_bottom(atom_string)*electron_margin;
						    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
						    	el_x = el_x + 2*electron_margin;
						    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
			    			} else if (atom_string.length() == 2) {
				    			el_x = x + Electron_Margin_Finder.get_margin_top_bottom(atom_string)*electron_margin;
						    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
						    	el_x = el_x + 2*electron_margin;
						    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
			    			}	
			    		} else if (electrons[k] == 7) {
		    				el_x = x - 3*electron_margin;
			    			el_y = y - 5*electron_margin;
					    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);
			    			el_y = el_y + 2*electron_margin;
					    	g2d.fillOval(el_x, el_y, electron_diameter, electron_diameter);

			    		}
			    	}
			    	if (correct == 1) {
			    		g2d.setColor(new Color(50, 205, 50));
			    		g2d.fillOval(panel_width - 100, 25, 75, 75);
					    g2d.setColor(new Color(240, 230, 230));
					    g2d.fillOval(panel_width - 85, 40, 45, 45);
					    g2d.setColor(Color.black);
					    

			    	} else if (correct == -1) {
			    		g2d.setColor(Color.RED);
			    		g2d.setStroke(new BasicStroke(10));
			    		g2d.drawLine(panel_width - 100, 25, panel_width - 25, 100);
			    		g2d.drawLine(panel_width - 100, 100, panel_width - 25, 25);
			    		g2d.setColor(Color.black);
			    		
			    	}
			    	
			    	//g2d.drawOval(x, y, width, height)
			    }
			    int stroke_width = font_size/20;
			    if (stroke_width < 2) {
			    	stroke_width = 1;
			    }
			    g2d.setStroke(new BasicStroke(stroke_width));
			    Iterator<int[]> bonds_iterator = bonds.iterator();
			    int[] coords;
			    while (bonds_iterator.hasNext()) {
			    	coords = bonds_iterator.next();
			    	g2d.drawLine(coords[0], coords[1], coords[2], coords[3]);
			    	
			    }	    
		  	}
		  	else if (molecule != null) {

		  		
		  		int atom_height = panel_height / molecule_height;
		  		int atom_width = panel_width / molecule_width;
		  		
		  		
		  		int atom_min_dim = atom_height;
		  		if (atom_height > atom_width) {
		  			atom_min_dim = atom_width;
		  		}
		  		font_size = atom_min_dim / 2;
		  		int bond_length = (int)(atom_min_dim/3.5);
		  		int atom_length = atom_min_dim;
		  		
		  		electron_margin = font_size / 10;
		  		electron_diameter = font_size / 20;
		  		
		  		

			    Graphics2D g2d = (Graphics2D) g;

			    Font font = new Font("Serif", Font.PLAIN, font_size);
		  		drawing = true;
			    molecule.print(font, bond_length, atom_length, g2d, this);
			    
		  	}
		    
		    
		    
		  }
		  
		  public int get_height() {
			  return panel_height;
		  }
		  public int get_width() {
			  return panel_width;
		  }
}
