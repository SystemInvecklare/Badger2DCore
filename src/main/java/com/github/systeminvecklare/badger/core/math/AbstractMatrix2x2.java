package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;
import com.github.systeminvecklare.badger.core.util.PoolableArrayOf16Floats;

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
	public Matrix2x2 copy(IPoolManager poolManager) {
		return copy(poolManager.getPool(Matrix2x2.class));
	}

	@Override
	public void getData(float[] result) {
		getData(result, 0);
	}
	
	@Override
	public float getDeterminant() {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			float[] data = ep.obtain(PoolableArrayOf16Floats.class).getArray();
			getData(data);
			return data[M11]*data[M22] - data[M12]*data[M21];
		} finally {
			ep.freeAllAndSelf();
		}
	}
	
	@Override
	public Position transform(Position argumentAndResult) {
		float x = argumentAndResult.getX();
		float y = argumentAndResult.getY();
		return argumentAndResult.setTo(getData(M11)*x+getData(M12)*y, getData(M21)*x+getData(M22)*y);
	}
	
	@Override
	public Vector transform(Vector argumentAndResult) {
		float x = argumentAndResult.getX();
		float y = argumentAndResult.getY();
		return argumentAndResult.setTo(getData(M11)*x+getData(M12)*y, getData(M21)*x+getData(M22)*y);
	}
}
