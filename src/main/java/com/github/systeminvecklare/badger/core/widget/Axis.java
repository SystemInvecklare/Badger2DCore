package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.IReadableVector;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.math.Vector;

public enum Axis {
	X,
	Y;

	public <T> void setCoordinate(T coordinated, ICoordinatedInterface<? super T> coordinatedInterface, float value) {
		switch (this) {
			case X: {
				coordinatedInterface.setX(coordinated, value);
			} break;
			case Y: {
				coordinatedInterface.setY(coordinated, value);
			} break;
		}
	}
	
	public void setCoordinate(Position position, float value) {
		setCoordinate(position, POSITION_INTERFACE, value);
	}
	
	public void setCoordinate(Vector vector, float value) {
		setCoordinate(vector, VECTOR_INTERFACE, value);
	}
	
	public void setCoordinate(ITransform transform, float value) {
		setCoordinate(transform, TRANSFORM_INTERFACE, value);
	}

	public float pick(float x, float y) {
		switch (this) {
			case X: return x;
			case Y: return y;
		}
		throw new RuntimeException();
	}
	
	public int pick(int x, int y) {
		switch (this) {
			case X: return x;
			case Y: return y;
		}
		throw new RuntimeException();
	}
	
	public int getPosition(IRectangle rectangle) {
		switch (this) {
			case X: return rectangle.getX();
			case Y: return rectangle.getY();
		}
		throw new RuntimeException();
	}
	
	public int getLength(IRectangle rectangle) {
		switch (this) {
			case X: return rectangle.getWidth();
			case Y: return rectangle.getHeight();
		}
		throw new RuntimeException();
	}

	public <T> float pick(T object, IReadableCoordinatedInterface<? super T> readableInterface) {
		switch (this) {
			case X: return readableInterface.getX(object);
			case Y: return readableInterface.getY(object);
		}
		throw new RuntimeException();
	}
	
	public float pick(IReadablePosition position) {
		return pick(position, READABLE_POSITION_INTERFACE);
	}
	
	public float pick(IReadableVector vector) {
		return pick(vector, READABLE_VECTOR_INTERFACE);
	}
	
	public float pick(IReadableTransform transform) {
		return pick(transform, READABLE_TRANSFORM_INTERFACE);
	}

	public Axis other() {
		switch (this) {
			case X: return Y;
			case Y: return X;
		}
		throw new RuntimeException();
	}
	
	public interface IReadableCoordinatedInterface<T> {
		float getX(T object);
		float getY(T object);
	}
	
	public interface ICoordinatedInterfaceExtension<T> {
		void setX(T object, float x);
		void setY(T object, float y);
	}
	
	public interface ICoordinatedInterface<T> extends IReadableCoordinatedInterface<T>, ICoordinatedInterfaceExtension<T> {
	}
	
	public static final IReadableCoordinatedInterface<IReadablePosition> READABLE_POSITION_INTERFACE = new IReadableCoordinatedInterface<IReadablePosition>() {
		@Override
		public float getX(IReadablePosition object) {
			return object.getX();
		}

		@Override
		public float getY(IReadablePosition object) {
			return object.getY();
		}
	};
	
	public static final ICoordinatedInterface<Position> POSITION_INTERFACE = extend(READABLE_POSITION_INTERFACE, new ICoordinatedInterfaceExtension<Position>() {
		@Override
		public void setX(Position object, float x) {
			object.setTo(x, object.getY());
		}
		
		@Override
		public void setY(Position object, float y) {
			object.setTo(object.getX(), y);
		}
	});
	
	public static final IReadableCoordinatedInterface<IReadableVector> READABLE_VECTOR_INTERFACE = new IReadableCoordinatedInterface<IReadableVector>() {
		@Override
		public float getX(IReadableVector object) {
			return object.getX();
		}

		@Override
		public float getY(IReadableVector object) {
			return object.getY();
		}
	};
	
	public static final ICoordinatedInterface<Vector> VECTOR_INTERFACE = extend(READABLE_VECTOR_INTERFACE, new ICoordinatedInterfaceExtension<Vector>() {
		@Override
		public void setX(Vector object, float x) {
			object.setTo(x, object.getY());
		}
		
		@Override
		public void setY(Vector object, float y) {
			object.setTo(object.getX(), y);
		}
	});
	
	public static final IReadableCoordinatedInterface<IReadableTransform> READABLE_TRANSFORM_INTERFACE = new IReadableCoordinatedInterface<IReadableTransform>() {
		@Override
		public float getX(IReadableTransform object) {
			return object.getPosition().getX();
		}

		@Override
		public float getY(IReadableTransform object) {
			return object.getPosition().getY();
		}
	};
	
	public static final ICoordinatedInterface<ITransform> TRANSFORM_INTERFACE = extend(READABLE_TRANSFORM_INTERFACE, new ICoordinatedInterfaceExtension<ITransform>() {
		@Override
		public void setX(ITransform object, float x) {
			object.setPosition(x, object.getPosition().getY());
		}
		
		@Override
		public void setY(ITransform object, float y) {
			object.setPosition(object.getPosition().getX(), y);
		}
	});
	
	private static <T> ICoordinatedInterface<T> extend(final IReadableCoordinatedInterface<? super T> readable, final ICoordinatedInterfaceExtension<T> extension) {
		return new ICoordinatedInterface<T>() {
			@Override
			public float getX(T object) {
				return readable.getX(object);
			}

			@Override
			public float getY(T object) {
				return readable.getY(object);
			}

			@Override
			public void setX(T object, float x) {
				extension.setX(object, x);
			}

			@Override
			public void setY(T object, float y) {
				extension.setY(object, y);
			}
		};
	}
}
