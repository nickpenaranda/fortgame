package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class EndMarker extends AbstractItem {
	private static Image image1, image2;

	public EndMarker(Owner parent) {
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
		return ("Path End Marker");
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
			image1 = new Image("gfx/items/end.png", Color.magenta);
			image2 = new Image("gfx/items/end2.png", Color.magenta);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
