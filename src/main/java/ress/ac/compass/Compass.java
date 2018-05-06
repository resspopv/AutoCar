package ress.ac.compass;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class Compass {
	private final static int SENSOR_ADDRESS = 0x1E; // I2C bus address
	private final static int SENSOR_X_ADR = 0x03; // X address when written to bus
	private final static int SENSOR_Y_ADR = 0x07; // Y address when written to bus
	private final static int SENSOR_Z_ADR = 0x05; // Z address when written to bus

	private final static byte SENSOR_SAMPLE_RATE = 0x70; // Set to 8 samples @ 15Hz.
	private final static byte SENSOR_GAIN = 0x20; // 1.3 gain LSb / Gauss 1090 (default)
	private final static byte SENSOR_CONTINUOUS_SAMPLING = 0x00;
	private final static float SCALE = 0.92f; // Scale for gain
	
//	private final static int X_OFFSET = -2;
//	PRIVATE FINAL STATIC Int Y_OFFSET = -45;
//	private final static int X_OFFSET = 235;
//	private final static int Y_OFFSET = -412;
	private final static int X_OFFSET = 117;
	private final static int Y_OFFSET = -294;
	private final static double DECLINATION = 0.045675267;

	private I2CBus bus;
	private I2CDevice sensor;

	public Compass() {
		try {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);
			sensor = bus.getDevice(SENSOR_ADDRESS);
        	sensor.write(0x00, SENSOR_SAMPLE_RATE);
        	sensor.write(0x01, SENSOR_GAIN);
			sensor.write(0x02, SENSOR_CONTINUOUS_SAMPLING);
		} catch (UnsupportedBusNumberException | IOException e) {
			e.printStackTrace();
		}
	}

	public double retrieveHeadingDegrees() {
		double xValue = (readWord(SENSOR_X_ADR) - X_OFFSET) * SCALE;
		double yValue = (readWord(SENSOR_Y_ADR) - Y_OFFSET) * SCALE;
		double zValue = readWord(SENSOR_Z_ADR) * SCALE;
		
		double heading = Math.atan2(yValue, xValue);
//		heading += DECLINATION;
		if (heading < 0)
            heading += (2 * Math.PI);
   
//	    if (heading > 2 * Math.PI)
//	    	heading -= 2 * Math.PI;

		return Math.toDegrees(heading);
	}

	private short readWord(int regAddress) {
		byte high = 0;
		byte low = 0;
		try {
			high = (byte) sensor.read(regAddress);
			low = (byte) sensor.read(regAddress + 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		short reading = (short) ((high << 8) + low); // Little endian
		
		if (reading >= 0x8000)
			reading = (short) -((0xFFFF - reading) + 1);
		
		return reading;
	}
	
	public void calibrateCompass(int loopAmount, int pauseDuration){
        PrintWriter w = null;
        try {
			w = new PrintWriter("output.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        
        int xMin = 0;
        int xMax = 0;
        int yMin = 0;
        int yMax = 0;
        int zMin = 0;
        int zMax = 0;
        
        int xScale = 0;
        int yScale = 0;
        int zScale = 0;

        for (int i = 0; i < loopAmount; i++){
			double xValue = (readWord(SENSOR_X_ADR)) * SCALE;
			double yValue = (readWord(SENSOR_Y_ADR)) * SCALE;
			double zValue = (readWord(SENSOR_Z_ADR)) * SCALE;
			
			if ((int)xValue < xMin)
				xMin = (int) xValue;
			if ((int)yValue < yMin)
				yMin = (int) yValue;
			if ((int)xValue > xMax)
				xMax = (int) xValue;
			if ((int)yValue > yMax)
				yMax = (int) yValue;
			if ((int)zValue < zMin)
				zMin = (int) zValue;
			if ((int)zValue > zMax)
				zMax = (int) xValue;
			
			try {
				Thread.sleep(pauseDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
        }
//		int fieldValue = (xMax + yMax + zMax) / 3;
//		xScale = fieldValue / xMax;
//		yScale = fieldValue / yMax;
//		zScale = fieldValue / zMax;
		
        w.write("X-MIN: " + xMin + " : ");
        w.write("X-MAX: " + xMax + " : ");
        w.write("Y-MIN: " + yMin + " : ");
        w.write("Y-MAX: " + yMax + " : ");
        w.write("X Offset: " + (xMax + xMin)/2);
        w.write(" : ");
        w.write("Y Offset: " + (yMax + yMin)/2);
        w.write(" : ");
        w.write("Z Offset: " + (zMax + zMin)/2);
//        w.write(" : ");
//        w.write("X Scale: " + (xScale));
//        w.write(" : ");
//        w.write("Y Scale: " + (yScale));
//        w.write(" : ");
//        w.write("Z Scale: " + (zScale));
        w.close();
	}
}