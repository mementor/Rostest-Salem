package haven;

import java.awt.event.KeyEvent;

import haven.GameUI.Belt;

public class ToolBelt extends Belt {

    @Override
    public int draw(GOut g, int by) {
	return 0;
    }

    @Override
    public boolean click(Coord c, int button) {
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
