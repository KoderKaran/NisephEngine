package LevelEditor;

import org.w3c.dom.css.Rect;

import java.awt.*;
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

    public Camera(double worldUnitsPerPixel, double viewPortWidth, double viewPortHeight, Screen displayScreen, GameObject parentGameObject ){
        this.worldUnitsPerPixel = worldUnitsPerPixel;   // Example: "1/100" would mean for every
                                                        // 1 unit there is 100 pixels.
        this.pixelsPerWorldUnit = ((double) 1) / worldUnitsPerPixel;
        this.displayScreen = displayScreen;
        Transform parentTransform = parentGameObject.getTransform();
        double transformX = parentTransform.pos.getX();
        double transformY = parentTransform.pos.getY();
        cameraViewport = new Viewport(transformX, transformY, displayScreen.getWidth() * worldUnitsPerPixel,
                displayScreen.getHeight() * worldUnitsPerPixel );
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
                        Sprite sprite = (Sprite) component;
                        Viewport srcViewport = sprite.getSpriteBounds();
                        if(srcViewport.intersects(cameraViewport.viewport)) {
                            Viewport intersection = srcViewport.createIntersection(cameraViewport.viewport);
                            final ReentrantLock imageLock = displayScreen.getImageLock();
                            imageLock.lock();
                            try{
                                BufferedImage spriteImageData = sprite.getImageData();
                                final int[] imgData = ((DataBufferInt)spriteImageData.getRaster().
                                        getDataBuffer()).getData();
                                double xDestWorldDisplacement = Math.abs((cameraViewport.viewport.x - intersection.getX()));
                                double yDestWorldDisplacement = Math.abs((cameraViewport.viewport.y - intersection.getY()));
                                int xDestPxStart = (int) Math.floor(xDestWorldDisplacement * pixelsPerWorldUnit);
                                int xDestPxEnd = (int) Math.floor((xDestWorldDisplacement + intersection.getWidth()) * pixelsPerWorldUnit);
                                int yDestPxStart = (int) Math.floor(yDestWorldDisplacement * pixelsPerWorldUnit);
                                int yDestPxEnd = (int) Math.floor((yDestWorldDisplacement + intersection.getHeight()) * pixelsPerWorldUnit);

                                double xSrcWorldDisplacement = Math.abs((srcViewport.getX() - intersection.getX()));
                                double ySrcWorldDisplacement = Math.abs((srcViewport.getY() - intersection.getY()));
                                int xSrcPxStart = (int) Math.floor(xSrcWorldDisplacement * (1/sprite.getWorldToPxRatio()));
                                int xSrcPxEnd = (int) Math.floor((xSrcWorldDisplacement + intersection.getWidth()) * (1/sprite.getWorldToPxRatio()));
                                int ySrcPxStart = (int) Math.floor(ySrcWorldDisplacement * (1/sprite.getWorldToPxRatio()));
                                int ySrcPxEnd = (int) Math.floor((ySrcWorldDisplacement + intersection.getHeight()) * (1/sprite.getWorldToPxRatio()));

                                System.out.println("xDestPxStart: " + xDestPxStart);
                                System.out.println("xDestPxEnd: " + xDestPxEnd);
                                System.out.println("yDestPxStart: " + yDestPxStart);
                                System.out.println("yDestPxEnd: " + yDestPxEnd);

                                double xSrcStep = 1;
                                double ySrcStep = 1;
                                double xDestStep = 1;
                                double yDestStep = 1;
                                // Zoom in = Src grows slower than Dest
                                // Zoom out = Src grows faster than Dest
                                for(double yDest = yDestPxStart, ySrc = ySrcPxStart; yDest < yDestPxEnd && ySrc < ySrcPxEnd;
                                    yDest = yDest + yDestStep, ySrc = ySrc + ySrcStep){
                                    for(double xDest = xDestPxStart, xSrc = xSrcPxStart; xDest < xDestPxEnd && xSrc < xSrcPxEnd;
                                        xDest = xDest + xDestStep, xSrc = xSrc + xSrcStep) {
                                        int xSrcPx = (int) Math.floor(xSrc);
                                        int ySrcPx = (int) Math.floor(ySrc);
                                        int srcPixel = imgData[ySrcPx * spriteImageData.getWidth() + xSrcPx];

                                        int xDestPx = (int) Math.floor(xDest);
                                        int yDestPx = (int) Math.floor(yDest);
                                        displayScreen.setPixel(srcPixel, xDestPx, yDestPx);
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
}