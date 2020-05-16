package Game.GObjects.DeadObjects.subObjects;

import com.Game.Engine.GameContainer;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GObjects.Item;

public class Ammo extends Item{

	private int maxAmount, amount;
	
	private AmmoName ammo;
	
	public Ammo(int itemID) {
		super(itemID);
		setItem(itemID);
	}

	public Ammo(int itemID, int tileX, int tileY) {
		super(itemID, tileX, tileY);
		setItem(itemID);
	}
	
	public Ammo(int itemID, int amount) {
		super(itemID);
		setItem(itemID);
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		String line = super.toString();
		line += " " + amount;
		return line;
	}
	
	public int loadAmmo(int ammo) {
		int need = this.ammo.getMax() - ammo;
		
		amount -= need;
		
		if(amount < 0) {
			need = amount+need;
			amount = 0;
		}

		return need+ammo;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
	}
	
	@Override
	public int getDurability() {
		return amount;
	}

	@Override
	public int getMaxDurability() {
		return maxAmount;
	}

	@Override
	protected void setDurability(float durability) {
		amount = (int) durability;
	}
	
	@Override
	protected void setItem(int ItemID) {
		switch(ItemID) {
		case 11:
			ammo = AmmoName.PISTOL;
			break;
		case 12:
			ammo = AmmoName.SHOTGUN;
			break;
		case 13:
			ammo = AmmoName.M4A1;
			break;
		}

		amount = ammo.getMax()*3;
	}

	@Override
	public void act(GameManager gm, int tileX, int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
		// TODO Auto-generated method stub
		
	}
}
