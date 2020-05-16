package Game.Menu.callouts;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.GFX;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;

import Game.GameManager;
import Game.GameObject;
import Game.GObjects.AliveObject;

public class Emogi extends GameObject{
	
	private GameObject go;
	private GFX icon;
	private int x, y;
	
	public Emogi(AliveObject object) {
		go = object;
		icon = ImageBuffer.load(Images.callOut);
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		x = (int) go.getPosX();
		y = (int) go.getPosY();
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImage(icon, x, y - GameManager.TS / 4);
	}

}
