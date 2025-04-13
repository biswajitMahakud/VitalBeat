package com.hospital.ExceptionHandler;

public class ResourceExistException extends RuntimeException{
	public ResourceExistException(String msg) {
		super(msg);
	}
}
