package com.Game.Engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window {

		private JFrame frame;
		private BufferedImage image;
		private Canvas canvas;
		private BufferStrategy bs;
		private Graphics g;
		private Dimension dms;
		private GameContainer gc;
		public Window(GameContainer gc) {
			this.gc = gc;
			
			image = new BufferedImage(GameContainer.getWidth(), GameContainer.getHeight(), BufferedImage.TYPE_INT_RGB);
			canvas = new Canvas();
			dms = new Dimension((int) (GameContainer.getWidth()*GameContainer.getScale()), (int) (GameContainer.getHeight()*GameContainer.getScale()));
			canvas.setPreferredSize(dms);
			canvas.setMinimumSize(dms);

			
			frame = new JFrame(GameContainer.getTitle());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());
			frame.add(canvas, BorderLayout.CENTER);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			canvas.createBufferStrategy(2);
			bs = canvas.getBufferStrategy();
			g = bs.getDrawGraphics();
		}
		
		protected void refresh() {
			dms = new Dimension((int) (GameContainer.getWidth()*GameContainer.getScale()), (int) (GameContainer.getHeight()*GameContainer.getScale()));
			canvas.setPreferredSize(dms);
			
			canvas.createBufferStrategy(2);
			bs = canvas.getBufferStrategy();
			g = bs.getDrawGraphics();
		}
		
		public void update() {
		
			int canvWH = canvas.getWidth() * canvas.getHeight();
			int curScale = (int) (GameContainer.getWidth()*GameContainer.getScale()) * (int) (GameContainer.getHeight()*GameContainer.getScale());
			
			if(canvWH != curScale) {
				System.out.println();
				System.out.println(canvWH);
				System.out.println(curScale);
				System.out.println((double) canvWH/curScale);
				gc.setScale((int) (GameContainer.getScale() * ((double) canvWH/curScale)));
				dms = new Dimension((int) (GameContainer.getWidth()*GameContainer.getScale()), (int) (GameContainer.getHeight()*GameContainer.getScale()));
				refresh();
				gc.refresh(); 
			}		
			g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
			bs.show();
		}

		public BufferedImage getImage() {
			return image;
		}

		public Canvas getCanvas() {
			return canvas;
		}

		public JFrame getFrame() {
			return frame;
		}
		
		protected void updateBounds() {
			
			Rectangle bounds = frame.getBounds();
			
			if(bounds.width != GameContainer.getWidth())
				gc.setWidth(bounds.width);
			
			if(bounds.height != GameContainer.getHeight())
				gc.setWidth(bounds.height);
		}
		
		protected void setBounds(int width, int height) {
			frame.setBounds(frame.getBounds().x, frame.getBounds().y, width, height);
		}

		public void dispose() {
			frame.dispose();
		}
		
}
