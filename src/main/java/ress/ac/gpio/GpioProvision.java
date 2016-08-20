package ress.ac.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GpioProvision {
	private static GpioController gpio = GpioFactory.getInstance();

	public static GpioHolder provisionGpioPins(){
		GpioHolder gpioHolder = new GpioHolder();
		
    	provisionWheelMotors(gpioHolder);
    	provisionUltraSonicSensor(gpioHolder);
    	
    	return gpioHolder;
	}
	
	private static void provisionWheelMotors(GpioHolder gpioHolder){
		//Driver Rear
		gpioHolder.setPin00(gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_00, "00", 0));
		gpioHolder.setPin02(gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_02, "02", 0));
    	//Driver Front
		gpioHolder.setPin03(gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_03, "03", 0));
		gpioHolder.setPin04(gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_04, "04", 0));
    	//Passenger Rear
		gpioHolder.setPin25(gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_25, "25", 0));
		gpioHolder.setPin27(gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_27, "27", 0));
    	//Passenger Front
		gpioHolder.setPin28(gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_28, "28", 0));
		gpioHolder.setPin29(gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_29, "29", 0));
	}
	
	private static void provisionUltraSonicSensor(GpioHolder gpioHolder){
		gpioHolder.setTrigger(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "21", PinState.LOW));
		gpioHolder.setEcho(gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, "22", PinPullResistance.PULL_DOWN));
	}
}
