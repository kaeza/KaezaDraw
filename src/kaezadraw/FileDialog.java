
package kaezadraw;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class FileDialog extends Form {

	private boolean save;
	private DrawCanvas next;

	private static final Command SELECT_COMMAND = new Command("Select", Command.ITEM, 1);
	private static final Command DELETE_COMMAND = new Command("Delete", Command.ITEM, 2);

	public FileDialog(boolean save, DrawCanvas next) {
		super(save ? "Save File As" : "Open File");
		this.save = save;
		this.next = next;
		StringItem s = new StringItem("Not implemented yet", null);
		s.addCommand(SELECT_COMMAND);
		s.addCommand(DELETE_COMMAND);
		append(s);
		addCommand(new Command("Back", Command.BACK, 1));
		setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c.getCommandType() == Command.BACK) {
					TheMIDlet.display.setCurrent(getNext());
				}
			}
		});
	}

	public DrawCanvas getNext() {
		return next;
	}

}
