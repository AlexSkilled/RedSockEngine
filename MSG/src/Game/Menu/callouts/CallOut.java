package Game.Menu.callouts;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;

import Game.GameManager;
import Game.GameObject;
import Game.GObjects.AliveObject;


public class CallOut extends GameObject{
	
	private GameObject go;
	private String[] text;
	private int counter, point, x , y, width, height;
	
	private float font[] = {0.9f, 0.9f};
	
	public CallOut(AliveObject object, String text) {
		go = object;
		point = 0;
		
		width = 7;
		
		int tW = 0;
		
		for(int i = 0; i < text.length(); i++) {
			if(tW >= width && text.charAt(i) == ' ') {
				text = text.substring(0, i) + "\n" + text.substring(i+1);
				width = tW;
				tW = 0;
			}
		tW++;
		}
		this.text = text.split("\n");
		counter = this.text.length * 6 + 99;
		width *= GameManager.TS/4;
		height = GameManager.TS*(this.text.length/4 +2);
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(counter >= 100) {
			if(counter % 6 == 0)
					point++;
		}
		
		counter--;
		
		x = (int) go.getPosX() - width;
		y = (int) go.getPosY() - height/2;
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect(x-8, y-8, width + 8, height + 8, 0xffffffff);
		r.drawRect(x-8, y-8, width + 8, height + 8, 0xff000000);
		for(int i = 0; i < point; i++) {
			r.drawText(text[i], x, y + GameManager.TS / 2 * i, 0xff000000, false, font[0], font[1]);
		}
		
	}
	
	public boolean isDead() {
		if(counter>0)
			return false;	
		return true;
	}

}
