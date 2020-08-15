package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public class HitRectangle implements IMovieClipLayer {
	private float x;
	private float y;
	private float width;
	private float height;

	public HitRectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		if(this.height < 0)
		{
			this.y += this.height;
			this.height = -this.height;
		}
		if(this.width < 0)
		{
			this.x += this.width;
			this.width = -this.width;
		}
	}
	
	public void draw(IDrawCycle drawCycle) {};

	@Override
	public boolean hitTest(IReadablePosition p) {
		return GeometryUtil.isInRectangle(p.getX(), p.getY(), x, y, width, height);
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}
}
