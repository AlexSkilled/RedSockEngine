package com.Game.Engine.gfx;

import Game.GameManager;

public class Image extends GFX{

	public Image(String path) {
		super(path);

		defaultRes = new int[2];
		defaultRes[0] = w;
		defaultRes[1] = h;
		
		if(defaultRes[0] != GameManager.TS && defaultRes[1] != GameManager.TS) {
			resize();
		}
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
