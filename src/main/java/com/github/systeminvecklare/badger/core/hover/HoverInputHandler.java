package com.github.systeminvecklare.badger.core.hover;

import com.github.systeminvecklare.badger.core.graphics.components.core.IIntSource;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor.FlashyInputHandler;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;

public abstract class HoverInputHandler extends FlashyInputHandler {
	private final HoverCollector hoverCollector;

	public HoverInputHandler(IIntSource heightSource) {
		super(heightSource);
		this.hoverCollector = new HoverCollector();
	}
	
	/**
	 * @return null if not currently hovering (for example on mobile device)
	 */
	public abstract Position getHoverPosition(EasyPooler ep);

	@Override
	public void handleInputs(IScene scene) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			Position hoverPosition = getHoverPosition(ep);
			hoverCollector.beginHover(hoverPosition.getX(), hoverPosition.getY());
		} finally {
			ep.freeAllAndSelf();
		}
		scene.visitLayers(hoverCollector);
		hoverCollector.endHover();
		super.handleInputs(scene);
	}
}
