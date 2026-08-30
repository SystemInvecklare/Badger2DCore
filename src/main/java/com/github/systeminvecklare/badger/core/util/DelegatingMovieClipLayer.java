package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public class DelegatingMovieClipLayer implements IMovieClipLayer {
	private boolean initialized = false;
	private IMovieClipLayer child = null;
	
	public DelegatingMovieClipLayer() {
	}

	public DelegatingMovieClipLayer(IMovieClipLayer child) {
		this.child = child;
	}

	public DelegatingMovieClipLayer set(IMovieClipLayer child, boolean updateLifecycle) {
		if(initialized && updateLifecycle && this.child != null) {
			this.child.dispose();
		}
		this.child = child;
		if(initialized && updateLifecycle && this.child != null) {
			this.child.init();
		}
		return this;
	}
	
	public DelegatingMovieClipLayer set(IMovieClipLayer child) {
		return set(child, true);
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
		if(child != null) {
			child.draw(drawCycle);
		}
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		if(child != null) {
			child.hitTest(p);
		}
		return false;
	}

	@Override
	public void init() {
		if(child != null) {
			child.init();
		}
		initialized = true;
	}

	@Override
	public void dispose() {
		if(child != null) {
			child.dispose();
			child = null;
		}
	}
}