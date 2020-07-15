package Game.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;
import com.Game.Enums.CameraStates;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GObjects.AliveObjects.Player;
import Game.Menu.SympleTypes.InsertArea;
import Game.Saves.Storage;

public class WorkingConsole extends Info{
	
	private String lastCommand;
	private static ArrayList<String> feed;
	private ArrayList<String> commands;
	private InsertArea input;
	
	public WorkingConsole() {

		turned = false;
		
		back = ImageBuffer.load(Images.GreyBack);
		feed = new ArrayList<String>();
		commands = new ArrayList<String>();
		
		feed.add("*");
		commands.add("");
		
		turned = false;
		position = 0;
		bgSize = GameManager.TS;
		width = 9;
		height = 11;
		
		input = new InsertArea(20, 200, 9, 1, 0xffffffff, 0xff000000){
			
			@Override
			public void update(int newX, int newY, boolean mouseManaging, GameContainer gc, GameManager gm) {
				x = newX;
				y = newY;
				lastButton = gc.getInput().getPressed();
				if(lastButton.length() == 1 && name.length() < 32) {
					name += lastButton;
				}
				else {
					switch(lastButton) {
					case "Space":
						name += " ";
						break;
					case "Up":
						name = orderCommand(false);
						break;
					case "Down":
						name = orderCommand(true);
						break;
					case "Enter":
						execute(gm, name.split(" "));
						name = "";
						break;
					case "Backspace":
						if(name.length() > 0)
							name = name.substring(0, name.length()-1);
						break;
					case "Escape":
						gc.getInput().turn();
						turn();
						break;
					case "Slash":
						gc.getInput().turn();
						turn();
						break;
					case "Minus":
						name+="-";
						break;
					}
				}
			}
			
			@Override
			public void render(GameContainer gc, Renderer r) {
				r.drawFillRect(x, y, width*GameManager.TS, height*GameManager.TS/2, colorInner);
				r.drawRect(x, y, width*GameManager.TS, height*GameManager.TS/2, colorOuter);
				
				r.drawText(name, x + 5, y + 3, 0xff000000, false, 0.75f, 0.75f);
			}
		};
		input.setFocused(true);
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(turned) {
			x = 20 + (int) gm.getCamera().getOffX();
			y = (int) ((float) (0.15f * (GameContainer.getHeight()/GameManager.TS))*GameManager.TS) 
					+ (int) gm.getCamera().getOffY();
			input.update(x, y, true, gc, gm);
			
			switch (gc.getInput().getScroll()) {
			case -1:
				if(offSet > 0) {
					offSet--;
				}
				break;
			case 1:
				if(offSet < feed.size() - 20) {
					offSet++;
				}
				break;
			}
		}
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		if(turned) {
			for(int i = 0; i < width; i++)
				for(int j = 0; j < height; j++)
					r.drawImage(back, x + i * (bgSize), y + 16 + j *  (bgSize));
			for(int i = offSet; i < feed.size(); i++)
				r.drawText(feed.get(i), x + 10, y + 20 * (i-offSet), 0xff000000, false, 0.75f, 0.75f);
			
			input.render(gc, r);
		}
	}

	public String orderCommand(boolean down) {

		if(down) {
			position += position > 0 ? -1 : 0;
		}else {
			position += position < commands.size() - 1 ? 1 : 0;
		}
		
		return commands.get(position);
	}
	
	private void execute(GameManager gm, String[] command) {
		
		lastCommand =  String.join(" ", command);
		if(lastCommand.length() == 0)
			return;
		
		feed.add("\n" + lastCommand);
		commands.add(1, lastCommand);
		position = 0;
		
		try {
			ConsoleCommands cmd = ConsoleCommands.valueOf(command[0]);
			
			String line;
			switch(cmd) {
			case SAVE:
				if(command.length > 1)
					line = command[1];
				else {
					line = (new Date()).toString();
					String[] saveNameS = line.split(" ");
					line = saveNameS[1] + saveNameS[2] 
							+ "_" + saveNameS[3].split(":")[0] + "_" + saveNameS[3].split(":")[1];
				}
				Storage.updateSave(line, gm);
				break;
				
			case CREATE:
				if(command.length > 1) {
					Player player = gm.getPlayer();
					int plX = 0, plY = 0;
					switch(player.getDirection()){
					case 0:
						plX = 1;
						break;
					case 1:
						plX = -1;
						break;
					case 2:
						plY = 1;
						break;
					case 3:
						plY = -1;
						break;
					default:
						break;
					}
					gm.getGround().putOnGround(Integer.parseInt(command[1]),
							player.getTileX() + plX, player.getTileY() + plY, gm);
				}else
					throwError("CREATE ITEM_ID");
				break;
				
			case NOCLIP:
				Player player = gm.getPlayer();
				if(player != null)
					player.noClip();
				else
					throwError("Player doesn't exist");
				break;
			case SPEED:
				if(command.length > 1) {
					Player player1 = gm.getPlayer();
					if(player1 != null)
						player1.setSpeed(Integer.parseInt(command[1]));
					else
						throwError("Player doesn't exist");
				}
				else
					throwError("SPEED SPEED_AMOUNT");
				break;
			case SAVEMAP:
					gm.saveMap();
				break;
			case OVERRIDEMAP:
					gm.saveMap();
					break;
			case CAMERA:
				int errorID = -1;
				try {
					switch(ConsoleCommands.valueOf(command[1])) {
					case FIX:
						errorID = 0;
						gm.fixiseCamera(CameraStates.valueOf(command[2]));
						break;
					case SAVE:
						errorID = 1;
						Storage.updateSaveWithCamera(gm.getLevel()+"", gm);
						break;
					case CHANGEOBJECTTAG:
						errorID = 2;
						gm.getCamera().setTargetTag(Objects.valueOf(command[2]));
						break;
					default:
						errorID = 3;
						gm.getCamera().setOffY(Integer.parseInt(command[2]));
						gm.getCamera().setOffX(Integer.parseInt(command[1]));
					}
				}catch(IndexOutOfBoundsException e) {
					switch(errorID) {
					case -1:
						throwError("UNKNOW ERROR");
						break;
					case 0:
						throwError("CAMERA FIX CAMERA STATE");
						break;
					case 1:
						throwError("SAVE");
						break;
					case 2:
						throwError("CAMERA CHANGEOBJECTTAG OBJECT_TAG");
						break;
					case 3:
						throwError("CAMERA OFFX OFFY");
						break;
					}
				}
				break;
			default:
				throwError("********************\n"
					+ "No such command"
				+ "\n------------------------");
				break;
			}
		
		}catch (IllegalArgumentException e) {
			throwError("********************\n"
						+ "No such command_error"
					+ "\n------------------------");
			e.printStackTrace();
			
		}
	}
	
	public static void throwError(String line) {
		feed.addAll(Arrays.asList(line.split("\n")));
	}

	@Override
	public void refresh() {
		bgSize = GameManager.TS + GameManager.TS/8;
	}
	
	public void turn() {
		turned = !turned;
	}

}
