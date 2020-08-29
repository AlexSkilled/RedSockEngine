package Game.Menu.SympleTypes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.Menu.Menus;
import Game.Menu.SimpleMenuManager;

public abstract class SimpleMenu {

	protected ArrayList<SimpleButton> paragraphs;
	protected ArrayList<SimpleRelButton> relativeBtns;
	protected boolean leftSide;
	protected int mouseX, mouseY;

	protected int cameraOffX, cameraOffY;
	
	protected int offSet;
	protected static int buttonsOnScreen;
	
	private Integer toDelete;
	protected boolean mouseManaging, mouseInField;
	protected Integer selected;// number of chosen menu item

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
		if (toDelete != null) {
			paragraphs.remove((int) toDelete);
			selected = paragraphs.size() - 1;
			newSelected(paragraphs.size() - 1);
			for (int i = toDelete; i < paragraphs.size(); i++)
				configureButtonLocation(paragraphs.get(i), i, offX, offY);
			toDelete = null;
		}

		if (gm.getCamera() != null) {
			newOffX = (int) gm.getCamera().getOffX();
			newOffY = (int) gm.getCamera().getOffY();
			if (cameraOffX != newOffX || cameraOffY != newOffY) {
				cameraOffX = (int) gm.getCamera().getOffX();
				cameraOffY = (int) gm.getCamera().getOffY();
				
				 for(SimpleButton sb : paragraphs)
					 updateButtonLocation(sb);

				for (SimpleRelButton sb : relativeBtns)
					updateRelButtonLocation(sb);
			}
		} else {
			cameraOffX = 0;
			cameraOffY = 0;
		}

		if (mouseX == gc.getInput().getMouseX() + cameraOffX && mouseY == gc.getInput().getMouseY() + cameraOffY) {
			mouseManaging = false;

		} else {
			mouseX = gc.getInput().getMouseX() + cameraOffX;
			mouseY = gc.getInput().getMouseY() + cameraOffY;
			mouseManaging = true;
		}
		for (int i = paragraphs.size() - 1; i >= 0; i--) {
			paragraphs.get(i).update(mouseX, mouseY, mouseManaging, gc, gm);

			if (paragraphs.get(i).isSelected() && i != selected)
				newSelected(i);
		}

		for (int i = relativeBtns.size() - 1; i >= 0; i--) {
			relativeBtns.get(i).update(mouseX, mouseY, mouseManaging, gc, gm);
		}

		if (gc.getInput().isButtonUp(1)) {
			if (paragraphs.get(selected).isInField()) {
				paragraphs.get(selected).act(gm, gc, menuManager);
			} else
				for (int i = 0; i < relativeBtns.size(); i++) {
					if (relativeBtns.get(i).isInField()) {
						relativeBtns.get(i).act(gm, gc, menuManager);
						break;
					}
				}
		}
		if (leftSide) {
			switch (gc.getInput().getScroll()) {
			case -1:
				if (offSet > buttonsOnScreen) {
					offSet--;
					offY += GameContainer.getHeight() / 5;
					updateButtonsPosition();
				}
				break;
			case 1:
				if (offSet < paragraphs.size()) {
					offSet++;
					offY -= GameContainer.getHeight() / 5;
					updateButtonsPosition();
				}
				break;
			}

			if (gc.getInput().isKeyUp(KeyEvent.VK_W) || gc.getInput().isKeyUp(KeyEvent.VK_UP)) {
				if (selected > 0) {
					newSelected((selected - 1));

				} else {
					newSelected((paragraphs.size() - 1));
				}
			} else if ((gc.getInput().isKeyUp(KeyEvent.VK_S) || gc.getInput().isKeyUp(KeyEvent.VK_DOWN))) {
				if (selected < paragraphs.size() - 1) {
					newSelected((selected + 1));
				} else {
					newSelected(0);
				}
			}

			if (gc.getInput().isKeyUp(KeyEvent.VK_ENTER)) {
				paragraphs.get(selected).act(gm, gc, menuManager);
			}
		}

	}

	public void render(GameContainer gc, Renderer r) {
		for (int i = 0; i < paragraphs.size(); i++) {
			paragraphs.get(i).render(gc, r);
		}
		for (int i = 0; i < relativeBtns.size(); i++) {
			relativeBtns.get(i).render(gc, r);
		}

	}

	protected void updateButtonsPosition() {
		for(int i = 0; i < paragraphs.size(); i++)
			configureButtonLocation(paragraphs.get(i), i, offX, offY);
	}
	
	protected void newSelected(Integer selected) {
		try {
			paragraphs.get(this.selected).setSelected(false);
			updateButtonLocation(paragraphs.get(this.selected));
		} catch (NullPointerException e) {

		} catch (IndexOutOfBoundsException e) {
			this.selected = paragraphs.size() - 1;
			newSelected(this.selected);
		}

		this.selected = selected;
		paragraphs.get(selected).setSelected(true);
		updateButtonLocation(paragraphs.get(selected));
	}

	protected void updateButtonLocation(SimpleButton simpleButton) {
		int X, Y;

		X = (int) Math.floor(simpleButton.getRelX() * GameContainer.getWidth()) + cameraOffX;
		Y = (int) Math.floor(simpleButton.getRelY() * GameContainer.getHeight()) + cameraOffY;

		if (!simpleButton.isSelected()) {
			simpleButton.setX(X);
			simpleButton.setY(Y);
		} else {
			simpleButton.setX(X - GameContainer.getWidth() / 64);
			simpleButton.setY(Y - GameContainer.getHeight() / 64);
		}
	}

	protected void configureButtonLocation(SimpleButton simpleButton, int number, int offX, int offY) {

		simpleButton.setRelativeX((GameContainer.getWidth() / 2 - simpleButton.width / 2)  + offX);
		simpleButton.setRelativeY(((1 + number) * GameContainer.getHeight() / 6) + offY);
		
		updateButtonLocation(simpleButton);
		
	}

	protected void updateRelButtonLocation(SimpleRelButton simpleButton) {
		int X, Y;

		X = (int) (GameContainer.getWidth() * simpleButton.getRelX() + cameraOffX);
		Y = (int) (GameContainer.getHeight() * simpleButton.getRelX() + cameraOffY);

		if (!simpleButton.isSelected()) {
			simpleButton.setX(X);
			simpleButton.setY(Y);
		} else {
			simpleButton.setX(X - GameContainer.getWidth() / 64);
			simpleButton.setY(Y - GameContainer.getHeight() / 64);
		}
	}

	public void addButton(SimpleButton button) {
		if (button instanceof SimpleRelButton) {
			relativeBtns.add((SimpleRelButton) button);
		} else {
			paragraphs.add(button);
		}

	}

	public void refresh() {
		for (SimpleButton sb : paragraphs) {
			sb.setSelected(false);
		}

		paragraphs.get(0).setSelected(true);

		buttonsOnScreen = 0;
		offSet = 5;
		for (SimpleRelButton btn : relativeBtns)
			btn.refresh();
		
		for (int i = 0; i < paragraphs.size(); i++) {
			configureButtonLocation(paragraphs.get(i), i, offX, offY);
			if(paragraphs.get(i).getRelY()<1) {
				buttonsOnScreen++;
			}
		}
		if (buttonsOnScreen < 5) {
			offSet = buttonsOnScreen;
		}
		newSelected(0);
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
		for (SimpleButton sb : paragraphs) {
			if (sb.getName().equals(sbName)) {
				toDelete = paragraphs.indexOf(sb);
				break;
			}
		}
	}

}