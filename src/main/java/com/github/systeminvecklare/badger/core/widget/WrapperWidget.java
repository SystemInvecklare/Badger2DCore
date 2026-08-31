package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Consumer;

public class WrapperWidget extends AbstractParentWidget<WrapperWidget.PositionedChild<?>> implements IWidget {
	public WrapperWidget() {
		this(0, 0);
	}

	public WrapperWidget(int x, int y) {
		super(x, y, 0, 0);
	}
	
	public <W extends IWidget> W addChild(W widget) {
		return addChild(widget, widget.getX(), widget.getY(), defaultLayoutSettings());
	}
	
	public <W extends IWidget> W addChild(W widget, int x, int y) {
		return addChild(widget, x, y, defaultLayoutSettings());
	}
	
	public <W extends IWidget> W addChild(W widget, int x, int y, final Consumer<ICellLayoutSettings> layoutSettings) {
		return addChild(widget, x, y, newLayoutSettings(layoutSettings));
	}
	
	public <W extends IWidget> W addChild(W widget, final int x, final int y, final ICellLayoutSettings layoutSettings) {
		getDefaultInterface(widget, new IDefaultInterfaceHandler() {
			@Override
			public <W2 extends IWidget> void onWidget(W2 widget, IWidgetInterface<W2> widgetInterface) {
				addChild(widget, widgetInterface, x, y, layoutSettings);
			}
		});
		return widget;
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface) {
		return addChild(widget, widgetInterface, widgetInterface.getX(widget), widgetInterface.getY(widget), defaultLayoutSettings());
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int x, int y) {
		return addChild(widget, widgetInterface, x, y, defaultLayoutSettings());
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int x, int y, Consumer<ICellLayoutSettings> layoutSettings) {
		return addChild(widget, widgetInterface, x, y, newLayoutSettings(layoutSettings));
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int x, int y, ICellLayoutSettings layoutSettings) {
		children.add(new PositionedChild<W>(widget, widgetInterface, x, y, (CellLayoutSettings) layoutSettings));
		return widget;
	}
	
	@Override
	protected void pack(int outerPaddingLeft, int outerPaddingRight, int outerPaddingTop, int outerPaddingBottom) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minWidthPush = 0;
		int minHeightPush = 0;
		for(PositionedChild<?> child : children) {
			if(!child.layoutSettings.fillHorizontal) {
				minX = Math.min(child.getMinX(), minX);
				maxX = Math.max(child.getMaxX(), maxX);
			} else {
				minWidthPush = Math.max(minWidthPush, child.layoutSettings.minWidth);
			}
			if(!child.layoutSettings.fillVertical) {
				minY = Math.min(child.getMinY(), minY);
				maxY = Math.max(child.getMaxY(), maxY);
			} else {
				minHeightPush = Math.max(minHeightPush, child.layoutSettings.minHeight);
			}
		}
		if(minX == Integer.MAX_VALUE) {
			minX = 0;
			maxX = 0;
		}
		if(minY == Integer.MAX_VALUE) {
			minY = 0;
			maxY = 0;
		}
		
		int widthSpan = Math.max(maxX - minX, minWidthPush);
		int heightSpan = Math.max(maxY - minY, minHeightPush);
		internalSetWidth(widthSpan + outerPaddingLeft + outerPaddingRight);
		internalSetHeight(heightSpan + outerPaddingTop + outerPaddingBottom);
		int parentX = getX() + outerPaddingLeft;
		int parentY = getY() + outerPaddingBottom ;
		for(PositionedChild<?> child : children) {
			child.setPositionPack(parentX, parentY, minX, minY, widthSpan, heightSpan);
		}
	}

	protected static class PositionedChild<W> extends AbstractParentWidget.Child<W> {
		private final int x;
		private final int y;
		
		public PositionedChild(W widget, IWidgetInterface<? super W> widgetInterface, int x, int y, CellLayoutSettings layoutSettings) {
			super(widget, widgetInterface, layoutSettings);
			this.x = x;
			this.y = y;
		}
		
		public void setPositionPack(int parentX, int parentY, int minX, int minY, int widthSpan, int heightSpan) {
			int x = layoutSettings.fillHorizontal ? minX : this.x;
			int y = layoutSettings.fillVertical ? minY : this.y;
			setPosition(parentX  - minX + x, parentY - minY + y, widthSpan, heightSpan);
		}

		public int getMinX() {
			return x;
		}
		
		public int getMinY() {
			return y;
		}
		
		public int getMaxX() {
			return x + getWidth();
		}
		
		public int getMaxY() {
			return y + getHeight();
		}
	}
}
