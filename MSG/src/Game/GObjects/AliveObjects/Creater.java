package Game.GObjects.AliveObjects;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.Image;
import com.Game.Engine.gfx.ImageTile;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;

import Game.GameManager;
import Game.GObjects.CreaterInventory;
import Game.GObjects.Item;
import Game.GObjects.ItemCreater;

public class Creater extends Player{

	public Creater(int posX, int posY, GameManager gm) {
		super(posX, posY);
		noClip = false;
		speed = 1000;
		loadInvent(gm);
		destX = 0;
		destY = 0;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		camOffX = (int) gm.getCamera().getOffX();
		camOffY = (int) gm.getCamera().getOffY();
		
		//checking buttons for movements
		moving(gc, gm, dt);
		
		destX = gc.getInput().getMouseX() + camOffX;
		destY = gc.getInput().getMouseY() + camOffY;
		//checking LB for shooting
		invent.useItem(gc, gm, dt, destX, destY);
		
		//direction where player is looking
		changePlayerDirection(gc, dt);
		
		//checking ground under Player for picking up items
		checkPos(gm, gm.getGround());
		
		invent.update(gc, gm, dt);
		
		finalPos();
		
		if(gc.getInput().isKeyUp(KeyEvent.VK_N))
			noClip = !noClip;
		
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
	}
	
	private void loadInvent(GameManager gm) {
		ArrayList<Item> items = new ArrayList<Item>();
		
		ImageTile image = (ImageTile) ImageBuffer.load(Images.enviroment);
		Image tempImage;
		int w = image.getW()/image.getTileW(), h = image.getH()/image.getTileH();
		
		for(int y = 0; y < h; y++)
			for(int x = 0; x < w; x++) {
				tempImage = image.getTileImage(x, y);
				if(Image.checkImage(tempImage.getW(), tempImage.getH(), tempImage.getP())) {
					items.add(ItemCreater.create(100 + x + y * w, gm));
				}
			}
		
		image = (ImageTile) ImageBuffer.load(Images.Items);
		w = image.getW()/image.getTileW();
		h = image.getH()/image.getTileH();
		
		for(int y = 0; y < h; y++)
			for(int x = 0; x < w; x++) {
				tempImage = image.getTileImage(x, y);
				if(Image.checkImage(tempImage.getW(), tempImage.getH(), tempImage.getP())) {
					items.add(ItemCreater.create(x + y*w, gm));
				}
			}
		invent = new CreaterInventory(items.size(), this);
		for(int i = 0; i < items.size(); i++) 
			invent.addItem(items.get(i));
	}
	
	@SuppressWarnings("resource")
	protected void loadFromFile(GameManager gm) {
		BufferedReader br = null;
		String[] data = null;
		try {
			br = new BufferedReader(new FileReader(GameContainer.getMainPath()+"settings/Items.cfg"));
			String temp;
			String file = "";
			while((temp = br.readLine()) != null)
				file+= temp+ "\n";
			data = file.split("\n");
		} catch (FileNotFoundException e) {
			GameContainer.addError(e);
			e.printStackTrace();
			return;
		} catch (IOException e) {
			GameContainer.addError(e);
			e.printStackTrace();
			return;
		}
		
		invent = new CreaterInventory(data.length, this);
		for(int i = 0; i < data.length; i++) {
			invent.addItem(ItemCreater.create(Integer.parseInt(data[i].split(" ")[0]), gm));
		}
	}
	@Override
	public String toString() {
		return "AliveObject Player " + posX + " " + posY + " 100 100 0 player \n" + 
				"null:null:null:null:null:null:null:null:";
	}
}
