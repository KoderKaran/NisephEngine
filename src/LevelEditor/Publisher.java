package LevelEditor;

import LevelEditor.EngineEventObject.EngineEvent;

public interface Publisher {

    public void publish(EngineEvent e, Object data);
}
