package Game.GObjects;

import java.awt.event.KeyEvent;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.ImageTile;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;

import Game.GameManager;
import Game.GameObject;
import Game.Ground;
import Game.GObjects.DeadObjects.Bullet;
import Game.GObjects.Inventory.EntityInventory;
import Game.GObjects.Inventory.Inventory;
import Game.Menu.callouts.CallOut;

public class AliveObject extends GameObject{

	protected int inHand;
	
	protected int direction;
	protected float anim;

	protected float animSpeed;
	
	protected Inventory invent;
	private CallOut callOut;
	
	protected float fallDistance;
	protected float fallSpeed;
	protected float jump;
	
	protected boolean ground, key_pressed;

	protected int healthPoints, maxHP, armor;
	
	protected ImageTile objectImage;
	
	@Override
	public String toString() { 
		String line = "AliveObject " + posX +" " + posY + " " + healthPoints + " " + maxHP +" "+ armor + " " + ImageBuffer.getName(objectImage) + " " + invent.toString() + " ";
		return line;
	}
	
	public AliveObject(int posX, int posY, int hp, int armor, Images name) {
		createObject(posX * GameManager.TS, posY * GameManager.TS, hp, hp, armor, name);
	}
	
	public AliveObject(String line) {
		String[] objectInfo = line.split(" ");
		int i = 0;
		createObject(Float.parseFloat(objectInfo[i++]), Float.parseFloat(objectInfo[i++]), 	//position
				Integer.parseInt(objectInfo[i++]), Integer.parseInt(objectInfo[i++]),		//hp , maxHp
				Integer.parseInt(objectInfo[i++]),  										// armor
				Images.valueOf(objectInfo[i++]));											//Image name
		
	}
	
	private void createObject(float posX, float posY, int hp, int maxHP, int armor, Images name) {
		inHand = 0;
		invent = new EntityInventory(8, this);
		
		objectImage = (ImageTile) ImageBuffer.load(name);
		
		direction = 0;
		anim = 0;
		animSpeed = 15;
		
		fallDistance = 0;
		ground = false;
		key_pressed = false;
		
		this.tileX = (int) (posX / GameManager.TS);
		this.tileY = (int) (posY / GameManager.TS);
		this.posX = posX;
		this.posY = posY;
		offX = 0;
		offY = 0;
		speed = 150;
		
		if(!GameManager.pDimension) {
			jump = 0;
			fallSpeed = 0;
		}else {
			jump = -3f;
			fallSpeed = 8;
		}
		
		healthPoints = hp;
		this.maxHP = maxHP;  
		this.armor = armor;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		Bullet bullet = gm.getBulletCollision(tileX, tileY);
		if(bullet != null && !bullet.getMasterTag().equals(tag)){
			healthPoints -= bullet.getDamage();
			bullet.setDead(true);
		}
		
		if(healthPoints <= 0) {
			dead = true;
			return;
		}
		
		if(callOut != null) {
			callOut.update(gc, gm, dt);
		}
		
		finalPos();
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImageTile(objectImage, (int) posX, (int) posY, (int) anim, direction);
		if(callOut != null)
			callOut.render(gc, r);
	}
	
	protected void finalPos() {

		if(offY > GameManager.TS/2) {
			tileY++;
			offY -= GameManager.TS;
		}		
		if(offY < -GameManager.TS/2) {
			tileY --;
			offY += GameManager.TS;
		}
		if(offX > GameManager.TS/2) {
			tileX ++;
			offX -= GameManager.TS;
		}		
		if(offX < -GameManager.TS/2) {
			tileX --;
			offX += GameManager.TS;
		}
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;		
	}	
	
	protected void falling(GameContainer gc, GameManager gm,  float dt) {
		fallDistance += dt * fallSpeed;
		
		if(key_pressed && ground) {
			fallDistance = jump;
			ground = false;
		}
		offY += fallDistance;
		if(fallDistance < 0)
			if((gm.getCollision(tileX, tileY-1) || gm.getCollision(tileX + (int)Math.signum((int)offX), tileY-1)) && offY < 0) {
				fallDistance = 0;
				offY = 0;
			}
		if(fallDistance > 0)
			if((gm.getCollision(tileX, tileY+1) || gm.getCollision(tileX + (int) Math.signum((int) offX), tileY+1)) && offY >0) {
				fallDistance = 0;
				offY = 0;
				ground = true;
			}
		}
	
	protected void checkPos(GameManager gm, Ground ground) {
		Item go = ground.getItem(tileX, tileY);
		if(go != null) {
			invent.equip(go);
			ground.deleteObject(tileX, tileY, gm);
		}
	}
	
	
	protected void changePlayerDirection(GameContainer gc, float dt) {
		if(gc.getInput().isKey(KeyEvent.VK_D)) {
			anim += dt*animSpeed;
			if(anim >= 4) 
				anim = 0;			
		}else if(gc.getInput().isKey(KeyEvent.VK_A)) {
			
			anim += dt*animSpeed;
			if(anim >= 4)
				anim = 0;
		}else if(gc.getInput().isKey(KeyEvent.VK_W) || gc.getInput().isKey(KeyEvent.VK_S)) {
			anim += dt * animSpeed;
			if(anim>=4)
				anim=0;
		} else
			anim = 0;	
	}
	
	protected void moveD(GameManager gm, float dt) {
		direction = 0;
		if(gm.getCollision(tileX + 1, tileY) || gm.getCollision(tileX + 1, tileY + (int)Math.signum((int) offY))) {
			if(offX < 0) {
				offX += dt * speed;
				if(offX > 0) {
					offX = 0;
				}
			}
			else {
				offX = 0;
			}
		}
		else {
			offX += dt * speed;
		}
		
	}
	
	protected void moveA(GameManager gm, float dt) {
		direction = 1;
		if(gm.getCollision(tileX - 1, tileY) || gm.getCollision(tileX - 1, tileY + (int)Math.signum((int) offY))) {
			if(offX > 0) {
				offX -= dt * speed;
				if(offX < 0) {
					offX = 0;
				}
			}
			else {
				offX = 0;
			}
		}
		else
		{
			offX -= dt * speed;
	
		}
	}

	protected void moveW(GameManager gm, float dt) {
		direction = 3;
			if(gm.getCollision(tileX, tileY-1) || gm.getCollision(tileX + (int) Math.signum((int) offX), tileY - 1 )) {
				if(offY > 0) {
					offY -= dt * speed;
					if(offY < 0) {
						offY = 0;
					}
				}
				else {
					offY = 0;
				}
			}
			else
			{
				offY -= dt * speed;
		
			}
	}

	protected void moveS(GameManager gm, float dt) {
		direction = 2;
		if(gm.getCollision(tileX, tileY+1) || gm.getCollision(tileX + (int)Math.signum((int) offX), tileY + 1 )) {
			if(offY < 0) {
				offY += dt * speed;
				if(offY > 0) {
					offY = 0;
				}
			}
			else {
				offY=0;
			}
		}
		else {
			offY += dt * speed;
		}
	}

	public void loadInventory(String[] data, GameManager gm) {
		invent.loadSelf(data, gm);
	}
	
	public int getHP() {
		return healthPoints;
	}

	public int inHand() {
		return inHand;
	}

	public void say(String text) {
		callOut = new CallOut(this, text);
	}
	
	public void setCallOut(CallOut callout) {
		this.callOut = callout;
	}

	public int getDirection() {
		return direction;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}

