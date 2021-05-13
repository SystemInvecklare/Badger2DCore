package com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;

public class Behavior implements IBehavior {
	private IMovieClip bound;

	@Override
	public void onClick(IClickEvent clickEvent, boolean consumedBeforeBehaviors) {
	}

	@Override
	public void think(ITic tic) {
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onBind(IMovieClip bounded) {
		bound = bounded;
	}
	
	@Override
	public void onUnbind(IMovieClip unbounded) {
		if(bound == unbounded)
		{
			bound = null;
		}
	}
	
	protected IMovieClip getBound()
	{
		return bound;
	}

	@Override
	public ITransform getTransform(IReadableTransform transform, ITransform result) {
		return result.setTo(transform);
	}

	@Override
	public ITransform setTransform(ITransform transform) {
		return transform;
	}

	@Override
	public IShader getShader(IShader shader) {
		return shader;
	}
}
