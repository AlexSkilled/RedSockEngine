package Game;

import Game.GObjects.Entity;
import Game.GObjects.Item;
import Game.GObjects.ItemCreater;

public class Ground{
	
	private Item[] items;
	private Entity[] entities;
	private int width, height;
	
	public Ground(int levelW, int levelH) {
		width = levelW;
		height = levelH;
		items = new Item[width*height];
		entities = new Entity[width*height];
	}
	
	/*
	 * method drops items on ground
	 */
	/**
	 * @param ID
	 * @param x
	 * @param y
	 * @param gm
	 */
	public void putOnGround(int ID, int x, int y, GameManager gm) {

		if(items[x + y * width] == null && !gm.getCollision(x, y) && !gm.getPlayer().isCollide(x, y)) {
			items[x + y * width] = ItemCreater.create(ID, x, y, gm);
		}
		else {
			int num = 2;
			outerloop:
			while(true) {
				for(int i = -num/2 ; i < num/2; i++) {
					for(int j = -num/2 ; j < num/2; j++) {
						if(x + i >= 0 && y + j >= 0) {
							//if(items[x + i + (y + j) * width] == null && !gm.getCollision(x+i, y+j) && !gm.getPlayer().isCollide(x+i, y+j)) {
							if(items[x + i + (y + j) * width] == null)
								if(!gm.getCollision(x+i, y+j))
										if(!gm.getPlayer().isCollide(x+i, y+j))
											items[x + i + (y + j) * width] = ItemCreater.create(ID, (x+i), (y+j), gm);
											break outerloop;
							}
						}
					}	
				num += 2;
				}
			}
	}
	
	
	/*
	 * method drops items from .save files on ground
	 */
	public void putOnGround(String[] data, GameManager gm) {
		int x = (int) (Float.parseFloat(data[1]) / GameManager.TS);
		int y = (int) (Float.parseFloat(data[2]) / GameManager.TS);
		Item item = ItemCreater.create(Integer.parseInt(data[0]), x, y, gm);
		if(item!=null)
			items[x + y * width] = item;
		else
			System.out.println("Error while loading " + String.join(" ", data) + " object");
	}
	
	/*
	 * use when player picks up item	
	 */
	public void deleteObject(int x, int y, GameManager gm) {
		gm.deleteObject(items[x+y*width]);
		items[x + y * width] = null;
	}
	/*
	 * returns an item located at (tileX;tileY)
	 */
	public Item getItem(int x, int y) {
		return items[x + y*width];
	}

	public Item[] getItems() {
		return items;
	}

	public void drop(Item item, int x, int y, GameManager gm) {
		if(items[x+y*width] == null) {
			item.setInPocket(false);
			item.setTileX(x);
			item.setTileY(y);
			items[x+y*width] = item;
			gm.addObject(items[x+y*width]);
		}
	}

	public void putEntity(Entity entity) {
		entities[entity.getTileX()+entity.getTileY()*width] = entity;
	}
	
	public void actEntity(int x, int y) {
		
	}
}
