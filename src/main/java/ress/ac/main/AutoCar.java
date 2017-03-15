package ress.ac.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import ress.ac.gpio.GpioHolder;
import ress.ac.gpio.GpioProvision;

public class AutoCar {

	private static UltraSonicSensor sensor = new UltraSonicSensor();
	// public static String path = "src/main/resources/lena.png";
	// public static String path = "src/main/resources/the-elder-scrolls.jpg";
	// public static String path = "src/main/resources/output.jpg";
	public static String path = "src/main/resources/house.jpeg";

	private static final double SIGMA = 0.33;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		detectEdges();

		// try {
		// testSensorStop(sensor);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	private static void detectEdges() {
		// read the RGB image
		Mat rgbImage = Imgcodecs.imread(path);

		// mat gray image holder
		Mat imageGray = new Mat();

		// mat canny image
		Mat imageCny = new Mat();

		// Show the RGB Image
		// ImageUtils.displayImage(ImageUtils.toBufferedImage(rgbImage), "RGB
		// Image");

		// Convert the image in to gray image
		Imgproc.cvtColor(rgbImage, imageGray, Imgproc.COLOR_RGB2GRAY);

		// Show the gray image
		// ImageUtils.displayImage(ImageUtils.toBufferedImage(imageGray), "Gray
		// Image");

		System.out.println(imageGray.type());
		System.out.println(imageGray.size());
		System.out.println(imageGray.channels());

		MatOfFloat range = new MatOfFloat(0f, 256f);
		MatOfInt histSize = new MatOfInt(256);
		MatOfInt output = new MatOfInt();
		List<Mat> images = new ArrayList<Mat>();
		images.add(imageGray);
		Imgproc.calcHist(images, new MatOfInt(0), new Mat(), output, histSize, range);
		MatOfInt hist = new MatOfInt(CvType.CV_32S);
		output.convertTo(hist, CvType.CV_32S);
		System.out.println(output.channels());
		int[] histInput = hist.toArray();
		for (int i = 0; i < histInput.length; i++)
			System.out.print(histInput[i] + ", ");
		System.out.println();
		System.out.println(histInput.length);

		double median = findMedian(histInput);
		System.out.println(median);

		int lower = (int) Math.max(0, (1.0 - SIGMA) * median);
		int upper = (int) Math.min(255, (1.0 + SIGMA) * median);

		// Canny Edge Detection
		Imgproc.Canny(imageGray, imageCny, lower, upper, 3, true);

		// Show the Canny Edge detector image
		// ImageUtils.displayImage(ImageUtils.toBufferedImage(imageCny), "Canny
		// Edge Detection Image");

	}
	
	
	/*
	 * 
0
3008x2000
1
1
0, 8, 20, 103, 312, 772, 1670, 3282, 5229, 7357, 9299, 10926, 12256, 13349, 14637, 16159, 18034, 19343, 20692, 21222, 22006, 22828, 23326, 23881, 25046, 25965, 26657, 27280, 27424, 27815, 27550, 27389, 27231, 27197, 26223, 26164, 25888, 25603, 25115, 24691, 24825, 24552, 24456, 24021, 23918, 23522, 23122, 22691, 22274, 22407, 22007, 21984, 21974, 21637, 21236, 21004, 20334, 20118, 19779, 19267, 19212, 19011, 18679, 18401, 18099, 18214, 18060, 18008, 17958, 18074, 18352, 18003, 18383, 18409, 18432, 18952, 18946, 19517, 19683, 20246, 20861, 21651, 22380, 23305, 24631, 25689, 26710, 27780, 28650, 29246, 29870, 30316, 30850, 31279, 32051, 33116, 33482, 33922, 34387, 34841, 35372, 36445, 36836, 37491, 38331, 39160, 40122, 41415, 41851, 42867, 43891, 45404, 46230, 47655, 48862, 49560, 51515, 52576, 52959, 53870, 53442, 53491, 52912, 53044, 51927, 50962, 49723, 48467, 46989, 45328, 43254, 41587, 39194, 37582, 35353, 33432, 32297, 30670, 28839, 27378, 26246, 25138, 24331, 23553, 22697, 22132, 21838, 21751, 21316, 21077, 20986, 21328, 21533, 21697, 22044, 22204, 22757, 23162, 23290, 24459, 25044, 25488, 25831, 26175, 26638, 26780, 27487, 28383, 30378, 33741, 37141, 38721, 38946, 37393, 35968, 33884, 32034, 30255, 27485, 23742, 20403, 18228, 17609, 18022, 19532, 20579, 20647, 19068, 16314, 14512, 13520, 13158, 12634, 11868, 11642, 11290, 11102, 10968, 10489, 9253, 8609, 8116, 8024, 8130, 8254, 8373, 8263, 7805, 7838, 7638, 8020, 8422, 8567, 8564, 8579, 8811, 9064, 9660, 10150, 10668, 11061, 11713, 12146, 12744, 12816, 12461, 12074, 12162, 12375, 12588, 13889, 15256, 16473, 17243, 17270, 18108, 18395, 18555, 18374, 18356, 17953, 17593, 17417, 17194, 16471, 15945, 15273, 14295, 13853, 13489, 15877, 23713, 26819, 43118, 42455, 52014, 
256
131.0

	 */

	private static double findMedian(int[] histInput) {
		int[] hist = new int[256];
		int sum = 0;
		int i = 0;
		double medianHist = 0;
		for (; (sum < ((histInput.length + 1) / 2)) && (i < hist.length); i++, medianHist++)
			sum += histInput[i];

		if ((histInput.length % 2) == 0 && sum == (histInput.length + 1) / 2) {
			for (; i < hist.length; i++) {
				if (hist[i] != 0) {
					medianHist = (medianHist + (i + 1)) / 2;
					break;
				}
			}
		}
		return sum;
	}

	
	
	
	
	/*
	 * 
0
3008x2000
1
1
0, 8, 20, 103, 312, 772, 1670, 3282, 5229, 7357, 9299, 10926, 12256, 13349, 14637, 16159, 18034, 19343, 20692, 21222, 22006, 22828, 23326, 23881, 25046, 25965, 26657, 27280, 27424, 27815, 27550, 27389, 27231, 27197, 26223, 26164, 25888, 25603, 25115, 24691, 24825, 24552, 24456, 24021, 23918, 23522, 23122, 22691, 22274, 22407, 22007, 21984, 21974, 21637, 21236, 21004, 20334, 20118, 19779, 19267, 19212, 19011, 18679, 18401, 18099, 18214, 18060, 18008, 17958, 18074, 18352, 18003, 18383, 18409, 18432, 18952, 18946, 19517, 19683, 20246, 20861, 21651, 22380, 23305, 24631, 25689, 26710, 27780, 28650, 29246, 29870, 30316, 30850, 31279, 32051, 33116, 33482, 33922, 34387, 34841, 35372, 36445, 36836, 37491, 38331, 39160, 40122, 41415, 41851, 42867, 43891, 45404, 46230, 47655, 48862, 49560, 51515, 52576, 52959, 53870, 53442, 53491, 52912, 53044, 51927, 50962, 49723, 48467, 46989, 45328, 43254, 41587, 39194, 37582, 35353, 33432, 32297, 30670, 28839, 27378, 26246, 25138, 24331, 23553, 22697, 22132, 21838, 21751, 21316, 21077, 20986, 21328, 21533, 21697, 22044, 22204, 22757, 23162, 23290, 24459, 25044, 25488, 25831, 26175, 26638, 26780, 27487, 28383, 30378, 33741, 37141, 38721, 38946, 37393, 35968, 33884, 32034, 30255, 27485, 23742, 20403, 18228, 17609, 18022, 19532, 20579, 20647, 19068, 16314, 14512, 13520, 13158, 12634, 11868, 11642, 11290, 11102, 10968, 10489, 9253, 8609, 8116, 8024, 8130, 8254, 8373, 8263, 7805, 7838, 7638, 8020, 8422, 8567, 8564, 8579, 8811, 9064, 9660, 10150, 10668, 11061, 11713, 12146, 12744, 12816, 12461, 12074, 12162, 12375, 12588, 13889, 15256, 16473, 17243, 17270, 18108, 18395, 18555, 18374, 18356, 17953, 17593, 17417, 17194, 16471, 15945, 15273, 14295, 13853, 13489, 15877, 23713, 26819, 43118, 42455, 52014, 
256
21906.0

	 */
	
//	 private static double findMedian(int[] modArray){
//		 
//
//	 Arrays.sort(modArray);
//	
//	 int mid = modArray.length/2;
//	 double median = 0;
//	 if (modArray.length % 2 == 1)
//		 median = modArray[mid];
//	 else
//		 median = (modArray[mid-1] + modArray[mid]) / 2;
//	
//	 return median;
//	 }

	private static void testSensorStop(UltraSonicSensor sensor) throws InterruptedException {
		GpioHolder gpio = GpioProvision.provisionGpioPins();

		Thread.sleep(10000);

		// GO
		gpio.getPin00().setPwm(100);
		gpio.getPin27().setPwm(100);
		gpio.getPin03().setPwm(100);
		gpio.getPin29().setPwm(100);

		// waitForSensorFront(sensor, gpio.getTrigger(), gpio.getEcho());
		waitForSensorFront(3, sensor, gpio.getTrigger(), gpio.getEcho());

		// STOP
		gpio.getPin00().setPwm(0);
		gpio.getPin27().setPwm(0);
		gpio.getPin03().setPwm(0);
		gpio.getPin29().setPwm(0);

		// BACKWARDS
		gpio.getPin02().setPwm(100);
		gpio.getPin25().setPwm(100);
		gpio.getPin04().setPwm(100);
		gpio.getPin28().setPwm(100);

		Thread.sleep(1000);

		gpio.getPin02().setPwm(0);
		gpio.getPin25().setPwm(0);
		gpio.getPin04().setPwm(0);
		gpio.getPin28().setPwm(0);

		// TURN AROUND
		turnRight(gpio);
		Thread.sleep(4000);

		gpio.getPin00().setPwm(0);
		gpio.getPin25().setPwm(0);
		gpio.getPin03().setPwm(0);
		gpio.getPin28().setPwm(0);

		turnLeft(gpio);
		Thread.sleep(4000);

		gpio.getPin02().setPwm(0);
		gpio.getPin27().setPwm(0);
		gpio.getPin04().setPwm(0);
		gpio.getPin29().setPwm(0);
	}

	private static void turnRight(GpioHolder gpio) {
		gpio.getPin00().setPwm(20); // DR
		gpio.getPin28().setPwm(20); // PR
		gpio.getPin25().setPwm(20); // PF
		gpio.getPin03().setPwm(20); // DF
		gpio.getPin00().setPwm(40); // DR
		gpio.getPin28().setPwm(40); // PR
		gpio.getPin25().setPwm(40); // PF
		gpio.getPin03().setPwm(40); // DF
		gpio.getPin00().setPwm(60); // DR
		gpio.getPin28().setPwm(60); // PR
		gpio.getPin25().setPwm(60); // PF
		gpio.getPin03().setPwm(60); // DF
		gpio.getPin00().setPwm(80); // DR
		gpio.getPin28().setPwm(80); // PR
		gpio.getPin25().setPwm(80); // PF
		gpio.getPin03().setPwm(80); // DF
		gpio.getPin00().setPwm(100); // DR
		gpio.getPin28().setPwm(100); // PR
		gpio.getPin25().setPwm(100); // PF
		gpio.getPin03().setPwm(100); // DF
	}

	private static void turnLeft(GpioHolder gpio) {
		gpio.getPin02().setPwm(20); // DR
		gpio.getPin29().setPwm(20); // PR
		gpio.getPin27().setPwm(20); // PF
		gpio.getPin04().setPwm(20); // DF
		gpio.getPin02().setPwm(40); // DR
		gpio.getPin29().setPwm(40); // PR
		gpio.getPin27().setPwm(40); // PF
		gpio.getPin04().setPwm(40); // DF
		gpio.getPin02().setPwm(60); // DR
		gpio.getPin29().setPwm(60); // PR
		gpio.getPin27().setPwm(60); // PF
		gpio.getPin04().setPwm(60); // DF
		gpio.getPin02().setPwm(80); // DR
		gpio.getPin29().setPwm(80); // PR
		gpio.getPin27().setPwm(80); // PF
		gpio.getPin04().setPwm(80); // DF
		gpio.getPin02().setPwm(100); // DR
		gpio.getPin29().setPwm(100); // PR
		gpio.getPin27().setPwm(100); // PF
		gpio.getPin04().setPwm(100); // DF
	}

	private static void waitForSensorFront(double distanceLimit, UltraSonicSensor sensor, GpioPinDigitalOutput trigger,
			GpioPinDigitalInput echo) {
		sensor.retrieveDistance(trigger, echo);
	}

}
