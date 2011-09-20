package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.n4p.mountainking.items.Appearance.Element;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class OdinstoneNode extends AbstractItem {
	private static Appearance appearance;

	public OdinstoneNode(Owner parent) {
		super(parent);
	}

	public void init() {
		if(appearance != null) return;
		
		try {
			appearance = new Appearance()
				.addLayer(Element.WORLD1,new Image("gfx/items/legendary_node.png", Color.magenta), new Color(255,204,26))
				.addLayer(Element.WORLD2,new Image("gfx/items/legendary_node2.png", Color.magenta), new Color(255,190,55));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getLabel() {
		return ("Odinstone Node");
	}

	@Override
	public int getFlags() {
		return (0);
	}

	@Override
	public Image getWorldAppearance(boolean animState) {
		return (appearance.getWorldAppearance(animState));
	}

	@Override
	public Image getInvAppearance() {
		return (appearance.getInventoryAppearance());
	}
}
