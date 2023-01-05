package com.github.systeminvecklare.badger.core.math.simplex;

import java.util.List;

import com.github.systeminvecklare.badger.core.math.simplex.exception.NoFeasibleSolutionException;
import com.github.systeminvecklare.badger.core.math.simplex.exception.UnboundedFeasableSolutionException;

/**
 * Solves a linear optimization problem given in standard form:
 * Maximize: a[0]x_0 + a[1]x_1 + ... +a[n]x_n
 * IStandardConstraint...
 * x_0,x_1... .> 0.  
 */
public interface ISimplexSolver {
	void solve(float[] utilityFactors, List<IStandardConstraint> constraints, float[] result) throws NoFeasibleSolutionException, UnboundedFeasableSolutionException;
}
