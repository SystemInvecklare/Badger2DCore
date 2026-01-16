package com.github.systeminvecklare.badger.core.widget;

public class SizeRectangle implements IRectangle {
	private final int width;
	private final int height;
	
	public SizeRectangle(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}