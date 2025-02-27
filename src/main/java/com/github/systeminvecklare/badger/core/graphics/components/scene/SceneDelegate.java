package com.github.systeminvecklare.badger.core.graphics.components.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyTypedListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IPrioritizedKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayerVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.util.LifecycleManagerComponent;
import com.github.systeminvecklare.badger.core.graphics.components.util.PoolableIterable;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IKeyPressEvent;


public class SceneDelegate implements ISceneDelegate {
	private final LifecycleManagerComponent managerComponent = new LifecycleManagerComponent();
	private Scene wrapper;
	
	private Map<String, ILayer> layerNames = new HashMap<String, ILayer>();
	private IPool<List<ILayer>> layersInitPool;
	private List<ILayer> layers;
	private boolean fullyDisposed = false;

	private DoubleList<KeyPressListenerHolder> keyPressListeners;
	private DoubleList<IKeyTypedListener> keyTypedListeners;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SceneDelegate(Scene wrapper) {
		this.wrapper = wrapper;
		this.layersInitPool = (IPool<List<ILayer>>) (IPool) FlashyEngine.get().getPoolManager().getPool(ArrayList.class);
		this.layers = layersInitPool.obtain();
		
		this.keyPressListeners = new DoubleList<KeyPressListenerHolder>();
		this.keyTypedListeners = new DoubleList<IKeyTypedListener>();
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
		if(!layers.isEmpty()) {
			for(ILayer layer : this.layers)
			{
				layer.draw(drawCycle);
			}
		}
	}

	@Override
	public void think(ITic tic) {
		if(!layers.isEmpty()) {
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
	}

	@Override
	public void init() {
		if(!getWrapper().isInitialized() && !getWrapper().isDisposed()) {
			if(!layers.isEmpty()) {
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
			managerComponent.init();
		}
	}

	@Override
	public void dispose() {
		managerComponent.dispose();
		if(!fullyDisposed)
		{
			if(!layers.isEmpty()) {
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
			}
			layerNames.clear();
			layerNames = null;
			layers.clear();
			layersInitPool.free(layers);
			layers = null;
			layersInitPool = null;
			
			keyPressListeners.free();
			keyPressListeners = null;
			
			keyTypedListeners.free();
			keyTypedListeners = null;
			
			fullyDisposed = true;
		}
	}
	

	@Override
	public void addKeyPressListener(IKeyPressListener listener) {
		if(listener instanceof IPrioritizedKeyPressListener) {
			this.addKeyPressListener((IPrioritizedKeyPressListener) listener);
		} else {
			this.keyPressListeners.add(new KeyPressListenerHolder(listener, new LowestPriorityKeyPressListener(listener)));
		}
	}
	
	@Override
	public void addKeyPressListener(IPrioritizedKeyPressListener listener) {
		this.keyPressListeners.add(new KeyPressListenerHolder(listener, listener));
	}
	
	@Override
	public void removeKeyPressListener(IKeyPressListener listener) {
		if(!isDisposed()) {
			Iterator<KeyPressListenerHolder> iterator = this.keyPressListeners.main.iterator();
			while(iterator.hasNext()) {
				if(listener.equals(iterator.next().originalObject)) {
					iterator.remove();
					return;
				}
			}
		}
	}
	
	@Override
	public void onKeyPress(IKeyPressEvent event) {
		if(keyPressListeners.prepareUtilList()) {
			Collections.sort(keyPressListeners.utilList, KeyPressListenerHolder.SORTER);
			for(KeyPressListenerHolder holder : keyPressListeners.utilList) {
				holder.originalObject.onKeyPress(event);
			}
			keyPressListeners.resetUtilList();
		}
	}
	

	@Override
	public void addKeyTypedListener(IKeyTypedListener listener) {
		this.keyTypedListeners.add(listener);
	}

	@Override
	public void removeKeyTypedListener(IKeyTypedListener listener) {
		this.keyTypedListeners.remove(listener);
	}

	@Override
	public void onKeyTyped(char c) {
		if(keyTypedListeners.prepareUtilList()) {
			for(IKeyTypedListener listener : keyTypedListeners.utilList) {
				listener.onKeyTyped(c);
			}
			keyTypedListeners.resetUtilList();
		}
	}
	
	@Override
	public void visitLayers(ILayerVisitor visitor) {
		if(layers.isEmpty()) {
			return;
		}
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
	
	@Override
	public void addManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		this.managerComponent.addManagedLifecycle(lifecycleOwner);
	}
	
	@Override
	public void removeManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		this.managerComponent.removeManagedLifecycle(lifecycleOwner);
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
	public void onForegrounded(IScene backgroundedScene, IScene foregroundedScene) {
	}
	
	@Override
	public void onBackgrounded(IScene backgroundedScene, IScene foregroundedScene) {
	}
	
	private static class DoubleList<T> {
		private boolean disposed = false;
		@SuppressWarnings("rawtypes")
		private final IPool<ArrayList> pool;
		private final ArrayList<T> main;
		private final ArrayList<T> utilList;
		
		@SuppressWarnings("unchecked")
		public DoubleList() {
			this.pool = FlashyEngine.get().getPoolManager().getPool(ArrayList.class);
			main = pool.obtain();
			utilList = pool.obtain();
		}
		
		public void remove(T listener) {
			if(!disposed) {
				main.remove(listener);
			}
		}

		public void add(T object) {
			if(!disposed) {
				main.add(object);
			}
		}

		public void free() {
			if(!disposed) {
				disposed = true;
				main.clear();
				utilList.clear();
				pool.free(main);
				pool.free(utilList);
			}
		}
		
		public boolean prepareUtilList() {
			if(disposed) {
				return false;
			}
			if(!main.isEmpty()) {
				utilList.clear();
				utilList.addAll(main);
				return true;
			} else {
				return false;
			}
		}
		
		public void resetUtilList() {
			if(!disposed) {
				utilList.clear();
			}
		}
	}
	
	private static class KeyPressListenerHolder {
		public static final Comparator<? super KeyPressListenerHolder> SORTER = new Comparator<KeyPressListenerHolder>() {
			@Override
			public int compare(KeyPressListenerHolder o1, KeyPressListenerHolder o2) {
				return -Float.compare(o1.prioritized.getKeyConsumerPriority(), o2.prioritized.getKeyConsumerPriority());
			}
		};
		
		private final IKeyPressListener originalObject;
		private final IPrioritizedKeyPressListener prioritized;
		
		public KeyPressListenerHolder(IKeyPressListener object, IPrioritizedKeyPressListener prioritized) {
			this.originalObject = object;
			this.prioritized = prioritized;
		}
	}
	
	private static class LowestPriorityKeyPressListener implements IPrioritizedKeyPressListener {
		private final IKeyPressListener listener;
		
		public LowestPriorityKeyPressListener(IKeyPressListener listener) {
			this.listener = listener;
		}

		@Override
		public void onKeyPress(IKeyPressEvent event) {
			listener.onKeyPress(event);
		}

		@Override
		public float getKeyConsumerPriority() {
			return Float.MIN_VALUE;
		}
	}
}
