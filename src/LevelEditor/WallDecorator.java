package LevelEditor;
//TESTING
public class WallDecorator extends GameObjectDecorator{
	private GameObject gameObject;

	public WallDecorator(BasicGameObject newGameObj) {
		gameObject = newGameObj;
	}

	@Override
	public void checkCollidability() {
		//does something and also calls gameObjects checkCollidability method
	}
}
