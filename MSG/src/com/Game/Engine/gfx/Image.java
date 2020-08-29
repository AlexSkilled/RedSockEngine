package com.Game.Engine.gfx;

public class Image extends GFX{

	public Image(String path) {
		super(path);

		defaultRes = new int[2];
		defaultRes[0] = w;
		defaultRes[1] = h;
	}
	
	public Image(String path, int w, int h) {
		super(path);
		defaultRes = new int[2];
		defaultRes[0] = w;
		defaultRes[1] = h;
	}
	
	public Image(int[] p, int w, int h) {
		super(p, w, h);
	}
}
