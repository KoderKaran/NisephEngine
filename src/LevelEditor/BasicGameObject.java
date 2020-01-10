package LevelEditor;

public class BasicGameObject implements GameObject{
	private Transform transform;
	private String pathToAsset;
	private double collidability;

	public BasicGameObject(int xPos, int yPos, String path){
		collidability = 0;
		transform = new Transform(new Vector2d(xPos,yPos), new Vector2d(0,0), new Vector2d(1,1));
		pathToAsset = path;
	}

	@Override
	public void configCollidability(double newCollidability) {
		collidability = newCollidability;
	}
}
