package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

public interface IMovieClipDelegate extends IMovieClip {
	public MovieClip getWrapper();
	
	void setVisibleBeforeThink();
}
