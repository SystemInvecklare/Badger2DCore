package com.github.systeminvecklare.badger.core.graphics.components.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.IBoolSource;
import com.github.systeminvecklare.badger.core.graphics.components.core.IFloatSource;
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

	public static IFloatSource of(final float value) {
		return new IFloatSource() {
			@Override
			public float getFromSource() {
				return value;
			}
		};
	}

	public static IBoolSource of(final boolean value) {
		return new IBoolSource() {
			@Override
			public boolean getFromSource() {
				return value;
			}
		};
	}
	
	public static <T> ISource<T> of(final T value) {
		return new ISource<T>() {
			@Override
			public T getFromSource() {
				return value;
			}
		};
	}
}
