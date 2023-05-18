package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Consumer;

public interface ICellLayoutSettings {
	ICellLayoutSettings padding(int padding);
	ICellLayoutSettings padding(int horizontal, int vertical);
	ICellLayoutSettings padding(int left, int right, int top, int bottom);
	ICellLayoutSettings paddingHorizontal(int padding);
	ICellLayoutSettings paddingVertical(int padding);
	ICellLayoutSettings paddingLeft(int left);
	ICellLayoutSettings paddingRight(int right);
	ICellLayoutSettings paddingTop(int top);
	ICellLayoutSettings paddingBottom(int bottom);
	ICellLayoutSettings align(float alignX, float alignY);
	ICellLayoutSettings alignX(float alignX);
	ICellLayoutSettings alignY(float alignY);
	ICellLayoutSettings fill(boolean fillHorizontal, boolean fillVertical);
	ICellLayoutSettings fillHorizontal(boolean fillHorizontal);
	ICellLayoutSettings fillHorizontal(boolean fillHorizontal, int minWidth);
	ICellLayoutSettings fillVertical(boolean fillVertical);
	ICellLayoutSettings fillVertical(boolean fillVertical, int minHeight);
	ICellLayoutSettings minWidth(int minWidth);
	ICellLayoutSettings minHeight(int minHeight);
	ICellLayoutSettings minSize(int minWidth, int minHeight);
	ICellLayoutSettings reset();
	ICellLayoutSettings also(Consumer<ICellLayoutSettings> extra);
}
