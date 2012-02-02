package recognition;

public class TrainingElement {
	public double[] inputs;
	public int classification;
	
	public TrainingElement(double[] inputer, int classificationer) {
		inputs = inputer; classification = classificationer;
	}
	public String toString() {
		String s= "classification: " + classification + "--: ";
		for (int i=0; i < inputs.length; i++) {
			s+= inputs[i] + ", ";
		}
		s += "\n";
		return s;
	}
}
