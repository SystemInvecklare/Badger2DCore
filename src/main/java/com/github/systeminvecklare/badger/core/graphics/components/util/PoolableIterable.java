package com.github.systeminvecklare.badger.core.graphics.components.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;


public class PoolableIterable<T> implements Iterable<T>, IPoolable {
	@SuppressWarnings("rawtypes")
	private IPool<PoolableIterable> pool;
	private Collection<T> collection = new ArrayList<T>();
	
	@SuppressWarnings("rawtypes")
	public PoolableIterable(IPool<PoolableIterable> pool) {
		this.pool = pool;
	}

	@Override
	public Iterator<T> iterator() {
		return collection.iterator();
	}
	
	public PoolableIterable<T> setToCopy(Iterable<? extends T> copyFrom)
	{
		collection.clear();
		for(T obj : copyFrom)
		{
			collection.add(obj);
		}
		return this;
	}

	@Override
	public void free() {
		collection.clear();
		pool.free(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IPool<PoolableIterable> getPool() {
		return pool;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> PoolableIterable<T> obtain(Class<T> iteratedType)
	{
		IPool<PoolableIterable> pool = FlashyEngine.get().getPoolManager().getPool(PoolableIterable.class);
		return pool.obtain();
	}
}
