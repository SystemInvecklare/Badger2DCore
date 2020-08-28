package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public abstract class AbstractPosition implements IReadablePosition {

	@Override
	public Vector vectorTo(IReadablePosition other, Vector result) {
		return Position.vectorTo(this, other, result);
	}
	
	@Override
	public float length() {
		return Mathf.sqrt(length2());
	}
	
	@Override
	public float length2() {
		float x = getX();
		float y = getY();
		return x*x+y*y;
	}
	
	@Override
	public Position copy() {
		return new Position(null).setTo(this);
	}
	
	@Override
	public Position copy(EasyPooler ep) {
		return ep.obtain(Position.class).setTo(this);
	}
	
	@Override
	public Position copy(IPool<Position> pool) {
		return pool.obtain().setTo(this);
	}
}
