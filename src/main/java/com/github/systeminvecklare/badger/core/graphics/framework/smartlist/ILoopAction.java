package com.github.systeminvecklare.badger.core.graphics.framework.smartlist;

public interface ILoopAction<T> {
	/**
	 * @param value
	 * @return true if the loop should continue, false if it should break.
	 */
	boolean onIteration(T value);
}