package org.apache.mahout.lcs.util;

public class SimLibraryError extends RuntimeException {
	public SimLibraryError() {
		super();
	}

	

	/**
	 * Constructor of an own error-class, used to distinguish from common errors. 
	 * @param message The error message.
	 */
	public SimLibraryError(String message) {
		super(message);
	}
}