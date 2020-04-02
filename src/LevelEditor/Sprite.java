//TODO: Different package perhaps, seperate from GUI stuff.
package LevelEditor;

import javax.swing.text.View;
import java.awt.image.BufferedImage;

public class Sprite implements EngineComponent {

    private GameObject parentGameObject;
    private ImageResource img;
    private double worldToPxRatio;

    public Sprite(ImageResource img, GameObject parentGameObject, double worldToPxRatio){
        this.img = img;
        this.parentGameObject = parentGameObject;
        this.worldToPxRatio = worldToPxRatio;

    }

    public Sprite(ImageResource img, GameObject parentGameObject) {
        this(img, parentGameObject, 1d/100d );
    }

    @Override
    public String getName() {
        return "Sprite: " + img.getResourceName();
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SPRITE;
    }

    @Override
    public GameObject getGameObject() {
        return parentGameObject;
    }

    @Override
    public Transform getTransform() {
        return parentGameObject.getTransform();
    }

    public BufferedImage getImageData(){
        return img.getResource();
    }

    public int getWidth(){
        return img.getWidth();
    }

    public int getHeight(){
        return img.getHeight();
    }

    public double getWorldToPxRatio(){
        return worldToPxRatio;
    }

    public Viewport getSpriteBounds(){
        double x = parentGameObject.getTransform().pos.getX();
        double y = parentGameObject.getTransform().pos.getY();
        double width = getWidth() * worldToPxRatio;
        double height = getHeight() * worldToPxRatio;
        return new Viewport(x, y, width, height);
    }




}

