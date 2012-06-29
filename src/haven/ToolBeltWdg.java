package haven;

import static haven.Inventory.invsq;

public class ToolBeltWdg extends Widget {
    private static final Coord invsz = invsq.sz();
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
		FastText.aprintf(g, c.add(invsz), 1, 1, "F%d", i + 1);
		g.chcolor();
	}
    }
    
    public int beltslot(Coord c){
	for(int i = 0; i < 12; i++) {
		if(c.isect(beltc(i), invsz)){
		    return i + (curbelt * 12);
		}
	}
	return -1;
    }
    
    
    
    @Override
    public boolean mousedown(Coord c, int button) {
	return false;
    }

    private Coord beltc(int i) {
	return(new Coord(/* ((sz.x - (invsq.sz().x * 12) - (2 * 11)) / 2) */
		135
		+ ((invsz.x + 2) * i)
		+ (10 * (i / 4)),
		sz.y - 26 - invsz.y - 2));
    }

}
