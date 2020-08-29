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

public abstract class Entity extends GameObject{

	protected int ID;
	protected Image image;
	
	public Entity(int id, int tileX, int tileY){
		
		this.tileX = tileX;
		this.tileY = tileY;
		
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		
		ID = id;
		this.tag = Objects.entity;
		loadImage();
	}
	
	public Entity(int id){
		ID = id;
		this.tag = Objects.entity;
		loadImage();
	}
	
	protected void loadImage() {
		if(ID < 100)
			image = (Image) ((ImageTile) ImageBuffer.load(Images.Items)).getTileImage(ID % 10, ID / 10);
		else {
			int x = ID % GameManager.getESX();
			int y = (ID - x - 100) / GameManager.getESX();
			image = (Image) ((ImageTile) ImageBuffer.load(Images.Enviroment)).getTileImage(x, y);
			image.downgrade(0.9, 0.9);
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImage(image, (int) posX, (int)  posY);
	}
	
	@Override
	public String toString() {
		String line = "";
		line = "Entity " + ID + " " + tileX + " " + tileY;
		return line;
	}
}
