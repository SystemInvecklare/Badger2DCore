package com.github.systeminvecklare.badger.core.graphics.components.util;

import java.util.Iterator;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;


public class PoolableIterable<T> implements Iterable<T>, IPoolable {
	@SuppressWarnings("rawtypes")
	private IPool<PoolableIterable> pool;
	private int arrayLength = 0;
	private Object[] array = new Object[0];
	private final MyIterator iterator = new MyIterator();
	
	@SuppressWarnings("rawtypes")
	public PoolableIterable(IPool<PoolableIterable> pool) {
		this.pool = pool;
	}

	@Override
	public Iterator<T> iterator() {
		return iterator.reset();
	}
	
	public PoolableIterable<T> setToCopy(Iterable<? extends T> copyFrom)
	{
		clearArray();
		for(T obj : copyFrom) {
			add(obj);
		}
		return this;
	}

	@Override
	public void free() {
		clearArray();
		pool.free(this);
	}
	
	private void ensureCapacity(int capacity) {
		if(array.length < capacity) {
			Object[] newArray = new Object[capacity];
			if(arrayLength > 0) {
				System.arraycopy(array, 0, newArray, 0, arrayLength);
			}
			array = newArray;
		}
	}
	
	private void add(T object) {
		ensureCapacity(arrayLength + 1);
		array[arrayLength++] = object;
	}
	
	private void clearArray() {
		arrayLength = 0;
	}
	
	@SuppressWarnings("unchecked")
	private T get(int index) {
		if(index < 0 || index >= arrayLength) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return (T) array[index];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> PoolableIterable<T> obtain(Class<T> iteratedType)
	{
		IPool<PoolableIterable> pool = FlashyEngine.get().getPoolManager().getPool(PoolableIterable.class);
		return pool.obtain();
	}
	
	private class MyIterator implements Iterator<T> {
		private int nextIndex = 0;

		@Override
		public boolean hasNext() {
			return arrayLength > nextIndex;
		}

		@Override
		public T next() {
			return get(nextIndex++);
		}
		
		public MyIterator reset() {
			nextIndex = 0;
			return this;
		}
	}
}
