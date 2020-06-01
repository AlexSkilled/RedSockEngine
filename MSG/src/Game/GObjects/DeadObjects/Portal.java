package Game.GObjects.DeadObjects;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Enums.Levels;

import Game.GameManager;
import Game.GameObject;

public class Portal extends GameObject {
	
	private Levels level;
	
	public Portal(int tileX, int tileY, Levels level) {
		setTileX(tileX);
		setTileY(tileY);
		
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(gm.isCollide("player", posX, posY)) {
			gm.changeLevel(level);
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
	}

}
