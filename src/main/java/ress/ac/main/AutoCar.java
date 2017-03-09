package ress.ac.main;

import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import ress.ac.gpio.GpioHolder;
import ress.ac.gpio.GpioProvision;

public class AutoCar {
	
	private static UltraSonicSensor sensor = new UltraSonicSensor();
//	public static String path = "src/main/resources/lena.png";
//	public static String path = "src/main/resources/the-elder-scrolls.jpg";
//	public static String path = "src/main/resources/output.jpg";
	public static String path = "src/main/resources/house.jpeg";
	
	private static final double SIGMA = 0.33;

	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
    public static void main(String[] args) {
    	Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
    	System.out.println("mat= " + mat.dump());
    	
    	detectEdges();
    	
//    	try {
//    		testSensorStop(sensor);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
    }	
    
    private static void detectEdges() {
		//read the RGB image
		 Mat rgbImage = Imgcodecs.imread(path);
	    
		//mat gray image holder
		 MatOfDouble imageGray = new MatOfDouble(CvType.CV_64F);
		
		//mat canny image
		Mat imageCny = new Mat();
		
		//Show the RGB Image
		ImageUtils.displayImage(ImageUtils.toBufferedImage(rgbImage), "RGB Image");

		
		//Convert the image in to gray image
		Imgproc.cvtColor(rgbImage, imageGray, Imgproc.COLOR_RGB2GRAY);
		
		//Show the gray image
		ImageUtils.displayImage(ImageUtils.toBufferedImage(imageGray), "Gray Image");

		System.out.println(imageGray.type());
		System.out.println(imageGray.size());
		double median = findMedian(imageGray);
        int lower = (int) Math.max(0, (1.0 - SIGMA) * median);
        int upper = (int) Math.min(255, (1.0 + SIGMA) * median);
        
		//Canny Edge Detection
		Imgproc.Canny(imageGray, imageCny, lower, upper, 3, true);
		
		//Show the Canny Edge detector image
	    ImageUtils.displayImage(ImageUtils.toBufferedImage(imageCny), "Canny Edge Detection Image");
		
	}
    
    private static double findMedian(Mat imageGray){
    	 MatOfDouble mod = new MatOfDouble(CvType.CV_64F);
    	 imageGray.convertTo(mod, CvType.CV_64F);
         double[] modArray = mod.toArray();
         Arrays.sort(modArray);
        
         int mid = modArray.length/2;
         double median = 0;
         if (modArray.length % 2 == 1)
                 median = modArray[mid];
         else
                 median = (modArray[mid-1] + modArray[mid]) / 2;
        
         return median;
    }
    
    private static void testSensorStop(UltraSonicSensor sensor) throws InterruptedException{
    	GpioHolder gpio = GpioProvision.provisionGpioPins();
    	
    	Thread.sleep(10000);
    	
    	//GO
    	gpio.getPin00().setPwm(100);
    	gpio.getPin27().setPwm(100);
    	gpio.getPin03().setPwm(100);
    	gpio.getPin29().setPwm(100);
    	
//		waitForSensorFront(sensor, gpio.getTrigger(), gpio.getEcho());
    	waitForSensorFront(3, sensor, gpio.getTrigger(), gpio.getEcho());
    	
    	//STOP
    	gpio.getPin00().setPwm(0);
    	gpio.getPin27().setPwm(0);
    	gpio.getPin03().setPwm(0);
    	gpio.getPin29().setPwm(0);
    	
    	//BACKWARDS
    	gpio.getPin02().setPwm(100);
    	gpio.getPin25().setPwm(100);
    	gpio.getPin04().setPwm(100);
    	gpio.getPin28().setPwm(100);
    	
    	Thread.sleep(1000);
    	
    	gpio.getPin02().setPwm(0);
    	gpio.getPin25().setPwm(0);
    	gpio.getPin04().setPwm(0);
    	gpio.getPin28().setPwm(0);
    	
//    	TURN AROUND
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
    
    private static void turnRight(GpioHolder gpio){
    	gpio.getPin00().setPwm(20); //DR
    	gpio.getPin28().setPwm(20); //PR
    	gpio.getPin25().setPwm(20); //PF
    	gpio.getPin03().setPwm(20); //DF
    	gpio.getPin00().setPwm(40); //DR
    	gpio.getPin28().setPwm(40); //PR
    	gpio.getPin25().setPwm(40); //PF
    	gpio.getPin03().setPwm(40); //DF
    	gpio.getPin00().setPwm(60); //DR
    	gpio.getPin28().setPwm(60); //PR
    	gpio.getPin25().setPwm(60); //PF
    	gpio.getPin03().setPwm(60); //DF
    	gpio.getPin00().setPwm(80); //DR
    	gpio.getPin28().setPwm(80); //PR
    	gpio.getPin25().setPwm(80); //PF
    	gpio.getPin03().setPwm(80); //DF
    	gpio.getPin00().setPwm(100); //DR
    	gpio.getPin28().setPwm(100); //PR
    	gpio.getPin25().setPwm(100); //PF
    	gpio.getPin03().setPwm(100); //DF
    }
    
    private static void turnLeft(GpioHolder gpio){
    	gpio.getPin02().setPwm(20); //DR
    	gpio.getPin29().setPwm(20); //PR
    	gpio.getPin27().setPwm(20); //PF
    	gpio.getPin04().setPwm(20); //DF
    	gpio.getPin02().setPwm(40); //DR
    	gpio.getPin29().setPwm(40); //PR
    	gpio.getPin27().setPwm(40); //PF
    	gpio.getPin04().setPwm(40); //DF
    	gpio.getPin02().setPwm(60); //DR
    	gpio.getPin29().setPwm(60); //PR
    	gpio.getPin27().setPwm(60); //PF
    	gpio.getPin04().setPwm(60); //DF
    	gpio.getPin02().setPwm(80); //DR
    	gpio.getPin29().setPwm(80); //PR
    	gpio.getPin27().setPwm(80); //PF
    	gpio.getPin04().setPwm(80); //DF
    	gpio.getPin02().setPwm(100); //DR
    	gpio.getPin29().setPwm(100); //PR
    	gpio.getPin27().setPwm(100); //PF
    	gpio.getPin04().setPwm(100); //DF
    }
        
    private static void waitForSensorFront(double distanceLimit, UltraSonicSensor sensor, GpioPinDigitalOutput trigger, GpioPinDigitalInput echo){
    	sensor.retrieveDistance(trigger, echo);
    }
    
}
