package com.github.systeminvecklare.badger.core.graphics.components.scene;

import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;

public interface ISceneChild {
	public IScene getScene();
	public ILayer getLayer();
	
	boolean isInitialized();
	boolean isDisposed();
}
