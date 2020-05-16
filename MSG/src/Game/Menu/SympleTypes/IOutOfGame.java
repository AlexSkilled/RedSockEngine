package Game.Menu.SympleTypes;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;

public interface IOutOfGame{

	void update(GameContainer gc, GameManager gm);
	void render(GameContainer gc, Renderer r);
}
