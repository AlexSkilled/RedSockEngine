package Game.GObjects.DeadObjects.Blocks;

import com.Game.Engine.GameContainer;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GObjects.Item;

public class Block extends Item{

	
	public Block(int itemID) {
		super(itemID);
	}

	@Override
	public int getDurability() {
		return 0;
	}

	@Override
	public int getMaxDurability() {
		return 0;
	}

	@Override
	protected void setItem(int ItemID) {
		

	}

	@Override
	protected void setDurability(float durability) {
	
	}

	@Override
	public void act(GameManager gm, int tileX, int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
			gm.setBlock(destX, destY, ID-100);
	}

}
