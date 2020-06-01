package Game.Saves;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.Game.Engine.GameContainer;

import Game.GameManager;

public class Storage{

	
	public static void createSave(String name, GameManager gm) {
		
		String path;
		
		path = GameContainer.getMainPath() + "saves/" + name;
		try {
			copyFolder(new File(GameContainer.getMainPath()+"levels"), new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static String loadSave(String path) {
		try{
			path = GameContainer.getMainPath() + path + "\\main.save";
			
			@SuppressWarnings({ "resource" })
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			String data = "", temp;
			
			data = "";
			br = new BufferedReader(new FileReader(path));
			
			while((temp = br.readLine()) != null) {
				data += temp +"\n";
			}
			
			return data;
		}catch (IOException e) {
			e.printStackTrace();
			return "";
		}	
	}
	
	public static void saveLevel(String data, String path) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(path, "UTF-8");
			pw.print(data);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateSave(GameManager gm, String name){
		 try {
			 
				String path = GameContainer.getMainPath() + gm.getDirectory() + "\\" + name;
				
				if(!name.equals(gm.getCurrentSaveName())) {
					
					String old = gm.getCurrentGameSaveFile();
					File newF = new File(path);
					
					copyFolder(new File(old), newF);
					
					deleteSave(old);
					
					gm.setCurrentSaveName(name);
					path = gm.getCurrentGameSaveFile();
				}

				String save = "LevelName:" + gm.getLevel() + "\n";
				
				saveLevel(save+gm.getAllObjects().toString() + "\n", path + "\\" + gm.getLevel() + "\\main.save");
				
				path += "/main.save";
				PrintWriter pw = new PrintWriter(path, "UTF-8");
				pw.print(save);
				pw.close();
				
			}catch (FileNotFoundException e){
				
			}catch (IOException e) {
				System.out.println(e);
				System.out.println("Ошибка записи файла");
			}
		}
	
	/*
	 * path suppose to be in 'GAME_FOLDER/saves(or building)/name' form
 	* */	
	public static void deleteSave(String path) {
		File folder = new File(path);
		
		if(!folder.exists())
			return;
		
		File[] listOfFiles = folder.listFiles();
		
        for(int i = 0; i < listOfFiles.length; i++) {
        	if(listOfFiles[i].isDirectory()) 
        		deleteSave(listOfFiles[i].getAbsolutePath());
        	
    		listOfFiles[i].delete();
        }
        
        folder.delete();
       
	}

    public static void copyFolder(File src, File dest)
    	throws IOException{

    	if(src.isDirectory()){

    		//if directory not exists, create it
    		if(!dest.exists()){
    		   dest.mkdir();
    		   //System.out.println("Directory copied from " + src + "  to " + dest);
    		}

    		//list all the directory contents
    		String files[] = src.list();

    		for (String file : files) {
    		   //construct the src and dest file structure
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   //recursive copy
    		   copyFolder(srcFile,destFile);
    		}

    	}else{
    		//if file, then copy it
    		//Use bytes stream to support all file types
    		InputStream in = new FileInputStream(src);
    	        OutputStream out = new FileOutputStream(dest);

    	        byte[] buffer = new byte[1024];

    	        int length;
    	        //copy the file content in bytes
    	        while ((length = in.read(buffer)) > 0){
    	    	   out.write(buffer, 0, length);
    	        }

    	        in.close();
    	        out.close();
    	        //System.out.println("File copied from " + src + " to " + dest);
    	}
    }

    
}
