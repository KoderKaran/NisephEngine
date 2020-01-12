package LevelEditor;

public class WallObject extends GameObjectDecorator{
	private GameObject gameObject;

	public WallObject(BasicGameObject newGameObj) {
		gameObject = newGameObj;
	}

	@Override
	public void checkCollidability() {
		//does something and also calls gameObjects checkCollidability method
	}
}
