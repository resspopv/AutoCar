package ress.ac.main;

import static ress.ac.constant.GlobalConstants.CM_TO_INCHES;
import static ress.ac.constant.GlobalConstants.DISTANCE;
import static ress.ac.constant.GlobalConstants.SOUND_SPEED;
import static ress.ac.constant.GlobalConstants.TIMEOUT;
import static ress.ac.constant.GlobalConstants.TRIG_DURATION_IN_MICROS;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class UltraSonicSensor {
	private ExecutorService executor = Executors.newCachedThreadPool();

    public void retrieveDistance(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) {
    	Map<String, Future<Double>> m = new ConcurrentHashMap<String, Future<Double>>();
    	while(true){
	    	Future<Double> f = executor.submit(this.callable(trigger, echo));
	    	m.put(f.getClass().toString(), f);
	    	
//	    	DOUBLE D = NULL;
//	    	TRY {
//				D = F.GET();
//			} CATCH (INTERRUPTEDEXCEPTION | EXECUTIONEXCEPTION E) {
//				E.PRINTSTACKTRACE();
//			}
//	    	IF (D <= DISTANCE)
//	    		BREAK;
    	}
    	
    }    
    private Callable<Double> triggerSensor(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) {
    	trigger.high();
    	try {
			Thread.sleep(0, TRIG_DURATION_IN_MICROS * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	trigger.low();
    	
		return null;
    }
    
    private double measureDistance(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) {
    	triggerSensor(trigger, echo);
    	
		waitForSignal(echo);
		double duration = 0;
		duration = measureSignal(echo);
    	
    	return (duration * SOUND_SPEED / (DISTANCE * 10000)) * CM_TO_INCHES;
    }
    private double measureSignal(GpioPinDigitalInput echo) {
    	int count = TIMEOUT;
    	long start = System.nanoTime();
    	
    	while (echo.isHigh() && count > 0)
    		count--;
    	
    	long stop = System.nanoTime();
    	
//    	IF (COUNT <= 0)
//    		THROW NEW TIMEOUTEXCEPTION("TIMEOUT MEASURING");
    	
    	return Math.ceil((stop - start) / 1000.0);
    }
    
    private void waitForSignal(GpioPinDigitalInput echo) {
    	int count = TIMEOUT;
    	
    	while (echo.isLow() && count > 0)
    		count--;
    	
//    	IF (COUNT <= 0)
//    		THROW NEW TIMEOUTEXCEPTION("TIMEOUT WAITING");
    }

    Callable<Double> callable(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo){
    	return () -> {
    		return this.measureDistance(trigger, echo);
    	};
    }
		
}

