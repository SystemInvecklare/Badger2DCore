package com.github.systeminvecklare.badger.core.graphics.framework.engine;

import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;

public interface ISceneManager {
	public void changeScene(IScene newScene);
	public float getWidth();
	public float getHeight();
	public void sendToTrashCan(IScene sceneToBeDisposed);
	public void emptyTrashCan();
	public float getStep();
	public void skipQueuedUpdates();
	public IApplicationContext getApplicationContext();
}
