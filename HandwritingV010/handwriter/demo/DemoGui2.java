package demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import chemistry.Molecule;

import contiguities.Contiguities;

import pixels.binary.Entity;
import pixels.binary.Image;
import pixels.binary.ImageCompression;
import pixels.binary.PixelToEntity;

import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;

import line.Line;
import line.LineConnect;
import line.LineExtractor;


import recognition.features.BinaryGridNetworkTrainer;
import recognition.features.Grid;
import recognition.features.GridNetworkBuilder;
import recognition.features.TrainingSet;
import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;
import structural_synthesis.Bond_Connect;
import structural_synthesis.Synthesize_Molecule;

public class DemoGui2 {
	
	public StrokeNetwork[][] nets;
	public DemoGui2() throws NumberFormatException, IOException {
		nets = new StrokeNetwork[6][];
		
		
		//StrokeNetwork[] a = new StrokeNetwork[1]; 
		//StrokeNetwork[] b = new StrokeNetwork[1]; 
		StrokeNetwork[] c = new StrokeNetwork[1];
		//StrokeNetwork[] d = new StrokeNetwork[1]; 
		//StrokeNetwork[] e = new StrokeNetwork[1];
		//StrokeNetwork[] f = new StrokeNetwork[1];
		StrokeNetwork[] h = new StrokeNetwork[1];
		
		/*
			a[0] = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testeraa.1.0.0.1.__10.net");
			b[0] = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testerbb.0.1.0.1.__10.net");
			c[0] = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testercc.1.0.0.1.__10.net");
			d[0] = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testerdd.0.1.0.1.__10.net");
			e[0] = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testeree.1.0.0.1.__10.net");
			f[0] = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/testerff.1.0.0.1.__10.net");
			
			
			
			nets[0] = a;
			nets[1] = b;
			nets[2] = c;
			nets[3] = d;
			nets[4] = e;
			nets[5] = f;
			*/
		
		c[0] = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/c_vs_h(C).1.0.0.1.__10.net");
		h[0] = GridNetworkBuilder.fileToNetwork("trainingcharacters/classifiers/grid/1/c_vs_h(H).1.0.0.1.__10.net");
		nets[0] = c;
		nets[1] = h;
	}
	
    public static void createAndShowGUI() {
		JFrame frame = new JFrame("title"); frame.setSize(1000, 1000);
		JPanel panel = new JPanel();
		JButton classifyChar = new JButton("Classify Character"); panel.add(classifyChar);
		JButton clear = new JButton("Clear Character"); panel.add(clear);
		JButton classifySelectedChar = new JButton("Classify Selected Characters"); panel.add(classifySelectedChar);
		
		JButton classifyMol = new JButton("Classify Molecule"); panel.add(classifyMol);
		
		JButton selector = new JButton("Select Character"); panel.add(selector);

		
		final JTextField file = new JTextField(20); 
		panel.add(file);

		
		frame.setContentPane(panel);
		final MyPanel2 drawpanel = new MyPanel2();
		frame.add(drawpanel); frame.pack(); frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);




		classifyChar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DemoGui demo = null;
				try {
					demo = new DemoGui();
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				drawpanel.strokeList.setCharacterRelativeCoordinates();
				Grid g = new Grid();
				double[] inputs = g.getNetworkInputs(drawpanel.strokeList);
				StrokeNetwork thisNet;
				double[] outputs = new double[5];
				
				for (int i=0; i < demo.nets.length-1; i++) {
					if (i != 4) {
						System.out.println(i);
						
					for (int j=0; j < demo.nets[i].length; j++) {
						
						outputs[i] += GridNetworkBuilder.getOutput(inputs, demo.nets[i][j]);
						//System.out.println(outputs[i]);
					}
					outputs[i] = outputs[i] / demo.nets[i].length;
					}
				}
				for (int i=0; i < outputs.length; i++) {
					System.out.println("output " + i + ": " + outputs[i]);
					
				}
				int max = BinaryGridNetworkTrainer.findMax(outputs);
				String character = "-1";
				if (max == 0) {
					character = "a";
				} else if (max == 1) {
					character = "b";
				} else if (max == 2) {
					character = "c";
				} else if (max == 3) {
					character = "d";
				} else if (max == 4) {
					character = "e";
				} else if (max == 5) {
					character = "f";
				}
				
				
				file.setText("did you write " + character + "?");
				drawpanel.strokeList = new StrokeList();
			}
		});
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawpanel.strokeList = new StrokeList();
			}
		});
		
		classifySelectedChar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				String characters = ""; 
				DemoGui demo = null;
				StrokeList selectedChar = new StrokeList();
				
				
				try {
					demo = new DemoGui();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				drawpanel.strokeList.setCharacterRelativeCoordinates();
				
				Iterator<Stroke> allStrokes = drawpanel.strokeList.getStrokes().iterator();
				Stroke thisStroke;
				while (allStrokes.hasNext()) {
					thisStroke = allStrokes.next();
					System.out.println("height: " + thisStroke.getStrokeHeight());
					System.out.println("width: " + thisStroke.getStrokeWidth());

					if (thisStroke.getMinX() > drawpanel.leftX && 
							thisStroke.getMaxX() < drawpanel.rightX &&
							thisStroke.getMinY() > drawpanel.topY &&
							thisStroke.getMaxY() < drawpanel.bottomY) {
						selectedChar.addStroke(thisStroke);
					}
				}
				selectedChar.setCharacterRelativeCoordinates();
				
				boolean[] features3x3 = new boolean[4];
				boolean[] features2x2 = new boolean[4];
				features3x3[0] = true; features3x3[1] = false; features3x3[2] = false; features3x3[3] = true;
				features2x2[0] = false; features2x2[1] = true; features2x2[2] = false; features2x2[3] = true;
		
				
				double[] inputs3x3 = (new TrainingSet(selectedChar, features3x3)).inputs[0];
				double[] inputs2x2 = (new TrainingSet(selectedChar, features2x2)).inputs[0];
				
				double[] outputs = new double[2];

				outputs[0] = GridNetworkBuilder.getOutput(inputs3x3, demo.nets[0][0]); // a
				outputs[1] = GridNetworkBuilder.getOutput(inputs3x3, demo.nets[1][0]); // b
				
				

				
				for (int i=0; i < outputs.length; i++) {
					System.out.println("output " + i + ": " + outputs[i]);
					
				}
				int max = BinaryGridNetworkTrainer.findMax(outputs);
				String character = "-1";
				if (max == 0) {
					character = "C";
				} else if (max == 1) {
					character = "H";
				}
				
				
				if (outputs[max] < -0.1) {
					character = "...what is that";
				}
				file.setText("did you write " + character + "?");
				
			}
		});
		
		selector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (drawpanel.drawRect) {
					drawpanel.drawRect = false;
				} else {
					drawpanel.drawRect = true;
				}
			}
		});
		
		classifyMol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		    	Molecule m = Synthesize_Molecule.synthesize(drawpanel.strokeList);
		    	int[] dimensions = m.set_dimensions();

		    	int height = m.get_height();
		    	int width = m.get_width();
		    	System.out.println("width: " + width);
		    	System.out.println("height: " + height);
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



class MyPanel2 extends JPanel {
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
	
    public MyPanel2() {
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
                		//repaint(boundaryStartX, boundaryStartY, 4, height); // left
                		//repaint(boundaryStartX, boundaryEndY, width, 4); // bottom
                		//repaint(boundaryEndX, boundaryStartY, 4, height); // right
                	
                		
                	} /*else if (startXisMin && !startYisMin) { // bottom left start. top right end.
                		repaint(boundaryStartX, boundaryEndY, width, 4); // top
                		repaint(boundaryStartX, boundaryEndY, 4, height); // left
                		repaint(boundaryStartX, boundaryStartY, width, 4); // bottom
                		repaint(boundaryEndX, boundaryEndY, 4, height); // right
                	} else if (!startXisMin && startYisMin) { // top right start. bottom left end.
                		repaint(boundaryEndX, boundaryStartY, width, 4); // top
                		repaint(boundaryEndX, boundaryStartY, 4, height); // left
                		repaint(boundaryEndX, boundaryEndY, width, 4); // bottom
                		repaint(boundaryStartX, boundaryStartY, 4, height); // right
                		
                	} else { // bottom right start. top left end.
                		repaint(boundaryEndX, boundaryEndY, width, 4); // top
                		repaint(boundaryEndX, boundaryEndY, 4, height); // left
                		repaint(boundaryEndX, boundaryStartY, width, 4); // bottom
                		repaint(boundaryStartX, boundaryEndY, 4, height); // right
                	}*/
            		
                	
            		
            		
            		
            	} else {
                	strokeList.addStroke(thisStroke);
                	thisStroke.terminate();
                	mouseDown = false;
            	}
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
    	}
    }
    public Dimension getPreferredSize() {
        return new Dimension(panelwidth, panelheight);
    }
    public void update ( Graphics g )
    { paint(g); } 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  
        if (drawRect) {
        	int x1, x2, y1, y2;
        	
        	if (startXisMin) {
        		x1 = boundaryStartX;
        		x2 = boundaryEndX;
        	} else {
        		x1 = boundaryEndX;
        		x2 = boundaryStartX;
        	}
        	if (startYisMin) {
        		y1 = boundaryStartY;
        		y2 = boundaryEndY;
        	} else {
        		y1 = boundaryEndY;
        		y2 = boundaryStartY;
        	}
        	
        	if (counter == 0) {
        		g.drawLine(x1, y1, x2, y1);
        		counter++;
        		repaint(boundaryStartX, boundaryStartY, 4, height); // left	
        	} else if (counter == 1) {
            	g.drawLine(x1, y1, x1, y2); // left
            	counter++;
        		repaint(boundaryStartX, boundaryEndY, width, 4); // bottom
        	} else if (counter == 2) {
            	g.drawLine(x1, y2, x2, y2); // bottom
            	counter++;
        		repaint(boundaryEndX, boundaryStartY, 4, height); // right
        	} else if (counter == 3){
            	g.drawLine(x2, y1, x2, y2); // right
            	counter =0;
        	}
        	

        } 
        else {
        	if (!erase || first) {g.setColor(Color.BLACK);}
        	else {g.setColor(Color.WHITE);}
        	
        	if (!all) {
        		g.fillRect(squareX,squareY,squareW,squareH);
        		g.drawRect(squareX,squareY,squareW,squareH);
        	} 
        }
    }  
}
