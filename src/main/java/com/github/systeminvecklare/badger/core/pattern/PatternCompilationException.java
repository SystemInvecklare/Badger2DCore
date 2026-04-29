package com.github.systeminvecklare.badger.core.pattern;

public class PatternCompilationException extends RuntimeException {
	private static final long serialVersionUID = -4307747346173044596L;

	public PatternCompilationException() {
		super();
	}

	public PatternCompilationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PatternCompilationException(String message) {
		super(message);
	}

	public PatternCompilationException(Throwable cause) {
		super(cause);
	}
}
