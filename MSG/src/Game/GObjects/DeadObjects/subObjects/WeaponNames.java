package Game.GObjects.DeadObjects.subObjects;

public enum WeaponNames{
						PISTOL(7, 1, 20, 1, 1, 3, 0),//ammo id cooldownTicks tileOfImageX tileOfImageY dmg
						SHOTGUN(2, 2, 40, 1, 2, 3, 0),
						M4A1(30, 3, 60, 5, 1, 3, 5);
	int coolRate[] = new int[2];
	int ID; 
	private int ammo[] = new int[2]; 
	int cooldown[] = new int[2]; 
	int image[] = new int[2];
	int damage;
	
	WeaponNames(int ammo, int ID, int cooldown, int imageX, int imageY, int damage, int coolRate){
		this.ammo[1] = ammo;
		this.ammo[0] = ammo;
		this.ID = ID;
		this.cooldown[1] = cooldown;
		this.coolRate[1] = coolRate;
		image[0] = imageX;
		image[1] = imageY;
		this.damage = damage;
	}
	/*
	 * Returns string description (for saves)
	 */
	public String toString() {
	String line = "";
	
	return line;
	}
	
	/*
	 * decreases ammo by 1 and return true if ammo is not empty
	 */
	public boolean fire() {
		
		if(ammo[0] > 0 && cooldown[0] + coolRate[0]==0) {
			ammo[0]--;
			coolRate[0]=coolRate[1];
			return true;
		}
		return false;
	}
	
	public void setAmmo(int ammo) {
		this.ammo[0] = ammo;
	}
	
	/*
	 * adds ammo into weapon and return remain ammo
	 */
	public void reload(Ammo aAmmo) {
		ammo[0] = aAmmo.loadAmmo(ammo[0]);
		cooldown[0] = cooldown[1];
	}
	
	/*
	 * 
	 */
	public int getMaxAmmo() {
		return ammo[1];
	}
	
	public int getID() {
		return ID;
	};
	
	public int getCoolDown() {
		return cooldown[0];
	}
	
	public int getCoolDownAmount() {
		return cooldown[1];
	}
	
	public int getCoolRate() {
		return coolRate[0];
	}
	
	public int getTiles(int num) {
		return image[num];
	}

	public int getDamage() {
		return damage;
	}
	
	public int coolD() {
		if(cooldown[0]>0)
			cooldown[0]--;
		if(coolRate[0]>0)
			coolRate[0]--;
		return cooldown[0]+coolRate[0];
	}
	
	public boolean isLoad() {
		if(ammo[0]>0 && cooldown[0]==0 && coolRate[0]==0)
			return true;
		return false;
		
	}

	public int getAmmo() {
		return ammo[0];
	}
	public void setAmmo(int ammo[]) {
		this.ammo = ammo;
	}
	public void refresh() {
		ammo[0] = ammo[1];
	}

}

