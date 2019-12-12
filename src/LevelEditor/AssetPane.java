package LevelEditor;

//TODO: Don't import EVERYTHING.
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class AssetPane extends JPanel {

    private JTree projectHierarchy;
    private JScrollPane treeView;
    private JScrollPane assetTilePane;
    private JPanel utilityPanel; //Flow layout this john.

    public AssetPane() {
        setLayout(new BorderLayout());
        //https://tinyurl.com/t6v6pky
        String rootPath = "C:\\Users\\The-Beast\\Documents\\GitHub\\NisephEngine";
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultMutableTreeNode root =
                        new DefaultMutableTreeNode(rootPath);
                addChildNode(root, new File(rootPath));
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        projectHierarchy = new JTree(root);
                        treeView = new JScrollPane(projectHierarchy);
                        add(treeView, BorderLayout.WEST);
                        revalidate();
                    }
                });
            }
        }).start();

        //JUST FOR TESTING//
        Sprite testSprite = new Sprite(rootPath + "\\res\\64x64.png");
        AssetTileComponent testTileComponent = new AssetTileComponent(testSprite, testSprite.getImageTitle(),
                (BufferedImage) testSprite.getAssetData());
        assetTilePane = new JScrollPane(testTileComponent);
        add(assetTilePane);
        //JUST FOR TESTING//
    }


    private void addChildNode(DefaultMutableTreeNode root, File projectDir) {
        String[] projectFiles = projectDir.list();
        if(projectFiles == null) { return; } //In case of permissions error anything else weird like that.
        for (int i = 0; i < projectFiles.length; i++) {
            String newPath = projectDir.getPath() + "\\" + projectFiles[i];
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(projectFiles[i]);
            if (Files.isDirectory(Path.of(newPath))) {
                File dir = new File(newPath);
                if(dir == null) { return; } //In case of permissions error anything else weird like that.
                addChildNode(childNode, dir);
            }
            root.add(childNode);
        }
    }
}
