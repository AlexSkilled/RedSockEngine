package Game.Menu.SympleTypes;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;

public class SimpleRelButton extends SimpleButton{

	
	public SimpleRelButton(String name, float x, float y, int w, int h) {
		super(name);
		
		xR = x;
		yR = y;
		widthR = w ;
		heightR = h;
		
		width = w*GameManager.TS;
		height = h*GameManager.TS;
		
		this.x = (int) (xR * (double) GameContainer.getWidth());
		this.y = (int) (yR * (double) GameContainer.getHeight());
		
	}

	@Override
	public void update(int mouseX, int mouseY, boolean mouseManaging, GameContainer gc, GameManager gm) {
		
		if((mouseX  > x && mouseX < x + width) && (mouseY > y && mouseY < y + height)) {
				inField = true;
				choosen = true;
		}else {
			inField = false;
			choosen = false;
		}
	}
	
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect(x, y, width, height, colorInner);
		r.drawRect(x, y, width, height, colorOuter);
		r.drawText(getName(), x + width/2, y + height/4 , 0xff000000, true);
	}
	
	
}
