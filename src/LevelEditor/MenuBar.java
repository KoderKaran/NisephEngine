package LevelEditor;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {

	private JMenuBar menu;

	public MenuBar(){
		menu = new JMenuBar();
		init();
	}

	private void init(){
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu run = new JMenu("Run");
		JMenuItem newProject = new JMenuItem("New Project");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveAs = new JMenuItem("Save As");
		JMenuItem settings = new JMenuItem("Project Settings");
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
