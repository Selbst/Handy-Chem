package line;

import java.util.Iterator;
import java.util.LinkedList;

import recognition.strokes.Coordinate;

public class Line {
	private double yIntercept; private double slope; public double getYIntercept(){return yIntercept;} public double getSlope() {return slope;}
	private double meanError; public double getMeanError(){return meanError;} public void setMeanError(double error){meanError =error;}
	private FuzzyLineClassifier classification; public FuzzyLineClassifier getClassification() {return classification;}
	
	public Line(double alpha, double beta) {
		yIntercept=alpha; slope=beta;
	}
	
	public static Line regressionLine(LinkedList<Coordinate> coordinates) { //double[0] = alpha double[1] = beta
		Iterator<Coordinate> coordinatesIterator = coordinates.iterator();
		Coordinate currentCoordinate; int currentX; int currentY; int currentXY;double xySum=0; double xSum=0; double ySum=0; int numCoordinates =0; int xSquaredSum=0;
		while (coordinatesIterator.hasNext()) {
			currentCoordinate = coordinatesIterator.next();
			currentX=currentCoordinate.getX(); currentY=currentCoordinate.getY(); currentXY = currentX*currentY;
			xySum += currentXY; xSum += currentX; ySum += currentY; numCoordinates++; xSquaredSum += currentX*currentX;
		}
		if (!coordinates.isEmpty()) {
			double xMean = xSum/numCoordinates; double yMean =ySum/numCoordinates; double xyMean= xySum/numCoordinates; double xSquaredMean = xSquaredSum/numCoordinates;
			double covariance = xyMean - (xMean*yMean); double variance = xSquaredMean - xMean*xMean;
			double beta = covariance/variance;
			double alpha = yMean - beta*xMean;
			Line line = new Line(alpha, beta);
			
			
			
			return line;
		}else {return null;}
	}
	
	public static double findMeanError(LinkedList<Coordinate> coordinates, Line regressionLine) {
		Iterator<Coordinate> coordinatesIterator = coordinates.iterator();
		Coordinate currentCoordinate; int currentX; int currentY; double currentError=0; int numCoordinates =0;
		double slope = regressionLine.getSlope(); double yIntercept = regressionLine.getYIntercept();
		
		while (coordinatesIterator.hasNext()) {
			currentCoordinate = coordinatesIterator.next();
			currentX=currentCoordinate.getX(); currentY=currentCoordinate.getY();
			currentError += Math.abs((currentY - yIntercept - (slope*currentX)));
			numCoordinates++;
		}
		return currentError/numCoordinates;
	}
	public void setClassification() {
		classification = new FuzzyLineClassifier();
		classification.calculateAreas(slope);
	}
	public String toString() {
		String returnString = "";
		returnString += "alpha: " + yIntercept + "beta: " + slope;
		
		return returnString;
	}
	public String toStringWithError() {
		String returnString = "";
		returnString += "alpha: " + yIntercept + "\nbeta: " + slope;
		returnString += "\nmean error: " + meanError;
		return returnString;
	}

}
