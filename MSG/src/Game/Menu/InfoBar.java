package Game.Menu;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Engine.gfx.buffer.ImageBuffer;
import com.Game.Engine.gfx.buffer.Images;

import Game.GameManager;
import Game.GObjects.AliveObjects.Player;

public class InfoBar extends Info{
	
	private int value, fps;
	private String[] feed;
	
	public InfoBar() {
		
		turned = false;
		
		back = ImageBuffer.load(Images.GreyBack);
		feed = new String[4];
		
		feed[0] = "FPS: ";
		
		feed[1] = "AVG FPS: ";
		
		feed[2] = "X: ";
		
		feed[3] = "Y: ";
		
		value = GameContainer.getFPS();
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(turned) {
			x = (int) gm.getCameraOffX();
			y = (int) gm.getCameraOffY();
			
			fps = GameContainer.getFPS();
			
			feed[0] = feed[0].substring(0, 5)  +  Integer.toString(fps);
			
			value = (value + fps)/2;
			feed[1] = feed[1].substring(0, 9) + Integer.toString(value);
			
			Player pl = gm.getPlayer(); 
			feed[2] = feed[2].substring(0, 3) + Double.toString((double) Math.round((pl.getPosX()*100)/GameManager.TS)/100);
			feed[3] = feed[3].substring(0, 3) + Double.toString((double) Math.round((pl.getPosY()*100)/GameManager.TS)/100);
		}	
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		if(turned) {
			for(int i = 0; i < feed.length; i++) {
				r.drawText(feed[i], x, y + i * (GameManager.TS), 0xffffffff, false);
			}
		}
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public void turn() {
		turned = !turned;
	}

	@Override
	public boolean isTurnedOn() {
		return turned;
	}

}
