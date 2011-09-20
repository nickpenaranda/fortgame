package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class StartMarker extends AbstractItem {
	private static Image image1, image2;

	public StartMarker(Owner parent) {
		super(parent);
	}

	@Override
	public Image getWorldAppearance(boolean animState) {
		return (animState ? image2 : image1);
	}

	@Override
	public Image getInvAppearance() {
		return null;
	}

	@Override
	public String getLabel() {
		return ("Path Start Marker");
	}

	@Override
	public int getFlags() {
		return (0);
	}

	@Override
	public void init() {
		if (image1 != null)
			return;

		try {
			image1 = new Image("gfx/items/start.png", Color.magenta);
			image2 = new Image("gfx/items/start2.png", Color.magenta);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
