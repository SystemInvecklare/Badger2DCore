package com.github.systeminvecklare.badger.core.graphics.components.layer;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.NonInvertibleMatrixException;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;

public class Layer implements ILayer {
	protected ILayerDelegate delegate;
	
	private void setDelegate(ILayerDelegate layerDelegate) {
		if(layerDelegate instanceof Layer)
		{
			throw new IllegalArgumentException();
		}
		this.delegate = layerDelegate;
	}

	public Layer() {
		setDelegate(FlashyEngine.get().newLayerDelegate(this));
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
	public void draw(IDrawCycle drawCycle) {
		delegate.draw(drawCycle);
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		return delegate.hitTest(p);
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
	public void setScene(IScene parentScene) {
		delegate.setScene(parentScene);
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
	public IReadableTransform getTransform() {
		return delegate.getTransform();
	}
	
	@Override
	public ITransform toGlobalTransform(IReadableTransform transform, ITransform result) {
		return delegate.toGlobalTransform(transform, result);
	}
	
	@Override
	public ITransform toGlobalTransform(ITransform result) {
		return delegate.toGlobalTransform(result);
	}
	
	@Override
	public ITransform toLocalTransform(IReadableTransform transform, ITransform result) throws NonInvertibleMatrixException {
		return delegate.toLocalTransform(transform, result);
	}
	
	@Override
	public ITransform toLocalTransform(ITransform result) throws NonInvertibleMatrixException {
		return delegate.toLocalTransform(result);
	}
	
	@Override
	public Position toGlobalPosition(IReadablePosition position, Position result) {
		return delegate.toGlobalPosition(position, result);
	}
	
	@Override
	public Position toGlobalPosition(Position result) {
		return delegate.toGlobalPosition(result);
	}
	
	@Override
	public Position toLocalPosition(IReadablePosition position, Position result) throws NonInvertibleMatrixException {
		return delegate.toLocalPosition(position, result);
	}
	
	@Override
	public Position toLocalPosition(Position result) throws NonInvertibleMatrixException {
		return delegate.toLocalPosition(result);
	}
	
	@Override
	public IShader getShader() {
		return delegate.getShader();
	}
	
	@Override
	public IShader resolveShader() {
		return delegate.resolveShader();
	}
	
	@Override
	public void addManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		delegate.addManagedLifecycle(lifecycleOwner);
	}
	
	@Override
	public void removeManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		delegate.removeManagedLifecycle(lifecycleOwner);
	}
	
	@Override
	public boolean isInitialized() {
		return delegate.isInitialized();
	}
	
	@Override
	public boolean isDisposed() {
		return delegate.isDisposed();
	}
}
