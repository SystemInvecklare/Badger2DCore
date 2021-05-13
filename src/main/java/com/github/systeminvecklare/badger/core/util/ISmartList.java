package com.github.systeminvecklare.badger.core.util;


public interface ISmartList<T> {
	void addToBirthList(T object);
	void addToDeathList(T object);
	QuickArray<T> getUpdatedArray();
	void clear();
	boolean isEmpty();
}
