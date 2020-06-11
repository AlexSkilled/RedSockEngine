package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.Game.Engine.GameContainer;
import com.Game.Engine.Renderer;
import com.Game.Enums.Objects;

import Game.GObjects.Item;
import Game.GObjects.AliveObjects.Player;
import Game.GObjects.DeadObjects.Bullet;

public class GameObjects extends ProgrammObject implements GameObjectsContainer{
	
	private HashMap<Objects, ArrayList<GameObject>> allObjects;
	private ArrayList<Objects> exceptions = new ArrayList<Objects>();
	
	public GameObjects() {
		
		allObjects = new HashMap<Objects, ArrayList<GameObject>>();
		
		allObjects.put(Objects.bullet, new ArrayList<GameObject>());
		allObjects.put(Objects.enemy, new ArrayList<GameObject>());
		allObjects.put(Objects.player, new ArrayList<GameObject>());
		allObjects.get(Objects.player).add(null);
		allObjects.put(Objects.item, new ArrayList<GameObject>());
		allObjects.put(Objects.entity, new ArrayList<GameObject>());
		
		Collections.addAll(exceptions, Objects.player, Objects.entity, Objects.enemy);
	}
	
	public void update(GameContainer gc, GameManager gm, float dt){
		ArrayList<GameObject> temp;
		for(Objects key : allObjects.keySet()){
			temp = allObjects.get(key);
			for(int i = 0; i < temp.size(); i++){
				if(temp.get(i).isDead()) {
					temp.remove(temp.get(i));
					i--;
				}
				temp.get(i).update(gc, gm, dt);
			}
		}
	}

	
	public void render(GameContainer gc, Renderer r){
		for(Objects key : allObjects.keySet()) {
			if(!key.equals(Objects.player)) {
				ArrayList<GameObject> temp = allObjects.get(key);
				for(GameObject gameObject : temp){
					gameObject.render(gc, r);
			}
		}
		}
		ArrayList<GameObject> temp = allObjects.get(Objects.player);
		for(GameObject gameObject : temp){
			gameObject.render(gc, r);
		}
	}
	
	public void add(GameObject object) {
		try {
			if(object instanceof Player)
				allObjects.get(Objects.player).set(0, object);
			else
				allObjects.get(object.getTag()).add(object);
		}catch (NullPointerException e) {
			allObjects.get(Objects.entity).add(object);
		}		
	}
	
	public void add(GameObject object, Objects a) {
		try {
			allObjects.get(a).add(object);
		}catch (NullPointerException e) {
			allObjects.get(Objects.entity).add(object);
		}		
	}
	
	public void addObjectToBegin(GameObject object) {
		allObjects.get(object.getTag()).add(0, object);
	}

	public GameObject getObject(Objects tag) {
		try {
			return allObjects.get(tag).get(0);
		}catch (NullPointerException e) {
			for(int i = 0; i < allObjects.get(Objects.entity).size(); i++) {
				if(allObjects.get(Objects.entity).get(i).getTag().equals(tag))
					return allObjects.get(Objects.entity).get(i);
			}
			return null;
		}
	}
	
	public Bullet getBulletCollision(float posX, float posY){
		ArrayList<GameObject> bullets = allObjects.get(Objects.bullet);
		for(int i = 0; i < bullets.size(); i++){
			if(bullets.get(i).getTag().equals(Objects.bullet)){
				if((bullets.get(i).getTileX() * GameManager.TS > posX * GameManager.TS - GameManager.TS/2 
							&& bullets.get(i).getTileX() * GameManager.TS < posX * GameManager.TS + GameManager.TS/2)
						&&
				   (bullets.get(i).getTileY() * GameManager.TS > posY * GameManager.TS - GameManager.TS/2 
							&& bullets.get(i).getTileY() * GameManager.TS < posY * GameManager.TS + GameManager.TS/2))
					
					return (Bullet) bullets.get(i);
			}
		}
		return null;
	}

	public Player getPlayer() {
		try {
			return (Player) allObjects.get(Objects.player).get(0);
		}catch (IndexOutOfBoundsException e){
			return null;
		}
	}

	public String toString() {
		String line = "";

		ArrayList<GameObject> temp;
		
		for(Objects key : allObjects.keySet()){
			if(exceptions.contains(key)) {
				temp = allObjects.get(key);
				for(int i = 0; i < temp.size(); i++){
					line += temp.get(i).toString()+"\n";
				}
			}
		}
		
		temp = allObjects.get(Objects.item);
		for(int i = 0; i < temp.size(); i++){
			if(!((Item) temp.get(i)).isInPocket())
				line += temp.get(i).toString()+"\n";
		}
		
		return line;
	}
	
	@Override
	public void clear() {
		for(Objects key : allObjects.keySet()) {
			if(!key.equals(Objects.player))
				allObjects.get(key).clear();
		}
	}

	@Override
	public void refresh() {
		for(Objects key : allObjects.keySet()){
			ArrayList<GameObject> temp = allObjects.get(key);
			for(int i = 0; i < temp.size(); i++){
				GameObject go = temp.get(i);
				if(go != null) {
					go.setPosX(go.getTileX() * GameManager.TS + go.getPosX() % GameManager.TS);
					go.setPosY(go.getTileY() * GameManager.TS + go.getPosY() % GameManager.TS);
					go.refresh();
				}
			}
		}
	}

	@Override
	public GameObject getObjectAt(int x, int y) {
		System.out.println("Я ещё не сделан");
		return null;
	}

	public void delete(GameObject object) {

		ArrayList<GameObject> temp;
		
		for(Objects key : allObjects.keySet()){
			if(exceptions.contains(key)) {
				temp = allObjects.get(key);
				for(int i = 0; i < temp.size(); i++){
					if(object.equals(temp.get(i)))
						temp.remove(i);
				}
			}
		}
	}

}
