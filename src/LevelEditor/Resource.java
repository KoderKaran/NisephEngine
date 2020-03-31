package LevelEditor;

public interface Resource {

    enum ResourceType { IMAGE, AUDIO } //TODO: Prefabs.
    // TODO: Consolidate the return type of "getResource" function to a less general object if
    //  possible in the future when it becomes more clear what type of Resource(s)
    //  implementing classes can provide.
    Object getResource();
    String getResourceName();
    ResourceType getResourceType();
}


