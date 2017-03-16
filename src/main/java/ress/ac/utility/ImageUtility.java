package ress.ac.utility;

import org.opencv.core.Mat;

public class ImageUtility {
	private static final double[] WHITE_COLOR = { 255 };
	private static final double[] BLACK_COLOR = { 0 };
	
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
}
