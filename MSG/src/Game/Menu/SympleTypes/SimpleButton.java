package Game.Menu.SympleTypes;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.Image;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;

import Game.GameManager;
import Game.Menu.SimpleMenuManager;

public class SimpleButton {
	protected String name, command;
	
	protected static final Image image  = (Image) ImageBuffer.load(Images.Button, false);
	
	protected int x, y, width = image.getW(), height = image.getH(), colorInner, colorOuter;
	protected float xR, yR, 
			widthR = GameContainer.getWidth()/GameManager.TS, 
			heightR = GameContainer.getHeight()/GameManager.TS;
	protected boolean choosen, inField;
	
	public SimpleButton(String name) {
		this.name = name;
		choosen = false;
		colorInner = 0xffa2a2a2;
		colorOuter = 0xff0000ff;
	}
	
	public SimpleButton(String name, String command) {
		this.name = name;
		choosen = false;
		this.command = command;
		colorInner = 0xffa2a2a2;
		colorOuter = 0xff0000ff;
	}

	
	public void update(int mouseX, int mouseY, boolean mouseManaging, GameContainer gc, GameManager gm) {
		if((mouseX  > x && mouseX < x + width) && (mouseY > y && mouseY < y + height)) {
			if(mouseManaging)
				choosen = true;
			inField = true;
		}else
			inField = false;
	}
	
	public void render(GameContainer gc, Renderer r) {
		
		//r.drawFillRect(x, y, width, height, getColor());
		//r.drawRect(x, y, width, height, colorOuter);
		r.drawImage(image, x, y);
		r.drawText(getName(), x + width/2, y + height/4 , 0xff000000, true);
	}
	
	public void refresh() {
		
	}

	public int getColor() {
		return colorInner;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void act(GameManager gm, GameContainer gc, SimpleMenuManager menuManager) {
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	/*Setting X
	 * 
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/*Setting X relative pos
	 * 
	 */
	public void setRelativeX(float x) {
		this.xR = (float) (x/GameContainer.getWidth());
	}
	
	public void setRelativeY(float y) {
		this.yR = (float) (y/GameContainer.getHeight());
	}

	public void setRelativeWidth(float i) {
		this.widthR = (float) (i/GameManager.TS);
	}
	
	public void setRelativeHeight(float i) {
		this.heightR = (float) (i/GameManager.TS);
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setSelected(boolean selected) {
		this.choosen = selected; 
	}

	public void setColorOuter(int colorOuter) {
		this.colorOuter = colorOuter;
	}

	public void setColorInner(int colorInner) {
		this.colorInner = colorInner;
	}

	public boolean isSelected() {
		return choosen;
	}

	public boolean isInField() {
		return inField;
	}

	public float getRelX() {
		return xR;
	}

	public float getRelY() {
		return yR;
	}

	public float getRelWidth() {
		return widthR;
	}
	
	public float getRelHeight() {
		return heightR;
	}
}
