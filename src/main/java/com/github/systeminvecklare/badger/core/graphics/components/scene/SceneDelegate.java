package com.github.systeminvecklare.badger.core.graphics.components.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayerVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.util.PoolableIterable;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IKeyPressEvent;


public class SceneDelegate implements ISceneDelegate {
	private Scene wrapper;
	
	private Map<String, ILayer> layerNames = new HashMap<String, ILayer>();
	private IPool<List<ILayer>> layersInitPool;
	private List<ILayer> layers;
	private boolean fullyDisposed = false;

	private IPool<List<IKeyPressListener>> keyPressListenersInitPool;
	private List<IKeyPressListener> keyPressListeners;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SceneDelegate(Scene wrapper) {
		this.wrapper = wrapper;
		this.layersInitPool = (IPool<List<ILayer>>) (IPool) FlashyEngine.get().getPoolManager().getPool(ArrayList.class);
		this.layers = layersInitPool.obtain();
		
		this.keyPressListenersInitPool = (IPool<List<IKeyPressListener>>) (IPool) FlashyEngine.get().getPoolManager().getPool(ArrayList.class);
		this.keyPressListeners = keyPressListenersInitPool.obtain();
		this.layers.clear();
	}
	
	@Override
	public ILayer getLayer(String name) {
		return layerNames.get(name);
	}

	@Override
	public ILayer addLayer(String name, ILayer layer) {
		if(layerNames.containsKey(name))
		{
			throw new IllegalArgumentException("Duplicate layer name in scene.");
		}
		this.layers.add(layer);
		this.layerNames.put(name, layer);
		layer.setScene(getWrapper());
		return layer;
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
		drawCycle.setShader(getWrapper().getShader());
		for(ILayer layer : this.layers)
		{
			layer.draw(drawCycle);
		}
	}

	@Override
	public void think(ITic tic) {
		PoolableIterable<ILayer> layersLoop = PoolableIterable.obtain(ILayer.class);
		try
		{
			for(ILayer layer : layersLoop.setToCopy(this.layers))
			{
				layer.think(tic);
			}
		}
		finally
		{
			layersLoop.free();
		}
	}

	@Override
	public void init() {
		PoolableIterable<ILayer> layersLoop = PoolableIterable.obtain(ILayer.class);
		try
		{
			for(ILayer layer : layersLoop.setToCopy(this.layers))
			{
				layer.init();
			}
		}
		finally
		{
			layersLoop.free();
		}
	}

	@Override
	public void dispose() {
		if(!fullyDisposed)
		{
			PoolableIterable<ILayer> layersLoop = PoolableIterable.obtain(ILayer.class);
			try
			{
				for(ILayer layer : layersLoop.setToCopy(this.layers))
				{
					layer.dispose();
				}
			}
			finally
			{
				layersLoop.free();
			}
			layerNames.clear();
			layerNames = null;
			layers.clear();
			layersInitPool.free(layers);
			layers = null;
			layersInitPool = null;
			
			keyPressListeners.clear();
			keyPressListenersInitPool.free(keyPressListeners);
			keyPressListeners = null;
			keyPressListenersInitPool = null;
			
			fullyDisposed = true;
		}
	}
	

	@Override
	public void addKeyPressListener(IKeyPressListener listener) {
		this.keyPressListeners.add(listener);
	}
	
	@Override
	public void onKeyPress(IKeyPressEvent event) {
		for(IKeyPressListener listener : this.keyPressListeners)
		{
			listener.onKeyPress(event);
		}
		// TODO add method removeKeyPressListener(IKeyPressListener listener)
	}
	
	@Override
	public void visitLayers(ILayerVisitor visitor) {
		PoolableIterable<ILayer> layerLoop = PoolableIterable.obtain(ILayer.class);
		try
		{
			for(ILayer layer : layerLoop.setToCopy(layers))
			{
				visitor.visit(layer);
			}
		}
		finally
		{
			layerLoop.free();
		}
	}
	
	@Override
	public IShader getShader() {
		return null;
	}

	@Override
	public Scene getWrapper() {
		return wrapper;
	}
}
