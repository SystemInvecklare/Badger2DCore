package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;

public interface IReadableVector extends ICopyablePoolable<Vector> {
	public float getX();
	public float getY();
	public float length();
	public float length2();
	public float dot(IReadableVector other);
	public float cross(IReadableVector other);
	public float getRotationTheta();
}
