package ress.ac.gpio;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;

public class GpioHolder {
	GpioPinPwmOutput pin00;
	GpioPinPwmOutput pin02;
	GpioPinPwmOutput pin03;
	GpioPinPwmOutput pin04;
	GpioPinPwmOutput pin27;
	GpioPinPwmOutput pin25;
	GpioPinPwmOutput pin28;
	GpioPinPwmOutput pin29;
	
	GpioPinDigitalOutput trigger;
	GpioPinDigitalInput echo;
	
	public GpioPinPwmOutput getPin00() {
		return pin00;
	}
	public void setPin00(GpioPinPwmOutput pin00) {
		this.pin00 = pin00;
	}
	public GpioPinPwmOutput getPin02() {
		return pin02;
	}
	public void setPin02(GpioPinPwmOutput pin02) {
		this.pin02 = pin02;
	}
	public GpioPinPwmOutput getPin03() {
		return pin03;
	}
	public void setPin03(GpioPinPwmOutput pin03) {
		this.pin03 = pin03;
	}
	public GpioPinPwmOutput getPin04() {
		return pin04;
	}
	public void setPin04(GpioPinPwmOutput pin04) {
		this.pin04 = pin04;
	}
	public GpioPinPwmOutput getPin27() {
		return pin27;
	}
	public void setPin27(GpioPinPwmOutput pin27) {
		this.pin27 = pin27;
	}
	public GpioPinPwmOutput getPin25() {
		return pin25;
	}
	public void setPin25(GpioPinPwmOutput pin25) {
		this.pin25 = pin25;
	}
	public GpioPinPwmOutput getPin28() {
		return pin28;
	}
	public void setPin28(GpioPinPwmOutput pin28) {
		this.pin28 = pin28;
	}
	public GpioPinPwmOutput getPin29() {
		return pin29;
	}
	public void setPin29(GpioPinPwmOutput pin29) {
		this.pin29 = pin29;
	}
	public GpioPinDigitalOutput getTrigger() {
		return trigger;
	}
	public void setTrigger(GpioPinDigitalOutput trigger) {
		this.trigger = trigger;
	}
	public GpioPinDigitalInput getEcho() {
		return echo;
	}
	public void setEcho(GpioPinDigitalInput echo) {
		this.echo = echo;
	}
}
