package LevelEditor;

//TODO: Don't import EVERYTHING.
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AssetPane extends JPanel {

    private JTree projectHierarchy;
    private JScrollPane treeView;
    private JScrollPane scrollAssetTilePane;
    private FileTilePane fileTilePane;
    private JPanel utilityPanel; //Flow layout this john.
    private JPanel assetPaneContainer;
    private JButton uploadButton;
    private JMenuBar pathToDirectory;

    public AssetPane() {
        setLayout(new BorderLayout());
        //https://tinyurl.com/t6v6pky
        String rootPath = System.getProperty("user.dir");

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

        //TODO: Big todo, have to implement custom layout that implement's wrap around feature for components.

        assetPaneContainer = new JPanel(new BorderLayout());
        fileTilePane = new FileTilePane(rootPath);
        assetPaneContainer.add(fileTilePane.getDirectoryBar(), BorderLayout.NORTH);


        scrollAssetTilePane = new JScrollPane(fileTilePane);
        scrollAssetTilePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollAssetTilePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollAssetTilePane.setWheelScrollingEnabled(true);

        assetPaneContainer.add(scrollAssetTilePane, BorderLayout.CENTER);
        add(assetPaneContainer);
        //JUST FOR TESTING//
        utilityPanel = new JPanel();
        uploadButton = new JButton("Upload");
        utilityPanel.add(uploadButton);
        add(utilityPanel, BorderLayout.EAST);

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
