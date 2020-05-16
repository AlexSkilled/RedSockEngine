package Game;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Enums.Objects;

import Game.GObjects.AliveObjects.Player;
import Game.GObjects.DeadObjects.Bullet;

public interface GameObjectsContainer {

	void update(GameContainer gc, GameManager gm, float dt);

	void render(GameContainer gc, Renderer r);
	
	void add(GameObject object, Objects string);

	void add(GameObject object);
	
	void addObjectToBegin(GameObject object);
	
	GameObject getObject(Objects tag);
	
	Bullet getBulletCollision(float posX, float posY);
	
	Player getPlayer();
	
	GameObject getObjectAt(int x, int y);

	void clear();

	void refresh();

}
