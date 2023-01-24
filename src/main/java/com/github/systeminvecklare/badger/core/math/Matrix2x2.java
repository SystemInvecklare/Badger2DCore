package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class Matrix2x2 extends AbstractMatrix2x2 implements IPoolable {
	private IPool<Matrix2x2> pool;
	
	private float[] data = new float[2*2];
	private final ColumnVector[] columnVectors = new ColumnVector[]{new ColumnVector(M11, M21),new ColumnVector(M12, M22)}; 
	
	public Matrix2x2(IPool<Matrix2x2> pool) {
		this.pool = pool;
	}
	
	public Matrix2x2 setTo(IReadableVector v1, IReadableVector v2)
	{
		data[M11] = v1.getX();
		data[M21] = v1.getY();
		
		data[M12] = v2.getX();
		data[M22] = v2.getY();
		
		return this;
	}
	
	/**
	 * setTo(float m11, float m12, float m21, float m22)
	 * 
	 * @param m11
	 * @param m12
	 * @param m21
	 * @param m22
	 * @return
	 */
	public Matrix2x2 setTo(float m11, float m12, float m21, float m22)
	{
		data[M11] = m11;
		data[M12] = m12;
		
		data[M21] = m21;
		data[M22] = m22;
		return this;
	}
	
	public Matrix2x2 setTo(IReadableMatrix2x2 other)
	{
		other.getData(this.data);
		return this;
	}
	
	public Matrix2x2 setToIdentity()
	{
		return setTo(1,0,0,1);
	}

	@Override
	public void free() {
		pool.free(this);
	}
	
	@Override
	public float getData(int coordinate) {
		return data[coordinate];
	}
	
	@Override
	public void getData(float[] result, int offset) {
		System.arraycopy(data, 0, result, offset, data.length);
	}
	
	public Matrix2x2 mult(Matrix2x2 other) {
		float oldM11 = data[M11];
		float oldM21 = data[M21];
		data[M11] = data[M11]*other.getData(M11)+data[M12]*other.getData(M21);
		data[M12] = oldM11*other.getData(M12)+data[M12]*other.getData(M22);
		data[M21] = data[M21]*other.getData(M11)+data[M22]*other.getData(M21);
		data[M22] = oldM21*other.getData(M12)+data[M22]*other.getData(M22);
		return this;
	}
	
	@Override
	public float getDeterminant() {
		return data[M11]*data[M22] - data[M12]*data[M21];
	}
	
	public Matrix2x2 invert() {
		float determinant = getDeterminant();
		
		float oldM11 = data[M11];
		
		data[M11] = data[M22]/determinant;
		data[M12] = -data[M12]/determinant;
		data[M21] = -data[M21]/determinant;
		data[M22] = oldM11/determinant;
		
		return this;
	}
	
	public Matrix2x2 transpose() {
		float oldM12 = data[M12];
		data[M12] = data[M21];
		data[M21] = oldM12;
		return this;
	}

	/**
	 * column 0 and column 1.
	 * 
	 * @param column
	 * @return
	 */
	public IReadableVector getColumn(int column) {
		return columnVectors[column];
	}
	
	private class ColumnVector extends AbstractVector {
		private int coordX;
		private int coordY;

		public ColumnVector(int coordX, int coordY) {
			this.coordX = coordX;
			this.coordY = coordY;
		}

		@Override
		public float getX() {
			return getData(coordX);
		}

		@Override
		public float getY() {
			return getData(coordY);
		}
	}
}
