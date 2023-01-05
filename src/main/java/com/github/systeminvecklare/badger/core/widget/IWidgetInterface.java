package com.github.systeminvecklare.badger.core.widget;

public interface IWidgetInterface<W> extends IRectangleInterface<W> {
	void setPosition(W widget, int x, int y);
	void addToPosition(W widget, int dx, int dy);
	
	public static final IWidgetInterfaceExtension<IWidget> _EXTENSION_WIDGET_INTERFACE = new IWidgetInterfaceExtension<IWidget>() {
		@Override
		public void setPosition(IWidget widget, int x, int y) {
			widget.setPosition(x, y);
		}
		
		@Override
		public void addToPosition(IWidget widget, int dx, int dy) {
			widget.addToPosition(dx, dy);
		}
	};
	
	public static final IWidgetInterface<IWidget> WIDGET_INTERFACE = Util.createExtension(IRectangleInterface.RECTANGLE_INTERFACE, _EXTENSION_WIDGET_INTERFACE);
	
	interface IWidgetInterfaceExtension<W2> {
		void setPosition(W2 widget, int x, int y);
		void addToPosition(W2 widget, int dx, int dy);
	}
	
	public static final class Util {
		private Util() {}
		/**
		 * "If you have a way to interpret something that R is (? super R refers to a
		 * type that is a supertype of R. Therefore R is a type of '? super R') as a
		 * rectangle and provide me the extension to interpret R as a widget, I can give
		 * you a way to interpret R as a widget."
		 */
		public static <R> IWidgetInterface<R> createExtension(final IRectangleInterface<? super R> rectangleInterface, final IWidgetInterfaceExtension<R> extension) {
			return new IWidgetInterface<R>() {
				@Override
				public int getX(R rectangle) {
					return rectangleInterface.getX(rectangle);
				}

				@Override
				public int getY(R rectangle) {
					return rectangleInterface.getY(rectangle);
				}

				@Override
				public int getWidth(R rectangle) {
					return rectangleInterface.getWidth(rectangle);
				}

				@Override
				public int getHeight(R rectangle) {
					return rectangleInterface.getHeight(rectangle);
				}

				@Override
				public void setPosition(R widget, int x, int y) {
					extension.setPosition(widget, x, y);
				}

				@Override
				public void addToPosition(R widget, int dx, int dy) {
					extension.addToPosition(widget, dx, dy);
				}
			};
		}
	}
}
