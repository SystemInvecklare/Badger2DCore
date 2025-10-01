package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;

public class HitBeam implements IMovieClipLayer {
	private final IReadablePosition start;
	private final IReadablePosition end;
	private final float thickness;
	
	public HitBeam(IReadablePosition start, IReadablePosition end, float thickness) {
		this.start = start;
		this.end = end;
		this.thickness = thickness;
	}

	public void draw(IDrawCycle drawCycle) {}

	@Override
	public boolean hitTest(IReadablePosition p) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			return GeometryUtil.isInBeam(p.getX(), p.getY(), getStart(ep), getEnd(ep), getThickness(), ep);
		} finally {
			ep.freeAllAndSelf();
		}
	}

	public float getThickness() {
		return thickness;
	}

	public IReadablePosition getStart(EasyPooler ep) {
		return start;
	}
	
	public IReadablePosition getEnd(EasyPooler ep) {
		return end;
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}
}
