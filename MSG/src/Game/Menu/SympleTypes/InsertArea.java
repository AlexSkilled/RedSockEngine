package Game.Menu.SympleTypes;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.Menu.SimpleMenuManager;

public class InsertArea extends SimpleButton{

	protected boolean focused;
	protected String lastButton;
	
	public InsertArea() {
		super("");
		name = "";
		setUpColors();
	}
	
	public InsertArea(int x, int y, int w, int h, int innerColor, int outerColor) {
		super("");
		name = "";
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		this.colorInner = innerColor;
		this.colorOuter = outerColor;
	}
	
	@Override
	public void update(int mouseX, int mouseY, boolean mouseManaging, GameContainer gc, GameManager gm) {
		super.update(mouseX, mouseY, mouseManaging, gc, gm);
		if(focused) {
			lastButton = gc.getInput().getPressed();
			if(lastButton.length() == 1 && name.length() < 8)
				name += lastButton;
			else {
				switch(lastButton) {
				case "Left":
					focused = false;
					choosen = false;
					break;
				case "Enter":
					focused = false;
					break;
				case "Backspace":
					focused = false;
					break;
				case "Escape":
					focused = false;
					break;
				}
				
				if(!focused) {
					gc.getInput().turnOffInserting();
				}
			}
		}
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect(x, y, width, height, colorInner);
		r.drawRect(x, y, width, height, colorOuter);
		r.drawText(name, x + width/2, y+height/3, 0xff000000, true);
	}
	
	@Override
	public void act(GameManager gm, GameContainer gc, SimpleMenuManager menuManager) {
		focused = !focused;
		
		flip();
		
		gc.getInput().turn();
	}
	
	protected void setUpColors() {
		colorInner = 0xff00ff00;
		colorOuter = 0xffff00ff;
	}
	
	private void flip() {
		int buf = colorInner;
		colorInner = colorOuter;
		colorOuter = buf;
	}
	
	@Override
	public void refresh() {
		name = "";
		focused = false;
		setUpColors();
	}
	
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
}
