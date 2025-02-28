package com.github.systeminvecklare.badger.core.font;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.util.IFloatRectangle;
import com.github.systeminvecklare.badger.core.util.TransformingGraphics;
import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.core.widget.IRectangleInterface;

public class MovieClipLayerEmbellishment implements ITextEmbellishment {
	private final TransformingGraphics transformingGraphics;
	private final IFloatRectangle bounds;
	private final IEmbellishmentTag tag;
	
	public <R> MovieClipLayerEmbellishment(IMovieClipLayer graphics, R bounds, IRectangleInterface<R> rectangleInterface, IEmbellishmentTag tag) {
		this(graphics, new FloatRectangleInterfaceAdapter<R>(bounds, rectangleInterface), tag);
	}
	
	public MovieClipLayerEmbellishment(IMovieClipLayer graphics, IRectangle bounds, IEmbellishmentTag tag) {
		this(graphics, new FloatRectangleAdapter(bounds), tag);
	}

	public MovieClipLayerEmbellishment(IMovieClipLayer graphics, IFloatRectangle bounds, IEmbellishmentTag tag) {
		this.transformingGraphics = new TransformingGraphics(graphics);
		this.bounds = bounds;
		this.tag = tag;
	}

	@Override
	public float getXAdvance() {
		return bounds.getWidth();
	}
	
	@Override
	public IEmbellishmentTag getEmbellishmentTag() {
		return tag;
	}
	
	@Override
	public IFloatRectangle getBounds() {
		return bounds;
	}
	
	@Override
	public void draw(IDrawCycle drawCycle, float x, float y) {
		transformingGraphics.setPosition(x, y);
		transformingGraphics.draw(drawCycle);
	}
	
	private static class FloatRectangleAdapter implements IFloatRectangle {
		private final IRectangle rectangle;

		public FloatRectangleAdapter(IRectangle rectangle) {
			this.rectangle = rectangle;
		}

		@Override
		public float getX() {
			return rectangle.getX();
		}

		@Override
		public float getY() {
			return rectangle.getY();
		}

		@Override
		public float getWidth() {
			return rectangle.getWidth();
		}

		@Override
		public float getHeight() {
			return rectangle.getHeight();
		}
	}
	
	private static class FloatRectangleInterfaceAdapter<R> implements IFloatRectangle {
		private final R rectangle;
		private final IRectangleInterface<R> rectangleInterface;

		public FloatRectangleInterfaceAdapter(R rectangle, IRectangleInterface<R> rectangleInterface) {
			this.rectangle = rectangle;
			this.rectangleInterface = rectangleInterface;
		}

		@Override
		public float getX() {
			return rectangleInterface.getX(rectangle);
		}

		@Override
		public float getY() {
			return rectangleInterface.getY(rectangle);
		}

		@Override
		public float getWidth() {
			return rectangleInterface.getWidth(rectangle);
		}

		@Override
		public float getHeight() {
			return rectangleInterface.getHeight(rectangle);
		}
	}
}
