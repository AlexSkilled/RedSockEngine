package Game;

import com.Game.Enums.Objects;

public abstract class GameObject extends ProgrammObject{
	
	protected Objects tag = Objects.entity;
	protected float posX, posY;
	protected int width, height;
	protected boolean dead = false;
	protected int tileX, tileY;
	protected float offX, offY, speed;
	
	

	protected String updateName(String[] a, String newName) {
		a[0] = newName;
		return String.join(" ", a);
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

	public Objects getTag() {
		return tag;
	}
	public void setTag(Objects tag) {
		this.tag = tag;
	}
	public float getPosX() {
		return posX;
	}
	public void setPosX(float posX) {
		this.posX = posX;
		tileX = (int) (posX/GameManager.TS);
	}
	public float getPosY() {
		return posY;
	}
	public void setPosY(float posY) {
		this.posY = posY;
		tileY = (int) (posY/GameManager.TS);
	}
	public void setTileX(int x) {
		tileX = x;
		posX = tileX * GameManager.TS;
	}
	public void setTileY(int y) {
		tileY = y;
		posY = tileY * GameManager.TS;
	}
	public int getTileX() {
		return tileX;
	}
	public int getTileY() {
		return tileY;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public boolean isCollide(int xF, int yF) {
		return tileX==xF && tileY==yF;
	}
	
}
