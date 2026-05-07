package com.github.systeminvecklare.badger.core.widget;

public abstract class AbstractResizableWidget extends AbstractWidget implements IResizableWidget {
	public void stretchTopTo(int top) {
		setHeight(top - getY());
	}
	
	public void stretchBottomTo(int bottom) {
		setHeight(getY() - bottom + getHeight());
		setPosition(getX(), bottom);
	}
	
	public void stretchLeftTo(int left) {
		setWidth(getX() - left + getWidth());
		setPosition(left, getY());
	}
	
	public void stretchRightTo(int right) {
		setWidth(right - getX());
	}
	
	public void inset(int amount) {
		addToPosition(amount, amount);
		setWidth(getWidth() - amount*2);
		setHeight(getHeight() - amount*2);
	}
	
	public void inset(int horizontal, int vertical) {
		addToPosition(horizontal, vertical);
		setWidth(getWidth() - horizontal*2);
		setHeight(getHeight() - vertical*2);
	}
	
	public void insetHorizontal(int amount) {
		addToPosition(amount, 0);
		setWidth(getWidth() - amount*2);
	}
	
	public void insetVertical(int amount) {
		addToPosition(0, amount);
		setHeight(getHeight() - amount*2);
	}
	
	public void insetLeft(int amount) {
		addToPosition(amount, 0);
		setWidth(getWidth() - amount);
	}
	
	public void insetRight(int amount) {
		setWidth(getWidth() - amount);
	}
	
	public void insetTop(int amount) {
		setHeight(getHeight() - amount);
	}
	
	public void insetBottom(int amount) {
		addToPosition(0, amount);
		setHeight(getHeight() - amount);
	}
	
	public void outset(int amount) {
		inset(-amount);
	}
	
	public void outset(int horizontal, int vertical) {
		inset(-horizontal, -vertical);
	}
	
	public void outsetHorizontal(int amount) {
		insetHorizontal(-amount);
	}
	
	public void outsetVertical(int amount) {
		insetVertical(-amount);
	}
	
	public void outsetLeft(int amount) {
		insetLeft(-amount);
	}
	
	public void outsetRight(int amount) {
		insetRight(-amount);
	}
	
	public void outsetTop(int amount) {
		insetTop(-amount);
	}
	
	public void outsetBottom(int amount) {
		insetBottom(-amount);
	}
}
