package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.math.IInterpolator;

public class InterpolatingBehavior extends AbstractInterpolatingBehavior<InterpolatingBehavior> {
	public InterpolatingBehavior(float duration, IInterpolator interpolator, boolean initialState,
			boolean initialTargetState) {
		super(duration, interpolator, initialState, initialTargetState);
	}

	public InterpolatingBehavior(float duration, IInterpolator interpolator, boolean autoStart) {
		super(duration, interpolator, autoStart);
	}

	public InterpolatingBehavior(float duration, IInterpolator interpolator) {
		super(duration, interpolator);
	}

	public InterpolatingBehavior(float duration) {
		super(duration);
	}
}
