package Main;

import Main.InnerWindows.LevelsWindow;
import Main.SettingsPanels.*;
import com.jidesoft.swing.ResizablePanel;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class InnerWindow extends ResizablePanel {

	private static final long serialVersionUID = 1L;
	private String title;
	private double x;
	private double y;
	private int width;
	private int height;
	private final String icon;
	private boolean floating;
	private Color alphaBorder = new Color(Defaults.ACCENT.getRed(), Defaults.ACCENT.getGreen(), Defaults.ACCENT.getBlue(), 100);
	private boolean isPinPressed = false;
	private boolean toggleState = true;
	private JButton closeButton = new JButton("\uE894");
	private JButton pinButton = new JButton("\uE840");
	private JButton minimizeButton = new JButton("\uE921");
	private JLabel pinButtonFill = new JLabel("  \uE842");
	private JPanel topBar = new JPanel(null);
	private JLabel windowIcon = new JLabel();
	private JButtonUI defaultUI = new JButtonUI();
	private JLabel titleText = new JLabel();


	//region Constructor for InnerWindow
	public InnerWindow(final String title, final int x, final int y, final int width, final int height, final String icon, boolean floating) {
		double y1;
		double x1;
		double ratio = 1920 / Defaults.screenSize.getWidth();
		this.title = title;
		if(!Settings.windowedMode) {
			x1 = x / ratio;
			y1 = y / ratio;
			if (x + width >= Defaults.screenSize.getWidth() + 1) {
				x1 = Defaults.screenSize.getWidth() + 1 - width;
			}
			if (x <= -1) {
				x1 = -1;
			}
			if (y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
				y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
			}
			if (y <= -1) {
				y1 = -1;
			}
			if (x + width >= Defaults.screenSize.getWidth() + 1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
				x1 = Defaults.screenSize.getWidth() + 1 - width;
				y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
			}
			if (x + width >= Defaults.screenSize.getWidth() + 1 && y <= -1) {
				x1 = Defaults.screenSize.getWidth() + 1 - width;
				y1 = -1;
			}
			if (x <= -1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
				x1 = -1;
				y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
			}
			if (x <= -1 && y <= -1) {
				x1 = -1;
				y1 = -1;
			}
			int middle = (int) (Defaults.screenSize.getWidth() / 2);
			if (x + width >= middle - 290 && x <= middle + 290 && y <= 93) {
				y1 = 93;
			}

			this.x = x1;
			this.y = y1;
		}
		else{
			this.x = x;
			this.y = y;
		}
		this.width = width;
		this.height = height;
		this.icon = icon;
		this.floating = floating;
	}
	//endregion
	public String getName(){
		return title;
	}
	public String getIcon(){
		return icon;
	}
	//region Create InnerWindow
	public ResizablePanel createPanel() {

		//region No Click Through listener
		setDoubleBuffered(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);
				moveToFront();
			}
		});

		//endregion

		//region Defaults and Attributes
		final boolean[] isDragging = {false};
		final boolean[] exited = {false};
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		if(!Settings.windowedMode) {
			setBounds((int) x, (int) y, width+10, height + 40);
		}
		else {
			if(floating) {
				setBounds((int) x, (int) y, width+10, height + 40);
			}
			else {

				if(title.startsWith("GDBoard")){
					Windowed.frame.setLocation((int) x, (int) y);
				}
				if(title.equalsIgnoreCase("Startup")){
					Onboarding.frame.setLocation((int) x, (int) y);
				}
			}
		}
		setOpaque(false);

		setBorder(new CompoundBorder(
				BorderFactory.createLineBorder(new Color(0,0,0,5),4),
				BorderFactory.createLineBorder(alphaBorder,1)));



		//endregion

		//region Mouse Relative to Window
		try {
			if(!Settings.getSettings("windowed").equalsIgnoreCase("true") && !floating) {
				Thread threadPos = new Thread(() -> {
					while (true) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Point p = null;
						try {
							p = MouseInfo.getPointerInfo().getLocation();
							SwingUtilities.convertPointFromScreen(p, topBar);
						} catch (NullPointerException ignored) {

						}

						if (Overlay.isVisible && !isDragging[0]) {
							assert p != null;
							if (p.getY() <= 30 && p.getY() >= 27 && p.getX() >= 0 && p.getX() <= width) {
								if (getY() <= -10) {
									Thread thread = new Thread(() -> {
										for (int j = 0; j < 15; j++) {
											try {
												Thread.sleep(1);
											} catch (InterruptedException ex) {
												ex.printStackTrace();
											}
											if (getY() >= -3 && getY() <= 0) {
												break;
											}
											setLocation(getX(), getY() + 3);
										}
									});
									thread.start();
								}
							}
						}

						assert p != null;
						if ((p.getX() >= width || p.getX() <= -1 || p.getY() <= -1 || p.getY() >= 30) && exited[0]) {
							if (getY() <= 0) {
								setLocation(getX(), -31);
							}
						}
					}
				});
				threadPos.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//endregion

		//region TopBar Dragging
		MouseInputAdapter mia = new MouseInputAdapter() {
			Point location;
			Point pressed;
			public void mousePressed(MouseEvent me) {
				moveToFront();
				pressed = me.getLocationOnScreen();
				if(!floating) {
					location = getLocation();
				}
				else{
					if(title.startsWith("GDBoard")){
						location = Windowed.frame.getLocation();
					}
					if(title.equalsIgnoreCase("Startup")){
						location = Onboarding.frame.getLocation();
					}
					if(title.equalsIgnoreCase("Settings")){
						location = SettingsWindow.frame.getLocation();
					}
				}
			}

			public void mouseDragged(MouseEvent me) {
				isDragging[0] = true;
				Point dragged = me.getLocationOnScreen();
				double x = location.x + dragged.getX() - pressed.getX();
				double y = location.y + dragged.getY() - pressed.getY();
				Point p = new Point();
				p.setLocation(x, y);
				if(!floating) {
					if (x + width >= Defaults.screenSize.getWidth() + 1) {
						p.setLocation(Defaults.screenSize.getWidth() + 1 - width, p.getY());
					}
					if (x <= -1) {
						p.setLocation(-1, p.getY());
					}
					if (y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
						p.setLocation(p.getX(), Defaults.screenSize.getHeight() + 1 - height - 32);
					}
					if (y <= -1) {
						p.setLocation(p.getX(), -1);
					}
					if (x + width >= Defaults.screenSize.getWidth() + 1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
						p.setLocation((int) Defaults.screenSize.getWidth() + 1 - width, (int) Defaults.screenSize.getHeight() + 1 - height - 32);
					}
					if (x + width >= Defaults.screenSize.getWidth() + 1 && y <= -1) {
						p.setLocation((int) Defaults.screenSize.getWidth() + 1 - width, -1);
					}
					if (x <= -1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
						p.setLocation(-1, (int) Defaults.screenSize.getHeight() + 1 - height - 32);
					}
					if (x <= -1 && y <= -1) {
						p.setLocation(-1, -1);
					}

					if (x + width >= MainBar.getMainBar().getX() && x <= MainBar.getMainBar().getWidth() + MainBar.getMainBar().getX() - 2 && y <= 93) {
						p.setLocation(p.getX(), 93);

					}
					setLocation(p);
				}
				else {
					if(!Settings.windowedMode && !title.equalsIgnoreCase("Startup")) {
						if (x + width >= Defaults.screenSize.getWidth() + Defaults.screenSize.x + 1) {
							p.setLocation(Defaults.screenSize.getWidth() + Defaults.screenSize.x + 1 - width, p.getY());
						}
						if (x <= -1 + Defaults.screenSize.x) {
							p.setLocation(-1 + Defaults.screenSize.x, p.getY());
						}
						if (y + height + 32 >= Defaults.screenSize.getHeight() + Defaults.screenSize.y + 1) {
							p.setLocation(p.getX(), Defaults.screenSize.getHeight() + Defaults.screenSize.y + 1 - height - 32);
						}
						if (y <= -1 + Defaults.screenSize.y) {
							p.setLocation(p.getX(), -1 + Defaults.screenSize.y);
						}
						if (x + width >= Defaults.screenSize.getWidth() + Defaults.screenSize.x + 1 && y + height + 32 >= Defaults.screenSize.getHeight() + Defaults.screenSize.y + 1) {
							p.setLocation((int) Defaults.screenSize.getWidth() + Defaults.screenSize.x + 1 - width, (int) Defaults.screenSize.getHeight() + Defaults.screenSize.y + 1 - height - 32);
						}
						if (x + width >= Defaults.screenSize.getWidth() + Defaults.screenSize.x + 1 && y <= -1 + Defaults.screenSize.y) {
							p.setLocation((int) Defaults.screenSize.getWidth() + Defaults.screenSize.x + 1 - width, -1 + Defaults.screenSize.y);
						}
						if (x <= -1 + Defaults.screenSize.x && y + height + 32 >= Defaults.screenSize.getHeight() + Defaults.screenSize.y + 1) {
							p.setLocation(-1 + Defaults.screenSize.x, (int) Defaults.screenSize.getHeight() + Defaults.screenSize.y + 1 - height - 32);
						}
						if (x <= -1 + Defaults.screenSize.x && y <= -1 + Defaults.screenSize.y) {
							p.setLocation(-1 + Defaults.screenSize.x, -1 + Defaults.screenSize.y);
						}

						if (x + width >= MainBar.getMainBar().getX() + Defaults.screenSize.x && x <= MainBar.getMainBar().getWidth() + MainBar.getMainBar().getX() + Defaults.screenSize.x - 2 && y <= 93 + Defaults.screenSize.y) {
							p.setLocation(p.getX(), 93 + Defaults.screenSize.y);

						}
					}
					if(title.startsWith("GDBoard")){
						Windowed.frame.setLocation(p);
						GraphicsDevice[] screens = GraphicsEnvironment
								.getLocalGraphicsEnvironment()
								.getScreenDevices();
						for (GraphicsDevice screen : screens) {
							if (screen.getDefaultConfiguration().getBounds().contains(p)) {
								Defaults.screenNum = Integer.parseInt(screen.getIDstring().replaceAll("Display", "").replace("\\", ""));
							}
						}
					}
					if(title.equalsIgnoreCase("Startup")){
						Onboarding.frame.setLocation(p);
					}
					if(title.equalsIgnoreCase("Settings")){
						SettingsWindow.frame.setLocation(p);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				isDragging[0] = false;
			}

			public void mouseEntered(MouseEvent e) {
				exited[0] = false;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exited[0] = true;
			}
		};

		MouseInputAdapter topScreenIA = new MouseInputAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				exited[0] = true;
			}
		};

		topBar.addMouseListener(mia);
		topBar.addMouseMotionListener(mia);


		//endregion

		//region WindowIcon attributes
		windowIcon.setForeground(Defaults.FOREGROUND);
		if(title.startsWith("GDBoard")){
			try {
				windowIcon.setIcon(new ImageIcon(ImageIO
						.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
								.getResource("Resources/Icons/windowIcon.png")))
						.getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			windowIcon.setText(icon);
		}
		windowIcon.setBounds(10, 0, 30, 30);
		windowIcon.setFont(Defaults.SYMBOLS.deriveFont(14f));
		topBar.add(windowIcon);
		//endregion

		//region TitleText attributes and initialization
		titleText.setText(title);
		titleText.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		titleText.setBounds(35, 2, width - 60, 30);
		titleText.setForeground(Defaults.FOREGROUND);
		topBar.add(titleText);
		//endregion

		//region CloseButton attributes
		closeButton.setFont(Defaults.SYMBOLS.deriveFont(14f));
		closeButton.setBounds(width - 30, 0, 30, 30);
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setForeground(Defaults.FOREGROUND);
		closeButton.setBorder(BorderFactory.createEmptyBorder());
		closeButton.setBackground(Defaults.TOP);
		closeButton.setUI(defaultUI);
		closeButton.addMouseListener(topScreenIA);
		closeButton.addMouseMotionListener(topScreenIA);

		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!floating){
					if(SwingUtilities.isLeftMouseButton(e)) {
						toggle();
					}
				} else {
					if(title.equalsIgnoreCase("Settings")) {
						SettingsWindow.toggleVisible();
					}
					if(title.startsWith("GDBoard")){
						Windowed.toggleVisible();
					}
					if(title.equalsIgnoreCase("Startup")){
						Onboarding.toggleVisible();
					}
				}

			}
		});

		topBar.add(closeButton);
		//endregion

		//region Pin Button Attributes

		minimizeButton.setBounds(width - 60, 0, 30, 30);
		minimizeButton.setMargin(new Insets(0, 0, 0, 0));
		minimizeButton.setBorder(BorderFactory.createEmptyBorder());
		minimizeButton.setBackground(Defaults.TOP);
		minimizeButton.setForeground(Defaults.FOREGROUND);
		minimizeButton.setUI(defaultUI);
		minimizeButton.addMouseListener(topScreenIA);
		minimizeButton.addMouseMotionListener(topScreenIA);
		minimizeButton.setFont(Defaults.SYMBOLS.deriveFont(14f));
		minimizeButton.setVisible(false);
		topBar.add(minimizeButton);
		//endregion

		//region Pin Switching

		minimizeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					if(!WindowedSettings.onTopOption) {
						Windowed.frame.setState(Frame.ICONIFIED);
					}
				}
			}
		});
		//endregion
		pinButton.setBounds(width - 60, 0, 30, 30);
		pinButton.setMargin(new Insets(0, 0, 0, 0));
		pinButton.setBorder(BorderFactory.createEmptyBorder());
		pinButton.setBackground(Defaults.TOP);
		pinButton.setForeground(Defaults.FOREGROUND);
		pinButton.setUI(defaultUI);
		pinButton.addMouseListener(topScreenIA);
		pinButton.addMouseMotionListener(topScreenIA);
		pinButton.setFont(Defaults.SYMBOLS.deriveFont(14f));
		pinButtonFill.setBounds(width - 60, 0, 30, 30);
		pinButtonFill.setBorder(BorderFactory.createEmptyBorder());
		pinButtonFill.setForeground(Defaults.FOREGROUND);
		pinButtonFill.setBackground(new Color(0, 0, 0, 0));
		pinButtonFill.setFont(Defaults.SYMBOLS.deriveFont(14f));
		pinButtonFill.setVisible(false);
		topBar.add(pinButton);
		pinButton.add(pinButtonFill);
		//endregion

		//region Pin Switching

		pinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					moveToFront();
					if (isPinPressed) {

						// --------------------
						// Set to unpressed Pin Icon

						pinButtonFill.setVisible(false);
						isPinPressed = false;

					} else {

						// --------------------
						// Set to pressed Pin Icon
						pinButtonFill.setVisible(true);
						isPinPressed = true;

					}
				}
			}
		});
		//region TopBar attributes
		topBar.setBackground(Defaults.TOP);
		topBar.setBounds(5, 5, width, 30);

		add(topBar);

		//endregion

		return this;
	}
	//endregion

	//region SetPin
	public void setPin(boolean pin){
		isPinPressed = pin;
		if (!isPinPressed) {
			pinButtonFill.setVisible(false);

		} else {
			pinButtonFill.setVisible(true);

		}
	}
	//endregion
	public void setTitle(String name){
		this.title = name;
		titleText.setText(name);
		topBar.updateUI();
	}
	//region Mouse Listener Refresh for Moving Window to Top
	public void refreshListener() {
		for (Component component : getComponents()) {
			if(component instanceof JPanel && !component.equals(topBar)) {
				for (Component component1 : ((JPanel) component).getComponents()) {
					if(component1 instanceof JPanel) {
						for (Component component2 : ((JPanel) component1).getComponents()) {
							component2.addMouseListener(new MouseAdapter() {
								public void mousePressed(MouseEvent e) {
									super.mouseClicked(e);
									moveToFront();
								}
							});
						}
					}
					component1.addMouseListener(new MouseAdapter() {

						public void mousePressed(MouseEvent e) {
							super.mouseClicked(e);
							moveToFront();
						}
					});
				}
			}
			component.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					super.mouseClicked(e);
					moveToFront();
				}
			});
		}
	}
	//endregion

	//region Refresh UI
	public void refreshUI() {


		defaultUI.setBackground(Defaults.TOP);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		topBar.setBackground(Defaults.TOP);
		closeButton.setForeground(Defaults.FOREGROUND);
		windowIcon.setForeground(Defaults.FOREGROUND);
		pinButton.setForeground(Defaults.FOREGROUND);
		pinButtonFill.setForeground(Defaults.FOREGROUND);
		minimizeButton.setForeground(Defaults.FOREGROUND);
		alphaBorder = new Color(Defaults.ACCENT.getRed(), Defaults.ACCENT.getGreen(), Defaults.ACCENT.getBlue(), 100);
		setBorder(new CompoundBorder(
				BorderFactory.createLineBorder(new Color(0,0,0,5),4),
				BorderFactory.createLineBorder(alphaBorder,1)));

		for (Component component : topBar.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.TOP);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		if(!Settings.windowedMode) {
			double y1;
			double x1;
			double ratioX = 1920 / Defaults.screenSize.getWidth();
			double ratioY = 1080 / Defaults.screenSize.getHeight();
			int x;
			int y;
			if(!floating) {
				x = getX();
				y = getY();
			}
			else {
				x = (int) ((Defaults.screenSize.getWidth()/2 - SettingsWindow.window.getWidth()/2) + Defaults.screenSize.x);
				y = 150 + Defaults.screenSize.y;

				System.out.println("Start: " + x + ", " + y);
			}
			x1 = x / ratioX;
			y1 = y / ratioY;
			if(!floating) {
				if (x + width >= Defaults.screenSize.getWidth() + 1) {
					x1 = Defaults.screenSize.getWidth() + 1 - width;
				}
				if (x <= -1) {
					x1 = -1;
				}
				if (y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
					y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
				}
				if (y <= -1) {
					y1 = -31;
				}
				if (x + width >= Defaults.screenSize.getWidth() + 1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
					x1 = Defaults.screenSize.getWidth() + 1 - width;
					y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
				}
				if (x + width >= Defaults.screenSize.getWidth() + 1 && y <= -1) {
					x1 = Defaults.screenSize.getWidth() + 1 - width;
					y1 = -31;
				}
				if (x <= -1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
					x1 = -1;
					y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
				}
				if (x <= -1 && y <= -1) {
					x1 = -1;
					y1 = -31;
				}
				int middle = (int) (Defaults.screenSize.getWidth() / 2);
				if (x + width >= middle - 290 && x <= middle + 290 && y <= 93) {
					y1 = 93;
				}
				setLocation((int) x1, (int) y1);
			}
		}
	}
	//endregion

	//region Refresh dimensions (for when resized)
	public void resetDimensions(int width, int height) {
		this.height = height;
		this.width = width;
		topBar.setBounds(5, 5, width, 30);
		pinButton.setBounds(width - 60, 0, 30, 30);
		pinButtonFill.setBounds(width - 60, 0, 30, 30);
		minimizeButton.setBounds(width - 60, 0, 30, 30);
		closeButton.setBounds(width - 30, 0, 30, 30);


	}
	//endregion

	public void setSettings(){
		Settings.setWindowSettings(title, getX() + "," + getY() + "," + isPinPressed + "," + isVisible());
	}

	//region Set InnerWindow visible
	public void setVisible() {
		topBar.setVisible(true);
		setBorder(new CompoundBorder(
				BorderFactory.createLineBorder(new Color(0,0,0,5),4),
				BorderFactory.createLineBorder(alphaBorder,1)));

		if (toggleState) {
			if(!floating) {
				setVisible(true);
				//Overlay.addToFrame(this);
			}
			else {
				if(title.equalsIgnoreCase("Settings")) {
					if(Settings.windowedMode){
						SettingsWindow.frame.setVisible(true);
					}
					else {
						SettingsWindow.window.setVisible(true);
					}
				}
				if(title.equalsIgnoreCase("Startup")){
					Onboarding.frame.setVisible(true);
				}
			}
		}
	}
	//endregion

	//region Set InnerWindow invisible
	public void setInvisible() {
		if (!isPinPressed) {
			if(!floating) {
				setVisible(false);
				//Overlay.removeFromFrame(this);
			}
			else {
				if(title.equalsIgnoreCase("Settings")) {
					if(Settings.windowedMode){
						SettingsWindow.frame.setVisible(false);
					}
					else {
						SettingsWindow.window.setVisible(false);
					}
				}
				if(title.startsWith("GDBoard")){
					Windowed.frame.setVisible(false);
					Main.close();
				}
				if(title.equalsIgnoreCase("Startup")){
					Onboarding.frame.setVisible(false);
				}
			}
		}
		topBar.setVisible(false);

		setBorder(BorderFactory.createEmptyBorder(-1,-1,-1,-1));
	}
	//endregion

	//region Toggle Visibility of InnerWindow
	public void toggle() {
		if (toggleState) {
			if(!floating) {
				setVisible(false);
				Overlay.removeFromFrame(this);
			}
			else {
				if(title.equalsIgnoreCase("Startup")){
					Onboarding.frame.setVisible(false);
					Main.close();
				}
				if(title.startsWith("GDBoard")){
					Main.close();
				}
			}
			if(title.equalsIgnoreCase("Settings")) {
				if(Settings.windowedMode){
					SettingsWindow.frame.setVisible(false);
				}
				else {
					SettingsWindow.window.setVisible(false);
					Overlay.frame.setFocusableWindowState(false);
					Overlay.frame.setFocusable(false);

				}
			}
			toggleState = false;
		} else {
			if(!floating) {
				setVisible(true);
				Overlay.addToFrame(this);
			}
			else {

				if(title.equalsIgnoreCase("Startup")){
					Onboarding.frame.setVisible(true);
				}
			}
			if(title.equalsIgnoreCase("Settings")) {
				if(Settings.windowedMode){
					SettingsWindow.frame.setVisible(true);
				}
				else {
					SettingsWindow.window.setVisible(true);
					Overlay.frame.setFocusableWindowState(true);
					Overlay.frame.setFocusable(true);

				}
			}
			toggleState = true;
		}
	}
	//endregion

	//region Move InnerWindow to front
	public void moveToFront() {
		Overlay.moveToFront(this);
	}
	//endregion

	//region Set PinButton visible
	void setPinVisible() {
		pinButton.setVisible(false);
	}
	public void setMinimize(boolean option) {
		minimizeButton.setVisible(option);
	}
	//endregion
}
