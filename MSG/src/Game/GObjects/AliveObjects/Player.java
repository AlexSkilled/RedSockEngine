package Game.GObjects.AliveObjects;

import java.awt.event.KeyEvent;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.buffer.Images;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GObjects.AliveObject;
import Game.GObjects.PlayerInventory;
import Game.Menu.WorkingConsole;

public class Player extends AliveObject{
	
	protected int camOffX, camOffY;
	protected boolean noClip;

	protected int destX, destY;
	
	public Player(int posX, int posY) {
		super(posX, posY, 100, 0, Images.player);
		createObject();
		noClip = false;
		objectImage.setLightBlock(1);
	}
	
	public Player(String data) {
		super(data);
		createObject();
		objectImage.setLightBlock(1);
	}
	
	private void createObject() {
		tag = Objects.player;
		speed  = 160;
		invent = new PlayerInventory(8, this);
	}
	
	@Override
	public String toString() {
		String line = super.toString();
		String[] line_ = line.split(" ");
		line_[0] += " Player";
		line = String.join(" ", line_);
		
		return line;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		camOffX = (int) gm.getCamera().getOffX();
		camOffY = (int) gm.getCamera().getOffY();
		
		destX = (int) (gc.getInput().getMouseX() + camOffX);
		destY = (int) (gc.getInput().getMouseY() + camOffY);
		
		//checking buttons for movements
		moving(gc, gm, dt);
		
		//checking LB for shooting
		invent.useItem(gc, gm, dt, destX, destY);
		
		//direction where player is looking
		changePlayerDirection(gc, dt);
		
		//checking ground under Player for picking up items
		checkPos(gm, gm.getGround());
		
		invent.update(gc, gm, dt);
		
		super.update(gc, gm, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
		invent.render(gc, r);
	}
	
	protected void moving(GameContainer gc, GameManager gm, float dt) {
		if(noClip) {
			if(gc.getInput().isKey(KeyEvent.VK_D))
				moveD(gm, dt);
			if(gc.getInput().isKey(KeyEvent.VK_A))
				moveA(gm, dt);
			
			if(jump == 0) {
				//moving up n down
				//up
				if(gc.getInput().isKey(KeyEvent.VK_W))
					moveW(gm, dt);
					
				//down
				if(gc.getInput().isKey(KeyEvent.VK_S))
					moveS(gm, dt);
				
			}else {
				// Begining jump & gravity
				if(gc.getInput().isKey(KeyEvent.VK_W))
					key_pressed = true;
				else
					key_pressed = false;
			}
		}
		else {
			if(gc.getInput().isKey(KeyEvent.VK_D))
				super.moveD(gm, dt);
			if(gc.getInput().isKey(KeyEvent.VK_A))
				super.moveA(gm, dt);
			
			if(jump == 0) {
				//moving up n down
				//up
				if(gc.getInput().isKey(KeyEvent.VK_W))
					super.moveW(gm, dt);
					
				//down
				if(gc.getInput().isKey(KeyEvent.VK_S))
					super.moveS(gm, dt);
				
			}else {
				// Begining jump & gravity
				if(gc.getInput().isKey(KeyEvent.VK_W))
					key_pressed = true;
				else
					key_pressed = false;
			}
			falling(gc, gm, dt);
			//end of jump n gravity
		}
	}
	
	protected void moveD(GameManager gm, float dt) {
		direction = 0;
		if(tileX+1 < gm.getLevelW())
			offX += dt * speed;
	}
	
	protected void moveA(GameManager gm, float dt) {
		direction = 1;
		if(tileX-1 >= 0)
			offX -= dt * speed;
	}

	protected void moveW(GameManager gm, float dt) {
		direction = 3;
		if(tileY-1 >= 0)
			offY -= dt * speed;
	}

	protected void moveS(GameManager gm, float dt) {
		direction = 2;
		if(tileY+1 < gm.getLevelH())
			offY += dt * speed;
	}
	
	public int getCamOffX() {
		return camOffX;
	}
	
	public int getCamOffY() {
		return camOffY;
	}
	
	public void noClip() {
		noClip = !noClip;
		String line = "noClip is ";
		if(noClip)
			line+= "on";
		else
			line += "off";
		WorkingConsole.throwError(line);
	}
	
}