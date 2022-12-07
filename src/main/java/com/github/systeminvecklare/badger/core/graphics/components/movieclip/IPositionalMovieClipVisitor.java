package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public interface IPositionalMovieClipVisitor extends IMovieClipVisitor {
	IReadablePosition getGlobalPosition();
}
