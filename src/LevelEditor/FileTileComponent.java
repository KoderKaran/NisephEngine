package LevelEditor;

//TODO: Don't import EVERYTHING.
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileTileComponent extends JPanel {

    private static final int ICON_WIDTH = 64;
    private static final int ICON_HEIGHT = 64;
    private String tileString;
    private ImageIcon tileIcon;
    private static ImageIcon defaultAudioIcon;
    private static ImageIcon defaultFileIcon;
    private static ImageIcon defaultFolderIcon;

    static {
        try {
            String pathToFolder = System.getProperty("user.dir") + "\\res";
            defaultFileIcon = new ImageIcon(ImageIO.read(new File(pathToFolder + "\\default.png"))
                    .getScaledInstance(ICON_WIDTH, ICON_HEIGHT,  java.awt.Image.SCALE_SMOOTH));
            defaultAudioIcon = new ImageIcon(ImageIO.read(new File(pathToFolder + "\\audio.png"))
                    .getScaledInstance(ICON_WIDTH, ICON_HEIGHT,  java.awt.Image.SCALE_SMOOTH));
            defaultFolderIcon = new ImageIcon(ImageIO.read(new File(pathToFolder + "\\folder.png"))
                    .getScaledInstance(ICON_WIDTH, ICON_HEIGHT,  java.awt.Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileTilePane parentTilePane;
    private File tileFile;

    public FileTileComponent(File tileFile, FileTilePane container) {
        this.tileFile = tileFile;
        this.tileString = tileFile.getName();
        this.parentTilePane = container;
        if(tileString == null) { return; } // TODO: I'm not really sure if this is correct, any edge cases?
        int dotIndex = tileString.lastIndexOf('.');
        String fileExtension;
        if(dotIndex != -1){
            fileExtension = tileString.substring(dotIndex + 1);
        } else{
            fileExtension = null;
        }

        if(fileExtension == null && tileFile.isDirectory()){
            tileIcon = defaultFolderIcon;

        } else if (fileExtension.equalsIgnoreCase("JPEG") || fileExtension.equalsIgnoreCase("PNG") ||
                fileExtension.equalsIgnoreCase("GIF") || fileExtension.equalsIgnoreCase("BMP")){
                try {
                    tileIcon = new ImageIcon(ImageIO.read(tileFile)
                            .getScaledInstance(ICON_WIDTH, ICON_HEIGHT,  java.awt.Image.SCALE_SMOOTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else if(fileExtension.equalsIgnoreCase("WAV") ||
                fileExtension.equalsIgnoreCase("MP3") || fileExtension.equalsIgnoreCase("ORG")){
            tileIcon = defaultAudioIcon;
        } else {
            tileIcon = defaultFileIcon;
        }
        FileTileComponent thisTile = this;
        //TODO: Since FileTileComponents are being constructed on a separate thread,
        // these need to be pushed on to EDT queue?
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setLayout(new BorderLayout());
                add(new JLabel(tileIcon), BorderLayout.CENTER);
                JLabel tileLabel = new JLabel(tileString);
                add(tileLabel, BorderLayout.SOUTH);
                FileTileTransferHandler transferHandler = new FileTileTransferHandler();
                setTransferHandler(new FileTileTransferHandler());
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        parentTilePane.setSelectedTile(thisTile);
                        parentTilePane.repaint();
                        transferHandler.exportAsDrag(thisTile, e, TransferHandler.MOVE);
                    }
                });
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

    private class FileTileTransferHandler extends TransferHandler {

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new AssetTransfarble();
        }

        @Override
        public int getSourceActions( JComponent c ) {
            return MOVE;
        }

        @Override
        protected void exportDone( JComponent source, Transferable data, int action ) {
            super.exportDone( source, data, action );
        }
    }

    private class AssetTransfarble implements Transferable{

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{ DataFlavor.javaFileListFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if(flavor.equals(DataFlavor.javaFileListFlavor)){
                return true;
            }
            return false;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            ArrayList<File> file = new ArrayList<File>();
            file.add(tileFile);
            return file;
        }
    }
}
