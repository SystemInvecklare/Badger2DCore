package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.IBehavior;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.IBehaviorVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransformOperation;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public class MovieClip implements IMovieClip {
	protected IMovieClipDelegate delegate;
	
	private void setDelegate(IMovieClipDelegate movieClipDelegate) {
		if(movieClipDelegate instanceof MovieClip)
		{
			throw new IllegalArgumentException();
		}
		this.delegate = movieClipDelegate;
	}
	
	public MovieClip() {
		this.setDelegate(FlashyEngine.get().newMovieClipDelegate(this));
	}

	@Override
	public void addMovieClip(IMovieClip child) {
		delegate.addMovieClip(child);
	}

	@Override
	public void removeMovieClip(IMovieClip child) {
		delegate.removeMovieClip(child);
	}

	@Override
	public void visitChildrenMovieClips(IMovieClipVisitor visitor) {
		delegate.visitChildrenMovieClips(visitor);
	}

	@Override
	public ITransform toGlobalTransform(IReadableTransform transform,
			ITransform result) {
		return delegate.toGlobalTransform(transform, result);
	}
	
	@Override
	public ITransform toGlobalTransform(ITransform result) {
		return delegate.toGlobalTransform(result);
	}

	@Override
	public ITransform toLocalTransform(IReadableTransform transform,
			ITransform result) {
		return delegate.toLocalTransform(transform, result);
	}
	
	@Override
	public ITransform toLocalTransform(ITransform result) {
		return delegate.toLocalTransform(result);
	}

	@Override
	public IScene getScene() {
		return delegate.getScene();
	}

	@Override
	public ILayer getLayer() {
		return delegate.getLayer();
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		return delegate.hitTest(p);
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
		delegate.draw(drawCycle);
	}
	
	@Override
	public void drawWithoutTransform(IDrawCycle drawCycle) {
		delegate.drawWithoutTransform(drawCycle);
	}

	@Override
	public void onClick(IClickEvent clickEvent) {
		delegate.onClick(clickEvent);
	}

	@Override
	public void think(ITic tic) {
		delegate.think(tic);
	}

	@Override
	public void init() {
		delegate.init();
	}

	@Override
	public void dispose() {
		delegate.dispose();
	}

	@Override
	public IMovieClipContainer getParent() {
		return delegate.getParent();
	}

	@Override
	public void setParent(IMovieClipContainer parent) {
		delegate.setParent(parent);
	}
	
	@Override
	public ITransform getTransform(ITransform result) {
		return delegate.getTransform(result);
	}
	
	@Override
	public IReadableTransform getTransformBypassBehaviors() {
		return delegate.getTransformBypassBehaviors();
	}
	
	@Override
	public void setTransform(IReadableTransform transform) {
		delegate.setTransform(transform);
	}
	
	@Override
	public void setTransformBypassBehaviors(IReadableTransform transform) {
		delegate.setTransformBypassBehaviors(transform);
	}
	
	@Override
	public void modifyTransform(ITransformOperation operation, boolean byPassBehaviorsOnGet,
			boolean byPassBehaviorsOnSet) {
		delegate.modifyTransform(operation, byPassBehaviorsOnGet, byPassBehaviorsOnSet);
	}
	
	@Override
	public void addBehavior(IBehavior behavior) {
		delegate.addBehavior(behavior);
	}
	
	@Override
	public void removeBehavior(IBehavior behavior) {
		delegate.removeBehavior(behavior);
	}
	
	@Override
	public void visitBehaviors(IBehaviorVisitor visitor) {
		delegate.visitBehaviors(visitor);
	}
	
	@Override
	public void addGraphics(IMovieClipLayer movieClipLayer) {
		delegate.addGraphics(movieClipLayer);
	}
	
	@Override
	public void removeGraphics(IMovieClipLayer movieClipLayer) {
		delegate.removeGraphics(movieClipLayer);
	}
	
	@Override
	public IShader getShader() {
		return delegate.getShader();
	}
	
	@Override
	public IShader resolveShader() {
		return delegate.resolveShader();
	}
}
