package LevelEditor;

public interface EngineComponent {
    //TODO: Consider making this an abstract class instead that implements the
    // getGameObject and getTransform methods AND has a constructor that does
    // the assignment of the component's parent game object.
    enum ComponentType { SPRITE, CAMERA }

    String getName();
    ComponentType getType();
    GameObject getGameObject();
    Transform getTransform();
}
