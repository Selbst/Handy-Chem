package line;

public class FuzzyLineClassifier {
	
	private double horizontalArea; private double verticalArea; private double backslashArea; private double forwardslashArea;
	public double getHorizontalArea() {return horizontalArea;} public double getVerticalArea() {return verticalArea;} public double getBackslashArea() {return backslashArea;} public double getForwardslashArea() {return forwardslashArea;}
	public double[] getAreas() {
		double[] areas = new double[4];
		areas[0]=horizontalArea; areas[1]=verticalArea; areas[2]=backslashArea; areas[3]=forwardslashArea;
		return areas;
	}
	private final double horizontalPeak =0; private final double backslashPeak = 1; private final double forwardslashPeak = -1;
	private final double areaConstraint = 100;
	
	// parameters
	private double horizontalMinLeft = -0.8; private double horizontalMinRight = 0.8;
	private double backslashMinLeft = .05; private double backslashMinRight = 20;
	private double forwardslashMinLeft = -20; private double forwardslashMinRight = -0.05;
	private double verticalMinLeft = -1.8; private double verticalMinRight = 1.8;
	private double verticalLimit = 25;
	private double horizontalWeight = 125; private double backslashWeight = 9.97506234414; private double forwardslashWeight = 9.97506234414; private double verticalWeight = 8.62068965518;

	public double getBackslashMinLeft() {return backslashMinLeft;} public double getBackslashMinRight() {return backslashMinRight;} public double getBackslashWeight() {return backslashWeight;}
	public double getForwardslashMinLeft() {return forwardslashMinLeft;} public double getForwardSlashMinRight() {return forwardslashMinRight;} public double getForwardSlashWeight() {return forwardslashWeight;}
	public double getHorizontalMinLeft() {return horizontalMinLeft;} public double getHorizontalMinRight() {return horizontalMinRight;} public double getHorizontalWeight() {return horizontalWeight;}
	public double getVerticalMinLeft() {return verticalMinLeft;} public double getVerticalMinRight() {return verticalMinRight;} public double getVerticalWeight() {return verticalWeight;}
	public double getVerticalLimit() {return verticalLimit;}
	// 13 parameters to optimize: x-intercepts , local maxima, and vertical limit.
	//
	// 	nonvertical parameters have constraint:
	// 			|leftmin - peak|*weight/2 + |rightmin-peak|*weight/2 = 100; normalizes all sets to peak at 100. (areaConstraint)
	//	vertical parameters have contstraint: 
	//			|vertical limit - verticalmin|*weight/2 = 100; normalizes to peak at 100 (areaConstraint)
	public void setBackslashMinLeft(double setTo) {backslashMinLeft =setTo;} public void getBackslashMinRight(double setTo) {backslashMinRight = setTo;} public void getBackslashWeight(double setTo) {backslashWeight = setTo;}
	public void setForwardslashMinLeft(double setTo) {forwardslashMinLeft=setTo;} public void setForwardSlashMinRight(double setTo) { forwardslashMinRight=setTo;} public void setForwardSlashWeight(double setTo) {forwardslashWeight=setTo;}
	public void setHorizontalMinLeft(double setTo) {horizontalMinLeft=setTo;} public void setHorizontalMinRight(double setTo) {horizontalMinRight=setTo;} public void setHorizontalWeight(double setTo) {horizontalWeight=setTo;}
	public void setVerticalMinLeft(double setTo) {verticalMinLeft=setTo;} public void setVerticalMinRight(double setTo) {verticalMinRight=setTo;} public void setVerticalWeight(double setTo) {verticalWeight=setTo;}
	public void setVerticalLimit(double setTo) {verticalLimit=setTo;}
	
	public void calculateAreas(double slope) {
		if (slope < verticalMinLeft) {
			if (Math.abs(slope) > verticalLimit) {verticalArea = areaConstraint;} 
			else {
				double base = Math.abs(-1*verticalLimit - verticalMinLeft);
				double verticalSlope = verticalWeight / base;
				double height = verticalSlope * Math.abs(slope - verticalMinLeft);
			
				verticalArea = base*height/2;
			}
		}else if (slope > verticalMinRight) {
			if (Math.abs(slope) > verticalLimit) {verticalArea = areaConstraint;} 
			else {
				double base = Math.abs(verticalLimit - verticalMinRight);
				double verticalSlope = verticalWeight / base;
				double height = verticalSlope * Math.abs(slope - verticalMinRight);
			
				verticalArea = base*height/2;
			}
		}
		if (slope > forwardslashMinLeft && slope <= forwardslashPeak) {
			double baseLeft = Math.abs(forwardslashPeak - forwardslashMinLeft);
			double baseRight = Math.abs(forwardslashPeak - forwardslashMinRight);
			double forwardslashSlope = forwardslashWeight / baseLeft;
			
			double distSlopeToMin = Math.abs(slope - forwardslashMinLeft);
			double height = forwardslashSlope*distSlopeToMin;
			forwardslashArea = baseLeft*height/2 + baseRight*height/2;

		}else if (slope > forwardslashPeak && slope < forwardslashMinRight) {
			double baseRight = Math.abs(forwardslashPeak - forwardslashMinRight);
			double baseLeft = Math.abs(forwardslashPeak - forwardslashMinLeft);
			double forwardslashSlope = forwardslashWeight / baseRight;
			
			double distSlopeToMin = Math.abs(slope - forwardslashMinRight);
			double height = forwardslashSlope*distSlopeToMin;
			
			forwardslashArea = baseRight*height/2 + baseLeft*height/2;
		}
		if (slope > backslashMinLeft && slope <= backslashPeak) {
			double baseLeft = Math.abs(backslashPeak - backslashMinLeft);
			double baseRight = Math.abs(backslashPeak - backslashMinRight);
			double backslashSlope = backslashWeight / baseLeft;
			
			double distSlopeToMin = Math.abs(slope - backslashMinLeft);
			double height = backslashSlope*distSlopeToMin;
			backslashArea = baseLeft*height/2 + baseRight*height/2;

		}else if (slope > backslashPeak && slope < backslashMinRight) {
			double baseRight = Math.abs(backslashPeak - backslashMinRight);
			double baseLeft = Math.abs(backslashPeak - backslashMinLeft);
			double backslashSlope = backslashWeight / baseRight;
			
			double distSlopeToMin = Math.abs(slope - backslashMinRight);
			double height = backslashSlope*distSlopeToMin;
			
			backslashArea = baseRight*height/2 + baseLeft*height/2;

		}
		if (slope > horizontalMinLeft && slope <= horizontalPeak) {
			double base = Math.abs(horizontalPeak - horizontalMinLeft);
			double horizontalSlope = horizontalWeight / base;
			
			double distSlopeToMin = Math.abs(slope - horizontalMinLeft);
			double height = horizontalSlope*distSlopeToMin;
			
			horizontalArea = base*height;

		}else if (slope > horizontalPeak && slope < horizontalMinRight) {
			double base = Math.abs(horizontalPeak - horizontalMinRight);
			double horizontalSlope = horizontalWeight / base;
			
			double distSlopeToMin = Math.abs(slope - horizontalMinRight);
			double height = horizontalSlope*distSlopeToMin;
			
			horizontalArea = base*height;
		}		
	}
	
	public double[] compareLines(FuzzyLineClassifier compareTo) {
			double sumDeviations = 0; 
			double deviations[] = new double[4];
			
			deviations[0] = Math.abs(horizontalArea - compareTo.getHorizontalArea());
			deviations[1] = Math.abs(verticalArea - compareTo.getVerticalArea());
			deviations[2] = Math.abs(backslashArea - compareTo.getBackslashArea());
			deviations[3] = Math.abs(forwardslashArea - compareTo.getForwardslashArea());

		return deviations;
	}
	public String toString() {
		String returnString = "";
		returnString += "horizontal area: " + horizontalArea;
		returnString += "\nvertical area: " + verticalArea;
		returnString += "\nbackslash area: " + backslashArea;
		returnString += "\nforwardslash area: " + forwardslashArea;
		return returnString;
	}
	public static void main(String[] args) {
		FuzzyLineClassifier classifier = new FuzzyLineClassifier();
		classifier.calculateAreas(-5);
		System.out.println(classifier.toString());
	}
}
