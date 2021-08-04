package lrj.math;

import static lrj.util.StringToObject.stringToFloat;
import static lrj.util.Utility.parse;

public class Region3 implements Box3, Region {
    private static final long
        serialVersionUID = 1L;
    protected float
        x, y, z,
        w, h, d;
    
    public Region3() {
        // do nothing
    }
    
    public Region3(
        Box b
    ) {
        mSet(
            b.x(), b.y(), b.z(),
            b.w(), b.h(), b.d()
        );
    }
    
    public Region3(
        Vector whd
    ) {
        mSet(
            0f     , 0f     , 0f     ,
            whd.x(), whd.y(), whd.z()
        );
    }
    
    public Region3(
        Vector xyz,
        Vector whd
    ) {
        mSet(
            xyz.x(), xyz.y(), xyz.z(),
            whd.x(), whd.y(), whd.z()
        );
    }
    
    public Region3(
        float x, float y, float z, Vector whd
    ) {
        mSet(x, y, z, whd.x(), whd.y(), whd.z());
    }
    
    public Region3(
        Vector xyz, float w, float h, float d
    ) {
        mSet(xyz.x(), xyz.y(), xyz.z(), w, h, d);
    }
    
    public Region3(
        float w, float h, float d
    ) {
        mSet(0f, 0f, 0f, w, h, d);
    }
    
    public Region3(
        float x, float y, float z,
        float w, float h, float d
    ) {
        mSet(x, y, z, w, h, d);
    }
    
    protected void mSet(
        float x, float y, float z,
        float w, float h, float d
    ) {
        this.x = x; this.y = y; this.z = z;
        this.w = w; this.h = h; this.d = d;
    }
    
    @Override
    public float x() { return x; }
    @Override
    public float y() { return y; }
    @Override
    public float z() { return z; }
    
    @Override
    public float w() { return w; }
    @Override
    public float h() { return h; }
    @Override
    public float d() { return d; }
    
    @Override
    public Region3 copy() {
        return new Region3(this);
    }
    
    @Override
    public String toString() {
        return Region3.toString(this);
    }
    
    public static String toString(Box b) {
        return "[" +
            b.x() + ", " + b.y() + ", " + b.z() + ", " +
            b.w() + ", " + b.h() + ", " + b.d() + "]";
    }
    
    public static Region3 fromString(String s) {
        return Region3.fromString(new Region3(), s);
    }
    
    protected static <B extends Region3> B fromString(B b, String s) {
        if(b != null && s != null) {
            int
                i = s.indexOf("["),
                j = s.indexOf("]");
            if (i >= 0 || j >= 0) {
                if (j > i)
                    s = s.substring(++ i, j);
                else
                    s = s.substring(++ i   );
            }
    
            String[] t = parse(s, "x", "y", "z", "w", "h", "d");
            b.x = stringToFloat(t[0]);
            b.y = stringToFloat(t[1]);
            b.z = stringToFloat(t[2]);
            b.w = stringToFloat(t[3]);
            b.h = stringToFloat(t[4]);
            b.d = stringToFloat(t[5]);
        }
        return b;
    }
    
    public static class Mutable extends Region3 implements Region.Mutable {
        private static final long
            serialVersionUID = 1L;
        
        public Mutable() {
            super();
        }
        
        public Mutable(
            Box b
        ) {
            super(b);
        }
        
        public Mutable(
            Vector whd
        ) {
            super(whd);
        }
        
        public Mutable(
            Vector xyz,
            Vector whd
        ) {
            super(xyz, whd);
        }
        
        public Mutable(
            float x, float y, float z, Vector whd
        ) {
            super(x, y, z, whd);
        }
        
        public Mutable(
            Vector xyz, float w, float h, float d
        ) {
            super(xyz, w, h, d);
        }
        
        public Mutable(
            float w, float h, float d
        ) {
            super(w, h, d);
        }
        
        public Mutable(
            float x, float y, float z,
            float w, float h, float d
        ) {
            super(x, y, z, w, h, d);
        }
        
        @Override
        public Region3.Mutable x(float x) { this.x = x; return this; }
        @Override
        public Region3.Mutable y(float y) { this.y = y; return this; }
        @Override
        public Region3.Mutable z(float z) { this.z = z; return this; }
        
        @Override
        public Region3.Mutable w(float w) { this.w = w; return this; }
        @Override
        public Region3.Mutable h(float h) { this.h = h; return this; }
        @Override
        public Region3.Mutable d(float d) { this.d = d; return this; }
        
        public Region3.Mutable set(
            Box b
        ) {
            mSet(
                b.x(), b.y(), b.z(),
                b.w(), b.h(), b.d()
            );
            return this;
        }
        
        public Region3.Mutable set(
            Vector whd
        ) {
            mSet(
                0f     , 0f     , 0f     ,
                whd.x(), whd.y(), whd.z()
            );
            return this;
        }
        
        public Region3.Mutable set(
            Vector xyz,
            Vector whd
        ) {
            mSet(
                xyz.x(), xyz.y(), xyz.z(),
                whd.x(), whd.y(), whd.z()
            );
            return this;
        }
        
        public Region3.Mutable set(
            float x, float y, float z, Vector whd
        ) {
            mSet(x, y, z, whd.x(), whd.y(), whd.z());
            return this;
        }
        
        public Region3.Mutable set(
            Vector xyz, float w, float h, float d
        ) {
            mSet(xyz.x(), xyz.y(), xyz.z(), w, h, d);
            return this;
        }
        
        public Region3.Mutable set(
            float w, float h, float d
        ) {
            mSet(0f, 0f, 0f, w, h, d);
            return this;
        }
        
        public Region3.Mutable set(
            float x, float y, float z,
            float w, float h, float d
        ) {
            mSet(x, y, z, w, h, d);
            return this;
        }
        
        @Override
        public Region3.Mutable copy() {
            return new Region3.Mutable(this);
        }
        
        public static Region3.Mutable fromString(String s) {
            return Region3.fromString(new Region3.Mutable(), s);
        }
    }
}
