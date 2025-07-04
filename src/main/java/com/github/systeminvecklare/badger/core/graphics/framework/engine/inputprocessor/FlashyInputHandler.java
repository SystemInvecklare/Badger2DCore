package com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.IPixelTranslator;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ILoopAction;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ISmartList;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;
import com.github.systeminvecklare.badger.core.pooling.SimplePool;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IPoolableKeyPressEvent;
import com.github.systeminvecklare.badger.core.standard.input.mouse.IPoolableClickEvent;
import com.github.systeminvecklare.badger.core.standard.input.mouse.IPoolableScrollEvent;
import com.github.systeminvecklare.badger.core.standard.input.mouse.PointerIdentifier;

public class FlashyInputHandler implements IInputHandler {
	private ISmartList<IQueuedInput> inputEvents = FlashyEngine.get().newSmartList();
	private IMovieClipCollector<IReadablePosition> hitCollector = new HitCollector(true);
	private Map<PointerIdentifier, IPoolableClickEvent> downMap = new HashMap<PointerIdentifier, IPoolableClickEvent>();
	private IPool<QueuedPress> queuedPressPool = new SimplePool<QueuedPress>(5,10) {
		@Override
		public QueuedPress newObject() {
			return new QueuedPress(this);
		}
	};
	private IPool<QueuedRelease> queuedReleasePool = new SimplePool<QueuedRelease>(5,10) {
		@Override
		public QueuedRelease newObject() {
			return new QueuedRelease(this);
		}
	};
	private IPool<QueuedDrag> queuedDragPool = new SimplePool<QueuedDrag>(20,40) {
		@Override
		public QueuedDrag newObject() {
			return new QueuedDrag(this);
		}
	};
	private IPool<QueuedScroll> queuedScrollPool = new SimplePool<QueuedScroll>(1,5) {
		@Override
		public QueuedScroll newObject() {
			return new QueuedScroll(this);
		}
	};
	
	@SuppressWarnings("rawtypes")
	private IPool<ArrayList> arrayListPool = new SimplePool<ArrayList>(5,10) {
		@Override
		public ArrayList newObject() {
			return new ArrayList(3);
		}
		@Override
		public void free(ArrayList poolable) {
			poolable.clear();
			super.free(poolable);
		}
		@Override
		public ArrayList obtain() {
			ArrayList obtained = super.obtain();
			if(obtained.size() != 0)
			{
				obtained.clear();
			}
			return obtained;
		}
	};
	
	private Map<Integer, IPoolableKeyPressEvent> keyPresses = new HashMap<Integer, IPoolableKeyPressEvent>(); 
	private IPool<QueuedKeyPressEvent> queuedKeyPressPool = new SimplePool<QueuedKeyPressEvent>(5,10) {
		@Override
		public QueuedKeyPressEvent newObject() {
			return new QueuedKeyPressEvent(this);
		}
	};
	private IPool<QueuedKeyRelease> queuedKeyReleasePool = new SimplePool<QueuedKeyRelease>(5,10) {
		@Override
		public QueuedKeyRelease newObject() {
			return new QueuedKeyRelease(this);
		}
	};
	private IPool<QueuedKeyTypedEvent> queuedKeyTypedPool = new SimplePool<QueuedKeyTypedEvent>(10,30) {
		@Override
		public QueuedKeyTypedEvent newObject() {
			return new QueuedKeyTypedEvent(this);
		}
	};
	
	private final IPixelTranslator pixelTranslator;
	private final IWindowCanvas requireInsideOrNull;
	
	public FlashyInputHandler(IPixelTranslator pixelTranslator) {
		this(pixelTranslator, null);
	}
	
	public FlashyInputHandler(IPixelTranslator pixelTranslator, IWindowCanvas requireInsideOrNull) {
		this.pixelTranslator = pixelTranslator;
		this.requireInsideOrNull = requireInsideOrNull;
	}


	@Override
	public boolean registerKeyDown(int keycode) {
		IPoolableKeyPressEvent pressEvent = newKeyPressEvent(keycode);
		inputEvents.addToBirthList(queuedKeyPressPool.obtain().setKeyPressEvent(pressEvent));
		return true;
	}
	
	
	@Override
	public boolean registerKeyUp(int keycode) {
		inputEvents.addToBirthList(queuedKeyReleasePool.obtain().setTo(keycode));
		return true;
	}
	
	@Override
	public boolean registerKeyTyped(char c) {
		inputEvents.addToBirthList(queuedKeyTypedPool.obtain().setTo(c));
		return true;
	}
	
	@Override
	public boolean registerPointerDown(int screenX, int screenY, int pointer, int button) {
		IPoolableClickEvent clickEvent = newClickEvent(screenX, screenY, pointer, button, false);
		if(clickEvent != null) {
			inputEvents.addToBirthList(queuedPressPool.obtain().setEvent(clickEvent));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean registerPointerUp(int screenX, int screenY, int pointer, int button) {
		inputEvents.addToBirthList(queuedReleasePool.obtain().setTo(screenX,screenY,pointer,button));
		return true;
	}
	
	@Override
	public boolean registerPointerDragged(int screenX, int screenY, int pointer) {
		inputEvents.addToBirthList(queuedDragPool.obtain().setTo(screenX,screenY,pointer));
		return true;
	}
	
	@Override
	public boolean registerScrolled(int screenX, int screenY, float amountX, float amountY) {
		inputEvents.addToBirthList(queuedScrollPool.obtain().setTo(screenX, screenY, amountX, amountY));
		return true;
	}

	
	private IPoolableClickEvent newClickEvent(int x, int y, int pointer, int button, boolean neverNull)
	{
		IPoolManager pm = FlashyEngine.get().getPoolManager();
		Position position = pixelTranslator.translate(x, y, pm.getPool(Position.class).obtain());
		try
		{
			if(!neverNull && requireInsideOrNull != null) {
				if(!isInsideTranslated(x, y, requireInsideOrNull, pixelTranslator)) {
					return null;
				}
			}
			return pm.getPool(IPoolableClickEvent.class).obtain().init(position, button, pointer);
		}
		finally
		{
			position.free();
		}
	}
	
	private IPoolableScrollEvent newScrollEvent(int x, int y, float scrollX, float scrollY)
	{
		IPoolManager pm = FlashyEngine.get().getPoolManager();
		Position position = pixelTranslator.translate(x, y, pm.getPool(Position.class).obtain());
		try
		{
			if(requireInsideOrNull != null) {
				if(!isInsideTranslated(x, y, requireInsideOrNull, pixelTranslator)) {
					return null;
				}
			}
			return pm.getPool(IPoolableScrollEvent.class).obtain().init(position, scrollX, scrollY);
		}
		finally
		{
			position.free();
		}
	}
	
	private IPoolableKeyPressEvent newKeyPressEvent(int keycode) {
		return FlashyEngine.get().getPoolManager().getPool(IPoolableKeyPressEvent.class).obtain().init(keycode);
	}
	
	private PointerIdentifier newPointerIdentifier(int pointer, int button)
	{
		return FlashyEngine.get().getPoolManager().getPool(PointerIdentifier.class).obtain().setTo(pointer, button);
	}

	@Override
	public void handleInputs(IScene scene) {
		inputEvents.forEach(new ExecuteQueuedInput(scene));
		inputEvents.clear();
	}
	
	//OLDish hitcollector
//	private static class HitCollector implements ITransformDependentLayerVisitor, ITransformDependentMovieClipVisitor {
//		private QuickArray<IMovieClip> movieClips = new QuickArray<IMovieClip>();
//		private Position clickPos = FlashyEngine.get().getPoolManager().getPool(Position.class).obtain();
//		private ITransform currentTransform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
//
//		@Override
//		public ITransform transform() {
//			return currentTransform;
//		}
//		
//		@Override
//		public void visit(ILayer layer) {
//			layer.visitChildrenMovieClips(this);
//		}
//		
//		@Override
//		public void visit(IMovieClip movieClip) {
//			IPool<Position> positionPool = FlashyEngine.get().getPoolManager().getPool(Position.class);
//			ITransform tempTrans = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
//			try
//			{
//				Position localPos = positionPool.obtain().setTo(clickPos);
//				try
//				{
//					tempTrans.setTo(currentTransform).invert().transform(localPos);
//					
//					boolean hit = movieClip.hitTest(localPos);
//					if(hit)
//					{
//						//(the order will be reversed in the end)
//						movieClips.add(movieClip);
//						movieClip.visitChildrenMovieClips(this);
//					}
//				}
//				finally
//				{
//					localPos.free();
//				}
//			}
//			finally
//			{
//				tempTrans.free();
//			}
//		}
//
//		public HitCollector reset(IReadablePosition position) {
//			movieClips.clear();
//			this.clickPos.setTo(position);
//			this.currentTransform.setToIdentity();
//			return this;
//		}
//	}
	
	private static interface IQueuedInput extends IPoolable {
		public void execute(IScene scene);
	}
	
	private static abstract class AbstractQueuedInput<SELF extends AbstractQueuedInput<?>> implements IQueuedInput {
		private final IPool<SELF> pool;
		
		public AbstractQueuedInput(IPool<SELF> pool) {
			this.pool = pool;
		}

		@Override
		public void free() {
			pool.free(self());
		}

		@SuppressWarnings("unchecked")
		protected SELF self() {
			return (SELF) this;
		}		
	}
	
	private class QueuedPress extends AbstractQueuedInput<QueuedPress> {
		private IPoolableClickEvent clickEvent;
		
		public QueuedPress(IPool<QueuedPress> pool) {
			super(pool);
		}

		@Override
		public void free() {
			this.clickEvent = null;
			super.free();
		}

		public QueuedPress setEvent(IPoolableClickEvent event) {
			this.clickEvent = event;
			return this;
		}

		@Override
		public void execute(IScene scene) {
			//PointerIdentifier freed in QueuedRelease::execute
			PointerIdentifier pointerIdentifier = newPointerIdentifier(clickEvent.getPointerID(), clickEvent.getButton());
			IPoolableClickEvent existingClickEvent = downMap.put(pointerIdentifier, clickEvent);
			if(existingClickEvent != null) {
				clickEvent.inheritListeners(existingClickEvent);
				existingClickEvent.free(); //Need to free it here, otherwise it will never be free. The old pointer identifier is however lost forever. But it's ok. Happens rarely. Actually, happens if you begin click, turn off mouse, release, turn mouse back on.
			}
			scene.visitLayers(hitCollector.reset(clickEvent.getPosition()));
			hitCollector.doClick(clickEvent);
		}
	}
	
	private class QueuedRelease extends AbstractQueuedInput<QueuedRelease> {
		private int screenX;
		private int screenY;
		private int pointer;
		private int button;
		
		public QueuedRelease(IPool<QueuedRelease> pool) {
			super(pool);
		}

		public QueuedRelease setTo(int screenX, int screenY, int pointer, int button) {
			this.screenX = screenX;
			this.screenY = screenY;
			this.pointer = pointer;
			this.button = button;
			return this;
		}

		@Override
		public void execute(IScene scene) {
			PointerIdentifier temp = newPointerIdentifier(pointer, button);
			try
			{
				IPoolableClickEvent event = downMap.get(temp);
				if(event != null)
				{
					IPoolableClickEvent release = newClickEvent(screenX, screenY, pointer, button, true);
					try
					{
						event.fireRelease(release);
						event.free();
					}
					finally
					{
						release.free();
					}
					Iterator<Entry<PointerIdentifier, IPoolableClickEvent>> downMapIt = downMap.entrySet().iterator();
					while(downMapIt.hasNext())
					{
						Entry<PointerIdentifier, IPoolableClickEvent> entry = downMapIt.next();
						if(entry.getKey().equals(temp))
						{
							entry.getKey().free();
							downMapIt.remove();
						}
					}
				}
			}
			finally
			{
				temp.free();
			}
		}
	}
	
	private class QueuedDrag extends AbstractQueuedInput<QueuedDrag> {
		private int screenX;
		private int screenY;
		private int pointer;

		public QueuedDrag(IPool<QueuedDrag> pool) {
			super(pool);
		}

		public QueuedDrag setTo(int screenX, int screenY, int pointer) {
			this.screenX = screenX;
			this.screenY = screenY;
			this.pointer = pointer;
			return this;
		}

		@Override
		public void execute(IScene scene) {
			@SuppressWarnings("unchecked")
			ArrayList<IPoolableClickEvent> events = arrayListPool.obtain();
			try
			{
				for(Entry<PointerIdentifier, IPoolableClickEvent> downEntry : downMap.entrySet())
				{
					if(downEntry.getKey().getPointer() == pointer)
					{
						events.add(downEntry.getValue());
					}
				}
				
				for(IPoolableClickEvent event : events)
				{
					IPoolableClickEvent dragEvent = newClickEvent(screenX, screenY, pointer, event.getButton(), true);
					try
					{
						event.fireDrag(dragEvent);
					}
					finally
					{
						dragEvent.free();
					}
				}
			}
			finally
			{
				arrayListPool.free(events);
			}
		}
	}
	
	private class QueuedKeyPressEvent extends AbstractQueuedInput<QueuedKeyPressEvent> {
		private IPoolableKeyPressEvent pressEvent;

		public QueuedKeyPressEvent(IPool<QueuedKeyPressEvent> pool) {
			super(pool);
		}

		public QueuedKeyPressEvent setKeyPressEvent(IPoolableKeyPressEvent pressEvent) {
			this.pressEvent = pressEvent;
			return this;
		}

		@Override
		public void execute(IScene scene) {
			keyPresses.put(pressEvent.getKeyCode(), pressEvent);
			scene.onKeyPress(pressEvent);
		}
	}
	
	private class QueuedKeyRelease extends AbstractQueuedInput<QueuedKeyRelease> {
		private int keycode;

		public QueuedKeyRelease(IPool<QueuedKeyRelease> pool) {
			super(pool);
		}

		public QueuedKeyRelease setTo(int keycode) {
			this.keycode = keycode;
			return this;
		}

		@Override
		public void execute(IScene scene) {
			Iterator<Entry<Integer, IPoolableKeyPressEvent>> keyPressIt = keyPresses.entrySet().iterator();
			while(keyPressIt.hasNext())
			{
				Entry<Integer, IPoolableKeyPressEvent> entry = keyPressIt.next();
				if(entry.getKey().intValue() == keycode)
				{
					IPoolableKeyPressEvent event = entry.getValue();
					if(event != null)
					{
							try
							{
								event.fireRelease(event);
							}
							finally
							{
								event.free();
							}
					}
					keyPressIt.remove();
				}
			}
		}
	}
	
	private class QueuedKeyTypedEvent extends AbstractQueuedInput<QueuedKeyTypedEvent> {
		private char c;

		public QueuedKeyTypedEvent(IPool<QueuedKeyTypedEvent> pool) {
			super(pool);
		}

		public QueuedKeyTypedEvent setTo(char c) {
			this.c = c;
			return this;
		}

		@Override
		public void execute(IScene scene) {
			scene.onKeyTyped(c);
		}
	}
	
	private class QueuedScroll extends AbstractQueuedInput<QueuedScroll> {
		private int screenX;
		private int screenY;
		private float scrollX;
		private float scrollY;

		public QueuedScroll(IPool<QueuedScroll> pool) {
			super(pool);
		}

		public QueuedScroll setTo(int screenX, int screenY, float scrollX, float scrollY) {
			this.screenX = screenX;
			this.screenY = screenY;
			this.scrollX = scrollX;
			this.scrollY = scrollY;
			return this;
		}

		@Override
		public void execute(IScene scene) {
			IPoolableScrollEvent scrollEvent = newScrollEvent(screenX, screenY, scrollX, scrollY);
			if(scrollEvent != null) {
				scene.visitLayers(hitCollector.reset(scrollEvent.getPosition()));
				hitCollector.doScroll(scrollEvent);
				scrollEvent.free();
			}
		}
	}
	
	
	private static class ExecuteQueuedInput implements ILoopAction<IQueuedInput> {
		private final IScene scene;

		public ExecuteQueuedInput(IScene scene) {
			this.scene = scene;
		}

		@Override
		public boolean onIteration(IQueuedInput queuedInput) {
			queuedInput.execute(scene);
			queuedInput.free();
			return true;
		}
	}
	
	public static boolean isInsideTranslated(int pixelX, int pixelY, IWindowCanvas windowCanvas, IPixelTranslator pixelTranslator) {
		IPool<Position> positionPool = FlashyEngine.get().getPoolManager().getPool(Position.class);
		Position translatedZero = positionPool.obtain();
		Position translatedOne = positionPool.obtain();
		try {
			translatedZero = pixelTranslator.translate(0, 0, translatedZero);
			translatedOne = pixelTranslator.translate(1, 1, translatedOne);
			
			float alphaX = translatedZero.getX();
			float betaX = translatedOne.getX() - alphaX;
			
			float xMin = -alphaX/betaX;
			float xMax = (windowCanvas.getWidth() - alphaX)/betaX;
			
			float alphaY = translatedZero.getY();
			float betaY = translatedOne.getY() - alphaY;
			
			float yMin = (windowCanvas.getHeight() - alphaY)/betaY;
			float yMax = -alphaY/betaY;
			
			return pixelX >= xMin && pixelY >= yMin && pixelX < xMax && pixelY < yMax;
		} finally {
			translatedZero.free();
			translatedOne.free();
		}
	}
}
