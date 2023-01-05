//package com.github.systeminvecklare.badger.core.graphics.components.util;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//
//import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
//import com.github.systeminvecklare.badger.core.pooling.IPool;
//import com.github.systeminvecklare.badger.core.pooling.IPoolable;
//
//
//public class OLD_PoolableIterable<T> implements Iterable<T>, IPoolable {
//	@SuppressWarnings("rawtypes")
//	private IPool<OLD_PoolableIterable> pool;
//	private Collection<T> collection = new ArrayList<T>();
//	
//	@SuppressWarnings("rawtypes")
//	public OLD_PoolableIterable(IPool<OLD_PoolableIterable> pool) {
//		this.pool = pool;
//	}
//
//	@Override
//	public Iterator<T> iterator() {
//		return collection.iterator();
//	}
//	
//	public OLD_PoolableIterable<T> setToCopy(Iterable<? extends T> copyFrom)
//	{
//		collection.clear();
//		for(T obj : copyFrom)
//		{
//			collection.add(obj);
//		}
//		return this;
//	}
//
//	@Override
//	public void free() {
//		collection.clear();
//		pool.free(this);
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static <T> OLD_PoolableIterable<T> obtain(Class<T> iteratedType)
//	{
//		IPool<OLD_PoolableIterable> pool = FlashyEngine.get().getPoolManager().getPool(OLD_PoolableIterable.class);
//		return pool.obtain();
//	}
//}
