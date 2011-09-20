package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.n4p.mountainking.items.Appearance.Element;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class RagnariteNode extends Item {
	private static Appearance appearance;

	public RagnariteNode(Owner parent) {
		super(parent);
	}

	public void init() {
		if(appearance != null) return;
		
		try {
			appearance = new Appearance()
				.addLayer(Element.WORLD1,new Image("gfx/items/legendary_node.png", Color.magenta), new Color(63,206,242))
				.addLayer(Element.WORLD2,new Image("gfx/items/legendary_node.png", Color.magenta), new Color(63,242,164));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getLabel() {
		return ("Ragnarite Node");
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
