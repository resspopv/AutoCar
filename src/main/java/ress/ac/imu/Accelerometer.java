package ress.ac.imu;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

public class Accelerometer {

	private static I2CDevice accSensor;
	
	public static boolean initialized = false;
	
//	private static final int ACC_ADDRESS = 0x1E;
//	private static final int CTRL_REG1_XM = 0x20;
//	private static final int CTRL_REG2_XM = 0x21;
//	private static final int AXIS_UPDATE_DATA_RATE = 0b01100111; 
//	private static final int SCALE = 0b00100000; 
//	private static final int OUT_X_L_A = 0x28;
	
	
	
	private static final int ACC_ADDRESS = 0x6b;
	private static final int CTRL1_XL = 0x10;
	private static final byte POWER_SCALE = 0b01000100; 
	private static final int OUTX_L_XL = 0x28;
	
	
	
	
	private static final double PI = 3.14159265358979323846;
	private static final double RAD_TO_DEG = 57.29578;
	
	public static void initialize(I2CBus bus){
		try {
			accSensor = bus.getDevice(ACC_ADDRESS);
		} catch (IOException e) {
			Logger.getLogger("ACC").log(Level.SEVERE, "Error getting device");
			e.printStackTrace();
			e.printStackTrace();
		}
		
		try {
			accSensor.write(CTRL1_XL, POWER_SCALE);
		} catch (IOException e) {
			Logger.getLogger("ACC").log(Level.SEVERE, "Error writing device");
			e.printStackTrace();
		}
		
		initialized = true;
	}
	
	public static void readRawValues(IMUValues imuValues){
		byte[] buffer = new byte[6];
		
		try {
			accSensor.read(OUTX_L_XL, buffer, 0, 6);
		} catch (IOException e) {
			Logger.getLogger("ACC").log(Level.SEVERE, "Error reading device");
			e.printStackTrace();
		}
		
		imuValues.getAccRawValues()[0] = buffer[0] + (buffer[1] << 8);
		imuValues.getAccRawValues()[1] = buffer[2] + (buffer[3] << 8);
		imuValues.getAccRawValues()[2] = buffer[4] + (buffer[5] << 8);
	}
	
	public static void convertRawToDegrees(IMUValues imuValues){
		imuValues.setAccAngleX((Math.atan2(imuValues.getAccRawValues()[1], imuValues.getAccRawValues()[2]) + PI) * RAD_TO_DEG);
		imuValues.setAccAngleY((Math.atan2(imuValues.getAccRawValues()[2], imuValues.getAccRawValues()[0]) + PI) * RAD_TO_DEG);
		imuValues.setAccAngleZ((Math.atan2(imuValues.getAccRawValues()[1], imuValues.getAccRawValues()[0]) + PI) * RAD_TO_DEG);
	}
	
	public static void changeRotationAngle(IMUValues imuValues){
		if (imuValues.getAccAngleX() > 180)
			imuValues.setAccAngleX(imuValues.getAccAngleX() - 360);
		if (imuValues.getAccAngleY() > 180)
			imuValues.setAccAngleY(imuValues.getAccAngleY() - 360);
		if (imuValues.getAccAngleZ() > 180)
			imuValues.setAccAngleZ(imuValues.getAccAngleZ() - 360);
		
		
//		if (imuValues.getAccAngleX() > 180)
//			imuValues.setAccAngleX(imuValues.getAccAngleX() - 360);
//		
//		imuValues.setAccAngleY(imuValues.getAccAngleY() - 90);
//		if (imuValues.getAccAngleY() > 180)
//			imuValues.setAccAngleY(imuValues.getAccAngleY() - 360);
		
	}
	
	private static void calibrate(){
		
	}
	
	public static boolean isInitialized() {
		return initialized;
	}
}
