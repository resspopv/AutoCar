package ress.ac.imu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

public class Gyro {	
	
	private static I2CDevice gyroSensor;
	
	public static boolean initialized = false;
	public static boolean calibrated = false;
	
//	private static final int GYRO_ADDRESS = 0x6A;
//	private static final int CTRL_REG1_G = 0x20;
//	private static final int CTRL_REG4_G = 0x23;
//	private static final int POWER_MODE_AXIS = 0b00001111; 
//	private static final int UPDATE_DPS_SCALE = 0b00110000; 
//	private static final int OUT_X_L_G = 0x28;
//	private static final double GAIN = 0.07;
	
	
	private static final int GYRO_ADDRESS = 0x6b;
	private static final int CTRL2_G = 0x11;
	private static final byte POWER_DPS = 0b00011100; 
	private static final int OUTX_L_G = 0x22;
	private static final double GAIN = 0.07;
	
	private static double avgXValue = 0;
	private static double avgYValue = 0;
	private static double avgZValue = 0;
	
	private static double maxXValue = 0;
	private static double maxYValue = 0;
	private static double maxZValue = 0;
	
	private static double minXValue = 0;
	private static double minYValue = 0;
	private static double minZValue = 0;
	
	public static void initialize(I2CBus bus){
		try {
			gyroSensor = bus.getDevice(GYRO_ADDRESS);
		} catch (IOException e) {
			Logger.getLogger("Gyro").log(Level.SEVERE, "Error getting device");
			e.printStackTrace();
		}
		
		try {
			gyroSensor.write(CTRL2_G, POWER_DPS);
		} catch (IOException e) {
			Logger.getLogger("Gyro").log(Level.SEVERE, "Error writing device");
			e.printStackTrace();
		}
		
		initialized = true;
	}
	
	public static void readRawValues(IMUValues imuValues){
		byte[] buffer = new byte[6];
		
		try {
			gyroSensor.read(OUTX_L_G, buffer, 0, 6);
		} catch (IOException e) {
			Logger.getLogger("Gyro").log(Level.SEVERE, "Error reading device");
			e.printStackTrace();
		}
		
		imuValues.getGyroRawValues()[0] = buffer[0] + (buffer[1] << 8);
		imuValues.getGyroRawValues()[1] = buffer[2] + (buffer[3] << 8);
		imuValues.getGyroRawValues()[2] = buffer[4] + (buffer[5] << 8);
	}
	
	public static void convertRawToDegreesPerSecond(IMUValues imuValues){
		imuValues.setGryoDegreesPerSecondX(imuValues.getGyroRawValues()[0] * GAIN);
		imuValues.setGryoDegreesPerSecondY(imuValues.getGyroRawValues()[1] * GAIN);
		imuValues.setGryoDegreesPerSecondZ(imuValues.getGyroRawValues()[2] * GAIN);
	}
	
	public static void calculateAngles(IMUValues imuValues, double deltaTime){
		imuValues.setGyroAngleX(imuValues.getGyroAngleX() + (imuValues.getGryoDegreesPerSecondX() * (((double)deltaTime) / 1000)));
		imuValues.setGyroAngleY(imuValues.getGyroAngleY() + (imuValues.getGryoDegreesPerSecondY() * (((double)deltaTime) / 1000)));
		imuValues.setGyroAngleZ(imuValues.getGyroAngleZ() + (imuValues.getGryoDegreesPerSecondZ() * (((double)deltaTime) / 1000)));
		
		applyCalibration(imuValues);
	}
	
	private static void applyCalibration(IMUValues imuValues){
		if (imuValues.getGyroAngleX() >= minXValue && imuValues.getGyroAngleX() <= maxXValue)
			imuValues.setGyroAngleX(0);
		else
			imuValues.setGyroAngleX(imuValues.getGyroAngleX() - avgXValue);
		
		if (imuValues.getGyroAngleY() >= minYValue && imuValues.getGyroAngleY() <= maxYValue)
			imuValues.setGyroAngleY(0);
		else
			imuValues.setGyroAngleY(imuValues.getGyroAngleY() - avgYValue);
		
		if (imuValues.getGyroAngleZ() >= minZValue && imuValues.getGyroAngleZ() <= maxZValue)
			imuValues.setGyroAngleZ(0);
		else
			imuValues.setGyroAngleZ(imuValues.getGyroAngleZ() - avgZValue);
	}
	
	@SuppressWarnings("unchecked")
	public static void calibrate(long delayTime){
		Map<String, Double>[] readings = new Map[100];
		IMUValues imuCalValues = new IMUValues();
		
		for (int i = 0; i < 100; i++){
			readRawValues(imuCalValues);
			convertRawToDegreesPerSecond(imuCalValues);
			calculateAngles(imuCalValues, ((double)delayTime) / 1000);
			
			Map<String, Double> values = new HashMap<String, Double>();
			values.put("X", imuCalValues.getGyroAngleX());
			values.put("Y", imuCalValues.getGyroAngleY());
			values.put("Z", imuCalValues.getGyroAngleZ());
			
			readings[i] = values;
			
			try {
				Thread.sleep(delayTime);
			} catch (InterruptedException e) {
				Logger.getLogger("Gyro").log(Level.SEVERE, "Error sleeping");
				e.printStackTrace();
			}
		}
		
		computeAvg(readings);
		
		computeMinMax(readings);
		
		calibrated = true;
		
		System.out.println("---------------------------------------------------------------------------------------------------------");
		System.out.println("GYRO CALIBRATION");
		System.out.println("Gyro X:   MinX: " + minXValue + " | MaxX: " + maxXValue + " | avgX: " + avgXValue);
		System.out.println("Gyro Y:   MinY: " + minYValue + " | MaxY: " + maxYValue + " | avgY: " + avgYValue);
		System.out.println("Gyro Z:   MinZ: " + minZValue + " | MaxZ: " + maxZValue + " | avgZ: " + avgZValue);
		System.out.println("---------------------------------------------------------------------------------------------------------");
	}
	
	private static void computeAvg(Map<String, Double>[] readings){
		double combinedXValues = 0;
		double combinedYValues = 0;
		double combinedZValues = 0;
		
		for (int i = 0; i < readings.length; i++){
			combinedXValues += readings[i].get("X");
			combinedYValues += readings[i].get("Y");
			combinedZValues += readings[i].get("Z");
		}
		
		avgXValue = combinedXValues / readings.length;
		avgYValue = combinedYValues / readings.length;
		avgZValue = combinedZValues / readings.length;
	}
	
	private static void computeMinMax(Map<String, Double>[] readings){
		for (int i = 0; i < readings.length; i++){
			double tempXValue = readings[i].get("X");
			double tempYValue = readings[i].get("Y");
			double tempZValue = readings[i].get("Z");
			
			if (tempXValue > maxXValue)
				maxXValue = tempXValue;
			if (tempXValue < minXValue)
				minXValue = tempXValue;
			
			if (tempYValue > maxYValue)
				maxYValue = tempYValue;
			if (tempYValue < minYValue)
				minYValue = tempYValue;
			
			if (tempZValue > maxZValue)
				maxZValue = tempZValue;
			if (tempZValue < minZValue)
				minZValue = tempZValue;
		}
	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static boolean isCalibrated() {
		return calibrated;
	}
}
