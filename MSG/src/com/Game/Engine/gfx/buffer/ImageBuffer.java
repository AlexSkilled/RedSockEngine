package com.Game.Engine.gfx.buffer;

import com.Game.Engine.gfx.GFX;
import com.Game.Engine.gfx.Image;
import com.Game.Engine.gfx.ImageTile;

import Game.GameManager;

public class ImageBuffer {
	
	private static GFX[] images = new GFX[20];
	private static Images[] names = new Images[20];
	private static int num = 0;
	private static Integer textureSize;
	
	public static void clear() {
		for(int i = 0; i < num; i++) {
			images[i] = null;
			names[i] = null;
		}
	}

	public static void resize() {
		textureSize = GameManager.TS;
		for(int i = 0; i < num; i++) {
			images[i].resize();
		}
	}
	
	public static GFX load(Images name) {
		if(textureSize == null)
			textureSize = 32;
		
		for(int i = 0; i < num; i++)
			if(names[i].equals(name))
				return images[i];

		switch(name) {
		case Items:
			images[num] = new ImageTile("textures/32/static/items/Items.png", textureSize, textureSize);
			break;
		case player:
			images[num] = new ImageTile("textures/32/moving/HeroSprites.png", textureSize, textureSize);
			break;
		case Machine:
			images[num] = new Image("textures/32/static/world/saveMachine.png");
			break;
		case zombie:
			images[num] =  new ImageTile("textures/32/moving/ZombieSprites.png", textureSize, textureSize);
			break;
		case callOut:
			images[num] =  new ImageTile("textures/32/static/world/CallOut.png", 32, 64);
			break;
		case GreyBack:
			images[num] =  new Image("textures/greyBack.png", 16, 16);
			images[num].resize();
			break;
		case enviroment:
			images[num] =  new ImageTile("textures/32/static/world/world.png", textureSize, textureSize);
			images[num].resize();
			break;
		default:
			images[num] =  new Image("textures/Error.png", 16, 16);
			images[num].resize();
			break;
		}
		
		names[num] = name;
		num++;
		
		if(num == images.length) {
			//CHECK IF WORKS
			GFX[] temp2 = new GFX[images.length + 5];
			temp2 = images;
			images = temp2;
		}
		return images[num-1];
	}

	public static Images getName(GFX objectImage) {
		for(int i = 0; i < num; i++)
			if(images[i] == objectImage)
				return names[i];
		return null;
	}
}
