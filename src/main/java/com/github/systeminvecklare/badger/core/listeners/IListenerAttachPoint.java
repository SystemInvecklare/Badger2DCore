package com.github.systeminvecklare.badger.core.listeners;

public interface IListenerAttachPoint<EVENT> {
	void addListener(IListener<? super EVENT> listener);
	void removeListener(IListener<? super EVENT> listener);
}
