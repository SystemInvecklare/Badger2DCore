package com.github.systeminvecklare.badger.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipContainer;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.IBehavior;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.IBehaviorVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.SimplePool;

public class MCUtil {
	private static final IPool<InitialTransformManipulatorBehaviorVisitor> itmbvPool = new SimplePool<MCUtil.InitialTransformManipulatorBehaviorVisitor>(2, 10) {
		@Override
		public InitialTransformManipulatorBehaviorVisitor newObject() {
			return new InitialTransformManipulatorBehaviorVisitor();
		}
		
		public void free(InitialTransformManipulatorBehaviorVisitor poolable) {
			poolable.clear();
			super.free(poolable);
		}
	};
	
	public static <T extends IMovieClip> Manipulator<T> manipulate(T movieclip)
	{
		return new Manipulator<T>(movieclip);
	}
	
	
	public static class Manipulator<T extends IMovieClip> {
		private T movieClip;
		
		public Manipulator(T movieClip) {
			this.movieClip = movieClip;
		}

		public Manipulator<T> setRotation(final float theta) {
			movieClip.addBehavior(new InitialTransformManipulatorBehavior() {
				@Override
				protected ITransform manipulateTransform(ITransform transform) {
					return transform.setRotation(theta);
				}
			});
			return this;
		}
		
		public Manipulator<T> addToRotation(final float dtheta) {
			movieClip.addBehavior(new InitialTransformManipulatorBehavior() {
				@Override
				protected ITransform manipulateTransform(ITransform transform) {
					return transform.addToRotation(dtheta);
				}
			});
			return this;
		}
		
		public Manipulator<T> addPosition(final float posX, final float posY) {
			movieClip.addBehavior(new InitialTransformManipulatorBehavior() {
				@Override
				protected ITransform manipulateTransform(ITransform transform) {
					return transform.addToPosition(posX, posY);
				}
			});
			return this;
		}
		
		public Manipulator<T> setPosition(final float posX, final float posY) {
			movieClip.addBehavior(new InitialTransformManipulatorBehavior() {
				@Override
				protected ITransform manipulateTransform(ITransform transform) {
					return transform.setPosition(posX, posY);
				}
			});
			return this;
		}
		
		public Manipulator<T> setScale(final float scale) {
			movieClip.addBehavior(new InitialTransformManipulatorBehavior() {
				@Override
				protected ITransform manipulateTransform(ITransform transform) {
					return transform.setScale(scale, scale);
				}
			});
			return this;
		}
		
		public Manipulator<T> setScale(final float scaleX, final float scaleY) {
			movieClip.addBehavior(new InitialTransformManipulatorBehavior() {
				@Override
				protected ITransform manipulateTransform(ITransform transform) {
					return transform.setScale(scaleX, scaleY);
				}
			});
			return this;
		}
		
		public Manipulator<T> scaleScale(float scale) {
			return scaleScale(scale, scale);
		}
		
		public Manipulator<T> scaleScale(final float scaleX, final float scaleY) {
			movieClip.addBehavior(new InitialTransformManipulatorBehavior() {
				@Override
				protected ITransform manipulateTransform(ITransform transform) {
					return transform.multiplyScale(scaleX, scaleY);
				}
			});
			return this;
		}
		
		public T end() {
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
	
	public static void moveUp(final IMovieClip movieClip) {
		move(movieClip, 1);
	}
	
	public static void moveDown(final IMovieClip movieClip) {
		move(movieClip, -1);
	}
	
	public static void moveToTop(final IMovieClip movieClip) {
		IMovieClipContainer parent = movieClip.getParent();
		if(parent != null) {
			parent.removeMovieClip(movieClip);
			parent.addMovieClip(movieClip);
		}
	}
	
	public static void moveToBottom(final IMovieClip movieClip) {
		IMovieClipContainer parent = movieClip.getParent();
		if(parent != null) {
			rearrangeChildren(parent, new Consumer<List<IMovieClip>>() {
				@Override
				public void accept(List<IMovieClip> sibling) {
					sibling.remove(movieClip);
					sibling.add(0, movieClip);
				}
			});
		}
	}
	
	private static void move(final IMovieClip movieClip, final int dindex) {
		IMovieClipContainer parent = movieClip.getParent();
		if(parent != null) {
			rearrangeChildren(parent, new Consumer<List<IMovieClip>>() {
				@Override
				public void accept(List<IMovieClip> siblings) {
					int currentIndex = siblings.indexOf(movieClip);
					int targetIndex = currentIndex+dindex;
					if(targetIndex < siblings.size() && targetIndex >= 0) {
						IMovieClip moved = siblings.remove(targetIndex);
						siblings.add(targetIndex, movieClip);
						siblings.remove(currentIndex);
						siblings.add(currentIndex, moved);
					}
				}
			});
		}
	}
	
	private static void rearrangeChildren(IMovieClipContainer parent, Consumer<List<IMovieClip>> rearranger) {
		final List<IMovieClip> children = new ArrayList<IMovieClip>();
		parent.visitChildrenMovieClips(new IMovieClipVisitor() {
			@Override
			public void visit(IMovieClip movieClip) {
				children.add(movieClip);
			}
		});
		for(IMovieClip child : children) {
			parent.removeMovieClip(child);
		}
		rearranger.accept(children);
		for(IMovieClip child : children) {
			parent.addMovieClip(child);
		}
	}
	
	public static ITransform getTransform(IMovieClip movieClip, EasyPooler ep) {
		return getTransform(movieClip, ep.obtain(ITransform.class));
	}
	
	public static ITransform getTransform(IMovieClip movieClip, final ITransform result) {
		if(movieClip.isInitialized()) {
			return movieClip.getTransform(result);
		} else {
			result.setToIdentity();
			InitialTransformManipulatorBehaviorVisitor behaviorVisitor = itmbvPool.obtain().reset(result);
			try {
				movieClip.visitBehaviors(behaviorVisitor);
			} finally {
				itmbvPool.free(behaviorVisitor);
			}
			return result;
		}
	}
	
	private static abstract class InitialTransformManipulatorBehavior extends Behavior {
		@Override
		public void init() {
			super.init();
			final IMovieClip bound = getBound();
			EasyPooler ep = EasyPooler.obtainFresh();
			try {
				bound.setTransformBypassBehaviors(manipulateTransform(ep.obtain(ITransform.class).setTo(bound.getTransformBypassBehaviors())));
			} finally {
				ep.freeAllAndSelf();
			}
			bound.removeBehavior(this);
			dispose();
		}
		
		protected abstract ITransform manipulateTransform(ITransform transform);
	}
	
	private static class InitialTransformManipulatorBehaviorVisitor implements IBehaviorVisitor {
		private ITransform result;
		
		public InitialTransformManipulatorBehaviorVisitor reset(ITransform result) {
			this.result = result;
			return this;
		}
		
		public void clear() {
			result = null;
		}

		@Override
		public void visit(IBehavior behavior) {
			if(behavior instanceof InitialTransformManipulatorBehavior) {
				ITransform potentiallyNewTransform = ((InitialTransformManipulatorBehavior) behavior).manipulateTransform(result);
				if(potentiallyNewTransform != result) {
					result.setTo(potentiallyNewTransform);
				}
			}
		}
	
	}
}
