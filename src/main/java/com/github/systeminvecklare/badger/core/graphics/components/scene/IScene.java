package com.github.systeminvecklare.badger.core.graphics.components.scene;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawable;
import com.github.systeminvecklare.badger.core.graphics.components.core.IForegroundable;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyTypedListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleManager;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IPrioritizedKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayerVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;

public interface IScene extends IDrawable, IThinker, ILifecycleOwner, ILifecycleManager, IKeyPressListener, IKeyTypedListener, IForegroundable {
	ILayer addLayer(String name, ILayer layer);
	ILayer getLayer(String name);
	void visitLayers(ILayerVisitor visitor);
	void addKeyPressListener(IKeyPressListener listener);
	void addKeyPressListener(IPrioritizedKeyPressListener listener);
	void removeKeyPressListener(IKeyPressListener listener);
	void addKeyTypedListener(IKeyTypedListener listener);
	void removeKeyTypedListener(IKeyTypedListener listener);
	
	IShader getShader();
	
	boolean isInitialized();
	boolean isDisposed();
}
