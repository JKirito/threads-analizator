package Utils;

import java.awt.Component;
import java.awt.Container;

public class SwingUtils {

	public static void setEnableContainer(Container c, boolean band) {
		Component[] components = c.getComponents();
		c.setEnabled(band);
		for (int i = 0; i < components.length; i++) {
			components[i].setEnabled(band);

			if (components[i] instanceof Container) {
				setEnableContainer((Container) components[i], band);
			}
		}
	}
}
