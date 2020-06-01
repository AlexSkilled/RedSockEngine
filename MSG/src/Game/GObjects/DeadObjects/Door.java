package Game.GObjects.DeadObjects;

import com.Game.Engine.GameContainer;
import com.Game.Enums.Levels;

import Game.GameManager;
import Game.GObjects.Entity;

public class Door extends Entity{

	private Levels levelTo;
	public Door(int tileX, int tileY, Levels levelName) {
		super(21, tileX, tileY);
		levelTo = levelName;
	}
	@Override
	public String toString() {
		return super.toString() + " " + levelTo;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
	}

}
