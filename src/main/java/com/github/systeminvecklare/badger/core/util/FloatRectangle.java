package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.core.widget.IRectangleInterface;

public final class FloatRectangle implements IFloatRectangle {
	private final float x;
	private final float y;
	private final float width;
	private final float height;
	
	public FloatRectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public FloatRectangle(IFloatRectangle rectangle) {
		this(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	public FloatRectangle(IRectangle rectangle) {
		this(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	public <R> FloatRectangle(R rectangle, IRectangleInterface<R> rectangleInterface) {
		this(rectangleInterface.getX(rectangle), rectangleInterface.getY(rectangle), rectangleInterface.getWidth(rectangle), rectangleInterface.getHeight(rectangle));
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}
}