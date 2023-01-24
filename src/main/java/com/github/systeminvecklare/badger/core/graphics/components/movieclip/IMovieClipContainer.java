package com.github.systeminvecklare.badger.core.graphics.components.movieclip;

import com.github.systeminvecklare.badger.core.graphics.components.scene.ISceneChild;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;


public interface IMovieClipContainer extends ISceneChild {
	public void addMovieClip(IMovieClip child);
	public void removeMovieClip(IMovieClip child);
	public void visitChildrenMovieClips(IMovieClipVisitor visitor);
	
	public ITransform toGlobalTransform(IReadableTransform transform, ITransform result);
	public ITransform toGlobalTransform(ITransform result);
	public ITransform toLocalTransform(IReadableTransform transform, ITransform result);
	public ITransform toLocalTransform(ITransform result);
	
	public Position toGlobalPosition(IReadablePosition position, Position result);
	public Position toGlobalPosition(Position result);
	public Position toLocalPosition(IReadablePosition position, Position result);
	public Position toLocalPosition(Position result);
	
	public IShader getShader();
	public IShader resolveShader();
}
