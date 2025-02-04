package com.github.systeminvecklare.badger.core.graphics.framework.engine;

import com.github.systeminvecklare.badger.core.math.Position;

/**
 * Translates physical pixels in the window to Position coordinates.
 * 
 * @author Mattias Selin
 */
public interface IPixelTranslator {
	Position translate(int x, int y, Position result);
}
