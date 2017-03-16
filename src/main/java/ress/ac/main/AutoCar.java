package ress.ac.main;

import java.util.ArrayList;
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
import ress.ac.utility.ImageUtility;

public class AutoCar {

	private static UltraSonicSensor sensor = new UltraSonicSensor();
//	 public static String path = "src/main/resources/lena.png";
//	 public static String path = "src/main/resources/the-elder-scrolls.jpg";
//	 public static String path = "src/main/resources/output.jpg";
//	public static String path = "src/main/resources/house.jpeg";
	public static String path = "src/main/resources/obstacle.jpg";

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
		Mat rgbImage = Imgcodecs.imread(path);
		Mat imageGray = new Mat();
		Mat imageGrayBlur = new Mat();
		Mat imageCny = new Mat();

		Imgproc.cvtColor(rgbImage, imageGray, Imgproc.COLOR_RGB2GRAY);
		Imgproc.medianBlur(imageGray, imageGrayBlur, 5);

//Create histogram ~3ms
		MatOfFloat range = new MatOfFloat(0f, 256f);
		MatOfInt histSize = new MatOfInt(256);
		MatOfInt output = new MatOfInt();
		List<Mat> images = new ArrayList<Mat>();
		images.add(imageGrayBlur);
		Imgproc.calcHist(images, new MatOfInt(0), new Mat(), output, histSize, range);
		MatOfInt hist = new MatOfInt(CvType.CV_32S);
		output.convertTo(hist, CvType.CV_32S);
		int[] histInput = hist.toArray();
		double median = ImageUtility.findMedian(histInput, imageGrayBlur.total());
		
//		int lower = (int) (.66 * median);
//		int upper = (int) (1.33 * median);
		int lower = (int) Math.max(0, (1.0 - SIGMA) * median);
		int upper = (int) Math.min(255, (1.0 + SIGMA) * median);

		Imgproc.Canny(imageGrayBlur, imageCny, lower, upper);
		
		ImageUtility.sideFill(imageCny);
		
		ImageUtils.displayImage(ImageUtils.toBufferedImage(rgbImage), "RGB Image");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageGray), "Gray Image");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageGrayBlur), "Gray Blur Image");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageCny), "Canny Edge Detection Image");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageCny), "Side Fill");
	}
	
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
