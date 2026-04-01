package com.github.systeminvecklare.badger.core.pooling;

import java.util.ArrayList;
import java.util.Collection;

public class SingleTypePooler<PT> implements IPooler {
	private final Class<PT> poolableType;
	private final IPool<PT> pool;
	private final Collection<PT> poolables = new ArrayList<PT>();
	
	public SingleTypePooler(Class<PT> poolableType, IPool<PT> pool) {
		this.poolableType = poolableType;
		this.pool = pool;
	}

	public void freeAll() {
		if(IPoolable.class.isAssignableFrom(poolableType)) {
			for(PT poolable : poolables) {
				((IPoolable) poolable).free();
			}
		} else {
			for(PT poolable : poolables) {
				pool.free(poolable);
			}
		}
		poolables.clear();
	}
	
	public PT obtain() {
		PT poolable = pool.obtain();
		if(poolable == null) {
			throw new NullPointerException("Pool "+pool+" returned null from IPool::obtain(). This is not allowed.");
		}
		try {
			poolables.add(poolable);
			return poolable;
		} catch (RuntimeException e) { // Note: Might be impossible atm. But keep it in case we update logic inside try-block
			if(poolable != null) {
				if(IPoolable.class.isAssignableFrom(poolableType)) {
					((IPoolable) poolable).free();
				} else {
					pool.free(poolable);
				}
			}
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IPoolable> T obtain(Class<T> type) {
		if(!type.isAssignableFrom(poolableType)) {
			throw new IllegalArgumentException("Tried to obtain "+type.getName()+" from "+getClass().getName()+" of "+poolableType.getName());
		}
		return (T) obtain();
	}
}
