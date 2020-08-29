package Game;

import java.io.BufferedReader;
import java.io.FileReader;

import com.Game.Engine.GameContainer;

public class Runner {
	
	public static void main(String args[]) {
		
		int width = 800, height = 480, TS = 32;
		Double scale = 2.0;
		
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(GameContainer.getMainPath() + "settings/config/start.cfg"));
			String temp, line = "";
			while((temp = br.readLine()) != null)
				line += temp+"\n";
			
			String[] configData = line.split("\n");
			
			width = Integer.parseInt(configData[0].split(" ")[1]);
			height = Integer.parseInt(configData[1].split(" ")[1]);
			scale = Double.parseDouble(configData[2].split(" ")[1]);
			TS = Integer.parseInt(configData[3].split(" ")[1]);
			
		} catch (Exception e) {
			e.printStackTrace();
			GameContainer.addError(e);
		}
		
		if(width < 800 || height < 400 || scale < 1) {
			width = 800;
			height = 480;
			scale = 2.0;
			TS = 32;
		}
		
		GameManager gm = new GameManager(TS);
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

