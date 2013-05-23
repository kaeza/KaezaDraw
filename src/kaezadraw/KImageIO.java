
package kaezadraw;

import javax.microedition.lcdui.*;
import javax.microedition.io.file.*;
import java.io.*;
import javax.microedition.io.Connector;

public class KImageIO {

	public static final int MAGIC_ID = 0x4B447700; // "KDw\0"

	public static final void save(DataOutputStream stream, Image img)
		throws IOException, SecurityException
	{
		stream.writeInt(MAGIC_ID);
		stream.writeShort(img.getWidth());
		stream.writeShort(img.getHeight());
		int[] data = new int[img.getWidth() * img.getHeight()];
		img.getRGB(data, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
		for (int i = 0; i < data.length; i++) {
			stream.writeInt(data[i]);
		}
	}

	public static final void save(String url, Image img)
		throws IOException, SecurityException
	{
		DataOutputStream stream = Connector.openDataOutputStream(url);
		save(stream, img);
		stream.close();
	}

	public static final Image load(DataInputStream stream)
		throws IOException, SecurityException
	{
		int id = stream.readInt();
		if (id != MAGIC_ID) {
			throw new IOException("Wrong magic ID: got 0x" + Integer.toHexString(id) +
				" expected 0x" + Integer.toHexString(MAGIC_ID)
			);
		}
		int w = stream.readShort();
		int h = stream.readShort();
		if ((w <= 0) || (w > 4096) || (h <= 0) || (h > 4096))
			throw new IOException("Image too big or too small");
		int[] data = new int[w * h];
		for (int i = 0; i < data.length; i++) {
			data[i] = stream.readInt();
		}
		Image img = Image.createRGBImage(data, w, h, false);
		return img;
	}

	public static final Image load(String url)
		throws IOException, SecurityException
	{
		DataInputStream stream = Connector.openDataInputStream(url);
		Image img = load(stream);
		stream.close();
		return img;
	}

}
