package com.github.systeminvecklare.badger.core.graphics.components.transform;

public class NonInvertibleMatrixException extends Exception {
	private static final long serialVersionUID = -5597697271325423304L;

	public NonInvertibleMatrixException() {
		super();
	}

	public NonInvertibleMatrixException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonInvertibleMatrixException(String message) {
		super(message);
	}

	public NonInvertibleMatrixException(Throwable cause) {
		super(cause);
	}
}
