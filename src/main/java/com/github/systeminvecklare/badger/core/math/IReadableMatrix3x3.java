package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;

public interface IReadableMatrix3x3 extends ICopyablePoolable<Matrix3x3> {
	public static final int M11 = 0;
	public static final int M12 = 1;
	public static final int M13 = 2;
	public static final int M21 = 3;
	public static final int M22 = 4;
	public static final int M23 = 5;
	public static final int M31 = 6;
	public static final int M32 = 7;
	public static final int M33 = 8;
	
	float getData(int coordinate);
	void getData(float[] result);
	void getData(float[] result, int offset);
	void transformAffinely(Position argumentAndResult);
	float getDeterminant();
}
