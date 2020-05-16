package com.Game.Engine.gfx;

public class DymanicLight extends Light{

	private float delta, deltaRadius;
	private int maxR, minR;

	public DymanicLight(int radius, int color, float delta, int bound) {
		super(radius, color);
		this.delta = delta;
		minR = radius;
		maxR = radius + bound;
		deltaRadius = radius;
	}

	public void update() {
		if(radius < minR || radius > maxR)
			delta *= -1;
		
		deltaRadius += delta;
		radius = (int) deltaRadius;
	}
	
	@Override
	public int getLightValue(int x, int y) {
		if(x < 0 || x >= diameter || y < 0 || y>=diameter) {
			return 0;
		} 
		int dX = -((int) Math.signum(x - radius/2))*(radius-minR);
		int dY = -((int) Math.signum(y - radius/2))*(radius-minR);
		System.out.println();
		System.out.println(x);
		System.out.println(y);
		System.out.println(dX);
		System.out.println(dY);
		return lm[x+dX+(y+dY)*diameter];
	}
}
