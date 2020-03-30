package LevelEditor;

public class BasicGameObject extends GameObject{

	public BasicGameObject(int xPos, int yPos){
		this.setTransform(new Transform(new Vector2d(xPos,yPos), new Vector2d(0,0), new Vector2d(1,1)));
	}

	@Override
	public void checkCollidability() {
		//do something
	}
}
