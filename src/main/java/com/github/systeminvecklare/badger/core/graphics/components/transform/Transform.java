package com.github.systeminvecklare.badger.core.graphics.components.transform;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.math.IReadableDeltaRotation;
import com.github.systeminvecklare.badger.core.math.IReadableMatrix3x3;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.IReadableRotation;
import com.github.systeminvecklare.badger.core.math.IReadableVector;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.math.Matrix2x2;
import com.github.systeminvecklare.badger.core.math.Matrix3x3;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.math.Rotation;
import com.github.systeminvecklare.badger.core.math.Vector;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.util.PoolableArrayOf16Floats;

public class Transform extends AbstractTransform {
	private IPool<ITransform> pool;
	
	private final Position position = new Position(null); //This is how you should obtain objects that you have no intention of returning
	private final Vector scale = new Vector(null);
	private final Rotation rotation = new Rotation(null);
	/**
	 * the <b>shx</b> value
	 *   <br>[   1   shx   0   ]
         <br>[  shy   1    0   ]
         <br>[   0    0    1   ]

	 */
	private float shear;
	
	public Transform(IPool<ITransform> pool) {
		this.pool = pool;
	}

	@Override
	public void free() {
		pool.free(this);
	}

	@Override
	public Transform setTo(IReadablePosition position, IReadableVector scale,IReadableRotation rotation, float shear) {
		this.position.setTo(position);
		this.scale.setTo(scale);
		this.rotation.setTo(rotation);
		this.shear = shear;
		return this;
	}

	@Override
	public Transform setTo(IReadableTransform other) {
		return setTo(other.getPosition(), other.getScale(), other.getRotation(), other.getShear());
	}
	
	@Override
	public Transform setToIdentity() {
		this.position.setToOrigin();
		this.scale.setTo(1, 1);
		this.rotation.setToZero();
		this.shear = 0;
		return this;
	}
	
	@Override
	public ITransform setPosition(IReadablePosition position) {
		this.position.setTo(position);
		return this;
	}
	
	@Override
	public ITransform setRotation(IReadableRotation rotation) {
		this.rotation.setTo(rotation);
		return this;
	}
	
	@Override
	public ITransform setScale(IReadableVector scale) {
		this.scale.setTo(scale);
		return this;
	}
	
	@Override
	public ITransform setShear(float shear) {
		this.shear = shear;
		return this;
	}

	@Override
	public Transform mult(IReadableTransform other) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			Matrix3x3 thisMatrixTransform = calculateMatrix3x3(this, ep.obtain(Matrix3x3.class));
			Matrix3x3 otherMatrixTransform = calculateMatrix3x3(other, ep.obtain(Matrix3x3.class));
			thisMatrixTransform.mult(otherMatrixTransform);
			
			return setTo(thisMatrixTransform);
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}
	
	private static void decompose(IReadableMatrix3x3 matrix, Matrix3x3 rot, Matrix3x3 scaleShear) {
		float m11 = matrix.getData(Matrix3x3.M11);
		float m21 = matrix.getData(Matrix3x3.M21);
		Vector v = FlashyEngine.get().getPoolManager().getPool(Vector.class).obtain();
		try {
			v.setTo(m11-Mathf.sqrt(m11*m11+m21*m21),m21);
			if(v.length2() < 0.000001f) {
				v.setTo(m11+Mathf.sqrt(m11*m11+m21*m21),m21);
			}
			v.normalize();
			
			rot.setToIdentity();
			PoolableArrayOf16Floats rotDataHolder = FlashyEngine.get().getPoolManager().getPool(PoolableArrayOf16Floats.class).obtain();
			try {
				float[] rotData = rotDataHolder.getArray();
				rot.getData(rotData);
				rotData[Matrix3x3.M11] += -2*v.getX()*v.getX();
				rotData[Matrix3x3.M12] += -2*v.getX()*v.getY();
				rotData[Matrix3x3.M21] += -2*v.getX()*v.getY();
				rotData[Matrix3x3.M22] += -2*v.getY()*v.getY();
				rot.setTo(rotData[Matrix3x3.M11], rotData[Matrix3x3.M12], rotData[Matrix3x3.M13], rotData[Matrix3x3.M21], rotData[Matrix3x3.M22], rotData[Matrix3x3.M23], rotData[Matrix3x3.M31], rotData[Matrix3x3.M32], rotData[Matrix3x3.M33]);
			} finally {
				rotDataHolder.free();
			}
		} finally {
			v.free();
		}
		
		scaleShear.setTo(rot).mult(matrix);
		
		if(rot.getDeterminant() < 0) {
			{
				PoolableArrayOf16Floats rotDataHolder = FlashyEngine.get().getPoolManager().getPool(PoolableArrayOf16Floats.class).obtain();
				try {
					float[] rotData = rotDataHolder.getArray();
					rot.getData(rotData);
					rotData[Matrix3x3.M11] *= -1;
					rotData[Matrix3x3.M21] *= -1;
					rotData[Matrix3x3.M31] *= -1;
					rot.setTo(rotData[Matrix3x3.M11], rotData[Matrix3x3.M12], rotData[Matrix3x3.M13], rotData[Matrix3x3.M21], rotData[Matrix3x3.M22], rotData[Matrix3x3.M23], rotData[Matrix3x3.M31], rotData[Matrix3x3.M32], rotData[Matrix3x3.M33]);
				} finally {
					rotDataHolder.free();
				}
			}
			
			{
				PoolableArrayOf16Floats scaleShearDataHolder = FlashyEngine.get().getPoolManager().getPool(PoolableArrayOf16Floats.class).obtain();
				try {
					float[] scaleShearData = scaleShearDataHolder.getArray();
					scaleShear.getData(scaleShearData);
					scaleShearData[Matrix3x3.M11] *= -1;
					scaleShearData[Matrix3x3.M12] *= -1;
					scaleShearData[Matrix3x3.M13] *= -1;
					scaleShear.setTo(scaleShearData[Matrix3x3.M11], scaleShearData[Matrix3x3.M12], scaleShearData[Matrix3x3.M13], scaleShearData[Matrix3x3.M21], scaleShearData[Matrix3x3.M22], scaleShearData[Matrix3x3.M23], scaleShearData[Matrix3x3.M31], scaleShearData[Matrix3x3.M32], scaleShearData[Matrix3x3.M33]);
				} finally {
					scaleShearDataHolder.free();
				}
			}
		}
		
		if(scaleShear.getData(Matrix3x3.M11) < 0 && scaleShear.getData(Matrix3x3.M22) < 0) {
			{
				PoolableArrayOf16Floats rotDataHolder = FlashyEngine.get().getPoolManager().getPool(PoolableArrayOf16Floats.class).obtain();
				try {
					float[] rotData = rotDataHolder.getArray();
					rot.getData(rotData);
					rotData[Matrix3x3.M11] *= -1;
					rotData[Matrix3x3.M21] *= -1;
					rotData[Matrix3x3.M12] *= -1;
					rotData[Matrix3x3.M22] *= -1;
					rot.setTo(rotData[Matrix3x3.M11], rotData[Matrix3x3.M12], rotData[Matrix3x3.M13], rotData[Matrix3x3.M21], rotData[Matrix3x3.M22], rotData[Matrix3x3.M23], rotData[Matrix3x3.M31], rotData[Matrix3x3.M32], rotData[Matrix3x3.M33]);
				} finally {
					rotDataHolder.free();
				}
			}
			
			{
				PoolableArrayOf16Floats scaleShearDataHolder = FlashyEngine.get().getPoolManager().getPool(PoolableArrayOf16Floats.class).obtain();
				try {
					float[] scaleShearData = scaleShearDataHolder.getArray();
					scaleShear.getData(scaleShearData);
					scaleShearData[Matrix3x3.M11] *= -1;
					scaleShearData[Matrix3x3.M21] *= -1;
					scaleShearData[Matrix3x3.M12] *= -1;
					scaleShearData[Matrix3x3.M22] *= -1;
					scaleShear.setTo(scaleShearData[Matrix3x3.M11], scaleShearData[Matrix3x3.M12], scaleShearData[Matrix3x3.M13], scaleShearData[Matrix3x3.M21], scaleShearData[Matrix3x3.M22], scaleShearData[Matrix3x3.M23], scaleShearData[Matrix3x3.M31], scaleShearData[Matrix3x3.M32], scaleShearData[Matrix3x3.M33]);
				} finally {
					scaleShearDataHolder.free();
				}
			}
		}
	}
	
	private Transform setTo(Matrix3x3 matrix3x3)
	{
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			this.position.setTo(matrix3x3.getData(Matrix3x3.M13),matrix3x3.getData(Matrix3x3.M23));
			
			Matrix3x3 rotationMatrix = ep.obtain(Matrix3x3.class);
			Matrix3x3 scaleShearMatrix = ep.obtain(Matrix3x3.class);
			
			decompose(matrix3x3, rotationMatrix, scaleShearMatrix);
			
			this.rotation.setTo(ep.obtain(Vector.class).setTo(rotationMatrix.getData(Matrix3x3.M11), rotationMatrix.getData(Matrix3x3.M21)));
			this.scale.setTo(scaleShearMatrix.getData(Matrix3x3.M11), scaleShearMatrix.getData(Matrix3x3.M22));
			this.shear = scaleShearMatrix.getData(Matrix3x3.M12);
			
			return this;
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}
	
	private static Matrix3x3 calculateMatrix3x3(IReadableTransform transform,Matrix3x3 matrix3x3)
	{
		EasyPooler ep = EasyPooler.obtainFresh(); //TODO obtain real pools for less overhead.
		try
		{
			float theta = transform.getRotation().getTheta();
			Matrix2x2 rotationMat = ep.obtain(Matrix2x2.class).setTo(Mathf.cos(theta),-Mathf.sin(theta),Mathf.sin(theta),Mathf.cos(theta));
			IReadableVector scale = transform.getScale();
			Matrix2x2 shearScaleMat = ep.obtain(Matrix2x2.class).setTo(scale.getX(), transform.getShear()*scale.getY(),0, scale.getY());
			
			rotationMat.mult(shearScaleMat);
			
			return matrix3x3.setTo(rotationMat.getColumn(0),rotationMat.getColumn(1),ep.obtain(Vector.class).setTo(transform.getPosition()));
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}
	

	@Override
	public Transform multLeft(IReadableTransform other) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			ITransform otherCopy = ep.obtain(ITransform.class).setTo(other);
			otherCopy.mult(this);
			return setTo(otherCopy);
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}

	@Override
	public Transform invert() throws NonInvertibleMatrixException {
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			Matrix3x3 thisMatrix = calculateMatrix3x3(this, ep.obtain(Matrix3x3.class));
			return setTo(thisMatrix.invert());
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}
	
	@Override
	public void transform(Position argumentAndResult) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			Matrix3x3 thisMatrix = calculateMatrix3x3(this, ep.obtain(Matrix3x3.class));
			thisMatrix.transformAffinely(argumentAndResult);
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}
	
	@Override
	public IReadablePosition getPosition() {
		return position;
	}

	@Override
	public IReadableRotation getRotation() {
		return rotation;
	}

	@Override
	public IReadableVector getScale() {
		return scale;
	}

	@Override
	public float getShear() {
		return shear;
	}

	@Override
	public ITransform setPosition(float x, float y) {
		position.setTo(x, y);
		return this;
	}

	@Override
	public ITransform setRotation(float theta) {
		rotation.setTo(theta);
		return this;
	}

	@Override
	public ITransform setScale(float x, float y) {
		scale.setTo(x, y);
		return this;
	}

	@Override
	public ITransform addToPosition(float dx, float dy) {
		position.add(dx, dy);
		return this;
	}

	@Override
	public ITransform addToPosition(IReadableVector dvector) {
		position.add(dvector);
		return this;
	}

	@Override
	public ITransform addToRotation(float dtheta) {
		rotation.add(dtheta);
		return this;
	}

	@Override
	public ITransform addToRotation(IReadableDeltaRotation drotation) {
		return addToRotation(drotation.getTheta());
	}
	
	@Override
	public ITransform multiplyScale(float sx, float sy) {
		scale.hadamardMult(sx,sy);
		return this;
	}
	
	@Override
	public ITransform multiplyScale(IReadableVector dscale) {
		return this.multiplyScale(dscale.getX(), dscale.getY());
	}
	
	@Override
	public ITransform multiplyScale(float s) {
		return this.multiplyScale(s, s);
	}
	
	@Override
	public ITransform copy() {
		return new Transform(null).setTo(this);
	}
}
