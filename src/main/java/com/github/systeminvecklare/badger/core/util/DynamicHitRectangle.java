package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.widget.IRectangle;

public class DynamicHitRectangle implements IMovieClipLayer {
	private final IRectangle rectangle;

	public DynamicHitRectangle(IRectangle rectangle) {
		this.rectangle = rectangle;
	}

	public void draw(IDrawCycle drawCycle) {}

	@Override
	public boolean hitTest(IReadablePosition p) {
		return GeometryUtil.isInRectangle(p.getX(), p.getY(), rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}
}
