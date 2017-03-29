package ress.ac.movement;

import ress.ac.gpio.GpioHolder;

public class Move {

	public static void goForward(GpioHolder gpio){
		gpio.getPin00().setPwm(100);
		gpio.getPin27().setPwm(100);
		gpio.getPin03().setPwm(100);
		gpio.getPin29().setPwm(100);
	}
	
	public static void reverse(GpioHolder gpio){
		gpio.getPin02().setPwm(100);
		gpio.getPin25().setPwm(100);
		gpio.getPin04().setPwm(100);
		gpio.getPin28().setPwm(100);
	}
	
	public static void stop(GpioHolder gpio){
		gpio.getPin00().setPwm(0);
		gpio.getPin02().setPwm(0);
		gpio.getPin25().setPwm(0);
		gpio.getPin27().setPwm(0);
		gpio.getPin03().setPwm(0);
		gpio.getPin04().setPwm(0);
		gpio.getPin28().setPwm(0);
		gpio.getPin29().setPwm(0);
	}
	
	
}
