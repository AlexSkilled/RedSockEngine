package com.Game.Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private Integer lastKeyUp;
	private boolean inserting;
	//для клавиатуры
	private final int NUM_KEYS = 256;
	private boolean[] keys = new boolean[NUM_KEYS];
	private boolean[] keysLast = new boolean[NUM_KEYS];

	//для мыши
	private final int NUM_BUTTONS = 5;
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];
	
	//для колеса мыши
	private int mouseX, mouseY;
	private int scroll;
	
	public Input(GameContainer gc) {
		mouseX = 0;
		mouseY = 0;
		scroll = 0;
		inserting = false;
		gc.getWindow().getCanvas().addKeyListener(this);
		gc.getWindow().getCanvas().addMouseMotionListener(this);
		gc.getWindow().getCanvas().addMouseListener(this);
		gc.getWindow().getCanvas().addMouseWheelListener(this);
	}
	
	public void update() {
		
		scroll = 0;
		
		for(int i = 0; i < NUM_KEYS; i++) {
			keysLast[i] = keys[i];
		}
		
		for(int i = 0; i < NUM_BUTTONS; i++) {
			buttonsLast[i] = buttons[i];
		}
	}
	
	public String getPressed() {

		String temp  = "";
		if(lastKeyUp != null) {
			temp = KeyEvent.getKeyText(lastKeyUp);
			lastKeyUp = null;
		}
		return temp;
		
	}

	//KEYBOARD ISs
	public boolean isKey(int keyCode) {
		return keys[keyCode];
	}
	
	public boolean isKeyUp(int keyCode) {
		return !keys[keyCode] && keysLast[keyCode]  && !inserting;
	}
	
	public boolean isKeyDown(int keyCode) {
		return keys[keyCode] && !keysLast[keyCode]  && !inserting;
	}
	
	//Mouse Buttons is
	public boolean isButton(int Button) {
		return buttons[Button];
	}
	
	public boolean isButtonUp(int Button) {
		return !buttons[Button] && buttonsLast[Button];
	}
	
	public boolean isButtonDown(int Button) {
		return buttons[Button] && !buttonsLast[Button];
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = (int) (e.getX()/GameContainer.getScale());
		mouseY = (int) (e.getY()/GameContainer.getScale());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = (int) (e.getX()/GameContainer.getScale());
		mouseY = (int) (e.getY()/GameContainer.getScale());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		isClicked();
	}
	
	private boolean isClicked() {
		return true;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(inserting)
			lastKeyUp = e.getKeyCode();
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scroll = e.getWheelRotation();
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public int getScroll() {
		return scroll;
	}
	
	public void turnOnInserting() {
		inserting = true;
	}
	
	public void turnOffInserting() {
		inserting = false;
	}
	
	public void turn() {
		inserting = !inserting;
	}

	public boolean isMouse() {
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i] || buttonsLast[i]) {
				return true;
			}
		}
		return false;
	}
}
