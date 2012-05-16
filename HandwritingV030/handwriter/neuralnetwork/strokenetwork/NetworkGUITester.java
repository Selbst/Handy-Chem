package neuralnetwork.strokenetwork;

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

import line.Line;
import line.LineConnect;
import line.LineExtractor;


import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;

public class NetworkGUITester {
    public static void createAndShowGUI() {
		JFrame frame = new JFrame("title"); frame.setSize(1000, 1000);
		JPanel panel = new JPanel();
		JButton linetest = new JButton("Line Test"); panel.add(linetest);
		JButton classify = new JButton("Classify"); panel.add(classify);
		JButton pixeltest = new JButton("Pixel Test"); panel.add(pixeltest);
		
		final JTextField id = new JTextField(5); panel.add(id);

		frame.setContentPane(panel);
		final MyPanel drawpanel = new MyPanel();
		frame.add(drawpanel); frame.pack(); frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		pixeltest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		linetest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Stroke lastStroke = drawpanel.strokeList.getStrokes().getLast();
				LinkedList<Coordinate> relativeCoordinates = lastStroke.getRelativeCoordinates();
				relativeCoordinates = LineExtractor.removeDuplicateCoordinates(relativeCoordinates);
				//System.out.println(relativeCoordinates.toString());
				Line line = Line.regressionLine(relativeCoordinates);
				line.setMeanError(Line.findMeanError(relativeCoordinates, line));
				line.setClassification();
				//System.out.println(line.toStringWithError() + "\n");
				//System.out.println("Classification: \n" + line.getClassification().toString());
				lastStroke.segment();
				int ID = Integer.parseInt(id.getText());
				lastStroke.setID(ID);
				lastStroke.setMetaCoordinates();

				Iterator<Coordinate> iterator = lastStroke.metaCoordinates.listIterator();
				FileWriter fstream = null;
				
				
				try {  // add line to training set.
					fstream = new FileWriter("stroketrainingset/stroke8/set1.tr", true);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(ID + "\n");
					int count = 0;
					while (iterator.hasNext()) {
						double[] areas = iterator.next().getAreas();
						out.write(areas[0] + "\n");
						out.write(areas[1] + "\n");
						out.write(areas[2] + "\n");
						out.write(areas[3] + "\n");
						count+=4;
					}
					for (int i =count; i < 80; i++) {
						out.write(0 + "\n");
					}
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
			}
		});
		
		classify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Stroke lastStroke = drawpanel.strokeList.getStrokes().getLast();
				LinkedList<Coordinate> relativeCoordinates = lastStroke.getRelativeCoordinates();
				relativeCoordinates = LineExtractor.removeDuplicateCoordinates(relativeCoordinates);
				//System.out.println(relativeCoordinates.toString());
				Line line = Line.regressionLine(relativeCoordinates);
				line.setMeanError(Line.findMeanError(relativeCoordinates, line));
				line.setClassification();
				//System.out.println(line.toStringWithError() + "\n");
				//System.out.println("Classification: \n" + line.getClassification().toString());
				lastStroke.segment();
				
				
				if (lastStroke.checkIfDot() == false) {
					lastStroke.setMetaCoordinates();
					Iterator<Coordinate> postIterate = lastStroke.metaCoordinates.iterator();
					while (postIterate.hasNext()) {
						System.out.println(postIterate.next().metaCoordToString());
					}
					
					try {
						StrokeNetwork[][] nets = new StrokeNetwork[9][5];
						for (int i=0; i < nets.length; i++) {
							for (int j=0; j < nets[i].length; j++) {
								nets[i][j] = StrokeNetworkBuilder.fileToNetwork("networkmodels/stroke" + i + "/model" + j + "0");
							}
						}
						double[] input = new double[80];
						Iterator<Coordinate> iterator = lastStroke.metaCoordinates.listIterator();
						int counter=0;
						while (iterator.hasNext()) {
							double[] areas = iterator.next().getAreas();
							input[counter] = areas[0]/100; input[counter+1] = areas[1]/100;
							input[counter+2] = areas[2]/100; input[counter+3] = areas[3]/100;
							counter+=4;
						}
						double[][] outputs = new double[nets.length][nets[0].length];
						for (int ii=0; ii < outputs.length; ii++) {
							for (int jj=0; jj < outputs[ii].length; jj++) {
								outputs[ii][jj] = StrokeNetworkBuilder.getOutput(input, nets[ii][jj]);
								System.out.println("output stroke "+ii +": model " + jj + ": " + outputs[ii][jj]);
							}	
						}
						double[] outputSums = new double[nets.length];
						for (int k=0; k < outputSums.length; k++) {
							for (int z=0; z < outputs[k].length; z++) {
								if (z==0) {outputSums[k] = outputs[k][z];}
								else {outputSums[k] += outputs[k][z];}
							}
						}
						double max = Double.NEGATIVE_INFINITY; int maxID =-1;
						for (int i=0; i < outputSums.length; i++) {
							if (outputSums[i] > max) {
								max = outputSums[i]; maxID = i;
							}
						}
						for (int h=0; h < outputSums.length; h++) {
							System.out.println("stroke " + h + " weight: " + outputSums[h]);
						}
						System.out.println("classified as : " + maxID + " with weight: " + max);
					
						StrokeNetwork newnet = StrokeNetworkBuilder.fileToNetwork("networkmodels/stroke" + 0 + "/model1" + 0);
						double newoutput = StrokeNetworkBuilder.getOutput(input, newnet);
						System.out.println("new classifier1 output: " + newoutput);
						newnet = StrokeNetworkBuilder.fileToNetwork("networkmodels/stroke" + 0 + "/model2" + 0);
						newoutput = StrokeNetworkBuilder.getOutput(input, newnet);
						System.out.println("new classifier2 output: " + newoutput);

					
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					System.out.println("stroke is a dot");
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
    private int[][] image = new int[panelwidth][panelheight];
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
