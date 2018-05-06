package ress.ac.imu;

public class IMUValues {

	public double[] gyroRawValues = new double[3];
	public double[] accRawValues = new double[3];
	
	public double gryoDegreesPerSecondX;
	public double gryoDegreesPerSecondY;
	public double gryoDegreesPerSecondZ;
	
	public double gyroAngleX;
	public double gyroAngleY;
	public double gyroAngleZ;
	
	public double accAngleX;
	public double accAngleY;
	public double accAngleZ;
	
	public double filterAngleX;
	public double filterAngleY;
	public double filterAngleZ;
	
	
	public double[] getGyroRawValues() {
		return gyroRawValues;
	}
	public double[] getAccRawValues() {
		return accRawValues;
	}
	public double getGryoDegreesPerSecondX() {
		return gryoDegreesPerSecondX;
	}
	public void setGryoDegreesPerSecondX(double gryoDegreesPerSecondX) {
		this.gryoDegreesPerSecondX = gryoDegreesPerSecondX;
	}
	public double getGryoDegreesPerSecondY() {
		return gryoDegreesPerSecondY;
	}
	public void setGryoDegreesPerSecondY(double gryoDegreesPerSecondY) {
		this.gryoDegreesPerSecondY = gryoDegreesPerSecondY;
	}
	public double getGryoDegreesPerSecondZ() {
		return gryoDegreesPerSecondZ;
	}
	public void setGryoDegreesPerSecondZ(double gryoDegreesPerSecondZ) {
		this.gryoDegreesPerSecondZ = gryoDegreesPerSecondZ;
	}
	public double getGyroAngleX() {
		return gyroAngleX;
	}
	public void setGyroAngleX(double gyroAngleX) {
		this.gyroAngleX = gyroAngleX;
	}
	public double getGyroAngleY() {
		return gyroAngleY;
	}
	public void setGyroAngleY(double gyroAngleY) {
		this.gyroAngleY = gyroAngleY;
	}
	public double getAccAngleX() {
		return accAngleX;
	}
	public void setAccAngleX(double accAngleX) {
		this.accAngleX = accAngleX;
	}
	public double getAccAngleY() {
		return accAngleY;
	}
	public void setAccAngleY(double accAngleY) {
		this.accAngleY = accAngleY;
	}
	public double getAccAngleZ() {
		return accAngleZ;
	}
	public void setAccAngleZ(double accAngleZ) {
		this.accAngleZ = accAngleZ;
	}
	public double getGyroAngleZ() {
		return gyroAngleZ;
	}
	public void setGyroAngleZ(double gyroAngleZ) {
		this.gyroAngleZ = gyroAngleZ;
	}
	public double getFilterAngleX() {
		return filterAngleX;
	}
	public void setFilterAngleX(double filterAngleX) {
		this.filterAngleX = filterAngleX;
	}
	public double getFilterAngleY() {
		return filterAngleY;
	}
	public void setFilterAngleY(double filterAngleY) {
		this.filterAngleY = filterAngleY;
	}
	public double getFilterAngleZ() {
		return filterAngleZ;
	}
	public void setFilterAngleZ(double filterAngleZ) {
		this.filterAngleZ = filterAngleZ;
	}
	
}
