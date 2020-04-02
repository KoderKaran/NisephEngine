package LevelEditor;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Camera implements EngineComponent {

    private Viewport cameraViewport;
    private GameObject parentGameObject;
    private double worldUnitsPerPixel;
    private double pixelsPerWorldUnit;
    private Screen displayScreen;

    public Camera(double worldUnitsPerPixel, Screen displayScreen, GameObject parentGameObject ){
        this.worldUnitsPerPixel = worldUnitsPerPixel;   // Example: "1/100" would mean for every
                                                        // 1 unit there is 100 pixels.
        this.pixelsPerWorldUnit = ((double) 1) / worldUnitsPerPixel;
        System.out.println(pixelsPerWorldUnit);
        this.displayScreen = displayScreen;
        Transform parentTransform = parentGameObject.getTransform();
        double transformX = parentTransform.pos.getX();
        double transformY = parentTransform.pos.getY();
        cameraViewport = new Viewport(transformX, transformY, displayScreen.getWidth() * worldUnitsPerPixel,
                displayScreen.getHeight() * worldUnitsPerPixel ); //TODO: Fix this.
        System.out.println("screenWidth: " + displayScreen.getWidth());   ///WHY IS THIS NOT THE CORRECT SCREEN SIZE???
        System.out.println("screenHeight: " + displayScreen.getHeight()); ///WHY IS THIS NOT THE CORRECT SCREEN SIZE???
        System.out.println(cameraViewport.viewport);
        this.parentGameObject = parentGameObject;
    }

    public void setViewport(){
        //TODO: Implement.
    }

    public Viewport getViewport(){
        return cameraViewport;
    }

    public void display(Level level){
        //TODO: Potentially do a check to see if camera is in the same level as level that is passed.
        final ReentrantLock lock = level.getObjectsInLevelLock();
        lock.lock();
        try {
            ArrayList<GameObject> objects = level.getObjectsInLevel();
            for(GameObject obj: objects){
                for(EngineComponent component: obj.getComponents()){
                    if(component.getType().equals(ComponentType.SPRITE)){
                        double tlX = component.getTransform().pos.getX();
                        double tlY = component.getTransform().pos.getY();
                        double width = ((Sprite) component).getImageData().getWidth() * worldUnitsPerPixel;
                        double height = ((Sprite) component).getImageData().getHeight() * worldUnitsPerPixel;
                        Rectangle2D.Double sourceImageBounds = new Rectangle2D.Double(tlX, tlY, width, height);
                        if(sourceImageBounds.intersects(cameraViewport.viewport)){
                            Rectangle2D.Double intersection
                                    = (Rectangle2D.Double) sourceImageBounds.createIntersection(cameraViewport.viewport);
                            final ReentrantLock imageLock = displayScreen.getImageLock();
                            imageLock.lock();
                            try{
                                BufferedImage spriteImageData = ((Sprite) component).getImageData();
                                final int[] imgData = ((DataBufferInt)spriteImageData.getRaster().
                                        getDataBuffer()).getData();

                                double xDestWorldDisplacement = Math.abs((cameraViewport.viewport.x - intersection.x));
                                double yDestWorldDisplacement = Math.abs((cameraViewport.viewport.y - intersection.y));
                                int xDestPxStart = (int) Math.floor(xDestWorldDisplacement * pixelsPerWorldUnit);
                                int xDestPxEnd = (int) Math.floor((xDestWorldDisplacement + intersection.width) * pixelsPerWorldUnit);
                                int yDestPxStart = (int) Math.floor(yDestWorldDisplacement * pixelsPerWorldUnit);
                                int yDestPxEnd = (int) Math.floor((yDestWorldDisplacement + intersection.height) * pixelsPerWorldUnit);

                                double xSrcWorldDisplacement = Math.abs((sourceImageBounds.x - intersection.x));
                                double ySrcWorldDisplacement = Math.abs((sourceImageBounds.y - intersection.y));
                                int xSrcPxStart = (int) Math.floor(xSrcWorldDisplacement * pixelsPerWorldUnit);
                                int ySrcPxStart = (int) Math.floor(ySrcWorldDisplacement * pixelsPerWorldUnit);

                                for(int yDest = yDestPxStart, ySrc = ySrcPxStart; yDest < yDestPxEnd; yDest++,ySrc++){
                                    for(int xDest = xDestPxStart, xSrc = xSrcPxStart; xDest < xDestPxEnd; xDest++,xSrc++){

                                        int srcPixel = imgData[ySrc * spriteImageData.getWidth() + xSrc];
                                        displayScreen.setPixel(srcPixel, xDest, yDest);
                                        //System.out.println("x: " + x);
                                        //System.out.println("y: " + y);
                                    }
                                }
                            } finally {
                                imageLock.unlock();
                            }
                        }
                    }
                }
            }
        } finally {
            lock.unlock();
        }
        //TODO: 1. Determine what's inside the viewport
        //TODO: 2. Render
        //TODO: 3. ???
        //TODO: 4. Profit
        //TODO: Implement. Def will not return void.
    }

    @Override
    public String getName() {
        return "Camera"; //idk ¯\_(ツ)_/¯
    }

    @Override
    public ComponentType getType() {
        return ComponentType.CAMERA;
    }

    @Override
    public GameObject getGameObject() {
        return parentGameObject;
    }

    @Override
    public Transform getTransform() {
        return parentGameObject.getTransform();
    }

    private class Viewport{

       //These parameters are in game units.
        public Rectangle2D.Double viewport;
        public Viewport(double tlX, double tlY, double width, double height) {
            viewport = new Rectangle2D.Double(tlX, tlY, width, height);
        }
    }
}