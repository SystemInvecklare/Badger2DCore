package com.github.systeminvecklare.badger.core.hover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ITransformDependentLayerVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IPositionalMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.ITransformDependentMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.Transform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public class HoverCollector implements ITransformDependentLayerVisitor, ITransformDependentMovieClipVisitor, IPositionalMovieClipVisitor {
	private final Position hoverPos = new Position(null); //TODO give lifecycle and get from pool
	private final Transform transform = new Transform(null); //TODO give lifecycle and get from pool
	private final List<IHoverable> newHovers = new ArrayList<IHoverable>();
	private List<IHoverable> hovered = new ArrayList<IHoverable>();
	private List<IHoverable> lastHovered = new ArrayList<IHoverable>();
	
	@Override
	public void visit(ILayer layer) {
		if(mayHoverChildren(layer)) {
			layer.visitChildrenMovieClips(this);
		}
	}
	
	private boolean mayHoverChildren(Object object) {
		if(object instanceof IHoverMasking) {
			return ((IHoverMasking) object).mayHoverChildren(hoverPos);
		}
		return true;
	}

	@Override
	public void visit(IMovieClip movieClip) {
		if(movieClip instanceof IHoverable) {
			IPool<Position> positionPool = FlashyEngine.get().getPoolManager().getPool(Position.class);
			ITransform tempTrans = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
			try {
				Position localPos = positionPool.obtain().setTo(hoverPos);
				try {
					tempTrans.setTo(transform).invert().transform(localPos);
					
					if(movieClip.hitTest(localPos)) {
						IHoverable hoverable = (IHoverable) movieClip;
						hovered.add(hoverable);
					}
				} finally {
					localPos.free();
				}
			} finally {
				tempTrans.free();
			}
		}
		if(mayHoverChildren(movieClip)) {
			movieClip.visitChildrenMovieClips(this);
		}
	}

	@Override
	public ITransform transform() {
		return transform;
	}
	
	@Override
	public IReadablePosition getGlobalPosition() {
		return hoverPos;
	}

	public void beginHover(float hoverX, float hoverY) {
		this.hoverPos.setTo(hoverX, hoverY);
		this.transform.setToIdentity();
		
		List<IHoverable> swap = this.lastHovered;
		this.lastHovered = this.hovered;
		this.hovered = swap;
	}

	public void endHover() {
		Collections.reverse(hovered);
		Iterator<IHoverable> hoveredIterator = hovered.iterator();
		boolean hoverConsumed = false;
		while(hoveredIterator.hasNext()) {
			IHoverable hoverable = hoveredIterator.next();
			if(hoverConsumed) {
				hoveredIterator.remove();
			} else if(hoverable.consumesHover()) {
				hoverConsumed = true;
			}
		}
		
		if(!newHovers.isEmpty()) {
			newHovers.clear();
		}
		newHovers.addAll(hovered);
		newHovers.removeAll(lastHovered);
		
		List<IHoverable> unHovered = lastHovered;
		//NOTE: lastHovered is dead in this scope. Don't use after this line (except clear at end)
		unHovered.removeAll(hovered);
		
		for(IHoverable hovered : newHovers) {
			hovered.onHoverChanged(true, hoverPos);
		}
		
		for(IHoverable hovered : unHovered) {
			hovered.onHoverChanged(false, hoverPos);
		}
		
		this.lastHovered.clear();
		this.newHovers.clear();
	}
}
