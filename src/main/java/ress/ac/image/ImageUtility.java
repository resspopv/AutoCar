package ress.ac.image;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

public class ImageUtility {
	private static final double[] WHITE_COLOR = { 255 };
	private static final double[] BLACK_COLOR = { 0 };
	private static final double SIGMA = 0.33;
	
	public static int[] createHistogram(Mat image){
		MatOfFloat range = new MatOfFloat(0f, 256f);
		MatOfInt histSize = new MatOfInt(256);
		MatOfInt output = new MatOfInt();
		
		List<Mat> images = new ArrayList<Mat>();
		images.add(image);
		Imgproc.calcHist(images, new MatOfInt(0), new Mat(), output, histSize, range);
		MatOfInt hist = new MatOfInt(CvType.CV_32S);
		output.convertTo(hist, CvType.CV_32S);
		int[] histOutput = hist.toArray();
		
		return histOutput;
	}
	
	public static double findMedian(int[] histInput, long totalElements) {
		int histLength = histInput.length;

		int binNum = 0;
		double halfTotalElements = totalElements / 2;
		for (int sum = 0; sum < halfTotalElements && binNum < histLength; binNum++)
			sum += histInput[binNum];

		return binNum;
	}

	public static void sideFill(Mat imageCny) {
		for (int j = imageCny.cols() - 1; j >= 0; j--) {
			boolean white = true;
			for (int i = imageCny.rows() - 1; i >= 0; i--) {
				if (imageCny.get(i, j)[0] > 0) {
					white = false;
				}
				if (white) {
					imageCny.put(i, j, WHITE_COLOR);
				} else {
					imageCny.put(i, j, BLACK_COLOR);
				}
			}
		}
	}
	
	public static int retrieveLowerBound(double median){
//		return (int) (.66 * median);
		System.out.println((int) Math.max(0, (1.0 - SIGMA) * median));
		return (int) Math.max(0, (1.0 - SIGMA) * median);
	}
	
	public static int retrieveUpperBound(double median){
//		return = (int) (1.33 * median);
		System.out.println((int) Math.min(255, (1.0 + SIGMA) * median));
		return (int) Math.min(255, (1.0 + SIGMA) * median);
	}
}
