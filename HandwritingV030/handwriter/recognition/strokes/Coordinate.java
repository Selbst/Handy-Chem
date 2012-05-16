package recognition.strokes;

public class Coordinate {
	private int x; private int y; private double xAvg, yAvg;
	private double horizontalArea; private double verticalArea; private double backslashArea; private double forwardslashArea;
	private double horizontalDeviation; private double verticalDeviation; private double backslashDeviation; private double forwardslashDeviation;
	private double totalDeviation; private long time;
	public Coordinate(int x, int y) {
		this.x = x; this.y =y;
	}
	public Coordinate(int x, int y, long t) {
		this.x = x; this.y = y; time = t;
	}
	public Coordinate(int x, int y, double xAvg, double yAvg) {
		this.x = x; this.y = y; this.xAvg = xAvg; this.yAvg = yAvg;
	}
	public Coordinate(double xAvg, double yAvg, double[] areas) {
		this.xAvg = xAvg; this.yAvg = yAvg;
		setAreas(areas);
	}
	public int getX(){return x;} public int getY(){return y;} public long getT() {return time;}
	public double getXAvg() {return xAvg;} public double getYAvg() { return yAvg;}
	public void setAreas(double[] areas) {
		horizontalArea = areas[0]; verticalArea = areas[1]; backslashArea = areas[2]; forwardslashArea = areas[3];
	}
	public double[] getAreas() {
		double[] areas = new double[4];
		areas[0]=horizontalArea; areas[1]=verticalArea; areas[2]=backslashArea; areas[3]=forwardslashArea;
		return areas;
	}
	public void setDeviations(double[] deviations) {
		horizontalDeviation = deviations[0]; verticalDeviation = deviations[1]; backslashDeviation = deviations[2]; forwardslashDeviation = deviations[3];
		totalDeviation = horizontalDeviation+verticalDeviation+backslashDeviation+forwardslashDeviation;
	}
	public double[] getDeviations() {
		double[] deviations = new double[5];
		deviations[0]=horizontalDeviation; deviations[1]=verticalDeviation; deviations[2]=backslashDeviation; deviations[3]=forwardslashDeviation;
		deviations[5]=totalDeviation;
		return deviations;
	}
	public Coordinate copy() {
		Coordinate copy = new Coordinate(x, y);
		copy.backslashArea = this.backslashArea; copy.backslashDeviation = this.backslashDeviation; copy.forwardslashArea = this.forwardslashArea;
		copy.forwardslashDeviation = this.forwardslashDeviation; copy.horizontalArea = this.horizontalArea; copy.horizontalDeviation = this.horizontalDeviation;
		copy.totalDeviation = this.totalDeviation; copy.verticalArea = this.verticalArea; copy.verticalDeviation = this.verticalDeviation;
		return copy;
	}
	
	public String toString() {
		return ""+x + "," + y;
	}	
	
	
	
	public String metaCoordToString() {
		String returnString = "";
			returnString += "areas: " + horizontalArea + ", " + verticalArea + ", " + backslashArea + ", " + forwardslashArea;
			
			returnString += "\nposition: " + xAvg + ", " + yAvg;

		return returnString;
	}
}
