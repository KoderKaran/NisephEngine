package LevelEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import LevelEditor.EngineEventObject.EngineEvent;

public class Level implements Subscriber {
    // Is this sceneGraph? or does it contain it? It's 5 am. idek anymore.
    private ResourcePool resourcePool;
    private ArrayList<EngineEvent> subscriptions;
    private ArrayList<File> pendingResources;
    private final Object LOCK_FOR_PENDING = new Object();
    private Thread eventHandler;
    private LinkedBlockingQueue<EngineEventObject> eventQueue;
    private final ReentrantLock objectsInLevelLock = new ReentrantLock(true);;

    public ArrayList<GameObject> getObjectsInLevel() {
        return objectsInLevel;
    }

    public ReentrantLock getObjectsInLevelLock(){
        return objectsInLevelLock;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    private ArrayList<GameObject> objectsInLevel; // Always lock when using the "objectsInLevelLock".
    private Screen screen;

    public Level() {
        objectsInLevel = new ArrayList<GameObject>();
        subscriptions = new ArrayList<EngineEvent>();
        screen = null; //TODO: Omg fix.
        this.subscribe(EngineEvent.RESOURCE_LOAD);
        resourcePool = new ResourcePool();
        eventQueue = new LinkedBlockingQueue<EngineEventObject>();
        pendingResources = new ArrayList<File>();
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

    public void loadAssetDnD(File asset/*, Vector2f position*/) { // TODO: Think of a less verbose name.
        Resource resource = resourcePool.getResource(asset);
        if(resource != null) {
            // For now this is fine if kept simple, but it should
            // probably eventually be moved to a separate thread.
            GameObject obj = new GameObject(0,0);
            obj.addComponent(new Sprite((ImageResource) resource, obj));
            addObjectToLevel(obj);
            System.out.println("From loadAssetDnD: " + objectsInLevel);
            return;
        } else {
            if(ResourcePool.getResourceType(asset) != null) {
                resourcePool.loadResource(asset);
                synchronized (LOCK_FOR_PENDING) {
                    pendingResources.add(asset);
                }
            }
        }
    }

    private void addObjectToLevel(GameObject object){
        objectsInLevelLock.lock();
        try {
            objectsInLevel.add(object);
        } finally {
            objectsInLevelLock.unlock();
        }
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
                System.out.println("Handler: " + e.getEventData().toString() + Thread.currentThread());
                File loadedFile = ((ImageResource) e.getEventData()).getImageFile();
                synchronized (LOCK_FOR_PENDING) {
                    if (pendingResources.contains(loadedFile)) {
                        System.out.println(pendingResources);
                        GameObject obj = new GameObject(0,0);
                        obj.addComponent(new Sprite((ImageResource) e.getEventData(), obj));
                        addObjectToLevel(obj);
                        System.out.println(objectsInLevel);
                        pendingResources.remove(loadedFile);
                    }
                }
                break;
        }
    }

}
