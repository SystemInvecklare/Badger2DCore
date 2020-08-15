package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class PoolableArrayOf16Floats implements IPoolable {
	private IPool<PoolableArrayOf16Floats> pool;
	private float[] array;
	
	public PoolableArrayOf16Floats(IPool<PoolableArrayOf16Floats> pool) {
		this.pool = pool;
		this.array = new float[16];
	}

	@Override
	public void free() {
		pool.free(this);
	}

	@Override
	public IPool<PoolableArrayOf16Floats> getPool() {
		return pool;
	}
	
	public PoolableArrayOf16Floats clear()
	{
		for(int i = 0; i < 16; ++i)
		{
			array[i] = 0f;
		}
		return this;
	}
	
	public PoolableArrayOf16Floats setToIdentity()
	{
		for(int i = 0; i < 16; ++i)
		{
			array[i] = (i%5 == 0) ? 1f : 0f;
		}
		return this;
	}
	
	public float[] getArray() {
		return array;
	}

}
