package com.github.systeminvecklare.badger.core.pooling;

import java.util.ArrayList;
import java.util.Collection;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;


public class EasyPooler {
	private IPool<EasyPooler> homePool;
	private Collection<IPoolable> poolables = new ArrayList<IPoolable>();
	private IPoolManager poolmanager;
	
	private EasyPooler() {}
	

	public void freeAllAndSelf()
	{
		for(IPoolable poolable : poolables)
		{
			poolable.free();
		}
		poolables.clear();
		IPool<EasyPooler> myHomePool = homePool;
		homePool = null;
		poolmanager = null;
		myHomePool.free(this);
	}
	
	public <T extends IPoolable> T obtain(Class<T> type)
	{
		IPool<T> pool = poolmanager.getPool(type);
		T poolable = pool.obtain();
		try
		{
			poolables.add(poolable);
			return poolable;
		}
		catch (RuntimeException e) {
			if(poolable != null) {poolable.free();}
			throw e;
		}
	}
	
	
	public static EasyPooler obtainFresh()
	{
		IPoolManager poolManager = FlashyEngine.get().getPoolManager();
		IPool<EasyPooler> easyPoolerPool = poolManager.getPool(EasyPooler.class);
		EasyPooler easyPooler = easyPoolerPool.obtain();
		easyPooler.homePool = easyPoolerPool;
		easyPooler.poolmanager = poolManager;
		easyPooler.poolables.clear();
		return easyPooler;
	}
	
	
	public static IPool<EasyPooler> newEasyPoolerPool(int initialCapacity, int maxSize)
	{
		return new SimplePool<EasyPooler>(initialCapacity, maxSize) {
			@Override
			public EasyPooler newObject() {
				return new EasyPooler();
			}
		};
	}
}
