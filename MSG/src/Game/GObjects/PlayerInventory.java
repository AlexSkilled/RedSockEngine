package Game.GObjects;

import java.awt.event.KeyEvent;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.Image;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;

import Game.GameManager;
import Game.GObjects.AliveObjects.Player;
import Game.GObjects.DeadObjects.subObjects.Ammo;
import Game.GObjects.DeadObjects.subObjects.Weapon;
import Game.GObjects.Inventory.Inventory;
import Game.Menu.SympleTypes.Bars.ItemBar;

public class PlayerInventory extends Inventory{
	
	protected Player player;
	protected int camOffX, camOffY;
	protected ItemBar bar;
	protected int x, y, openAnimation, delta;
	protected Item holdingItem;
	protected Image back;
	
	public PlayerInventory(int size, Player ao) {
		inv = new Item[size];
		this.player = ao;
		inHand = 0;
		x = GameContainer.getWidth()/3;
		y = GameContainer.getHeight() - GameManager.TS;
		back = (Image) ImageBuffer.load(Images.GreyBack);
		back.setAlpha(false);
		delta = 0;
		openAnimation = 0;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		camOffX = player.getCamOffX();
		camOffY = player.getCamOffY();
		
		posX = player.getPosX() + GameManager.TS/2;
		posY = player.getPosY() + GameManager.TS/2;
		
		if(inv[inHand] != null)
			if(inv[inHand].getItemID() < 10) {
				Weapon temp = ((Weapon) inv[inHand]);
				if(temp.isCoolDown()) 
					if(bar == null)
						bar = new ItemBar(0xff000000, 0xffffffff, temp.getItemEnum().getCoolDownAmount(), temp.getItemEnum().getCoolDown(),
								player, temp);
					else
						bar.update(gc, gm, dt);
				else
					bar = null;
			}
		
		changeHoldingItem(gc);
		
		if(gc.getInput().isKeyUp(KeyEvent.VK_Q) && inv.length > 8) {
			if(openAnimation < 0) {
				delta = 4;
			}else {
				delta = -4;
			}
		}
		if(delta != 0) {
			openAnimation += delta;
			y += delta ;
			if(openAnimation < -GameManager.TS*(inv.length/8) || openAnimation > -1) {
				delta = 0;
			}
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		int newX, newY;
		for(int i = 0; i < inv.length; i++) {
			newX = x + (i % 8) * GameManager.TS + camOffX;
			newY = y + (i / 8) * GameManager.TS + camOffY;
			
			r.drawImage(back, newX, newY);
			
			if(inv[i] != null) {
				r.drawImage(inv[i].image, newX,  newY);
			}
			
			if(holdingItem != null) {
					r.drawImage(holdingItem.image, gc.getInput().getMouseX() - GameManager.TS/2 + camOffX,
													gc.getInput().getMouseY() - GameManager.TS + camOffY);
			}

			if(i == inHand)
				r.drawRect(newX, newY, GameManager.TS - 1, GameManager.TS - 1, 0xff00ff00);
			else 
				r.drawRect(newX, newY, GameManager.TS - 1, GameManager.TS - 1, 0xffff0000);
			
		}
		
		if(inv[inHand] != null)
			if(inv[inHand].getItemID() < 10) {
				String text = Integer.toString(((Weapon) inv[inHand]).getItemEnum().getAmmo());
				r.drawText(text, GameManager.TS + camOffX, GameManager.TS * 14 + camOffY, 0xff000000, false);
				if(bar != null)
					bar.render(gc, r);
			}
		
		if(inv.length > 8 && openAnimation != 0) {
			
		}
		
	}
	
	@Override
	protected void changeHoldingItem(GameContainer gc) {
		//by mouse wheel
		switch (gc.getInput().getScroll()) {
		case -1:
			if(inHand>0) inHand-=1;
			break;
		case 1:
			if(inHand<7) inHand+=1;
		}
		
		//by keyboard 1-8 keys
		if(gc.getInput().isKey(KeyEvent.VK_1)) inHand = 0;
		else if (gc.getInput().isKey(KeyEvent.VK_2)) inHand=1;
		else if (gc.getInput().isKey(KeyEvent.VK_3)) inHand=2;
		else if (gc.getInput().isKey(KeyEvent.VK_4)) inHand=3;
		else if (gc.getInput().isKey(KeyEvent.VK_5)) inHand=4;
		else if (gc.getInput().isKey(KeyEvent.VK_6)) inHand=5;
		else if (gc.getInput().isKey(KeyEvent.VK_7)) inHand=6;
		else if (gc.getInput().isKey(KeyEvent.VK_8)) inHand=7;
	}
	
	@Override
	public void useItem(GameContainer gc, GameManager gm, float dt, int destX, int destY) {
		//shoting
		//isKeyDown is for 1 bullet per pressing
		//isKey is for releasing bullets 'till button is pressed

		if((destX > x + camOffX && destX < x + camOffX + GameManager.TS*8) && destY > y + camOffY) {
			if(gc.getInput().isButtonUp(1)) {
				int pos = (destX-x - camOffX)/GameManager.TS + 8 * ((destY - y - camOffY)/GameManager.TS);
				if(pos < inv.length) {
					Item temp;
					temp = holdingItem;
					holdingItem = inv[pos];
					inv[pos] = temp;
				}
			}
		}
		else {
			if(inv[inHand] != null){

				if(gc.getInput().isKeyUp(KeyEvent.VK_G)) {
					gm.getGround().drop(inv[inHand], destX / GameManager.TS, destY / GameManager.TS, gm);
					inv[inHand] = null;
				}
				else
				if (inv[inHand].getItemID() < 10) {
					Weapon temp = ((Weapon) inv[inHand]);
					if(gc.getInput().isKeyUp(KeyEvent.VK_R)) {
						Ammo tempItem = (Ammo) getItem(10 + temp.getItemID()%10);
						if(tempItem != null) {
							temp.getItemEnum().reload((Ammo) tempItem);
							if(tempItem.getDurability()<1) {
								deleteItem(tempItem);
							}
						}
					}
					if(temp.isLoad()) {
						//single shot
						if(gc.getInput().isButtonUp(1)) 
							inv[inHand].act(gm, (int) posX, (int) posY, destX, destY, gc, player.getTag());
						
						//multiply shots
						if(temp.getItemID()>2)
							if(gc.getInput().isButton(1))
								if(temp.isLoad()) {
									inv[inHand].act(gm, (int) posX, (int) posY, destX, destY, gc, player.getTag());								
								}
							}
				}else {
					destX = (int) (destX / GameManager.TS);
					destY = (int) (destY / GameManager.TS);
					if(gc.getInput().isButton(1)) {
						inv[inHand].act(gm, (int) posX, (int) posY,
								destX, destY, gc, player.getTag());
					}
					else {
						if(gc.getInput().isButtonUp(2)) {
							changeBlock(gm.getBlockAt(destX, destY));
						}
					else {
						if(gc.getInput().isButton(3))
							gm.setBlock(destX, destY, GameManager.getCollisionStop());
						}
					}
				}
			}
		}
			
	}
	
	protected void changeBlock(short num) {
		Item item = inv[inHand];
		for(int i = 0; i < inv.length; i++)
			if(inv[i] != null && inv[i].getItemID() == (int) num + 100) {
				inv[inHand] = inv[i];
				inv[i] = item;
				break;
			}
	}

}
