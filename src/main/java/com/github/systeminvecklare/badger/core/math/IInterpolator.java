package com.github.systeminvecklare.badger.core.math;

/**
 * <p>Functions used for interpolation.</p>
 * <p>The only rules are:</p>
 * <ul>
 *    <li>eval(0) = 0</li>
 *    <li>eval(1) = 1</li>
 *    <li>eval(t) defined for t in [0,1]</li>
 * </ul>
 * @author Mattias Selin
 */
public interface IInterpolator {
	float eval(float t);
}
