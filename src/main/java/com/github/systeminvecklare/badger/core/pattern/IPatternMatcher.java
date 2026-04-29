package com.github.systeminvecklare.badger.core.pattern;

public interface IPatternMatcher {
	boolean match(String string, IPatternMatchHandler handler) throws PatternMatchingException;
}
