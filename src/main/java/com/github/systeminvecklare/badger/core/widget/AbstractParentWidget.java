package com.github.systeminvecklare.badger.core.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/*package-protected*/ abstract class AbstractParentWidget<C extends AbstractParentWidget.Child<?>> extends AbstractWidget implements IWidget {
	private int outerPaddingLeft = 0;
	private int outerPaddingRight = 0;
	private int outerPaddingTop = 0;
	private int outerPaddingBottom = 0;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	protected final List<C> children = new ArrayList<C>();
	private final CellLayoutSettings defaultLayoutSettings = createDefaultLayoutSettings();
	
	public AbstractParentWidget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
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
	
	/*package-protected*/ void internalSetWidth(int width) {
		this.width = width;
	}
	
	/*package-protected*/ void internalSetHeight(int height) {
		this.height = height;
	}
	
	@Override
	public void addToPosition(int dx, int dy) {
		setPosition(this.x + dx, this.y + dy);
	}
	
	@Override
	public void setPosition(int x, int y) {
		for(Child<?> child : children) {
			child.addToPosition(x - this.x, y - this.y);
		}
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public final void pack() {
		int outerPaddingLeft = this.getOuterPaddingLeft();
		int outerPaddingRight = this.getOuterPaddingRight();
		int outerPaddingBottom = this.getOuterPaddingBottom();
		int outerPaddingTop = this.getOuterPaddingTop();
		pack(outerPaddingLeft, outerPaddingRight, outerPaddingTop, outerPaddingBottom);
	}
	
	protected abstract void pack(int outerPaddingLeft, int outerPaddingRight, int outerPaddingTop, int outerPaddingBottom);

	public void removeChild(Object child) {
		Iterator<C> it = children.iterator();
		while(it.hasNext()) {
			if(Objects.equals(it.next().widget, child)) {
				it.remove();
			}
		}
	}
	

	public void clear() {
		children.clear();
	}
	
	protected void getDefaultInterface(IWidget widget, IDefaultInterfaceHandler handler) {
		if(widget instanceof IResizableWidget) {
			handler.onWidget((IResizableWidget) widget, IResizableWidgetInterface.RESIZABLE_WIDGET_INTERFACE);
		} else {
			handler.onWidget(widget, IWidgetInterface.WIDGET_INTERFACE);
		}
	}
	
	public void setOuterPadding(int padding) {
		setOuterPadding(padding, padding, padding, padding);
	}
	
	public void setOuterPadding(int horizontal, int vertical) {
		setOuterPadding(horizontal, horizontal, vertical, vertical);
	}
	
	public void setOuterPadding(int left, int right, int top, int bottom) {
		setOuterPaddingLeft(left);
		setOuterPaddingRight(right);
		setOuterPaddingTop(top);
		setOuterPaddingBottom(bottom);
	}
	
	public void setOuterPaddingHorizontal(int padding) {
		setOuterPaddingHorizontal(padding, padding);
	}
	
	public void setOuterPaddingHorizontal(int left, int right) {
		setOuterPaddingLeft(left);
		setOuterPaddingRight(right);
	}
	
	public void setOuterPaddingVertical(int padding) {
		setOuterPaddingVertical(padding, padding);
	}
	
	public void setOuterPaddingVertical(int top, int bottom) {
		setOuterPaddingTop(top);
		setOuterPaddingBottom(bottom);
	}
	
	public void setOuterPaddingLeft(int left) {
		this.outerPaddingLeft = left;
	}
	
	public void setOuterPaddingRight(int right) {
		this.outerPaddingRight = right;
	}
	
	public void setOuterPaddingTop(int top) {
		this.outerPaddingTop = top;
	}
	
	public void setOuterPaddingBottom(int bottom) {
		this.outerPaddingBottom = bottom;
	}
	
	public int getOuterPaddingLeft() {
		return outerPaddingLeft;
	}
	
	public int getOuterPaddingRight() {
		return outerPaddingRight;
	}
	
	public int getOuterPaddingBottom() {
		return outerPaddingBottom;
	}
	
	public int getOuterPaddingTop() {
		return outerPaddingTop;
	}
	
	protected CellLayoutSettings createDefaultLayoutSettings() {
		return new CellLayoutSettings(0, 0, 0, 0, 0, 0, false, false, 0, 0);
	}
	
	protected ICellLayoutSettings newLayoutSettings(final Consumer<ICellLayoutSettings> layoutSettings) {
		ICellLayoutSettings settings = newLayoutSettings();
		layoutSettings.accept(settings);
		return settings;
	}
	
	public ICellLayoutSettings defaultLayoutSettings() {
		return defaultLayoutSettings;
	}
	
	
	public ICellLayoutSettings newLayoutSettings() {
		return defaultLayoutSettings.copy();
	}
	
	protected static class Child<W> {
		public final W widget;
		public final IWidgetInterface<? super W> widgetInterface;
		public final CellLayoutSettings layoutSettings;
		
		public Child(W widget, IWidgetInterface<? super W> widgetInterface, CellLayoutSettings layoutSettings) {
			this.widget = widget;
			this.widgetInterface = widgetInterface;
			this.layoutSettings = layoutSettings.copy();
		}
		

		public void addToPosition(int dx, int dy) {
			widgetInterface.addToPosition(widget, dx, dy);
		}

		public void setPosition(int x, int y, int availableSpaceX, int availableSpaceY) {
			availableSpaceX -= layoutSettings.paddingLeft+layoutSettings.paddingRight;
			availableSpaceY -= layoutSettings.paddingTop+layoutSettings.paddingBottom;
			int width = widgetInterface.getWidth(widget);
			int height = widgetInterface.getHeight(widget);
			
			if((availableSpaceX > 0 || availableSpaceY > 0) && (layoutSettings.fillHorizontal || layoutSettings.fillVertical)) {
				IResizableWidgetInterface<W> resizable = IResizableWidget.Util.tryCast(widget, widgetInterface);
				if(resizable != null) {
					if(availableSpaceX > 0 && layoutSettings.fillHorizontal && width != availableSpaceX) {
						resizable.setWidth(widget, availableSpaceX);
						width = availableSpaceX;
					}
					if(availableSpaceY > 0 && layoutSettings.fillVertical && height != availableSpaceY) {
						resizable.setHeight(widget, availableSpaceY);
						height = availableSpaceY;
					}
				}
			}
			
			widgetInterface.setPosition(widget, x+layoutSettings.getOffsetX(availableSpaceX-width), y+layoutSettings.getOffsetY(availableSpaceY-height));
		}

		public int getWidth() {
			int width = layoutSettings.fillHorizontal ? layoutSettings.minWidth : widgetInterface.getWidth(widget);
			return width+layoutSettings.paddingLeft+layoutSettings.paddingRight;
		}
		
		public int getHeight() {
			int height = layoutSettings.fillVertical ? layoutSettings.minHeight : widgetInterface.getHeight(widget);
			return height+layoutSettings.paddingTop+layoutSettings.paddingBottom;
		}
	}
	
	protected interface IDefaultInterfaceHandler {
		<W extends IWidget> void onWidget(W widget, IWidgetInterface<W> widgetInterface);
	}
}
