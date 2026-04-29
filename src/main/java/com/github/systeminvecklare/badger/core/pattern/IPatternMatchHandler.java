package com.github.systeminvecklare.badger.core.pattern;

public interface IPatternMatchHandler {
	void onText(String text);
	
	/**
	 * The patternMatch object is only valid in the frame (of the onPattern method).
	 * @param patternMatch - only valid in the frame
	 */
	void onPattern(IPatternMatch patternMatch);
}
