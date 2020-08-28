package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;

public interface IReadableMatrix2x2 extends ICopyablePoolable<Matrix2x2> {
	public float getData(int coordinate);
	public void getData(float[] result);
	public void getData(float[] result, int offset);
}
