package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class Matrix3x3 extends AbstractMatrix3x3 implements IPoolable {
	private IPool<Matrix3x3> pool;
	
	private float[] data = new float[3*3];
	
	public Matrix3x3(IPool<Matrix3x3> pool) {
		this.pool = pool;
	}
	
	public Matrix3x3 setTo(IReadableVector v1, IReadableVector v2, IReadableVector translation)
	{
		data[M11] = v1.getX();
		data[M21] = v1.getY();
		data[M31] = 0;
		
		data[M12] = v2.getX();
		data[M22] = v2.getY();
		data[M32] = 0;
		
		data[M13] = translation.getX();
		data[M23] = translation.getY();
		data[M33] = 1;
		
		return this;
	}
	
	/**
	 * setTo(float m11, float m12,float m13, float m21, float m22,float m23, float m31, float m32,float m33)
	 * 
	 * @param m11
	 * @param m12
	 * @param m13
	 * @param m21
	 * @param m22
	 * @param m23
	 * @param m31
	 * @param m32
	 * @param m33
	 * @return
	 */
	public Matrix3x3 setTo(float m11, float m12,float m13, float m21, float m22,float m23, float m31, float m32,float m33)
	{
		data[M11] = m11;
		data[M12] = m12;
		data[M13] = m13;
		
		data[M21] = m21;
		data[M22] = m22;
		data[M23] = m23;
		
		data[M31] = m31;
		data[M32] = m32;
		data[M33] = m33;
		return this;
	}
	
	public Matrix3x3 setTo(IReadableMatrix3x3 other)
	{
		other.getData(this.data);
		return this;
	}
	
	public Matrix3x3 setToIdentity()
	{
		return setTo(1, 0, 0, 0, 1, 0, 0, 0, 1);
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
	
	public Matrix3x3 mult(IReadableMatrix3x3 other) {
		float oldM11 = data[M11];
		float oldM12 = data[M12];
		data[M11] = data[M11]*other.getData(M11)+data[M12]*other.getData(M21)+data[M13]*other.getData(M31);
		data[M12] = oldM11*other.getData(M12)+data[M12]*other.getData(M22)+data[M13]*other.getData(M32);
		data[M13] = oldM11*other.getData(M13)+oldM12*other.getData(M23)+data[M13]*other.getData(M33);
		
		float oldM21 = data[M21];
		float oldM22 = data[M22];
		data[M21] = data[M21]*other.getData(M11)+data[M22]*other.getData(M21)+data[M23]*other.getData(M31);
		data[M22] = oldM21*other.getData(M12)+data[M22]*other.getData(M22)+data[M23]*other.getData(M32);
		data[M23] = oldM21*other.getData(M13)+oldM22*other.getData(M23)+data[M23]*other.getData(M33);
		
		float oldM31 = data[M31];
		float oldM32 = data[M32];
		data[M31] = data[M31]*other.getData(M11)+data[M32]*other.getData(M21)+data[M33]*other.getData(M31);
		data[M32] = oldM31*other.getData(M12)+data[M32]*other.getData(M22)+data[M33]*other.getData(M32);
		data[M33] = oldM31*other.getData(M13)+oldM32*other.getData(M23)+data[M33]*other.getData(M33);
		
		return this;
	}

	public Matrix3x3 invert() {
		float determinant = getDeterminant();
		
		float oldM11 = data[M11];
		float oldM12 = data[M12];
		float oldM13 = data[M13];
		float oldM21 = data[M21];
		float oldM22 = data[M22];
		float oldM31 = data[M31];;
		data[M11] = (data[M22]*data[M33] - data[M23]*data[M32])/determinant;
		data[M12] = (data[M13]*data[M32] - data[M12]*data[M33])/determinant;
		data[M13] = (oldM12*data[M23] - data[M13]*data[M22])/determinant;
		data[M21] = (data[M23]*data[M31] - data[M21]*data[M33])/determinant;
		data[M22] = (oldM11*data[M33] - oldM13*data[M31])/determinant;
		data[M23] = (oldM13*oldM21 - oldM11*data[M23])/determinant;
		data[M31] = (oldM21*data[M32] - oldM22*data[M31])/determinant;
		data[M32] = (oldM12*oldM31 - oldM11*data[M32])/determinant;
		data[M33] = (oldM11*oldM22 - oldM12*oldM21)/determinant;

		return this;
	}
	
	public Matrix3x3 transpose() {
		float oldM12 = data[M12];
		float oldM13 = data[M13];
		float oldM23 = data[M23];
		data[M12] = data[M21];
		data[M13] = data[M31];
		data[M21] = oldM12;
		data[M23] = data[M32];
		data[M31] = oldM13;
		data[M32] = oldM23;
		return this;
	}
	
	@Override
	public float getDeterminant() {
		return data[M11]*data[M22]*data[M33] + data[M12]*data[M23]*data[M31] + data[M13]*data[M21]*data[M32] - data[M11]*data[M23]*data[M32] - data[M12]*data[M21]*data[M33] - data[M13]*data[M22]*data[M31];
	}

	public static void transformAffinely(IReadableMatrix3x3 mat, Position argumentAndResult)
	{
		float x = argumentAndResult.getX();
		float y = argumentAndResult.getY();
		float h = x*mat.getData(M31)+y*mat.getData(M32)+mat.getData(M33);
		argumentAndResult.setTo((x*mat.getData(M11)+y*mat.getData(M12)+mat.getData(M13))/h, (x*mat.getData(M21)+y*mat.getData(M22)+mat.getData(M23))/h);
	}
}
