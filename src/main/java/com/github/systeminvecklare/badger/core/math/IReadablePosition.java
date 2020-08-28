package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;

public interface IReadablePosition extends ICopyablePoolable<Position> {
	public float getX();
	public float getY();
	public Vector vectorTo(IReadablePosition other, Vector result);
	public float length2();
	public float length();
}
