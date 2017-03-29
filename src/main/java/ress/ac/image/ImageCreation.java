package ress.ac.image;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageCreation {
	//	 public static String path = "src/main/resources/lena.png";
	//	 public static String path = "src/main/resources/the-elder-scrolls.jpg";
	//	 public static String path = "src/main/resources/output.jpg";
//		public static String path = "src/main/resources/house.jpeg";
		public static String path = "src/main/resources/obstacle.jpg";
		
		public static Mat retrieveOriginalImage(){
			return Imgcodecs.imread(path);
		}
		
		public static Mat convertToGrayImage(Mat inputImage){
			Mat imageGray = new Mat();
			Imgproc.cvtColor(inputImage, imageGray, Imgproc.COLOR_RGB2GRAY);
			return imageGray;
		}
		
		public static Mat blurImage(Mat inputImage){
			Mat imageBlur = new Mat();
			Imgproc.medianBlur(inputImage, imageBlur, 3);
			return imageBlur;
		}
		
		public static Mat createCannyImage(Mat inputImage){
			Mat imageCny = new Mat();
			
			int[] hist = ImageUtility.createHistogram(inputImage);
			double median = ImageUtility.findMedian(hist, inputImage.total());
			int lower = ImageUtility.retrieveLowerBound(median);
			int upper = ImageUtility.retrieveUpperBound(median);
			
			Imgproc.Canny(inputImage, imageCny, lower, upper);
			return imageCny;
		}
		
		public static Mat sideFill(Mat inputImage){
			Mat imageSideFill = new Mat();
			inputImage.copyTo(imageSideFill);
			ImageUtility.sideFill(imageSideFill);
			return imageSideFill;
		}
		
		public static Mat erodeImage(Mat inputImage){
			Mat erode = new Mat(inputImage.rows(),inputImage.cols(),inputImage.type());
			Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2*15 + 1, 2*15+1));
			
			Imgproc.erode(inputImage, erode, element);
			return erode;
		}
}
