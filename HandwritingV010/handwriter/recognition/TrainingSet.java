package recognition;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import neuralnetwork.strokenetwork.StrokeNetwork;
import neuralnetwork.strokenetwork.StrokeNetworkBuilder;


public class TrainingSet {
	public Vector<TrainingElement> trainingelements;

	public TrainingSet(String filename, int inputSize) throws NumberFormatException, IOException {

		trainingelements = new Vector<TrainingElement>();
		Vector<Integer> classifications = new Vector<Integer>();
		Vector<Double> input = new Vector<Double>();
		Vector<Vector<Double>> inputSets = new Vector<Vector<Double>>();
		String thisLine; int count = 0;
		
		int classification=-1;
		double[] inputs = new double[inputSize];
		TrainingElement trainingelement;
		
		try {
			BufferedReader reader  = new BufferedReader(new FileReader(filename));
			while ((thisLine = reader.readLine()) != null) {
				if (count % (inputSize +1) == 0) {
					classification = Integer.valueOf(thisLine);
					count=0;
				} else { 
					inputs[count-1] = Double.valueOf(thisLine)/100;
				}
				if (count % inputSize == 0 && count !=0) {
					trainingelement = new TrainingElement(inputs, classification);
					trainingelements.add(trainingelement);
					inputs = new double[inputSize];
				}
				count++;
			} 
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String toString() {
		String s = "";
		Iterator<TrainingElement> iterator = trainingelements.listIterator();
		int count=0;
		while (iterator.hasNext()) {
			s += "training element " + count + "\n";
			s += "   " + iterator.next().toString() + "\n";
			count++;
		}
		return s;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		//TrainingSet set = new TrainingSet("stroketrainingset/stroke0/set1.tr", 80);
		StrokeNetwork net = StrokeNetworkBuilder.fileToNetwork("networkmodels/stroke0/test1.net");
		System.out.println(StrokeNetworkBuilder.toString(net));
		//System.out.println(set.toString());
	}
	
	
}
