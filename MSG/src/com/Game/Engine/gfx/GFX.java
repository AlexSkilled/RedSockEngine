package com.Game.Engine.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.Game.Engine.GameContainer;

import Game.GameManager;

public abstract class GFX {
	
	protected int w, h;
	private int[] p;
	private boolean alpha = true;
	private int lightBlock = Light.NONE;
	protected String name;
	protected int defaultRes[];
	
	public GFX(String path) {
		BufferedImage image;
		try {
			if(path.substring(0, 1).equals("C")) {
				image = ImageIO.read(new File(path));
			}
			else {
				image = ImageIO.read(new File(GameContainer.getMainPath() + path));
			}
			w = image.getWidth();
			h = image.getHeight();
			p = image.getRGB(0, 0, w, h, null, 0, w);
			name = path;
			image.flush();
		}catch (IllegalArgumentException | IOException e) {
			GameContainer.addError(e);
		}
		
	}
	
	public GFX(String path, int resX, int resY) {
		
		try {
			BufferedImage image;
			if(path.substring(0, 1).equals("C")) {
				image = ImageIO.read(new File(path));
			}
			else {
				image = ImageIO.read(new File(GameContainer.getMainPath() + path));
			}
			w = image.getWidth();
			h = image.getHeight();
			p = image.getRGB(0, 0, w, h, null, 0, w);
			defaultRes = new int[2];
			defaultRes[0] = resX;
			defaultRes[1] = resY;
			name = path;
			image.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GFX(int[] p, int w, int h) {
		this.p = p;
		this.h = h;
		this.w = w;
	}
	
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int[] getP() {
		return p;
	}
	public void setP(int[] p) {
		this.p = p;
	}
	public boolean isAlpha() {
		return alpha;
	}
	public void setAlpha(boolean alpha) {
		this.alpha = alpha;
	}

	public int getLightBlock() {
		return lightBlock;
	}

	public void setLightBlock(int lightBlock) {
		this.lightBlock = lightBlock;
	}

	public String getName() {
		return name;
	}
	
	public void resize(double resizeW, double resizeH) {
		try {
			BufferedImage image;
			if(name.substring(0, 1).equals("C")) {
				image = ImageIO.read(new File(name));
			}
			else {
				image = ImageIO.read(new File(GameContainer.getMainPath() + name));
			}
			w = image.getWidth();
			h = image.getHeight();
			p = image.getRGB(0, 0, w, h, null, 0, w);
			image.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//new sizes of resized image
		int resdH = (int) (h*resizeH), 
				resdW = (int) (w*resizeW); 
		int[] newArray = new int[resdH*resdW];;
		
		for(int i = 0; i < h; i++)
			for(int y = 0; y < resizeH; y++)
				for(int j = 0; j < w; j++)
					for(int x = 0; x < resizeW; x++)
						newArray[(int) (j * resizeW) + x + ((int) (i * resizeH) + y) * ((int) (w * resizeW))] = p[j + i * w];
		w = (int) (w * resizeW);
		h = (int) (h * resizeH);
		p = newArray;
	}

	public void resize() {
		resize((double) GameManager.TS / (double) defaultRes[0], (double) GameManager.TS / (double) defaultRes[1]);
	}
	
	public int[] getDefaultRes() {
		return defaultRes;
	}
	
	public void downgrade(double resizeW, double resizeH) {
		int[] newArray;
		/*
		newArray = new int[(int) (w) * (int) (h)];
		
		int startX = (int) (w*(1-resizeW))*2;
		int startY = (int) (h*(1-resizeH))*2;
		
		for(int i = startY; i < h; i++)
			for(int y = 0; y < resizeH; y++)
				for(int j = startX; j < w; j++)
					for(int x = 0; x < resizeW; x++)
						newArray[(int) (j * resizeW) + x + ((int) (i * resizeH) + y) * ((int) (w * resizeW))] = p[j + i * w];
		*/
		int resdH = (int) (h*resizeH), 
				resdW = (int) (w*resizeW); 	
		
		newArray = new int[resdH*resdW];
		
		int startX, startY;
		
		startX = w/2;
		startY = h/2;
		
		for(int y = startY; y >= 0; y--) {
			for(int j = 0; j < resizeH; j++)
				for(int x = startX; x >= 0; x--) {
					for(int i = 0; i < resizeW; i++)
						newArray[(int) (x * resizeW) + i + ((int) (y * resizeH) + j) * resdW] = p[x+y*w];
			}
			for(int j = 0; j < resizeH; j++)
				for(int x = startX; x < w; x++) {
					for(int i = 0; i < resizeW; i++)
						newArray[(int) (x * resizeW) + i + ((int) (y * resizeH) + j) * resdW] = p[x+y*w];
			}
		}
		for(int y = startY; y < h; y++) {
			for(int j = 0; j < resizeH; j++)	
				for(int x = startX; x >= 0; x--) {
					for(int i = 0; i < resizeW; i++)
						newArray[(int) (x * resizeW) + i + ((int) (y * resizeH) + j) * resdW] = p[x+y*w];
			}
			for(int j = 0; j < resizeH; j++)
				for(int x = startX; x < w; x++) {
					for(int i = 0; i < resizeW; i++)
						newArray[(int) (x * resizeW) + i + ((int) (y * resizeH) + j) * resdW] = p[x+y*w];
			}
		}
		
		/*
		int nStartX = (int) (w*(1-resizeW))*2;
		int nstartY = (int) (h*(1-resizeH))*2;
		
		for(int x = 0; x < resdH; x++)
				for(int y = 0; y < resdW; y++)
						p[x+startX+(y+startY)*w] = newArray[(x) + (y)*resdW];
						//newArray[(int) (j * resizeW) + x + ((int) (i * resizeH) + y) * ((int) (w * resizeW))] = p[j + i * w];
		*/
		int dX = (w - resdW)/2;
		int dY = (h - resdH)/2;
		p = new int[w*h];
		for(int y = 0; y < resdH; y++)
			for(int x = 0; x < resdW; x++)
				p[x + dX +(y+dY)*w] = newArray[x+y*resdW]; 
		//w = (int) (w * resizeW);
		//h = (int) (h * resizeH);
		//p = newArray;
		
		
	}
	
	public static boolean checkImage(int w, int h, int[] p) {
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				if(p[x+y*w] != 16777215)
					return true;
			}
		}
		return false;
	}
}
