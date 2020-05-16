package Game.Menu.SympleTypes.Bars;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.GameObject;
import Game.GObjects.AliveObject;

public class HPbar extends GameObject{

	private int colorLeft, colorRight;
	protected int max;
	protected double current;
	private AliveObject go;
	
	public HPbar(int colorLeft, int colorRight, int max, int current, AliveObject go) {
		this.colorLeft = colorLeft;
		this.colorRight = colorRight;
		this.max = max;
		this.go = go;
		this.max = max;
		this.current = (double) current/max;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		current = (double) go.getHP() / max;
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect((int) go.getPosX(), (int) go.getPosY() - GameManager.TS/6, GameManager.TS, GameManager.TS/6,
				colorRight);
		r.drawFillRect((int) go.getPosX(), (int) go.getPosY() - GameManager.TS/6,
				(int) (GameManager.TS*current), (int) (GameManager.TS/6) ,colorLeft);
	}
	
	
}
