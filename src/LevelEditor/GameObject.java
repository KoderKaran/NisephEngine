package LevelEditor;

import java.util.concurrent.CopyOnWriteArrayList;

import LevelEditor.EngineComponent.ComponentType;

public class GameObject {
	private CopyOnWriteArrayList<EngineComponent> objectComponents;	//CopyOnWriteArrayList Removes the need for
															// synchronization, but is expensive when mutating.
	private Transform transform;

	public GameObject(int xPos, int yPos) {
		this.setTransform(new Transform(new Vector2d(xPos,yPos), new Vector2d(0,0), new Vector2d(1,1)));
		objectComponents = new CopyOnWriteArrayList<EngineComponent>();
	}

	public void addComponent(EngineComponent component) {
		//!!!*IMPORTANT* Design choice: All components must be unique. *IMPORTANT*!!!
		if(containsComponentType(component.getType())){
			return;
		}
		objectComponents.add(component);
	}

	public void removeComponent(EngineComponent component) {
		objectComponents.remove(component);
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform newTransform) {
		transform = newTransform;
	}

	public CopyOnWriteArrayList<EngineComponent> getComponents(){
		return objectComponents;
	}

	private boolean containsComponentType(ComponentType type){
		for(EngineComponent component: objectComponents){
			if(type.equals(component.getType())){
				return true;
			}
		}
		return false;
	}

}
