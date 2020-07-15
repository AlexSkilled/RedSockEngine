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
import com.Game.Enums.CameraStates;

import Game.GameManager;

public class Storage{

	
	public static void createSave(String name, GameManager gm) {
		
		String path;
		
		path = GameContainer.getMainPath() + "saves/" + name;
		try {
			copyFolder(new File(GameContainer.getMainPath()+"levels"), new File(path), "png ".split(" "));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static String loadMap(String name){
		try{
			String path = GameContainer.getMainPath() + GameManager.getDirectory() + name + ".save";
			
			@SuppressWarnings({ "resource" })
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			String data = "", temp;
			
			data = "";
			br = new BufferedReader(new FileReader(path));
			
			while((temp = br.readLine()) != null) {
				data += temp +"\n";
			}
			
			return data;
		}catch(FileNotFoundException e2){
			e2.printStackTrace();
			return null;
		}catch (IOException e) {
			e.printStackTrace();
			return null;
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
	
	public static void updateSave(String name, GameManager gm){
		 try {
				String path = GameContainer.getMainPath() + GameManager.getDirectory() + name;
				File newF = new File(path);
				
				
				if(!name.equals(gm.getCurrentSaveName()+gm.getLevel())) {
					String old = gm.getCurrentGameSaveFile();
					
					copyFolder(new File(old), newF, null);
					
					deleteSave(old);
					
					gm.setCurrentSaveName(name);
					path = gm.getCurrentGameSaveFile();
				}
				
				String save = "LevelName:" + gm.getLevel() + "\n" + GameManager.TS +"\n"+gm.getAllObjects().toString();
				
				path += gm.getLevel() + ".save";
				PrintWriter pw = new PrintWriter(path, "UTF-8");
				pw.print(save);
				pw.close();
				
			}catch (FileNotFoundException e){
				
			}catch (IOException e) {
				System.out.println(e);
				System.out.println("Ошибка записи файла");
			}
		}
	
	
	
	public static void updateSaveWithCamera(String name, GameManager gm){
		 try {
			 
				String path = GameContainer.getMainPath() + GameManager.getDirectory() + name;
				File newF = new File(path);
				
				if(newF.isDirectory())
					if(!name.equals(gm.getCurrentSaveName())) {
						
						String old = gm.getCurrentGameSaveFile();
						
						copyFolder(new File(old), newF, null);
						
						deleteSave(old);
						
						gm.setCurrentSaveName(name);
						path = gm.getCurrentGameSaveFile();
					}

				String save = "LevelName:" + gm.getLevel() + "\n" + gm.getAllObjects().toString();

				boolean xLess = gm.getLevelW()*GameManager.TS<GameContainer.getWidth();
				boolean yLess = gm.getLevelH()*GameManager.TS<GameContainer.getHeight();
				
				if(xLess || yLess) {
					int camX, camY;
					camX =- (GameContainer.getWidth()/3 - gm.getLevelW()/2);
					camY =- (GameContainer.getHeight()/3 - gm.getLevelH()/2);
					String type;
					if(xLess && !yLess)
						type=""+CameraStates.XFIXED;
					else if(!xLess && yLess)
						type=""+CameraStates.YFIXED;
					else
						type=""+CameraStates.FULLFIXED;
					save+="Camera " + type + " " + camX + " " + camY + "\n";
				}
				
				path += ".save";
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

    public static void copyFolder(File src, File dest, String[] exceptions)
    	throws IOException{
    	int amountOfExceptions;
    	
    	if(exceptions == null)
    		amountOfExceptions =- 1;
    	else
    		amountOfExceptions = exceptions.length;
    	
    	if(src.isDirectory()){

    		//if directory not exists, create it
    		if(!dest.exists()){
    		   dest.mkdir();
    		   //System.out.println("Directory copied from " + src + "  to " + dest);
    		}

    		//list all the directory contents
    		String files[] = src.list();
    		String extension;
    		outer:
    		for (String file : files) {
    			if(new File(src+"\\"+file).isFile()) {
    				extension = file.split("\\.")[1];
	    			for(int i = 0; i < amountOfExceptions; i++) {
		    			if(extension.equals(exceptions[i])) {
		    				continue outer;
		    			}
	    			}
	    		}

    		   //construct the src and dest file structure
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   //recursive copy
    		   copyFolder(srcFile, destFile, exceptions);
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
