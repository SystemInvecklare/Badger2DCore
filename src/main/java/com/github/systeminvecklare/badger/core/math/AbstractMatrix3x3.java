package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;
import com.github.systeminvecklare.badger.core.util.PoolableArrayOf16Floats;

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
	public Matrix3x3 copy(IPoolManager poolManager) {
		return copy(poolManager.getPool(Matrix3x3.class));
	}


	@Override
	public void getData(float[] result) {
		getData(result, 0);
	}

	@Override
	public void transformAffinely(Position argumentAndResult) {
		Matrix3x3.transformAffinely(this, argumentAndResult);
	}
	
	@Override
	public float getDeterminant() {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			float[] data = ep.obtain(PoolableArrayOf16Floats.class).getArray();
			getData(data);
			return data[M11]*data[M22]*data[M33] + data[M12]*data[M23]*data[M31] + data[M13]*data[M21]*data[M32] - data[M11]*data[M23]*data[M32] - data[M12]*data[M21]*data[M33] - data[M13]*data[M22]*data[M31];
		} finally {
			ep.freeAllAndSelf();
		}
	}
}
