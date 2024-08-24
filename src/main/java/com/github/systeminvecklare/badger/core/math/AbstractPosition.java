package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;

public abstract class AbstractPosition implements IReadablePosition {

	@Override
	public Vector vectorTo(IReadablePosition other, Vector result) {
		return Position.vectorTo(this, other, result);
	}
	
	@Override
	public Position interpolate(float t, IReadablePosition other, Position result) {
		return result.setTo(Mathf.lerp(t, this.getX(), other.getX()), Mathf.lerp(t, this.getY(), other.getY()));
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
	
	@Override
	public Position copy(IPoolManager poolManager) {
		return copy(poolManager.getPool(Position.class));
	}
	
	@Override
	public float distance2(IReadablePosition other) {
		return distance2(other.getX(), other.getY());
	}
	
	@Override
	public float distance(IReadablePosition other) {
		return distance(other.getX(), other.getY());
	}
	
	@Override
	public float distance2(float x, float y) {
		float dx = this.getX()-x;
		float dy = this.getY()-y;
		return dx*dx+dy*dy;
	}
	
	@Override
	public float distance(float x, float y) {
		return Mathf.sqrt(distance2(x, y));
	}
}
