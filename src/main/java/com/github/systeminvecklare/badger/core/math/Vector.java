package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class Vector implements IPoolable, IReadableVector {
	private float x;
	private float y;
	
	private IPool<Vector> pool;
	
	public Vector(IPool<Vector> pool) {
		this.pool = pool;
	}
	
	public Vector setToOrigin()
	{
		return setTo(0, 0);
	}
	

	public Vector setTo(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector setTo(IReadableVector other) {
		return setTo(other.getX(), other.getY());
	}
	
	public Vector setTo(IReadablePosition position) {
		return setTo(position.getX(), position.getY());
	}

	@Override
	public float getX()
	{
		return x;
	}
	
	@Override
	public float getY()
	{
		return y;
	}
	
	public Vector scale(float factor)
	{
		this.x = this.getX()*factor;
		this.y = this.getY()*factor;
		return this;
	}
	
	@Override
	public float dot(IReadableVector other)
	{
		return dot(this, other);
	}
	
	@Override
	public float cross(IReadableVector other)
	{
		return cross(this, other);
	}
	
	public Vector add(IReadableVector other)
	{
		return setTo(this.getX()+other.getX(),this.getY()+other.getY());
	}
	
	public Vector sub(IReadableVector other)
	{
		return setTo(this.getX()-other.getX(),this.getY()-other.getY());
	}
	
	public Vector hadamardMult(IReadableVector other)
	{
//		return setTo(this.getX()*other.getX(),this.getY()*other.getY());
		return hadamardMult(other.getX(), other.getY());
	}
	
	public Vector hadamardMult(float otherX, float otherY)
	{
		return setTo(this.getX()*otherX,this.getY()*otherY);
	}
	
	@Override
	public float length() {
		return Mathf.sqrt(length2());
	}
	
	@Override
	public float length2() {
		return dot(this);
	}

	public Vector rotate(IReadableDeltaRotation deltaRotation) {
		float theta = deltaRotation.getTheta();
		return rotate(theta);
	}
	
	public Vector rotate(float theta) {
		return setTo(x*Mathf.cos(theta)-y*Mathf.sin(theta), y*Mathf.cos(theta)+x*Mathf.sin(theta));
	}
	
	public static float dot(IReadableVector a, IReadableVector b)
	{
		return a.getX()*b.getX()+a.getY()*b.getY();
	}
	
	public static float cross(IReadableVector a, IReadableVector b)
	{
		return a.getX()*b.getY()-a.getY()*b.getX();
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	@Override
	public IPool<Vector> getPool() {
		return pool;
	}
	
	@Override
	public int hashCode() {
		return Float.valueOf(x).hashCode() ^ Float.valueOf(y).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof IReadableVector) && equalsOwn((IReadableVector) obj);
	}

	private boolean equalsOwn(IReadableVector obj) {
		return this.x == obj.getX() && this.y == obj.getY();
	}

	public Vector normalize() {
		return scale(1f/length());
	}

	public Vector setToUnitVector(IReadableRotation rotation) {
		return setToUnitVector(rotation.getTheta());
	}

	@Override
	public float getRotationTheta() {
		return getRotationTheta(this);
	}
	
	public static float getRotationTheta(IReadableVector vector)
	{
		return Mathf.atan2(vector.getY(), vector.getX());
	}

	public Vector add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector setToUnitVector(float theta) {
		return setTo(Mathf.cos(theta), Mathf.sin(theta));
	}
}
