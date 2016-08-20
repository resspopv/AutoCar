package ress.ac.main;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import ress.ac.exception.TimeOutException;

public class UltraSonicSensor {
	private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s
	private final static double CM_TO_INCHES = 0.3937;
	private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s
    private final static int TIMEOUT = 2100;
    private final static double DISTANCE = 3;
    
    public double retrieveDistance(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) throws InterruptedException, TimeOutException{
//		double distance = measureDistance(trigger, echo);
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
