package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDragEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IReleaseEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipContainer;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IPointerStateEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.math.Vector;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;

public class DragBehavior extends Behavior {
	private boolean beingDragged = false;
	private Position lastPosition;
	private Vector currentOffset;
	private boolean disposed = false;
	private IMovieClipContainer parentOfBound;
	
	@Override
	public void init() {
		super.init();
		IPoolManager pm = FlashyEngine.get().getPoolManager();
		this.lastPosition = pm.getPool(Position.class).obtain().setToOrigin();
		this.currentOffset = pm.getPool(Vector.class).obtain().setToOrigin();
		applyConstraints(currentOffset);
		this.parentOfBound = getBound().getParent();
	}
	
	@Override
	public void dispose() {
		this.lastPosition.free();
		this.lastPosition = null;
		this.currentOffset.free();
		this.currentOffset = null;
		super.dispose();
		disposed = true;
	}
	
	@Override
	public void onClick(IClickEvent clickEvent) {
		if(disposed) {
			return;
		}
		if(!beingDragged) {
			if(isCorrectButton(clickEvent) && !clickEvent.isConsumed()) {
				clickEvent.consume();
				beingDragged = true;
				
				onPosition(clickEvent.getPosition());
				
				clickEvent.addDragListener(new IDragEventListener() {
					@Override
					public void onDrag(IPointerStateEvent e) {
						onPosition(e.getPosition());
					}
				});
				clickEvent.addReleaseListener(new IReleaseEventListener() {
					@Override
					public void onRelease(IPointerStateEvent e) {
						onPosition(e.getPosition());
						beingDragged = false;
					}
				});
			}
		}
	}
	
	private void onPosition(IReadablePosition readablePosition) {
		if(!disposed) {
			this.lastPosition.setTo(readablePosition);
			EasyPooler ep = EasyPooler.obtainFresh();
			try {
				updateCurrentPosition(getPositionInParent(readablePosition, ep));
			} finally {
				ep.freeAllAndSelf();
			}
		}
	}
	
	private Position getPositionInParent(IReadablePosition position, EasyPooler ep) {
		return getPositionInParent(position, ep.obtain(Position.class));
	}
	
	private Position getPositionInParent(IReadablePosition position, Position result) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			return result.setTo(parentOfBound.toLocalTransform(ep.obtain(ITransform.class).setToIdentity().setPosition(position)).getPosition());
		} finally {
			ep.freeAllAndSelf();
		}
	}
	
	private void updateCurrentPosition(IReadablePosition position) {
		currentOffset.setTo(position);
		applyConstraints(currentOffset);
	}
	
	@Override
	public void think(ITic tic) {
		if(beingDragged && !disposed) {
			onPosition(lastPosition);
		}
	}
	
	protected void applyConstraints(Vector currentOffset) {
	}

	protected boolean isCorrectButton(IClickEvent clickEvent) {
		return true;
	}
	
	@Override
	public ITransform getTransform(IReadableTransform transform, ITransform result) {
		if(disposed) {
			return null;
		} else {
			return result.setTo(transform).addToPosition(currentOffset);
		}
	}
}
