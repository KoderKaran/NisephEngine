package LevelEditor;

//TODO: Limit these imports.

import javax.swing.JTabbedPane;
import javax.swing.Icon;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Rectangle;

public class LevelTabbedPane extends JTabbedPane {

	//TODO: Perhaps, generate these dimensions dynamically dependant on screen size.
	public static final Dimension popoutWindowSize = new Dimension(600, 600);
	public static final Dimension mainWindowSize = new Dimension(400, 800);

	private Dimension tabSize;
	private boolean isMainPane;

	LevelTabbedPane(Dimension paneSize, Dimension tabSize, boolean isMainPane) {
		this.setPreferredSize(paneSize);
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
		this.revalidate();
		// Delete some of the test code.
		for(int i = 0; i < this.getTabCount(); i++){
			Rectangle UIBounds = this.getUI().getTabBounds(this, i);
			Component currentComponent = this.getTabComponentAt(i);
			System.out.print("UI: ");
			System.out.println(UIBounds);
			System.out.print("Component");
			System.out.println(currentComponent.getBounds());
			currentComponent.setPreferredSize(new Dimension(UIBounds.width, UIBounds.height));
		}
		this.revalidate();
	}

	public boolean isMainPane() {
		return isMainPane;
	}

}
