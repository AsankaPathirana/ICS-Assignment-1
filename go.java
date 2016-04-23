/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assfan;

import java.awt.event.*;
import javax.swing.*;

public class go {
    public static void main(String args[]) {
        JFrame director = new Director();
	director.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	maybeAddLAFMenu(director);
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
		} catch (Exception x) {}
	    }
	}

	JMenuBar menus = frame.getJMenuBar();
	if (menus == null) return;

        UIManager.LookAndFeelInfo[] lafs =
	    UIManager.getInstalledLookAndFeels();
        Action a;
        JMenu lafMenu = new JMenu("Look&Feel");
        for (int i = 0; i < lafs.length; i++) {
            a = new SetLookAndFeel(lafs[i].getName(),
				   lafs[i].getClassName(), frame);
            lafMenu.add(new JMenuItem(a));
        }
        lafMenu.addSeparator();
        a = new SetLookAndFeel("default",
			       UIManager.getSystemLookAndFeelClassName(),
			       frame);
        lafMenu.add(new JMenuItem(a));
	menus.add(Box.createHorizontalGlue());
	menus.add(lafMenu);
    }
}


