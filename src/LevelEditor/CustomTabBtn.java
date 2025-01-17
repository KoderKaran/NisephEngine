package LevelEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomTabBtn extends JPanel implements MouseListener {

    private boolean held = false;
    private LevelTabbedPane pane;
    JLabel title;

    CustomTabBtn(int width, int height, LevelTabbedPane lpane,String tabTitle){
        setPreferredSize(new Dimension(width,height));

        addMouseListener(this);
        pane = lpane;
        title = new JLabel(tabTitle);
        add(title);
    }

    private Rectangle calculateDropRectangle(){
        //TODO: Eventually do some safety checks to see if tlX and tlY are 0,0 so they don't go negative when - padX or
        // - padY.

        //TODO: BUG - If component is offscreen and this is called on it, exception is called. Therefore, do a check to
        // make sure that component is on screen or smthing like that.
        int padX = 10;
        int padY = 10;
        int screenSpaceX = this.getLocationOnScreen().x;
        int screenSpaceY = this.getLocationOnScreen().y;
        int tlX = screenSpaceX - padX;
        int tlY = screenSpaceY - padY;
        int width = this.getWidth() + padX * 2;
        int height = this.getHeight() + padY * 2;
        return new Rectangle(tlX, tlY, width, height);
    }

    public LevelTabbedPane getParentTabbedPane(){
        return pane;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Changes the tab.
        System.out.println("Tab Count on click: "+ pane.getTabCount());
        int index = pane.indexOfTabComponent(this);
        pane.setSelectedIndex(index);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        held = true;
        System.out.println("Mouse Pressed on me.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        held = false;
        System.out.println("Mouse Released on me.");
//
//        System.out.println("----");
//        System.out.println(getParent());
//        System.out.println(getParent().getParent());
//        System.out.println(getParent().getParent().getParent());
//        System.out.println(getParent().getParent().getParent().getParent());
//        System.out.println(getParent().getParent().getParent().getParent().getParent());
//        System.out.println(getParent().getParent().getParent().getParent().getParent().getParent());
//        System.out.println("----");
//
        Point pScreenSpace = e.getLocationOnScreen();

        Point paneTL = pane.getLocationOnScreen();
        Rectangle paneScreenSpaceBounds = new Rectangle(paneTL.x, paneTL.y, pane.getWidth(), pane.getHeight() );
        //Check to see if new window should open

        //We need to build a way to detect whetheqr this is the main window or if it is a JDialogue.
        //If src is mainWindow AND tabCount > 1.
        Component compMouseIn = DnDHelper.getComponentMouseIn();
        if(pane.getTabCount() != 1 && compMouseIn != null){
            //TODO: Make into private helper method.
            if(compMouseIn instanceof LevelTabbedPane &&
                    !pane.equals(compMouseIn)) {
                this.DnDOnPane();
            } else if(compMouseIn instanceof CustomTabBtn &&
                    !pane.equals(((CustomTabBtn) compMouseIn).getParentTabbedPane())) {
                this.DnDOnCustomTab();
            }
        } else if(!pane.isMainPane() && pane.getTabCount() ==  1 && compMouseIn != this && compMouseIn != pane &&
                (compMouseIn instanceof LevelTabbedPane || compMouseIn instanceof CustomTabBtn)){
            JDialog window = (JDialog) SwingUtilities.windowForComponent(pane);
            //TODO: Make into private helper method.
            if(compMouseIn instanceof LevelTabbedPane &&
                    !pane.equals(compMouseIn)) {
                this.DnDOnPane();
            } else if(compMouseIn instanceof CustomTabBtn &&
                    !pane.equals(((CustomTabBtn) compMouseIn).getParentTabbedPane())) {
                this.DnDOnCustomTab();
            }
            window.dispose();
        } else if(!calculateDropRectangle().contains(pScreenSpace) && !paneScreenSpaceBounds.contains(pScreenSpace) && pane.getTabCount() != 1){
            //Conditions: Not inside of tab's bound box AND not inside of parent JTabbedPane AND not the only existing tab.
            //Do all this shit in another thread//
            int index = pane.indexOfTabComponent(this);

            Component srcTabContent = pane.getComponentAt(index);
            if(srcTabContent != null){
                pane.remove(index);
                JDialog newWindow = new JDialog();

                LevelTabbedPane newLevelTabbedPane = new LevelTabbedPane(LevelTabbedPane.POPOUT_WINDOW_SIZE, pane.getTabSize(), false);
                newLevelTabbedPane.addTab(this.title.getText(), srcTabContent);

                newWindow.setPreferredSize(LevelTabbedPane.POPOUT_WINDOW_SIZE);
                newWindow.add(newLevelTabbedPane);
                newWindow.pack();
                newWindow.setLocation(pScreenSpace.x - LevelTabbedPane.POPOUT_WINDOW_SIZE.width/2, pScreenSpace.y - LevelTabbedPane.POPOUT_WINDOW_SIZE.height/2);
                newWindow.setVisible(true);
            }
        }
    }

    private void DnDOnPane(){
        LevelTabbedPane destPane = (LevelTabbedPane) DnDHelper.getComponentMouseIn();
        int srcIndex = pane.indexOfTabComponent(this);
        Component srcTabContent = pane.getComponentAt(srcIndex);
        if(srcTabContent != null) {
            pane.remove(srcIndex);
            destPane.addTab(this.title.getText(), srcTabContent);
            destPane.setSelectedIndex(destPane.getTabCount() - 1);
        }
    }

    private void DnDOnCustomTab(){
        CustomTabBtn destComponent = (CustomTabBtn) DnDHelper.getComponentMouseIn();
        LevelTabbedPane destPane = destComponent.getParentTabbedPane();
        int srcIndex = pane.indexOfTabComponent(this);
        int destIndex = destPane.indexOfTabComponent(destComponent);
        Component srcTabContent = pane.getComponentAt(srcIndex);
        if(srcTabContent != null) {
            pane.remove(srcIndex);
            destPane.insertTab(this.title.getText(), null, srcTabContent,
                    null, destIndex);
            destPane.setSelectedIndex(destIndex);
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        DnDHelper.setComponentMouseIn(e.getComponent());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        DnDHelper.setComponentMouseIn(null);
        if(held){
            System.out.println("I've taken a prisoner.");
        }
    }
}