package LevelEditor;

public class EngineEventObject {

    public enum EngineEvent { RESOURCE_LOAD };
    private Object callbackData;
    private EngineEvent eventType;

    public EngineEventObject(EngineEvent eventType, Object callbackData ){
        this.callbackData = callbackData;
        this.eventType = eventType;
    }

    public Object getEventData(){
        return callbackData;
    }

    public EngineEvent getEventType(){
        return eventType;
    }
}
