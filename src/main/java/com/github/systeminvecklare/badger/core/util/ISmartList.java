package com.github.systeminvecklare.badger.core.util;


public interface ISmartList<T> {
	public void addToBirthList(T object);
	public void addToDeathList(T object);
	public QuickArray<T> getUpdatedArray();
	public void clear();
}
