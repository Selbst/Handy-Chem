package pixels.binary;

import java.awt.image.IndexColorModel;

import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;

public class ImageCompression {

	public static int[][] compress(int[][] uncompressed) {
		int[][] compressed = new int[20][20]; double amplifier;
		
		BinaryProcessor binp = getBinaryProcessor(uncompressed);

		ByteProcessor bp;
		// determine resize based on vertical or horizontal (whichever is larger).
		if (uncompressed.length >= uncompressed[0].length) { 
			amplifier = 20 / uncompressed.length;
			bp = (ByteProcessor) binp.resize(20, (int)Math.ceil(amplifier*uncompressed[0].length));
			
		} else {
			amplifier = 20 / uncompressed[0].length;
			bp = (ByteProcessor) binp.resize((int)Math.ceil(amplifier*uncompressed.length), 20);
		}
		binp = new BinaryProcessor(bp);

		int thisPix;
		for (int i=0; i < binp.getHeight(); i++) {
			for (int j=0; j < binp.getWidth(); j++) {
				thisPix = binp.get(i, j);
				if (thisPix == 255 || thisPix == 1) {
					compressed[i][j] = 1;
				}
			}
		}
		

		
		return compressed;
	}
	
	
	public static BinaryProcessor getBinaryProcessor(int[][] image) {
		byte[] r = new byte[image.length*image[0].length];
		byte[] g = new byte[image.length*image[0].length];
		byte[] b = new byte[image.length*image[0].length];
		
		int index = 0;
		for (int i=0; i < image.length; i++) {
			for (int j=0; j < image[0].length; j++) {
				if (image[i][j] == 1) {
					r[index] = g[index] = b[index] = -1;
				}
				index++;
				
			}
		}
		IndexColorModel icm = new IndexColorModel(8, image.length*image[0].length, r, g, b);
		ByteProcessor bp = new ByteProcessor(image.length, image[0].length, r, icm);
		BinaryProcessor binp = new BinaryProcessor(bp);
		return binp;
	}
	
	
	public static int[][] fatten(int[][] image, int howfat) {
		int[][] newimage = new int[image.length][image[0].length];
		for (int i=0; i < image.length; i++) {
			for (int j=0; j < image.length; j++) {
				if (image[i][j] == 1) {
					for (int z = 0; z < howfat; z++) {
						for (int k = 0; k < howfat; k++) {
							if (j - k >= 0) {
								if (i - z >= 0) {
									newimage[i-z][j-k] = 1;
								}
							}
							if (j + k < image[0].length) {
								if (i + z < image.length) {
									newimage[i+z][j+k] = 1;
								}
							}
							
						}
					}
					
					
				}
				
			}
		}
		
		return newimage;
	}
	
	public static void main(String[] args) {
		System.out.println((byte)127);
		
		int[][] image = new int[2][4]; { image[0][0] =0; image[0][1] = 1; image[1][0] = 1; image[1][1] = 0; image[1][2] = 0; image[0][2] = 1; image[1][3] = 0; image[0][3] = 1;}
		image = compress(image);
		for (int i=0; i <image.length; i++) {
			for (int j=0; j < image[0].length; j++) {
				System.out.print(image[i][j]);
			}
			System.out.println();
		}
		image = fatten(image, 2);
		
		for (int i=0; i <image.length; i++) {
			for (int j=0; j < image[0].length; j++) {
				System.out.print(image[i][j]);
			}
			System.out.println();
		}
	}

}
