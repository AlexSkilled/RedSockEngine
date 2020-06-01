package Game.GObjects.AliveObjects;

import java.util.Random;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.buffer.Images;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GObjects.AliveObject;
import Game.Menu.SympleTypes.Bars.HPbar;

public abstract class Enemy extends AliveObject{
	
	protected float destX, destY;
	private boolean directionX, directionY;	//if true -> move A or W 
										   	//if false-> move D or S 	
	private HPbar hpBar;
	
	public Enemy(int posX, int posY, int hp, int armor, Images image) {
		super(posX, posY, hp, armor, image);
		createObject();
		
	}
	
	public Enemy(String info) {
		super(info);
		createObject();
	}

	private void createObject() {
		tag = Objects.enemy;
		
		speed = 150;
		hpBar = new HPbar(0xff00ff00, 0xffff0000, maxHP, healthPoints, this);
		
		Random random = new Random();
		directionX = random.nextBoolean();
		directionY = random.nextBoolean();
	}
	
	@Override
	public String toString() {
		String line = super.toString();
		String[] line_ = line.split(" ");
		line_[0] += " Enemy";
		line = String.join(" ", line_);
		return line;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		anim += dt * animSpeed;
		if(anim + dt * animSpeed >= 4)
			anim = 0;
			
		
		patrolX(gm, dt);
		patrolY(gm, dt);

		falling(gc, gm, dt);
		super.update(gc, gm, dt);
		hpBar.update(gc, gm, dt);
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
		hpBar.render(gc, r);
	}
	
	public void patrolX(GameManager gm, float dt) {
		
		if(gm.getCollision((int) ((posX + GameManager.TS) / GameManager.TS), tileY) || 
		   gm.getCollision((int) ((posX - 1) / GameManager.TS), tileY))
			directionX = !directionX;
		
		if(directionX)
			moveA(gm, dt);
		else
			moveD(gm, dt);
	}
	
	public void patrolY(GameManager gm, float dt) {
		
		if(gm.getCollision(tileX, (int) (posY + GameManager.TS)/GameManager.TS + 1) || 
		   gm.getCollision(tileX, (int) (posY - GameManager.TS)/GameManager.TS))
			directionY = !directionY;
		
		if(directionY)
			moveW(gm, dt);
		else
			moveS(gm, dt);

	}
	
	protected void walkTo(float destX, float destY) {
		float alpha = (float) Math.atan((posY - destY * GameManager.TS) / (posX - destX * GameManager.TS));
		
		if(destX * GameManager.TS < tileX) 
			speed *= -1;
		
		else 
			if(destY * GameManager.TS < tileY) 
				speed *=-1;
		
		offX += speed * Math.cos(alpha);
		offY += speed * Math.sin(alpha);
		
		this.destX = destX;
		this.destY = destY;
	}

	protected void searchEntity(String tagToSearch) {
		
	}
}
