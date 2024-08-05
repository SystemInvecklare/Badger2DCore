package com.github.systeminvecklare.badger.core.util;

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