package testing.data.character;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import pixels.binary.Entity;
import pixels.binary.Image;
import pixels.binary.ImageCompression;
import pixels.binary.PixelToEntity;

import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;

import line.Line;
import line.LineConnect;
import line.LineExtractor;


import recognition.features.Grid;
import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;

public class TestingGUI {
    public static void createAndShowGUI() {
		JFrame frame = new JFrame("title"); frame.setSize(1000, 1000);
		JPanel panel = new JPanel();
		JButton saveChar = new JButton("Save Character"); panel.add(saveChar);
		JButton clear = new JButton("Clear Character"); panel.add(clear);
		
		final JTextField id = new JTextField(5); panel.add(id);
		final JTextField file = new JTextField(20); panel.add(file);
		frame.setContentPane(panel);
		final MyPanel drawpanel = new MyPanel();
		frame.add(drawpanel); frame.pack(); frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		

		saveChar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Image windowImage = new Image(drawpanel.getImage());
				PixelToEntity Ent = new PixelToEntity(); 				
				Ent.mapEntities(windowImage);
				Entity thisEntity = Ent.entityImages.getLast();
				thisEntity.setImage();
				drawpanel.strokeList.setCharacterRelativeCoordinates();
				Grid g = new Grid(drawpanel.strokeList);
				System.out.println(g.toString());
				FileWriter fstream = null;
				try {  // add training character
					fstream = new FileWriter("testingcharacters/" + file.getText(), true);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write("char\n");								//          	"char"
					out.write(Integer.parseInt(id.getText()) + "\n"); // 				<id#>
					out.write(drawpanel.strokeList.getStrokes().size()+ "\n");  // 		<# of strokes>
					
					// write strokes to file.
					Iterator<Stroke> strokeIterator = drawpanel.strokeList.getStrokes().iterator(); Iterator<Coordinate> coordinateIterator;
					Stroke thisStroke; Coordinate thisCoordinate;
					while (strokeIterator.hasNext()) {
						thisStroke = strokeIterator.next();
						out.write(thisStroke.getMinX() + "\n");                 //		<min x of stroke>
						out.write(thisStroke.getMinY()  + "\n");				//		<min y of stroke>
						out.write(thisStroke.getMaxX()  + "\n");				// 		<max x of stroke>
						out.write(thisStroke.getMaxY()  + "\n");				// 		<max y of stroke>
						out.write(thisStroke.getCoordinates().size() + "\n"); //		<stroke i # of coordinates>
						out.write(thisStroke.metaCoordinates.size() + "\n");   	//		<stroke i # of meta coords>
						coordinateIterator = thisStroke.getCoordinates().iterator();
						while (coordinateIterator.hasNext()) {
							thisCoordinate = coordinateIterator.next();
							out.write(thisCoordinate.getX() + "\n");			//		<x coord>
							out.write(thisCoordinate.getY() + "\n");			//      <y coord>
							out.write(thisCoordinate.getT() + "\n");			// 		<time coord>
						}

						coordinateIterator = thisStroke.metaCoordinates.iterator();
						while (coordinateIterator.hasNext()) {
							thisCoordinate = coordinateIterator.next();
							String a = "areas : ";

							double[] areas = thisCoordinate.getAreas();
							a += areas[0] +", ";
							a += areas[1] + ", ";
							a += areas[2] + ", ";
							a += areas[3];
							System.out.println(areas);
							out.write(thisCoordinate.getXAvg() + "\n");			//		<x average of meta coord>
							out.write(thisCoordinate.getYAvg() + "\n");			// 		<y average of meta coord>
							out.write(areas[0] + "\n");							//		
							out.write(areas[1] + "\n");
							out.write(areas[2] + "\n");
							out.write(areas[3] + "\n");
	
						}
					}					
					
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				drawpanel.strokeList = new StrokeList();
			}
		});
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawpanel.strokeList = new StrokeList();
			}
		});
    }
    
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
    
}



class MyPanel extends JPanel {
	private int panelwidth = 1000; public int getPanelWidth() {return panelwidth;}
	private int panelheight = 800; public int getPanelHeight() {return panelheight;}
	private boolean erase = false; public void setEraser() {erase=true;} public void setPencil() {erase=false;}
	private boolean all = false; private boolean first = true;
	private int squareX = -1; private int squareY = -1; private int squareW = 6; private int squareH = 6;
    private int[][] image = new int[panelwidth][panelheight]; public int[][] getImage() {return image;}
    private int x; private int y; 
    private int prevX; private int prevY;
    public Stroke thisStroke; public StrokeList strokeList = new StrokeList();
    boolean mouseDown = false; private long strokeStartTime;
    
    public MyPanel() {
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
    }  
}
