/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package librerias;

import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NumeroLinea extends JPanel implements CaretListener, DocumentListener, PropertyChangeListener {

    private static final int MARGIN = 5;
    private final JTextArea textArea;

    public NumeroLinea(JTextArea textArea) {
        this.textArea = textArea;

        textArea.getDocument().addDocumentListener(this);
        textArea.addCaretListener(this);
        textArea.addPropertyChangeListener(this);
        textArea.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });

        setFont(textArea.getFont());
        setPreferredSize(new Dimension(40, Integer.MAX_VALUE));
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
        Insets insets = textArea.getInsets();
        int lineHeight = fontMetrics.getHeight();
        int startOffset = textArea.viewToModel2D(new Point(0, insets.top));

        try {
            int startLine = textArea.getLineOfOffset(startOffset);
            int y = insets.top + fontMetrics.getAscent();

            for (int i = startLine; i < textArea.getLineCount(); i++) {
                int lineStartOffset = textArea.getLineStartOffset(i);
                Rectangle r = textArea.modelToView2D(lineStartOffset).getBounds();

                y = r.y + fontMetrics.getAscent();
                g.drawString((i + 1) + "", MARGIN, y);
                
                if (y > getHeight()) break;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override public void caretUpdate(CaretEvent e) { repaint(); }
    @Override public void insertUpdate(DocumentEvent e) { repaint(); }
    @Override public void removeUpdate(DocumentEvent e) { repaint(); }
    @Override public void changedUpdate(DocumentEvent e) {}
    @Override public void propertyChange(PropertyChangeEvent evt) { repaint(); }
}
