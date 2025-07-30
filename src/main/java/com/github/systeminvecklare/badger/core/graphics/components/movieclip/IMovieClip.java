package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

import com.github.systeminvecklare.badger.core.graphics.components.core.IClickable;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawable;
import com.github.systeminvecklare.badger.core.graphics.components.core.IHittable;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleManager;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.IBehavior;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.IBehaviorVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransformOperation;

public interface IMovieClip extends IMovieClipContainer, IHittable, IDrawable, IClickable, IThinker, ILifecycleOwner, ILifecycleManager {
	public IMovieClipContainer getParent();
	public void setParent(IMovieClipContainer parent);
	
	public ITransform getTransform(ITransform result);
	public IReadableTransform getTransformBypassBehaviors();
	
	public void setTransform(IReadableTransform transform);
	public void setTransformBypassBehaviors(IReadableTransform transform);
	
	public void modifyTransform(ITransformOperation operation, boolean byPassBehaviorsOnGet, boolean byPassBehaviorsOnSet);
	
	public void drawWithoutTransform(IDrawCycle drawCycle);
	
	public void addBehavior(IBehavior behavior);
	public void removeBehavior(IBehavior behavior);
	public void visitBehaviors(IBehaviorVisitor visitor);
	
	public void addGraphics(IMovieClipLayer movieClipLayer);
	public void removeGraphics(IMovieClipLayer movieClipLayer);
	
	public boolean isVisible();
	
//	public IShader resolveShader();
}
