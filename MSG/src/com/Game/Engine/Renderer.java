package com.Game.Engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.Game.Engine.gfx.Font;
import com.Game.Engine.gfx.GFX;
import com.Game.Engine.gfx.ImageRequest;
import com.Game.Engine.gfx.ImageTile;
import com.Game.Engine.gfx.Light;
import com.Game.Engine.gfx.LightRequest;

import Game.GameManager;

public class Renderer {
	//класс для отрисовки всего что будет в игре
	private Font font = Font.STANDART;//загрузка шрифта в память
	private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();
	private ArrayList<LightRequest> lightRequest = new ArrayList<LightRequest>();
	
	private int pixelWidth, pixelHeight;//размеры
	private int[] pixels;//массив с пикселями
	private int[] zb;
	private int texureSize;
	private int ambientColor = 0xff2b2b2b;
	private int[] lm;
	private int[] lb;

	private static boolean[][] newLB;
	private int zDepth = 0;
	private boolean processing = false;
	private int camX, camY, camX2, camY2;
	
	public Renderer(GameContainer gc) {
		//конструктор отрисовки
		texureSize = GameManager.TS;
		pixelWidth = GameContainer.getWidth();
		pixelHeight = GameContainer.getHeight();
		pixels = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
		zb = new int[pixels.length];
		lm = new int[pixels.length];
		lb = new int[pixels.length];
	}
	
	public void refresh(GameContainer gc) {
		texureSize = GameManager.TS;
		pixelWidth = GameContainer.getWidth();
		pixelHeight = GameContainer.getHeight();
		pixels = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
		zb = new int[pixels.length];
		lm = new int[pixels.length];
		lb = new int[pixels.length];
	}
	
	public void clear() {
		//очистка экрана
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i]=0;
			zb[i] = 0;
			lm[i] = ambientColor;
			lb[i] = ambientColor;
		}
	}
	
	public void process() {
		
		processing = true;
		
		Collections.sort(imageRequest, new Comparator<ImageRequest>() {

			@Override
			public int compare(ImageRequest i0, ImageRequest i1) {
				if(i0.zDepth<i1.zDepth) return -1;
				if(i0.zDepth>i1.zDepth) return 1;
				return 0;
			}
			
		}
		);
		
		for(int i = 0;i < imageRequest.size(); i++) {
			ImageRequest ir = imageRequest.get(i);
			setzDepth(ir.zDepth);
			drawImage(ir.image, ir.offX, ir.offY);
		}
		
		//Draw lighting
		for(int i = 0; i < lightRequest.size();i++) {
			LightRequest l = lightRequest.get(i);
			drawLightRequest(l.light, l.locX, l.locY);
		}
		
		for(int i = 0; i < pixels.length; i++) {
			
			float r = ((lm[i] >> 16) & 0xff)/255f; 
			float g = ((lm[i] >> 8) & 0xff)/255f; 
			float b = (lm[i] & 0xff)/255f; 
		
			pixels[i] = ((int)(((pixels[i] >> 16) & 0xff) * r) << 16 | (int)(((pixels[i] >> 8) & 0xff) * g) << 8 | (int)((pixels[i] & 0xff) * b));
		}
		
		
		
		imageRequest.clear();
		lightRequest.clear();
		processing = false;
	}
	
	public void setPixel(int x, int y, int value) {
		//отрисовка пикселя, если он в экране
		
		int alpha = (value >>24) & 0xff;
				
		if((x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) || alpha == 0 ) {
			return;
		}
		
		int index = x + y * pixelWidth;
	
		if(zb[index]>zDepth)
			return;
		
		zb[index] = zDepth;
		
		if(alpha == 255) {
			pixels[index]= value;
		}else {
			int pixelColor = pixels[index];
			
			int newRed = ((pixelColor>>16) & 0xff) - (int) ((((pixelColor>>16) & 0xff)-((value>>16) & 0xff))*(alpha/255f));
			int newGreen = ((pixelColor>>8) & 0xff) - (int) ((((pixelColor>>8) & 0xff)-((value>>8) & 0xff))*(alpha/255f));
			int newBlue = (pixelColor & 0xff) - (int) (((pixelColor & 0xff)-(value & 0xff))*(alpha/255f));
			
			pixels[index]= (newRed<<16 | newGreen<<8 | newBlue );
		}
	}
	
	public void setLightMap(int x, int y, int value) {
		
		if(x<0|| x>=pixelWidth || y < 0 || y >= pixelHeight) {
			return;
		}
		
		int baseColor = lm[x+y*pixelWidth];
		
		int maxRed = Math.max((baseColor >> 16) & 0xff, ((value >> 16) & 0xff));
		int maxGreen = Math.max((baseColor >> 8) & 0xff, ((value >> 8) & 0xff));
		int maxBlue = Math.max(baseColor & 0xff, value & 0xff);
		
		lm[x+y*pixelWidth] = maxRed<<16 | maxGreen<<8 | maxBlue ;
		
	}
	
	public void setLightBlock(int x, int y, int value) {
				
		if(x < -texureSize|| x >= pixelWidth+texureSize || y < -texureSize || y >= pixelHeight + texureSize) return;
		
		if(zb[x + y * pixelWidth] > zDepth) return;
		
		lb[x+y*pixelWidth] = value;
		
	}
	
	public void drawText(String text, int offX, int offY, int color, boolean centered) {
		//отрисовка текста
		
		offX -= camX;
		offY -= camY;
		
		//text = text.toUpperCase();
		int offSet = 0;
		int rescaleX = texureSize/16;
		int rescaleY = texureSize/16;
		
		if(centered) {
			for(int i = 0; i < text.length();i++) {
				int unicode = text.codePointAt(i);
				offSet -= font.getWidths()[unicode] * rescaleX;
			}
			offSet /= 2;
		}
		
		for(int i = 0; i < text.length();i++) {
			int unicode = text.codePointAt(i); //-32
			
			for(int y = 0; y < font.getFontImage().getH();y++) {
				for(int yR = 0; yR < rescaleY; yR ++) {
						
					for (int x = 0; x < font.getWidths()[unicode];x++) {
						for(int xR = 0; xR < rescaleX; xR++) {
							
							if(font.getFontImage().getP()[x + font.getOffsets()[unicode] + y * font.getFontImage().getW()] == 0xffffffff) {
								setPixel(x*rescaleX + xR + offX + offSet, y*rescaleY + yR + offY, color);
							}
						}
					}
				}
			}
			offSet += font.getWidths()[unicode] * rescaleY;
		}
	}
	
	public void drawText(String text, int offX, int offY, int color, boolean centered, float rescaleX, float rescaleY) {
		//отрисовка текста
		
		offX -= camX;
		offY -= camY;
		
		//text = text.toUpperCase();
		int offSet = 0;
		
		if(centered) {
			for(int i = 0; i < text.length();i++) {
				int unicode = text.codePointAt(i);
				offSet -= font.getWidths()[unicode] * rescaleX;
			}
		
			offSet /= 2;
		}
			
			int unicode;
			int width, height;
			for(int i = 0; i < text.length(); i++) {		
				unicode = text.codePointAt(i);

				width = font.getWidths()[unicode];
				height = font.getFontImage().getH();
				for(int y = 0; y < height;y++) {
					for(int yR = 0; yR < rescaleY; yR ++) {
							
						for (int x = 0; x < width; x++) {
							for(int xR = 0; xR < rescaleX; xR++) {
								
								if(font.getFontImage().getP()[x + font.getOffsets()[unicode] + y * font.getFontImage().getW()] == 0xffffffff) {
									setPixel((int) (x * rescaleY + xR + offSet) + offX, (int) (y * rescaleX + yR) + offY, color);
								}
							}
						}
				}
			}
			offSet += font.getWidths()[unicode]*rescaleY;
		}
	}
	
	public void drawImage(GFX image, int offX, int offY) {
		//Метод отрисовки не постоянного изображения 
		offX -= camX;
		offY -= camY;
		if(image.isAlpha() && !processing) {
			imageRequest.add(new ImageRequest(image, zDepth, offX, offY));
			return;
		}
		
		//Не рисует если вне экрана игры
		if(offX < -texureSize) 
			return;
		if(offY < -texureSize) 
			return;
		if(offX >= pixelWidth+texureSize) 
			return;
		if(offY >= pixelHeight + texureSize) 
			return;
		
		int newX = 0;
		int newY = 0;
		int newWidth = image.getW();
		int newHeight = image.getH();
		
		if(offX < 0) 
			newX -= offX;
		if(offY < 0) 
			newY -= offY;
		if(newWidth + offX > pixelWidth) 
			newWidth -= newWidth + offX - pixelWidth;		
		if(newHeight + offY > pixelHeight) 
			newHeight -= newHeight + offY - pixelHeight;
		for(int y = newY; y < newHeight; y++) {
			for(int x = newX; x < newWidth; x++) {
				setPixel((int) (x + offX) ,(int) (y + offY), image.getP()[x + y * image.getW()]);
				setLightBlock((int) (x + offX) , (int) (y + offY), image.getLightBlock());
			}
		}
	}
	
	
	public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {
		//анимации
		
		offX -= camX;
		offY -= camY;

		if(image.isAlpha() && !processing) {
			imageRequest.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
			return;
		}
		
		//Не рисует если вне экрана игры
		
		if(offX < -texureSize) return;
		if(offY < -texureSize) return;
		if(offX >= pixelWidth+texureSize) return;
		if(offY >= pixelHeight+texureSize) return;
		
		int newX = 0;
		int newY = 0;
		int newWidth = image.getTileW();
		int newHeight = image.getTileH();
		
		
		if(offX<0) 
			newX -= offX ;
		if(offY<0)
			newY -= offY ;
		if(newWidth + offX > pixelWidth) 
			newWidth -= newWidth + offX - pixelWidth;		
		if(newHeight + offY > pixelHeight)
			newHeight -= newHeight + offY - pixelHeight;
		
		for(int y = newY; y < newHeight; y++) {
			for(int x = newX; x < newWidth; x++) {
				setPixel(x + offX, y + offY,
						image.getP()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getW()]);	
				setLightBlock(x + offX, y + offY, image.getLightBlock());
			}
		}
	}
	
	public void drawRect(int offX, int offY, int width, int height, int color) {
	
		offX -= camX;
		offY -= camY;
		
		int rescale = texureSize/16;
		
		for(int y = 0; y <= height;y++) {
			for(int yR = 0; yR < rescale; yR++) {
				setPixel(offX + yR, y + offY, color);
				setPixel(offX + width + yR, y + offY, color);
			}
		}
		
		for(int x = 0; x <= width; x++) {
			for(int xR = 0; xR<rescale; xR++) {
				setPixel(x + offX, offY + xR , color);
				setPixel(x + offX, offY + xR +height, color);
			}
		}
	}
	
	/**
	 * 
	 * @param offX
	 * @param offY
	 * @param width
	 * @param height
	 * @param color in 0xff00ff00 format
	 */
	public void drawFillRect(int offX, int offY, int width, int height, int color) {
		
		offX -= camX;
		offY -= camY;
		
		if(offX < -width) return;
		if(offY < -height) return;
		if(offX >= pixelWidth) return;
		if(offY >= pixelHeight) return;
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++) {
				setPixel(x + offX, y + offY, color);
			}
		}
	}

	public void drawLight(Light l, int offX, int offY) {
		offX -= camX2;
		offY -= camY2;
		lightRequest.add(new LightRequest(l, offX, offY));
	}
	
	private void drawLightRequest(Light l, int offX, int offY) {
		
		for(int i = 0; i <= l.getDiameter(); i++) {
			drawLightLine(l, l.getRadius(), l.getRadius(), i, 0, offX, offY);
			drawLightLine(l, l.getRadius(), l.getRadius(), i, l.getDiameter(), offX, offY);
			drawLightLine(l, l.getRadius(), l.getRadius(), 0, i, offX, offY);
			drawLightLine(l, l.getRadius(), l.getRadius(), l.getDiameter(), i, offX, offY);
		}
	}
	
	private void drawLightLine(Light l, int x0, int y0, int x1, int y1, int offX, int offY) {
		
		int dx = Math.abs(x1-x0);
		int dy = Math.abs(y1-x0);
		
		int sx = x0 < x1 ? 1:-1;
		int sy = y0 < y1 ? 1:-1;
		
		int err = dx-dy;
		int e2;
		while(true) {
			int screenX = x0 - l.getRadius() + offX;
			int screenY = y0 - l.getRadius() + offY;
			int lightColor = l.getLightValue(x0, y0);
			
			if(screenX < -l.getRadius() || screenX >= pixelWidth+l.getRadius() || screenY < -l.getRadius() || screenY >= pixelHeight+l.getRadius()) {
				return;
			}
			
			if(lightColor == 0)  {
				return;
			}
			e2 = screenX + screenY*pixelWidth;

			int x3 = (screenX+camX2)/texureSize, y3 = (screenY+camY2)/texureSize;
			
			if(x3 < newLB.length && x3>=0 && y3<newLB[x3].length && y3>=0)
				if(newLB[x3][y3]) 
					return;
			setLightMap(screenX, screenY, lightColor);
			
			if(x0 == x1 && y1 == y0) 
				break;
			
			e2 = 2*err;
			
			if(e2>-1*dy) {
				err -=dy;
				x0 += sx;
			}
			
			if(e2<dx) {
				err+=dx;
				y0+=sy;
			}
					
		}
	}
	

	public int getzDepth() {
		return zDepth;
	}


	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}


	public int getAmbientColor() {
		return ambientColor;
	}


	public void setAmbientColor(int ambientColor) {
		this.ambientColor = ambientColor;
	}


	public int getCamX() {
		return camX;
	}


	public void setCamX(int camX) {
		if(camX>0 || this.camX==0) camX2=camX;
		this.camX = camX;
	}

	public int getCamY() {
		return camY;
	}

	public void setCamY(int camY) {
		if(camY>0 || this.camY==0) camY2=camY;
		this.camY = camY;
	}

	public static void setLighBlockMap(boolean[][] lbMap) {
		newLB = lbMap;
	}

	public Font getFont() {
		return font;
	}

	public static void updateCollisionMap(int x, int y, boolean value) {
		try {
			newLB[x][y] = value;
		}catch (ArrayIndexOutOfBoundsException e) {
			
		}
	}
	
}
