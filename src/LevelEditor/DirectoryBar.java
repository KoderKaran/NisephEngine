package LevelEditor;

//TODO: Don't import EVERYTHING.
import javax.swing.*;
import java.util.ArrayList;

public class DirectoryBar extends JMenuBar {

    String currentDisplayPath;
    ArrayList<JMenu> directoryChain;

    public DirectoryBar(String currentDisplayPath) {
        this.currentDisplayPath = currentDisplayPath;
        directoryChain = new ArrayList<JMenu>();
        createDirectoryChain();
        addDirectoryChain();
    }

    public String getCurrentDisplayPath(){
        return currentDisplayPath;
    }

    private void createDirectoryChain() {
        String[] s = currentDisplayPath.split("\\\\");
        for(int i = 0; i < s.length; i++){
            JMenu folder = new JMenu(s[i]);
            directoryChain.add(folder);
        }
    }

    private void addDirectoryChain() {
        for(JMenu directory: directoryChain) {
            add(directory);
        }
    }
}
