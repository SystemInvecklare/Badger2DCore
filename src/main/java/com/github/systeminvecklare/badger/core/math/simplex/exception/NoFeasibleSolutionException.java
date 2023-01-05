package com.github.systeminvecklare.badger.core.math.simplex.exception;

public class NoFeasibleSolutionException extends SimplexException {
	private static final long serialVersionUID = -2371993048596455738L;

	public NoFeasibleSolutionException() {
		super();
	}

	public NoFeasibleSolutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoFeasibleSolutionException(String message) {
		super(message);
	}

	public NoFeasibleSolutionException(Throwable cause) {
		super(cause);
	}
}
