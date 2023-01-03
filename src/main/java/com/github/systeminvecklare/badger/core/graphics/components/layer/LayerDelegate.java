package com.github.systeminvecklare.badger.core.graphics.components.layer;

import java.util.ArrayList;
import java.util.Collection;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipContainer;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.ITransformDependentMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.util.LifecycleManagerComponent;
import com.github.systeminvecklare.badger.core.graphics.components.util.PoolableIterable;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;


public class LayerDelegate implements ILayerDelegate {
	private final LifecycleManagerComponent managerComponent = new LifecycleManagerComponent();
	private Layer wrapper;
	
	private IScene scene;
	private IPool<Collection<IMovieClip>> movieClipsInitPool;
	private Collection<IMovieClip> movieClips;
	
	private ITransform transform;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LayerDelegate(Layer wrapper) {
		this.wrapper = wrapper;
		this.movieClipsInitPool = (IPool<Collection<IMovieClip>>) (IPool) FlashyEngine.get().getPoolManager().getPool(ArrayList.class);
		this.movieClips = movieClipsInitPool.obtain();
		this.movieClips.clear();
	}

	@Override
	public void setScene(IScene parentScene) {
		this.scene = parentScene;
	}

	@Override
	public IScene getScene() {
		return scene;
	}

	@Override
	public ILayer getLayer() {
		return getWrapper();
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			ITransform drawCycleTransform = drawCycle.getTransform();
			drawCycle.setShader(getWrapper().resolveShader());
			
			if(!movieClips.isEmpty()) {
				ITransform original = ep.obtain(ITransform.class).setTo(drawCycleTransform);
				drawCycleTransform.mult(getWrapper().getTransform());
				ITransform drawTransformCopy = ep.obtain(ITransform.class).setTo(drawCycleTransform);
				
				for(IMovieClip movieClip : movieClips)
				{
					drawCycleTransform.setTo(drawTransformCopy);
					movieClip.draw(drawCycle);
				}
				
				drawCycleTransform.setTo(original);
			}
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		if(movieClips.isEmpty()) {
			return false;
		}
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			ITransform inverted = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain().setTo(getWrapper().getTransform()).invert();
			Position transformedPoint = ep.obtain(Position.class).setTo(p);
			inverted.transform(transformedPoint);
			
			Position transformedPointOriginal = ep.obtain(Position.class).setTo(transformedPoint);
			
			for(IMovieClip clip : movieClips)
			{
				transformedPoint.setTo(transformedPointOriginal);
				if(clip.hitTest(transformedPoint))
				{
					return true;
				}
			}
			return false;
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}

	@Override
	public void think(ITic tic) {
		if(!movieClips.isEmpty()) {
			PoolableIterable<IMovieClip> movieClipsLoop = PoolableIterable.obtain(IMovieClip.class);
			try
			{
				for(IMovieClip movieClip : movieClipsLoop.setToCopy(movieClips))
				{
					movieClip.think(tic);
				}
			}
			finally
			{
				movieClipsLoop.free();
			}
		}
	}

	@Override
	public void init() {
		if(!getWrapper().isInitialized() && !getWrapper().isDisposed()) {
			transform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain().setToIdentity();
			if(!movieClips.isEmpty()) {
				PoolableIterable<IMovieClip> movieClipsLoop = PoolableIterable.obtain(IMovieClip.class);
				try
				{
					for(IMovieClip movieClip : movieClipsLoop.setToCopy(movieClips))
					{
						movieClip.init();
					}
				}
				finally
				{
					movieClipsLoop.free();
				}
			}
			managerComponent.init();
		}
	}

	@Override
	public void dispose() {
		managerComponent.dispose();
		if(!movieClips.isEmpty()) {
			PoolableIterable<IMovieClip> movieClipsLoop = PoolableIterable.obtain(IMovieClip.class);
			try
			{
				for(IMovieClip movieClip : movieClipsLoop.setToCopy(movieClips))
				{
					movieClip.dispose();
				}
			}
			finally
			{
				movieClipsLoop.free();
			}
		}
		transform.free();
		transform = null;
		movieClips.clear();
		movieClipsInitPool.free(movieClips);
		movieClips = null;
		movieClipsInitPool = null;
		
		scene = null;
	}
	
	@Override
	public void addManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		managerComponent.addManagedLifecycle(lifecycleOwner);
	}
	
	@Override
	public void removeManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		managerComponent.removeManagedLifecycle(lifecycleOwner);
	}
	
	@Override
	public boolean isInitialized() {
		return managerComponent.isInitialized();
	}
	
	@Override
	public boolean isDisposed() {
		return managerComponent.isDisposed();
	}

	@Override
	public Layer getWrapper() {
		return wrapper;
	}

	@Override
	public void addMovieClip(IMovieClip child) {
		IMovieClipContainer oldParent = child.getParent();
		if(oldParent != null)
		{
			oldParent.removeMovieClip(child);
		}
		child.setParent(getWrapper());
		movieClips.add(child);
	}

	@Override
	public void removeMovieClip(IMovieClip child) {
		if(getWrapper().equals(child.getParent()))
		{
			child.setParent(null);
		}
		movieClips.remove(child);
	}

	@Override
	public void visitChildrenMovieClips(IMovieClipVisitor visitor) {
		if(movieClips.isEmpty()) {
			return;
		}
		PoolableIterable<IMovieClip> movieClipsLoop = PoolableIterable.obtain(IMovieClip.class);
		try
		{
			if(visitor instanceof ITransformDependentMovieClipVisitor)
			{
				ITransformDependentMovieClipVisitor transVisitor = ((ITransformDependentMovieClipVisitor) visitor);
				ITransform original = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
				try
				{
					original.setTo(transVisitor.transform());
					try
					{
						transVisitor.transform().mult(getWrapper().getTransform());
						for(IMovieClip movieClip : movieClipsLoop.setToCopy(movieClips))
						{
							visitor.visit(movieClip);
						}
					}
					finally
					{
						transVisitor.transform().setTo(original);
					}
				}
				finally
				{
					original.free();
				}
			}
			else
			{
				for(IMovieClip movieClip : movieClipsLoop.setToCopy(movieClips))
				{
					visitor.visit(movieClip);
				}
			}
		}
		finally
		{
			movieClipsLoop.free();
		}
	}

	@Override
	public IReadableTransform getTransform() {
		return transform;
	}
	
	@Override
	public ITransform toGlobalTransform(IReadableTransform transform, ITransform result) {
		return result.setTo(getWrapper().getTransform()).mult(transform);
	}
	
	@Override
	public ITransform toGlobalTransform(ITransform result) {
		return result.multLeft(getWrapper().getTransform());
	}
	
	@Override
	public ITransform toLocalTransform(IReadableTransform transform, ITransform result) {
		return result.setTo(getWrapper().getTransform()).invert().mult(transform);
	}
	
	@Override
	public ITransform toLocalTransform(ITransform result) {
		return result.invert().mult(getWrapper().getTransform()).invert(); //TODO optimize?
	}
	
	@Override
	public IShader getShader() {
		return null;
	}
	
	@Override
	public IShader resolveShader() {
		IShader shader = getWrapper().getShader();
		return shader != null ? shader : getWrapper().getScene().getShader();
	}
}
