package LevelEditor;

public class Transform {
	// Might be most efficient to expose these variables since they are being updated
	// so often instead of calling functions to update them?
	public Vector2d pos;
	public Vector2d rot;
	public Vector2d scale;

	public Transform(Vector2d position, Vector2d rotation, Vector2d newScale){
		pos = position;
		rot = rotation;
		scale = newScale;
	}
}
