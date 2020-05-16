package com.Game.Enums;

public enum Objects {
	entity("entity"),
	item("item"),
	enemy("enemy"),
	player("player"),
	bullet("bullet"),
	teaMachine("teaMachine");
	
	String name;
	private Objects(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
