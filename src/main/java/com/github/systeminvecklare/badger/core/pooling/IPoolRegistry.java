package com.github.systeminvecklare.badger.core.pooling;

public interface IPoolRegistry {
	<T> void registerPool(Class<T> type, IPool<T> pool);
}
