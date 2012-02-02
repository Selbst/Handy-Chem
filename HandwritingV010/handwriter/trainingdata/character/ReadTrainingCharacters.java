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
			String[] training_files = new String[4*filtered_options.length]; 
			for (int i=1; i < 5; i++) {
				for (int j=0; j < filtered_options.length; j++) {
					System.out.println("trainingcharacters/choose_from_all/" + i + "_" +  filtered_options[j] + ".tr");
					training_files[filtered_options.length*(i-1) + j] = "trainingcharacters/choose_from_all/" + i + "_" +  filtered_options[j] + ".tr";
				}
				
			}
			String outputFile = "trainingcharacters/filtered/" + output;
			combineTrainingFiles(training_files, outputFile);
		} else {
			String[] training_files = new String[1*filtered_options.length]; 
			for (int i=1; i < 2; i++) {
				for (int j=0; j < filtered_options.length; j++) {
					System.out.println("testingcharacters/1/" + i + "_" +  filtered_options[j] + ".ts");
					training_files[filtered_options.length*(i-1) + j] = "testingcharacters/0/" + i + "_" +  filtered_options[j] + ".ts";
				}
				
			}
			String outputFile = "testingcharacters/1/filtered/" + output;
			combineTrainingFiles(training_files, outputFile);
		}

	}
	
	public static void main(String[] args) throws IOException {
		/*
		for (int i=0; i < 25; i++) {
			fill_characters("trainingcharacters/choose_from_all/0_" + i + ".tr", "trainingcharacters/choose_from_all/4_" + i + ".tr");
			fill_characters("testingcharacters/0/" + i + ".ts", "testingcharacters/0/0_" + i + ".ts");

		}*/
		//fill_characters("testingcharacters/0/all.ts", "testingcharacters/0/all_3.ts");
		
		//LinkedList<StrokeList> chars = getCharsFromFile("testingcharacters/0/1_22.ts");

		LinkedList<StrokeList> chars = getCharsFromFile("testingcharacters/0/left0/0_all.ts");
		//LinkedList<StrokeList> chars = getCharsFromFile("trainingcharacters/left0/0_all.tr");

		Iterator<StrokeList> it = chars.iterator();
		//while (it.hasNext()) {
			//System.out.println(it.next().toString());
		//}
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
		
		/*
		String[] files = new String[21];
		files[0] = "trainingcharacters/l_a-f_0.tr";
		files[1] = "trainingcharacters/l_g_1.tr";
		files[2] = "trainingcharacters/l_h_1.tr";
		files[3] = "trainingcharacters/l_i_1.tr";
		files[4] = "trainingcharacters/l_j_1.tr";
		files[5] = "trainingcharacters/l_k_1.tr";
		files[6] = "trainingcharacters/l_l_1.tr";
		files[7] = "trainingcharacters/l_m_1.tr";
		files[8] = "trainingcharacters/l_n_1.tr";
		files[9] = "trainingcharacters/l_o_1.tr";
		files[10] = "trainingcharacters/l_p_1.tr";
		files[11] = "trainingcharacters/l_q_1.tr";
		files[12] = "trainingcharacters/l_r_1.tr";
		files[13] = "trainingcharacters/l_s_1.tr";
		files[14] = "trainingcharacters/l_t_1.tr";
		files[15] = "trainingcharacters/l_u_1.tr";
		files[16] = "trainingcharacters/l_v_1.tr";
		files[17] = "trainingcharacters/l_w_1.tr";
		files[18] = "trainingcharacters/l_x_1.tr";
		files[19] = "trainingcharacters/l_y_1.tr";
		files[20] = "trainingcharacters/l_z_1.tr";
		
		
		
		
		String outputFile = "trainingcharacters/l_a-z_4.tr";
		combineTrainingFiles(files, outputFile);
		*/
		
		/*
		String[] files = new String[1];
		
		files[0] = "trainingcharacters/l_m_2.tr";
		String outputFile = "trainingcharacters/l_m_1.tr";
		
		combineTrainingFiles(files, outputFile);
		*/
		
		/*
		String[] training_files = new String[26];
		for (int i=0; i < 25; i++) {
			training_files[i] = "testingcharacters/0/2_" + i + ".ts";
		}
		training_files[25] = "testingcharacters/0/all_5.ts";
		String outputFile = "testingcharacters/0/all_6.ts";
		combineTrainingFiles(training_files, outputFile);
		*/
		/*
		String[] files = new String[2];
		files[0] = "trainingcharacters/choose_from_all/4_all.tr";
		files[1] = "trainingcharacters/choose_from_all/5_all.tr";
		String outputFile = "trainingcharacters/choose_from_all/6_all.tr";
		combineTrainingFiles(files, outputFile);
		*/
		
		/*
		String[] training_files = new String[39];
		int j2 = 0;
		for (int i=0; i < 3; i++) {
			for (int j =0; j < 25; j++) {
				if (j == 0 || j == 1 || j == 3 || j == 7 || j == 8 || j == 9 || j == 10 || j == 11 || j == 19 || j == 20 || j == 21 || j == 22 || j == 23) {
					if (j ==0) {
						System.out.println(j);
					}
					System.out.println("testingcharacters/0/" + i + "_" + j + ".ts");
						training_files[13*i + j2] = "testingcharacters/0/" + j + ".ts";
						training_files[13*i + j2] = "testingcharacters/0/" + i + "_" + j + ".ts";
					j2++;
				}
			}
			j2 =0;
		}
		String outputFile = "testingcharacters/0/left0/0_all.ts";
		combineTrainingFiles(training_files, outputFile);
		*/
		/*
		String[] training_files = new String[52];
		int j2 = 0;
		for (int i=1; i < 5; i++) {
			for (int j =0; j < 25; j++) {
				if (j == 0 || j == 1 || j == 3 || j == 7 || j == 8 || j == 9 || j == 10 || j == 11 || j == 19 || j == 20 || j == 21 || j == 22 || j == 23) {
					if (j ==0) {
						System.out.println(j);
					}
					System.out.println("trainingcharacters/choose_from_all/" + i + "_" + j + ".tr");
						training_files[13*(i-1) + j2] = "trainingcharacters/choose_from_all/" + j + ".tr";
						training_files[13*(i-1) + j2] = "trainingcharacters/choose_from_all/" + i + "_" + j + ".tr";
					j2++;
				}
			}
			j2 =0;
		}
		String outputFile = "trainingcharacters/left0/0_all.tr";
		combineTrainingFiles(training_files, outputFile);
		*/
		

		
	}
		
	
}
