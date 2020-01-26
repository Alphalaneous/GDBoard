package Main;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class InnerWindow extends JPanel {

	// --------------------
	// Constructor

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String title;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final String iconLocation;

	// --------------------
	// Starting values and Initialization

	private boolean isPinPressed = false;
	private boolean toggleState = true;
	JButton pinButton;
	JPanel topBar = new JPanel(null);
	
	// --------------------
	// JButtonUI Changes

	JButtonUI defaultUI = new JButtonUI();

	InnerWindow(final String title, final int x, final int y, final int width, final int height, final String iconLocation) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.iconLocation = iconLocation;
	}

	// --------------------

	JPanel createPanel() {
		
		// --------------------
		// Mouse Listener to stop allowing clicking through the panel to close the
		// window

		setDoubleBuffered(true);
        addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);
				moveToFront();
			}
		});
		

		

		// TODO: Make dimension values relate to screen size
		// --------------------
		// Panel that holds everything

		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		setBounds(x, y, width + 2, height + 32);
		setOpaque(false);
		setBorder(BorderFactory.createLineBorder(new Color(255,255,255,50)));

		// --------------------
		// Top Bar Panel (holds X, title, and Pin)

		topBar.setBackground(Defaults.TOP);
		topBar.setBounds(1, 1, width, 30);

		// --------------------
		// Frame movement when dragging Top Bar
		MouseInputAdapter mia = new MouseInputAdapter(){
		    Point location;
		    Point pressed;

		    public void mousePressed(MouseEvent me){
		    	moveToFront();
		        pressed = me.getLocationOnScreen();
		        location = getLocation();
		    }

		    public void mouseDragged(MouseEvent me){
		        Point dragged = me.getLocationOnScreen();
		        double x = location.x + dragged.getX() - pressed.getX();
		        double y = location.y + dragged.getY() - pressed.getY();
		        Point p = new Point();
		        p.setLocation(x, y);
		        setLocation(p);
		        if(x + width >= 1921) {
		        	p.setLocation(1921 - width, p.getY());
					setLocation(p);
				}
				if(x <= -1) {
					p.setLocation(-1, p.getY());
					setLocation(p);
				}
				if(y + height + 32 >= 1081) {
					p.setLocation(p.getX(), 1081 - height - 32);
					setLocation(p);
				}
				if(y <= -1) {
					p.setLocation(p.getX(), -1);
					setLocation(p);
				}
				if(x + width >= 1921 && y + height + 32 >= 1081) {
					setLocation(1921 - width, 1081 - height - 32);
				}
				if(x + width >= 1921 &&y <= -1) {
					setLocation(1921 - width, -1);
				}
				if(x <= -1 && y + height + 32 >= 1081) {
					setLocation(-1, 1081 - height - 32);
				}
				if(x <= -1 && y <= -1) {
					setLocation(-1, -1);
				}
				if(x + width >= 672 && x <= 1248 && y <= 93) {
					p.setLocation(p.getX(), 93);
					setLocation(p);
				}
				
		     }
		};

		topBar.addMouseListener(mia);
		topBar.addMouseMotionListener(mia);
		
		BufferedImage iconImage = null;
		try {
			iconImage = ImageIO.read(new File(iconLocation));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image iconImageScaled = iconImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
		ImageIcon iconImageFinal = new ImageIcon(iconImageScaled);
		JLabel windowIcon = new JLabel(iconImageFinal);
		
		windowIcon.setBounds(3,0,30,30);
		
		topBar.add(windowIcon);

		// --------------------
		// Title text on Top Bar

		JLabel titleText = new JLabel(title);
		titleText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		titleText.setBounds(35, 0, width - 60, 30);
		titleText.setForeground(Defaults.FOREGROUND);
		topBar.add(titleText);

		// --------------------
		// X Icon Image on Button

		BufferedImage xImage = null;
		try {
			xImage = ImageIO.read(new File("src/resources/WindowIcons/X.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image xIconScaled = xImage.getScaledInstance(17, 17, Image.SCALE_SMOOTH);
		ImageIcon xIcon = new ImageIcon(xIconScaled);

		JButton closeButton = new JButton(xIcon);
		closeButton.setBounds(width - 30, 0, 30, 30);
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setBorder(BorderFactory.createEmptyBorder());
		closeButton.setBackground(Defaults.TOP);
		closeButton.setUI(defaultUI);

		// --------------------
		// InnerWindow Closing

		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toggle();
			}
		});

		topBar.add(closeButton);

		// --------------------
		// Pin Icon Image on Button

		BufferedImage pinImage = null;
		try {
			pinImage = ImageIO.read(new File("src/resources/WindowIcons/pin.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image pinScaled = pinImage.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
		ImageIcon pinIcon = new ImageIcon(pinScaled);

		// --------------------
		// Pressed Pin Icon Image on Button

		BufferedImage origPinPressed = null;
		try {
			origPinPressed = ImageIO.read(new File("src/resources/WindowIcons/pinPressed.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image pinPressed = origPinPressed.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
		ImageIcon pinIconPressed = new ImageIcon(pinPressed);

		pinButton = new JButton(pinIcon);
		pinButton.setBounds(width - 60, 0, 30, 30);
		pinButton.setMargin(new Insets(0, 0, 0, 0));
		pinButton.setBorder(BorderFactory.createEmptyBorder());
		pinButton.setBackground(Defaults.TOP);
		pinButton.setUI(defaultUI);

		// --------------------
		// Pin Switching

		pinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				moveToFront();
				if (isPinPressed) {

					// --------------------
					// Set to unpressed Pin Icon

					pinButton.setIcon(pinIcon);
					isPinPressed = false;

				} else {

					// --------------------
					// Set to pressed Pin Icon
					pinButton.setIcon(pinIconPressed);
					isPinPressed = true;

				}
			}
		});

		topBar.add(pinButton);

		// --------------------

		add(topBar);
		return this;
	}
	public void refreshListener() {
		Component[] components = getComponents();
        for (Component component : components)
        {
        	component.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mousePressed(MouseEvent e) {
    				super.mouseClicked(e);
    				moveToFront();
    			}
    		});
        }
	}
	
	public void refreshUI() {
		defaultUI.setBackground(Defaults.TOP);
		defaultUI.setHover(Defaults.HOVER);
		topBar.setBackground(Defaults.TOP);
		for (Component component : topBar.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.TOP);
			}
			if(component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		
	}
	
	public void setVisible() {
		topBar.setVisible(true);
		setBorder(BorderFactory.createLineBorder(new Color(255,255,255,50)));
		if(toggleState) {
			setVisible(true);
		}
	}

	void setInvisible() {
		if (!isPinPressed) {
			setVisible(false);
		}
		topBar.setVisible(false);
		setBorder(BorderFactory.createLineBorder(new Color(0,0,0,0)));

	}
	void toggle() {
		if(toggleState) {
			setVisible(false);
			toggleState = false;
		}
		else if(!toggleState) {
			setVisible(true);
			toggleState = true;
		}
	}
	boolean getPinState() {
		return isPinPressed;
	}
	void moveToFront() {
		Overlay.moveToFront(this);
	}
	void setPinVisible(boolean option) {
		pinButton.setVisible(option);
	}
}
