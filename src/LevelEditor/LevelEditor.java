package LevelEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class LevelEditor {

    private static final float SCALE = 1f;
    private static final int  WIDTH = (int)(800 * SCALE);
    private static final int  HEIGHT = (int)(600 * SCALE);

    public static void main(String[] args) throws IOException {

        TempSaver tempSaver = new TempSaver("Hello");
        tempSaver.updateTempConfig();
        UIManager.put("TabbedPane.tabInsets", new Insets(0, 0, 0, 0)); // This is like...amazing.
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0)); // This is like...amazing.
        UIManager.put("TabbedPane.selectedTabPadInsets",  new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.contentBorderInsets",  new Insets(0, 0, 0, 0));

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
                @Override
                public void eventDispatched(AWTEvent event) {
                    if(event instanceof MouseEvent) {
                        MouseEvent mouseEvent = (MouseEvent) event;
                        if (mouseEvent.getID() == MouseEvent.MOUSE_ENTERED) {
                            DnDHelper.setComponentMouseIn(mouseEvent.getComponent());
                        } else if (mouseEvent.getID() == MouseEvent.MOUSE_EXITED) {
                            DnDHelper.setComponentMouseIn(null);
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

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel tab1 = new JPanel();
                tab1.setName("tab1_JPanel");
                JPanel tab2 = new JPanel();
                tab2.setName("tab2_JPanel");
                JPanel tab3 = new JPanel();
                tab3.setName("tab3_JPanel");
                JPanel tab4 = new JPanel();
                tab4.setName("tab4_JPanel");

                Dimension tabSize = new Dimension(200, 30);
                LevelTabbedPane levelPane = new LevelTabbedPane(LevelTabbedPane.POPOUT_WINDOW_SIZE, tabSize, true);
                LevelTabbedPane utilityPane = new LevelTabbedPane(LevelTabbedPane.POPOUT_WINDOW_SIZE, tabSize, true);
                utilityPane.setTabPlacement(JTabbedPane.BOTTOM);

                levelPane.addTab("Tab 1", tab1);
                levelPane.addTab("Tab 2", tab2);
                levelPane.addTab("Tab 3", tab3);
                levelPane.addTab("Tab 4", tab4);
                MenuBar menu = new MenuBar();
                AssetPane assetManager = new AssetPane();
                utilityPane.addTab("Asset Manager", assetManager);
                JFrame frame = new JFrame("Level Editor");
                menu.addToFrame(frame);
                frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(true);
                frame.add(menu, BorderLayout.SOUTH);
                frame.setLayout(new BorderLayout());
                frame.add(levelPane, BorderLayout.NORTH);
                frame.add(utilityPane, BorderLayout.SOUTH);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}


