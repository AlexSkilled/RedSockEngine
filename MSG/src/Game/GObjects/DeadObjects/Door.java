package Game.GObjects.DeadObjects;

import com.Game.Engine.GameContainer;
import com.Game.Enums.Levels;

import Game.GameManager;
import Game.GObjects.Entity;

public class Door extends Entity{

	private Levels levelTo;
	private int dX, dY; //direction from door where the player should be dropped after loading
	public Door(int tileX, int tileY, Levels levelName, int dX, int dY) {
		super(21, tileX, tileY);
		this.dX = dX;
		this.dY = dY;
		levelTo = levelName;
	}
	@Override
	public String toString() {
		return super.toString() + " " + levelTo + " " + dX +" " + dY;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(gm.isCollide("player", posX, posY)) {
			gm.changeLevel(levelTo, dX, dY);
		}
	}

}
