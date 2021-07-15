package pel.old.scene;

import pel.util.Debug;
import pel.util.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.HashMap;

public class Sprite implements Renderable.Image {
    public static final int
        STOP = 0,
        PLAY = 1,
        LOOP = 2;
    protected int[][]
        frames;
    protected int
        x, y,
        w, h;
    protected boolean
        flip,
        flop;
    protected float
        frame,
        speed;
    protected int
        mode;
    
    public int at(int i, int j) {
        return j * w + i;
    }
    
    public void play() {
    
    }
    
    public void loop() {
    
    }
    
    public void stop() {
    
    }
    
    @Override
    public void onRenderImage(Renderable.ImageContext context) {
        int
            i1 = Math.max(x, context.x) - x,
            j1 = Math.max(y, context.y) - y,
            i2 = Math.min(x + w, context.x + context.w) - x,
            j2 = Math.min(y + h, context.y + context.h) - y;
        int[] buffer = frames[(int)frame];
        
        for(int i = i1; i < i2; i ++)
            for(int j = j1; j < j2; j ++)
                context.argb(i + x, j + y, buffer[at(i, j)]);
        
        switch(mode) {
            case STOP: break; // do nothing
            case PLAY:
                frame += speed * context.fixed_dt;
                if(frame < 0             ) { frame = 0            ; stop(); }
                if(frame >= frames.length) { frame = frames.length; stop(); }
                break;
            case LOOP:
                frame += speed * context.fixed_dt;
                if(frame < 0             ) frame = frames.length - frame;
                if(frame >= frames.length) frame = frame - frames.length;
                break;
        }
    }
    
    protected static final HashMap<String, Sprite>
        index = new HashMap<String, Sprite>();
    
    public static Sprite from(String    uid) {
        Sprite sprite = index.get(uid);
        if(sprite == null)
            Debug.warn(new Object() { }, "Failed to find indexed Sprite with uid '" + uid + "'");
        return Sprite.from(sprite);
    }
    
    public static Sprite from(Sprite sprite) {
        if(sprite != null) {
            Sprite copy = new Sprite();
            
            return copy;
        } else
            return null;
    }
    
    public static Sprite load(String uid, Resource resource, int w, int h) {
        Sprite.index(uid, resource, w, h);
        return Sprite.from(uid);
    }
    
    public static Sprite load(String uid, String   resource, int w, int h) {
        Sprite.index(uid, new Resource(resource), w, h);
        return Sprite.from(uid);
    }
    
    public static Sprite load(String uid, Class<?> from, String path, int w, int h) {
        Sprite.index(uid, new Resource(from, path), w, h);
        return Sprite.from(uid);
    }
    
    public static Sprite load(String uid, String   from, String path, int w, int h) {
        Sprite.index(uid, new Resource(from, path), w, h);
        return Sprite.from(uid);
    }
    
    public static void index(String uid, Resource resource, int w, int h) {
        try {
            if(index.get(uid) != null)
                Debug.warn(new Object() { }, "Sprite with uid '" + uid + "' already exists");
            BufferedImage image = ImageIO.read(resource.newInputStream());
            int
                _w = image.getWidth() ,
                _h = image.getHeight(),
                _n = (_w / w) * (_h / h);
            int[][] frames = new int[_n][w * h];
            for(int x = 0; x < w; x ++)
                for(int y = 0; y < h; y ++)
                    frames[h * y + x] = ((DataBufferInt)image.getSubimage(
                        x * w,
                        y * h,
                        w, h
                    ).getRaster().getDataBuffer()).getData();
            
            
        } catch (IOException ioe) {
            Debug.warn(new Object() { }, "Unable to index Sprite resource '" + resource + "'");
        }
    }
}
