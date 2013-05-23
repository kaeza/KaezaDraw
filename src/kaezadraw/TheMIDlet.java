
package kaezadraw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.file.*;
import javax.microedition.io.*;

public class TheMIDlet extends MIDlet implements CommandListener, FileDialog.FileDialogListener {

	public static Display display;
	public static DrawCanvas canvas;

	private static final Command PALETTE_COMMAND = new Command("Palette", Command.SCREEN, 1);
	private static final Command SAVE_FILE_COMMAND = new Command("Save File", Command.SCREEN, 2);
	private static final Command LOAD_FILE_COMMAND = new Command("Load File", Command.SCREEN, 2);
	private static final Command CLEAR_COMMAND = new Command("Clear", Command.SCREEN, 3);

	public static final void show(Displayable d) {
		TheMIDlet.display.setCurrent(d);
	}

	public void startApp() {
		TheMIDlet.canvas = new DrawCanvas();
		TheMIDlet.canvas.addCommand(new Command("Quit", Command.EXIT, 1));
		TheMIDlet.canvas.addCommand(PALETTE_COMMAND);
		TheMIDlet.canvas.addCommand(SAVE_FILE_COMMAND);
		TheMIDlet.canvas.addCommand(LOAD_FILE_COMMAND);
		TheMIDlet.canvas.addCommand(CLEAR_COMMAND);
		TheMIDlet.canvas.setCommandListener(this);
		TheMIDlet.display = Display.getDisplay(this);
		TheMIDlet.show(TheMIDlet.canvas);
	}

	public void commandAction(Command c, Displayable d) {
		if (c == SAVE_FILE_COMMAND) {
			FileDialog fd = new FileDialog(true, TheMIDlet.canvas);
			fd.setFileDialogListener(this);
			TheMIDlet.show(fd);
		}
		else if (c == LOAD_FILE_COMMAND) {
			FileDialog fd = new FileDialog(false, TheMIDlet.canvas);
			fd.setFileDialogListener(this);
			TheMIDlet.show(fd);
		}
		else if (c == PALETTE_COMMAND) {
			PaletteForm pf = new PaletteForm(TheMIDlet.canvas);
			TheMIDlet.show(pf);
		}
		else if (c == CLEAR_COMMAND) {
			
		}
		else if (c.getCommandType() == Command.EXIT) {
			destroyApp(false);
			notifyDestroyed();
		}
	}

	public boolean fileSelected(String filename, FileDialog dialog) {
		System.err.println("FFS WORK DAMMIT!: Filename before: " + filename);
		try {
			if (!filename.endsWith(".kdw"))
				filename = filename + ".kdw";
			System.err.println("FFS WORK DAMMIT!: Filename after: " + filename);
			if (dialog.isSave()) {
				KImageIO.save("file:///" + filename, TheMIDlet.canvas.image);
				TheMIDlet.display.setCurrent(new Alert("File Saved!"), canvas);
			}
			else {
				TheMIDlet.canvas.setImage(KImageIO.load("file:///" + filename));
				TheMIDlet.display.setCurrent(new Alert("File Loaded!"), canvas);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TheMIDlet.display.setCurrent(new Alert(e.toString()), dialog);
		}
		return false;
	}

	public void pauseApp() {
		//display.setCurrent(TheMIDlet.canvas = null);
	}

	public void destroyApp(boolean unconditional) {
		display.setCurrent(TheMIDlet.canvas = null);
	}

}
