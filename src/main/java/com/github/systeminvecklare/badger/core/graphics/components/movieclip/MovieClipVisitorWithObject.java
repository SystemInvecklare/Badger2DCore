package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

public abstract class MovieClipVisitorWithObject<T> implements IMovieClipVisitor {
	private T object;
	
	public MovieClipVisitorWithObject(T object) {
		this.object = object;
	}

	@Override
	public final void visit(IMovieClip movieClip) {
		this.visitWith(movieClip, object);
	}

	protected abstract void visitWith(IMovieClip movieClip, T object);
}
