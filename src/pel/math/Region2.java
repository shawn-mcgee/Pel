package pel.math;

import static pel.util.StringToObject.stringToFloat;
import static pel.util.Utility.parse;

public class Region2 implements Box2, Region {
    private static final long
        serialVersionUID = 1L;
    protected float
        x, y,
        w, h;
    
    public Region2() {
        // do nothing
    }
    
    public Region2(
        Box b
    ) {
        mSet(
            b.x(), b.y(),
            b.w(), b.h()
        );
    }
    
    public Region2(
        Vector wh
    ) {
        mSet(
            0f    , 0f    ,
            wh.x(), wh.y()
        );
    }
    
    public Region2(
        Vector xy,
        Vector wh
    ) {
        mSet(
            xy.x(), xy.y(),
            wh.x(), wh.y()
        );
    }
    
    public Region2(
        float x, float y, Vector wh
    ) {
        mSet(x, y, wh.x(), wh.y());
    }
    
    public Region2(
        Vector xy, float w, float h
    ) {
        mSet(xy.x(), xy.y(), w, h);
    }
    
    public Region2(
        float w, float h
    ) {
        mSet(0f, 0f, w, h);
    }
    
    public Region2(
        float x, float y,
        float w, float h
    ) {
        mSet(x, y, w, h);
    }
    
    protected void mSet(
        float x, float y,
        float w, float h
    ) {
        this.x = x; this.y = y;
        this.w = w; this.h = h;
    }
    
    @Override
    public float x() { return x; }
    @Override
    public float y() { return y; }
    
    @Override
    public float w() { return w; }
    @Override
    public float h() { return h; }
    
    @Override
    public Region2 copy() {
        return new Region2(this);
    }
    
    @Override
    public String toString() {
        return Region2.toString(this);
    }
    
    public static String toString(Box b) {
        return "[" +
            b.x() + ", " + b.y() + ", " +
            b.w() + ", " + b.h() + "]";
    }
    
    public static Region2 fromString(String s) {
        return Region2.fromString(new Region2(), s);
    }
    
    protected static <B extends Region2> B fromString(B b, String s) {
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
        
            String[] t = parse(s, "x", "y", "w", "h");
            b.x = stringToFloat(t[0]);
            b.y = stringToFloat(t[1]);
            b.w = stringToFloat(t[2]);
            b.h = stringToFloat(t[3]);
        }
        return b;
    }
    
    public static class Mutable extends Region2 implements Region.Mutable {
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
            Vector wh
        ) {
            super(wh);
        }
        
        public Mutable(
            Vector xy,
            Vector wh
        ) {
            super(xy, wh);
        }
        
        public Mutable(
            float x, float y, Vector wh
        ) {
            super(x, y, wh);
        }
        
        public Mutable(
            Vector xy, float w, float h
        ) {
            super(xy, w, h);
        }
        
        public Mutable(
            float w, float h
        ) {
            super(w, h);
        }
        
        public Mutable(
            float x, float y,
            float w, float h
        ) {
            super(x, y, w, h);
        }
    
        @Override
        public Region2.Mutable x(float x) { this.x = x; return this; }
        @Override
        public Region2.Mutable y(float y) { this.y = y; return this; }
    
        @Override
        public Region2.Mutable w(float w) { this.w = w; return this; }
        @Override
        public Region2.Mutable h(float h) { this.h = h; return this; }
        
        public Region2.Mutable set(
            Box b
        ) {
            mSet(
                b.x(), b.y(),
                b.w(), b.h()
            );
            return this;
        }
        
        public Region2.Mutable set(
            Vector wh
        ) {
            mSet(
                0f    , 0f    ,
                wh.x(), wh.y()
            );
            return this;
        }
        
        public Region2.Mutable set(
            Vector xy,
            Vector wh
        ) {
            mSet(
                xy.x(), xy.y(),
                wh.x(), wh.y()
            );
            return this;
        }
        
        public Region2.Mutable set(
            float x, float y, Vector wh
        ) {
            mSet(x, y, wh.x(), wh.y());
            return this;
        }
    
        public Region2.Mutable set(
            Vector xy, float w, float h
        ) {
            mSet(xy.x(), xy.y(), w, h);
            return this;
        }
        
        public Region2.Mutable set(
            float w, float h
        ) {
            mSet(0f, 0f, w, h);
            return this;
        }
        
        public Region2.Mutable set(
            float x, float y,
            float w, float h
        ) {
            mSet(x, y, w, h);
            return this;
        }
    
        @Override
        public Region2.Mutable copy() {
            return new Region2.Mutable(this);
        }
        
        public static Region2.Mutable fromString(String s) {
            return Region2.fromString(new Region2.Mutable(), s);
        }
    }
}
