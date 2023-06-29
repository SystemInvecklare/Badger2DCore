package com.github.systeminvecklare.badger.core.graphics.components.core;

import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;

public interface IForegroundable {
	void onForegrounded(IScene backgroundedScene, IScene foregroundedScene);
	void onBackgrounded(IScene backgroundedScene, IScene foregroundedScene);
}
