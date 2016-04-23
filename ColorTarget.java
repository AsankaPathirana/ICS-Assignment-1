package assfan;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColorTarget extends JPanel {

    public ColorTarget(int width, int height) {
	setPreferredSize(new Dimension(width, height));
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
        g.setColor(getForeground());
        g.fillOval(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2);
    }
}
