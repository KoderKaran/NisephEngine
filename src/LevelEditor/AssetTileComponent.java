package LevelEditor;

//TODO: Don't import EVERYTHING.
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class AssetTileComponent extends JPanel {

    private Asset asset;
    private String tileString;
    private BufferedImage tileImage;
    private AssetTilePane parentTilePane;

    public AssetTileComponent(Asset _asset, String _tileString, BufferedImage _tileImage, AssetTilePane _parentTilePane) {
        asset = _asset;
        tileString = _tileString;
        tileImage = _tileImage;
        parentTilePane = _parentTilePane;

        setLayout(new BorderLayout());
        add(new JLabel(new ImageIcon(tileImage)), BorderLayout.CENTER);
        JLabel tileLabel = new JLabel(tileString);
        add(tileLabel, BorderLayout.SOUTH);
        AssetTileComponent thisTile = this; // TODO: Better way to do this?
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                parentTilePane.setSelectedTile(thisTile);
                parentTilePane.repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        if(this.equals(parentTilePane.getSelectedTile())) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(70, 170, 255, 65));
            g2d.fillRect(0, 0, getWidth() + 10, getHeight() + 10);
        }
    }
}
