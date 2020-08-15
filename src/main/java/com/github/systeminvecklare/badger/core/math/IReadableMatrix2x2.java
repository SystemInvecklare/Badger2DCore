package com.github.systeminvecklare.badger.core.math;

public interface IReadableMatrix2x2 {
	public float getData(int coordinate);
	public void getData(float[] result);
	public void getData(float[] result, int offset);
}
