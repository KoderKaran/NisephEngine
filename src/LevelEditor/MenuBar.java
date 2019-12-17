package LevelEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuBar extends JMenuBar {

	private JMenuBar menu;

	public MenuBar(){
		menu = new JMenuBar();
		init();
	}

	private void init(){
		// TODO: MOST OF THE FUNCTIONALITY OF THE BUTTONS ARE PLACEHOLDERS RN!
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu run = new JMenu("Run");
		JMenuItem newProject = new JMenuItem(new AbstractAction("New Project") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Its making a new Project!");
			}
		});
		JMenuItem open = new JMenuItem(new AbstractAction("Open") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Its Opening!");
			}
		});
		JMenuItem save = new JMenuItem(new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Its Saving!");
			}
		});
		JMenuItem saveAs = new JMenuItem(new AbstractAction("Save As") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Its Saving as!");
			}
		});
		JMenuItem settings = new JMenuItem(new AbstractAction("Project Settings") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("The Project Settings are opening!");
			}
		});
		newProject.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		open.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		save.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		saveAs.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		settings.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		file.add(newProject);
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.add(settings);
		menu.add(file);
		menu.add(edit);
		menu.add(run);
	}

	public void addToFrame(JFrame frame){
		frame.setJMenuBar(menu);
	}

}
