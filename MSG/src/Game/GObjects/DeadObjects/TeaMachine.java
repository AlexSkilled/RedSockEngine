package Game.GObjects.DeadObjects;

import java.awt.event.KeyEvent;
import java.util.Date;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Enums.Objects;

import Game.GameManager;
import Game.GObjects.Entity;
import Game.Menu.Menus;
import Game.Menu.SimpleMenuManager;
import Game.Menu.SympleTypes.ComplexButton;
import Game.Menu.SympleTypes.InsertArea;
import Game.Menu.SympleTypes.MachineInterface;
import Game.Menu.SympleTypes.SimpleButton;
import Game.Menu.callouts.CallOut;
import Game.Saves.Storage;

public class TeaMachine extends Entity{
	
	private boolean menuOpened;
	private MachineInterface mi;
	private boolean said;
	private CallOut callOut;
	
	public TeaMachine(int posX, int posY) {
		super(20, posX, posY);
		
		tag = Objects.teaMachine;
		mi = new MachineInterface();
		mi.setOffX(-200);
		mi.setOffY(40);
		mi.setSubOffY(40);
		
		ComplexButton saveButton = new ComplexButton("Save") {
			public void act(GameManager gm, GameContainer gc, SimpleMenuManager menuManager) {
				
				String saveName = (new Date()).toString();
				String[] saveNameS = saveName.split(" ");
				saveName = saveNameS[1] + saveNameS[2] 
						+ "_" + saveNameS[3].split(":")[0] + "_" + saveNameS[3].split(":")[1];
				
				if(subButtons.size() != 0)
					for(SimpleButton button : subButtons)
						if(button instanceof InsertArea)
							if(!button.getName().equals(""))
								saveName = button.getName();
				Storage.updateSave(saveName, gm);
				gm.continueGame();
				gc.getInput().turnOffInserting();
				menuOpened = false;
				
				menuManager.updateScene(Menus.PauseMenu);
			}
		};
		saveButton.addSubButton(new InsertArea());

		mi.changeBackButton(new SimpleButton("Back") {
			public void act(GameManager gm, GameContainer gc, SimpleMenuManager menuManager) {
				menuManager.setWorkingBackGround(false);
				menuManager.updateScene(Menus.PauseMenu);
				gm.continueGame();
				gc.getInput().turnOffInserting();
				menuOpened = false;
			}
		});
		mi.addButton(saveButton);
		mi.flush();
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(gm.isCollide("player", posX, posY)) {
			if(gc.getInput().isKeyUp(KeyEvent.VK_E)) {
				gm.pauseGame();		
				menuOpened = true;
				gm.getMenuManager().updateScene(mi);
				if(callOut != null)
					callOut = null;	
			}else {
				if(!said) {
					callOut = new CallOut(gm.getPlayer(), "'E' to save game");
					said = true;
				}
			}
		}else{
			said = false;
			if(callOut != null)
				if(callOut.isDead())
					callOut = null;
		}
		
		if(callOut != null)
			callOut.update(gc, gm, dt);
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_ESCAPE) && menuOpened) {
			menuOpened = false;
			gm.continueGame();
			gm.getMenuManager().updateScene(Menus.PauseMenu);
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
		
		if(callOut != null)
			callOut.render(gc, r);
	}
	
}
