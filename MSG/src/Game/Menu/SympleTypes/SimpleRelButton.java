package Game.Menu.SympleTypes;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.Image;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;

import Game.GameManager;

public class SimpleRelButton extends SimpleButton{

	private Image relImage = (Image) ImageBuffer.loadCopy(Images.Button);
	
	private int delta;
	
	public SimpleRelButton(String name, float x, float y, int w, int h) {
		super(name);
		
		xR = x;
		yR = y;
		
		widthR = w ;
		heightR = h;
		
		relImage.downgrade(0.5, 1);
		width = relImage.getW();
		height = relImage.getH();
		
		relImage.setAlpha(false);
		
		this.x = (int) (xR * (double) GameContainer.getWidth());
		this.y = (int) (yR * (double) GameContainer.getHeight());
		
		
	}

	@Override
	public void update(int mouseX, int mouseY, boolean mouseManaging, GameContainer gc, GameManager gm) {
		
		if((mouseX  > x && mouseX < x + width) && (mouseY > y && mouseY < y + height)) {
				inField = true;
				choosen = true;
				delta = GameManager.TS/2;
		}else {
			inField = false;
			choosen = false;
			delta = 0;
		}
	}
	
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		//r.drawFillRect(x, y, width, height, colorInner);
		//r.drawRect(x, y, width, height, colorOuter);
		r.drawImage(relImage, x-delta, y-delta);
		r.drawText(getName(), x-delta + width/2, y-delta + height/4 , 0xff000000, true);
	}
	
	
}
