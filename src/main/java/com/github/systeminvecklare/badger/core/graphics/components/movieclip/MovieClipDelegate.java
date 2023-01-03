package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawable;
import com.github.systeminvecklare.badger.core.graphics.components.core.IHittable;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.IBehavior;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.IBehaviorVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransformOperation;
import com.github.systeminvecklare.badger.core.graphics.components.util.LifecycleManagerComponent;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ILoopAction;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ISmartList;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.LoopAction;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ThinkAction;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public class MovieClipDelegate implements IMovieClipDelegate {
	private final LifecycleManagerComponent managerComponent = new LifecycleManagerComponent();
	private MovieClip wrapper;
	
	private IMovieClipContainer parent;
	
	private ISmartList<IMovieClipLayer> graphics;
	private ISmartList<IMovieClip> children;
	private ISmartList<IBehavior> behaviours;
	
	private ITransform transform;
	
	private boolean disposed = false;
	
	private final ThinkAction thinkAction = new ThinkAction(null);
	
	public MovieClipDelegate(MovieClip wrapper) {
		this.wrapper = wrapper;
		
		this.graphics = FlashyEngine.get().newSmartList();
		this.children = FlashyEngine.get().newSmartList();
		this.behaviours = FlashyEngine.get().newSmartList();
	}

	@Override
	public IMovieClipContainer getParent() {
		return parent;
	}

	@Override
	public void setParent(IMovieClipContainer parent) {
		this.parent = parent;
	}

	@Override
	public void addMovieClip(IMovieClip child) {
		IMovieClipContainer oldParent = child.getParent();
		if(oldParent != null)
		{
			oldParent.removeMovieClip(child);
		}
		child.setParent(getWrapper());
		children.addToBirthList(child);
	}

	@Override
	public void removeMovieClip(IMovieClip child) {
		if(getWrapper().equals(child.getParent()))
		{
			child.setParent(null);
		}
		children.addToDeathList(child);
	}

	
	@Override
	public void visitChildrenMovieClips(IMovieClipVisitor visitor) {
		if(children.isEmpty()) {
			return;
		}
		if(visitor instanceof ITransformDependentMovieClipVisitor)
		{
			ITransformDependentMovieClipVisitor transVisitor = ((ITransformDependentMovieClipVisitor) visitor);
			IPool<ITransform> transformPool = FlashyEngine.get().getPoolManager().getPool(ITransform.class);
			ITransform original = transformPool.obtain();
			ITransform temp = transformPool.obtain();
			try
			{
				original.setTo(transVisitor.transform());
				try
				{
					transVisitor.transform().mult(getWrapper().getTransform(temp));
					children.forEach(new VisitClipAction(visitor));
				}
				finally
				{
					transVisitor.transform().setTo(original);
				}
			}
			finally
			{
				original.free();
				temp.free();
			}
		}
		else
		{
			children.forEach(new VisitClipAction(visitor));
		}
	}
	
	@Override
	public IReadableTransform getTransformBypassBehaviors() {
		return transform;
	}
	
	@Override
	public void setTransformBypassBehaviors(IReadableTransform transform) {
		this.transform.setTo(transform);
	}
	
	@Override
	public ITransform getTransform(ITransform result) {
		if(!behaviours.isEmpty()) {
			IPool<ITransform> transformPool = FlashyEngine.get().getPoolManager().getPool(ITransform.class);
			
			ITransform mutableTrans =  transformPool.obtain().setTo(getWrapper().getTransformBypassBehaviors());
			ITransform loopResult = transformPool.obtain().setTo(mutableTrans);
			
			try
			{
				behaviours.forEach(new GetTransformBehaviorAction(mutableTrans, loopResult));
				return result.setTo(loopResult);
			}
			finally
			{
				mutableTrans.free();
				loopResult.free();
			}
		} else {
			return result.setTo(getWrapper().getTransformBypassBehaviors());
		}
	}
	
	@Override
	public void setTransform(IReadableTransform transform) {
		if(!behaviours.isEmpty()) {
			ITransform mutableTransform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
			try
			{
				mutableTransform.setTo(transform);
				behaviours.forEach(new SetTransformBehaviorAction(mutableTransform));
				getWrapper().setTransformBypassBehaviors(mutableTransform);
			}
			finally
			{
				mutableTransform.free();
			}
		} else {
			getWrapper().setTransformBypassBehaviors(transform);
		}
	}
	
	@Override
	public void modifyTransform(ITransformOperation operation, boolean byPassBehaviorsOnGet,
			boolean byPassBehaviorsOnSet) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			ITransform transform;
			if(byPassBehaviorsOnGet) {
				transform = getWrapper().getTransformBypassBehaviors().copy(ep);
			} else {
				transform = getWrapper().getTransform(ep.obtain(ITransform.class));
			}
			transform = operation.execute(transform);
			if(byPassBehaviorsOnSet) {
				getWrapper().setTransformBypassBehaviors(transform);
			} else {
				getWrapper().setTransform(transform);
			}
		} finally {
			ep.freeAllAndSelf();
		}
	}
	
	@Override
	public ITransform toGlobalTransform(IReadableTransform transform, ITransform result) {
		ITransform temp = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
		try
		{
			return getWrapper().getParent().toGlobalTransform(getWrapper().getTransform(temp).mult(transform), result);
		}
		finally
		{
			temp.free();
		}
	}
	
	@Override
	public ITransform toGlobalTransform(ITransform result) {
		ITransform temp = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
		try
		{
			return getWrapper().getParent().toGlobalTransform(getWrapper().getTransform(temp).mult(result), result);
		}
		finally
		{
			temp.free();
		}
	
	}

	@Override
	public ITransform toLocalTransform(IReadableTransform transform, ITransform result) {
		result = getWrapper().getParent().toLocalTransform(transform, result);
		ITransform parentL = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
		try
		{
			parentL.setTo(result);
			return getWrapper().getTransform(result).invert().mult(parentL);
		}
		finally
		{
			parentL.free();
		}
	}
	
	@Override
	public ITransform toLocalTransform(ITransform result) {
		result = getWrapper().getParent().toLocalTransform(result);
		ITransform parentL = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
		try
		{
			parentL.setTo(result);
			return getWrapper().getTransform(result).invert().mult(parentL);
		}
		finally
		{
			parentL.free();
		}
	}

	@Override
	public IScene getScene() {
		return getWrapper().getParent().getScene();
	}

	@Override
	public ILayer getLayer() {
		return getWrapper().getParent().getLayer();
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try
		{
			Position ptDst = ep.obtain(Position.class);
			Position ptDstCopy = ep.obtain(Position.class);
			ITransform trans = ep.obtain(ITransform.class);
			
			getWrapper().getTransform(trans).invert().transform(ptDst.setTo(p));
			
			HitTestAction hitTestAction = new HitTestAction();
			
			if(!graphics.isEmpty()) {
				hitTestAction.reset(ptDst, ptDstCopy);
				graphics.forEach(hitTestAction);
				if(hitTestAction.hit) {
					return true;
				}
			}
			if(!children.isEmpty()) {
				hitTestAction.reset(ptDst, ptDstCopy);
				children.forEach(hitTestAction);
				if(hitTestAction.hit) {
					return true;
				}
			}
			return false;
		}
		finally
		{
			ep.freeAllAndSelf();
		}
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
		if(!disposed)
		{
			IPool<ITransform> transformPool = FlashyEngine.get().getPoolManager().getPool(ITransform.class);
			
			ITransform drawCycleTransform = drawCycle.getTransform();
			drawCycle.setShader(getWrapper().resolveShader());
			
			ITransform original = transformPool.obtain().setTo(drawCycleTransform);
			try
			{
				ITransform temp = transformPool.obtain();
				try
				{
					drawCycleTransform.mult(getWrapper().getTransform(temp));
				}
				finally
				{
					temp.free();
				}
				
				getWrapper().drawWithoutTransform(drawCycle);
				
				drawCycleTransform.setTo(original);
			}
			finally
			{
				original.free();
			}
		}
	}
	
	@Override
	public void drawWithoutTransform(IDrawCycle drawCycle) {
		if(!disposed)
		{
			IPool<ITransform> transformPool = FlashyEngine.get().getPoolManager().getPool(ITransform.class);
			
			ITransform originalTransform = transformPool.obtain().setTo(drawCycle.getTransform());
			try
			{
				if(!graphics.isEmpty()) {
					graphics.forEach(new DrawAction(drawCycle, originalTransform));
				}
				if(!children.isEmpty()) {
					children.forEach(new DrawAction(drawCycle, originalTransform));
				}
			}
			finally
			{
				originalTransform.free();
			}
		}
	}

	@Override
	public void onClick(IClickEvent clickEvent) {
		if(!behaviours.isEmpty()) {
			boolean consumedBeforeBehaviors = clickEvent.isConsumed();
			behaviours.forEach(new BehaviorClickAction(clickEvent, consumedBeforeBehaviors));
		}
	}

	@Override
	public void think(ITic tic) {
		if(!disposed)
		{
			this.thinkAction.setTic(tic);
			if(!children.isEmpty()) {
				children.forEach(this.thinkAction);
			}
			
			if(!behaviours.isEmpty()) {
				behaviours.forEach(this.thinkAction);
			}
		}
	}

	@Override
	public void init() {
		if(!getWrapper().isInitialized() && !getWrapper().isDisposed()) {
			transform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain().setToIdentity();
			
			//Init movieClipLayers
			if(!graphics.isEmpty()) {
				graphics.forEach(LoopAction.INIT);
			}
			
			//Init children
			if(!children.isEmpty()) {
				children.forEach(LoopAction.INIT);
			}
			
			//Init behaviours
			if(!behaviours.isEmpty()) {
				behaviours.forEach(LoopAction.INIT);
			}
			
			//Init managed
			managerComponent.init();
		}
	}
	
	@Override
	public void addBehavior(IBehavior behavior) {
		behaviours.addToBirthList(behavior);
		behavior.onBind(getWrapper());
	}
	
	@Override
	public void removeBehavior(IBehavior behavior) {
		behaviours.addToDeathList(behavior);
		behavior.onUnbind(getWrapper());
	}
	
	@Override
	public void visitBehaviors(IBehaviorVisitor visitor) {
		if(behaviours.isEmpty()) {
			return;
		}
		behaviours.forEach(new VisitBehaviorAction(visitor));
	}
	
	@Override
	public void addGraphics(IMovieClipLayer movieClipLayer) {
		graphics.addToBirthList(movieClipLayer);
	}
	
	@Override
	public void removeGraphics(IMovieClipLayer movieClipLayer) {
		graphics.addToDeathList(movieClipLayer);
	}
	
	@Override
	public IShader resolveShader() {
		IShader shader = getWrapper().getShader();
		
		if(!behaviours.isEmpty()) {
			ShaderFinderAction shaderFinder = new ShaderFinderAction(shader);
			behaviours.forEach(shaderFinder);
			shader = shaderFinder.shader;
		}
		
		if(shader != null)
		{
			return shader;
		}
		else
		{
			IMovieClipContainer parent = getWrapper().getParent();
			return parent == null ? null : parent.resolveShader();
		}
	}
	
	@Override
	public IShader getShader() {
		return null;
	}

	@Override
	public void dispose() {
		//Disposed managed
		managerComponent.dispose();
		
		//Dispose behaviours 
		if(!behaviours.isEmpty()) {
			behaviours.forEach(LoopAction.DISPOSE);
		}

		//Dispose children
		if(!children.isEmpty()) {
			children.forEach(LoopAction.DISPOSE);
		}

		//Dispose movieClipLayers
		if(!graphics.isEmpty()) {
			graphics.forEach(LoopAction.DISPOSE);
		}
		
		transform.free();
		transform = null;
		
		behaviours.clear();
		behaviours = null;
		
		children.clear();
		children = null;

		graphics.clear();
		graphics = null;
		
		parent = null;
		
		disposed = true;
	}
	
	@Override
	public void addManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		managerComponent.addManagedLifecycle(lifecycleOwner);
	}
	
	@Override
	public void removeManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		managerComponent.removeManagedLifecycle(lifecycleOwner);
	}
	
	@Override
	public boolean isInitialized() {
		return managerComponent.isInitialized();
	}
	
	@Override
	public boolean isDisposed() {
		return managerComponent.isDisposed();
	}

	@Override
	public MovieClip getWrapper() {
		return wrapper;
	}

	private static class VisitClipAction implements ILoopAction<IMovieClip> {
		private final IMovieClipVisitor visitor;

		public VisitClipAction(IMovieClipVisitor visitor) {
			this.visitor = visitor;
		}

		@Override
		public boolean onIteration(IMovieClip value) {
			visitor.visit(value);
			return true;
		}
	}
	
	private static class VisitBehaviorAction implements ILoopAction<IBehavior> {
		private final IBehaviorVisitor visitor;

		public VisitBehaviorAction(IBehaviorVisitor visitor) {
			this.visitor = visitor;
		}

		@Override
		public boolean onIteration(IBehavior value) {
			visitor.visit(value);
			return true;
		}
	}
	
	private static class GetTransformBehaviorAction implements ILoopAction<IBehavior> {
		private final ITransform mutableTrans;
		private final ITransform loopResult;
		
		public GetTransformBehaviorAction(ITransform mutableTrans, ITransform loopResult) {
			this.mutableTrans = mutableTrans;
			this.loopResult = loopResult;
		}


		@Override
		public boolean onIteration(IBehavior behavior) {
			ITransform returned = behavior.getTransform(mutableTrans, loopResult.setTo(mutableTrans));
			
			if(returned != null && !(returned == loopResult))
			{
				loopResult.setTo(returned);
			}
			
			mutableTrans.setTo(loopResult);
			return true;
		}
	}
	
	private static class ShaderFinderAction implements ILoopAction<IBehavior> {
		private IShader shader;
		
		public ShaderFinderAction(IShader shader) {
			this.shader = shader;
		}

		@Override
		public boolean onIteration(IBehavior behavior) {
			shader = behavior.getShader(shader);
			return true;
		}
	}
	
	private static class SetTransformBehaviorAction implements ILoopAction<IBehavior> {
		private final ITransform mutableTransform;
		
		public SetTransformBehaviorAction(ITransform mutableTransform) {
			this.mutableTransform = mutableTransform;
		}


		@Override
		public boolean onIteration(IBehavior behavior) {
			ITransform returned = behavior.setTransform(mutableTransform);
			if(returned != mutableTransform)
			{
				mutableTransform.setTo(returned);
			}
			return true;
		}
	}
	
	private static class HitTestAction implements ILoopAction<IHittable> {
		public boolean hit = false;
		private Position ptDst;
		private Position ptDstCopy;
		
		public HitTestAction reset(Position ptDst, Position ptDstCopy) {
			this.ptDst = ptDst;
			this.ptDstCopy = ptDstCopy;
			this.hit = false;
			return this;
		}

		@Override
		public boolean onIteration(IHittable hittable) {
			if(hittable.hitTest(ptDstCopy.setTo(ptDst))) {
				hit = true;
				return false; //Return
			}
			return true;
		}
	}
	
	private static class DrawAction implements ILoopAction<IDrawable> {
		private final IDrawCycle drawCycle;
		private final ITransform originalTransform;

		public DrawAction(IDrawCycle drawCycle, ITransform originalTransform) {
			this.drawCycle = drawCycle;
			this.originalTransform = originalTransform;
		}

		@Override
		public boolean onIteration(IDrawable drawable) {
			drawable.draw(drawCycle);
			drawCycle.getTransform().setTo(originalTransform);
			return true;
		}
	}
	
	private static class BehaviorClickAction implements ILoopAction<IBehavior> {
		private final IClickEvent clickEvent;
		private final boolean consumedBeforeBehaviors;

		public BehaviorClickAction(IClickEvent clickEvent, boolean consumedBeforeBehaviors) {
			this.clickEvent = clickEvent;
			this.consumedBeforeBehaviors = consumedBeforeBehaviors;
		}

		@Override
		public boolean onIteration(IBehavior behavior) {
			behavior.onClick(clickEvent, consumedBeforeBehaviors);
			return true;
		}
	}
}
