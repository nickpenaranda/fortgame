package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.n4p.mountainking.items.Appearance.Element;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class DrowiteNode extends AbstractItem {
	private static Appearance appearance;

	public DrowiteNode(Owner parent) {
		super(parent);
	}

	public void init() {
		if(appearance != null) return;
		
		Color color = new Color(62, 97, 160);

		try {
			appearance = new Appearance()
				.addLayer(Element.WORLD1,new Image("gfx/items/uncommon_node.png", Color.magenta), color)
				.addLayer(Element.WORLD2,new Image("gfx/items/uncommon_node2.png", Color.magenta), color);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getLabel() {
		return ("Drowite Node");
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
