package assfan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Director extends JFrame implements ActionListener, ChangeListener {
    private final String FANS_label = "<html>"+ "<font color=#FF0000>F</font>"+ "<font color=#00FF00>A</font>"+ "<font color=#0000FF>N</font>"+ "<font color=#FF0000>S</font>"+ "</html>";
    private final String FANS_CONTROLS_label = "<html>"+ "<font color=#FF0000>F</font>"+ "<font color=#00FF00>A</font>"+ "<font color=#0000FF>N</font>"+ "<font color=#FF0000>S </font>"+ "<font color=#00FFFF>C</font>"+ "<font color=#FF00FF>O</font>"+ "<font color=#FFFF00>N</font>"+ "<font color=#00FFFF>T</font>"+ "<font color=#FF00FF>R</font>"+ "<font color=#FFFF00>O</font>"+"<font color=#00FFFF>L</font>"	+"<font color=#FF00FF>S</font>"+ "</html>";
    ImageIcon iconAbout = new ImageIcon(Director.class.getResource("about.gif"));
    ImageIcon iconQuit = new ImageIcon(Director.class.getResource("quit.gif"));
    private final String aboutText = "COMP9751 - Interactive Computer Systems \n Assignment  01 \n Fullest effeort to get 30% out of 30% of Final marks";
    //Foure Buttons Under Group Controller
    private JButton groupPower, groupReverse, fansButton,fansGroupsButton;
    private JSlider groupSpeed;
    JPanel fans, fanControls, fansWithController;
    JPanel[] individualFans;
    public static JFrame[] directorsCotainer;
    public static JFrame director;
    public static int fanCountNum;
    int fanGroupsNum = 1;
    public Director() {
        super("Fan Group");
        JMenuBar menus = new JMenuBar();
        setJMenuBar(menus);
        menus.add(makeFileMenu());
        JTabbedPane main = new JTabbedPane();
        main.addTab("Multiple Fans", makeGroupPanel(fanCountNum));
        main.setToolTipTextAt(0, "Let me control the fans individually and as group");
        main.addTab("Instructions", makeInstructionPanel());
        main.setToolTipTextAt(1, "Let me know how to control fan(s)?");
        getContentPane().add(main, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (null != cmd) {
            switch (cmd) {
                case "ON":
                    for (JPanel fan : individualFans) {
                        Fan f = (Fan) fan;
                        f.powerOn();
                    }
                    groupPower.setText("OFF");
                    break;
                case "OFF":
                    for (JPanel fan : individualFans) {
                        Fan f = (Fan) fan;
                        f.powerOff();
                    }
                    groupPower.setText("ON");
                    break;
                case "Reverse":
                    for (JPanel fan : individualFans) {
                        Fan f = (Fan) fan;
                        f.rotationFunction();
                    }
                    break;
                case "Fans":
                    int newFanCount;
                    JFrame frame = new JFrame();
                    String input = JOptionPane.showInputDialog(frame, "How many fans you need?");
                    if(input != null ){
                       newFanCount = Integer.parseInt(input);
                    }
                    else{
                       newFanCount = 3;       
                    }
                    fans.removeAll();
                    fans.repaint();
                    fanControls.validate();
                    fanControls.removeAll();
                    fanControls.repaint();
                    updateFans(newFanCount);
                    fans.validate();
                    fanControls.validate();
                    Toolkit.getDefaultToolkit().beep();
                    break;
                case "Fans Groups":
                    String fanGroupsVal = JOptionPane.showInputDialog(director, "How many more groups you need?");
                    if(fanGroupsVal != null ){
                        fanGroupsNum = Integer.parseInt(fanGroupsVal);
                        directorsCotainer = new JFrame[fanGroupsNum+1];
                        directorsCotainer[0] = director;
                        while(fanGroupsNum!=0){
                            fanGroupsNum--;
                            int directorIndex = 1;
                            directorsCotainer[directorIndex] = new Director();
                            director = (Director)directorsCotainer[directorIndex];
                            director.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            maybeAddLAFMenu(director);
                            director.setResizable(true);
                            director.pack();
                            director.setVisible(true);
                        }
                    }
                    else{
                       Toolkit.getDefaultToolkit().beep();      
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == groupSpeed) {
            groupSpeed = (JSlider) e.getSource();
            for (int i = 0; i < individualFans.length; i++) {
                Fan a = (Fan) individualFans[i];
                a.individualFan.getTimer().setDelay(21 - groupSpeed.getValue());
            }
        }

    }
    
    private JMenu makeFileMenu() {
        final JFrame frame = this;
        int c = ActionEvent.CTRL_MASK;
        JMenuItem about = new JMenuItem("About");
        about.setIcon(iconAbout);
        about.setMnemonic(KeyEvent.VK_A);
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, aboutText,"About Fans Group", -1, iconAbout);
            }
        });
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, c));

        JMenuItem quit = new JMenuItem("Quit");
        quit.setIcon(iconQuit);
        quit.setMnemonic(KeyEvent.VK_Q);
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, c));

        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        menu.add(about);
        menu.addSeparator();
        menu.add(quit);
        return menu;
    }
    private JPanel makeGroupPanel(int x) {
        JSplitPane s = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        s.setLeftComponent(makeFans(x));
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BorderLayout());
        groupPanel.add(s, BorderLayout.CENTER);
        groupPanel.add(makeGroupController(), BorderLayout.SOUTH);
        return groupPanel;
    }
    private JPanel makeFans(int x) {
        fansWithController = new JPanel();
        fansWithController.setLayout(new BoxLayout(fansWithController,BoxLayout.Y_AXIS));
        fans = new JPanel();
        fans.setBorder(BorderFactory.createTitledBorder(FANS_label));
        fanControls = new JPanel();
        fanControls.setBorder(BorderFactory.createTitledBorder(FANS_CONTROLS_label));
        
        int row = 2;
        if(x>8)
            row =3;
        else if(x>15)
            row = 4;
        else if(x>24)
            row = 5;
        fans.setLayout(new GridLayout(row, 0, 10, 8));
        fanControls.setLayout(new GridLayout(row, 0, 10, 8));
        individualFans = new JPanel[x];
        for (int i = 0; i < individualFans.length; i++) {
            individualFans[i] = new Fan();
            Fan f = (Fan)individualFans[i];
            fans.add(f.makeIndividualFan(i));
            fanControls.add(f.makeIndividualFanControl(i));
        }
        fansWithController.add(fans);
        fansWithController.add(fanControls);
        return fansWithController;
    }
    private JPanel updateFans(int x) {
        int row = 2;
        if(x>8)
            row =3;
        else if(x>15)
            row = 4;
        else if(x>24)
            row = 5;
        fans.setLayout(new GridLayout(row, 0, 10, 8));
        fanControls.setLayout(new GridLayout(row, 0, 10, 8));
        individualFans = new JPanel[x];
        for (int i = 0; i < individualFans.length; i++) {
            individualFans[i] = new Fan();
            Fan f = (Fan)individualFans[i];
            fans.add(f.makeIndividualFan(i));
            fanControls.add(f.makeIndividualFanControl(i));
        }
        fansWithController.add(fans);
        fansWithController.add(fanControls);
        return fansWithController;
    }
    

    private JPanel makeGroupController() {
        groupPower = new JButton("ON");
        groupPower.addActionListener(this);
        groupPower.setToolTipText("Power ON/OFF");

        groupReverse = new JButton("Reverse");
        groupReverse.addActionListener(this);
        groupReverse.setToolTipText("Reverse");
        groupReverse.setMnemonic(KeyEvent.VK_S);

        groupSpeed = new JSlider(JSlider.HORIZONTAL, 2, 20, 2);
        groupSpeed.addChangeListener(this);
        groupSpeed.setBackground(Color.BLACK);
        groupSpeed.setToolTipText("Group Speed Change");
        
       
        JPanel groupController = new JPanel();
        groupController.setLayout(new BoxLayout(groupController, BoxLayout.X_AXIS));
        groupController.setBackground(Color.BLACK);
        groupController.setBorder(BorderFactory.createTitledBorder("Group Controller"));
        groupController.add(groupPower);
        groupController.add(groupReverse);
        groupController.add(Box.createHorizontalGlue());
        groupController.add(groupSpeed);
    
        fansButton = new JButton("Fans");
        fansButton.addActionListener(this);
        fansButton.setToolTipText("Get Requested Fan");
        fansGroupsButton = new JButton("Fans Groups");
        fansGroupsButton.addActionListener(this);
        fansGroupsButton.setToolTipText("Get Requested Fan");
        groupController.add(fansButton);
        groupController.add(fansGroupsButton);
        return groupController;
    }
    private JScrollPane makeInstructionPanel() {
        JEditorPane e;
        try {
            e = new JEditorPane(Director.class.getResource("instructions.htm"));
        } catch (IOException x) {
            e = new JEditorPane();
            e.setText("Can't read instruction page");
        }
        e.setEditable(false);

        JScrollPane scrollPaneInstruction = new JScrollPane(e) {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 500;
                return d;
            }
        };
        scrollPaneInstruction.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneInstruction.setBorder(BorderFactory.createEtchedBorder());
        return scrollPaneInstruction;
    }

    public static void main(String args[]) {
//        JFrame frame = new JFrame();
//        String input = JOptionPane.showInputDialog(frame, "How many fans you need?");
//        if(input != null ){
//           fanCount1 = Integer.parseInt(input);
//        }
//        else{
//           fanCount1 = 3;       
//        }
        fanCountNum = 4;
        director = new Director();
        director.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maybeAddLAFMenu(director);
        director.setResizable(true);
        director.pack();
        director.setVisible(true);
    }

    private static void maybeAddLAFMenu(JFrame frame) {
        class SetLookAndFeel extends AbstractAction {

            private String laf;
            private JFrame frame;

            public SetLookAndFeel(String name, String laf, JFrame frame) {
                super(name);
                this.laf = laf;
                this.frame = frame;
            }

            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(laf);
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (Exception x) {
                }
            }
        }

        JMenuBar menus = frame.getJMenuBar();
        if (menus == null) {
            return;
        }

        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        Action a;
        JMenu lafMenu = new JMenu("Look&Feel");
        for (int i = 0; i < lafs.length; i++) {
            a = new SetLookAndFeel(lafs[i].getName(),
                    lafs[i].getClassName(), frame);
            lafMenu.add(new JMenuItem(a));
        }
        lafMenu.addSeparator();
        a = new SetLookAndFeel("default", UIManager.getSystemLookAndFeelClassName(), frame);
        lafMenu.add(new JMenuItem(a));
        menus.add(Box.createHorizontalGlue());
        menus.add(lafMenu);
    }
}
