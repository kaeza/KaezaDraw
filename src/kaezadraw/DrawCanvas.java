
package kaezadraw;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class DrawCanvas extends GameCanvas {

	public Image image;
	public Graphics graphics;

	int lastX = 0;
	int lastY = 0;

	public DrawCanvas() {
		super(false);
	}

	public void paint(Graphics g) {
		if (image == null) {
			image = Image.createImage(getWidth(), getHeight());
			graphics = image.getGraphics();
		}
		g.drawImage(image, 0, 0, Graphics.TOP|Graphics.LEFT);
	}

	public void pointerPressed(int x, int y) {
		super.pointerPressed(x, y);
		graphics.drawRect(x, y, 1, 1);
		lastX = x;
		lastY = y;
		repaint();
		serviceRepaints();
	}

	public void pointerDragged(int x, int y) {
		super.pointerDragged(x, y);
		graphics.drawLine(lastX, lastY, x, y);
		lastX = x;
		lastY = y;
		repaint();
		serviceRepaints();
	}

	public void pointerReleased(int x, int y) {
		super.pointerReleased(x, y);
		graphics.drawLine(lastX, lastY, x, y);
		lastX = x;
		lastY = y;
		repaint();
		serviceRepaints();
	}

	public void setImage(Image img) {
		image = img;
		repaint();
		serviceRepaints();
	}

}
