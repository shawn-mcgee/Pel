package pel.old.scene;

public interface Renderable {
    
    public static interface Image extends Renderable {
        public void onRenderImage(Renderable.ImageContext context);
        
        public static final int
            A = 0xff000000, // 24
            R = 0x00ff0000, // 16
            G = 0x0000ff00, //  8
            B = 0x000000ff; //  0
    
        // get a component of argb
        public static int a(int argb) {
            return (argb & A) >> 24;
        }
        // get r component of argb
        public static int r(int argb) {
            return (argb & R) >> 16;
        }
        // get g component of argb
        public static int g(int argb) {
            return (argb & G) >>  8;
        }
        // get b component of argb
        public static int b(int argb) {
            return (argb & B);
        }
        
        // convert a component into argb
        public static int A(int a) {
            return (a & 0xff) << 24;
        }
        // convert r component into argb
        public static int R(int r) {
            return (r & 0xff) << 16;
        }
        // convert g component into argb
        public static int G(int g) {
            return (g & 0xff) <<  8;
        }
        // convert b component into argb
        public static int B(int b) {
            return (b & 0xff);
        }
    
        // set a component of argb
        public static int a(int argb, int a) {
            return (argb & ~A) | A(a);
        }
        // set r component of argb
        public static int r(int argb, int r) {
            return (argb & ~R) | R(r);
        }
        // set g component of argb
        public static int g(int argb, int g) {
            return (argb & ~G) | G(g);
        }
        // set b component of argb
        public static int b(int argb, int b) {
            return (argb & ~B) | B(b);
        }
    }
    
    public static interface Audio extends Renderable {
        public void onRenderAudio(Renderable.AudioContext context);
    }
    
    public static class RenderContext {
        public float
            t ,
            dt,
            fixed_dt;
    }
    
    public static class ImageContext extends RenderContext {
        public int[]
            buffer;
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
        
        public int argb(int x, int y) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _argb(i, j) : 0;
        }
        
        public int a(int x, int y) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _a(i, j) : 0;
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
    
        public int argb(int x, int y, int argb) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _argb(i, j, argb) : 0;
        }
    
        public int a(int x, int y, int a) {
            int
                i = x - this.x,
                j = y - this.y;
            return _check(i, j) ? _a(i, j, a) : 0;
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
        
        public int _argb(int i, int j) {
            return buffer[_at(i, j)];
        }
    
        public int _a(int i, int j) {
            return Image.a(buffer[_at(i, j)]);
        }
    
        public int _r(int i, int j) {
            return Image.r(buffer[_at(i, j)]);
        }
    
        public int _g(int i, int j) {
            return Image.g(buffer[_at(i, j)]);
        }
    
        public int _b(int i, int j) {
            return Image.b(buffer[_at(i, j)]);
        }
        
        public int _argb(int i, int j, int argb) {
            int at = _at(i, j);
            return buffer[at] = argb;
        }
    
        public int _a(int i, int j, int a) {
            int at = _at(i, j);
            return buffer[at] = Image.a(buffer[at], a);
        }
    
        public int _r(int i, int j, int r) {
            int at = _at(i, j);
            return buffer[at] = Image.r(buffer[at], r);
        }
    
        public int _g(int i, int j, int g) {
            int at = _at(i, j);
            return buffer[at] = Image.g(buffer[at], g);
        }
    
        public int _b(int i, int j, int b) {
            int at = _at(i, j);
            return buffer[at] = Image.b(buffer[at], b);
        }
    }
    
    public static class AudioContext extends RenderContext {
        public byte[]
            buffer;
        public int
            sample_rate,
            sample_size,
            channels;
    }
}
