package LevelEditor;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.AbstractAction;
import java.awt.Toolkit;
import java.awt.Insets;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;


public class LevelEditor {

	private static final float SCALE = 0.2f;
	private static final int  WIDTH = (int)(800 * SCALE);
	private static final int  HEIGHT = (int)(600 * SCALE);
	private static int i = 0;

	public static void main(String[] args){
		UIManager.put("TabbedPane.tabInsets", new Insets(0, 0, 0, 0)); // This is like...amazing.
		UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0)); // This is like...amazing.
		UIManager.put("TabbedPane.selectedTabPadInsets",  new Insets(0, 0, 0, 0));
		UIManager.put("TabbedPane.contentBorderInsets",  new Insets(0, 0, 0, 0));

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if(event instanceof MouseEvent) { // REMIND TO EXPLAIN SHIT WAY THAT I DID
					MouseEvent mouseEvent = (MouseEvent) event;
					if (mouseEvent.getID() == MouseEvent.MOUSE_ENTERED) {
						DnDHelper.setComponentMouseIn(mouseEvent.getComponent());
						i++;
					} else if (mouseEvent.getID() == MouseEvent.MOUSE_EXITED) {
						DnDHelper.setComponentMouseIn(null);
						i++;
					}
				}
			}
		}, AWTEvent.MOUSE_EVENT_MASK);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder( BorderFactory.createLineBorder(Color.black));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		JPanel mainPanel2 = new JPanel();
		mainPanel2.setBorder( BorderFactory.createLineBorder(Color.red));
		Dimension size = new Dimension(WIDTH/2, HEIGHT);
		mainPanel.setPreferredSize(size);
		mainPanel.setMaximumSize(size);
		mainPanel.setMinimumSize(size);

		mainPanel2.setPreferredSize(size);
		mainPanel2.setMaximumSize(size);
		mainPanel2.setMinimumSize(size);

		JButton btn = new JButton("Test1");
		JButton btn1 = new JButton("Test2");
		JButton btn2 = new JButton("Test3");

		JPanel tab1 = new JPanel();
		tab1.setPreferredSize(new Dimension(30,20));
		tab1.setName("tab1_JPanel");
		tab1.add(btn);
		JPanel tab2 = new JPanel();
		tab2.setPreferredSize(new Dimension(30,20));
		tab2.setName("tab2_JPanel");
		JPanel tab3 = new JPanel();
		tab3.setPreferredSize(new Dimension(30,20));
		tab3.setName("tab3_JPanel");
		JPanel tab4 = new JPanel();
		tab4.setPreferredSize(new Dimension(30,20));
		tab4.setName("tab4_JPanel");

		Dimension tabSize = new Dimension(200, 30);
		LevelTabbedPane tabbedPane = new LevelTabbedPane(LevelTabbedPane.popoutWindowSize, tabSize, true);

		tabbedPane.addTab("Tab 1", tab1);
		tabbedPane.addTab("Tab 2", tab2);
		tabbedPane.addTab("Tab 3", tab3);
		tabbedPane.addTab("Tab 4", tab4);

		btn1.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(tabbedPane);

			}
		});

		btn.addActionListener( new AbstractAction("name of button") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Setting size.");
				tabbedPane.setTabSize(new Dimension(200,30));
			}
		});

		mainPanel.add(btn1);
		mainPanel.add(btn2);
		JFrame frame = new JFrame("Level Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.add(mainPanel, BorderLayout.WEST);
		frame.add(mainPanel2, BorderLayout.EAST);
		frame.add(tabbedPane, BorderLayout.NORTH);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}


