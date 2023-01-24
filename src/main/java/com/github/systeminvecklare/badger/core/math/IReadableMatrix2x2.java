package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;

public interface IReadableMatrix2x2 extends ICopyablePoolable<Matrix2x2> {
	public static final int M11 = 0;
	public static final int M12 = 1;
	public static final int M21 = 2;
	public static final int M22 = 3;
	
	float getData(int coordinate);
	void getData(float[] result);
	void getData(float[] result, int offset);
	
	float getDeterminant();
	
	Vector transform(Vector argumentAndResult);
	Position transform(Position argumentAndResult);
}
