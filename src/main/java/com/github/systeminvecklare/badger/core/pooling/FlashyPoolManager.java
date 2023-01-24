package com.github.systeminvecklare.badger.core.pooling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.Transform;
import com.github.systeminvecklare.badger.core.graphics.components.util.PoolableIterable;
import com.github.systeminvecklare.badger.core.math.DeltaRotation;
import com.github.systeminvecklare.badger.core.math.Matrix2x2;
import com.github.systeminvecklare.badger.core.math.Matrix3x3;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.math.Rotation;
import com.github.systeminvecklare.badger.core.math.Vector;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IPoolableKeyPressEvent;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.KeyPressEvent;
import com.github.systeminvecklare.badger.core.standard.input.mouse.ClickEvent;
import com.github.systeminvecklare.badger.core.standard.input.mouse.IPoolableClickEvent;
import com.github.systeminvecklare.badger.core.standard.input.mouse.PointerIdentifier;
import com.github.systeminvecklare.badger.core.util.PoolableArrayOf16Floats;


public class FlashyPoolManager implements IPoolManager {
	@SuppressWarnings("rawtypes")
	private Map<Class, IPool> poolMap = new HashMap<Class, IPool>();
	
	@SuppressWarnings("rawtypes")
	public FlashyPoolManager() {
		registerPool(ITransform.class, newPool(0,getPoolSizeForType(ITransform.class),new IFactory<ITransform>() 
				{
					@Override
					public ITransform newObject(IPool<ITransform> pool) {
						return new Transform(pool);
					}
				}));
		registerPool(Position.class, newPool(0,getPoolSizeForType(Position.class),new IFactory<Position>() 
				{
					@Override
					public Position newObject(IPool<Position> pool) {
						return new Position(pool);
					}
				}));
		registerPool(Vector.class, newPool(0,getPoolSizeForType(Vector.class), new IFactory<Vector>()
				{
					@Override
					public Vector newObject(IPool<Vector> pool) {
						return new Vector(pool);
					}
				}));
		registerPool(Rotation.class, newPool(0,getPoolSizeForType(Rotation.class),new IFactory<Rotation>()
				{
					@Override
					public Rotation newObject(IPool<Rotation> pool) {
						return new Rotation(pool);
					}
				}));
		registerPool(DeltaRotation.class, newPool(0,getPoolSizeForType(DeltaRotation.class),new IFactory<DeltaRotation>()
				{
					@Override
					public DeltaRotation newObject(IPool<DeltaRotation> pool) {
						return new DeltaRotation(pool);
					}
				}));
		registerPool(Matrix3x3.class, newPool(0, getPoolSizeForType(Matrix3x3.class), new IFactory<Matrix3x3>() {
			@Override
			public Matrix3x3 newObject(IPool<Matrix3x3> pool) {
				return new Matrix3x3(pool);
			}
		}));
		registerPool(Matrix2x2.class, newPool(0, getPoolSizeForType(Matrix2x2.class), new IFactory<Matrix2x2>() {
			@Override
			public Matrix2x2 newObject(IPool<Matrix2x2> pool) {
				return new Matrix2x2(pool);
			}
		}));
		registerPool(PoolableIterable.class, newPool(0,getPoolSizeForType(PoolableIterable.class),new IFactory<PoolableIterable>()
				{
					@SuppressWarnings("unchecked")
					@Override
					public PoolableIterable newObject(IPool<PoolableIterable> pool) {
						return new PoolableIterable(pool);
					}
				}));
		registerPool(EasyPooler.class, EasyPooler.newEasyPoolerPool(3,getPoolSizeForType(EasyPooler.class)));
		registerPool(ArrayList.class, new SimplePool<ArrayList>(0, getPoolSizeForType(ArrayList.class)) {
			@Override
			public ArrayList newObject() {
				return new ArrayList();
			}
			
			@Override
			public ArrayList obtain() {
				ArrayList arrayList = super.obtain();
				if(arrayList != null && !arrayList.isEmpty()) {
					arrayList.clear();
				}
				return arrayList;
			}
		});
		registerPool(IPoolableClickEvent.class, new SimplePool<IPoolableClickEvent>(10,20) {
			@Override
			public IPoolableClickEvent newObject() {
				return new ClickEvent(this);
			}
		});
		registerPool(PointerIdentifier.class, new SimplePool<PointerIdentifier>(10,10) {
			@Override
			public PointerIdentifier newObject() {
				return new PointerIdentifier(this);
			}
		});
		registerPool(IPoolableKeyPressEvent.class, new SimplePool<IPoolableKeyPressEvent>(5,20) {
			@Override
			public IPoolableKeyPressEvent newObject() {
				return new KeyPressEvent(this);
			}
		});
		registerPool(PoolableArrayOf16Floats.class, new SimplePool<PoolableArrayOf16Floats>(5, 10) {
			@Override
			public PoolableArrayOf16Floats newObject() {
				return new PoolableArrayOf16Floats(this);
			}
		});
	}
	
	protected int getPoolSizeForType(Class<?> type) {
		return 30;
	}

	public <T> void registerPool(Class<T> type, IPool<T> pool)
	{
		this.poolMap.put(type, pool);
	}
	
	protected <T> IPool<T> newPool(int initCapacity,int maxSize, final IFactory<T> factory)
	{
		return new SimplePool<T>(initCapacity,maxSize) {
			@Override
			public T newObject() {
				return factory.newObject(this); //this == the SimplePool
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> IPool<T> getPool(Class<T> poolableType) {
		return poolMap.get(poolableType);
	}
	
	public static interface IFactory<T> {
		public T newObject(IPool<T> pool);
	}
}
