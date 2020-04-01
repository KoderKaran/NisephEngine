package LevelEditor;

import javax.imageio.ImageIO;
import java.awt.*;
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
            // The below shenanigans are necessary because we must
            // ensure that the imageType is BufferedImage.TYPE_INT_RGB.
            BufferedImage tempImage = ImageIO.read(imageFile);
            imageData = new BufferedImage(tempImage.getWidth(), tempImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = imageData.getGraphics();
            g.drawImage(tempImage, 0, 0, null);
            g.dispose();
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
