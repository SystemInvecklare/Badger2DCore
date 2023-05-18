package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Consumer;

/*package-private*/ class CellLayoutSettings implements ICellLayoutSettings {
	public int paddingLeft;
	public int paddingRight;
	public int paddingTop;
	public int paddingBottom;
	public float alignX;
	public float alignY;
	public boolean fillHorizontal;
	public boolean fillVertical;
	public int minWidth;
	public int minHeight;
	
	public CellLayoutSettings(int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, float alignX,
			float alignY, boolean fillHorizontal, boolean fillVertical, int minWidth, int minHeight) {
		this.paddingLeft = paddingLeft;
		this.paddingRight = paddingRight;
		this.paddingTop = paddingTop;
		this.paddingBottom = paddingBottom;
		this.alignX = alignX;
		this.alignY = alignY;
		this.fillHorizontal = fillHorizontal;
		this.fillVertical = fillVertical;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
	}
	
	
	@Override
	public ICellLayoutSettings reset() {
		this.paddingLeft = 0;
		this.paddingRight = 0;
		this.paddingTop = 0;
		this.paddingBottom = 0;
		this.alignX = 0;
		this.alignY = 0;
		this.fillHorizontal = false;
		this.fillVertical = false;
		this.minWidth = 0;
		this.minHeight = 0;
		return this;
	}

	public int getOffsetY(int extraSpace) {
		return paddingBottom + ((int) (alignY*extraSpace));
	}

	public int getOffsetX(int extraSpace) {
		return paddingLeft + ((int) (alignX*extraSpace));
	}

	@Override
	public ICellLayoutSettings paddingLeft(int left) {
		this.paddingLeft = left;
		return this;
	}
	
	@Override
	public ICellLayoutSettings paddingRight(int right) {
		this.paddingRight = right;
		return this;
	}
	
	@Override
	public ICellLayoutSettings paddingTop(int top) {
		this.paddingTop = top;
		return this;
	}
	
	@Override
	public ICellLayoutSettings paddingBottom(int bottom) {
		this.paddingBottom = bottom;
		return this;
	}
	
	@Override
	public ICellLayoutSettings alignX(float alignX) {
		this.alignX = alignX;
		return this;
	}
	
	@Override
	public ICellLayoutSettings alignY(float alignY) {
		this.alignY = alignY;
		return this;
	}
	
	@Override
	public ICellLayoutSettings fillHorizontal(boolean fillHorizontal) {
		this.fillHorizontal = fillHorizontal;
		return this;
	}
	
	@Override
	public ICellLayoutSettings fillVertical(boolean fillVertical) {
		this.fillVertical = fillVertical;
		return this;
	}

	@Override
	public ICellLayoutSettings padding(int padding) {
		return padding(padding, padding, padding, padding);
	}

	@Override
	public ICellLayoutSettings padding(int horizontal, int vertical) {
		return paddingHorizontal(horizontal).paddingVertical(vertical);
	}

	@Override
	public ICellLayoutSettings padding(int left, int right, int top, int bottom) {
		return paddingLeft(left).paddingRight(right).paddingTop(top).paddingBottom(bottom);
	}

	@Override
	public ICellLayoutSettings paddingHorizontal(int padding) {
		return paddingLeft(padding).paddingRight(padding);
	}

	@Override
	public ICellLayoutSettings paddingVertical(int padding) {
		return paddingTop(padding).paddingBottom(padding);
	}
	
	@Override
	public ICellLayoutSettings align(float alignX, float alignY) {
		return alignX(alignX).alignY(alignY);
	}
	
	@Override
	public ICellLayoutSettings fill(boolean fillHorizontal, boolean fillVertical) {
		return fillHorizontal(fillHorizontal).fillVertical(fillVertical);
	}
	
	@Override
	public ICellLayoutSettings fillVertical(boolean fillVertical, int minHeight) {
		return fillVertical(fillVertical).minHeight(minHeight);
	}
	
	@Override
	public ICellLayoutSettings fillHorizontal(boolean fillHorizontal, int minWidth) {
		return fillHorizontal(fillHorizontal).minWidth(minWidth);
	}
	
	@Override
	public ICellLayoutSettings minWidth(int minWidth) {
		this.minWidth = minWidth;
		return this;
	}
	
	@Override
	public ICellLayoutSettings minHeight(int minHeight) {
		this.minHeight = minHeight;
		return this;
	}
	
	@Override
	public ICellLayoutSettings minSize(int minWidth, int minHeight) {
		return minWidth(minWidth).minHeight(minHeight);
	}
	
	@Override
	public ICellLayoutSettings also(Consumer<ICellLayoutSettings> extra) {
		extra.accept(this);
		return this;
	}
	
	public CellLayoutSettings copy() {
		return new CellLayoutSettings(paddingLeft, paddingRight, paddingTop, paddingBottom, alignX, alignY, fillHorizontal, fillVertical, minWidth, minHeight);
	}
}
