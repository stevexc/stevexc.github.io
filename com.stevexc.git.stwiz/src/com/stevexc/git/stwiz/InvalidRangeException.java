package com.stevexc.git.stwiz;

public class InvalidRangeException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRangeException(String errorMessage) {
        super(errorMessage);
    }
	public InvalidRangeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}