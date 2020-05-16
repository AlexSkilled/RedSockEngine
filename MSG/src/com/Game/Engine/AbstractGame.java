package com.Game.Engine;

public abstract class AbstractGame {
	//интерфейс обновления и отрисовки игры
	//да, я знаю что можно сделать интерфейс не классом, а именно интерфейсом, спасибо.
	public abstract void init(GameContainer gc);
	public abstract void update(GameContainer gc, float dt);
	public abstract void render(GameContainer gc, Renderer r);
	
	
}
