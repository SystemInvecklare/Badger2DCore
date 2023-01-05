package com.github.systeminvecklare.badger.core.math.simplex;

import java.util.ArrayList;
import java.util.List;

import com.github.systeminvecklare.badger.core.math.simplex.exception.NoFeasibleSolutionException;
import com.github.systeminvecklare.badger.core.math.simplex.exception.UnboundedFeasableSolutionException;

public class StandardFormSimplexProblem {
	private final float[] utilityCoefs;
	private final List<IStandardConstraint> constraints = new ArrayList<IStandardConstraint>();
	private ConstraintBuilder currentConstraintBuilder = null;
	
	public StandardFormSimplexProblem(float ... utilityCoefs) {
		this.utilityCoefs = utilityCoefs;
	}

	public ConstraintBuilder addConstraint(float ... coefs) {
		if(coefs.length != utilityCoefs.length) {
			throw new IllegalArgumentException("Expected "+utilityCoefs.length+" coefficients but got "+coefs.length);
		}
		if(currentConstraintBuilder != null) {
			throw new IllegalStateException("Previous constraint has not had its right-hand-side (rhs) set!");
		}
		currentConstraintBuilder = new ConstraintBuilder(coefs);
		return currentConstraintBuilder;
	}

	public float[] solve(ISimplexSolver solver) throws NoFeasibleSolutionException, UnboundedFeasableSolutionException {
		float[] result = new float[utilityCoefs.length];
		solver.solve(utilityCoefs, constraints, result);
		return result;
	}
	
	public class ConstraintBuilder {
		private final float[] coefs;
		public ConstraintBuilder(float[] coefs) {
			this.coefs = coefs;
		}

		public void rhs(float value) {
			if(currentConstraintBuilder == this) {
				constraints.add(new Constraint(coefs, value));
				currentConstraintBuilder = null;
			} else {
				throw new IllegalStateException("RHS value set on non-active row!");
			}
		}
	}
	
	private static class Constraint implements IStandardConstraint {
		private final float[] coefs;
		private final float rhs;
		
		public Constraint(float[] coefs, float rhs) {
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
		
	}
}
