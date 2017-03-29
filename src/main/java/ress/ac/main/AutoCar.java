package ress.ac.main;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import ress.ac.compass.Compass;
import ress.ac.gpio.GpioHolder;
import ress.ac.image.ImageCreation;

public class AutoCar {

//	private final static GpioHolder gpio = GpioProvision.provisionGpioPins();
	private static UltraSonicSensor sensor = new UltraSonicSensor();

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	

	public static void main(String[] args) {
		Compass compass = new Compass();
		for (int i = 0; i < 5000; i++){
			System.out.println(compass.retrieveHeadingDegrees());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		detectEdges();

		// try {
		// testSensorStop(sensor);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	private static void detectEdges() {
		Mat rgbImage = ImageCreation.retrieveOriginalImage();
		Mat imageGray = ImageCreation.convertToGrayImage(rgbImage);
		Mat imageGrayBlur = ImageCreation.blurImage(imageGray);
		Mat imageCny = ImageCreation.createCannyImage(imageGrayBlur);
		Mat imageSideFill = ImageCreation.sideFill(imageCny);
		Mat imageErode = ImageCreation.erodeImage(imageSideFill);
		
		ImageUtils.displayImage(ImageUtils.toBufferedImage(rgbImage), "RGB Image");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageGray), "Gray Image");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageGrayBlur), "Gray Blur Image");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageCny), "Canny Edge Detection Image");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageSideFill), "Side Fill");
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageErode), "Erode");
	}
	
	private static void testSensorStop(UltraSonicSensor sensor) throws InterruptedException {
		Thread.sleep(10000);

//		Move.goForward(gpio);

		// waitForSensorFront(sensor, gpio.getTrigger(), gpio.getEcho());
//		waitForSensorFront(3, sensor, gpio.getTrigger(), gpio.getEcho());

//		Move.stop(gpio);

//		Move.reverse(gpio);

		Thread.sleep(1000);

//		Move.stop(gpio);

		// TURN AROUND
//		turnRight(gpio);
		Thread.sleep(4000);

//		Move.stop(gpio);

//		turnLeft(gpio);
		Thread.sleep(4000);

//		Move.stop(gpio);
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

	private static void waitForSensorFront(double distanceLimit, UltraSonicSensor sensor, GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) {
		sensor.retrieveDistance(trigger, echo);
	}

}
