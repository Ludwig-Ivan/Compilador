/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ludwig_sergio_leo.compilador;
import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;

public class NumeroLinea extends JPanel {
    private final JTextArea textArea;

    public NumeroLinea(JTextArea textArea) {
        this.textArea = textArea;
        setPreferredSize(new Dimension(40, Integer.MAX_VALUE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FontMetrics fm = g.getFontMetrics(textArea.getFont());
        int fontHeight = fm.getHeight();
        int startOffset = textArea.viewToModel2D(new Point(0, 0));
        Element root = textArea.getDocument().getDefaultRootElement();
        int startLine = root.getElementIndex(startOffset);

        Rectangle clip = g.getClipBounds();
        int start = clip.y / fontHeight;
        int end = (clip.y + clip.height) / fontHeight + 1;

        for (int i = start; i < end && i < root.getElementCount(); i++) {
            int y = i * fontHeight + fm.getAscent();
            g.drawString((i + 1) + " |", 2, y);
        }
    }
}