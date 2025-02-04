package com.github.systeminvecklare.badger.core.hover;

import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.IPixelTranslator;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor.FlashyInputHandler;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor.IWindowCanvas;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;

public abstract class HoverInputHandler extends FlashyInputHandler {
	private final HoverCollector hoverCollector;

	public HoverInputHandler(IPixelTranslator pixelTranslator, IWindowCanvas requireInsideOrNull) {
		super(pixelTranslator, requireInsideOrNull);
		this.hoverCollector = new HoverCollector();
	}
	
	/**
	 * @return null if not currently hovering (for example on mobile device)
	 */
	public abstract Position getHoverPosition(EasyPooler ep);

	@Override
	public void handleInputs(IScene scene) {
		boolean hovering = false;
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			Position hoverPosition = getHoverPosition(ep);
			if(hoverPosition != null) {
				hovering = true;
				hoverCollector.beginHover(hoverPosition.getX(), hoverPosition.getY());
			}
		} finally {
			ep.freeAllAndSelf();
		}
		if(hovering) {
			scene.visitLayers(hoverCollector);
			hoverCollector.endHover();
		}
		super.handleInputs(scene);
	}
}
