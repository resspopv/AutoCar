package ress.ac.imu;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class IMU {

	private IMUValues imuValues;
	private final long DELAY_TIME_MS = 20;
	private final double FILTER_CONSTRAINT = 0.98;
	
	private I2CBus bus;
	
	public IMU(){
		try {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);
		} catch (UnsupportedBusNumberException | IOException e) {
			Logger.getLogger("IMU").log(Level.SEVERE, "Error creating bus");
			e.printStackTrace();
		}
		imuValues = new IMUValues();
	}
	
	public void run(){		
		while (true) {
			
			if (!Gyro.isInitialized())
				Gyro.initialize(bus);
			if (!Gyro.isCalibrated())
				Gyro.calibrate(DELAY_TIME_MS);  // or do it on initialize
			if (!Accelerometer.isInitialized())
				Accelerometer.initialize(bus);
			
			Gyro.readRawValues(imuValues);
			Accelerometer.readRawValues(imuValues);
			
			Gyro.convertRawToDegreesPerSecond(imuValues);
			Gyro.calculateAngles(imuValues, DELAY_TIME_MS);
			
			Accelerometer.convertRawToDegrees(imuValues);
			Accelerometer.changeRotationAngle(imuValues);
			
			retrieveCombinedFilterValues(DELAY_TIME_MS / 1000);
			
			System.out.println("---------------------------------------------------------------------------------------------------------");
			System.out.println("GYRO---  rawX: " + imuValues.getGyroRawValues()[0] + " | rawY: " + imuValues.getGyroRawValues()[1] + " | rawZ: " + imuValues.getGyroRawValues()[2]);
			System.out.println("GYRO---  dpsX: " + imuValues.getGryoDegreesPerSecondX() + " | dpsY: " + imuValues.getGryoDegreesPerSecondY() + " | dpsZ: " + imuValues.getGryoDegreesPerSecondZ());
			System.out.println("GYRO---  angleX: " + imuValues.getGyroAngleX() + " | angleY: " + imuValues.getGyroAngleY() + " | angleZ: " + imuValues.getGyroAngleZ());
			System.out.println("ACC---  rawX: " + imuValues.getAccRawValues()[0] + " | rawY: " + imuValues.getAccRawValues()[1] + " | rawZ: " + imuValues.getAccRawValues()[2]);
			System.out.println("ACC---  angX: " + imuValues.getAccAngleX() + " | angY: " + imuValues.getAccAngleY() + " | angZ: " + imuValues.getAccAngleZ());
			System.out.println("FILTER---  X: " + imuValues.getFilterAngleX() + " | Y: " + imuValues.getFilterAngleY() + " | Z: " + imuValues.getFilterAngleZ());
			System.out.println("---------------------------------------------------------------------------------------------------------");
			
			delayLoop();
		}
	}
	
	private void retrieveCombinedFilterValues(double deltaTime){
		double delay = (double)DELAY_TIME_MS / 1000;
		imuValues.setFilterAngleX(FILTER_CONSTRAINT * (imuValues.getFilterAngleX() + imuValues.getGryoDegreesPerSecondX() * delay) + (1 - FILTER_CONSTRAINT) * imuValues.getAccAngleX());
		imuValues.setFilterAngleY(FILTER_CONSTRAINT * (imuValues.getFilterAngleY() + imuValues.getGryoDegreesPerSecondY() * delay) + (1 - FILTER_CONSTRAINT) * imuValues.getAccAngleY());
		imuValues.setFilterAngleZ(FILTER_CONSTRAINT * (imuValues.getGryoDegreesPerSecondZ() * deltaTime) + (1 - FILTER_CONSTRAINT) * imuValues.getAccAngleZ());
	}
	
	private void delayLoop(){
		try {
			Thread.sleep(DELAY_TIME_MS);
		} catch (InterruptedException e) {
			Logger.getLogger("IMU").log(Level.SEVERE, "Error sleeping");
			e.printStackTrace();
		}
	}
}
