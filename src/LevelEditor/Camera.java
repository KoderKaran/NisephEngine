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
                                final int[] srcImgData = ((DataBufferInt)spriteImageData.getRaster().
                                        getDataBuffer()).getData();

                                int xWorldDisplacementCamera = (int) Math.abs(cameraViewport.getX()
                                        - intersection.getX());
                                int yWorldDisplacementCamera = (int) Math.abs(cameraViewport.getY()
                                        - intersection.getY());
                                int xCameraPxStart = (int) Math.floor(xWorldDisplacementCamera * pixelsPerWorldUnit);
                                int xCameraPxEnd = (int) Math.floor((xWorldDisplacementCamera + intersection.getWidth())
                                        * pixelsPerWorldUnit);
                                int yCameraPxStart = (int) Math.floor(yWorldDisplacementCamera * pixelsPerWorldUnit);
                                int yCameraPxEnd = (int) Math.floor((yWorldDisplacementCamera + intersection.getHeight())
                                        * pixelsPerWorldUnit);
                                //---
                                int xWorldDisplacementSprite = (int) Math.abs(sprite.getSpriteBounds().getX()
                                        - intersection.getX());
                                int yWorldDisplacementSprite = (int) Math.abs(sprite.getSpriteBounds().getY()
                                        - intersection.getY());
                                int xSpritePxStart = (int) Math.floor(xWorldDisplacementSprite
                                        * (1d/sprite.getWorldToPxRatio()));
                                int xSpritePxEnd = (int) Math.floor((xWorldDisplacementSprite + intersection.getWidth())
                                        * (1d/sprite.getWorldToPxRatio()));
                                int ySpritePxStart = (int) Math.floor(yWorldDisplacementSprite
                                        * (1d/sprite.getWorldToPxRatio()));
                                int ySpritePxEnd = (int) Math.floor((yWorldDisplacementSprite + intersection.getHeight())
                                        * (1d/sprite.getWorldToPxRatio()));
                                //---
                                //-----
                                int interCameraPixelLen = (int) Math.floor(xCameraPxEnd - xCameraPxStart);
                                int interCameraPixelHeight = (int) Math.floor(yCameraPxEnd - yCameraPxStart);
                                int interSpritePixelLen = (int) Math.floor(xSpritePxEnd - xSpritePxStart);
                               // int interCameraPxHeight = (int) Math.floor(intersection.getHeight() * pixelsPerWorldUnit);
                               // int interSpritePxHeight = (int) Math.floor(intersection.getHeight()
                               //         * (1d/sprite.getWorldToPxRatio()));
                                //-----
                                double spritePxStepX = ((double) interSpritePixelLen) / interCameraPixelLen;
                                //double spritePxStepY = ((double) interSpritePxHeight) / interCameraPxHeight;

                                double spritePxX = ySpritePxStart * sprite.getWidth() + xSpritePxStart;
                                //double spritePxY = spritePxStepY * sprite.getHeight() + ySpritePxStart;
                                Viewport vp = sprite.getSpriteBounds();

                                double scaleFactor = ((double) interCameraPixelLen) / interSpritePixelLen ;
                                int remainingLengthBefore = xCameraPxStart;
                                int remainingLengthAfter = Math.abs(displayScreen.getWidth() - xCameraPxEnd);
                                int yOffset = yCameraPxStart * displayScreen.getWidth();
                                for(int y = ySpritePxStart; y < ySpritePxEnd; y++) {
                                    if(scaleFactor < 1 && y % 2 == 1) continue;
                                    for (int x = xSpritePxStart; x < xSpritePxEnd; x++) {
                                        int color = srcImgData[y * sprite.getWidth() + x];
                                        int baseIndex = (int) Math.floor(
                                                remainingLengthBefore
                                                + x * scaleFactor
                                                + y * remainingLengthAfter
                                                + y * remainingLengthBefore
                                                + y * displayScreen.getWidth()
                                                + y * interCameraPixelLen
                                                + yOffset);
                                        for(int j = 0; j < scaleFactor; j++){
                                            for(int k = 0; k < scaleFactor; k++){
                                                int someWidth = interCameraPixelLen
                                                        + remainingLengthAfter + remainingLengthBefore;
                                                int index = baseIndex +  j * someWidth + k;
                                                displayScreen.setPixel(color, index);
                                            }
                                        }
                                    }
                                }









                                /*
                                for(int y = yCameraPxStart; y < yCameraPxEnd; y++){
                                    for(int x = xCameraPxStart; x < xCameraPxEnd; x++){
                                        int color = srcImgData[(int) Math.floor(spritePxX)];
                                        displayScreen.setPixel(color, x, y);
                                        spritePxX = spritePxX + spritePxStepX;
                                        //System.out.println("Doing things");
                                    }


                                    //spritePxY = spritePxY + spritePxStepY;
                                    System.out.println(y);
                                }
/*

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
                                     */

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