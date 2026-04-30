package com.github.systeminvecklare.badger.core.font;

import com.github.systeminvecklare.badger.core.pattern.IPatternMatch;
import com.github.systeminvecklare.badger.core.pattern.IPatternMatcher;

public abstract class Embellishment<C> {
	public final IPatternMatcher pattern;

	public Embellishment(IPatternMatcher pattern) {
		this.pattern = pattern;
	}
	
	public abstract ITextEmbellishment create(IFlashyFont<C> baseFont, C tint, float capHeight, IPatternMatch matcher);

	protected void preload() {
	}
}
