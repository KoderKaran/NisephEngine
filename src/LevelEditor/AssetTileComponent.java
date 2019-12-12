package LevelEditor;

//TODO: Don't import EVERYTHING.
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AssetTileComponent extends JPanel {

    private Asset asset;
    private String tileString;
    private BufferedImage tileImage;

    public AssetTileComponent(Asset _asset, String _tileString, BufferedImage _tileImage) {
        asset = _asset;
        tileString = _tileString;
        tileImage = _tileImage;
        setLayout(new BorderLayout());
        add(new JLabel(new ImageIcon(tileImage)), BorderLayout.CENTER);
        JLabel tileLabel = new JLabel(tileString);
        add(tileLabel, BorderLayout.SOUTH);
    }

}
