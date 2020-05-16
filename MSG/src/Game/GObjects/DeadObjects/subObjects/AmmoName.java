package Game.GObjects.DeadObjects.subObjects;

public enum AmmoName {
	PISTOL(7),
	SHOTGUN(2),
	M4A1(30);
	
	int maxAmmo;
	private AmmoName(int maxAmmo) {
		this.maxAmmo = maxAmmo;
	}
	
	public int getMax() {
		return maxAmmo;
	}
	
	
}
