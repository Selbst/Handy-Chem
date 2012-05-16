/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_grader;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;

import chemistry.Molecule;

import line.LineConnect;
import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;
import structural_synthesis.Synthesize_Molecule;

/**
 *
 * @author selbst
 */
public class Drawing_Area extends javax.swing.JPanel {
	private int panelwidth = 1872; 
	public int getPanelWidth() {return panelwidth;}
	private int panelheight = 642;
	public int getPanelHeight() {return panelheight;}
	private boolean erase = false; 
	public void setEraser() {erase=true;} 
	public void setPencil() {erase=false;}
	private boolean all = false; 
	private boolean first = true;
	private int squareX = -1; 
	private int squareY = -1; 
	private int squareW = 6; 
	private int squareH = 6;
    private int[][] image = new int[panelwidth][panelheight]; 
    public int[][] getImage() {return image;}
    private int x; 
    private int y; 
    private int prevX; 
    private int prevY;
    public Stroke thisStroke; 
    private StrokeList strokeList = new StrokeList();
    public StrokeList get_stroke_list() {
    	return strokeList;
    }
    boolean mouseDown = false; 
    private long strokeStartTime;
    
    boolean drawRect = false;
    public int boundaryStartX = -1; 
    public int boundaryStartY = -1;
    public int boundaryEndX = -1;
    public int boundaryEndY = -1;
    public int leftX = -1;
    public int topY = -1;
    public int rightX = -1;
    public int bottomY = -1;
    int counter = 0;
    
	public boolean startXisMin = false;
	public boolean startYisMin = false;
	public int width;
	public int height;
	private boolean erase_all;
    public LinkedList<Coordinate> toPaint = new LinkedList<Coordinate>(); boolean coordPaint = false;
    public Print_Area printing_area;
	public void clear_stroke_list() {
		strokeList = new StrokeList();
	}
	private Molecule m;
	public Molecule get_molecule() {
		return m;
	}
    /**
     * Creates new form Drawing_Area
     */
	public Drawing_Area(final Print_Area printing_area) {
        initComponents();
        this.printing_area = printing_area;
        for (int i=0; i<panelwidth; i++) {
        	for (int j=0; j<panelheight; j++) {
        		image[i][j]=0;
        	}
        }
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { first = false;
            	x=e.getX(); y=e.getY();
                ink(x,y);
                image[x][y]=1;
                thisStroke = new Stroke(x, y); strokeStartTime = System.currentTimeMillis();
                mouseDown = true;
                prevX = e.getX(); prevY = e.getY();
                repaint();
            }
            public void mouseReleased(MouseEvent e) {
            	strokeList.addStroke(thisStroke);
            	thisStroke.terminate();
            	mouseDown = false;
    			StrokeList temp = strokeList.copy();
    			//System.out.println("number of strokes (pre): " + strokeList.getStrokes().size());
        		try {
        			printing_area.correct = 0;
        			m = Synthesize_Molecule.synthesize(strokeList);
        			
        			
        	    	int[] dimensions = m.set_dimensions();

        	    	int height = m.get_height();
        	    	int width = m.get_width();
        	    	
        	    	printing_area.print_molecule(m);
        	    	
        		} catch (NumberFormatException x) {
        			x.printStackTrace();
        		} catch (IOException x) {
        			x.printStackTrace();
        		} catch (NullPointerException x) {
        			x.printStackTrace();
        		}
        		
    	    	strokeList = temp;
    			//System.out.println("number of strokes (post): " + strokeList.getStrokes().size());


        		
            }
            public void mouseExited(MouseEvent e) {
            	if (mouseDown) {
            		strokeList.addStroke(thisStroke);
            	}
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
            	x=e.getX(); y=e.getY();
                //ink(x,y);
                image[x][y]=1;
                coordPaint = true;
                if (prevX != x || prevY != y) {
                	toPaint.addAll(connectPoints(prevX, prevY, x, y));
                }
                thisStroke.addCoordinate(x,y, System.currentTimeMillis() - strokeStartTime);
                prevX = e.getX(); prevY = e.getY();
                
                repaint();
                
            }
        });
    } 
    
    private void ink(int x, int y) {
        int OFFSET = 1;
        squareX = x;
        squareY = y;
        //repaint(x,y,squareW+OFFSET,squareH+OFFSET);
    }
    public void set_stroke_list(StrokeList this_mol) {
    	strokeList = this_mol;
    	image = new int[image.length][image[0].length];
    	for (int i = 0; i < this_mol.getStrokes().size(); i++) {
    		Stroke this_stroke = this_mol.getStrokes().get(i);
    		Iterator<Coordinate> coord_it = this_stroke.getCoordinates().iterator();
    		for (int j=0; j < this_stroke.getCoordinates().size(); j++) {
    			Coordinate this_coord = coord_it.next();
    			image[this_coord.getX()][this_coord.getY()] = 1;
    		}
    		
    	}
    	refresh_panel();
    }
    public void refresh_panel() {
    	repaint();
    }
    
    
    
    public void inkall() {all = true;}

    public LinkedList<Coordinate> connectPoints(int x, int y, int lastX, int lastY) {
    	LinkedList<Coordinate> coordinates = LineConnect.connect(x, y, lastX, lastY);
    	Iterator<Coordinate> coordinateIterator = coordinates.iterator();
    	Coordinate nextCoord;
    	while (coordinateIterator.hasNext()) {
    		nextCoord = coordinateIterator.next();
    		image[nextCoord.getX()][nextCoord.getY()] = 1;
    	}
    	
    	return coordinates;
    	
    	
    }
    public Dimension getPreferredSize() {
        return new Dimension(panelwidth, panelheight);
    }
	public void clear_molecule() {
		
		erase_all = true;
		paintComponent(getGraphics());
		erase_all = false;
		for (int i=0; i < image.length; i++) {
			for (int j=0; j < image[i].length; j++) {
				image[i][j] = 0;
			}
		}
		
	}
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  
        for (Stroke s : strokeList.getStrokes()) {
        	Iterator<Coordinate> c = s.getCoordinates().iterator();
        	while (c.hasNext()) {
        		Coordinate xy = c.next();
        		int x = xy.getX();
        		int y = xy.getY();
				g.fillRect(x, y, 6, 6);
				g.drawRect(x, y, 6, 6);
        	}
       
        }
        if (!coordPaint) {
        	if (!erase || first) {g.setColor(Color.BLACK);}
        	else {g.setColor(Color.WHITE);}
    	
        	if (!all) {
        		g.fillRect(squareX,squareY,squareW,squareH);
        		g.drawRect(squareX,squareY,squareW,squareH);
        	} else {
        		for (int i=0; i<panelheight; i++) {
        			for (int j=0; j<panelwidth; j++) {
        				if (image[i][j] == 1) {
        					
        					g.fillRect(j, i, 6, 6);
        					g.drawRect(j, i, 6, 6);
        				}
        			}
        		}
        		all = false;
        	}
        } else {
        	g.setColor(Color.BLACK);
        	Iterator<Coordinate> it = toPaint.iterator();
        	while (it.hasNext()) {
        		Coordinate thisCoord = it.next();
        		g.fillRect(thisCoord.getX(), thisCoord.getY(), 6, 6);
        		g.drawRect(thisCoord.getX(), thisCoord.getY(), squareW, squareH);
        	}
		    Graphics2D g2d = (Graphics2D) g;

        	for (int i=0; i < panelheight; i++) {
        		for (int j=0; j < panelwidth; j++) {
        			if (image[j][i] == 1) {
        			    g2d.setStroke(new BasicStroke(4));
        				g2d.drawLine(j, i, j, i);
        			}
        		}
        	}
        	
        	toPaint.clear();
        	coordPaint=false;
        }
        
        
    }  
        
        
   
    
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setFocusTraversalPolicyProvider(true);
        setMinimumSize(new java.awt.Dimension(1872, 642));
        setPreferredSize(new java.awt.Dimension(1872, 642));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1872, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 642, Short.MAX_VALUE)
        );
    }// </editor-fold>
    // Variables declaration - do not modify
    // End of variables declaration
}
