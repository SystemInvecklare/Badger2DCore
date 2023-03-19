package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Function;

import com.github.systeminvecklare.badger.core.widget.IRectangleInterface;

public class DelegatingRectangleInterface<R, D> implements IRectangleInterface<R> {
	private final IRectangleInterface<D> delegateInterface;
	private final Function<R, ? extends D> delegateAccessor;
	
	public DelegatingRectangleInterface(IRectangleInterface<D> delegateInterface, Function<R, ? extends D> delegateAccessor) {
		this.delegateInterface = delegateInterface;
		this.delegateAccessor = delegateAccessor;
	}

	private D getDelegate(R rectangle) {
		return delegateAccessor.apply(rectangle);
	}
	
	@Override
	public int getX(R rectangle) {
		return delegateInterface.getX(getDelegate(rectangle));
	}


	@Override
	public int getY(R rectangle) {
		return delegateInterface.getY(getDelegate(rectangle));
	}

	@Override
	public int getWidth(R rectangle) {
		return delegateInterface.getWidth(getDelegate(rectangle));
	}

	@Override
	public int getHeight(R rectangle) {
		return delegateInterface.getHeight(getDelegate(rectangle));
	}
}
