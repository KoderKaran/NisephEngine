//TODO: Different package perhaps, seperate from GUI stuff.
package LevelEditor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite implements Asset{

    String imageTitle;
    BufferedImage imageData;
    File imageFile;
    String path;

    public Sprite(String _imagePath){
        imageFile = new File(_imagePath);
        path = imageFile.getAbsolutePath();
        imageTitle = path.substring(path.lastIndexOf('\\') + 1);
        try {
            imageData = ImageIO.read(imageFile);
        } catch (IOException e) {
        }
    }

    @Override
    public String getPathToAsset() {
        return path;
    }

    @Override
    public Object getAssetData() {
        return imageData;
    }

    public String getImageTitle(){
        return imageTitle;
    }
}

