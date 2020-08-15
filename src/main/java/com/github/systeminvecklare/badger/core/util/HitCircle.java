package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public class HitCircle implements IMovieClipLayer {
	private double cx;
	private double cy;
	private double rad;
	
	public HitCircle(double x, double y, double radius) {
		this.cx = x;
		this.cy = y;
		this.rad = radius;
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		return GeometryUtil.isInCircle(p.getX(), p.getY(), cx, cy, rad);
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
	}
}
