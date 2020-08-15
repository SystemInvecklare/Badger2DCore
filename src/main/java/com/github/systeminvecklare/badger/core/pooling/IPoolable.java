package com.github.systeminvecklare.badger.core.pooling;


public interface IPoolable {
	public void free();
	public IPool<? extends IPoolable> getPool(); //TODO remove. This is never used!
}
