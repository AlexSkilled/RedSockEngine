package Game.GObjects;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.Image;
import com.Game.Engine.gfx.ImageTile;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GameObject;

public abstract class Item extends GameObject {
	
	protected boolean inPocket = true;
	protected Image image;
	protected int ID;
	
	public Item(int itemID) {
		ID = itemID;
		this.tag = Objects.item;
		loadImage();
	}
	
	public Item(int itemID, int tileX, int tileY) {
		ID = itemID;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		this.tag = Objects.item;
		this.inPocket = false;		
		loadImage();
	}
	
	private void loadImage() {
		if(ID < 100)
			image = (Image) ((ImageTile) ImageBuffer.load(Images.Items)).getTileImage(ID % 10, ID / 10);
		else {
			int x = ID % GameManager.getESX();
			int y = (ID - x - 100) / GameManager.getESX();
			image = (Image) ((ImageTile) ImageBuffer.load(Images.enviroment)).getTileImage(x, y);
			image.downgrade(0.9, 0.9);
		}
	}
	
	@Override
	public String toString() {
		String line = "";
		if(inPocket)
			line = "Item " + Integer.toString(ID);
		else
			line = "Item " + Integer.toString(ID)+ " " + posX + " " + posY;
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
