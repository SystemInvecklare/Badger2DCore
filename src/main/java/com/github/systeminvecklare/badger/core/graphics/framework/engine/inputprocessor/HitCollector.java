package com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ITransformDependentLayerVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.ITransformDependentMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClipVisitorWithObject;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.util.QuickArray;

public class HitCollector implements ITransformDependentLayerVisitor, ITransformDependentMovieClipVisitor, IMovieClipCollector<IReadablePosition> {
	private QuickArray<IMovieClip> movieClips;
	private Position clickPos;
	private ITransform currentTransform;
	
	public HitCollector(boolean initNow) {
		if(initNow)
		{
			this.init();
		}
	}
	
	public HitCollector() {
		this(false);
	}

	@Override
	public ITransform transform() {
		return currentTransform;
	}
	
	@Override
	public void visit(ILayer layer) {
		layer.visitChildrenMovieClips(this);
	}
	
	@Override
	public void visit(IMovieClip movieClip) {
		IPool<Position> positionPool = FlashyEngine.get().getPoolManager().getPool(Position.class);
		ITransform tempTrans = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
		try
		{
			Position localPos = positionPool.obtain().setTo(clickPos);
			try
			{
				tempTrans.setTo(currentTransform).invert().transform(localPos);
				
				boolean hit = movieClip.hitTest(localPos);
				if(hit)
				{
					//(the order will be reversed in the end)
					movieClips.add(movieClip);
					movieClip.visitChildrenMovieClips(this);
				}
			}
			finally
			{
				localPos.free();
			}
		}
		finally
		{
			tempTrans.free();
		}
	}
	
	@Override
	public void traverseAndClearCollected(IMovieClipVisitor visitor)
	{
		int size = movieClips.getSize();
		for(int i = size-1; i >= 0; --i)
		{
			visitor.visit(movieClips.get(i));
		}
		movieClips.clear();
	}
	
	@Override
	public void doClick(IClickEvent clickEvent)
	{
		traverseAndClearCollected(new MovieClipVisitorWithObject<IClickEvent>(clickEvent) {
			@Override
			protected void visitWith(IMovieClip movieClip, IClickEvent event) {
				if(!event.isConsumed())
				{
					movieClip.onClick(event);
				}
			}
		});
	}

	@Override
	public HitCollector reset(IReadablePosition position) {
		movieClips.clear();
		this.clickPos.setTo(position);
		this.currentTransform.setToIdentity();
		return this;
	}

	public QuickArray<IMovieClip> getMovieClips() {
		return movieClips;
	}

	@Override
	public void init() {
		this.movieClips = new QuickArray<IMovieClip>();
		this.clickPos = FlashyEngine.get().getPoolManager().getPool(Position.class).obtain();
		this.currentTransform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
	}

	@Override
	public void dispose() {
		if(this.movieClips != null)
		{
			this.movieClips.clear();
			this.movieClips = null;
		}
		if(this.clickPos != null)
		{
			this.clickPos.free();
			this.clickPos = null;
		}
		if(this.currentTransform != null)
		{
			this.currentTransform.free();
			this.currentTransform = null;
		}
	}
}
