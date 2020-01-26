package Main;
import java.awt.*;

public class Defaults {
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static Color MAIN;
	public static Color BUTTON;
	public static Color HOVER;
	public static Color SUB_MAIN;
	public static Color SELECT;
	public static Color TOP;
	public static Color FOREGROUND;
	public static Color BUTTON_HOVER;
	
	
	static void setDark() {
		MAIN = new Color(31,31,31);
		BUTTON = new Color(50,50,50);
		HOVER = new Color(60,60,60);
		SUB_MAIN = new Color(20,20,20);
		SELECT = new Color(70,70,70);
		BUTTON_HOVER = new Color(80,80,80);
		TOP = Color.BLACK;
		FOREGROUND = Color.WHITE;
		Overlay.refreshUI();
	}
	static void setLight() {
		MAIN = new Color(230,230,230);
		BUTTON = new Color(210,210,210);
		HOVER = new Color(211,211,211);
		SUB_MAIN = new Color(240,240,240);
		SELECT = new Color(215,215,215);
		BUTTON_HOVER = new Color(200,200,200);
		TOP = Color.WHITE;
		FOREGROUND = Color.BLACK;
		Overlay.refreshUI();
	}
}
