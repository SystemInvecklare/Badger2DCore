package com.github.systeminvecklare.badger.core.util;

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
}
