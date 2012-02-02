package pixels.binary;

public class SimpleCoordinate {
	public int row;
	public int column;
	public boolean propagated;
	public SimpleCoordinate(int r, int c) {
		row = r; column = c;
	}
	public SimpleCoordinate(int r, int c, boolean p) {
	row = r; column = c; propagated = p;
	}
}
