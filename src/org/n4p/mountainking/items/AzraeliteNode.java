package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.n4p.mountainking.items.Appearance.Element;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AzraeliteNode extends Item {
	private static Appearance appearance;

	public AzraeliteNode(Owner parent) {
		super(parent);
	}

	public void init() {
		if(appearance != null) return;
		
		Color color = new Color(77, 209, 25);

		try {
			appearance = new Appearance()
				.addLayer(Element.WORLD1,new Image("gfx/items/rare_node.png", Color.magenta), color)
				.addLayer(Element.WORLD2,new Image("gfx/items/rare_node2.png", Color.magenta), color);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getLabel() {
		return ("Azraelite Node");
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
