
package kaezadraw;

import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.io.file.*;
import javax.microedition.io.*;

public class FileDialog extends Form implements CommandListener, ItemCommandListener {

	public interface FileDialogListener {
		public boolean fileSelected(String filename, FileDialog dialog);
	}

	private boolean save;
	private DrawCanvas next;
	private String path;
	private int level;

	private static final Command SELECT_COMMAND = new Command("Select", Command.ITEM, 1);

	private FileDialogListener listener;

	public FileDialog(boolean save, DrawCanvas next) {
		super("");
		setTicker(new Ticker(save ? "Save File As" : "Open File"));
		this.save = save;
		this.next = next;
		path = "";
		level = 0;
		addCommand(new Command("Back", Command.BACK, 1));
		setCommandListener(this);
		populateList();
	}

	public void setFileDialogListener(FileDialogListener listener) {
		this.listener = listener;
	}

	public boolean isSave() {
		return save;
	}

	public void commandAction(Command c, Displayable d) {
		if (c.getCommandType() == Command.BACK) {
			TheMIDlet.show(getNext());
		}
	}

	public void commandAction(Command c, Item item) {
		if (c == SELECT_COMMAND) {
			String filename;
			if (item instanceof TextField) {
				TextField field = (TextField)item;
				filename = field.getString().trim();
				if (filename.length() == 0) {
					TheMIDlet.display.setCurrent(new Alert("You must provide a filename!"), this);
					return;
				}
			}
			else {
				filename = ((StringItem)item).getText();
				if (filename.endsWith("/")) {
					if (filename.equals("../")) {
						level--;
						int ind = path.lastIndexOf('/', path.length() - 2);
						String p = path.substring(0, ind + 1);
						populateList(p);
					}
					else {
						level++;
						populateList(path + filename);
					}
					return;
				}
			}
			if (listener != null) {
				if (!listener.fileSelected(path + filename, this))
					return;
			}
			TheMIDlet.show(getNext());
		}
	}

	private void populateList() {
		populateList(null);
	}

	private void populateList(String newPath) {
		try {
			if (level == 0) {
				deleteAll();
				Enumeration drives = FileSystemRegistry.listRoots();
				while (drives.hasMoreElements()) {
					String driveString = drives.nextElement().toString();
					String driveName = (driveString.toLowerCase().equals("c:/") ? "Phone" : "Memory Card");
					StringItem item = new StringItem(driveName, driveString);
					item.setLayout(Item.LAYOUT_NEWLINE_AFTER);
					item.addCommand(SELECT_COMMAND);
					item.setDefaultCommand(SELECT_COMMAND);
					item.setItemCommandListener(this);
					append(item);
				}
			}
			else {
				String tmpPath = (newPath != null ? newPath : path);
				Vector dirVec = new Vector();
				Vector fileVec = new Vector();
				FileConnection fc = (FileConnection)Connector.open("file:///" + tmpPath, Connector.READ);
				Enumeration filelist = fc.list("*", true);
				fc.close();
				String filename;
				dirVec.addElement("../");
				while (filelist.hasMoreElements()) {
					filename = filelist.nextElement().toString();
					fc = (FileConnection)Connector.open("file:///" + tmpPath + filename, Connector.READ);
					if (fc.isDirectory()) {
						dirVec.addElement(filename);
					}
					else {
						fileVec.addElement(filename);
					}
					fc.close();
				}
				deleteAll();
				for (int i = 0; i < dirVec.size(); i++) {
					filename = dirVec.elementAt(i).toString();
					StringItem item = new StringItem("Dir", filename);
					item.setLayout(Item.LAYOUT_NEWLINE_AFTER);
					item.addCommand(SELECT_COMMAND);
					item.setDefaultCommand(SELECT_COMMAND);
					item.setItemCommandListener(this);
					append(item);
				}
				for (int i = 0; i < fileVec.size(); i++) {
					filename = fileVec.elementAt(i).toString();
					StringItem item = new StringItem("File", filename);
					item.setLayout(Item.LAYOUT_NEWLINE_AFTER);
					item.addCommand(SELECT_COMMAND);
					item.setDefaultCommand(SELECT_COMMAND);
					item.setItemCommandListener(this);
					append(item);
				}
				if (save) {
					TextField item = new TextField("<new file>", "", 20, TextField.ANY);
					item.addCommand(SELECT_COMMAND);
					item.setDefaultCommand(SELECT_COMMAND);
					item.setItemCommandListener(this);
					append(item);
				}
			}
			if (newPath != null)
				path = newPath;
			if (level == 0)
				setTitle("File Systems");
			else
				setTitle(path);
		}
		catch (Exception e) {
			System.err.println(e.toString());
			TheMIDlet.display.setCurrent(new Alert(e.toString()), this);
		}
	}

	public DrawCanvas getNext() {
		return next;
	}

}
