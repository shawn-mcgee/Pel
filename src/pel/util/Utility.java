package pel.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public final class Utility {
    
    private Utility() {
        // do nothing
    }
    
    public static <T> Class<T> typeOf(T t) {
        return Unsafe.cast(t.getClass());
    }
    
    public static <T> T copyOf(T t) {
        if (t instanceof Copyable) {
            Copyable<?> u = Unsafe.cast(t       );
            return          Unsafe.cast(u.copy());
        } else if (t != null)
            Debug.warn(new Object() { }, typeOf(t) + " is not of type jasper.util.Copyable");
        return t;
    }
    
    public static GraphicsDevice getGraphicsDevice(int i) {
        GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if(i >= 0 && i < gd.length)
            return gd[i];
        else if (gd.length > 0)
            return gd[0];
        else
            return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }
    
    public static BufferedImage createBufferedImage(int i, int w, int h) {
        return Utility.createBufferedImage(i, w, h, Transparency.BITMASK);
    }
    
    public static VolatileImage createVolatileImage(int i, int w, int h) {
        return Utility.createVolatileImage(i, w, h, Transparency.BITMASK);
    }
    
    public static BufferedImage createBufferedImage(int i, int w, int h, int t) {
        GraphicsDevice        gd = Utility.getGraphicsDevice(i);
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        return gc.createCompatibleImage(w, h, t);
    }
    
    public static VolatileImage createVolatileImage(int i, int w, int h, int t) {
        GraphicsDevice        gd = Utility.getGraphicsDevice(i);
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        return gc.createCompatibleVolatileImage(w, h, t);
    }
    
    public static String[] parse(String s, String... tags) {
        String[]
            // split s at commas, append " " for edge case when string ends with comma
            t  = (s.strip() + " ").split(","),
            u0 = new String[t.length], // split labels
            u1 = new String[t.length]; // split values
        int
            l = tags.length, // cached length
            m =    t.length; // cached length
        
        // split t into u0 and u1, and count labeled values
        int i, j;
        int a0 = 0, a1; // number of labeled and unlabeled value
        for(i = 0; i < m; i ++) {
            // split t[i] at colons, append " " for edge case when string ends with colon
            String[] u = (t[i] + " ").split(":");
            if(u.length > 1) { // a label is being used     (e.g. label:value)
                u0[i] = u[0].strip();
                u1[i] = u[1].strip();
                a0 ++;
            } else             // a label is not being used (e.g.       value)
                u1[i] = u[0].strip();
        }
        a1 = m - a0;
        
        t = new String[l]; // recycle t
        
        // split tags into alias array
        String[][] alias = new String[l][0];
        for(i = 0; i < l; i ++) {
            // split tags[i] at pipes, append " " for edge case when string ends with pipe
            String[] a = (tags[i] + " ").split("\\|");
            for(j = 0; j < a.length; j ++)
                a[j] = a[j].strip();
            alias[i] = a;
        }
        
        // find, move, and count matching labels
        int b0 = 0, b1; // number of matched and unmatched tags
        for(i = 0; i < l; i ++)
b:          for(j = 0; j < m; j ++) {
//                if (u0[j] != null && u0[j].equals(tags[i])) {
                if(u0[j] != null) {
                    for(String tag: alias[i])
                        if(u0[j].equals(tag)) {
                            t[i] = u1[j];
                            u0[j] = null;
                            u1[j] = null;
                            b0 ++;
                            continue b;
                        }
                }
            }
        b1 = l - b0;
        
        // compute final array size
        String[] u = new String[
            l + // number of tags
            Math.max(a1 - b1, 0) + // number of remaining unlabeled values
            Math.max(a0 - b0, 0)   // number of remaining   labeled values
        ];
        
        // fill empty slots with unlabeled values
        j = 0;
        for(i = 0; i < l; i ++)
            if(t[i] != null)
                u[i] = t[i];
            else
                for(; j < m; j ++)
                    if(u0[j] == null && u1[j] != null) {
                        u[i] = u1[j];
                        u0[j] = null;
                        u1[j] = null;
                        break;
                    }
        
        // fill empty slots with remaining values
        j = 0;
        for(i = l; i < u.length; i ++)
            if(u[i] == null)
                for(; j < m; j ++)
                    if(u1[j] != null) {
                        u[i] = u1[j];
                        u0[j] = null;
                        u1[j] = null;
                        break;
                    }
        
        return u;
    }
}
