package com.github.systeminvecklare.badger.core.math.simplex;

public class StandardConstraint implements IStandardConstraint {
	public static class Builder {
		private final float[] coefs;
		private float rhs = 0;
		
		public Builder(int variables) {
			coefs = new float[variables];
		}
		
		public Builder setCoef(int variable, float value) {
			coefs[variable] = value;
			return this;
		}
		
		public Builder setMax(float value) {
			rhs = value;
			return this;
		}
		
		public float getMax() {
			return rhs;
		}
		
		public IStandardConstraint build() {
			return new StandardConstraint(coefs, rhs);
		}
	}
	
	private final float[] coefs;
	private final float rhs;

	private StandardConstraint(float[] coefs, float rhs) {
		this.coefs = coefs;
		this.rhs = rhs;
	}

	@Override
	public float getMax() {
		return rhs;
	}

	@Override
	public void getCoefs(float[] result) {
		System.arraycopy(coefs, 0, result, 0, coefs.length);
	}

	public static Builder builder(int variables) {
		return new Builder(variables);
	}
}
