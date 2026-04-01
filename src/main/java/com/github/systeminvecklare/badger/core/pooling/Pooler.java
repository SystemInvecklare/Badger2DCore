package com.github.systeminvecklare.badger.core.pooling;

import java.util.ArrayList;
import java.util.Collection;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;

public class Pooler implements IPooler {
	private IPoolManager poolManager;
	private final Collection<IPoolable> poolables = new ArrayList<IPoolable>();
	
	public Pooler() {
		this(null);
	}
	
	public Pooler(IPoolManager poolManager) {
		this.poolManager = poolManager;
	}

	public void freeAll() {
		for(IPoolable poolable : poolables) {
			poolable.free();
		}
		poolables.clear();
	}
	
	@Override
	public <T extends IPoolable> T obtain(Class<T> type) {
		IPool<T> pool = getPoolManager().getPool(type);
		T poolable = pool.obtain();
		if(poolable == null) {
			throw new NullPointerException("Pool "+pool+" returned null from IPool::obtain(). This is not allowed.");
		}
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
	
	private IPoolManager getPoolManager() {
		if(poolManager == null) {
			poolManager = FlashyEngine.get().getPoolManager();
		}
		return poolManager;
	}
}
