package Game;

import java.awt.event.KeyEvent;
import java.util.Date;

import com.Game.Engine.AbstractGame;
import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.GFX;
import com.Game.Engine.gfx.GFXMap;
import com.Game.Engine.gfx.Image;
import com.Game.Engine.gfx.ImageTile;
import com.Game.Engine.gfx.Light;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;
import com.Game.Enums.Levels;
import com.Game.Enums.Mod;
import com.Game.Enums.Objects;

import Game.GObjects.AliveObject;
import Game.GObjects.Entity;
import Game.GObjects.AliveObjects.Creater;
import Game.GObjects.AliveObjects.Player;
import Game.GObjects.AliveObjects.Enemies.Zombie;
import Game.GObjects.DeadObjects.Bullet;
import Game.GObjects.DeadObjects.Door;
import Game.GObjects.DeadObjects.TeaMachine;
import Game.GObjects.DeadObjects.LightSources.LightSource;
import Game.Menu.Info;
import Game.Menu.InfoBar;
import Game.Menu.SimpleMenuManager;
import Game.Menu.WorkingConsole;
import Game.Saves.CreativeSave;
import Game.Saves.ImageStorage;
import Game.Saves.Storage;

public class GameManager extends AbstractGame {

	public static int TS = 64, lastTS = 64; //one Tile in pixels
	public static boolean pDimension = false;
	
	private Ground playGround; 				//map of items lying on floor
		
	private GameObjects gameObjects;
	
	private Levels levelName;
	private String currentSaveName, directory;
	
	private SimpleMenuManager menuManager;
	private boolean draw;
	private Info[] console;
	
	private ProgrammObject curObj;
	
	private short[][] collisionMap;
	private Image[] enviromentTexture;
	
	private static Integer envDeltaX, envDeltaY, collisionStop;;
	
	private int levelW, levelH;
	
	private boolean gameStarted;			/*true if game launched. 
											Used for identify is game should be on screen or menu; */
	private Camera camera;
	/**
	 * 
	 * @param fixed
	 * is used for showing grid
	 */
	public GameManager(int TS) {
		GameManager.TS = TS;
		ImageBuffer.load(Images.enviroment);
	}

	/**
	 * @param gc is GameContainer object
	 * @param dt if float variable
	 */
	@Override
	public void update(GameContainer gc, float dt){

		dt = dt * ((float) TS / 32);
		if(gc.getInput().isKeyUp(KeyEvent.VK_ESCAPE) && gameStarted){
			menuManager.turn();
		}
		
		if(gc.getInput().isKeyUp(KeyEvent.VK_SLASH) && gameStarted){
			gc.getInput().turn();
			console[0].turn();
		}
		
		if(gc.getInput().isKeyUp(KeyEvent.VK_F3) && gameStarted){
			console[1].turn();
		}
		
		if((!menuManager.isTurnedOn() && !console[0].isTurnedOn()) && gameStarted) {
			camera.update(gc, this, dt);
			curObj.update(gc, this, dt);		
		}
		else
			if(menuManager.isTurnedOn()){
				menuManager.update(gc, this, dt);
			}
		
		for(int i = 0; i < console.length; i++)
			console[i].update(gc, this, dt);
			
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		draw = false;
		
		if(gameStarted) {
			camera.render(r);
			
			if((!menuManager.isTurnedOn() || (menuManager.isTurnedOn() && menuManager.isBackground()))) {
				draw = true;
			}
			
			int cur;
			if(draw) {
				for (int y = 0; y < levelH; y++) {
					for (int x = 0; x < levelW; x++) {
						cur = x + y * levelW;
						for(int i = 0; i < collisionMap[cur].length; i++)
							r.drawImage(enviromentTexture[collisionMap[cur][i]],
								x*TS, y*TS);
					}
				}
				gameObjects.render(gc, r);
			}
		}
		
		if(menuManager.isTurnedOn()){		
			menuManager.render(gc, r);
		}
		
		for(int i = 0; i < console.length; i++)
			console[i].render(gc, r);
	}
	
	public boolean isGameStarted() {
		return gameStarted;
	}
	
	public void startNewGame() {
		curObj = gameObjects;
		
		menuManager.setTurnedOn(false);
		
		this.levelName = Levels.city_backs_1;
		gameObjects.clear();
		
		camera = new Camera(Objects.player);
		
		currentSaveName = (new Date()).toString();
		String[] saveNameS = currentSaveName.split(" ");
		
		currentSaveName = saveNameS[1] + saveNameS[2] 
				+ "_" + saveNameS[3].split(":")[0] + "_" + saveNameS[3].split(":")[1];

		directory = "saves\\";
		
		loadGame();
		
		gameStarted = true;
	}
	
	public void loadGame() {
		Storage.createSave(currentSaveName, this);
		loadGame(Storage.loadSave(directory+currentSaveName));
	}
	
	public void loadGame(String directory, String currentSaveName) {
		this.currentSaveName = currentSaveName;
		this.directory = directory;
		loadGame(Storage.loadSave(directory+currentSaveName));
	}
	
	private void loadGame(String data){
		String[] info = data.split("\n");
		this.levelName = Levels.valueOf(info[0].split(":")[1]); //loading name of the current level
		loadGameObjects(Storage.loadSave(directory + currentSaveName + "\\" + levelName + "\\"));
	}
	
	private void loadGameObjects(String data) {
		
		String[] info = data.split("\n");
		//loading background and setting wall collision
		loadLevel(GameContainer.getMainPath() + directory + currentSaveName + "\\" + levelName + "\\" + levelName, false);	
		
		gameObjects.clear();
		String[] currObject;
		
		for(int i = 1; i < info.length; i++) {
			currObject = info[i].split(" ");
			switch(currObject[0]) {
			
			//check if it is AliveObj
			case "AliveObject":
				currObject = deleteFirst(currObject);
				AliveObject tempAO = null;
				//check what kind of AO it is
				switch(currObject[0]) {
					case "Enemy":
						currObject = deleteFirst(currObject);
						//check what kind of Enemy it is
						switch(currObject[0]) {
							case "Zombie":
								currObject = deleteFirst(currObject);
								tempAO = new Zombie(String.join(" ", currObject));
								break;
							}
							break;
							
					case "Player":
						currObject = deleteFirst(currObject);
						tempAO = new Player(String.join(" ", currObject));
						break;
						
						
				}
				if(tempAO!=null) {
					tempAO.loadInventory(info[++i].split(":"), this);
					gameObjects.add(tempAO);
				}
				break;
				
			case "Entity":
				currObject = deleteFirst(currObject);
				Entity entity = null;
				switch(currObject[0]) {
					case "20":
						currObject = deleteFirst(currObject);
						entity = new TeaMachine(Integer.parseInt(currObject[0]),
								Integer.parseInt(currObject[1]));
						break;
					case "21":
						currObject = deleteFirst(currObject);
						entity = new Door((int) Float.parseFloat(currObject[0]),
								(int) Float.parseFloat(currObject[1]), Levels.valueOf(currObject[2]));
						break;
					case "25":
						currObject = deleteFirst(currObject);
						entity = new LightSource(Integer.parseInt(currObject[0]),
												Integer.parseInt(currObject[1]),
												Float.parseFloat(currObject[2]),
												Integer.parseInt(currObject[3], 16));
						break;
					default:
						System.out.println("Error while loading " + i + " line \nNo such entity");
						break;
				}
				if(entity != null) {
					gameObjects.add(entity);
					playGround.putEntity(entity);
				}
				break;
				
			case "Item":
				currObject = deleteFirst(currObject);
				playGround.putOnGround(currObject, this);
				break;
			}
		}
		
		curObj = gameObjects;
		
		menuManager.setTurnedOn(false);

		camera = new Camera(Objects.player);
		
		gameStarted = true;
	}
	
	
	private String[] deleteFirst(String[] array) {
		String[] array2 = new String[array.length-1];
		for(int i = 0; i < array2.length; i++)
			array2[i] = array[i+1];
		return array2;
 	}
	
	public void loadGameLevel(Levels levelName, Mod mod) {
		curObj = null;
		gameObjects.clear();
		
		this.levelName = levelName;
		this.currentSaveName = "";
		
		if(mod.equals(Mod.creater))
			directory = "building\\";
		loadGameObjects(Storage.loadSave(directory + levelName.toString() + "\\"));
		
		int x = getPlayer().getTileX();
		int y = getPlayer().getTileY();
		
		switch(mod) {
		case creater: 
			gameObjects.add(new Creater(x, y, this));
			break;
		case player:
			gameObjects.add(new Player(x, y));
			break;
		case spectator:
			gameObjects.add(new Creater(x, y, this));
			break;
		default:
			gameObjects.add(new Creater(x, y, this));
			break;
		}
		
		camera = new Camera(Objects.player);
		
		menuManager.setTurnedOn(false);
		
		curObj = gameObjects;

		gameStarted = true;

	}
	
	public void stopGame() {
		gameStarted = false;
		menuManager.setTurnedOn(true);
		
		camera = null;
		gameObjects.clear();
		curObj = null;
	}
	
	public void pauseGame() {
		menuManager.setTurnedOn(true);
	}
	
	public void continueGame() {
		menuManager.setTurnedOn(false);
	}
	
	/**
	 * Initialize game, sets AmbientColor to -1
	 * @param gc is GameContainer object
	 */
	
	@Override
	public void init(GameContainer gc) {
		gameObjects = new GameObjects();
		menuManager = new SimpleMenuManager();
		
		console = new Info[2];
		console[0] = new WorkingConsole();
		console[1] = new InfoBar();
	}
	
	/**
	 * Loads level from .png image from path
	 * 
	 * @param path
	 * 	path that loads level
	 * @return
	 * returns nothing it's the void function you idiot
	 */
	private void loadLevel(String path, boolean loadStuff) {
		
		//String path = GameContainer.getMainPath() + "levels\\" + levName;
		ImageTile tempImage = (ImageTile) ImageBuffer.load(Images.enviroment);
		
		envDeltaX = tempImage.getW()/tempImage.getTileW();
		envDeltaY = tempImage.getH()/tempImage.getTileH();
		collisionStop = envDeltaX * envDeltaY / 2;
		
		enviromentTexture = new Image[(tempImage.getW()/tempImage.getTileW())*(tempImage.getH()/tempImage.getTileH())];
		
		for(int y = 0; y < tempImage.getH()/tempImage.getTileH(); y++)
			for(int x = 0; x < tempImage.getH()/tempImage.getTileH(); x++){
				enviromentTexture[x + y * envDeltaX] = tempImage.getTileImage(x, y);
				if(x + y * envDeltaX < collisionStop)
					enviromentTexture[x + y * envDeltaX].setLightBlock(Light.FULL);
			}
		GFX levelImage = new GFXMap(path+".png");
		
		int levelWFull = levelImage.getW();
		levelW = levelWFull / 2;
		levelH = levelImage.getH();
		
		collisionMap = new short[levelW * levelH][1];
		
		playGround = new Ground(levelW, levelH);
		
		for (int y = 0; y < levelH ; y++) {
			for (int x = levelW; x < levelW * 2; x++) {
				switch (Integer.toHexString(levelImage.getP()[x + y * levelWFull]).substring(2, 4)) {
				case "08":
					collisionMap[(x-levelW) + y * levelW] = new short[2];
					collisionMap[(x-levelW) + y * levelW][1] = 
					(short) (Integer.parseInt(Integer.toHexString(levelImage.getP()[x + y * levelWFull]).substring(4, 6))
					+
					Integer.parseInt(Integer.toHexString(levelImage.getP()[x + y * levelWFull]).substring(6, 8))*envDeltaX);
					break;
				default:
					collisionMap[(x-levelW) + y * levelW] = new short[1];
					break;
				}
			}
		
		}
		
		for (int y = 0; y < levelH; y++) {
			for (int x = 0; x < levelW; x++) {
				
				collisionMap[x + y * levelW][0] = 60;
				
				switch (Integer.toHexString(levelImage.getP()[x + y * levelImage.getW()]).substring(2, 4)) {
				
				/*Player*/
				case "fe":
					if(loadStuff) {
						getPlayer().setPosX(x*TS);
						getPlayer().setPosY(y*TS);
					}
					collisionMap[x + y * levelW][0] = 
							(short) (Integer.parseInt(Integer.toHexString(levelImage.getP()[x + y * levelImage.getW()]).substring(4, 6))
							+
							Integer.parseInt(Integer.toHexString(levelImage.getP()[x + y * levelImage.getW()]).substring(6, 8))*envDeltaX);
					break;
				/*Environment*/
				case "08":
					collisionMap[x + y * levelW][0] = 
					(short) (Integer.parseInt(Integer.toHexString(levelImage.getP()[x + y * levelImage.getW()]).substring(4, 6))
					+
					Integer.parseInt(Integer.toHexString(levelImage.getP()[x + y * levelImage.getW()]).substring(6, 8))*envDeltaX);
					break;
				case "fa":
					collisionMap[x + y * levelW][0] = (byte) (
							(byte) Integer.parseInt(Integer.toHexString(levelImage.getP()[x + y * levelImage.getW()]).substring(6, 8)));
					break;
				}
			}
		}
		
		boolean array[][] = new boolean[levelW][levelH];
		for(int i = 0; i < levelW; i++)
			for(int j = 0; j < levelH; j++)
				array[i][j] = enviromentTexture[collisionMap[i+j*levelW][0]].getLightBlock()==1;
		Renderer.setLighBlockMap(array);
	}
	
	/**
	 * Iterates thought bullets in the world 
	 * @param posX position of object in X
	 * @param posY position of object in Y
	 * @return Returns True if bullet is in [posX; posY]
	 */
	public Bullet getBulletCollision(float posX, float posY) {
		return gameObjects.getBulletCollision(posX, posY);
	}
	
	public void addObjectToBegin(GameObject object) {
		gameObjects.addObjectToBegin(object);
	}
	
	public void addObject(GameObject object) {	
		gameObjects.add(object);
	}
	
	public GameObject getObject(Objects tag) {
		return gameObjects.getObject(tag);
	}
	
	
	
	public GameObject getObjectAt(int x, int y) {
		return gameObjects.getObjectAt(x, y);
	}
	
	public boolean getCollision(int x, int y) {
		if (x < 0 || x >= levelW || y < 0 || y >= levelH)
			return true;
		return (collisionMap[x+y*levelW][0]  < collisionStop);
	}
	
	public boolean isCollide(String tag, float x, float y) {
		
		GameObject object = gameObjects.getObject(Objects.player);
		if((object.getPosX() + TS/2 >= x &&  object.getPosX() <= x + TS/2)
				&&
			(object.getPosY() + TS/2 >= y && object.getPosY() <= y + TS/2)){
			return true;
		}
	
		return false;
	}

	/**
	 * Used for getting current playground
	 *@return playGround 
	 */
	public Ground getGround() {
		return playGround;
	}
	
	public int getLevelW() {
		return levelW;
	}

	public int getLevelH() {
		return levelH;
	}

	public SimpleMenuManager getMenuManager() {
		return menuManager;
	}
	public Levels getLevel() {
		return levelName;
	}
	public Camera getCamera() {
		return camera;
	}
	
	public void setTS(int TS) {
		lastTS = GameManager.TS;
		GameManager.TS = TS;
		gameObjects.refresh();
		ImageBuffer.resize();
		console[0].refresh();
	}
	
	public GameObject getEntityInRadius(int tileX, int tileY, int radius) {
		return null;
	}

	public void refresh() {
		gameObjects.refresh();
	}
	
	public short[][] getAllCollision() {
		return collisionMap;
	}

	public GameObjects getAllObjects() {
		return (GameObjects) gameObjects;
	}

	public Player getPlayer() {
		return gameObjects.getPlayer();
	}
	
	public void setBlock(float xIn, float yIn, int id) {
		int x = (int) xIn;
		int y = (int) yIn;
		
		if(x+y*levelW < collisionMap.length && x+y*levelW >= 0)
			if(id > (collisionStop/2) && (id<collisionStop) || id > ((collisionStop*3)/2) - 1 && (id<collisionStop * 2)) {
				if(collisionMap[x + y*levelW].length > 1) {
					
					int[] data = {0, 0, 0, 0}; 	//left up right down
					int adress;
					for(int i = -1; i < 2; i+=2) {	
						
						adress = x + i + y * levelW;
						
						if(adress >= 0 && adress < collisionMap.length) {
							if(collisionMap[adress].length > 1) {
								if(((int) (id/4))*4 == ((int) (collisionMap[adress][1]/4)*4)) {
									if(i < 0)
										data[0] = 1;
									else
										data[2] = 1;
								}
							}
						}
						
						adress = x + (y + i) * levelW;
						
						if(collisionMap[adress].length > 1) {
							if(((int) (id/4))*4 == ((int) (collisionMap[adress][1]/4)*4)) {
								if(i < 0)
									data[1] = 1;
								else
									data[3] = 1;
							}
						}
					}
					collisionMap[x + y*levelW][1] = (short) id;
				}
				else {
					short temp = collisionMap[x + y*levelW][0];
					collisionMap[x + y*levelW] = new short[2];
					collisionMap[x + y*levelW][0] = temp;
					collisionMap[x + y*levelW][1] = (short) id;
				}
			}else {
				collisionMap[x + y*levelW] = new short[1];
				collisionMap[x + y*levelW][0] = (short) id;
			}
	}
	
	public void saveMap() {
		ImageStorage.saveLevel(collisionMap, levelW, levelH, 
				GameContainer.getMainPath() + "building/" + levelName + "/"+levelName+".png",
				this);
		CreativeSave.createSave(this, levelName);
	}

	public short getBlockAt(int destX, int destY) {
		return collisionMap[destX+destY*levelW][0];
	}

	public static int getESX() {
		if(envDeltaX == null)
			countEnviroment();
		return envDeltaX;
	}

	public static int getESY() {
		if(envDeltaY == null)
			countEnviroment();
		return envDeltaY;
	}

	public static int getCollisionStop() {
		if(collisionStop == null)
			countEnviroment();
		return collisionStop;
	}

	private static void countEnviroment() {
		ImageTile tempImage = (ImageTile) ImageBuffer.load(Images.enviroment);
		envDeltaX = tempImage.getW()/tempImage.getTileW();
		envDeltaY = tempImage.getH()/tempImage.getTileH();
		collisionStop = envDeltaX *envDeltaY/2;
	}
	
	public void deleteObject(GameObject object) {
		gameObjects.delete(object);
	}

	public float getCameraOffX() {
		return camera.getOffX();
	}
	
	public float getCameraOffY() {
		return camera.getOffY();
	}

	public void changeLevel(Levels level) {
		
	}
	public void setCurrentSaveName(String name) {
		currentSaveName = name;
	}
	public String getCurrentSaveName() {
		return currentSaveName;
	}

	public String getCurrentGameSaveFile() {
		return GameContainer.getMainPath()+directory+currentSaveName;
	}

	public String getDirectory() {
		return directory;
	}
}
