package LevelEditor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResource implements Resource {

    private File imageFile;
    private BufferedImage imageData;
    private String imageName;
    private String imageFilePath;

    public ImageResource(File imageFile){
        this.imageFile = imageFile;
        this.imageFilePath = imageFile.getAbsolutePath();
        this.imageName = imageFile.getName();
        loadImage();
    }

    @Override
    public BufferedImage getResource() {
        return imageData;
    }

    public File getImageFile(){
        return imageFile;
    }

    @Override
    public String getResourceName() {
        return imageName;
    }

    private void loadImage(){
        try {
            imageData = ImageIO.read(imageFile);
        } catch (IOException e) {
            //TODO: Implement proper handling if unable to load.
            System.out.println("Unable to load image with path: " + imageFilePath);
        }
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.IMAGE;
    }
}
