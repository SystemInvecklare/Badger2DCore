package com.github.systeminvecklare.badger.core.math;

public interface IReadableMatrix3x3 {
	public float getData(int coordinate);
	public void getData(float[] result);
	public void getData(float[] result, int offset);
	public void transformAffinely(Position argumentAndResult);
}
