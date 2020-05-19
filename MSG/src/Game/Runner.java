package Game;

import java.io.BufferedReader;
import java.io.FileReader;

import com.Game.Engine.GameContainer;

public class Runner {
	
	public static void main(String args[]) {
		
		int width = 800, height = 480, TS = 32;
		float scale = 2f;
		
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(GameContainer.getMainPath() + "settings/config/start.cfg"));
			
			width = Integer.parseInt(br.readLine().split(" ")[1]);
			height = Integer.parseInt(br.readLine().split(" ")[1]);
			scale = (((float) 900/height) + ((float) 1600/width))/2;
			TS = (int) Math.floor(width/25);
			
		} catch (Exception e) {
			e.printStackTrace();
			GameContainer.addError(e);
		}
		
		GameManager gm = new GameManager(TS);
		//gm.setTS(TS);
		GameContainer gc = new GameContainer(gm);	
		
		gc.setWidth(width);
		gc.setHeight(height);
		
		gc.setScale(scale);
		
		try {
			gc.start();
		}
		catch(Exception e) {
			GameContainer.addError(e);
		}
	}
}

