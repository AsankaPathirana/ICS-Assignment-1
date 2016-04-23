package assfan;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Fan extends JPanel implements ActionListener, ChangeListener{
    IndividualFan individualFan;
    private final JButton buttonPower = new JButton("ON");
    private final JButton buttonRotation = new JButton("Reverse");
    private final JButton buttonTheme = new JButton("Theme");
    private JSlider sliderSpeed = new JSlider(JSlider.HORIZONTAL, 2, 20, 2);
    private final String FAN_label = "<html>"+ "<font color=#FF0000>F</font>"+ "<font color=#00FF00>A</font>"+ "<font color=#0000FF>N</font>"	+ "</html>";
    private final String CONTROL_label = "<html>"+ "<font color=#00FFFF>C</font>"+ "<font color=#FF00FF>O</font>"+ "<font color=#FFFF00>N</font>"+ "<font color=#00FFFF>T</font>"+ "<font color=#FF00FF>R</font>"+ "<font color=#FFFF00>O</font>"+"<font color=#00FFFF>L</font>"	+ "</html>";
    //Controllers
    int hours, minutes, seconds;
    Themes themes = new Themes("Themes");
    Color colorBackground, colorBlade;
    //Constructor
    public Fan(){
        super();
        JPanel main = new JPanel(new BorderLayout(10, 5));
        main.setBorder(new LineBorder(Color.yellow));
        colorBackground = themes.getColor();
        main.setBackground(colorBackground);
        main.add(makeIndividualFan(1), BorderLayout.CENTER);
        main.add(makeIndividualFanControl(1), BorderLayout.SOUTH);
        add(main);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("ON".equals(cmd)) {
            powerOn();
        } 
        else if ("OFF".equals(cmd)) {
            powerOff();
        } 
        else if ("Reverse".equals(cmd)) {
            rotationFunction(); 
            System.out.println("Reverse vvv");           
        }
        else if ("Theme".equals(cmd)) {
            this.buttonPower.setEnabled(false);
            this.buttonRotation.setEnabled(false);
            this.buttonTheme.setEnabled(false);
            JFrame newTheme =  new Themes(this);          
            newTheme.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newTheme.pack();
            newTheme.setVisible(true);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == sliderSpeed){
            sliderSpeed = (JSlider)e.getSource();
            sliderFunction(sliderSpeed.getValue());//setdSpeed(sliderSpeed.getValue()); 
        }
    }
    
    public JPanel makeIndividualFan(int i) {
	individualFan = new IndividualFan(100,100);
        individualFan.setBackground(colorBackground);
 	JPanel p = new JPanel();
	p.setBorder(BorderFactory.createTitledBorder("Fan "+(i+1)));
        p.setForeground(colorBlade);
        p.setBackground(colorBackground);
        System.out.print(p.getBackground());
    	p.setLayout(new OverlayLayout(p));
    	p.add(individualFan);
	return p;
    }
    
    public JPanel makeIndividualFanControl(int i){
        JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(BorderFactory.createTitledBorder("Fan "+(i+1)+" Controls"));
        p.setBackground(colorBackground);
        buttonPower.addActionListener(this);
        p.add(buttonPower);
        buttonRotation.addActionListener(this);
        p.add(buttonRotation);
        sliderSpeed.addChangeListener(this);
        sliderSpeed.setBackground(colorBackground);
        p.add(sliderSpeed);
        buttonTheme.addActionListener(this);
        p.add(buttonTheme);
        return p;
    }
    public void powerOn(){   
        individualFan.getTimer().start();
        buttonPower.setText("OFF");
    }
    public void powerOff(){
        individualFan.getTimer().stop();
        buttonPower.setText("ON");        
    }
    public void rotationFunction(){
        individualFan.setRotation(); 
    }
    public void sliderFunction(int speed){
        individualFan.getTimer().setDelay(21 -speed); 
    }
    public IndividualFan getFan(){
        return individualFan;
    }
    public static void main(String[] args) {
        JComponent contents = new Fan();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Testing Fan");
        frame.getContentPane().add(contents);
        frame.pack();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }
}

class IndividualFan extends JPanel implements ActionListener{
    int alpha = 0, alphaChange =2;
    int delay, tempDelay;
    public Timer timer = new Timer(20, new IndividualFan.TimerListener());
    Themes t = new Themes("");
    Color bladeColor = t.getColor();
    Color circleColor = t.getColor();
    public IndividualFan(int width, int height) {
	setPreferredSize(new Dimension(width, height));
    }
    public void setRotation(){
        alphaChange = alphaChange * (-1);
    }
    public Timer getTimer() {
        return timer; // getter method for timer
    }
    //Make speed change smoothly
    public void setSmoothDelay(int delay){
        tempDelay = this.delay;
        while(tempDelay <= delay){
            timer.setDelay(tempDelay);
            tempDelay++;
        }
    }    
     
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        alpha = alpha+alphaChange;
        int xCenter = getWidth() / 2;
        int yCenter = getHeight() / 2;
        int radius = (int)(Math.min(getWidth(), getHeight()) * 0.5);
        int x = xCenter - radius;
        int y = yCenter - radius;
        
        g.setColor(bladeColor);
        g.fillArc(x, y, 2 * radius, 2 * radius, 0+ alpha, 30);
        g.fillArc(x, y, 2 * radius, 2 * radius, 90+ alpha, 30);
        g.fillArc(x, y, 2 * radius, 2 * radius, 180+ alpha, 30);
        g.fillArc(x, y, 2 * radius, 2 * radius, 270+ alpha, 30);
        g.setColor(circleColor);
        g.fillOval(xCenter-(int)(getWidth()*.1/2), yCenter-(int)(getHeight()*.1/2), (int)(getWidth()*.1), (int)(getHeight()*.1));
        //repaint();
    }
    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
}