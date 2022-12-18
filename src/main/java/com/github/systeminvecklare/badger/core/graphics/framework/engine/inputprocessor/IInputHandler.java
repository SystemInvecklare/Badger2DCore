package com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor;

import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;

public interface IInputHandler {
	boolean registerKeyDown(int keycode);
	boolean registerKeyUp(int keycode);
	boolean registerKeyTyped(char c);
	boolean registerPointerDown(int screenX, int screenY, int pointer, int button);
	boolean registerPointerUp(int screenX, int screenY, int pointer, int button);
	boolean registerPointerDragged(int screenX, int screenY, int pointer);
	void handleInputs(IScene scene);
}
