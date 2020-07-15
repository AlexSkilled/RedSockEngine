package com.Game.Engine;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.Game.Engine.gfx.buffer.ImageBuffer;

import Game.GameManager;
import Game.Saves.ConfigStorage;

public class GameContainer implements Runnable {
	
		private static String error;
		private static int errorCounter = 0;
		private Thread thread;//Основной поток
		private Window window;//Окно в котором будем работать
		private Renderer renderer;//Отрисовка
		private Input input;//Ввод данных
		private GameManager game;//интерфейс игры для обновления и вызова отрисовки
		private ImageBuffer imageBuffer;
		private static int fps;
		private boolean running = false;// игра ваще работает?
		public final double UPDATE_CAP = 1.0/60;//частота обновлений в секунду
		private static int width = 800, height = 480;//Разрешение
		private static Double scale = 2.0;//увеличение разрешения
		private static String title = "RedSock engine";//название
		
		public GameContainer(GameManager game) {
			this.game = game;
			error = "";
		}

		
		public void start() {
			window = new Window(this);
			renderer = new Renderer(this);
			input = new Input(this);
			imageBuffer = new ImageBuffer();
			thread = new Thread(this);
			try {
				thread.run();// .run is for MAIN Thread / .start is for side Thread
			}catch(Exception e) {
				e.printStackTrace();
				
				FileWriter fw;
				try {
					fw = new FileWriter (System.getProperty("user.home") + "/Desktop/" + errorCounter+".cfg", true);
					PrintWriter	pw = new PrintWriter (fw);
					e.printStackTrace(pw);
					pw.flush();
					pw.close();
					fw.flush();
					fw.close();
				} catch (IOException e1) {
					
				}
				
				addError(e);
				//errorOut("All");
			}
		}
		
		public void stop() {
			
		}
		
		public void run() {
			
			running = true;
			
			boolean render = false;//Надо ли отрисовывать 
			double loopWorked = 0;//Время после цикла
			double loopStay = System.nanoTime() / 1000000000.0;//время до цикла
			double passedTime = 0;//время цикла
			double unprocessedTime = 0;//
			
			double frameTime = 0;//время на отрисовку кадра
			int frames = 0;//Количество кадров
			fps = 0;//количество кадров в секунду
			
			game.init(this);
			
			while(running) {
				
				//игровой цикл
				//render = false;
				
				loopWorked = System.nanoTime()/1000000000.0;
				passedTime = loopWorked - loopStay;
				loopStay = loopWorked;
				
				unprocessedTime += passedTime;
				frameTime += passedTime;
				
				while(unprocessedTime>= UPDATE_CAP){
					unprocessedTime-= UPDATE_CAP;
					render = true;
					
					game.update(this, (float) UPDATE_CAP);
					
					input.update();
										
					if(frameTime>=1.0) {
						frameTime = 0;
						fps = frames;
						frames = 0;						
					}
				}
				
				if(render) {
					renderer.clear();
					renderer.process();
					game.render(this, renderer);
					renderer.setCamX(0);
					renderer.setCamY(0);
					//renderer.drawText("FPS " + fps, 0, 0, 0xff00ff00, false);
					window.update();
					frames++;
				} 
				else {
					//if game doesn't update&render anything we kill Thread so they don't waste recourses
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(!error.isEmpty())
					errorOut("Simple");
			}
			dispose();
		}
		
		public void dispose(){
			System.exit(0);
		}

		public static int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			GameContainer.width = width;
		}

		public static int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			GameContainer.height = height;
		}

		public static double getScale() {
			return scale;
		}

		public void setScale(double scale) {
			GameContainer.scale = scale;
		}

		public static String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			GameContainer.title = title;
		}

		public Window getWindow() {
			return window;
		}

		public Input getInput() {
			return input;
		}

		public Renderer getRenderer() {
			return renderer;
		}

		public void setRenderer(Renderer renderer) {
			this.renderer = renderer;
		}

		public void refresh() {
			
			if(scale<1)
				return;
			
			renderer = null;
			//window.dispose();
			window.refresh();
			//window = new Window(this);
			//window.setBounds((int) (width * scale), (int) (height*scale));
			renderer = null;
			renderer = new Renderer(this);
			input = new Input(this);
			ConfigStorage.updateConfig();
		}

		public void setBounds(int width, int height, double scale) {
			GameContainer.width = width;
			GameContainer.height = height;
			GameContainer.scale = scale;
			window.setBounds((int) (width*scale),(int) (height*scale));
			
		}

		public ImageBuffer getImageBuffer() {
			return imageBuffer;
		}
		
		public static void addError(Exception e) {
			e.printStackTrace();
			error+="\n\n"+e;
			FileWriter fw;
			try {
				fw = new FileWriter (System.getProperty("user.home") + "/Desktop/" + errorCounter + ".cfg", true);
				PrintWriter	pw = new PrintWriter (fw);
				e.printStackTrace(pw);
				pw.flush();
				pw.close();
			} catch (IOException e1) {
				
			}
			
			errorCounter++;
			//errorOut(Integer.toString(errorCounter));
		}
		
		public static String getErrors() {
			return error;
		}
		
		public static void errorOut(String name) {
			PrintWriter pw;
			try {
				pw = new PrintWriter(System.getProperty("user.home") + "/Desktop/"+name+".cfg", "UTF-8");
				pw.println(error);
				pw.flush();
				pw.close();
				error = "";
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}


		public static String getMainPath() {
			return System.getProperty("user.home")+"\\Documents\\MSG\\";
		}
		public static int getFPS() {
			return fps;
		}
}
