package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public abstract class AbstractMatrix3x3 implements IReadableMatrix3x3 {

	@Override
	public Matrix3x3 copy() {
		return new Matrix3x3(null).setTo(this);
	}

	@Override
	public Matrix3x3 copy(IPool<Matrix3x3> pool) {
		return pool.obtain().setTo(this);
	}

	@Override
	public Matrix3x3 copy(EasyPooler ep) {
		return ep.obtain(Matrix3x3.class).setTo(this);
	}


	@Override
	public void getData(float[] result) {
		getData(result, 0);
	}

	@Override
	public void transformAffinely(Position argumentAndResult) {
		Matrix3x3.transformAffinely(this, argumentAndResult);
	}
}
