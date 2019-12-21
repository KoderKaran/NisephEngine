package LevelEditor;

//TODO: Don't import EVERYTHING.
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class FileTilePane extends JPanel {

    private ArrayList<FileTileComponent> tiles;
    private FileTileComponent selectedTile = null;
    private DirectoryBar directoryBar;

    public FileTilePane(String projectPath) {
        super();
        tiles = new ArrayList<FileTileComponent>();
        //TODO: Figure out a way to subscribe to the event of the buttons on the directory bar being clicked.
        // This way we can rebuild the fileTilePane to display the tiles in the appropriate directory.
        directoryBar = new DirectoryBar(projectPath);
        constructTileComponents();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(tiles);
                setSelectedTile(null);
                repaint();
            }
        });
    }

    public void setSelectedTile(FileTileComponent tile) {
        selectedTile = tile;
    }

    public FileTileComponent getSelectedTile() {
        return selectedTile;
    }

    public ArrayList<FileTileComponent> getTiles() {
        return tiles;
    }

    public DirectoryBar getDirectoryBar() {
        return directoryBar;
    }

    //Run this method outside of EDT.
    public void constructTileComponents() {
        //TODO: As to avoid starting threads and re-constructing tiles every time user switches directories,
        // store every directories tiles in some manner and check if they have been constructed already. If not,
        // only run a new thread and construct them and all that jazz.
        // TODO_2: Make a Key value pair, between the list of tiles and directory. Should be an adequate solution.
        FileTilePane thisContainer = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                File currentDir = new File(directoryBar.getCurrentDisplayPath());
                String[] projectFiles = currentDir.list();
                if (projectFiles == null) {
                    return;
                } // In case of permissions error anything else weird like that.
                for (int i = 0; i < projectFiles.length; i++) {
                    String filePath = currentDir.getPath() + "\\" + projectFiles[i];
                    FileTileComponent newTile = new FileTileComponent(new File(filePath), thisContainer);
                    tiles.add(newTile);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        for(FileTileComponent tile: tiles) {
                            thisContainer.add(tile);
                        }
                    }
                });
            }
        }).start();
    }
}
