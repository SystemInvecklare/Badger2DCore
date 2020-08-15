package com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior;

import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;

public class LifeCycleManagingBehavior implements IBehavior {
	private ILifecycleOwner[] lifeCycleOwners;
	
	public LifeCycleManagingBehavior(ILifecycleOwner ... managed) {
		this.lifeCycleOwners = managed;
	}

	@Override
	public void onClick(IClickEvent clickEvent) {
	}

	@Override
	public void think(ITic tic) {
	}

	@Override
	public void init() {
		if(lifeCycleOwners != null)
		{
			for(ILifecycleOwner lifecycleOwner : lifeCycleOwners)
			{
				lifecycleOwner.init();
			}
		}
	}

	@Override
	public void dispose() {
		if(lifeCycleOwners != null)
		{
			for(ILifecycleOwner lifecycleOwner : lifeCycleOwners)
			{
				lifecycleOwner.dispose();
			}
			lifeCycleOwners = null;
		}
	}

	@Override
	public void onBind(IMovieClip bounded) {
	}

	@Override
	public void onUnbind(IMovieClip unbounded) {
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
