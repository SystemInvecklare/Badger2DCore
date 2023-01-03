package com.github.systeminvecklare.badger.core.math;

public class Interpolators {
	private static final ExtensibleInterpolator LINEAR = new ExtensibleInterpolator() {
		@Override
		public float evalOpen(float t) {
			return t;
		}
	};
	private static final ExtensibleInterpolator SINE = new ExtensibleInterpolator() {
		@Override
		protected float evalOpen(float t) {
			return (1f-Mathf.cos(t*Mathf.PI))/2;
		}
	};
	private static final ExtensibleInterpolator SINE_IN = new ExtensibleInterpolator() {
		@Override
		protected float evalOpen(float t) {
			return Mathf.sin(t*Mathf.PI/2);
		}
	};
	private static final ExtensibleInterpolator SINE_OUT = new ExtensibleInterpolator() {
		@Override
		protected float evalOpen(float t) {
			return Mathf.sin(t*Mathf.PI/2);
		}
	};
	private static final ExtensibleInterpolator EASE = sympower(2);
	private static final IInterpolator PSEUDO_INTERPOLATOR_ZERO = new IInterpolator() {
		@Override
		public float eval(float t) {
			return 0;
		}
	};
	private static final IInterpolator PSEUDO_INTERPOLATOR_ONE = new IInterpolator() {
		@Override
		public float eval(float t) {
			return 1;
		}
	};
	
	public static ExtensibleInterpolator linear() {
		return LINEAR;
	}
	

	public static IInterpolator ease() {
		return EASE;
	}
	
	public static ExtensibleInterpolator cubic(final float derivativeAtZero, final float derivativeAtOne) {
		// f(t) = t*((t-1)*g(t) + 1)
		// f(0) = 0
		// f(1) = 1
		// f'(t) = (t-1)*g(t)+1+t*(g(t)+(t-1)*g'(t))
		// f'(0)=  1-g(0)
		// f'(1) = 1+g(1)
		// --> g(0) = 1-f'(0), g(1) = f'(1)-1
		// So let g(t) = (1-t)*(1-f'(0))+t*(f'(1)-1) = 1-f'(0) + t*(f'(0) + f'(1) - 2)
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return t*(
							(t-1)*(
									(1-derivativeAtZero)+t*(derivativeAtZero+derivativeAtOne-2)
								  )
								  +1
						 );
			}
		};
	}
	
	public static ExtensibleInterpolator power(final float power) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return Mathf.pow(t, power);
			}
		};
	}
	
	public static ExtensibleInterpolator sympower(final float power) {
		return power(power).symmetrize();
	}
	
	/**
	 * <pre><code>
	 *      -  
	 *    / 
	 *  -
	 * </code></pre>
	 */
	public static ExtensibleInterpolator sine() {
		return SINE;
	}
	
	/**
	 * <pre><code>
	 *    -- 
	 *  /
	 * </code></pre>
	 */
	public static ExtensibleInterpolator sineIn() {
		return SINE_IN;
	}
	
	/**
	 * <pre><code>
	 *     / 
	 *  --
	 * </code></pre>
	 */
	public static ExtensibleInterpolator sineOut() {
		return SINE_OUT;
	}
	
	public static ExtensibleInterpolator of(final IInterpolator interpolator) {
		if(interpolator instanceof ExtensibleInterpolator) {
			return (ExtensibleInterpolator) interpolator;
		} else {
			return new ExtensibleInterpolator() {
				@Override
				protected float evalOpen(float t) {
					return interpolator.eval(t);
				}
			};
		}
	}
	
	/**
	 * <p>Extends the interpolator by prepending the function with constant 0 at the start and constant 1 at the end.</p>
	 * <p>{@code prepend} and @{code append} are the 'weights' of each section. The middle section always has weight 1.</p>
	 * <p>Ex: {@code extend(interpolator, 1, 1)} will be constant 0 for the first third of [0,1] then the interpolator is
	 * applied during the next third, and finally it will be constant 1 for the last third.</p>
	 */
	public static ExtensibleInterpolator extend(final IInterpolator interpolator, final float prepend, final float append) {
		final float totalWeight = 1+prepend+append;
		return new ExtensibleInterpolator() {
			@Override
			protected float evalOpen(float t) {
				 t *= totalWeight;
				 t -= prepend;
				 if(t < 0) {
					 return 0;
				 } else if(t > 1) {
					 return 1;
				 } else {
					 return interpolator.eval(t);
				 }
			}
		};
	}
	
	public static ExtensibleInterpolator easeIn(final IInterpolator interpolator) {
		return easeIn(interpolator, 1);
	}
	
	public static ExtensibleInterpolator easeIn(IInterpolator interpolator, float amount) {
		return easeIn(interpolator, EASE, amount);
	}
	
	public static ExtensibleInterpolator easeIn(IInterpolator interpolator, IInterpolator ease) {
		return easeIn(interpolator, ease, 1);
	}
	
	public static ExtensibleInterpolator easeIn(IInterpolator interpolator, IInterpolator ease, float amount) {
		amount = Mathf.clamp(amount, 0, 1);
		if(amount == 0f) {
			return of(interpolator);
		}
		return interpolate(interpolator, PSEUDO_INTERPOLATOR_ONE, extend(ease, (2-amount)/amount, 0)); 
	}
	
	public static ExtensibleInterpolator easeOut(final IInterpolator interpolator) {
		return easeOut(interpolator, 1);
	}
	
	public static ExtensibleInterpolator easeOut(final IInterpolator interpolator, float amount) {
		return easeOut(interpolator, EASE, amount);
	}
	
	public static ExtensibleInterpolator easeOut(final IInterpolator interpolator, final IInterpolator ease) {
		return easeOut(interpolator, ease, 1);
	}
	
	public static ExtensibleInterpolator easeOut(final IInterpolator interpolator, final IInterpolator ease, float amount) {
		amount = Mathf.clamp(amount, 0, 1);
		if(amount == 0f) {
			return of(interpolator);
		}
		return interpolate(PSEUDO_INTERPOLATOR_ZERO, interpolator, extend(ease, 0, (2-amount)/amount));
	}
	
	public static ExtensibleInterpolator easeInOut(final IInterpolator interpolator) {
		return easeInOut(interpolator, 1);
	}
	
	public static ExtensibleInterpolator easeInOut(final IInterpolator interpolator, float amount) {
		return easeInOut(interpolator, EASE, amount);
	}
	
	public static ExtensibleInterpolator easeInOut(final IInterpolator interpolator, final IInterpolator ease) {
		return easeInOut(interpolator, ease, 1);
	}
	
	public static ExtensibleInterpolator easeInOut(final IInterpolator interpolator, final IInterpolator ease, float amount) {
		if(amount == 0f) {
			return of(interpolator);
		}
		return easeIn(easeOut(interpolator, ease, amount), ease, amount);
	}
	
	/**
	 * Scales around y = x
	 */
	public static ExtensibleInterpolator scale(final IInterpolator interpolator, final float amount) {
		return new ExtensibleInterpolator() {
			@Override
			protected float evalOpen(float t) {
				return (interpolator.eval(t)-t)*amount + t;
			}
		};
	}
	
	public static ExtensibleInterpolator wobble(final IInterpolator interpolator, final float amount, final int wobbles) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return interpolator.eval(t) + amount*Mathf.sin(Mathf.PI*t*wobbles);
			}
		};
	}
	
	public static ExtensibleInterpolator symmetrize(final IInterpolator interpolator) {
		return concatenate(interpolator, conjugate(interpolator));
	}
	
	public static ExtensibleInterpolator concatenate(final IInterpolator a, final IInterpolator b) {
		return new ExtensibleInterpolator() {
			@Override
			protected float evalOpen(float t) {
				if(t < 0.5f) {
					return a.eval(2*t)*0.5f;
				} else if(t > 0.5f) {
					return b.eval(2*t - 1)*0.5f+0.5f;
				} else {
					return 0.5f;
				}
			}
		};
	}
	
	public static ExtensibleInterpolator repeat(final IInterpolator interpolator, final int times) {
		return new ExtensibleInterpolator() {
			@Override
			protected float evalOpen(float t) {
				float scaledT = t*times;
				float ft = Mathf.mod(scaledT, 1);
				return (interpolator.eval(ft) + (scaledT - ft))/times;
			}
		};
	}
	
	public static ExtensibleInterpolator power(final IInterpolator interpolator, final float power) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return Mathf.pow(interpolator.eval(t), power);
			}
		};
	}
	
	public static ExtensibleInterpolator multiply(final IInterpolator a, final IInterpolator b) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return a.eval(t)*b.eval(t);
			}
		};
	}
	
	public static ExtensibleInterpolator mix(final IInterpolator a, final IInterpolator b, final float amount) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return (1-amount)*a.eval(t)+amount*b.eval(t);
			}
		};
	}
	
	public static ExtensibleInterpolator lerp(final IInterpolator a, final IInterpolator b) {
		return interpolate(a, b, LINEAR);
	}
	
	/**
	 * Uses {@code interpolator} to interpolate between {@code a} and {@code b}.
	 */
	public static ExtensibleInterpolator interpolate(final IInterpolator a, final IInterpolator b, final IInterpolator interpolator) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				final float amount = interpolator.eval(t);
				return (1-amount)*a.eval(t)+amount*b.eval(t);
			}
		};
	}
	
	public static ExtensibleInterpolator compose(final IInterpolator a, final IInterpolator b) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return a.eval(b.eval(t));
			}
		};
	}
	
	public static ExtensibleInterpolator clamp(final IInterpolator interpolator) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return Mathf.clamp(interpolator.eval(t), 0, 1);
			}
		};
	}
	
	public static ExtensibleInterpolator conjugate(final IInterpolator interpolator) {
		return new ExtensibleInterpolator() {
			@Override
			public float evalOpen(float t) {
				return 1f-interpolator.eval(1f-t);
			}
		};
	}
	
	private Interpolators() {}
	
	public static abstract class ExtensibleInterpolator implements IInterpolator {
		@Override
		public final float eval(float t) {
			return t == 0 ? 0 : (t == 1 ? 1 : evalOpen(t));
		}
		
		protected abstract float evalOpen(float t);
		
		public ExtensibleInterpolator compose(IInterpolator b) {
			return Interpolators.compose(this, b);
		}
		
		public ExtensibleInterpolator precompose(IInterpolator a) {
			return Interpolators.compose(a, this);
		}
		
		public ExtensibleInterpolator clamp() {
			return Interpolators.clamp(this);
		};
		
		public ExtensibleInterpolator conjugate() {
			return Interpolators.conjugate(this);
		}
		
		public ExtensibleInterpolator power(float power) {
			return Interpolators.power(this, power);
		}
		
		public ExtensibleInterpolator multiply(IInterpolator b) {
			return Interpolators.multiply(this, b);
		}
		
		public ExtensibleInterpolator mix(IInterpolator b, float amount) {
			return Interpolators.mix(this, b, amount);
		}
		
		public ExtensibleInterpolator lerp(IInterpolator b) {
			return Interpolators.lerp(this, b);
		}
		
		public ExtensibleInterpolator prelerp(IInterpolator a) {
			return Interpolators.lerp(a, this);
		}
		
		public ExtensibleInterpolator interpolate(IInterpolator b, IInterpolator interpolator) {
			return Interpolators.interpolate(this, b, interpolator);
		}
		
		public ExtensibleInterpolator wobble(float amount, int wobbles) {
			return Interpolators.wobble(this, amount, wobbles);
		}
		
		public ExtensibleInterpolator symmetrize() {
			return Interpolators.symmetrize(this);
		}
		
		public ExtensibleInterpolator concatenate(IInterpolator b) {
			return Interpolators.concatenate(this, b);
		}
		
		public ExtensibleInterpolator preconcatenate(IInterpolator a) {
			return Interpolators.concatenate(a, this);
		}
		
		public ExtensibleInterpolator repeat(int times) {
			return Interpolators.repeat(this, times);
		}
		
		/**
		 * Scales around y = x
		 */
		public ExtensibleInterpolator scale(float amount) {
			return Interpolators.scale(this, amount);
		}
		
		public ExtensibleInterpolator easeIn() {
			return Interpolators.easeIn(this);
		}
		
		public ExtensibleInterpolator easeOut() {
			return Interpolators.easeOut(this);
		}
		
		public ExtensibleInterpolator easeInOut() {
			return Interpolators.easeInOut(this);
		}
		
		public ExtensibleInterpolator easeIn(IInterpolator ease) {
			return Interpolators.easeIn(this, ease);
		}
		
		public ExtensibleInterpolator easeOut(IInterpolator ease) {
			return Interpolators.easeOut(this, ease);
		}
		
		public ExtensibleInterpolator easeInOut(IInterpolator ease) {
			return Interpolators.easeInOut(this, ease);
		}
		
		public ExtensibleInterpolator easeIn(IInterpolator ease, float amount) {
			return Interpolators.easeIn(this, ease, amount);
		}
		
		public ExtensibleInterpolator easeOut(IInterpolator ease, float amount) {
			return Interpolators.easeOut(this, ease, amount);
		}
		
		public ExtensibleInterpolator easeInOut(IInterpolator ease, float amount) {
			return Interpolators.easeInOut(this, ease, amount);
		}
		
		public ExtensibleInterpolator easeIn(float amount) {
			return Interpolators.easeIn(this, amount);
		}
		
		public ExtensibleInterpolator easeOut(float amount) {
			return Interpolators.easeOut(this, amount);
		}
		
		public ExtensibleInterpolator easeInOut(float amount) {
			return Interpolators.easeInOut(this, amount);
		}
		
		/**
		 * <p>Extends the interpolator by prepending the function with constant 0 at the start and constant 1 at the end.</p>
		 * <p>{@code prepend} and @{code append} are the 'weights' of each section. The middle section always has weight 1.</p>
		 * <p>Ex: {@code interpolator.extend(1, 1)} will be constant 0 for the first third of [0,1] then the interpolator is
		 * applied during the next third, and finally it will be constant 1 for the last third.</p>
		 */
		public ExtensibleInterpolator extend(float prepend, float append) {
			return Interpolators.extend(this, prepend, append);
		}
	}
}
