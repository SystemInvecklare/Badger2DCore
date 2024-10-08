package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.math.Mathf;

public abstract class AbstractWidget implements IWidget {
	public void setAbove(IRectangle other, float alignX) {
		setAbove(other, alignX, 0);
	}
	
	public void setBelow(IRectangle other, float alignX) {
		setBelow(other, alignX, 0);
	}
	
	public void setRightOf(IRectangle other, float alignY) {
		setRightOf(other, alignY, 0);
	}
	
	public void setLeftOf(IRectangle other, float alignY) {
		setLeftOf(other, alignY, 0);
	}
	
	public void setAbove(IRectangle other, float alignX, int padding) {
		setPosition(other.getX() + (int) Mathf.lerp(alignX, 0, other.getWidth() - this.getWidth()), other.getY() + other.getHeight() + padding);
	}
	
	public void setBelow(IRectangle other, float alignX, int padding) {
		setPosition(other.getX() + (int) Mathf.lerp(alignX, 0, other.getWidth() - this.getWidth()), other.getY() - this.getHeight() - padding);
	}
	
	public void setRightOf(IRectangle other, float alignY, int padding) {
		setPosition(other.getX() + other.getWidth() + padding, other.getY() + (int) Mathf.lerp(alignY, 0, other.getHeight() - this.getHeight()));
	}
	
	public void setLeftOf(IRectangle other, float alignY, int padding) {
		setPosition(other.getX() - this.getWidth() - padding, other.getY() + (int) Mathf.lerp(alignY, 0, other.getHeight() - this.getHeight()));
	}
	
	public void setInside(IRectangle other, float alignX, float alignY) {
		setInside(other, alignX, alignY, 0);
	}
	
	public void setInside(IRectangle other, float alignX, float alignY, int padding) {
		setInside(other, alignX, alignY, padding, padding);
	}
	
	public void setInside(IRectangle other, float alignX, float alignY, int paddingX, int paddingY) {
		int newX = other.getX() + (int) Mathf.lerp(alignX, paddingX, other.getWidth() - this.getWidth() - paddingX);
		int newY = other.getY() + (int) Mathf.lerp(alignY, paddingY, other.getHeight() - this.getHeight() - paddingY);
		setPosition(newX, newY);
	}
	
	public void setLeft(int left) {
		setPosition(left, getY());
	}
	
	public void setRight(int right) {
		setPosition(right - getWidth(), getY());
	}
	
	public void setBottom(int bottom) {
		setPosition(getX(), bottom);
	}
	
	public void setTop(int top) {
		setPosition(getX(), top - getHeight());
	}
	
	public final int getLeft() {
		return getX();
	}
	
	public final int getRight() {
		return getX() + getWidth();
	}
	
	public final int getBottom() {
		return getY();
	}
	
	public final int getTop() {
		return getY() + getHeight();
	}
}
