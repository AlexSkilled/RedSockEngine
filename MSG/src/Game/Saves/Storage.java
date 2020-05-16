package Game.Saves;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.Game.Engine.GameContainer;

import Game.GameManager;

public class Storage{

	public static void loadSave(String name, GameManager gm, String path) {
		try{
			@SuppressWarnings({ "resource" })
			BufferedReader br = new BufferedReader(new FileReader(path));
			String data = "", temp;
			
			while((temp = br.readLine()) != null) {
				data += temp +"\n";
			}
			
			gm.loadGame(data);
			return;
		}catch (IOException e) {
			e.printStackTrace();
			return;
		}	
	}
	
	public static void createSave(GameManager gm, String name){
		 try {
				String save = "LevelName:" + gm.getLevel() + "\n";
				
				save += gm.getAllObjects().toString() + "\n";
				
				PrintWriter pw = new PrintWriter(GameContainer.getMainPath() + "saves/" + name + ".save", "UTF-8");

				System.out.println(save);
				pw.print(save);
				pw.close();
				
			}catch (FileNotFoundException e){
				
			}catch (IOException e) {
				System.out.println(e);
				System.out.println("Ошибка записи файла");
			}
		}
	
	public static void deleteSave(String path) {
		new File(path).delete();
	}
}
