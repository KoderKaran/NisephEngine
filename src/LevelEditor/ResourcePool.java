package LevelEditor;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import LevelEditor.EngineEventObject.EngineEvent;
import LevelEditor.Resource.ResourceType;

public class ResourcePool implements Publisher { //

    private HashMap<File, Resource> resourcePool;   // Don't allow access to this from outer classes!
                                                    // Needs to be synced!
    private LinkedBlockingQueue<File> resourceLoadQueue;
    private final Object RESOURCE_POOL_LOCK = new Object();
    private Thread poolThread;

    public ResourcePool() {
        resourcePool = new HashMap<File, Resource>();
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
        while (true) {
            try {
                File fileResource = resourceLoadQueue.take();
                Resource resource = null;
                ResourceType resourceType = getResourceType(fileResource);
                switch (resourceType) {
                    case IMAGE:
                        resource = new ImageResource(fileResource);
                        break;
                    case AUDIO:
                        break;
                }
                if (resource != null) {
                    synchronized (RESOURCE_POOL_LOCK) {
                        resourcePool.put(fileResource, resource);
                    }
                } else {
                    throw new NullPointerException("ResourcePool->loadResourceFromQueue: Resource type is null");
                }
                System.out.println("Resource Loaded Successfully:" + fileResource);
                System.out.println("Loaded from: " + Thread.currentThread());
                publish(EngineEvent.RESOURCE_LOAD, resource);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                //e.printStackTrace();
            }
        }
    }

    public static ResourceType getResourceType(File file){ //Maybe move this method to a utility class eventually.
        String resourceName = file.getName();
        int dotIndex = resourceName.lastIndexOf('.');
        String fileExtension = "";
        if (dotIndex != -1) {
            fileExtension = resourceName.substring(dotIndex + 1);
        }
        fileExtension = fileExtension.toLowerCase();
        //// All supported formats for ImageIO.read()
        if(fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("gif") ||
                fileExtension.equals("bmp") || fileExtension.equals("wbmp")) {
            return ResourceType.IMAGE;
        } else if(fileExtension.equals("wav")){
            //TODO: Implement audio resource loading then determine actual supported formats.
            return ResourceType.AUDIO;
        } else {
            return null;
        }
    }

    public Resource getResource(File key){
        synchronized (RESOURCE_POOL_LOCK){
            return resourcePool.get(key);
        }
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
