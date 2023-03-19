package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Function;

import com.github.systeminvecklare.badger.core.widget.IWidgetInterface;

public class DelegatingWidgetInterface<W, D> extends DelegatingRectangleInterface<W, D> implements IWidgetInterface<W> {
	private final IWidgetInterface<D> delegateInterface;
	private final Function<W, ? extends D> delegateAccessor;
	
	public DelegatingWidgetInterface(IWidgetInterface<D> delegateInterface, Function<W, ? extends D> delegateAccessor) {
		super(delegateInterface, delegateAccessor);
		this.delegateInterface = delegateInterface;
		this.delegateAccessor = delegateAccessor;
	}

	private D getDelegate(W widget) {
		return delegateAccessor.apply(widget);
	}
	
	@Override
	public void setPosition(W widget, int x, int y) {
		delegateInterface.setPosition(getDelegate(widget), x, y);
	}

	@Override
	public void addToPosition(W widget, int dx, int dy) {
		delegateInterface.addToPosition(getDelegate(widget), dx, dy);
	}
}
