package assfan;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Themes extends JFrame implements ActionListener, ChangeListener {

    private final int RGB_MODE = 1;
    private final int HSB_MODE = 2;
    private final int CMY_MODE = 3;

    private final String RGB_label = "<html>"
	+ "<font color=#FF0000>R</font>"
	+ "<font color=#00FF00>G</font>"
	+ "<font color=#0000FF>B</font>"
	+ "</html>";
    private final String HSB_label = "HSB";
    private final String CMY_label = "<html>"
	+ "<font color=#00FFFF>C</font>"
	+ "<font color=#FF00FF>M</font>"
	+ "<font color=#FFFF00>Y</font>"
	+ "</html>";


    private Fan target;
    private JButton solve;
    private JButton scramble;
    private JLabel labelA;
    private JLabel labelB;
    private JLabel labelC;
    private JSlider sliderA;
    private JSlider sliderB;
    private JSlider sliderC;
    private JRadioButton RGB;
    private JRadioButton HSB;
    private JRadioButton CMY;

    private int colorMode;

    public Themes() {
	super("ColorChooser");
        getContentPane().add(makeGamePanel(), BorderLayout.CENTER);
        setColorMode(RGB_MODE);
        scrambleTarget();
    }
    public Themes(Fan fan) {
	super("ColorChooser");
        target = fan;
        getContentPane().add(makeGamePanel(), BorderLayout.CENTER);
        setColorMode(RGB_MODE);
        scrambleTarget();
    }
    public Themes(String s) {
	super("ColorChooser");
    }
        
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == scramble) {
            scrambleTarget();
        } else if (source == solve) {
            solveTarget();
            updateSliders();
        } else if (source == RGB) {
	    setColorMode(RGB_MODE);
        } else if (source == HSB) {
	    setColorMode(HSB_MODE);
	} else if (source == CMY) {
	    setColorMode(CMY_MODE);
        }
    }

    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == sliderA || source == sliderB || source == sliderC) {
            updateTarget();
        }
    }

    
    public void setColorMode(int mode) {
	if (colorMode != mode) {
	    colorMode = mode;
	    updateSliders();
	}
    }

    public void solveTarget() {
        target.setForeground(target.getBackground());
    }

    public void scrambleTarget() {
        int r = (int)(Math.random() * 256);
        int g = (int)(Math.random() * 256);
        int b = (int)(Math.random() * 256);
        target.setBackground(new Color(r, g, b));
    }
    public Color getColor() {
        int r = (int)(Math.random() * 256);
        int g = (int)(Math.random() * 256);
        int b = (int)(Math.random() * 256);
        return new Color(r, g, b);    
    }

    private void updateSliders() {
        Color t = target.getForeground();
        int r = t.getRed();
        int g = t.getGreen();
        int b = t.getBlue();
        switch (colorMode) {
        case RGB_MODE:
            sliderA.setValue(r);
	    labelA.setText("R");
            sliderB.setValue(g);
	    labelB.setText("G");
            sliderC.setValue(b);
	    labelC.setText("B");
            break;
        case HSB_MODE:
            float[] hsb = Color.RGBtoHSB(r, g, b, null);
            sliderA.setValue((int)(hsb[0]*256));
	    labelA.setText("H");
            sliderB.setValue((int)(hsb[1]*256));
	    labelB.setText("S");
            sliderC.setValue((int)(hsb[2]*256));
	    labelC.setText("B");
            break;
	case CMY_MODE:
            sliderA.setValue(255-r);
	    labelA.setText("C");
            sliderB.setValue(255-g);
	    labelB.setText("M");
            sliderC.setValue(255-b);
	    labelC.setText("Y");
            break;
        }
    }

    private void updateTarget() {
        int r = sliderA.getValue();
        int g = sliderB.getValue();
        int b = sliderC.getValue();
        switch (colorMode) {
        case RGB_MODE:
            target.setForeground(new Color(r, g, b));
            break;
        case HSB_MODE:
            target.setForeground(Color.getHSBColor(r/256f, g/256f, b/256f));
            break;
	case CMY_MODE:
	    target.setForeground(new Color(255-r, 255-g, 255-b));
	    break;
        }
    }

    private JPanel makeGamePanel() {
	JSplitPane s = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	s.setLeftComponent(makeTargetPanel());
	s.setRightComponent(makeControlPanel());

	JPanel p = new JPanel();
	p.setLayout(new BorderLayout());
	p.add(s, BorderLayout.CENTER);
        p.add(makeButtonPanel(), BorderLayout.SOUTH);
	return p;
    }

    private JPanel makeTargetPanel() {
        if(target == null)
	target = new Fan ();

	JPanel p = new JPanel();
	p.setBorder(BorderFactory.createTitledBorder("Set the spot color"));
	p.setLayout(new OverlayLayout(p));
	p.add(target);
	return p;
    }

    private JPanel makeControlPanel() {
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(makeModePanel());
	p.add(makeValuePanel());
	return p;
    }

    private JPanel makeButtonPanel() {
	solve = new JButton("Solve");
	solve.addActionListener(this);
	solve.setToolTipText("I give up! Set the spot color for me.");

	scramble = new JButton("Scramble");
	scramble.addActionListener(this);
	scramble.setToolTipText("Make a new random color");
	scramble.setMnemonic(KeyEvent.VK_S);

        JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        p.add(scramble);
        p.add(solve); 
        p.add(Box.createHorizontalGlue());
	
	return p;
    }

    private JPanel makeModePanel() {
	RGB = new JRadioButton(RGB_label);
        RGB.addActionListener(this);
	RGB.setToolTipText("Set RGB color mode");
	RGB.setMnemonic(KeyEvent.VK_R);

	HSB = new JRadioButton(HSB_label);
        HSB.addActionListener(this);
	HSB.setToolTipText("Set HSB color mode");
	HSB.setMnemonic(KeyEvent.VK_H);

	CMY = new JRadioButton(CMY_label);
	CMY.addActionListener(this);
	CMY.setToolTipText("Set CMY color mode");
	CMY.setMnemonic(KeyEvent.VK_C);

        ButtonGroup g = new ButtonGroup();
	g.add(RGB);
	g.add(HSB);
	g.add(CMY);

	JPanel inner = new JPanel();
	inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.add(RGB); 
        inner.add(HSB);
	inner.add(CMY);

	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	p.add(inner);
	p.add(Box.createHorizontalGlue());
	p.setBorder(BorderFactory.createTitledBorder("Mode"));
	p.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	return p;
    }

    private JPanel makeValuePanel() {
	labelA = new JLabel();
	labelB = new JLabel();
	labelC = new JLabel();
	sliderA = new JSlider(JSlider.VERTICAL, 0, 255, 0);
        sliderA.addChangeListener(this);
	sliderB = new JSlider(JSlider.VERTICAL, 0, 255, 0);
        sliderB.addChangeListener(this);
	sliderC = new JSlider(JSlider.VERTICAL, 0, 255, 0);
        sliderC.addChangeListener(this);

        JPanel p = new JPanel();
	p.setLayout(new GridLayout(1, 3, 5, 0));
	p.setBorder(BorderFactory.createTitledBorder("Value"));
	p.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        p.add(makeLabelledSlider(sliderA, labelA));
        p.add(makeLabelledSlider(sliderB, labelB));
        p.add(makeLabelledSlider(sliderC, labelC));
	return p;
    }

    private JPanel makeLabelledSlider(JSlider s, JLabel l) {
	s.setAlignmentX(JPanel.CENTER_ALIGNMENT);
	l.setAlignmentX(JPanel.CENTER_ALIGNMENT);
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(l);
	p.add(s);
	return p;
    }

    public static void main(String args[]) {
        JFrame director = new Themes();
	director.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	director.pack();
        director.setVisible(true);
    }
}
