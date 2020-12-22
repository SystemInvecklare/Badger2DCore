package com.github.systeminvecklare.badger.core.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

	public static float clamp(float value, float min, float max) {
		return value > max ? max : (value < min ? min : value);
	}

	public static float mod(float value, float mod) {
		if(mod < 0f) {
			mod = -mod;
		}
		if(value == 0f) {
			return value;
		} else {
			return value - mod*Mathf.floor(value/mod);
		}
	}
	
	public static float lerpClamp(float t, float interpolationLength, float min, float max) {
		if(t >= interpolationLength) {
			return max;
		} else if(t <= 0) {
			return min;
		}
		float relativeT = t/interpolationLength;
		return min+relativeT*(max-min);
	}
	
	public static float lerp(float t, float a, float b) {
		return (1f-t)*a+t*b;
	}

	public static float conjPow(float t, float exp) {
		return 1f-Mathf.pow(1f-t, exp);
	}

	public static <T> List<T> randomOrder(Collection<T> collection) {
		// Collections.shuffle() isn't supported by our version of Gwt...
		List<T> copy = new ArrayList<T>(collection);
		List<T> result = new ArrayList<T>();
		while(!copy.isEmpty()) {
			result.add(copy.remove((int) Math.floor(Math.random()*copy.size())));
		}
		return result;
	}
}
