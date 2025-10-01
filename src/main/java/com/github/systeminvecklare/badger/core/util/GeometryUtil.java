package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.NonInvertibleMatrixException;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.math.Vector;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.widget.IRectangle;

public class GeometryUtil {
	public static boolean isInRectangle(float px, float py, float x, float y, float width,float height)
	{
		return px >= x && py >= y && px < x+width && py < y+height;
	}
	
	public static boolean isInRectangle(double px, double py, double x, double y, double width,double height)
	{
		return px >= x && py >= y && px < x+width && py < y+height;
	}
	
	public static boolean isInRectangle(float px, float py, IRectangle rectangle) {
		return isInRectangle(px, py, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	
	public static boolean isInRectangle(float px, float py, IFloatRectangle rectangle) {
		return isInRectangle(px, py, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	public static boolean isInCircle(float px, float py, float x, float y, float radius)
	{
		float dx = px-x;
		float dy = py-y;
		return dx*dx + dy*dy < radius*radius;
	}
	
	public static boolean isInCircle(double px, double py, double x, double y, double radius)
	{
		double dx = px-x;
		double dy = py-y;
		return dx*dx + dy*dy < radius*radius;
	}
	
	public static boolean isInOval(float px, float py, float x, float y, float radiusX, float radiusY) {
		float dx = (px-x)/radiusX;
		float dy = (py-y)/radiusY;
		return dx*dx + dy*dy < 1f;
	}

	public static boolean isInBeam(float px, float py, IReadablePosition beamStart, IReadablePosition beamEnd, float beamThickness, EasyPooler ep) {
		ITransform transform = ep.obtain(ITransform.class).setToIdentity().setRotation(beamStart.vectorTo(beamEnd, ep.obtain(Vector.class)).getRotationTheta()).setPosition(beamStart);
		try {
			transform.invert();
		} catch (NonInvertibleMatrixException e) {
			return false;
		}
		
		Position transformedPosition = ep.obtain(Position.class).setTo(px, py);
		transform.transform(transformedPosition);
		
		float width = Math.round(beamStart.distance(beamEnd));
		return GeometryUtil.isInRectangle(transformedPosition.getX(), transformedPosition.getY(), 0, -beamThickness/2, width, beamThickness);
	}
}
