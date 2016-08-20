package ress.ac.exception;

@SuppressWarnings("serial")
public class TimeOutException extends Exception {

	public TimeOutException(){
		super();
	}
	public TimeOutException(String message){
		super(message);
	}
}
