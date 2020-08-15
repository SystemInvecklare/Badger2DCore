package com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor;

import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayerVisitor;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipVisitor;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;

/**
 * 
 * @author Matte
 *
 * @param <A> The argument type used at resets.
 */
public interface IMovieClipCollector<A> extends ILayerVisitor, IMovieClipVisitor, ILifecycleOwner {
	public void traverseAndClearCollected(IMovieClipVisitor visitor);
	public void doClick(IClickEvent clickEvent);
	public IMovieClipCollector<A> reset(A value);
}
