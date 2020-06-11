package Game.Menu.SympleTypes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.Menu.Menus;
import Game.Menu.SimpleMenuManager;

public abstract class SimpleMenu{

	protected ArrayList<SimpleButton> paragraphs;
	protected ArrayList<SimpleRelButton> relativeBtns;
	protected boolean leftSide;
	protected int mouseX, mouseY;
	
	protected int cameraOffX, cameraOffY;
	protected int offSet;
	private Integer toDelete;
	protected boolean mouseManaging, mouseInField;
	protected Integer selected;//number of chosen menu item

	protected boolean delay;
	
	protected boolean onScreen;
	protected int offX;
	protected int offY;
	protected Menus menuName;
	protected String name;
	private int newOffX, newOffY;
	
	public SimpleMenu(Menus menuName) {
		this.menuName = menuName;
		name = "EMPTY";
		selected = 0;
		offSet = 5;
		paragraphs = new ArrayList<SimpleButton>();
		relativeBtns = new ArrayList<SimpleRelButton>();
		delay = true;
		setOffX(0);
		offY = 0;
		leftSide = true;
	}

	public void update(GameContainer gc, GameManager gm, SimpleMenuManager menuManager) {
		if(toDelete != null) {
			paragraphs.remove((int) toDelete);
			selected = paragraphs.size() - 1;
			newSelected(paragraphs.size() - 1);
			for(int i = toDelete; i < paragraphs.size(); i++)
				setupButtonLocation(paragraphs.get(i), i, offX, offY);
			toDelete = null;
		}
		
		if(gm.getCamera() != null) {
			newOffX = (int) gm.getCamera().getOffX();
			newOffY = (int) gm.getCamera().getOffY();
			if(cameraOffX != newOffX || cameraOffY != newOffY) {
				cameraOffX = (int) gm.getCamera().getOffX();
				cameraOffY = (int) gm.getCamera().getOffY();
				for(int i = 0; i < paragraphs.size(); i++)
					setupButtonLocation(paragraphs.get(i), i, offX, offY);
			}
		}
		else {
			cameraOffX = 0;
			cameraOffY = 0;
		}
		
		if(mouseX == gc.getInput().getMouseX() + cameraOffX && mouseY == gc.getInput().getMouseY() + cameraOffY){
			mouseManaging = false;
			
		}else{
			mouseX = gc.getInput().getMouseX() + cameraOffX;
			mouseY = gc.getInput().getMouseY() + cameraOffY;	
			mouseManaging = true;
		}
		
		for(int i = paragraphs.size() - 1; i >= 0; i--) {
			paragraphs.get(i).update(mouseX, mouseY, mouseManaging, gc, gm);
			setButtonLocation(paragraphs.get(i));
			
			if(paragraphs.get(i).isSelected() && i != selected)
				newSelected(i);
		}
		
		for(int i = relativeBtns.size() - 1; i >= 0; i--) {
			relativeBtns.get(i).update(mouseX, mouseY, mouseManaging, gc, gm);
			setButtonLocation(relativeBtns.get(i));
		}		
		
		if(gc.getInput().isButtonUp(1)) {
			if(paragraphs.get(selected).isInField()) {
				paragraphs.get(selected).act(gm, gc, menuManager);
			}else
				for(int i = 0; i < relativeBtns.size(); i++) {
					if(relativeBtns.get(i).isInField()) {
						relativeBtns.get(i).act(gm, gc, menuManager);
					break;	
					}
				}
		}
		if(leftSide) {
			switch (gc.getInput().getScroll()) {
			case -1:
				if(offSet > 5) {
					offSet--;
					offY += GameContainer.getHeight() / 5;
				}
				break;
			case 1:
				if(offSet < paragraphs.size()) {
					offSet++;
					offY -= GameContainer.getHeight() / 5;
				}
				break;
			}
				
			if(gc.getInput().isKeyUp(KeyEvent.VK_W) || gc.getInput().isKeyUp(KeyEvent.VK_UP)) {
				if(selected > 0) {
					newSelected((selected - 1));
					
				}
				else {
					newSelected((paragraphs.size() - 1));
				}
			}else 
				if((gc.getInput().isKeyUp(KeyEvent.VK_S) || gc.getInput().isKeyUp(KeyEvent.VK_DOWN))) {
					if(selected < paragraphs.size() - 1) {
						newSelected((selected + 1));
					}
					else {
						newSelected(0);
					}
			}
		
			if(gc.getInput().isKeyUp(KeyEvent.VK_ENTER)) {
				paragraphs.get(selected).act(gm, gc, menuManager);
			}
		}
		
	}

	public void render(GameContainer gc, Renderer r) {
		for(int i = 0; i < paragraphs.size(); i++) {
			paragraphs.get(i).render(gc, r);
		}
		for(int i = 0; i < relativeBtns.size(); i++) {
			relativeBtns.get(i).render(gc, r);
		}
		
	}

	protected void newSelected(Integer selected) {
		try {
			paragraphs.get(this.selected).setSelected(false);
			
		}catch (NullPointerException e) {
			
		}
		catch(IndexOutOfBoundsException e) {
			this.selected = paragraphs.size() - 1;
			newSelected(this.selected);
		}
		
		this.selected = selected;
		paragraphs.get(selected).setSelected(true);
	}
	
	protected void setButtonLocation(SimpleButton simpleButton) {
		int X, Y, W, H;
		
		X = (int) (simpleButton.getRelX() * GameContainer.getWidth());
		Y = (int) (simpleButton.getRelY() * GameContainer.getHeight());
		W = (int) (simpleButton.getRelWidth() * GameManager.TS);
		H = (int) (simpleButton.getRelHeight() * GameManager.TS);
		
		if(!simpleButton.isSelected()) {	
			simpleButton.setX(X);
			simpleButton.setY(Y);
			simpleButton.setWidth(W);
			simpleButton.setHeight(H);
		}else {
			simpleButton.setX(X - GameContainer.getWidth() / 64);
			simpleButton.setY(Y - GameContainer.getHeight() / 64);
			simpleButton.setWidth(W + GameContainer.getWidth() / 32);
			simpleButton.setHeight(H + GameContainer.getHeight() / 32);
		}
	}
	
	protected void setupButtonLocation(SimpleButton simpleButton, int number, int offX, int offY){
		
		simpleButton.setRelativeX((35 * GameContainer.getWidth()/96) + cameraOffX + offX);
		simpleButton.setRelativeY(((1 + number) * GameContainer.getHeight()/ 6) + cameraOffY + offY);
		
		simpleButton.setRelativeWidth(GameContainer.getWidth() / 32 + (GameContainer.getWidth()*5)/16);
		simpleButton.setRelativeHeight(GameContainer.getHeight() / 8);
		
		setButtonLocation(simpleButton);
	}
	
	public void addButton(SimpleButton button) {
		if(button instanceof SimpleRelButton) {
			relativeBtns.add((SimpleRelButton) button);
		}
		else {
			paragraphs.add(button);
			setupButtonLocation(paragraphs.get(paragraphs.size()-1), paragraphs.size()-1, offX, offY);
		}
	}

	public void refresh() {
		newSelected(0);
		
		offSet = 5;
		
		for(SimpleRelButton btn : relativeBtns)
			btn.refresh();
		for(int i = 0; i <  paragraphs.size(); i++)
			setupButtonLocation(paragraphs.get(i), i, offX, offY);
	}
	
	public Menus getEnumName() {
		return menuName;
	}

	public int getOffX() {
		return offX;
	}

	public void setOffX(int offX) {
		this.offX = offX;
	}
	
	public int getOffY() {
		return offX;
	}

	public void setOffY(int offY) {
		this.offY = offY;
	}
	
	public void deleteButton(String sbName) {
		for(SimpleButton sb : paragraphs) {
			if(sb.getName().equals(sbName)) {
				toDelete = paragraphs.indexOf(sb);
				break;
			}	
		}		
	}
	
}