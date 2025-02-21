package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Consumer;

public class WidgetUtil {
	public static final Consumer<ICellLayoutSettings> DEFAULT_LAYOUT = new Consumer<ICellLayoutSettings>() {
		@Override
		public void accept(ICellLayoutSettings settings) {
		}
	};
	
	public static AbstractWidget makeAbstract(final IWidget widget) {
		if(widget instanceof AbstractWidget) {
			return (AbstractWidget) widget;
		}
		return new AbstractWidget() {
			@Override
			public int getX() {
				return widget.getX();
			}
			
			@Override
			public int getY() {
				return widget.getY();
			}
			
			@Override
			public int getWidth() {
				return widget.getWidth();
			}
			
			@Override
			public int getHeight() {
				return widget.getHeight();
			}
			
			@Override
			public void setPosition(int x, int y) {
				widget.setPosition(x, y);
			}
			
			@Override
			public void addToPosition(int dx, int dy) {
				widget.addToPosition(dx, dy);
			}
		};
	}
	
	public static AbstractResizableWidget makeAbstract(final IResizableWidget widget) {
		return new AbstractResizableWidget() {
			@Override
			public int getX() {
				return widget.getX();
			}
			
			@Override
			public int getY() {
				return widget.getY();
			}
			
			@Override
			public int getWidth() {
				return widget.getWidth();
			}
			
			@Override
			public int getHeight() {
				return widget.getHeight();
			}
			
			@Override
			public void setPosition(int x, int y) {
				widget.setPosition(x, y);
			}
			
			@Override
			public void addToPosition(int dx, int dy) {
				widget.addToPosition(dx, dy);
			}

			@Override
			public void setWidth(int width) {
				widget.setWidth(width);
			}

			@Override
			public void setHeight(int height) {
				widget.setHeight(height);
			}
		};
	}
	
	public static <T> AbstractWidget makeAbstract(final T widget, final IWidgetInterface<T> widgetInterface) {
		return new AbstractWidget() {
			@Override
			public int getX() {
				return widgetInterface.getX(widget);
			}
			
			@Override
			public int getY() {
				return widgetInterface.getY(widget);
			}
			
			@Override
			public int getWidth() {
				return widgetInterface.getWidth(widget);
			}
			
			@Override
			public int getHeight() {
				return widgetInterface.getHeight(widget);
			}
			
			@Override
			public void setPosition(int x, int y) {
				widgetInterface.setPosition(widget, x, y);
			}
			
			@Override
			public void addToPosition(int dx, int dy) {
				widgetInterface.addToPosition(widget, dx, dy);
			}
		};
	}
	
	public static <T> AbstractResizableWidget makeAbstract(final T widget, final IResizableWidgetInterface<T> widgetInterface) {
		return new AbstractResizableWidget() {
			@Override
			public int getX() {
				return widgetInterface.getX(widget);
			}
			
			@Override
			public int getY() {
				return widgetInterface.getY(widget);
			}
			
			@Override
			public int getWidth() {
				return widgetInterface.getWidth(widget);
			}
			
			@Override
			public int getHeight() {
				return widgetInterface.getHeight(widget);
			}
			
			@Override
			public void setPosition(int x, int y) {
				widgetInterface.setPosition(widget, x, y);
			}
			
			@Override
			public void addToPosition(int dx, int dy) {
				widgetInterface.addToPosition(widget, dx, dy);
			}

			@Override
			public void setWidth(int width) {
				widgetInterface.setWidth(widget, width);
			}

			@Override
			public void setHeight(int height) {
				widgetInterface.setHeight(widget, height);
			}
		};
	}
	
	public static <T> IWidget widget(final T object, final IWidgetInterface<T> widgetInterface) {
		return new IWidget() {
			@Override
			public int getX() {
				return widgetInterface.getX(object);
			}
			
			@Override
			public int getY() {
				return widgetInterface.getY(object);
			}
			
			@Override
			public int getWidth() {
				return widgetInterface.getWidth(object);
			}
			
			@Override
			public int getHeight() {
				return widgetInterface.getHeight(object);
			}
			
			@Override
			public void setPosition(int x, int y) {
				widgetInterface.setPosition(object, x, y);
			}
			
			@Override
			public void addToPosition(int dx, int dy) {
				widgetInterface.addToPosition(object, dx, dy);
			}
		};
	}
	
	public static <T> IResizableWidget resizableWidget(final T object, final IResizableWidgetInterface<T> widgetInterface) {
		return new IResizableWidget() {
			@Override
			public int getX() {
				return widgetInterface.getX(object);
			}
			
			@Override
			public int getY() {
				return widgetInterface.getY(object);
			}
			
			@Override
			public int getWidth() {
				return widgetInterface.getWidth(object);
			}
			
			@Override
			public int getHeight() {
				return widgetInterface.getHeight(object);
			}
			
			@Override
			public void setPosition(int x, int y) {
				widgetInterface.setPosition(object, x, y);
			}
			
			@Override
			public void addToPosition(int dx, int dy) {
				widgetInterface.addToPosition(object, dx, dy);
			}

			@Override
			public void setWidth(int width) {
				widgetInterface.setWidth(object, width);
			}

			@Override
			public void setHeight(int height) {
				widgetInterface.setHeight(object, height);
			}
		};
	}
	
	
	private WidgetUtil() {}
}
