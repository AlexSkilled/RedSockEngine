package Game.Saves;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import com.Game.Engine.GameContainer;

import Game.GameManager;

public class ConfigStorage{

	public static void updateConfig(String data) {
		try {
			String dataD[] = data.split(" ");
			String save = 	"Width: " + dataD[0] + "\r\n" + 
							"Height: " + dataD[1] + "\r\n" + 
							"Scale: " + dataD[2] + "\r\n" + 
							"TS: " + dataD[3] + "\r\n";	
	        
	        PrintWriter pw = new PrintWriter(GameContainer.getMainPath() + "settings/config/start.cfg", "UTF-8");
			pw.print(save);
			pw.close();
			
		}catch (FileNotFoundException e){
			
		}catch (IOException e) {
			System.out.println(e);
			System.out.println("Ошибка записи конфига");
		}
	}
	
	public static void updateConfig() {
		updateConfig(GameContainer.getWidth() + " "
				+ GameContainer.getHeight() + " "
				+ GameContainer.getScale() + " "
				+ GameManager.TS);
	}
	
}
