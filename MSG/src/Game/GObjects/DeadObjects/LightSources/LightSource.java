package Game.GObjects.DeadObjects.LightSources;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.Light;

import Game.GameManager;
import Game.GObjects.Entity;

public class LightSource extends Entity{

	private Light light;
	private float delta = 0.25f;
	private int maxR, minR;
	private float diam;
	private float gamma;
	
	public LightSource(int tileX, int tileY, float r, int color) {
		super(25, tileX, tileY);
		maxR = (int) (r*GameManager.TS);
		minR = maxR-5;
		light = new Light(maxR, color);
		diam = light.getDiameter();
		gamma = 0;
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
		r.drawLight(light,(int) (GameManager.TS*(tileX+gamma)),(int) (GameManager.TS*(tileY+gamma)));
		r.setzDepth(1);
	}
	@Override
	public String toString() {
		return (super.toString() + " "+ (float) (maxR/GameManager.TS) + " " + Integer.toHexString(light.getColor()));
	}
}
