package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.ISource;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.IReadableVector;

public class OffsetBehavior extends Behavior {
	@Override
	public ITransform getTransform(IReadableTransform transform, ITransform result) {
		return result.setTo(transform).addToPosition(getOffsetX(), getOffsetY());
	}

	public float getOffsetY() {
		return 0;
	}

	public float getOffsetX() {
		return 0;
	}
	
	public static OffsetBehavior liveOffset(final IReadablePosition position) {
		return new OffsetBehavior() {
			@Override
			public float getOffsetX() {
				return position.getX();
			}
			
			@Override
			public float getOffsetY() {
				return position.getY();
			}
		};
	}
	
	public static OffsetBehavior livePositionSourceOffset(final ISource<IReadablePosition> positionSource) {
		return new OffsetBehavior() {
			@Override
			public float getOffsetX() {
				return positionSource.getFromSource().getX();
			}
			
			@Override
			public float getOffsetY() {
				return positionSource.getFromSource().getY();
			}
		};
	}
	
	public static OffsetBehavior liveOffset(final IReadableVector vector) {
		return new OffsetBehavior() {
			@Override
			public float getOffsetX() {
				return vector.getX();
			}
			
			@Override
			public float getOffsetY() {
				return vector.getY();
			}
		};
	}
	
	public static OffsetBehavior liveVectorSourceOffset(final ISource<IReadableVector> vectorSource) {
		return new OffsetBehavior() {
			@Override
			public float getOffsetX() {
				return vectorSource.getFromSource().getX();
			}
			
			@Override
			public float getOffsetY() {
				return vectorSource.getFromSource().getY();
			}
		};
	}
}
