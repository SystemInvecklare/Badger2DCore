package com.github.systeminvecklare.badger.core.font;

import java.util.regex.Pattern;

public abstract class Embellishment<C> {
	public final Pattern pattern;

	public Embellishment(Pattern pattern) {
		this.pattern = pattern;
	}
	
	public abstract ITextEmbellishment create(IFlashyFont<C> baseFont, C tint, float capHeight, GwtSafeMatcher matcher);

	protected void preload() {
	}
}
