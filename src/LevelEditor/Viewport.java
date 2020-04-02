package LevelEditor;

import java.awt.geom.Rectangle2D;

public class Viewport{

    public Rectangle2D.Double viewport;
    public Viewport(double tlX, double tlY, double width, double height) {
        viewport = new Rectangle2D.Double(tlX, tlY, width, height);
    }

    public Viewport(Rectangle2D.Double newViewport) {
        viewport = newViewport;
    }

    public double getX() {
        return viewport.x;
    }

    public double getY() {
        return viewport.y;
    }

    public double getWidth() {
        return viewport.width;
    }

    public double getHeight() {
        return viewport.height;
    }

    public void setX(double x) {
        viewport.x = x;
    }

    public void setY(double y) {
        viewport.y = y;
    }

    public void setWidth(double width) {
        viewport.width = width;
    }

    public void setHeight(double height) {
        viewport.height = height;
    }

    public boolean intersects(Rectangle2D r) {
        return viewport.intersects(r);
    }

    public Viewport createIntersection(Rectangle2D r){
        return new Viewport((Rectangle2D.Double) viewport.createIntersection(r));
    }




}