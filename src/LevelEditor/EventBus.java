package LevelEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import LevelEditor.EngineEventObject.EngineEvent;

public class EventBus {
    private static final Object SUB_LOCK = new Object(); // This is fine to be used as a lock, right?
    private static final HashMap<EngineEvent, ArrayList<Subscriber>> subsSortedByEvent = new HashMap<>();
    private static LinkedBlockingQueue<EngineEventObject> eventQueue
            = new LinkedBlockingQueue<EngineEventObject>();

    private static Thread eventNotificationThread = new Thread(){
        public void run(){
            try {
                while(true) {
                    EngineEventObject eventObject = eventQueue.take();
                    notifySubscribers(eventObject);
                    System.out.println("I notified! " + Thread.currentThread());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    static {
        // Generate all the possible event types.
        for (EngineEvent dir : EngineEvent.values()) {
            subsSortedByEvent.put(dir, new ArrayList<Subscriber>());
        }
        eventNotificationThread.start();
    }



    public static void notifyEventBus(EngineEventObject e) {
        eventQueue.add(e);
    }

    private static void notifySubscribers(EngineEventObject e) {
        synchronized (SUB_LOCK) {
            EngineEvent event = e.getEventType();
            for(Subscriber sub: subsSortedByEvent.get(event)) {
                sub.handleEvent(e);
            }
        }
    }

    public static void addSubscriber(Subscriber subscriber, EngineEvent e) {
        // Slightly blocking, but should be fine. If a problem invoke in worker thread.
        // Should only be executed at the init stage of any operation so that's my rational
        // for it not being a problem.
        synchronized (SUB_LOCK){
            ArrayList<Subscriber> eventSubs = subsSortedByEvent.get(e);
            // Only want subscriber to be able to subscribe once in list,
            // otherwise duplicate msgs will be sent to them.
            if(!eventSubs.contains(subscriber)){
                eventSubs.add(subscriber);
            }
        }
    }

    public static void removeSubscriber(Subscriber subscriber, EngineEvent e) {
        synchronized (SUB_LOCK) {
            ArrayList<Subscriber> eventSubs = subsSortedByEvent.get(e);
            // Make sure subscriber exists for event type.
            if (eventSubs.contains(subscriber)) {
                eventSubs.remove(subscriber);
            }
        }
    }
}
