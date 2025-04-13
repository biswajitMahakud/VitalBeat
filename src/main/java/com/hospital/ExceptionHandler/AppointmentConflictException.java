package com.hospital.ExceptionHandler;

public class AppointmentConflictException extends RuntimeException{
	public AppointmentConflictException(String msg) {
		super(msg);
	}
}
