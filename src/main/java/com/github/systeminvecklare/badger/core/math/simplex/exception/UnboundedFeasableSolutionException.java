package com.github.systeminvecklare.badger.core.math.simplex.exception;

public class UnboundedFeasableSolutionException extends SimplexException {
	private static final long serialVersionUID = -2464814335724746368L;

	public UnboundedFeasableSolutionException() {
		super();
	}

	public UnboundedFeasableSolutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnboundedFeasableSolutionException(String message) {
		super(message);
	}

	public UnboundedFeasableSolutionException(Throwable cause) {
		super(cause);
	}
}
