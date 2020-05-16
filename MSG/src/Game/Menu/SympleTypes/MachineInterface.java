package Game.Menu.SympleTypes;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.Menu.Menus;
import Game.Menu.SimpleMenuManager;

public class MachineInterface extends SimpleSecondMenu{
	
	public MachineInterface() {
		super(Menus.MachinInventory);
		name = "ObjectInterface";
		onScreen = false;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, SimpleMenuManager menuManager) {
		super.update(gc, gm, menuManager);
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
	}

	public void setOffY(int offY) {
		this.offY = offY;
	}

}
