package com.github.systeminvecklare.badger.core.pooling;

public interface IPool<T> {
	public T obtain();
	public void free(T poolable);	
}
