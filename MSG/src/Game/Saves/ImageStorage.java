package Game.Saves;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.Game.Engine.GameContainer;

import Game.GameManager;

public class ImageStorage {
	
	/*
	 * int[] envData - data of enviroment
	 * Item[] items - items on ground
	 */
	public static void saveLevel(short[][] envData, int w, int h, String path, GameManager gm) {
		
		int[] data = new int[w*2*h];
		int ID, x2, y2;
		//uploading enviroment
		for(int y = 0; y < h; y++)
			for(int x = 0; x < w; x++) {
					for(int i = 0; i < envData[x+y*w].length; i++ ) {
						ID = envData[x+y*w][i];
						x2 = ID % GameManager.getESX();
						y2 = (ID - x2) / GameManager.getESX();
							data[x+ w*(i) +y*w*2] = Integer.parseInt("08"
																+ (x2 > 9 ? "":"0") + Integer.toString(x2)
																+ (y2 > 9 ? "":"0") + Integer.toString(y2),
														   16);
						
				}
			}
		//player position
		int x = gm.getPlayer().getTileX();
		int y = gm.getPlayer().getTileY();
		
		ID = envData[x+y*w][0];
		
		x2 = ID % GameManager.getESX();
		y2 = (ID - x2) / GameManager.getESX();
		data[x+y*w*2] = Integer.parseInt("fe"+ 
									(x2 > 9 ? "":"0") + Integer.toString(x2)+
									(y2 > 9 ? "":"0") + Integer.toString(y2), 
								16);

		
		//uploading 
		BufferedImage result = new BufferedImage(w*2, h, BufferedImage.TYPE_INT_RGB);
		result.setRGB(0, 0, w*2, h, data, 0, w*2);
		File file = new File(path);
		try {
			ImageIO.write(result, "png", file);
		} catch (IOException e) {
			GameContainer.addError(e);
			e.printStackTrace();
		}
	}
}
