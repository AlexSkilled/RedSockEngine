package Game.GObjects;

import com.Game.Engine.GameContainer;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GObjects.DeadObjects.Bullet;
import Game.GObjects.DeadObjects.Blocks.Block;
import Game.GObjects.DeadObjects.subObjects.Ammo;
import Game.GObjects.DeadObjects.subObjects.Weapon;

public class ItemCreater {

	public static Item create(int iD, int x, int y, GameManager gm) {
		Item item = null;
		if(iD<10)
			switch(iD) {
				case 1: item = new Weapon(iD, x, y) {
					@Override
					public void act(GameManager gm, int tileX,  int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
						if(item.fire()) {
								gm.addObject(new Bullet(tileX, tileY, item.getDamage(), destX, destY, masterTag));
								sound.play();
							}
						}
				};
				break;
				
				case 2: item = new Weapon(iD, x, y) {
					@Override
					public void act(GameManager gm, int tileX,  int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
						if(item.fire()) {
							for(int i = -3; i < 3; i++) {
								gm.addObject(new Bullet(tileX, tileY, item.getDamage(), destX + i*15, destY + i * 15, masterTag));
								sound.play();	
							}
							}
						}
				};
				break;
				
				case 3: item = new Weapon(iD, x, y) {
					@Override
					public void act(GameManager gm, int tileX,  int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
						if(item.fire()) {
							gm.addObject(new Bullet(tileX, tileY, item.getDamage(), destX, destY, masterTag));
							sound.play();
							}
						}
				};
				break;
				default:
					item = new Weapon(iD, x, y) {
						@Override
						public void act(GameManager gm, int tileX,  int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
							if(item.fire()) {
								gm.addObject(new Bullet(tileX, tileY, item.getDamage(), destX, destY, masterTag));
								sound.play();
							}
						}
					};
					break;
			}
		else 
			if(iD<20)
				item = new Ammo(iD, x, y);
				
		if(item!=null)
			gm.addObjectToBegin(item);
		return item;
	}

	public static Item create(int iD, GameManager gm) {
		Item item = null;
		if(iD<10)
			switch(iD) {
				case 1: item = new Weapon(iD) {
					@Override
					public void act(GameManager gm, int tileX,  int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
						if(item.fire()) {
								gm.addObject(new Bullet(tileX, tileY, item.getDamage(), destX, destY, masterTag));
								sound.play();
							}
						}
				};
				break;
				
				case 2: item = new Weapon(iD) {
					@Override
					public void act(GameManager gm, int tileX,  int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
						if(item.fire()) {
							for(int i = -3; i < 3; i++) {
								gm.addObject(new Bullet(tileX, tileY, item.getDamage(), destX + i*15, destY + i * 15, masterTag));
								sound.play();	
							}
							}
						}
				};
				break;
				
				case 3: item = new Weapon(iD) {
					@Override
					public void act(GameManager gm, int tileX,  int tileY, int destX, int destY, GameContainer gc, Objects masterTag) {
						if(item.fire()) {
							gm.addObject(new Bullet(tileX, tileY, item.getDamage(), destX, destY, masterTag));
							sound.play();
							}
						}
				};
				break;
			}
		else
			if(iD<20)
					item = new Ammo(iD);
			else
				if(iD < 100 + GameManager.getESX()*GameManager.getESY())
					item = new Block(iD);
		if(item!=null)
			item.setInPocket(true);

		return item;
	}
	
	public static Item create(String[] line, GameManager gm) {
		Item item = null;
		item = create(Integer.parseInt(line[1]), gm);
		item.setDurability(Float.parseFloat(line[2]));
		return item;
	}
}
