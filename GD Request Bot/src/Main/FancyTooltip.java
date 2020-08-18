package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FancyTooltip extends JToolTip {

	static ArrayList<FancyTooltip> tooltips = new ArrayList<FancyTooltip>();

	public FancyTooltip(JComponent component) {
		super();
		setComponent(component);
		setBackground(Defaults.TOP);
		setForeground(Defaults.FOREGROUND);
		setFont(Defaults.SEGOE.deriveFont(14f));
		setBorder(BorderFactory.createEmptyBorder());
		tooltips.add(this);
	}

	public void refresh() {
		setBackground(Defaults.TOP);
		setForeground(Defaults.FOREGROUND);
	}
}
