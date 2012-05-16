package neuralnetwork.pixels;

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

import line.Line;
import line.LineConnect;
import line.LineExtractor;


import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;

public class PixelTrainingSetMaker {
    public static void createAndShowGUI() {
		JFrame frame = new JFrame("title"); frame.setSize(1000, 1000);
		JPanel panel = new JPanel();
		JButton classify = new JButton("Classify"); panel.add(classify);
		JButton pixeltest = new JButton("Pixel Test"); panel.add(pixeltest);
		
		final JTextField id = new JTextField(5); panel.add(id);

		frame.setContentPane(panel);
		final MyPanel drawpanel = new MyPanel();
		frame.add(drawpanel); frame.pack(); frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		

		
		pixeltest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[][] skeleton = drawpanel.getImage();
				System.out.println("height: " + skeleton.length);
				System.out.println("width: " + skeleton[0].length);
				
				Image imageImage = new Image(skeleton);
				
				PixelToEntity Ent = new PixelToEntity(); 				
				Ent.mapEntities(imageImage);
				Entity thisEntity = Ent.entityImages.getLast();
				
				thisEntity.setImage();
				
				thisEntity.image = ImageCompression.fatten(thisEntity.image, 1);
				
				thisEntity.compressedImage = ImageCompression.compress(thisEntity.image);
				
				int[][] standardized = thisEntity.compressedImage;
				
				
				int ID = Integer.parseInt(id.getText()); thisEntity.ID = ID;

				
				for (int i=0; i < thisEntity.image.length; i++) {
					for (int j=0; j < thisEntity.image[i].length; j++) {
						System.out.print(thisEntity.image[i][j]);
					}
					System.out.println();
				}
				/*for (int i=0; i < image.length; i++) {
					for (int j=0; j < image[i].length; j++) {
						System.out.print(image[i][j]);
					}
				}*/
				
				
				FileWriter fstream = null;
				
				try {  // add line to training set.
					fstream = new FileWriter("pixeltrainingset/entity0/set0.tre", true);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(ID + "\n");
					for (int i=0; i < standardized.length; i++) {
						for (int j=0; j < standardized[0].length; j++) {
							System.out.print(standardized[i][j]);
						}
						System.out.println();
					}

					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
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
    boolean mouseDown = false;
    
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
                thisStroke = new Stroke(x, y);
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
                thisStroke.addCoordinate(x,y);
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
    		//ink(nextCoord.getX(), nextCoord.getY());
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
