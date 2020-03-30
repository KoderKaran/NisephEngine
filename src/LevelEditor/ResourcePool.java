package LevelEditor;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import LevelEditor.EngineEventObject.EngineEvent;

public class ResourcePool implements Publisher { //

    HashMap<String, Resource> resourcePool;
    LinkedBlockingQueue<File> resourceLoadQueue;
    Thread poolThread;

    public ResourcePool() {
        resourcePool = new HashMap<String, Resource>();
        resourceLoadQueue = new LinkedBlockingQueue<File>();
        poolThread = new Thread() {
            public void run() {
                loadResourceFromQueue();
            }
        };
        poolThread.start();
    }

    public void loadResource(File resource){
        resourceLoadQueue.add(resource);
    }

    private void loadResourceFromQueue () {
        try {
            while (true) {
                //Perform loading here.
                File resource = resourceLoadQueue.take();
                System.out.println("Resource to be loaded:" + resource);
                System.out.println("Passed to: " + Thread.currentThread());
                //TODO: LOAD, then publish that to the EventBus.
                publish(EngineEvent.RESOURCE_LOAD, "DATA: Loaded " + resource);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //resourcePool.put(key,value)
        System.out.println("Resource loaded from Asset Pool");
    }

    public void unloadResource(){
        //TODO: Implement this with care since objects could be referencing the data and relying it to exist.
    }

    @Override
    public void publish(EngineEvent e, Object data) {
        EngineEventObject eventObject = new EngineEventObject(EngineEvent.RESOURCE_LOAD, data);
        EventBus.notifyEventBus(eventObject);
    }
}
