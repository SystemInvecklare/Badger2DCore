package com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor;

import com.github.systeminvecklare.badger.core.graphics.components.core.IIntSource;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.IPixelTranslator;
import com.github.systeminvecklare.badger.core.math.Position;

/**
 * Use this where you previously sent in IIntSournce but that now needs a IPixelTranslator.
 * 
 * @author Mattias Selin
 */
public class HeightSourcePixelTranslator implements IPixelTranslator {
	private final IIntSource screenHeightSource;
	
	public HeightSourcePixelTranslator(IIntSource screenHeightSource) {
		this.screenHeightSource = screenHeightSource;
	}

	@Override
	public Position translate(int x, int y, Position result) {
		if(screenHeightSource != null) {
			return result.setTo(x, screenHeightSource.getFromSource() - y);
		} else {
			return result.setTo(x, y);
		}
	}
}
