package com.github.systeminvecklare.badger.core.util;


public interface ISmartList<T> {
	void addToBirthList(T object);
	void addToDeathList(T object);
	IQuickArray<T> getUpdatedArray();
	void clear();
	boolean isEmpty();
}
