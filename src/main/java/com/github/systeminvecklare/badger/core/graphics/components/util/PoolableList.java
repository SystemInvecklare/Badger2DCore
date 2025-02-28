package com.github.systeminvecklare.badger.core.graphics.components.util;

import java.util.ArrayList;
import java.util.List;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolRegistry;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;
import com.github.systeminvecklare.badger.core.pooling.SimplePool;

public class PoolableList implements IPoolable {
	private final IPool<PoolableList> pool;
	@SuppressWarnings("rawtypes")
	private final List value;
	
	public PoolableList(IPool<PoolableList> pool, @SuppressWarnings("rawtypes") List value) {
		this.pool = pool;
		this.value = value;
	}

	@Override
	public void free() {
		pool.free(this);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list() {
		return value;
	}
	
	public <T> List<T> list(Class<T> elementType) {
		return list();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> copy(List<T> copied) {
		value.clear();
		value.addAll(copied);
		return list();
	}
	
	public <T> List<T> harvest(List<T> copied) {
		List<T> result = copy(copied);
		copied.clear();
		return result;
	}

	public static void registerPool(IPoolRegistry poolRegistry) {
		poolRegistry.registerPool(PoolableList.class, new SimplePool<PoolableList>(1, 10) {
			@Override
			public PoolableList obtain() {
				PoolableList object = super.obtain();
				object.value.clear();
				return object;
			}
			
			@Override
			public PoolableList newObject() {
				return new PoolableList(this, new ArrayList<Object>(1));
			}
			
			@Override
			public void free(PoolableList poolable) {
				poolable.value.clear();
				super.free(poolable);
			}
		});
	}
}
