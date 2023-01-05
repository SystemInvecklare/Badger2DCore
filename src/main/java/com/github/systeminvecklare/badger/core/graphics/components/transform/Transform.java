package com.github.systeminvecklare.badger.core.graphics.components.transform;

import com.github.systeminvecklare.badger.core.math.IReadableDeltaRotation;
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
			
			//Parse out data and update this transform
//			this.position.setTo(thisMatrixTransform.getData(Matrix3x3.M13),thisMatrixTransform.getData(Matrix3x3.M23));
//			
//			Matrix2x2 _2dmatrix = ep.obtain(Matrix2x2.class).setTo(thisMatrixTransform.getData(Matrix3x3.M11), thisMatrixTransform.getData(Matrix3x3.M12), thisMatrixTransform.getData(Matrix3x3.M21), thisMatrixTransform.getData(Matrix3x3.M22));
//			
//			Vector temp = ep.obtain(Vector.class);
//			
//			Vector u0 = ep.obtain(Vector.class).setTo(_2dmatrix.getColumn(0));
//			Vector e0 = ep.obtain(Vector.class).setTo(u0).normalize();
//			Vector u1 = ep.obtain(Vector.class).setTo(_2dmatrix.getColumn(1)).sub(temp.setTo(e0).scale(e0.dot(_2dmatrix.getColumn(1))));
//			Vector e1 = ep.obtain(Vector.class).setTo(u1).normalize();
//			
//			double scaleX = e0.dot(_2dmatrix.getColumn(0));
//			double scaleY = e1.dot(_2dmatrix.getColumn(1));
//			double newShear = e0.dot(_2dmatrix.getColumn(1))/scaleY; 
//			this.scale.setTo(scaleX, scaleY);
//			this.shear = newShear;
//			//e0.getX() == cos(theta), e0.getY() == sin(theta)
//			this.rotation.setTo(e0);
//			
//			return this;
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}
	
	private Transform setTo(Matrix3x3 matrix3x3)
	{
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			this.position.setTo(matrix3x3.getData(Matrix3x3.M13),matrix3x3.getData(Matrix3x3.M23));
			
			Matrix2x2 _2dmatrix = ep.obtain(Matrix2x2.class).setTo(matrix3x3.getData(Matrix3x3.M11), matrix3x3.getData(Matrix3x3.M12), matrix3x3.getData(Matrix3x3.M21), matrix3x3.getData(Matrix3x3.M22));
			
			Vector temp = ep.obtain(Vector.class);
			
			Vector u0 = ep.obtain(Vector.class).setTo(_2dmatrix.getColumn(0));
			Vector e0 = ep.obtain(Vector.class).setTo(u0).normalize();
			Vector u1 = ep.obtain(Vector.class).setTo(_2dmatrix.getColumn(1)).sub(temp.setTo(e0).scale(e0.dot(_2dmatrix.getColumn(1))));
			Vector e1 = ep.obtain(Vector.class).setTo(u1).normalize();
			
			float scaleX = e0.dot(_2dmatrix.getColumn(0));
			float scaleY = e1.dot(_2dmatrix.getColumn(1));
			float newShear = e0.dot(_2dmatrix.getColumn(1))/scaleY; 
			this.scale.setTo(scaleX, scaleY);
			this.shear = newShear;
			//e0.getX() == cos(theta), e0.getY() == sin(theta)
			this.rotation.setTo(e0);
			
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
	public Transform invert() {
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
