package Game;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Enums.Objects;

public class FreeCamera extends Camera {

	private int deltaX, deltaY;
	
	public FreeCamera(Objects tag, int deltaX, int deltaY) {
		super(tag);
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	public FreeCamera(GameObject object) {
		super(object.getTag());
		target = object;
	}
	
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(target  == null) {
			target = gm.getObject(targetTag);
		}
		if(target == null) {
			return;
		}
		
		
		offX += deltaX;
		offY += deltaY;

		if(offX < 0)
			offX = 0;
		else
			if(offX + GameContainer.getWidth() > gm.getLevelW()*GameManager.TS) 
				offX = gm.getLevelW() * GameManager.TS - GameContainer.getWidth();
		
		if(offY < 0)
			offY = 0;
		else
			
			if(offY + GameContainer.getHeight() > gm.getLevelH()*GameManager.TS) 
				offY = gm.getLevelH() * GameManager.TS - GameContainer.getHeight();
		
	}
	
	public void render(Renderer r) {
		r.setCamX((int) offX);
		r.setCamY((int) offY);
	}

	
}
