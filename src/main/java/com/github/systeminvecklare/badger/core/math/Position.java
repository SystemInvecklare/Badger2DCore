package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class Position extends AbstractPosition implements IPoolable, IReadablePosition {
	private float x;
	private float y;
	private IPool<Position> pool;
	
	public Position(IPool<Position> pool) {
		this.pool = pool;
	}
	
	@Override
	public float getX()
	{
		return x;
	}
	
	@Override
	public float getY()
	{
		return y;
	}
	
	public Position setTo(float x, float y)
	{
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Position setToOrigin()
	{
		return this.setTo(0,0);
	}
	
	public Position setTo(IReadablePosition other)
	{
		this.x = other.getX();
		this.y = other.getY();
		return this;
	}
	
	public Position setTo(IReadableVector v)
	{
		this.x = v.getX();
		this.y = v.getY();
		return this;
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	public Position add(IReadableVector vector)
	{
		this.x += vector.getX();
		this.y += vector.getY();
		return this;
	}
	
	public Position add(float x,float y)
	{
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Position addScaled(IReadableVector other, float scale) {
		return add(other.getX()*scale, other.getY()*scale); 
	}
	
	public Position addRadialVector(float theta, float scale) { 
		return add(Mathf.cos(theta)*scale, Mathf.sin(theta)*scale);
	}
	
	public Position interpolateTo(float t, IReadablePosition other) {
		return interpolate(t, other, this);
	}
	
	public static Vector vectorTo(IReadablePosition from, IReadablePosition to, Vector result)
	{
		return result.setTo(to.getX()-from.getX(), to.getY()-from.getY());
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof IReadablePosition) && equalsOwn((IReadablePosition) obj);
	}

	private boolean equalsOwn(IReadablePosition obj) {
		return this.x == obj.getX() && this.y == obj.getY();
	}
	
	@Override
	public int hashCode() {
		return Float.valueOf(x).hashCode() ^ Float.valueOf(y).hashCode(); 
	}
}
