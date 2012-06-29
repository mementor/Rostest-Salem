package haven;

import java.awt.event.KeyEvent;

import haven.GameUI.Belt;

public class ToolBelt extends Belt {
    protected GameUI gameui;
    protected ToolBeltWdg wdg;
    public ToolBelt(GameUI gameui) {
	this.gameui = gameui;
	wdg = new ToolBeltWdg(gameui);
    }
    
    @Override
    public int draw(GOut g, int by) {
	return 0;
    }

    @Override
    public boolean click(Coord c, int button) {
	int slot = wdg.beltslot(c);
	if(slot != -1) {
	    if(button == 1)
		gameui.wdgmsg("belt", slot, 1, gameui.ui.modflags());
	    if(button == 3)
		gameui.wdgmsg("setbelt", slot, 1);
	    return(true);
	}
	return false;
    }

    @Override
    public boolean key(KeyEvent ev) {
	return false;
    }

    @Override
    public boolean item(Coord c) {
	return false;
    }

    @Override
    public boolean thing(Coord c, Object thing) {
	return false;
    }

}
