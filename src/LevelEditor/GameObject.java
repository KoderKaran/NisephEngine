package LevelEditor;

public abstract class GameObject {
	private Transform transform;

	public void setTransform(Transform newTrans){
		transform = newTrans;
	}
	//all the game objects behaviors will be public abstract
	public abstract void checkCollidability();
}
