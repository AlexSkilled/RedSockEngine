package Game;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Enums.Objects;

public class Camera {
	
	protected float offX, offY;
	
	protected Objects targetTag;
	protected GameObject target = null;
	
	public Camera(Objects tag) {
		this.targetTag = tag;
	}
	
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(target  == null) {
			target = gm.getObject(targetTag);
		}
		
		if(target == null) {
			return;
		}
		
		float targetX = (target.getPosX() + target.getWidth() / 2) - GameContainer.getWidth() / 2;
		float targetY = (target.getPosY() + target.getHeight() / 2) - GameContainer.getHeight() / 2;

		offX -= dt * (offX - targetX) * 10;
		offY -= dt * (offY - targetY) * 10;
		
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

	public float getOffX() {
		return offX;
	}

	public void setOffX(float offX) {
		this.offX = offX;
	}

	public float getOffY() {
		return offY;
	}

	public void setOffY(float offY) {
		this.offY = offY;
	}

	public Objects getTargetTag() {
		return targetTag;
	}

	public void setTargetTag(Objects targetTag) {
		this.targetTag = targetTag;
	}

	public GameObject getTarget() {
		return target;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}
	
}
