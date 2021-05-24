package com.github.systeminvecklare.badger.core.graphics.framework.smartlist;


public interface ISmartList<T> {
	void addToBirthList(T object);
	void addToDeathList(T object);
	void clear();
	boolean isEmpty();
	void forEach(ILoopAction<? super T> loopAction);
}
