package com.github.systeminvecklare.badger.core.pooling;

public interface IPoolManager {
	public <T> IPool<T> getPool(Class<T> poolableType);
}
