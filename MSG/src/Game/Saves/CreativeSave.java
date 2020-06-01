package Game.Saves;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.Game.Engine.GameContainer;
import com.Game.Enums.Levels;

import Game.GameManager;

public class CreativeSave {
	
	public static String loadSaved(GameManager gm, Levels level) {
		try{
			
			String path = GameContainer.getMainPath() + "building/" + level + "/" + level + ".save";
			@SuppressWarnings({ "resource" })
			BufferedReader br = new BufferedReader(new FileReader(path));
			String data = "", temp;
			
			while((temp = br.readLine()) != null) {
				data += temp +"\n";
			}
			return data;
		}catch (IOException e) {
			e.printStackTrace();
			return "";
		}	
	}
	
	public static void createSave(GameManager gm, Levels level){
		 try {
			 	
				String save = "LevelName:" + gm.getLevel() + "\n";
				
				save += gm.getAllObjects().toString() + "\n";
				
				PrintWriter pw = new PrintWriter(GameContainer.getMainPath() + "building/" + level + "/" + level +".save", "UTF-8");

				pw.print(save);
				pw.close();
				
			}catch (FileNotFoundException e){
				
			}catch (IOException e) {
				System.out.println(e);
				System.out.println("Ошибка записи файла");
			}
		}
}
