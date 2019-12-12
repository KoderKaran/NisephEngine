package LevelEditor;

//TODO: Limit these imports.
import javax.swing.*;
import java.awt.*;

public class LevelTabbedPane extends JTabbedPane {

    //TODO: Perhaps, generate these dimensions dynamically dependant on screen size.
    public static final Dimension POPOUT_WINDOW_SIZE = new Dimension(600, 600);
    public static final Dimension MAIN_WINDOW_SIZE = new Dimension(400, 800);

    private Dimension tabSize;
    private boolean isMainPane;

    LevelTabbedPane(Dimension paneSize, Dimension tabSize, boolean isMainPane) {
        this.setName("JLevelTabbedPane"); //TODO: Is this a good name?
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.tabSize = tabSize;
        this.isMainPane = isMainPane;
    }

    //Eventually make it take a level object or w/e its going to be called. For now string will do.
    @Override
    public void addTab(String levelTitle, Component component) {
        super.addTab(levelTitle, component);
        this.customAddTab(levelTitle);
    }

    @Override
    public void addTab(String levelTitle, Icon icon, Component component) {
        super.addTab(levelTitle, icon, component);
        this.customAddTab(levelTitle);
    }

    @Override
    public void addTab(String levelTitle, Icon icon, Component component, String tip) {
        super.addTab(levelTitle, icon, component, tip);
        this.customAddTab(levelTitle);
    }

    @Override
    public void insertTab(String levelTitle, Icon icon, Component component, String tip, int index) {
        super.insertTab(levelTitle, icon, component, tip, index);
        this.setTabComponentAt(index,
                new CustomTabBtn((int)this.tabSize.getWidth(), (int)this.tabSize.getHeight(), this, levelTitle));
    }

    private void customAddTab(String levelTitle) {
        this.setTabComponentAt(this.getTabCount() - 1,
                new CustomTabBtn((int)this.tabSize.getWidth(), (int)this.tabSize.getHeight(), this, levelTitle));
    }

    public Dimension getTabSize(){
        return this.tabSize;
    }

    public void setTabSize(Dimension newTabSize) {
        for(int i = 0; i < this.getTabCount(); i++){
            this.getTabComponentAt(i).setPreferredSize(newTabSize);
        }
        this.tabSize = newTabSize;
        // Delete some of the test code.
        //this.revalidate();
        for(int i = 0; i < this.getTabCount(); i++){
            Rectangle UIBounds = this.getUI().getTabBounds(this, i);

            this.getTabComponentAt(i).setPreferredSize(new Dimension(UIBounds.width, UIBounds.height));
            System.out.println("UI: " + UIBounds);
        }
        this.revalidate();
    }

    public boolean isMainPane() {
        return isMainPane;
    }

}
