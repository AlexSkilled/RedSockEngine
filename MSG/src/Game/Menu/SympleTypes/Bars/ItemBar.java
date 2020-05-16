package Game.Menu.SympleTypes.Bars;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.GObjects.AliveObject;
import Game.GObjects.Item;

public class ItemBar extends HPbar{

	private Item item;
	
	public ItemBar(int colorLeft, int colorRight, int max, int current, AliveObject go, Item item) {
		super(colorLeft, colorRight, max, current, go);
		this.item = item;
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		current = 1 - (double) item.getDurability()/max;
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
	}
	
	
}
