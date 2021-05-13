package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
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
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.util.ISmartList;
import com.github.systeminvecklare.badger.core.util.QuickArray;

public class MovieClipDelegate implements IMovieClipDelegate {
	private MovieClip wrapper;
	
	private IMovieClipContainer parent;
	
	private ISmartList<IMovieClipLayer> graphics;
	private ISmartList<IMovieClip> children;
	private ISmartList<IBehavior> behaviours;
	
	private ITransform transform;
	
	private boolean disposed = false;
	
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
					QuickArray<IMovieClip> array = children.getUpdatedArray();
					for(int i = 0; i < array.getSize(); ++i)
					{
						visitor.visit(array.get(i));
					}
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
			QuickArray<IMovieClip> array = children.getUpdatedArray();
			for(int i = 0; i < array.getSize(); ++i)
			{
				visitor.visit(array.get(i));
			}
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
				QuickArray<IBehavior> array = behaviours.getUpdatedArray();
				for(int i = 0; i < array.getSize(); ++i)
				{
					ITransform returned = array.get(i).getTransform(mutableTrans, loopResult.setTo(mutableTrans));
					
					if(returned != null && !(returned == loopResult))
					{
						loopResult.setTo(returned);
					}
					
					mutableTrans.setTo(loopResult);
				}
				
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
				QuickArray<IBehavior> array = behaviours.getUpdatedArray();
				for(int i = 0; i < array.getSize(); ++i)
				{
					ITransform returned = array.get(i).setTransform(mutableTransform);
					if(returned != mutableTransform)
					{
						mutableTransform.setTo(returned);
					}
				}
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
			
			if(!graphics.isEmpty()) {
				QuickArray<IMovieClipLayer> graphicsArray = graphics.getUpdatedArray();
				for(int i = 0; i < graphicsArray.getSize(); ++i)
				{
					if(graphicsArray.get(i).hitTest(ptDstCopy.setTo(ptDst)))
					{
						return true;
					}
				}
			}
			if(!children.isEmpty()) {
				QuickArray<IMovieClip> childrenArray = children.getUpdatedArray();
				for(int i = 0; i < childrenArray.getSize(); ++i)
				{
					if(childrenArray.get(i).hitTest(ptDstCopy.setTo(ptDst)))
					{
						return true;
					}
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
					QuickArray<IMovieClipLayer> graphicsArray = graphics.getUpdatedArray();
					for(int i = 0; i < graphicsArray.getSize(); ++i)
					{
						graphicsArray.get(i).draw(drawCycle);
						drawCycle.getTransform().setTo(originalTransform);
					}
				}
				if(!children.isEmpty()) {
					QuickArray<IMovieClip> childrenArray = children.getUpdatedArray();
					for(int i = 0; i < childrenArray.getSize(); ++i)
					{
						childrenArray.get(i).draw(drawCycle);
						drawCycle.getTransform().setTo(originalTransform);
					}
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
			QuickArray<IBehavior> behaviourArray = behaviours.getUpdatedArray();
			for(int i = 0; i < behaviourArray.getSize(); ++i)
			{
				behaviourArray.get(i).onClick(clickEvent, consumedBeforeBehaviors);
			}
		}
	}

	@Override
	public void think(ITic tic) {
		if(!disposed)
		{
			if(!children.isEmpty()) {
				QuickArray<IMovieClip> childrenArray = children.getUpdatedArray();
				for(int i = 0; i < childrenArray.getSize(); ++i)
				{
					childrenArray.get(i).think(tic);
				}
			}
			
			if(!behaviours.isEmpty()) {
				QuickArray<IBehavior> behaviourArray = behaviours.getUpdatedArray();
				for(int i = 0; i < behaviourArray.getSize(); ++i)
				{
					behaviourArray.get(i).think(tic);
				}
			}
		}
	}

	@Override
	public void init() {
		transform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain().setToIdentity();
		
		//Init movieClipLayers
		if(!graphics.isEmpty()) {
			QuickArray<IMovieClipLayer> graphicsArray = graphics.getUpdatedArray();
			for(int i = 0; i < graphicsArray.getSize(); ++i)
			{
				graphicsArray.get(i).init();
			}
		}
		
		//Init children
		if(!children.isEmpty()) {
			QuickArray<IMovieClip> childrenArray = children.getUpdatedArray();
			for(int i = 0; i < childrenArray.getSize(); ++i)
			{
				childrenArray.get(i).init();
			}
		}
		
		//Init behaviours
		if(!behaviours.isEmpty()) {
			QuickArray<IBehavior> behaviourArray = behaviours.getUpdatedArray();
			for(int i = 0; i < behaviourArray.getSize(); ++i)
			{
				behaviourArray.get(i).init();
			}
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
		QuickArray<IBehavior> array = behaviours.getUpdatedArray();
		for(int i = 0; i < array.getSize(); ++i) {
			visitor.visit(array.get(i));
		}
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
			QuickArray<IBehavior> behaviourArray = behaviours.getUpdatedArray();
			for(int i = 0; i < behaviourArray.getSize(); ++i)
			{
				shader = behaviourArray.get(i).getShader(shader);
			}
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
		//Dispose behaviours 
		if(!behaviours.isEmpty()) {
			QuickArray<IBehavior> behaviourArray = behaviours.getUpdatedArray();
			for(int i = 0; i < behaviourArray.getSize(); ++i)
			{
				behaviourArray.get(i).dispose();
			}
		}

		//Dispose children
		if(!children.isEmpty()) {
			QuickArray<IMovieClip> childrenArray = children.getUpdatedArray();
			for(int i = 0; i < childrenArray.getSize(); ++i)
			{
				childrenArray.get(i).dispose();
			}
		}

		//Dispose movieClipLayers
		if(!graphics.isEmpty()) {
			QuickArray<IMovieClipLayer> graphicsArray = graphics.getUpdatedArray();
			for(int i = 0; i < graphicsArray.getSize(); ++i)
			{
				graphicsArray.get(i).dispose();
			}
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
	public MovieClip getWrapper() {
		return wrapper;
	}

}
