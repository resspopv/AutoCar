package ress.ac.main;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import ress.ac.exception.TimeOutException;
import ress.ac.gpio.GpioHolder;
import ress.ac.gpio.GpioProvision;

public class AutoCar {
	
    public static void main(String[] args) {
    	UltraSonicSensor sensor = new UltraSonicSensor();
    	
    	try {
    		testSensorStop(sensor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }	
    
    private static void testSensorStop(UltraSonicSensor sensor) throws InterruptedException{
    	GpioHolder gpio = GpioProvision.provisionGpioPins();
    	
    	Thread.sleep(10000);
    	
    	//GO
    	gpio.getPin00().setPwm(100);
    	gpio.getPin27().setPwm(100);
    	gpio.getPin03().setPwm(100);
    	gpio.getPin29().setPwm(100);
    	
    	waitForSensorFront(2, sensor, gpio.getTrigger(), gpio.getEcho());
    	
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
    	
    	Thread.sleep(2000);
    	
    	gpio.getPin02().setPwm(0);
    	gpio.getPin25().setPwm(0);
    	gpio.getPin04().setPwm(0);
    	gpio.getPin28().setPwm(0);
    	
//    	TURN AROUND
    	turnRight(gpio);
    	
    	Thread.sleep(2000);
    	
    	gpio.getPin00().setPwm(0);
    	gpio.getPin25().setPwm(0);
    	gpio.getPin03().setPwm(0);
    	gpio.getPin28().setPwm(0);
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
    
    private static void waitForSensorFront(double distanceLimit, UltraSonicSensor sensor, GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) throws InterruptedException{
    	while (true){
			try {
				double distance = sensor.retrieveDistance(trigger, echo);
				System.out.println(distance + " : ");
				if (distance < distanceLimit)
					break;
			} catch (TimeOutException e) {
				System.err.println(e.getMessage());;
			}
    	}
    }
    
}
