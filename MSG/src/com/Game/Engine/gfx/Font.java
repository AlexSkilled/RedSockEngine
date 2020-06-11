package com.Game.Engine.gfx;

public class Font {
	
	public static final Font STANDART = new Font("Fonts/standard2.png");
	
	private GFX fontImage;
	private int[] offsets;
	private int[] widths;
	
	public Font(String path) {
		fontImage = new GFXMap(path, 16, 16);
		
		offsets = new int[256];
		widths = new int[256];
		
		int unicode = 0;
		
		for(int i = 0; i < fontImage.getW();i++) {
			if(fontImage.getP()[i] == 0xff0000ff) {
				offsets[unicode] = i;
			}
			if(fontImage.getP()[i] == 0xffffff00) {
				widths[unicode] = i-offsets[unicode];
				unicode++;
			}
		}
	}

	public GFX getFontImage() {
		return fontImage;
	}

	public int getWorldLength(String text) {
		int offSet = 0;
		int unicode;
		for(int i = 0; i < text.length();i++) {
			unicode = text.codePointAt(i);
			offSet += widths[unicode];
		}
		return offSet;
	}
	
	public void setFontImage(GFX fontImage) {
		this.fontImage = fontImage;
	}

	public int[] getOffsets() {
		return offsets;
	}

	public void setOffsets(int[] offsets) {
		this.offsets = offsets;
	}

	public int[] getWidths() {
		return widths;
	}

	public void setWidths(int[] widths) {
		this.widths = widths;
	}

	public int[] getDefaultRes() {
		return fontImage.getDefaultRes();
	}
}
