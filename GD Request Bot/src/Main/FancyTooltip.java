package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class FancyTooltip extends JToolTip {
	public FancyTooltip(JComponent component) {
		super();
		setComponent(component);
		setBackground(Defaults.TOP);
		setForeground(Defaults.FOREGROUND);
		setFont(Defaults.MAIN_FONT.deriveFont(14f));
		setBorder(BorderFactory.createEmptyBorder());
	}

	public void refresh() {
		setBackground(Defaults.TOP);
		setForeground(Defaults.FOREGROUND);
	}
}
