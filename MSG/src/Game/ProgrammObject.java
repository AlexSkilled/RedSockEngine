package Game;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

public abstract class ProgrammObject {

	public abstract void update(GameContainer gc, GameManager gm, float dt);
	public abstract void render(GameContainer gc, Renderer r);
	
}
