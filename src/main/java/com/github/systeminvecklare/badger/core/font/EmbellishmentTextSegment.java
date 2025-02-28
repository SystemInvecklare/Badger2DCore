package com.github.systeminvecklare.badger.core.font;

public class EmbellishmentTextSegment {
	public final TransformedFlashyText transformed;
	public final IEmbellishmentTag embellishmentTag;
	
	public EmbellishmentTextSegment(TransformedFlashyText transformed, IEmbellishmentTag embellishmentTag) {
		this.transformed = transformed;
		this.embellishmentTag = embellishmentTag;
	}
}
