/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_grader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;

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
	private int squareW = 3; 
	private int squareH = 3;
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
	
	public void clear_stroke_list() {
		strokeList = new StrokeList();
	}
    /**
     * Creates new form Drawing_Area
     */
	public Drawing_Area() {
        initComponents();
        for (int i=0; i<panelwidth; i++) {
        	for (int j=0; j<panelheight; j++) {
        		image[i][j]=0;
        	}
        }
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { 
            	if (drawRect) {
            		boundaryStartX = e.getX();
            		boundaryStartY = e.getY();
            		
            	} else {
                	first = false;
                	x=e.getX(); y=e.getY();
                    ink(x,y);
                    image[x][y]=1;
                    thisStroke = new Stroke(x, y); strokeStartTime = System.currentTimeMillis();
                    mouseDown = true;
                    prevX = e.getX(); prevY = e.getY();
            	}

            }
            public void mouseReleased(MouseEvent e) {
            	if (drawRect) {
            		boundaryEndX = e.getX();
            		boundaryEndY = e.getY();

                	if (boundaryStartX < boundaryEndX) {
                		startXisMin = true;
                		leftX = boundaryStartX;
                		rightX = boundaryEndX;
                	} else {
                		leftX = boundaryEndX;
                		rightX = boundaryStartX;
                	}
                	if (boundaryStartY < boundaryEndY) {
                		startYisMin = true;
                		topY = boundaryStartY;
                		bottomY = boundaryEndY;
                	} else {
                		topY = boundaryEndY;
                		bottomY = boundaryStartY;
                	}
            		
                	width = Math.abs(boundaryStartX - boundaryEndX);
                	
                	height = Math.abs(boundaryStartY - boundaryEndY);
            		
                	if (startXisMin && startYisMin) { // top left start. bottom right end.	
                		repaint(boundaryStartX, boundaryStartY, width, 4); // top
                	}    		
            	} else {
                	strokeList.addStroke(thisStroke);
                	thisStroke.terminate();
                	mouseDown = false;
            	}
				strokeList.setCharacterRelativeCoordinates();
				            	
            }
            public void mouseExited(MouseEvent e) {
            	if (mouseDown) {
            		strokeList.addStroke(thisStroke);
            	}
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
            	if (!drawRect) {
            		x=e.getX(); y=e.getY();
            		ink(x,y);
            		image[x][y]=1;
            		if (prevX != x || prevY != y) {
            			connectPoints(prevX, prevY, x, y);
            		}
            		thisStroke.addCoordinate(x,y, System.currentTimeMillis() - strokeStartTime);
            		prevX = e.getX(); prevY = e.getY();
            	}
            }
        });
    } 
    
    private void ink(int x, int y) {
        int OFFSET = 1;
        squareX = x;
        squareY = y;
        repaint(x,y,squareW+OFFSET,squareH+OFFSET);
    }
    
    
    
    public void inkall() {all = true;}

    public void connectPoints(int x, int y, int lastX, int lastY) {
    	LinkedList<Coordinate> coordinates = LineConnect.connect(x, y, lastX, lastY);
    	Iterator<Coordinate> coordinateIterator = coordinates.iterator();
    	Coordinate nextCoord;
    	while (coordinateIterator.hasNext()) {
    		nextCoord = coordinateIterator.next();
    		image[nextCoord.getX()][nextCoord.getY()] = 1;
    		thisStroke.add(nextCoord);
    		//repaint(nextCoord.getX(), nextCoord.getY(), 1, 1);
    	}
    }
    public Dimension getPreferredSize() {
        return new Dimension(panelwidth, panelheight);
    }
    public void update (Graphics g)
    { paint(g); } 
    
	public void clear_molecule() {
		
		erase_all = true;
		paintComponent(getGraphics());
		erase_all = false;
		
	}
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  
        
        if (erase_all) {
        	g.setColor(Color.white);
        	g.fillRect(0, 0, panelwidth-1, height-1);
        }
        if (!erase || first) {g.setColor(Color.BLACK);}
        else {g.setColor(Color.WHITE);}
        	
        if (!all) {
        	g.fillRect(squareX,squareY,squareW,squareH);
        	g.drawRect(squareX,squareY,squareW,squareH);
        } else {
        		
        		
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
