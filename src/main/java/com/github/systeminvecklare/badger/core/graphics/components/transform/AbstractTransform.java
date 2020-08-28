package com.github.systeminvecklare.badger.core.graphics.components.transform;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public abstract class AbstractTransform implements ITransform {
	@Override
	public ITransform copy(IPool<ITransform> pool) {
		return pool.obtain().setTo(this);
	}

	@Override
	public ITransform copy(EasyPooler ep) {
		return ep.obtain(ITransform.class).setTo(this);
	}
}
