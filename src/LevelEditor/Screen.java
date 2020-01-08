package LevelEditor;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Screen extends Canvas implements ComponentListener {

    private int pixels[];
    private Thread thread;
    private AtomicBoolean keepRendering = new AtomicBoolean(true);
    private ReentrantLock imageLock;
    private int screenWidth;
    private int screenHeight;
    private BufferedImage image;

    public Screen(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        imageLock = new ReentrantLock(true);
    }

    public void setPixel(int r, int g, int b, int x, int y) {
        int color = (r << 16) | (g << 8) | b;
        pixels[y * screenWidth + x] = color;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void stop() {
        if (thread != null) {
            System.out.println("Stopping...");
            keepRendering.set(false);
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Stopped");
        }
    }

    public void start() {
        if (thread != null) {
            stop();
        }

        keepRendering.set(true);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                createBufferStrategy(1); // We have to keep this at 1 if we want to make the tabs
                // for levels draggable out.
                do {
                    BufferStrategy bs = getBufferStrategy();
                    while (bs == null) {
                        bs = getBufferStrategy();
                    }
                    do {
                        do {
                            Graphics graphics = bs.getDrawGraphics();
                            Graphics g = bs.getDrawGraphics();
                            if(image == null && g == null) {
                                continue;
                            }
                            g.drawImage(image, 0, 0, null);
                            g.dispose();
                        } while (bs.contentsRestored());
                        bs.show();
                    } while (bs.contentsLost());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } while (keepRendering.get());
            }
        });
        thread.start();
    }

    public int[] getPixels(){
        return pixels;
    }

    public int getHeight(){
        return screenHeight;
    }

    public int getWidth(){
        return screenWidth;
    }

    public ReentrantLock getImageLock(){
        return imageLock;
    }
    @Override
    public void componentResized(ComponentEvent e) {
      imageLock.lock();
      try{
          int width = e.getComponent().getWidth();
          int height = e.getComponent().getHeight();
          float aspectRatio = 4 / 3f;
          if (width > height) {
              width = (int) (height * aspectRatio);
          } else if (height > width) {
              height = width;
          }
          if (width < 0 || height < 0) {
              width = 1;
              height = 1;
          }
          this.screenWidth = width;
          this.screenHeight = height;
          image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
          pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
      } finally{
          imageLock.unlock();
      }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Moved");
    }

    @Override
    public void componentShown(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Shown");
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Hidden");
    }
}
