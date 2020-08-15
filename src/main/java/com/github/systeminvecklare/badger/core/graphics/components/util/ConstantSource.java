package com.github.systeminvecklare.badger.core.graphics.components.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.ISource;

public class ConstantSource<T> implements ISource<T> {
	private T object;
	
	public ConstantSource(T object)
	{
		this.object = object;
	}
	
	@Override
	public T getFromSource() {
		return object;
	}

}
