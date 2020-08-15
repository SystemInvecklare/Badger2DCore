package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;

public interface ITransformDependentMovieClipVisitor extends IMovieClipVisitor {
	public ITransform transform();
}
