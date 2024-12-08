package com.github.systeminvecklare.badger.core.graphics.components.transform;

import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;

public abstract class AbstractTransform implements ITransform {
	@Override
	public ITransform copy(IPool<ITransform> pool) {
		return pool.obtain().setTo(this);
	}

	@Override
	public ITransform copy(EasyPooler ep) {
		return ep.obtain(ITransform.class).setTo(this);
	}
	
	@Override
	public ITransform copy(IPoolManager poolManager) {
		return copy(poolManager.getPool(ITransform.class));
	}
	
	public ITransform addRadialToPosition(float theta, float scale) {
		return addToPosition(Mathf.cos(theta)*scale, Mathf.sin(theta)*scale);
	}
}
