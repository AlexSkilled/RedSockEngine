package Game.Menu.SympleTypes;

import java.util.ArrayList;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;

public class ComplexButton extends SimpleButton{

	protected ArrayList<SimpleButton> subButtons;
	
	public ComplexButton(String name) {
		super(name);
		subButtons = new ArrayList<SimpleButton>();

	}
	public void update(int mouseX, int mouseY, boolean mouseManaging, GameContainer gc, GameManager gm) {
		super.update(mouseX, mouseY, mouseManaging, gc, gm);
		
		if(choosen)
			for(SimpleButton subButton : subButtons)
				subButton.update(mouseX, mouseY, mouseManaging, gc, gm);
		/*else
			for(SimpleButton subButton : subButtons)
				subButton.refresh();*/
	}
	
	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
		if(choosen)
			for(SimpleButton subButton : subButtons) {
				subButton.render(gc, r);
			}
	}
	
	public void addSubButton(SimpleButton simpleButton) {
		subButtons.add(simpleButton);
	}
	
	public ArrayList<SimpleButton> getSubButtons() {
		return subButtons;
	}
	
	@Override
	public void refresh() {
		super.refresh();
		for(SimpleButton subButton : subButtons)
			subButton.refresh();
	}
	
}
