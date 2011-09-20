package org.n4p.mountainking.items;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Appearance {
	public static enum Element {
		WORLD1, WORLD2, INV
	};

	public static int WIDTH = 32;
	public static int HEIGHT = 48;
	public static int WIDTH_INV = 32;
	public static int HEIGHT_INV = 32;

	private Image mImgWorld1, mImgWorld2, mImgInv;

	public Appearance() {
		try {
			mImgWorld1 = new Image(WIDTH, HEIGHT);
			mImgWorld2 = new Image(WIDTH, HEIGHT);
			mImgInv = new Image(WIDTH_INV, HEIGHT_INV);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Appearance addLayer(Element element, Image base, Color filter)
			throws SlickException {
		Graphics g = null;
		switch (element) {
		case WORLD1:
			if (base.getWidth() != WIDTH || base.getHeight() != HEIGHT)
				throw new SlickException(String.format("Bad image dimensions: (%d,%d)",
						base.getWidth(), base.getHeight()));
			g = mImgWorld1.getGraphics();
			break;
		case WORLD2:
			if (base.getWidth() != WIDTH || base.getHeight() != HEIGHT)
				throw new SlickException(String.format("Bad image dimensions: (%d,%d)",
						base.getWidth(), base.getHeight()));
			g = mImgWorld2.getGraphics();
			break;
		case INV:
			if (base.getWidth() != WIDTH_INV || base.getHeight() != HEIGHT_INV)
				throw new SlickException(String.format("Bad image dimensions: (%d,%d)",
						base.getWidth(), base.getHeight()));
			g = mImgInv.getGraphics();
			break;
		default:
			return (null);
		}
		if (filter != null)
			g.drawImage(base, 0, 0, filter);
		else
			g.drawImage(base, 0, 0, Color.white);
		
		g.flush();
		return (this);
	}

	public Image getWorldAppearance(boolean animState) {
		return (animState ? mImgWorld2 : mImgWorld1);
	}
	
	public Image getInventoryAppearance() {
		return (mImgInv);
	}
}
