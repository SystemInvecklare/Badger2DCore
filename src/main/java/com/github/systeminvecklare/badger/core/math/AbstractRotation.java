package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;

public abstract class AbstractRotation implements IReadableRotation {

	/**
	 * Gets the shortest delta rotation to other.
	 * @param other
	 * @return
	 */
	@Override
	public DeltaRotation deltaTo(IReadableRotation other, DeltaRotation result) {
		return Rotation.deltaTo(this, other, result);
	}
	
	@Override
	public Rotation copy() {
		return new Rotation(null).setTo(this);
	}
	
	@Override
	public Rotation copy(EasyPooler ep) {
		return ep.obtain(Rotation.class).setTo(this);
	}
	
	@Override
	public Rotation copy(IPool<Rotation> pool) {
		return pool.obtain().setTo(this);
	}
	
	@Override
	public Rotation copy(IPoolManager poolManager) {
		return copy(poolManager.getPool(Rotation.class));
	}
	
	@Override
	public Vector toUnitVector() {
		return new Vector(null).setToUnitVector(this);
	}
	
	@Override
	public Vector toUnitVector(EasyPooler ep) {
		return ep.obtain(Vector.class).setToUnitVector(this);
	}
	
	@Override
	public Vector toUnitVector(IPool<Vector> pool) {
		return pool.obtain().setToUnitVector(this);
	}
}
