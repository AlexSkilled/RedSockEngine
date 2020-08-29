package Game.Menu.SympleTypes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.Menu.Menus;
import Game.Menu.SimpleMenuManager;

public class SimpleSecondMenu extends SimpleMenu{

	private ArrayList<SimpleButton> cBtn;
	private int subSelected;
	public int subOffY;
	
	public SimpleSecondMenu(Menus menuName) {
		super(menuName);
		paragraphs.add(new SimpleButton("Back"));
		subSelected = 0;
		offX = - GameContainer.getWidth()/4;
	}

	public void update(GameContainer gc, GameManager gm, SimpleMenuManager menuManager) {
		super.update(gc, gm, menuManager);

		if(gc.getInput().isKeyUp(KeyEvent.VK_D) || gc.getInput().isKeyUp(KeyEvent.VK_RIGHT)) {
			leftSide = false;
			newSubSelected(0);
		}
		else if(gc.getInput().isKeyUp(KeyEvent.VK_A) ||gc.getInput().isKeyUp(KeyEvent.VK_LEFT))
			leftSide = true;

		if(paragraphs.get(selected) instanceof ComplexButton) {

			cBtn = ((ComplexButton) paragraphs.get(selected)).getSubButtons();

			for(SimpleButton button : cBtn) {

				if(button.isSelected())
					newSubSelected(cBtn.indexOf(button));
				//updateButtonLocation(button);
				//configureButtonLocation(button, cBtn.indexOf(button), -offX, subOffY);
				updateButtonLocation(button);
			}

			if(gc.getInput().isButtonUp(1)&& !cBtn.isEmpty() && cBtn.get(subSelected).isInField()) {
				cBtn.get(subSelected).act(gm, gc, menuManager);
			}
		}

		if(!leftSide) {
			if((gc.getInput().isKeyUp(KeyEvent.VK_W) || gc.getInput().isKeyUp(KeyEvent.VK_UP))) {
				if(subSelected > 0)
					newSubSelected((subSelected - 1));
				else
					newSubSelected((cBtn.size() - 1));
			}else 
				if(gc.getInput().isKeyUp(KeyEvent.VK_S) || gc.getInput().isKeyUp(KeyEvent.VK_DOWN)) {
					if(subSelected < cBtn.size() - 1)
						newSubSelected((subSelected + 1));
					else
						newSubSelected(0);
				}

			if(gc.getInput().isKeyUp(KeyEvent.VK_ENTER)) {
				cBtn.get(subSelected).act(gm, gc, menuManager);
			}
		}
	}

	public void render(GameContainer gc, Renderer r) {
		super.render(gc, r);
	}

	public void changeBackButton(Menus menu){
		paragraphs.set(0, new SimpleButton("Back"){
			public void act(GameManager gm, GameContainer gc, SimpleMenuManager menuManager) {
				menuManager.updateScene(menu);
			}
		});
	}

	public void flush() {
		addButton(paragraphs.get(0));
		paragraphs.remove(0);
	}

	protected void newSubSelected(int number) {
		try {
			cBtn.get(subSelected).setSelected(false);
		}catch (IndexOutOfBoundsException | NullPointerException e) {

		}
		subSelected = number;
		cBtn.get(subSelected).setSelected(true);
	}

	public void changeBackButton(SimpleButton backButton){
		paragraphs.set(0, backButton);
	}

	protected void setSubButton(GameContainer gc, GameManager gm, int number, int offX, int offY){
		for(int i = 0; i < cBtn.size(); i++) {
			updateButtonLocation(cBtn.get(i));
			cBtn.get(i).update(mouseX, mouseY, mouseManaging, gc, gm);
		}
	}

	@Override
	protected void newSelected(Integer selected) {
		super.newSelected(selected);
		
		try {
			cBtn = ((ComplexButton) paragraphs.get(selected)).getSubButtons();
			for(SimpleButton button : cBtn) {
				configureButtonLocation(button, cBtn.indexOf(button), -offX, subOffY);
			}
		}catch(ClassCastException e) {}
	}

	@Override
	public void refresh() {
		super.refresh();

		for(SimpleButton parentButton : paragraphs)
			parentButton.refresh();
		
		if(cBtn != null)
			newSubSelected(0);

	}

	public void setSubOffY(int subOffY) {
		this.subOffY = subOffY;
	}
}


