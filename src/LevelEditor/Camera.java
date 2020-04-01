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
        this.pixelsPerWorldUnit = 1 / worldUnitsPerPixel;
        this.displayScreen = displayScreen;
        Transform parentTransform = parentGameObject.getTransform();
        double transformX = parentTransform.pos.getX();
        double transformY = parentTransform.pos.getY();
        cameraViewport = new Viewport(transformX,
                transformY, displayScreen.getWidth() * worldUnitsPerPixel,
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
                                ///temp todo: delete 2 statments below ////
                                double endConditonY = intersection.y + intersection.height;
                                double endConditonX = intersection.x + intersection.width;
                                for(double y = intersection.y; y <= intersection.y + intersection.height;
                                    y = y + worldUnitsPerPixel) {
                                    for(double x = intersection.x; x <= intersection.x + intersection.width;
                                        x = x + worldUnitsPerPixel) {
                                        //////THERE PROBABLY STILL IS A BUG IN HERE, TRYING TO FIGURE IT OUT.
                                        ///// Test by drag and dropping an image into the screen and seeing
                                        // how it handles it.
                                        /*
                                        System.out.println("--------");
                                        System.out.println("Starting point x: " + intersection.x);
                                        System.out.println("Starting point y: " + intersection.y);
                                        System.out.println("Current x: " + x);
                                        System.out.println("Current y: " + y);
                                        System.out.println("End condition x: " + endConditonX);
                                        System.out.println("End Conditon y: " + endConditonY);


                                         */
                                        double xSrcWorldDisplacement = Math.abs(sourceImageBounds.x + x);
                                        double ySrcWorldDisplacement = Math.abs(sourceImageBounds.y + y);
                                        int xSrcPixelDisplacement = (int) Math.floor(xSrcWorldDisplacement * pixelsPerWorldUnit);
                                        int ySrcPixelDisplacement = (int) Math.floor(ySrcWorldDisplacement * pixelsPerWorldUnit);
/*
                                        System.out.println("xSrcWorldDisplacement: " + xSrcWorldDisplacement);
                                        System.out.println("ySrcWorldDisplacement: " + ySrcWorldDisplacement);
                                        System.out.println("xSrcPixelDisplacement: " + xSrcPixelDisplacement);
                                        System.out.println("xSrcPixelDisplacement: " + ySrcPixelDisplacement);
                                        System.out.println("spriteImageData.getWidth(): " + spriteImageData.getWidth());
                                        System.out.println("###");

 */
                                        int srcPixel = imgData[ ySrcPixelDisplacement * spriteImageData.getWidth()
                                                + xSrcPixelDisplacement];

                                        double xDestWorldDisplacement = Math.abs(cameraViewport.viewport.x + x);
                                        double yDestWorldDisplacement = Math.abs(cameraViewport.viewport.y + y);
                                        int xDestPixelDisplacement = (int) Math.floor(xDestWorldDisplacement * pixelsPerWorldUnit);
                                        int yDestPixelDisplacement = (int) Math.floor(yDestWorldDisplacement * pixelsPerWorldUnit);
/*
                                        System.out.println("xDestWorldDisplacement: " + xDestWorldDisplacement);
                                        System.out.println("yDestWorldDisplacement: " + yDestWorldDisplacement);
                                        System.out.println("xDestPixelDisplacement: " + xDestPixelDisplacement);
                                        System.out.println("yDestPixelDisplacement: " + yDestPixelDisplacement);
                                        System.out.println("screen.getWidth(): " + displayScreen.getWidth());


 */
                                        displayScreen.setPixel(srcPixel, xDestPixelDisplacement, yDestPixelDisplacement);


                                    }
                                }
                            } finally {
                                imageLock.unlock();
                            }
                            //System.out.println("Screen drawn!");
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