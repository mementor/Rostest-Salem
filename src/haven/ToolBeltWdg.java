package haven;

import static haven.Inventory.invsq;

public class ToolBeltWdg extends Widget {
    protected static final IBox wbox = new IBox("gfx/hud", "tl", "tr", "bl", "br", "extvl", "extvr", "extht", "exthb");
    protected GameUI gameui;
    protected int curbelt = 0;
    
    public ToolBeltWdg(GameUI parent) {
	super(Coord.z, new Coord(100,100), parent);
	gameui = parent;
    }

    @Override
    public void draw(GOut g) {
	for(int i = 0; i < 12; i++) {
		int slot = i + (curbelt * 12);
		Coord c = beltc(i);
		g.image(invsq, beltc(i));
		try {
		    if(gameui.belt[slot] != null)
			g.image(gameui.belt[slot].get().layer(Resource.imgc).tex(), c.add(1, 1));
		} catch(Loading e) {}
		g.chcolor(156, 180, 158, 255);
		FastText.aprintf(g, c.add(invsq.sz()), 1, 1, "F%d", i + 1);
		g.chcolor();
	}
    }
    
    private Coord beltc(int i) {
	return(new Coord(/* ((sz.x - (invsq.sz().x * 12) - (2 * 11)) / 2) */
		135
		+ ((invsq.sz().x + 2) * i)
		+ (10 * (i / 4)),
		sz.y - 26 - invsq.sz().y - 2));
    }

}
