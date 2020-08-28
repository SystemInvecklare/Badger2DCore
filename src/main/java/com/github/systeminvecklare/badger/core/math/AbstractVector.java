package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public abstract class AbstractVector implements IReadableVector {

	@Override
	public Vector copy() {
		return new Vector(null).setTo(this);
	}

	@Override
	public Vector copy(IPool<Vector> pool) {
		return pool.obtain().setTo(this);
	}

	@Override
	public Vector copy(EasyPooler ep) {
		return ep.obtain(Vector.class).setTo(this);
	}

	@Override
	public float length() {
		return Mathf.sqrt(length2());
	}

	@Override
	public float length2() {
		return dot(this);
	}

	@Override
	public float dot(IReadableVector other) {
		return Vector.dot(this, other);
	}

	@Override
	public float cross(IReadableVector other) {
		return Vector.cross(this, other);
	}
	
	@Override
	public float getRotationTheta() {
		return Vector.getRotationTheta(this);
	}
}
