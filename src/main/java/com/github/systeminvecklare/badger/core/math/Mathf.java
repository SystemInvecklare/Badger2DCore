package com.github.systeminvecklare.badger.core.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Mathf {
	public static final float PI = (float) Math.PI;
	public static final float TWO_PI = 2f*PI;

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
	
	public static float ceil(float a) {
		return (float) Math.ceil(a);
	}

	public static float clamp(float value, float min, float max) {
		return value > max ? max : (value < min ? min : value);
	}
	
	public static int clamp(int value, int min, int max) {
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
	
	public static float lerp2(float t, float a, float b, float c) {
		float tConj = 1f - t;
		return tConj*(tConj*a+2*t*b)+t*t*c;
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

	public static int randomInt(int min, int max) {
		return (int) (Math.random()*(1+max-min)) + min;
	}

	public static float randomFloat(float min, float max) {
		return Mathf.random()*(max-min) + min;
	}
	
	public static float randomFloat(Random random, float min, float max) {
		return random.nextFloat()*(max-min) + min;
	}

	public static float randomAngle() {
		return randomFloat(0, PI*2);
	}
	
	public static int mod(int x, int y) {
		if(y < 0) {
			return -mod(x, -y);
		}
		if(x >= 0) {
			return x%y;
		} else {
			int res = y - (-x)%y;
			return res == y ? 0 : res;
		}
	}

	public static int ceilToInt(float f) {
		return (int) Math.ceil(f);
	}
	
	public static int floorToInt(float f) {
		return (int) Math.floor(f);
	}
}
