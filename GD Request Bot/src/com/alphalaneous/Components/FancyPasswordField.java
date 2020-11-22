package com.alphalaneous.Components;

import com.alphalaneous.Defaults;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class FancyPasswordField extends JPasswordField {

	private UndoManager undoManager = new UndoManager();

	public static ArrayList<FancyPasswordField> fields = new ArrayList<>();

	public FancyPasswordField() {

		setBackground(Defaults.TEXT_BOX);
		setForeground(Defaults.FOREGROUND);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(175, 175, 175)),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		setCaret(new MyCaret());
		setCaretColor(Defaults.FOREGROUND);
		setFont(Defaults.SEGOE.deriveFont(14f));
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Defaults.ACCENT),
						BorderFactory.createEmptyBorder(4, 8, 4, 8)));
			}

			@Override
			public void focusLost(FocusEvent e) {
				setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(175, 175, 175)),
						BorderFactory.createEmptyBorder(4, 8, 4, 8)));
			}
		});

		Document doc = getDocument();
		UndoableEditListener undoableEditListener = e -> undoManager.addEdit(e.getEdit());
		doc.addUndoableEditListener(undoableEditListener);

		InputMap im = getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo");
		//noinspection MagicConstant
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | Event.SHIFT_MASK), "Redo");

		am.put("Undo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (undoManager.canUndo()) {
						undoManager.undo();
					}
				} catch (CannotUndoException exp) {
					exp.printStackTrace();
				}
			}
		});
		am.put("Redo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (undoManager.canRedo()) {
						undoManager.redo();
					}
				} catch (CannotUndoException exp) {
					exp.printStackTrace();
				}
			}
		});

	}
	public void clearUndo(){
		undoManager.discardAllEdits();
	}

	public void refresh_(){
		setBackground(Defaults.TEXT_BOX);
		setForeground(Defaults.FOREGROUND);
		setCaretColor(Defaults.FOREGROUND);
	}

	public static void refreshAll(){
		for(FancyPasswordField field : fields){
			field.refresh_();
		}
	}

	public static class MyCaret extends DefaultCaret {

		MyCaret() {
			setBlinkRate(500);
		}

		@Override
		protected synchronized void damage(Rectangle r) {
			if (r == null) {
				return;
			}

			JTextComponent comp = getComponent();
			FontMetrics fm = comp.getFontMetrics(comp.getFont());
			int textWidth = fm.stringWidth("|");
			int textHeight = fm.getHeight();
			x = r.x;
			y = r.y;
			width = textWidth;
			height = textHeight;
			repaint();
		}

		@Override
		public void paint(Graphics g) {
			JTextComponent comp = getComponent();
			if (comp == null) {
				return;
			}

			int dot = getDot();
			Rectangle r;
			try {
				r = comp.modelToView(dot);
			} catch (BadLocationException e) {
				return;
			}
			if (r == null) {
				return;
			}

			if ((x != r.x) || (y != r.y)) {
				repaint();
				damage(r);
			}

			if (isVisible()) {
				FontMetrics fm = comp.getFontMetrics(comp.getFont());

				g.setColor(comp.getCaretColor());
				String mark = "|";
				g.drawString(mark, x, y + fm.getAscent() - 1);
			}
		}

	}
}
