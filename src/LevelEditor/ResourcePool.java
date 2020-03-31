package LevelEditor;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import LevelEditor.EngineEventObject.EngineEvent;
import LevelEditor.Resource.ResourceType;

public class ResourcePool implements Publisher { //

    HashMap<File, Resource> resourcePool;
    LinkedBlockingQueue<File> resourceLoadQueue;
    Thread poolThread;

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
                String resourceName = fileResource.getName();
                int dotIndex = resourceName.lastIndexOf('.');
                String fileExtension;
                if (dotIndex != -1) {
                    fileExtension = resourceName.substring(dotIndex + 1);
                } else {
                    throw new IllegalArgumentException("ResourcePool->loadResourceFromQueue: Invalid file name, " +
                            "no extension detected.");
                }
                Resource resource = null;
                ResourceType resourceType = getResourceType(fileExtension);
                switch (resourceType) {
                    case IMAGE:
                        resource = new ImageResource(fileResource);
                        break;
                    case AUDIO:
                        break;
                }
                if (resource != null) {
                    resourcePool.put(fileResource, resource);
                } else {
                    throw new NullPointerException("ResourcePool->loadResourceFromQueue: Resource type is null");
                }
                System.out.println("Resource Loaded Successfully:" + fileResource);
                System.out.println("Loaded from: " + Thread.currentThread());
                publish(EngineEvent.RESOURCE_LOAD, "DATA: Loaded " + fileResource);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                //e.printStackTrace();
            }
        }
    }

    private ResourceType getResourceType(String fileExtension){
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

    public void unloadResource(){
        //TODO: Implement this with care since objects could be referencing the data and relying it to exist.
    }

    @Override
    public void publish(EngineEvent e, Object data) {
        EngineEventObject eventObject = new EngineEventObject(EngineEvent.RESOURCE_LOAD, data);
        EventBus.notifyEventBus(eventObject);
    }
}
