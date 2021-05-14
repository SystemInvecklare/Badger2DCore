package com.github.systeminvecklare.badger.core.util;

public interface IQuickArray<T> {
	void add(T object);
	void addAllFrom(IQuickArray<T> other);
	int getSize();
	T get(int index);
	void copyToArray(T[] target, int offset, int length);
	void removeAllIn(IQuickArray<T> other);
	void clear();
}
