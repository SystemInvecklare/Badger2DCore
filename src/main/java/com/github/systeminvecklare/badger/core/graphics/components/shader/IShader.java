package com.github.systeminvecklare.badger.core.graphics.components.shader;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;

public interface IShader {
	public String getName();
	public void onBind(IDrawCycle drawCycle);
	public IShader bindUniformf(String name, IUniformFloat var);
	public IShader bindUniformi(String name, IUniformInteger var);
	public IShader bindUniform2fv(String name, IUniformVector2 var);
	public IShader bindUniformfArray(String name, int length ,IUniformFloatArray var);
	public IShader bindUniform2fvArray(String name, int length, IUniformVector2 var);
	
	
	public static interface IUniformFloat {
		public float getValue();
	}
	
	public static interface IUniformInteger {
		public int getValue();
	}
	
	public static interface IUniformVector2 {
		public float[] getVec2(float[] result);
	}
	
	public static interface IUniformFloatArray {
		public float[] getArray(float[] result);
	}
}
