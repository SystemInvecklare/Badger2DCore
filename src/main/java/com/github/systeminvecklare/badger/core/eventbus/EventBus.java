package com.github.systeminvecklare.badger.core.eventbus;

import java.util.HashMap;
import java.util.Map;

import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleManager;
import com.github.systeminvecklare.badger.core.listeners.IListener;
import com.github.systeminvecklare.badger.core.listeners.ListenerBroadcaster;
import com.github.systeminvecklare.badger.core.listeners.SmartListener;

public class EventBus {
	public static final EventBus MAIN = new EventBus();
	
	private final EventPublishers eventPublishers = new EventPublishers();
	
	public <EVENT> void subscribe(Class<EVENT> eventType, ILifecycleManager lifecycleManager, IListener<EVENT> listener) {
		Publisher<EVENT> publisher = eventPublishers.get(eventType);
		if(publisher == null) {
			publisher = new Publisher<EVENT>();
			eventPublishers.put(eventType, publisher);
		}
		publisher.subscribe(lifecycleManager, listener);
	}
	
	public <EVENT> void publish(Class<EVENT> eventType, EVENT event) {
		Publisher<EVENT> publisher = eventPublishers.get(eventType);
		if(publisher != null) {
			publisher.publish(event);
		}
	}
	
	private static class Publisher<EVENT> {
		private final ListenerBroadcaster<EVENT> broadcaster = new ListenerBroadcaster<EVENT>();
		
		public void subscribe(ILifecycleManager lifecycleManager, IListener<EVENT> listener) {
			SmartListener.attach(lifecycleManager, broadcaster, listener);
		}
		
		public void publish(EVENT event) {
			if(broadcaster.hasListeners()) {
				broadcaster.onEvent(event);
			}
		}
	}
	
	private static class EventPublishers {
		private final Map<Class<?>, Publisher<?>> map = new HashMap<Class<?>, EventBus.Publisher<?>>();
		
		@SuppressWarnings("unchecked")
		public <EVENT> Publisher<EVENT> get(Class<EVENT> eventType) {
			return (Publisher<EVENT>) map.get(eventType);
		}
		
		public <EVENT> void put(Class<EVENT> eventType, Publisher<EVENT> publisher) {
			map.put(eventType, publisher);
		}
	}
}
