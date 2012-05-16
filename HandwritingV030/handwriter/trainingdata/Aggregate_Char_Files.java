package trainingdata;

import java.io.IOException;
import java.util.LinkedList;

import recognition.strokes.StrokeList;
import trainingdata.character.ReadTrainingCharacters;

public class Aggregate_Char_Files {

	public static LinkedList<StrokeList> get_training_chars(int[] char_ids, int[] file_ids) {
		
		String[] files = new String[char_ids.length*file_ids.length];
		for (int i=0; i < char_ids.length; i++) {
			for (int j=0; j < file_ids.length; j++) {
				files[i*file_ids.length + j] = "trainingcharacters/choose_from_all/"  + file_ids[j] +  "_" + char_ids[i] + ".tr";
			}
		}
		
		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		for (int i=0; i < files.length; i++) {
			try {
				chars.addAll(ReadTrainingCharacters.getCharsFromFile(files[i]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return chars;
		
		
	}
	public static LinkedList<StrokeList> get_vaildation_chars(int[] char_ids, int[] file_ids) {
		String[] files = new String[char_ids.length*file_ids.length];
		for (int i=0; i < char_ids.length; i++) {
			for (int j=0; j < file_ids.length; j++) {
				files[i*file_ids.length + j] = "testingcharacters/0/"  + file_ids[j] +  "_" + char_ids[i] + ".ts";
			}
		}
		
		LinkedList<StrokeList> chars = new LinkedList<StrokeList>();
		for (int i=0; i < files.length; i++) {
			try {
				chars.addAll(ReadTrainingCharacters.getCharsFromFile(files[i]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return chars;
	}
}
