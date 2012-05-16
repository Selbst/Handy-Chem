package trainingdata.character;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import recognition.features.Grid;
import recognition.strokes.Coordinate;
import recognition.strokes.Stroke;
import recognition.strokes.StrokeList;

public class ReadTrainingCharacters {

	
	public static LinkedList<StrokeList> getCharsFromFile(String filepath) throws IOException {
		LinkedList<StrokeList> characters = new LinkedList<StrokeList>();
		
		
		StrokeList thisCharacter;
		Stroke thisStroke;
		LinkedList<Coordinate> metaCoordinates;
		int x, y; long t;
		String in;
		int numStrokes, numCoords, numMetaCoords;
		double xAvg, yAvg;
		double[] areas = new double[4];
		int errorcounter =-1; int errorid =-1;
		
		
		try {
			BufferedReader reader  = new BufferedReader(new FileReader(filepath));
			while ((in = reader.readLine()) != null) {
				if (in.equals("char")) {
					errorcounter++;
					try { 
						
					
					thisCharacter = new StrokeList();
					thisCharacter.id = Integer.parseInt(reader.readLine());
					errorid = thisCharacter.id;
					numStrokes = Integer.parseInt(reader.readLine());
					for (int i=0; i < numStrokes; i++) {
						thisStroke = new Stroke();
						thisStroke.setMinX(Integer.parseInt(reader.readLine()));
						thisStroke.setMinY(Integer.parseInt(reader.readLine()));
						thisStroke.setMaxX(Integer.parseInt(reader.readLine()));
						thisStroke.setMaxY(Integer.parseInt(reader.readLine()));
						
						numCoords = Integer.parseInt(reader.readLine());
						numMetaCoords = Integer.parseInt(reader.readLine());
						//System.out.println("read " + numCoords + " number of Coordinates, and " + numMetaCoords + " number of meta coordinates.");
						for (int j=0; j < numCoords; j++) {
							x = Integer.parseInt(reader.readLine());
							y = Integer.parseInt(reader.readLine());
							t = Long.parseLong(reader.readLine());
							thisStroke.add(new Coordinate(x, y, t));
						}
						metaCoordinates = new LinkedList<Coordinate>();
						for (int j=0; j < numMetaCoords; j++) {
							xAvg = Double.parseDouble(reader.readLine());
							//System.out.println("x average: " + xAvg);
							yAvg = Double.parseDouble(reader.readLine());
							//System.out.println("y average: " + yAvg);

							for (int z =0; z < 4; z++) {
								areas[z] = Double.parseDouble(reader.readLine());
							}
							metaCoordinates.add(new Coordinate(xAvg, yAvg, areas));
						}
						thisStroke.metaCoordinates = metaCoordinates;
						thisCharacter.addStroke(thisStroke);
					}
					thisCharacter.setCharacterRelativeCoordinates();
					characters.add(thisCharacter);
					}
					catch(NumberFormatException e) {
						System.out.println(e.toString());
						System.out.println("character #: " + errorcounter);
						System.out.println("character id: " + errorid);
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return characters;
	}
	public static void combineTrainingFiles(String[] inputFiles, String outputFile) throws IOException {
		FileWriter fstream = new FileWriter(outputFile, true);
		BufferedWriter out = new BufferedWriter(fstream);
		String in = "";
		for (int i=0; i < inputFiles.length; i++) {
			BufferedReader reader  = new BufferedReader(new FileReader(inputFiles[i]));
			while ((in = reader.readLine()) != null) {
				out.write(in + "\n");
			}
			reader.close();
		}
		out.close();
	}
	
	public static void fill_characters(String input_file, String output_file) throws IOException {
		LinkedList<StrokeList> chars = getCharsFromFile(input_file);
		
		
		Iterator<StrokeList> it = chars.iterator();
				
		LinkedList<Stroke> strokes;
		while (it.hasNext()) {
			strokes = it.next().getStrokes();
			Iterator<Stroke> it2 = strokes.iterator();
			while (it2.hasNext()) {
				it2.next().terminate();
			}
		}
		
		it = chars.iterator();
		
		while (it.hasNext()) {
			StrokeList.to_file(it.next(), output_file);
		}
		
		
		
		
		
		
	}
	
	public static void combine_filtered_files(int[] filtered_options, boolean training, String output) throws IOException {
		if (training) {
			String[] training_files = new String[6*filtered_options.length]; 
			for (int i=1; i < 7; i++) {
				for (int j=0; j < filtered_options.length; j++) {
					System.out.println("trainingcharacters/choose_from_all/" + i + "_" +  filtered_options[j] + ".tr");
					training_files[filtered_options.length*(i-1) + j] = "trainingcharacters/choose_from_all/" + i + "_" +  filtered_options[j] + ".tr";
				}
				
			}
			String outputFile = "trainingcharacters/filtered/" + output;
			combineTrainingFiles(training_files, outputFile);
		} else {
			String[] training_files = new String[3*filtered_options.length]; 
			for (int i=0; i < 3; i++) {
				for (int j=0; j < filtered_options.length; j++) {
					System.out.println("testingcharacters/0/" + i + "_" +  filtered_options[j] + ".ts");
					training_files[filtered_options.length*(i) + j] = "testingcharacters/0/" + i + "_" +  filtered_options[j] + ".ts";
				}
				
			}
			String outputFile = "testingcharacters/filtered/" + output;
			combineTrainingFiles(training_files, outputFile);
		}

	}
	
	public static void main(String[] args) throws IOException {

		LinkedList<StrokeList> chars = getCharsFromFile("trainingcharacters/choose_from_all/12_10.tr");

		Iterator<StrokeList> it = chars.iterator();

		Grid g = new Grid(chars.getFirst());
		System.out.println(g.toString());
		System.out.println("doneser");
		int countChars =0;
		int[] countClassInstance = new int[26];
		StrokeList thisChar;
		while (it.hasNext()) {
			thisChar = it.next();
			countClassInstance[thisChar.id]++;
			countChars++;
		}
		System.out.println(countChars + " characters:");
		for (int i=0; i < 26; i++) {
			System.out.print(i + ": " + countClassInstance[i] + ", ");
		}
		chars.clear();
		
		

	}
		
	
}
