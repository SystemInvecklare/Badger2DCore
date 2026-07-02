package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
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
import com.github.systeminvecklare.badger.core.graphics.components.transform.NonInvertibleMatrixException;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;

public class MinimalMovieClip implements IMovieClip {
	private IMovieClipContainer parent;

	@Override
	public void think(ITic tic) {
	}
	
	@Override
	public boolean isVisible() {
		return false;
	}
	
	@Override
	public void draw(IDrawCycle drawCycle) {
	}

	@Override
	public void addMovieClip(IMovieClip child) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeMovieClip(IMovieClip child) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visitChildrenMovieClips(IMovieClipVisitor visitor) {
	}

	@Override
	public ITransform toGlobalTransform(IReadableTransform transform, ITransform result) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ITransform toGlobalTransform(ITransform result) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ITransform toLocalTransform(IReadableTransform transform, ITransform result)
			throws NonInvertibleMatrixException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ITransform toLocalTransform(ITransform result) throws NonInvertibleMatrixException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Position toGlobalPosition(IReadablePosition position, Position result) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Position toGlobalPosition(Position result) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Position toLocalPosition(IReadablePosition position, Position result) throws NonInvertibleMatrixException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Position toLocalPosition(Position result) throws NonInvertibleMatrixException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IShader getShader() {
		return null;
	}

	@Override
	public IShader resolveShader() {
		return null;
	}

	@Override
	public IScene getScene() {
		return getParent().getScene();
	}

	@Override
	public ILayer getLayer() {
		return getParent().getLayer();
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public boolean isDisposed() {
		return false;
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		return false;
	}

	@Override
	public void onClick(IClickEvent clickEvent) {
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void addManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMovieClipContainer getParent() {
		return parent;
	}

	@Override
	public void setParent(IMovieClipContainer parent) {
		this.parent = parent;
	}

	@Override
	public ITransform getTransform(ITransform result) {
		return result.setToIdentity();
	}

	@Override
	public IReadableTransform getTransformBypassBehaviors() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTransform(IReadableTransform transform) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTransformBypassBehaviors(IReadableTransform transform) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void modifyTransform(ITransformOperation operation, boolean byPassBehaviorsOnGet,
			boolean byPassBehaviorsOnSet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void drawWithoutTransform(IDrawCycle drawCycle) {
	}

	@Override
	public void addBehavior(IBehavior behavior) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeBehavior(IBehavior behavior) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visitBehaviors(IBehaviorVisitor visitor) {
	}

	@Override
	public void addGraphics(IMovieClipLayer movieClipLayer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeGraphics(IMovieClipLayer movieClipLayer) {
		throw new UnsupportedOperationException();
	}
}
