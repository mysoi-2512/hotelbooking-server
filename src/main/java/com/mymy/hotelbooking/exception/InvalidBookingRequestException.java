package com.mymy.hotelbooking.exception;

public class InvalidBookingRequestException extends RuntimeException {
	
	public InvalidBookingRequestException (String message) {
		super(message);
	}

}
