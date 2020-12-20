package com.github.systeminvecklare.badger.core.graphics.components.scene;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawable;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayerVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;

public interface IScene extends IDrawable, IThinker, ILifecycleOwner, IKeyPressListener {
	public ILayer addLayer(String name, ILayer layer);
	public ILayer getLayer(String name);
	public void visitLayers(ILayerVisitor visitor);
	public void addKeyPressListener(IKeyPressListener listener);
	public void removeKeyPressListener(IKeyPressListener listener);
	
	public IShader getShader();
}
