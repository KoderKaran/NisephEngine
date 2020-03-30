package LevelEditor;

import LevelEditor.EngineEventObject.EngineEvent;

import java.util.ArrayList;

public interface Subscriber {

    public ArrayList<EngineEvent> getEventSubscriptions();
    public void subscribe(EngineEvent e);
    public void unsubscribe(EngineEvent e);
    public void handleEvent(EngineEventObject e);
}
