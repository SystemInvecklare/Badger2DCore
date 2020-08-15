package com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor;

import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;

public interface IInputHandler {
	public boolean registerKeyDown(int keycode);
	public boolean registerKeyUp(int keycode);
	public boolean registerPointerDown(int screenX, int screenY, int pointer, int button);
	public boolean registerPointerUp(int screenX, int screenY, int pointer, int button);
	public boolean registerPointerDragged(int screenX, int screenY, int pointer);
	public void handleInputs(IScene scene);
}
