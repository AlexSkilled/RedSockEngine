package Game.GObjects.DeadObjects.LightSources;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.Light;

import Game.GameManager;
import Game.GameObject;

public class LightSource extends GameObject{

	private Light light;
	private float delta = 0.25f;
	private int maxR, minR;
	private float diam;
	public LightSource(int x, int y, int r, int color) {
		this.tileX = x;
		this.tileY = y;
		maxR = r;
		minR = r-5;
		light = new Light(r, color);
		diam = light.getDiameter();
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		diam += delta;
		light.setDiameter((int) diam);
		if(light.getDiameter() < minR*2 || light.getDiameter() > maxR*2 )
			delta*=-1;
			
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.setzDepth(0);
		r.drawLight(light, tileX*32, tileY*32);
		r.setzDepth(1);
	}
	
}
