package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Function;

public class DelegatingInterface {
	public static <R, D> IRectangleInterface<R> rectangle(IRectangleInterface<D> delegateInterface, Function<R, ? extends D> delegateAccessor) {
		return new DelegatingRectangleInterface<R, D>(delegateInterface, delegateAccessor);
	}
	
	public static <R, D extends IRectangle> IRectangleInterface<R> rectangle(Function<R, ? extends D> delegateAccessor) {
		return rectangle(IRectangleInterface.RECTANGLE_INTERFACE, delegateAccessor);
	}
	
	public static <R, W> IWidgetInterface<R> widget(IWidgetInterface<W> delegateInterface, Function<R, ? extends W> delegateAccessor) {
		return new DelegatingWidgetInterface<R, W>(delegateInterface, delegateAccessor);
	}
	
	public static <R, W extends IWidget> IWidgetInterface<R> widget(Function<R, ? extends W> delegateAccessor) {
		return widget(IWidgetInterface.WIDGET_INTERFACE, delegateAccessor);
	}
	
	public static <R, W> IResizableWidgetInterface<R> resizableWidget(IResizableWidgetInterface<W> delegateInterface, Function<R, ? extends W> delegateAccessor) {
		return new DelegatingResizableWidgetInterface<R, W>(delegateInterface, delegateAccessor);
	}
	
	public static <R, W extends IResizableWidget> IResizableWidgetInterface<R> resizableWidget(Function<R, ? extends W> delegateAccessor) {
		return resizableWidget(IResizableWidgetInterface.RESIZABLE_WIDGET_INTERFACE, delegateAccessor);
	}
}
