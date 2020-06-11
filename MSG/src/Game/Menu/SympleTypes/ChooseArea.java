package Game.Menu.SympleTypes;

import java.awt.event.KeyEvent;

import com.Game.Engine.GameContainer;
import com.Game.Enums.Levels;

import Game.GameManager;

public class ChooseArea extends SimpleButton {

	private String[] values;
	private int currentPos;
	
	
	public ChooseArea() {
		super("");
		loadValues();
		currentPos = 0;
		name = values[currentPos];
	}

	private void loadValues() {
		Levels[] val = Levels.values();
		values = new String[val.length];
		for(int i = 0; i < val.length; i++) {
			values[i] = val[i]+"";
		}
	}	
	
	@Override
	public void update(int mouseX, int mouseY, boolean mouseManaging, GameContainer gc, GameManager gm) {
		super.update(mouseX, mouseY, mouseManaging, gc, gm);
		int temp = currentPos;
		if(gc.getInput().isKeyUp(KeyEvent.VK_D)) {
			if(currentPos<values.length-1) {
				currentPos++;
			}
		}else if(gc.getInput().isKeyUp(KeyEvent.VK_A)){
			if(currentPos>0) {
				currentPos--;
			}
		}
		if(temp != currentPos)
			name = values[currentPos];
	}
}
