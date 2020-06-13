package Main;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FancyTextArea extends JTextArea {
	public FancyTextArea(boolean intFilter, boolean allowNegative) {

		setBackground(Defaults.TEXT_BOX);
		setForeground(Defaults.FOREGROUND);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(175, 175, 175)),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		setCaret(new MyCaret());
		setCaretColor(Defaults.FOREGROUND);
		setFont(Defaults.MAIN_FONT.deriveFont(14f));
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Defaults.ACCENT),
						BorderFactory.createEmptyBorder(8, 8, 8, 8)));
			}

			@Override
			public void focusLost(FocusEvent e) {
				setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(175, 175, 175)),
						BorderFactory.createEmptyBorder(8, 8, 8, 8)));
			}
		});
		if(intFilter) {
			PlainDocument doc = (PlainDocument) getDocument();
			if(allowNegative){
				doc.setDocumentFilter(new MyNegIntFilter());
			}
			else {
				doc.setDocumentFilter(new MyIntFilter());
			}
		}
	}
	public void refreshAll(){
		setBackground(Defaults.TEXT_BOX);
		setForeground(Defaults.FOREGROUND);
		setCaretColor(Defaults.FOREGROUND);
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
				g.drawString(mark, x, y + fm.getAscent());
			}
		}

	}

	static class MyIntFilter extends DocumentFilter {
		@Override
		public void insertString(FilterBypass fb, int offset, String string,
								 AttributeSet attr) throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.insert(offset, string);

			if (test(sb.toString())) {
				super.insertString(fb, offset, string, attr);
			}
		}

		private boolean test(String text) {
			try {
				if(text.equalsIgnoreCase("")){
					return true;
				}
				if(text.contains("-")){
					return false;
				}
				Integer.parseInt(text);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text,
							AttributeSet attrs) throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);

			if (test(sb.toString())) {
				super.replace(fb, offset, length, text, attrs);
			}

		}

		@Override
		public void remove(FilterBypass fb, int offset, int length)
				throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.delete(offset, offset + length);

			if (sb.toString().length() == 0) {
				super.replace(fb, offset, length, "", null);
			} else {
				if (test(sb.toString())) {
					super.remove(fb, offset, length);
				}
			}
		}
	}
	static class MyNegIntFilter extends DocumentFilter {
		@Override
		public void insertString(FilterBypass fb, int offset, String string,
								 AttributeSet attr) throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.insert(offset, string);

			if (test(sb.toString())) {
				super.insertString(fb, offset, string, attr);
			}
		}

		private boolean test(String text) {
			try {
				if(text.equalsIgnoreCase("")){
					return true;
				}
				if(text.equalsIgnoreCase("-")){
					text = text + "0";
				}
				Integer.parseInt(text);
				return true;
			} catch (NumberFormatException e) {
				return false;

			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text,
							AttributeSet attrs) throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);

			if (test(sb.toString())) {
				super.replace(fb, offset, length, text, attrs);
			}

		}

		@Override
		public void remove(FilterBypass fb, int offset, int length)
				throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.delete(offset, offset + length);

			if (sb.toString().length() == 0) {
				super.replace(fb, offset, length, "", null);
			} else {
				if (test(sb.toString())) {
					super.remove(fb, offset, length);
				}
			}
		}
	}
}
