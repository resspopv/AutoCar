package ress.ac.compass;

import java.io.File;
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
	
	private final static int X_OFFSET = -263;
	private final static int Y_OFFSET = -70;
	private final static double DECLINATION = -2.37;

	private I2CBus bus;
	private I2CDevice sensor;

	public Compass() {
		try {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);
			sensor = bus.getDevice(SENSOR_ADDRESS);
		} catch (UnsupportedBusNumberException | IOException e) {
			e.printStackTrace();
		}
	}

	public double retrieveHeadingDegrees() {
//		byte[] i2cWriteInput = new byte[] { SENSOR_SAMPLE_RATE, SENSOR_GAIN, SENSOR_CONTINUOUS_SAMPLING };
//		try {
//			sensor.write(i2cWriteInput, 0, 3);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        try {
        	sensor.write(0x00, SENSOR_SAMPLE_RATE);
        	sensor.write(0x01, SENSOR_GAIN);
			sensor.write(0x02, SENSOR_CONTINUOUS_SAMPLING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

		double xValue = (readWord(SENSOR_X_ADR) - X_OFFSET) * SCALE;
		double yValue = (readWord(SENSOR_Y_ADR) - Y_OFFSET) * SCALE;
		double zValue = readWord(SENSOR_Z_ADR) * SCALE;
		
		double heading = Math.atan2(yValue, xValue);
		if (heading < 0)
			heading += 2 * Math.PI;

		heading = (heading * 180 / Math.PI) + DECLINATION;

		return heading;
	}

	private short readWord(int regAddress) {
//		 byte high = 0;
//		 byte low = 0;
//		try {
//			high = (byte)(sensor.read(regAddress) & 0xFF);
//			low = (byte)(sensor.read(regAddress + 1) & 0xFF);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(high);
//		System.out.println(low);
//		 short reading = (short)(((high << 8) + low) & 0xFFFF);
		
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
}