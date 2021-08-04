package pel.core;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Renderable {
    
    public static interface Image extends Renderable {
        public void onRenderImage(ImageContext context);
    
        public static final int
            R = 0x00ff0000, // 16
            G = 0x0000ff00, //  8
            B = 0x000000ff; //  0
        
        // get r component of rgb
        public static int r(int rgb) {
            return (rgb & R) >> 16;
        }
        // get g component of rgb
        public static int g(int rgb) {
            return (rgb & G) >>  8;
        }
        // get b component of rgb
        public static int b(int rgb) {
            return (rgb & B);
        }
        
        // convert r component into rgb
        public static int R(int r) {
            return (r & 0xff) << 16;
        }
        // convert g component into rgb
        public static int G(int g) {
            return (g & 0xff) <<  8;
        }
        // convert b component into rgb
        public static int B(int b) {
            return (b & 0xff);
        }
        
        // set r component of rgb
        public static int r(int rgb, int r) {
            return (rgb & ~R) | R(r);
        }
        // set g component of rgb
        public static int g(int rgb, int g) {
            return (rgb & ~G) | G(g);
        }
        // set b component of rgb
        public static int b(int rgb, int b) {
            return (rgb & ~B) | B(b);
        }
    }
    
    public static interface Audio extends Renderable {
        public void onRenderAudio(AudioContext context);
        
        public static byte clip(short x) {
            return x >= Byte.MIN_VALUE ? x <= Byte.MAX_VALUE ? (byte)x : Byte.MAX_VALUE : Byte.MIN_VALUE;
        }
        
        public static byte clip(int   x) {
            return x >= Byte.MIN_VALUE ? x <= Byte.MAX_VALUE ? (byte)x : Byte.MAX_VALUE : Byte.MIN_VALUE;
        }
        
        public static byte clip(long  x) {
            return x >= Byte.MIN_VALUE ? x <= Byte.MAX_VALUE ? (byte)x : Byte.MAX_VALUE : Byte.MIN_VALUE;
        }
        
        public static byte clip(float  x) {
            return x >= Byte.MIN_VALUE ? x <= Byte.MAX_VALUE ? (byte)x : Byte.MAX_VALUE : Byte.MIN_VALUE;
        }
    
        public static byte clip(double x) {
            return x >= Byte.MIN_VALUE ? x <= Byte.MAX_VALUE ? (byte)x : Byte.MAX_VALUE : Byte.MIN_VALUE;
        }
    }
    
    public static class RenderContext {
        public float
            t,
            dt,
            fixed_dt;
    }
    
    public static class ImageContext extends RenderContext {
        public BufferedImage
            image;
        public int[]
            image_buffer;
        public int
            x, y,
            w, h;
    
        public boolean check(int x, int y) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j);
        }
    
        public int at(int x, int y) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _at(i, j) : -1;
        }
    
        public int rgb(int x, int y) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _rgb(i, j) : 0;
        }
    
        public int r(int x, int y) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _r(i, j) : 0;
        }
    
        public int g(int x, int y) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _g(i, j) : 0;
        }
    
        public int b(int x, int y) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _b(i, j) : 0;
        }
    
        public int rgb(int x, int y, int rgb) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _rgb(i, j, rgb) : 0;
        }
    
        public int r(int x, int y, int r) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _r(i, j, r) : 0;
        }
    
        public int g(int x, int y, int g) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _g(i, j, g) : 0;
        }
    
        public int b(int x, int y, int b) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _b(i, j, b) : 0;
        }
    
        public boolean _check(int i, int j) {
            return
                i >= 0 && i < w &&
                    j >= 0 && j < h;
        }
    
        public int _at(int i, int j) {
            return w * j + i;
        }
    
        public int _rgb(int i, int j) {
            return image_buffer[_at(i, j)];
        }
    
        public int _r(int i, int j) {
            return Image.r(image_buffer[_at(i, j)]);
        }
    
        public int _g(int i, int j) {
            return Image.g(image_buffer[_at(i, j)]);
        }
    
        public int _b(int i, int j) {
            return Image.b(image_buffer[_at(i, j)]);
        }
    
        public int _rgb(int i, int j, int rgb) {
            int at = _at(i, j);
            return image_buffer[at] = rgb;
        }
    
        public int _r(int i, int j, int r) {
            int at = _at(i, j);
            return image_buffer[at] = Image.r(image_buffer[at], r);
        }
    
        public int _g(int i, int j, int g) {
            int at = _at(i, j);
            return image_buffer[at] = Image.g(image_buffer[at], g);
        }
    
        public int _b(int i, int j, int b) {
            int at = _at(i, j);
            return image_buffer[at] = Image.b(image_buffer[at], b);
        }
    }
    
    public static class AudioContext extends RenderContext {
        public byte[]
            audio_buffer;
        public int
            audio_sample_rate,
            audio_sample_size,
            audio_channels;
    }
}
