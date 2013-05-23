
package kaezadraw;

import javax.microedition.lcdui.*;

public class PaletteForm extends Form implements CommandListener, ItemCommandListener {

	private class CustomColorForm extends Form implements CommandListener {

		private PaletteForm parent;
		private ColorItem customItem;
		private Gauge rg;
		private Gauge gg;
		private Gauge bg;

		public CustomColorForm(PaletteForm parent, ColorItem customItem) {
			super("Custom Color");
			this.parent = parent;
			this.customItem = customItem;
			setCommandListener(this);
			int rgb = customItem.getRGB();
			int r = (rgb >> 16) & 0xFF;
			int g = (rgb >>  8) & 0xFF;
			int b = (rgb      ) & 0xFF;
			append(rg = new Gauge("R", true, 255, r));
			append(gg = new Gauge("G", true, 255, g));
			append(bg = new Gauge("B", true, 255, b));
			addCommand(new Command("OK", Command.OK, 1));
			addCommand(new Command("Back", Command.BACK, 1));
		}

		public void commandAction(Command c, Displayable d) {
			if (c.getCommandType() == Command.OK) {
				int rgb = (
					(rg.getValue() << 16)
					| (gg.getValue() << 8)
					| (bg.getValue())
				);
				customItem.setRGB(rgb);
				TheMIDlet.show(parent);
			}
			else if (c.getCommandType() == Command.BACK) {
				TheMIDlet.show(parent);
			}
		}

	}

	private class ColorItem extends ImageItem {

		private int rgb;

		public ColorItem() {
			this("", 0);
		}

		public ColorItem(String label, int rgb) {
			super(label, null, ImageItem.LAYOUT_DEFAULT, label);
			Image image = Image.createImage(16, 16);
			Graphics g = image.getGraphics();
			g.setColor(rgb);
			g.fillRect(0, 0, 16, 16);
			setImage(image);
			this.rgb = rgb;
		}

		public void setRGB(int rgb) {
			this.rgb = rgb;
			Image img = getImage();
			Graphics g = img.getGraphics();
			g.setColor(rgb);
			g.fillRect(0, 0, img.getWidth(), img.getHeight());
			setImage(img);
		}

		public int getRGB() {
			return rgb;
		}

	}

	private DrawCanvas next;
	private ColorItem customItem;

	private final Command CUSTOM_EDIT_COMMAND = new Command("Edit", Command.ITEM, 2);

	public PaletteForm(DrawCanvas next) {
		super("Palette");
		setCommandListener(this);
		this.next = next;
		initColorItems();
	}

	private void initColorItems() {
		doAppend(customItem = new ColorItem("Custom",  0x000000));
		doAppend(new ColorItem("Black",   0x000000));
		doAppend(new ColorItem("Blue",    0x0000FF));
		doAppend(new ColorItem("Green",   0x00FF00));
		doAppend(new ColorItem("Cyan",    0x00FFFF));
		doAppend(new ColorItem("Red",     0xFF0000));
		doAppend(new ColorItem("Magenta", 0xFF00FF));
		doAppend(new ColorItem("Yellow",  0xFFFF00));
		doAppend(new ColorItem("White",   0xFFFFFF));
		addCommand(new Command("Back", Command.BACK, 1));
		customItem.addCommand(CUSTOM_EDIT_COMMAND);
	};

	private void doAppend(ColorItem item) {
		Command ok = new Command("Select", Command.OK, 1);
		item.setItemCommandListener(this);
		item.addCommand(ok);
		item.setDefaultCommand(ok);
		append(item);
	}

	public void commandAction(Command c, Displayable d) {
		int t = c.getCommandType();
		if (t == Command.BACK) {
			TheMIDlet.show(next);
		}
	}

	public void commandAction(Command c, Item item) {
		int t = c.getCommandType();
		if (t == Command.OK) {
			ColorItem ci = (ColorItem)item;
			TheMIDlet.canvas.graphics.setColor(ci.getRGB());
			TheMIDlet.show(next);
		}
		else if (c == CUSTOM_EDIT_COMMAND) {
			CustomColorForm ccf = new CustomColorForm(this, customItem);
			TheMIDlet.show(ccf);
		}
	}

}
