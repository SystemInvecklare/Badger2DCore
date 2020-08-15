package com.github.systeminvecklare.badger.core.pooling;

import java.util.ArrayList;
import java.util.List;

public abstract class SimplePool<T> implements IPool<T> {
	private List<T> pool;
	private int maxSize;
	
	public SimplePool(int initCapacity,int maxSize) {
		this.pool = new ArrayList<T>(initCapacity);
		this.maxSize = maxSize;
	}

	@Override
	public T obtain() {
		if(pool.size() > 0)
		{
			try
			{
				T obj = pool.remove(pool.size()-1);
				return obj;
			}
			catch(IndexOutOfBoundsException e)
			{
				return newObject();
			}
		}
		else
		{
			return newObject();
		}
	}

	@Override
	public void free(T poolable) {
		if(pool.size() < maxSize)
		{
			pool.add(poolable);
		}
	}

	public abstract T newObject();
}
