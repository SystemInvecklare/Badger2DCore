package com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior;

import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;

public interface IBehavior extends IThinker, ILifecycleOwner {
	public void onBind(IMovieClip bounded);
	public void onUnbind(IMovieClip unbounded);
	public ITransform getTransform(IReadableTransform transform, ITransform result);
	public ITransform setTransform(ITransform transform);
	public IShader getShader(IShader shader);
	public void onClick(IClickEvent clickEvent, boolean consumedBeforeBehaviors);
}
