package LevelEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class LevelEditor {

    private static final float SCALE = 2f;
    private static final int  WIDTH = (int)(800 * SCALE);
    private static final int  HEIGHT = (int)(600 * SCALE);

    public static void main(String[] args) throws IOException {
        TempSaver tempSaver = new TempSaver("Hello");
        tempSaver.updateTempConfig();

        UIManager.put("TabbedPane.tabInsets", new Insets(0, 0, 0, 0)); // This is like...amazing.
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0)); // This is like...amazing.
        UIManager.put("TabbedPane.selectedTabPadInsets",  new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.contentBorderInsets",  new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.contentAreaColor", Color.BLACK);
        UIManager.put("TabbedPane.selected", Color.BLACK);
        UIManager.put("TabbedPane.selectedForeground", Color.BLACK);
        UIManager.put("TabbedPane.selectHighlight", Color.BLACK);
        UIManager.put("TabbedPane.shadow", Color.BLACK);
       try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
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


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Level level = new Level();
                Screen screen = new Screen(WIDTH, 300, level);

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
                levelPane.addComponentListener(screen);
                MenuBar menu = new MenuBar();
                AssetPane assetManager = new AssetPane();
                utilityPane.addTab("Asset Manager", assetManager);
                JFrame frame = new JFrame("Level Editor");
                menu.addToFrame(frame);
                frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(true);
                frame.setLayout(new BorderLayout());
                frame.add(menu, BorderLayout.NORTH);
                frame.add(levelPane, BorderLayout.CENTER);
                frame.add(utilityPane, BorderLayout.SOUTH);
                frame.pack();
                frame.setLocationRelativeTo(null);
                levelPane.addTab("Tab 1", screen);
                levelPane.addTab("Tab 2", tab2);
                levelPane.addTab("Tab 3", tab3);
                levelPane.addTab("Tab 4", tab4);
                frame.setVisible(true);

                screen.start();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000); // waiting for screen to be fully initialized,
                                                      // 1 sec is just estimate.
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ReentrantLock objLock = level.getObjectsInLevelLock();
                        GameObject camera = new GameObject(0,0);
                        Camera cameraComp = new Camera((double) 1/100, screen, camera);
                        camera.addComponent(cameraComp);
                        objLock.lock();
                        try{
                            level.getObjectsInLevel().add(camera);
                        } finally{
                            objLock.unlock();
                        }
                        int i = 0;
                        while(true) {
                            //System.out.println("Times rendered: " + i);
                            cameraComp.display(level);
                            i++;
                        }
                    }
                }).start();
            }
        });
    }
}


