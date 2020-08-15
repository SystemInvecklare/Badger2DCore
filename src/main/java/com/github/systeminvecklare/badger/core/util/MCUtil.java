package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipContainer;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;

public class MCUtil {
	public static <T extends IMovieClip> Manipulator<T> manipulate(T movieclip)
	{
		return new Manipulator<T>(movieclip);
	}
	
	
	public static class Manipulator<T extends IMovieClip> {
		private T movieClip;
		
		public Manipulator(T movieClip) {
			this.movieClip = movieClip;
		}

		public Manipulator<T> setRotation(final float theta)
		{
			movieClip.addBehavior(new Behavior()
			{
				private IMovieClip bound;
				@Override
				public void onBind(IMovieClip bounded) {
					this.bound = bounded;
				}
				@Override
				public void init() {
					super.init();
					EasyPooler ep = EasyPooler.obtainFresh();
					try
					{
//						bound.setTransform(bound.getTransform(ep.obtain(ITransform.class)).setRotation(theta));
						bound.setTransformBypassBehaviors(ep.obtain(ITransform.class).setTo(bound.getTransformBypassBehaviors()).setRotation(theta));
					}
					finally
					{
						ep.freeAllAndSelf();
					}
					bound.removeBehavior(this);
					dispose();
				}
			});
			return this;
		}
		
		public Manipulator<T> addPosition(final float posX, final float posY)
		{
			movieClip.addBehavior(new Behavior()
			{
				private IMovieClip bound;
				@Override
				public void onBind(IMovieClip bounded) {
					this.bound = bounded;
				}
				@Override
				public void init() {
					super.init();
					EasyPooler ep = EasyPooler.obtainFresh();
					try
					{
						bound.setTransformBypassBehaviors(ep.obtain(ITransform.class).setTo(bound.getTransformBypassBehaviors()).addToPosition(posX, posY));
					}
					finally
					{
						ep.freeAllAndSelf();
					}
					bound.removeBehavior(this);
					dispose();
				}
			});
			return this;
		}
		
		public Manipulator<T> setPosition(final float posX, final float posY)
		{
			movieClip.addBehavior(new Behavior()
			{
				private IMovieClip bound;
				@Override
				public void onBind(IMovieClip bounded) {
					this.bound = bounded;
				}
				@Override
				public void init() {
					super.init();
					EasyPooler ep = EasyPooler.obtainFresh();
					try
					{
						bound.setTransformBypassBehaviors(ep.obtain(ITransform.class).setTo(bound.getTransformBypassBehaviors()).setPosition(posX, posY));
					}
					finally
					{
						ep.freeAllAndSelf();
					}
					bound.removeBehavior(this);
					dispose();
				}
			});
			return this;
		}
		
		public Manipulator<T> setScale(final float scale) {
			movieClip.addBehavior(new Behavior()
			{
				private IMovieClip bound;
				@Override
				public void onBind(IMovieClip bounded) {
					this.bound = bounded;
				}
				@Override
				public void init() {
					super.init();
					EasyPooler ep = EasyPooler.obtainFresh();
					try
					{
//						bound.setTransform(bound.getTransform(ep.obtain(ITransform.class)).setScale(scale, scale));
						bound.setTransformBypassBehaviors(ep.obtain(ITransform.class).setTo(bound.getTransformBypassBehaviors()).setScale(scale, scale));
					}
					finally
					{
						ep.freeAllAndSelf();
					}
					bound.removeBehavior(this);
					dispose();
				}
			});
			return this;
		}
		
		public Manipulator<T> setScale(final float scaleX,final float scaleY) {
			movieClip.addBehavior(new Behavior()
			{
				private IMovieClip bound;
				@Override
				public void onBind(IMovieClip bounded) {
					this.bound = bounded;
				}
				@Override
				public void init() {
					super.init();
					EasyPooler ep = EasyPooler.obtainFresh();
					try
					{
						bound.setTransformBypassBehaviors(ep.obtain(ITransform.class).setTo(bound.getTransformBypassBehaviors()).setScale(scaleX, scaleY));
					}
					finally
					{
						ep.freeAllAndSelf();
					}
					bound.removeBehavior(this);
					dispose();
				}
			});
			return this;
		}
		
		public Manipulator<T> scaleScale(float scale) {
			return scaleScale(scale, scale);
		}
		
		public Manipulator<T> scaleScale(final float scaleX,final float scaleY) {
			movieClip.addBehavior(new Behavior()
			{
				private IMovieClip bound;
				@Override
				public void onBind(IMovieClip bounded) {
					this.bound = bounded;
				}
				@Override
				public void init() {
					super.init();
					EasyPooler ep = EasyPooler.obtainFresh();
					try
					{
						bound.setTransformBypassBehaviors(ep.obtain(ITransform.class).setTo(bound.getTransformBypassBehaviors()).multiplyScale(scaleX, scaleY));
					}
					finally
					{
						ep.freeAllAndSelf();
					}
					bound.removeBehavior(this);
					dispose();
				}
			});
			return this;
		}
		
		
		public T end()
		{
			return movieClip;
		}
	}


	public static void attach(IMovieClip movieClip, IMovieClipContainer container, IReadablePosition atPosition) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			ITransform newTrans = container.toLocalTransform(ep.obtain(ITransform.class).setToIdentity().addToPosition(atPosition.getX(), atPosition.getY()), ep.obtain(ITransform.class));
			MCUtil.manipulate(movieClip).setPosition(newTrans.getPosition().getX(), newTrans.getPosition().getY()).end();
			container.addMovieClip(movieClip);
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}


	public static void transfer(IMovieClip movieClip, IMovieClipContainer newParent) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			IMovieClipContainer oldParent = movieClip.getParent();
			ITransform newTransform = newParent.toLocalTransform(oldParent.toGlobalTransform(movieClip.getTransform(ep.obtain(ITransform.class))));
			newParent.addMovieClip(movieClip);
			movieClip.setTransform(newTransform);
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}


	public static void delete(IMovieClip movieClip) {
		IMovieClipContainer parent = movieClip.getParent();
		if(parent != null)
		{
			parent.removeMovieClip(movieClip);
		}
		movieClip.dispose();
	}
}
