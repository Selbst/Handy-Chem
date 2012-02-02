package pixels.binary;

public class Image {
	public int width; public int height;
	public int[][] binaryImage;
	
	public Image(int[][] binaryimage) {
		height = binaryimage.length; width = binaryimage[0].length;
		binaryImage = binaryimage;
	}
	
	
}
