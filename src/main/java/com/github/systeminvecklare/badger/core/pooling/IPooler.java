package com.github.systeminvecklare.badger.core.pooling;

public interface IPooler {
	<T extends IPoolable> T obtain(Class<T> type);
}
