package Game.GObjects;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Enums.Objects;

import Game.GameManager;

public abstract class Item extends Entity {
	
	protected boolean inPocket = true;
	
	public Item(int itemID) {
		super(itemID);
		this.tag = Objects.item;
	}
	
	public Item(int itemID, int tileX, int tileY) {
		super(itemID, tileX, tileY);
		this.tag = Objects.item;
		this.inPocket = false;		
	}
	
	@Override
	public String toString() {
		String line = "";
		if(inPocket)
			line = "Item " + ID;
		else
			line = "Item " + ID + " " + tileX + " " + tileY;
		return line;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		if(!inPocket) {
			r.drawImage(image, (int) posX, (int) posY);
		}
	}
	
	public void setInPocket(boolean isIt) {
		inPocket = isIt;
		image.setAlpha(!inPocket);
	}
	
	public boolean isInPocket() {
		return inPocket;
	}
	
 	public int getItemID() {
		return ID;
	}
	
	public int getTile(String var) {
		if(var == "x")
			return  (int) posX / GameManager.TS;
		else return  (int) posY / GameManager.TS;
	}

	public abstract void act(GameManager gm, int tileX,  int tileY, int destX, int destY, GameContainer gc, Objects masterTag);
	
	public abstract int getDurability();

	public abstract int getMaxDurability();
	
	protected abstract void setItem(int ItemID);

	protected abstract void setDurability(float durability);

}
