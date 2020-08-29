package com.Game.Engine.gfx.buffer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.Game.Engine.GameContainer;
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

	private static GFX load(String path, String name) {
		File dir = new File(GameContainer.getMainPath() + path);
		File[] files = dir.listFiles();
		
		List<File> dirs = new ArrayList<File>();
		GFX response = null;	//new Image("textures/Error.png", 16, 16);

		for(File f : files) {
			if(f.isDirectory()) {
				dirs.add(f);
			}else {
				
				if(name.equals(f.getName())) {
					path += "/" + f.getName();
					if(path.contains("moving")) {
						response =  new ImageTile(path, textureSize, textureSize);
						break;
					}else {
						response = new Image(path);
						break;
					}
				}
			}
		}

		if(response == null) {
			for(File d : dirs) {
				response = load(path + d.getName()+"/", name);
				if(response != null)
					break;
			}
		}
		if(response != null && !response.getName().contains("/originTextures/")){
			response.resizeIfNotFitTS();
		}
		
		return response;
	}
	
	public static GFX load(Images name) {
		if(textureSize == null)
			textureSize = 32;
		String path  = "textures/";

		for(int i = 0; i < num; i++)
			if(names[i].equals(name))
				return images[i];

		images[num] = load(path, name + ".png");
		
		if(images[num]==null) {
			images[num] = new Image("textures/32/static/menu/Error.png", 16, 16);
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
	
	public static GFX load(Images name, boolean alpha) {
		GFX image = load(name);
		image.setAlpha(alpha);
		return image;
	}

	public static GFX loadCopy(Images name) {
		String path  = "textures/";
		GFX toReturn = null;
		toReturn = load(path, name + ".png");
		return toReturn;
	}
	
	public static Images getName(GFX objectImage) {
		for(int i = 0; i < num; i++)
			if(images[i] == objectImage)
				return names[i];
		return null;
	}
}
