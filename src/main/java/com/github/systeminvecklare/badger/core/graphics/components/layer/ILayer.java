package com.github.systeminvecklare.badger.core.graphics.components.layer;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawable;
import com.github.systeminvecklare.badger.core.graphics.components.core.IHittable;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipContainer;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;

public interface ILayer extends IMovieClipContainer,IDrawable,IHittable,IThinker,ILifecycleOwner {
	public IReadableTransform getTransform();
	
	public void setScene(IScene parentScene);
}
