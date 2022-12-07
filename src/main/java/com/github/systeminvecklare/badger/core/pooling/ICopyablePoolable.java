package com.github.systeminvecklare.badger.core.pooling;

public interface ICopyablePoolable<T> {
	public T copy();
	public T copy(IPool<T> pool);
	public T copy(EasyPooler ep);
	public T copy(IPoolManager poolManager);
}
