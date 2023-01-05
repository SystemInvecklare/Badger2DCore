package com.github.systeminvecklare.badger.core.widget;

public class SpacerWidget implements IWidget {
	private int x;
	private int y;
	private final int width;
	private final int height;
	
	public SpacerWidget(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void addToPosition(int dx, int dy) {
		setPosition(this.x + dx, this.y + dy);
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}

	public static SpacerWidget width(int width) {
		return new SpacerWidget(width, 0);
	}
	
	public static SpacerWidget height(int height) {
		return new SpacerWidget(0, height);
	}
}
