package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;

public interface IReadablePosition extends ICopyablePoolable<Position> {
	public float getX();
	public float getY();
	public Vector vectorTo(IReadablePosition other, Vector result);
	public Position interpolate(float t, IReadablePosition other, Position result);
	public float length2();
	public float length();
	public float distance2(IReadablePosition other);
	public float distance(IReadablePosition other);
	public float distance2(float x, float y);
	public float distance(float x, float y);
}
