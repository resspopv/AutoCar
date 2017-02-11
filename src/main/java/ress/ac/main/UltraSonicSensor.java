package ress.ac.main;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import ress.ac.exception.TimeOutException;

//public class UltraSonicSensor {
//	private final static float SOUND_SPEED = 340.29f;  
//	private final static double CM_TO_INCHES = 0.3937;
//	private final static int TRIG_DURATION_NANO = 10000;
//    private final static int WAIT_DURATION_MILLIS = 50;
//    private final static int TIMEOUT = 2100;
//    
//    public double retrieveDistance(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) throws InterruptedException, TimeOutException{
//		Thread.sleep(WAIT_DURATION_MILLIS);
//
//		triggerSensor(trigger);
//		waitForSignal(echo);
//		
//		double duration = measureSignal(echo);
//    	
//		return (duration * SOUND_SPEED/20000) * CM_TO_INCHES;
//    }
//    
//    private void triggerSensor(GpioPinDigitalOutput trigger) throws InterruptedException{
//    	trigger.high();
//    	Thread.sleep(0, TRIG_DURATION_NANO);
//    	trigger.low();
//    }
//    
//    private double measureSignal(GpioPinDigitalInput echo) throws TimeOutException{
//    	long start = System.nanoTime();
//    	long startMillis = System.currentTimeMillis();
//    	
//    	while (echo.isHigh() && System.currentTimeMillis() - startMillis < WAIT_DURATION_MILLIS){
//    	}
//    	
//    	long stop = System.nanoTime();
//    	
//    	return Math.ceil((stop - start) / 1000.0);
//    }
//    
//    private void waitForSignal(GpioPinDigitalInput echo) throws TimeOutException{
//    	long start = System.nanoTime();
//    	
//    	while (echo.isLow() && System.currentTimeMillis() - start < WAIT_DURATION_MILLIS){
//    	}
//    }
//}









public class UltraSonicSensor {
	private final static float SOUND_SPEED = 340.29f;  
	private final static double CM_TO_INCHES = 0.3937;
	private final static int TRIG_DURATION_IN_MICROS = 10; 
    private final static int WAIT_DURATION_IN_MILLIS = 60; 
    private final static int TIMEOUT = 2100;
    private final static double DISTANCE = 3;
    
    public double retrieveDistance(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) throws InterruptedException, TimeOutException{
		Thread.sleep(WAIT_DURATION_IN_MILLIS);
    	
		return measureDistance(trigger, echo);
    }
    
    private void triggerSensor(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) throws InterruptedException{
    	trigger.high();
    	Thread.sleep(0, TRIG_DURATION_IN_MICROS * 1000);
    	trigger.low();
    }
    
    private double measureDistance(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) throws InterruptedException, TimeOutException{
    	triggerSensor(trigger, echo);
    	
		waitForSignal(echo);
		double duration = measureSignal(echo);
    	
    	return (duration * SOUND_SPEED / (DISTANCE * 10000)) * CM_TO_INCHES;
    }
    private double measureSignal(GpioPinDigitalInput echo) throws TimeOutException{
    	int count = TIMEOUT;
    	long start = System.nanoTime();
    	
    	while (echo.isHigh() && count > 0)
    		count--;
    	
    	long stop = System.nanoTime();
    	
    	if (count <= 0)
    		throw new TimeOutException("TIMEOUT MEASURING");
    	
    	return Math.ceil((stop - start) / 1000.0);
    }
    
    private void waitForSignal(GpioPinDigitalInput echo) throws TimeOutException{
    	int count = TIMEOUT;
    	
    	while (echo.isLow() && count > 0)
    		count--;
    	
    	if (count <= 0)
    		throw new TimeOutException("TIMEOUT WAITING");
    }
		
}

