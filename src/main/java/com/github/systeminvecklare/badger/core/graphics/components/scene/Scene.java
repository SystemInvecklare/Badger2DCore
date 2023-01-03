package com.github.systeminvecklare.badger.core.graphics.components.scene;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyTypedListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayerVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IKeyPressEvent;


public class Scene implements IScene {
	protected ISceneDelegate delegate;

	private void setDelegate(ISceneDelegate sceneDelegate) {
		if(sceneDelegate instanceof Scene)
		{
			throw new IllegalArgumentException();
		}
		this.delegate = sceneDelegate;
	}
	
	public Scene() {
		setDelegate(FlashyEngine.get().newSceneDelegate(this));
	}
	
	@Override
	public void draw(IDrawCycle drawCycle) {
		delegate.draw(drawCycle);
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
	public ILayer addLayer(String name, ILayer layer) {
		return delegate.addLayer(name, layer);
	}

	@Override
	public ILayer getLayer(String name) {
		return delegate.getLayer(name);
	}
	
	@Override
	public void visitLayers(ILayerVisitor visitor) {
		delegate.visitLayers(visitor);
	}
	
	@Override
	public void onKeyPress(IKeyPressEvent event) {
		delegate.onKeyPress(event);
	}
	
	@Override
	public void addKeyPressListener(IKeyPressListener listener) {
		delegate.addKeyPressListener(listener);
	}
	
	@Override
	public void removeKeyPressListener(IKeyPressListener listener) {
		delegate.removeKeyPressListener(listener);
	}
	
	@Override
	public void addKeyTypedListener(IKeyTypedListener listener) {
		delegate.addKeyTypedListener(listener);
	}
	
	@Override
	public void removeKeyTypedListener(IKeyTypedListener listener) {
		delegate.removeKeyTypedListener(listener);
	}
	
	@Override
	public IShader getShader() {
		return delegate.getShader();
	}

	@Override
	public void onKeyTyped(char c) {
		delegate.onKeyTyped(c);
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
	public boolean isDisposed() {
		return delegate.isDisposed();
	}
	
	@Override
	public boolean isInitialized() {
		return delegate.isInitialized();
	}
}
