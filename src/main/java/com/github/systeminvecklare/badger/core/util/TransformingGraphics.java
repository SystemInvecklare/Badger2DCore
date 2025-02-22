package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.NonInvertibleMatrixException;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;

public class TransformingGraphics implements IMovieClipLayer {
	private final IMovieClipLayer wrapped;
	private final boolean takeOwnership;
	private ITransform transform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain().setToIdentity();
	
	public TransformingGraphics(IMovieClipLayer wrapped) {
		this(wrapped, true);
	}
	
	public TransformingGraphics(IMovieClipLayer wrapped, boolean takeOwnership) {
		this.wrapped = wrapped;
		this.takeOwnership = takeOwnership;
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			ITransform drawCycleTransform = drawCycle.getTransform();

			ITransform original = drawCycleTransform.copy(ep);
			ITransform temp = ep.obtain(ITransform.class);
			drawCycleTransform.mult(getTransform(temp));

			wrapped.draw(drawCycle);

			drawCycleTransform.setTo(original);
		} finally {
			ep.freeAllAndSelf();
		}
	}
	
	protected ITransform getTransform(ITransform result) {
		return result.setTo(transform);
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			Position position = p.copy(ep);
			getTransform(ep.obtain(ITransform.class)).invert().transform(position);
			return wrapped.hitTest(position);
		} catch (NonInvertibleMatrixException e) {
			return false;
		} finally {
			ep.freeAllAndSelf();
		}
	}

	@Override
	public void init() {
		if(takeOwnership) {
			wrapped.init();
		}
	}

	@Override
	public void dispose() {
		if(takeOwnership) {
			wrapped.dispose();
		}
		if(transform != null) {
			transform.free();
			transform = null;
		}
	}

	public TransformingGraphics setRotation(final float theta) {
		if(transform != null) {
			transform.setRotation(theta);
		}
		return this;
	}
	
	public TransformingGraphics addToRotation(final float dtheta) {
		if(transform != null) {
			transform.addToRotation(dtheta);
		}
		return this;
	}
	
	public TransformingGraphics addPosition(final float posX, final float posY) {
		if(transform != null) {
			transform.addToPosition(posX, posY);
		}
		return this;
	}
	
	public TransformingGraphics setPosition(final float posX, final float posY) {
		if(transform != null) {
			transform.setPosition(posX, posY);
		}
		return this;
	}
	
	public TransformingGraphics setScale(final float scale) {
		if(transform != null) {
			transform.setScale(scale, scale);
		}
		return this;
	}
	
	public TransformingGraphics setScale(final float scaleX, final float scaleY) {
		if(transform != null) {
			transform.setScale(scaleX, scaleY);
		}
		return this;
	}
	
	public TransformingGraphics scaleScale(float scale) {
		return scaleScale(scale, scale);
	}
	
	public TransformingGraphics scaleScale(final float scaleX, final float scaleY) {
		if(transform != null) {
			transform.multiplyScale(scaleX, scaleY);
		}
		return this;
	}
	
	public TransformingGraphics setTransform(final IReadableTransform readableTransform) {
		if(transform != null) {
			transform.setTo(readableTransform);
		}
		return this;
	}
}
