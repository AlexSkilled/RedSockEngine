package Game.GObjects.Inventory;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.GObjects.AliveObject;
import Game.GObjects.Item;

public class EntityInventory extends Inventory{

	private AliveObject ao;
	
	public EntityInventory(int size, AliveObject ao) {
		this.ao = ao;
		inv = new Item[size];
	}
	
	@Override
	protected void changeHoldingItem(GameContainer gc) {
		
	}

	@Override
	public void useItem(GameContainer gc, GameManager gm, float dt, int destX, int destY) {
		if(inv[inHand] != null)
			inv[inHand].act(gm, destY, destY, destX, destY, gc, ao.getTag());
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
	}

}
