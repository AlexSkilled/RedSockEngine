package com.Game.Engine.gfx;

public class ImageRequest {
	
	public GFX image;
	public int zDepth;
	public int offX, offY;
	
	public ImageRequest(GFX image, int zDepth, int offX, int offY) {
		this.image = image;
		this.zDepth = zDepth;
		this.offX = offX;
		this.offY = offY;
	}
}
