package testing.data.character;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import line.LineConnect;

import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;
import trainingdata.character.ReadTrainingCharacters;

public class DisplayCharacter {
	static int index =-1;
	public static LinkedList<StrokeList> chars;
	public static Iterator<StrokeList> charIt;

    public static void createAndShowGUI() throws IOException {
		JFrame frame = new JFrame("title"); frame.setSize(1000, 1000);
		JPanel panel = new JPanel();
		JButton nextChar = new JButton("Next Character"); panel.add(nextChar);
		JButton prevChar = new JButton("Previous Character"); panel.add(prevChar);
		
		frame.setContentPane(panel);
		final DisplayPanel drawpanel = new DisplayPanel();
		frame.add(drawpanel); frame.pack(); frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		chars = ReadTrainingCharacters.getCharsFromFile("testingcharacters/0/3_3" +
				".ts");
		//chars = ReadTrainingCharacters.getCharsFromFile("trainingcharacters/choose_from_all/cap_6_7" +				".tr");
		//charIt = chars.iterator();
		
		
		
		nextChar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StrokeList character = returnNextChar();
				Iterator<Stroke> strokeIt = character.getStrokes().iterator();
				System.out.println(index);
				
				LinkedList<Coordinate> coords = new LinkedList<Coordinate>();
				
				while (strokeIt.hasNext()) {
					coords.addAll(strokeIt.next().getCoordinates());
				}
				drawpanel.ink2(coords);
			}
		});
		
		prevChar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StrokeList character = returnPrevChar();
				Iterator<Stroke> strokeIt = character.getStrokes().iterator();
				System.out.println(index);
				
				LinkedList<Coordinate> coords = new LinkedList<Coordinate>();
				
				while (strokeIt.hasNext()) {
					coords.addAll(strokeIt.next().getCoordinates());
				}
				drawpanel.ink2(coords);
			}
		});
		
		
		
    }
    public static StrokeList returnNextChar() {
    	index++;
    	return chars.get(index);
    	 
    }
    public static StrokeList returnPrevChar() {
    	index--;
    	return chars.get(index);
    	
    }
    
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}
    
}

class DisplayPanel extends JPanel {
	private int panelwidth = 1000; public int getPanelWidth() {return panelwidth;}
	private int panelheight = 800; public int getPanelHeight() {return panelheight;}
	private boolean erase = false; public void setEraser() {erase=true;} public void setPencil() {erase=false;}
	private boolean all = false; private boolean first = true;
	private  int squareX = -1; private  int squareY = -1; private  int squareW = 1; private  int squareH = 1;
    private int[][] image = new int[panelwidth][panelheight]; public int[][] getImage() {return image;}
    private int x; private int y; 
    private int prevX; private int prevY;
    public Stroke thisStroke; public StrokeList strokeList = new StrokeList();
    boolean mouseDown = false; private long strokeStartTime;
    
    LinkedList<Coordinate> coordsToPaint;
    
    public DisplayPanel() {
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
                System.out.println(x + ", " + y);
            }
            public void mouseReleased(MouseEvent e) {
            	strokeList.addStroke(thisStroke);
            	thisStroke.terminate();
            	mouseDown = false;
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
                ink(x,y);
                image[x][y]=1;
                if (prevX != x || prevY != y) {
                	connectPoints(prevX, prevY, x, y);
                }
                thisStroke.addCoordinate(x,y, System.currentTimeMillis() - strokeStartTime);
                prevX = e.getX(); prevY = e.getY();
            }
        });
    } 
    
    public void ink(int x, int y) {
        int OFFSET = 1;
        squareX = x;
        squareY = y;
        
        repaint(x,y,squareW + OFFSET, squareH + OFFSET);
    }
    
    public void ink2(LinkedList<Coordinate> character) {
    	coordsToPaint = character;
    	repaint();
    }
    
    
    
    
    public void inkall() {all = true;}

    public void connectPoints(int x, int y, int lastX, int lastY) {
    	LinkedList<Coordinate> coordinates = LineConnect.connect(x, y, lastX, lastY);
    	Iterator<Coordinate> coordinateIterator = coordinates.iterator();
    	Coordinate nextCoord;
    	while (coordinateIterator.hasNext()) {
    		nextCoord = coordinateIterator.next();
    		image[nextCoord.getX()][nextCoord.getY()] = 1;
    	}
    }
    public Dimension getPreferredSize() {
        return new Dimension(panelwidth, panelheight);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  
        
    	if (!erase || first) {g.setColor(Color.BLACK);}
    	else {g.setColor(Color.WHITE);}
    	
        if (!all) {
        	g.fillRect(squareX,squareY,squareW,squareH);
        	g.drawRect(squareX,squareY,squareW,squareH);
        } else {
        	for (int i=0; i<panelheight; i++) {
        		for (int j=0; j<panelwidth; j++) {
        			if (image[i][j] == 1) {
        				g.fillRect(j, i, squareW, squareH);
        				g.drawRect(j, i, squareW, squareH);
        			}
        		}
        	}
        	all = false;
        }
        if (coordsToPaint != null) {
        	Iterator<Coordinate> coordsIt = coordsToPaint.iterator();
        	Coordinate thisCoord;
        	while (coordsIt.hasNext()) {
        		thisCoord = coordsIt.next();
        		g.drawLine(thisCoord.getX(), thisCoord.getY(), thisCoord.getX(), thisCoord.getY());
        	}
        }
    }  
}

