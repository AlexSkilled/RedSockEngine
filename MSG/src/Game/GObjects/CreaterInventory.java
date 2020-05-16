package Game.GObjects;

import java.awt.event.KeyEvent;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.GObjects.AliveObjects.Player;
import Game.GObjects.DeadObjects.subObjects.Ammo;
import Game.GObjects.DeadObjects.subObjects.Weapon;

public class CreaterInventory extends PlayerInventory{

	private int lookAtX, lookAtY, radius;
	private boolean selection;
	public CreaterInventory(int size, Player ao) {
		super(size, ao);
		lookAtX = lookAtY = radius = 0;
		selection = false;
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		super.update(gc, gm, dt);
		if(gc.getInput().isKeyUp(KeyEvent.VK_I))
			selection = !selection;
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);

		if(selection) {
			int ts = GameManager.TS;
			r.drawRect((lookAtX - radius)*ts, (lookAtY - radius)*ts, ts*(radius+1), ts*(radius+1), 0xffff0000);
		}
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
					
					if(gc.getInput().isKeyUp(KeyEvent.VK_P))
						radius++;
					else
						if(gc.getInput().isKeyUp(KeyEvent.VK_O))
							radius+= radius > 0 ? -1:0;
							
					destX = (int) (destX / GameManager.TS);
					destY = (int) (destY / GameManager.TS);
					
					if(!(destX >= (lookAtX - radius/2) &&
							destX <= lookAtX)){
						lookAtX += destX>lookAtX ? 1:-1;
					}
					
					if(!(destY >= (lookAtY - radius/2) &&
						destY <= lookAtY)) {
						lookAtY += destY>lookAtY ? 1:-1;
					}
					
					if(gc.getInput().isButton(1)) {
						for(int i = 0; i <= radius; i++)
							for(int j = 0; j <= radius; j++)
								inv[inHand].act(gm, (int) posX, (int) posY,
										lookAtX - i , lookAtY - j, gc, player.getTag());
					}
					else {
						if(gc.getInput().isButtonUp(2)) {
							changeBlock(gm.getBlockAt(destX, destY));
						}
					else {
						if(gc.getInput().isButton(3))
							gm.setBlock(lookAtX, lookAtY, GameManager.getCollisionStop());
						}
					}
				}
			}
		}
			
	}
}
