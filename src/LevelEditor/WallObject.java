package LevelEditor;

public class WallObject extends GameObjectDecorator{

	public WallObject(BasicGameObject newGameObj) {
		super(newGameObj);
		super.configCollidability(5);
	}

}
