package com.github.systeminvecklare.badger.core.math.simplex;

/**
 * Represents a constraint on the form
 * c[0]*x_0+c[1]*x_1 + ... c[n]*x_n <= getMax() 
 */
public interface IStandardConstraint {
	float getMax();
	void getCoefs(float[] result);
}
