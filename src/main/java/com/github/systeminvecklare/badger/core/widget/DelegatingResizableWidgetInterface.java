package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Function;

import com.github.systeminvecklare.badger.core.widget.IResizableWidgetInterface;

public class DelegatingResizableWidgetInterface<RW, D> extends DelegatingWidgetInterface<RW, D> implements IResizableWidgetInterface<RW> {
	private final IResizableWidgetInterface<D> delegateInterface;
	private final Function<RW, ? extends D> delegateAccessor;
	
	public DelegatingResizableWidgetInterface(IResizableWidgetInterface<D> delegateInterface, Function<RW, ? extends D> delegateAccessor) {
		super(delegateInterface, delegateAccessor);
		this.delegateInterface = delegateInterface;
		this.delegateAccessor = delegateAccessor;
	}

	private D getDelegate(RW widget) {
		return delegateAccessor.apply(widget);
	}

	@Override
	public void setWidth(RW widget, int width) {
		delegateInterface.setWidth(getDelegate(widget), width);
	}

	@Override
	public void setHeight(RW widget, int height) {
		delegateInterface.setHeight(getDelegate(widget), height);
	}
}
