/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kaezadraw;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author diego
 */
public class TheMIDlet extends MIDlet {

	private Display display;
	DrawForm form;

	public void startApp() {
		form = new DrawForm();
		form.addCommand(new Command("Quit", Command.EXIT, 0));
		form.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				destroyApp(false);
				notifyDestroyed();
			}
		});
		display = Display.getDisplay(this);
		display.setCurrent(form);
	}

	public void pauseApp() {
		display.setCurrent(form = null);
	}

	public void destroyApp(boolean unconditional) {
		display.setCurrent(form = null);
	}

}
