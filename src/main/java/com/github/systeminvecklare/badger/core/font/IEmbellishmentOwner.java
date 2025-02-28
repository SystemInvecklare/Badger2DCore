package com.github.systeminvecklare.badger.core.font;

import java.util.List;
import java.util.function.Supplier;

public interface IEmbellishmentOwner {
	List<EmbellishmentTextSegment> getEmbellishments(List<EmbellishmentTextSegment> result, Supplier<TransformedFlashyText> transfromedTextSupplier);
}
