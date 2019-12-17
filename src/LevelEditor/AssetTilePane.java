package LevelEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class AssetTilePane extends JPanel {

    private ArrayList<AssetTileComponent> tiles;
    private AssetTileComponent selectedTile = null;

    public AssetTilePane() {
        super();
        tiles = new ArrayList<AssetTileComponent>();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setSelectedTile(null);
                repaint();
            }
        });
    }

    @Override
    public Component add(Component comp) {
        if(comp instanceof AssetTileComponent){
            tiles.add((AssetTileComponent) comp);
        }
        return super.add(comp);
    }

    public void setSelectedTile(AssetTileComponent tile) { selectedTile = tile; }
    public AssetTileComponent getSelectedTile() { return selectedTile; }
    public ArrayList<AssetTileComponent> getTiles() { return tiles; }
}
