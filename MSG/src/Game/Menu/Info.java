package Game.Menu;

import com.Game.Engine.gfx.GFX;

import Game.ProgrammObject;

public abstract class Info extends ProgrammObject{
	protected GFX back;
	protected int x, y, width, height, offSet, position;
	protected int bgSize;
	
	protected boolean turned;
	
	public abstract void refresh();
	
	public abstract void turn();
	
	public boolean isTurnedOn(){
		return turned;
	}
}
