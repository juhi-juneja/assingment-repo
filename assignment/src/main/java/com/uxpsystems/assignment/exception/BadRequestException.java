package com.uxpsystems.assignment.exception;

public class BadRequestException extends CustomException{

	private static final long serialVersionUID = 1L;


	public BadRequestException(String message) {
		super(message);
	}
}
