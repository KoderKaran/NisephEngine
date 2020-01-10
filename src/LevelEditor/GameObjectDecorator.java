package LevelEditor;

public abstract class GameObjectDecorator implements GameObject{
	protected BasicGameObject gameObj;

	public GameObjectDecorator(BasicGameObject newGameObj){
		gameObj = newGameObj;
	}
	//all potential game object methods just do a call to the gameObj.,
	public void configCollidability(double newCollidability){
		gameObj.configCollidability(newCollidability);
	}


}
