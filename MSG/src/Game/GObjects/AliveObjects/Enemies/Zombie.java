package Game.GObjects.AliveObjects.Enemies;

import com.Game.Engine.gfx.buffer.Images;

import Game.GObjects.AliveObjects.Enemy;

public class Zombie extends Enemy{

	public Zombie(int posX, int posY, int hp, int armor, Images image) {
		super(posX, posY, hp, armor, image);
	}
	
	public Zombie(String info) {
		super(info);
	}

	@Override
	public String toString() {
		String line = super.toString();
		String[] line_ = line.split(" ");
		line_[1] += " Zombie";
		line = String.join(" ", line_);
		return line;
	}
}
