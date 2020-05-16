package Game.GObjects;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.Image;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;

import Game.GameManager;
import Game.GameObject;

public class Entity extends GameObject{

	protected Image image;
	
	public Entity(int posX, int posY, Images image){
		tileX = posX / GameManager.TS;
		tileY = posY / GameManager.TS;
		this.posX = posX;
		this.posY = posY;
		this.image = (Image) ImageBuffer.load(image);
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImage(image, (int) posX, (int) posY);
	}

	@Override
	public String toString() {
		String line = "Entity " + posX +" " + posY + " " + ImageBuffer.getName(image);
		return line;
	}
}
