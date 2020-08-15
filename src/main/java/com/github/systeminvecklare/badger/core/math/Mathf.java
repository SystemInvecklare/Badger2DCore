package com.github.systeminvecklare.badger.core.math;

public class Mathf {
	public static final float PI = (float) Math.PI;

	public static float sin(float theta)
	{
		return (float) Math.sin(theta);
	}
	
	public static float cos(float theta)
	{
		return (float) Math.cos(theta);
	}
	
	public static float sqrt(float a)
	{
		return (float) Math.sqrt(a);
	}

	public static float atan2(float y, float x) {
		return (float) Math.atan2(y, x);
	}

	public static float random() {
		return (float) Math.random();
	}

	public static float pow(float a, float b) {
		return (float) Math.pow(a, b);
	}

	public static float floor(float a) {
		return (float) Math.floor(a);
	}
}
