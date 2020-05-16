package Game.GObjects.Inventory;

import com.Game.Engine.GameContainer;

import Game.GameManager;
import Game.GameObject;
import Game.GObjects.Item;
import Game.GObjects.ItemCreater;

public abstract class Inventory extends GameObject{
	
	protected Item[] inv;
	protected int inHand;
	
	/*
	 * returns true if it's possible to add an item
	 * returns false if it isn't
	 */
	public boolean addItem(Item item) {
		for(int i = 0; i < inv.length; i++) {
			if(inv[i] == null){
				inv[i] = item;
				return true;
			}
		}
		return false;
	}
	
	public void equip(Item item) {
		
		for(int i = 0; i < inv.length; i++) {
			if(inv[i] == null || inv[i].getItemID() == 0) {
				item.setInPocket(true);
				inv[i] = item;
				break;
			}
		}
	}

	protected Item getItem(int ID) {
		for(Item item : inv)
			if(item!=null && item.getItemID() == ID)
				return item;
		return null;
	}
	
	protected void deleteItem(Item item) {
		for(int i = 0; i < inv.length ;i++)
			if(inv[i] != null && inv[i].equals(item)) {
				inv[i] = null;
				return;
			}
	}
	protected abstract void changeHoldingItem(GameContainer gc);
	public abstract void useItem(GameContainer gc, GameManager gm, float dt, int destX, int destY);
	
	public String toString() {
		String line = "\n";
		for(int i = 0; i < inv.length; i++) {
			if(inv[i] != null)
				line += inv[i].toString()+ ":";
			else
				line += "null:";
		}
		return line;
	}

	public void loadSelf(String data[], GameManager gm) {
		inv = new Item[data.length];
		for(int i = 0; i < data.length; i++) {
			if(data[i].equals("null"))
				inv[i] = null;
			else
				inv[i] = ItemCreater.create(data[i].split(" "), gm);
		}
	}
}
