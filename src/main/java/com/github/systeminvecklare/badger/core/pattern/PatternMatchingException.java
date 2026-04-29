package com.github.systeminvecklare.badger.core.pattern;

public class PatternMatchingException extends RuntimeException {
	private static final long serialVersionUID = -6433470931518416718L;

	public PatternMatchingException() {
		super();
	}

	public PatternMatchingException(String message, Throwable cause) {
		super(message, cause);
	}

	public PatternMatchingException(String message) {
		super(message);
	}

	public PatternMatchingException(Throwable cause) {
		super(cause);
	}
}
