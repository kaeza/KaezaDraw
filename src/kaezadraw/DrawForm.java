/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kaezadraw;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class DrawForm extends Form {

	private class DrawCanvas extends GameCanvas {

		private Image image = null;
		private Graphics graphics;

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

		protected void pointerPressed(int x, int y) {
			super.pointerPressed(x, y);
			graphics.drawRect(x, y, 1, 1);
			lastX = x;
			lastY = y;
			repaint();
			serviceRepaints();
		}

		protected void pointerDragged(int x, int y) {
			super.pointerDragged(x, y);
			graphics.drawLine(lastX, lastY, x, y);
			lastX = x;
			lastY = y;
			repaint();
			serviceRepaints();
		}

		protected void pointerReleased(int x, int y) {
			super.pointerReleased(x, y);
			graphics.drawLine(lastX, lastY, x, y);
			lastX = x;
			lastY = y;
			repaint();
			serviceRepaints();
		}

	}

	private DrawCanvas canvas;

	public DrawForm() {
		super("KaezaDraw");
		canvas = new DrawCanvas();
	}

}
