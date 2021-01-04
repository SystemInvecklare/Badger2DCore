package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDragEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IReleaseEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IPointerStateEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.math.Vector;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public class DragBehavior extends Behavior {
	private Vector positionOffset;
	private Vector tentativeOffset;
	private Position dragStart;
	private Position lastMeaningfulMousePos;
	private boolean dragging = false;
	private CancellableReleaseEventListener currentReleaseEventListener = null;
	private CancellableDragEventListener currentDragEventListener = null;
	
	private boolean disposed = false;
	
	@Override
	public void onClick(IClickEvent clickEvent) {
		if(!dragging && !clickEvent.isConsumed() && isValidClick(clickEvent)) {
			clickEvent.consume();
			dragging = true;
			
			onStartDrag(clickEvent.getPosition());
			
			convertPosition(clickEvent.getPosition(), dragStart); // Global pos right?
			lastMeaningfulMousePos.setTo(clickEvent.getPosition());
			
			
			
			cancelDragAndReleaseListeners();
			currentDragEventListener = new CancellableDragEventListener() {
				@Override
				protected void onDragNotCancelled(IPointerStateEvent e) {
					if(disposed) {
						return;
					}
					
					onDragged(e.getPosition(), true);
					
					lastMeaningfulMousePos.setTo(e.getPosition());
				}
			};
			currentReleaseEventListener = new CancellableReleaseEventListener() {
				@Override
				protected void onReleaseNotCancelled(IPointerStateEvent e) {
					if(disposed) {
						return;
					}
					lastMeaningfulMousePos.setTo(e.getPosition());
					
					forceRelease();
				}
			};
			clickEvent.addDragListener(currentDragEventListener);
			clickEvent.addReleaseListener(currentReleaseEventListener);
		}
	}
	
	private void cancelDragAndReleaseListeners() {
		if(currentDragEventListener != null) {
			currentDragEventListener.cancel();
			currentDragEventListener = null;
		}
		if(currentReleaseEventListener != null) {
			currentReleaseEventListener.cancel();
			currentReleaseEventListener = null;
		}
	}

	public final void forceRelease() {
		if(disposed) {
			return;
		}
		cancelDragAndReleaseListeners();
		
		if(dragging) {
			updateTentativeOffset();
			dragging = false;
			positionOffset.add(tentativeOffset);
			tentativeOffset.setToOrigin();
			
			onStopDrag(lastMeaningfulMousePos);
		}
	}
	
	protected void onStartDrag(IReadablePosition globalPosition) {
	}
	
	protected void onStopDrag(IReadablePosition globalPosition) {
	}
	
	protected void onDragged(IReadablePosition globalPosition, boolean mouseMoved) {
	}
	
	public Vector harvestPositionOffset(Vector result) {
		if(disposed) {
			return result;
		}
		if(result != null) {
			result.add(positionOffset);
		}
		positionOffset.setToOrigin();
		if(dragging) {
			if(result != null) {
				updateTentativeOffset();
				result.add(tentativeOffset);
			}
			tentativeOffset.setToOrigin();
			dragStart.setTo(lastMeaningfulMousePos);
		}
		return result;
	}

	protected final void updateTentativeOffset() {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			Position currentPos = ep.obtain(Position.class);
			convertPosition(lastMeaningfulMousePos, currentPos);
			dragStart.vectorTo(currentPos, tentativeOffset);
		} finally {
			ep.freeAllAndSelf();
		}
	}

	@Override
	public void think(ITic tic) {
		if(dragging) {
			updateTentativeOffset();
			onDragged(lastMeaningfulMousePos, false);
		}
	}

	protected boolean isValidClick(IClickEvent clickEvent) {
		return true;
	}

	@Override
	public void init() {
		IPool<Vector> vectorPool = FlashyEngine.get().getPoolManager().getPool(Vector.class);
		IPool<Position> positionPool = FlashyEngine.get().getPoolManager().getPool(Position.class);
		positionOffset = vectorPool.obtain().setToOrigin();
		tentativeOffset = vectorPool.obtain().setToOrigin();
		dragStart = positionPool.obtain().setToOrigin();
		lastMeaningfulMousePos = positionPool.obtain().setToOrigin();
	}
	
	@Override
	public void dispose() {
		cancelDragAndReleaseListeners();
		disposed = true;
		positionOffset.free();
		positionOffset = null;
		tentativeOffset.free();
		tentativeOffset = null;
		dragStart.free();
		dragStart = null;
		lastMeaningfulMousePos.free();
		lastMeaningfulMousePos = null;
	}
	
	protected void convertPosition(IReadablePosition globalPosition, Position result) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			IReadablePosition localPos = getBound().getParent().toLocalTransform(ep.obtain(ITransform.class).setToIdentity().setPosition(globalPosition)).getPosition();
			result.setTo(localPos);
		} finally {
			ep.freeAllAndSelf();
		}
	}
	
	@Override
	public ITransform getTransform(IReadableTransform transform, ITransform result) {
		result = result.setTo(transform).addToPosition(positionOffset);
		if(dragging) {
			return result.addToPosition(tentativeOffset);
		}
		return result;
	}
	
	private abstract static class CancellableReleaseEventListener implements IReleaseEventListener {
		private boolean cancelled = false;

		@Override
		public final void onRelease(IPointerStateEvent e) {
			if(cancelled) {
				return;
			}
			onReleaseNotCancelled(e);
		}

		protected abstract void onReleaseNotCancelled(IPointerStateEvent e);
		
		public void cancel() {
			cancelled = true;
		}
	}
	
	private abstract static class CancellableDragEventListener implements IDragEventListener {
		private boolean cancelled = false;
		
		@Override
		public final void onDrag(IPointerStateEvent e) {
			if(cancelled) {
				return;
			}
			onDragNotCancelled(e);
		}

		protected abstract void onDragNotCancelled(IPointerStateEvent e);
		
		public void cancel() {
			cancelled = true;
		}
	}
}
