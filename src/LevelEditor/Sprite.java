//TODO: Different package perhaps, seperate from GUI stuff.
package LevelEditor;

import java.awt.image.BufferedImage;

public class Sprite implements EngineComponent {

    private GameObject parentGameObject;
    private ImageResource img;

    public Sprite(ImageResource img, GameObject parentGameObject) {
        this.parentGameObject = parentGameObject;
        this.img = img;
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
}

