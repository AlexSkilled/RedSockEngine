package Game.GObjects.DeadObjects.subObjects;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.audio.SoundClip;

import Game.GameManager;
import Game.GObjects.Item;

public abstract class Weapon extends Item {
	
	protected WeaponNames item;
	protected SoundClip sound;
	
	public Weapon(int itemID) {
		super(itemID);
		setItem(itemID);
	}
	
	public Weapon(int itemID, int tileX, int tileY) {
		super(itemID, tileX, tileY);
		setItem(itemID);
		
	}
	
	public Weapon(int itemID, int ammo) {
		super(itemID);
		setItem(itemID);
		item.setAmmo(ammo);
	}
	
	protected void setItem(int itemID) {
		switch(itemID) {
		case 1:
			item = WeaponNames.PISTOL;
			break;
		case 2:
			item = WeaponNames.SHOTGUN;
			break;		
		case 3:
			item = WeaponNames.M4A1;
			break;
		}
		item.refresh();
		sound = new SoundClip("audio/pistolShot.wav");
	}
	
	@Override
	public String toString() {
		String line = super.toString();
		line+=" "+item.getAmmo();
		return line;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		item.coolD();
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
			
	}
	
	public boolean isLoad() {
		if(item.getAmmo()<1)
			return false;
		return true;
	}

	public boolean isCoolDown() {
		if(item.getCoolDown()>0)
			return true;
		return false;
	}
	
	@Override
	public int getMaxDurability() {
		return item.getCoolDownAmount();
	}
	
	@Override
	public int getDurability() {
		return item.getCoolDown();
	}
	
	
	public WeaponNames getItemEnum() {
		return item;
	}

	@Override
	protected void setDurability(float ammo) {
		item.setAmmo((int) ammo);
	}


}
