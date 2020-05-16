package Game.GObjects.DeadObjects;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GameObject;

public class Bullet extends GameObject {

	//private int mouseX, mouseY;
	private int damage;
	private float bulletSpeed = 500f;
	private float alpha;
	private Objects masterTag;
	
	/**
	 * @param 
	 * 	tileX the X of square where bullet located
	 * @param 
	 * 	tileY the Y of square where bullet located
	 * @param 
	 * 	offX the actual X position of bullet
	 * @param 
	 * 	offY the actual Y position of bullet
	 * @param 
	 * 	damage the damage bullet does
	 * @param
	 * 	gc the GameContainer object where game is running
	 * @param
	 * 	masterTag the Tag of entity that shoot
	 *
	 */
	public Bullet(float posX, float posY, int damage, int mouseX, int mouseY, Objects masterTag) {
		
		tag = Objects.bullet;
		
		this.masterTag = masterTag;
		
		this.posX = posX;
		this.posY = posY;
		
		this.tileX = (int) posX/GameManager.TS;
		this.tileY = (int) posY/GameManager.TS;
		
		this.damage = damage; 
		alpha = (float) Math.atan((posY - mouseY) / (posX - mouseX));
		
		if(mouseX < posX)
			bulletSpeed *= -1; 
	}
	
	@Override
	public String toString() {
		String line ="Bullet " +  tileX + " " + tileY + " " + offX + " " + offY + " " + damage + " " + masterTag.getName();
		return line;
	}
	
	
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		if(gm.getCollision((int) (posX / GameManager.TS), (int) (posY / GameManager.TS)))
			this.dead = true;
		
		posX += dt * bulletSpeed * Math.cos(alpha);
		posY += dt * bulletSpeed * Math.sin(alpha);
		
		tileX = (int) (posX/GameManager.TS);
		tileY = (int) (posY/GameManager.TS);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.drawFillRect((int) posX, (int) posY, (int) GameManager.TS/16, (int) GameManager.TS/16, 0xffff0000);
	
	}
	public int getDamage() {
		return damage;
	}
	
	public Objects getMasterTag() {
		return masterTag;
	}

	@Override
	protected String updateName(String[] a, String newName) {
		// TODO Auto-generated method stub
		return null;
	}
}
