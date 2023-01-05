package com.github.systeminvecklare.badger.core.widget;

public interface IResizableWidgetInterface<W> extends IWidgetInterface<W> {
	void setWidth(W widget, int width);
	void setHeight(W widget, int height);
	
	public static final IResizableWidgetInterfaceExtension<IResizableWidget> _EXTENSION_RESIZABLE_WIDGET_INTERFACE = new IResizableWidgetInterfaceExtension<IResizableWidget>() {
		@Override
		public void setWidth(IResizableWidget widget, int width) {
			widget.setWidth(width);
		}
		
		@Override
		public void setHeight(IResizableWidget widget, int height) {
			widget.setHeight(height);
		}
	};
	
	public static final IResizableWidgetInterface<IResizableWidget> RESIZABLE_WIDGET_INTERFACE = Util.createExtension(IWidgetInterface.WIDGET_INTERFACE, _EXTENSION_RESIZABLE_WIDGET_INTERFACE);
	
	interface IResizableWidgetInterfaceExtension<W2> {
		void setWidth(W2 widget, int width);
		void setHeight(W2 widget, int height);
	}
	
	public static final class Util {
		private Util() {}
		public static <R> IResizableWidgetInterface<R> createExtension(final IWidgetInterface<? super R> widgetInterface, final IResizableWidgetInterfaceExtension<R> extension) {
			return new IResizableWidgetInterface<R>() {
				@Override
				public void setPosition(R widget, int x, int y) {
					widgetInterface.setPosition(widget, x, y);
				}

				@Override
				public void addToPosition(R widget, int dx, int dy) {
					widgetInterface.addToPosition(widget, dx, dy);
				}

				@Override
				public int getX(R rectangle) {
					return widgetInterface.getX(rectangle);
				}

				@Override
				public int getY(R rectangle) {
					return widgetInterface.getY(rectangle);
				}

				@Override
				public int getWidth(R rectangle) {
					return widgetInterface.getWidth(rectangle);
				}

				@Override
				public int getHeight(R rectangle) {
					return widgetInterface.getHeight(rectangle);
				}

				@Override
				public void setWidth(R widget, int width) {
					extension.setWidth(widget, width);
				}

				@Override
				public void setHeight(R widget, int height) {
					extension.setHeight(widget, height);
				}
			};
		}
	}
}
