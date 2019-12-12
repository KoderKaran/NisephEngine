package LevelEditor;

public interface Asset {

    // Or would it be better to call it getURL and return a URL? (to be more general, might be overkill though.)
    String getPathToAsset();
    Object getAssetData();
}
