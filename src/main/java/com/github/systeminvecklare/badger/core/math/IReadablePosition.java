package com.github.systeminvecklare.badger.core.math;

public interface IReadablePosition {
	public float getX();
	public float getY();
	public Vector vectorTo(IReadablePosition other, Vector result);
	public float length2();
	public float length();
}
