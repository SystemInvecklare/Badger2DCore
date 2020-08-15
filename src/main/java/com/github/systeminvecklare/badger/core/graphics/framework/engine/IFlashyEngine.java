package com.github.systeminvecklare.badger.core.graphics.framework.engine;

import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayerDelegate;
import com.github.systeminvecklare.badger.core.graphics.components.layer.Layer;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipDelegate;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.scene.ISceneDelegate;
import com.github.systeminvecklare.badger.core.graphics.components.scene.Scene;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;
import com.github.systeminvecklare.badger.core.util.ISmartList;


public interface IFlashyEngine {
	public IPoolManager getPoolManager();
	
	public ISceneDelegate newSceneDelegate(Scene wrapper);
	public ILayerDelegate newLayerDelegate(Layer wrapper);
	public IMovieClipDelegate newMovieClipDelegate(MovieClip wrapper);
	
	public <T> ISmartList<T> newSmartList();
}
