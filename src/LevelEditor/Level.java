package LevelEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import LevelEditor.EngineEventObject.EngineEvent;

public class Level implements Subscriber {
    // Is this sceneGraph? or does it contain it? It's 5 am. idek anymore.
    ResourcePool resourcePool;
    ArrayList<EngineEvent> subscriptions;
    Thread eventHandler;
    private LinkedBlockingQueue<EngineEventObject> eventQueue;
    //GameObjects[] <---- For The level editor, this is what is
    // going to be populated do display the stuff in the menu. If we populate this through
    // the drag-and-drop feature, then we can simply pass it to the Screen class and have it render
    // all of the objects in this array.
    Screen[] views; // Is an array just in case we want to draw the Level in more than one place.

    public Level() {

        subscriptions = new ArrayList<EngineEvent>();
        this.subscribe(EngineEvent.RESOURCE_LOAD);
        resourcePool = new ResourcePool();
        eventQueue = new LinkedBlockingQueue<EngineEventObject>();
        eventHandler = new Thread() {
            public void run() {
                try {
                    while (true) {
                        EngineEventObject eventObject = eventQueue.take();
                        handleEventsFromQueue(eventObject);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        eventHandler.start();
    }

    public void loadAssetIntoLevel(File asset/*, Vector2f position*/) { // TODO: Think of a less verbose name.
        resourcePool.loadResource(asset);
    }

    @Override
    public ArrayList<EngineEvent> getEventSubscriptions() {
        return null;
    }

    @Override
    public void subscribe(EngineEvent e) {
        EventBus.addSubscriber(this, e);
    }

    @Override
    public void unsubscribe(EngineEvent e) {
        EventBus.removeSubscriber(this, e);
    }

    @Override
    public void handleEvent(EngineEventObject e) {
        eventQueue.add(e);
    }

    private void handleEventsFromQueue(EngineEventObject e){
        switch(e.getEventType()) {
            case RESOURCE_LOAD:
                System.out.println("Handler: " + e.getEventData().toString() + Thread.currentThread()); //TEST.
                //TODO: Do something.
                break;
        }
    }
}
