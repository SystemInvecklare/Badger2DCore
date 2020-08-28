package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public abstract class AbstractMatrix2x2 implements IReadableMatrix2x2 {
	@Override
	public Matrix2x2 copy() {
		return new Matrix2x2(null).setTo(this);
	}

	@Override
	public Matrix2x2 copy(IPool<Matrix2x2> pool) {
		return pool.obtain().setTo(this);
	}

	@Override
	public Matrix2x2 copy(EasyPooler ep) {
		return ep.obtain(Matrix2x2.class).setTo(this);
	}

	@Override
	public void getData(float[] result) {
		getData(result, 0);
	}
}
