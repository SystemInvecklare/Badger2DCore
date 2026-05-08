package com.github.systeminvecklare.badger.core.util;

/**
 * <code>ideal(t) = decay^t</code>
 * <br>
 * <code>d/dt ideal(t) = d/dt decay^t = d/dt exp(ln(decay^t)) = d/dt exp(t*ln(decay)) = ln(decay)*exp(t*ln(decay)) = ln(decay)*ideal(t)</code>
 * <br>
 * <br>
 * For small steps <code>step</code>, we have <code>ideal(t+step) = ideal(t) + step*(d/dt ideal)(t) = ideal(y) + step*ln(decay)*ideal(t) = (1 + step*ln(decay))*ideal(t)</code>
 * <br>
 * So we can define
 * <code>approx(t + step) = approx(t)*(1 + step*ln(decay))</code>
 * to be discreetly updated. (Where <code>approx(0) = ideal(0) = 1</code>)
 * <br>
 * <br>
 * For small <code>step</code>, a time <code>t</code> can be approximated as <code>t = n*step</code> some <code>n</code>.
 * <br>
 * <br>
 * <code>approx(t) = approx(n*step) = approx((n-1)*step)*(1 + step*ln(decay)) = ... = approx(0)*(1+step*ln(decay))^n = (1+step*ln(decay))^n = (1+step*ln(decay))^(t/step)</code>
 * <br>
 * <br>
 * Consider the limit of <code>(1+step*ln(decay))^(t/step)</code> as <code>step</code> approaches <code>0</code>.
 * <br>
 * Let <code>m = 1/step</code>. <code>m</code> approaches <code>inf</code> as <code>step</code> approaches <code>0</code>.
 * <br>
 * <code>(1+step*ln(decay))^(t/step) = (1+ln(decay)/m)^(t*m) = ((1+ln(decay)/m)^m)^t</code>. <code>(1+a/m)^m</code> is a standard limit that approaches <code>exp(a)</code> as <code>m</code> approaches <code>inf</code>.
 * <br>
 * So <code>((1+ln(decay)/m)^m)^t</code> approaches <code>(exp(ln(decay)))^t = decay^t = ideal(t)</code>.
 * <br>
 * <br>
 * Thus we have shown that <code>approx(t)</code> approaches <code>ideal(t)</code> as the step lengths <code>step</code> approaches <code>0</code>.
 * 
 * @author Mattias Selin
 */
public class ExponentialDecay {
	private final double lnDecayPerSecond;
	
	private ExponentialDecay(double decay, double perTime) {
		this(Math.log(decay)/perTime);
	}
	
	private ExponentialDecay(double lnDecayPerSecond) {
		this.lnDecayPerSecond = lnDecayPerSecond;
	}
	
	public float getScaleForStep(float step) {
		return Math.max(0, (float) (1 + step*lnDecayPerSecond));
	}
	
	public static ExponentialDecay perSecond(float decayPerSecond) {
		return new ExponentialDecay((double) decayPerSecond, 1.0);
	}
	
	public static ExponentialDecay perSecond(double decayPerSecond) {
		return new ExponentialDecay(decayPerSecond, 1.0);
	}
	
	public static ExponentialDecay perTime(float decay, float perTime) {
		return new ExponentialDecay((double) decay, (double) perTime);
	}
	
	public static ExponentialDecay perTime(double decay, double perTime) {
		return new ExponentialDecay(decay, perTime);
	}
	
	public static ExponentialDecay perFrames(double decay, int frames, int fps) {
		// ln(decay^(1/time)) = ln(decay^(1/(frames/fps))) = ln(decay^(fps/frames)) = fps*ln(decay)/frames
		return new ExponentialDecay((fps*Math.log(decay))/frames);
	}
	
	public static ExponentialDecay perFrames(float decay, int frames, int fps) {
		return perFrames((double) decay, frames, fps);
	}
}
