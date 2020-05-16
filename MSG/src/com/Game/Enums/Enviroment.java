package com.Game.Enums;

public enum Enviroment {
	wall(true),	
	floor(false);
	boolean collide;
	int color;
	
	Enviroment(boolean collide) {
		this.collide = collide;
	}
	
	public boolean isCollide() {
		return collide;
	}
	
	public int getID() {
		return color;
	}
}
