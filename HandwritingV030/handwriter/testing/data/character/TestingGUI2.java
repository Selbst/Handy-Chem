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

public class TestingGUI2 {
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

