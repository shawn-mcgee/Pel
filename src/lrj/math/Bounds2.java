package lrj.math;

import static lrj.util.StringToObject.stringToFloat;
import static lrj.util.Utility.parse;

public class Bounds2 implements Box2, Bounds {
    private static final long
        serialVersionUID = 1L;
    protected float
        x0, y0,
        x1, y1;
    
    public Bounds2() {
        // do nothing
    }
    
    public Bounds2(
        Box b
    ) {
        mSet(
            b.x0(), b.y0(),
            b.x1(), b.y1()
        );
    }
    
    public Bounds2(
        Vector xy0,
        Vector xy1
    ) {
        mSet(
            xy0.x(), xy0.y(),
            xy1.x(), xy1.y()
        );
    }
    
    public Bounds2(
        float x0, float y0, Vector xy1
    ) {
        mSet(x0, y0, xy1.x(), xy1.y());
    }
    
    public Bounds2(
        Vector xy0, float x1, float y1
    ) {
        mSet(xy0.x(), xy0.y(), x1, y1);
    }
    
    public Bounds2(
        float x0, float y0,
        float x1, float y1
    ) {
        mSet(x0, y0, x1, y1);
    }
    
    protected void mSet(
        float x0, float y0,
        float x1, float y1
    ) {
        if(x0 <= x1) {
            this.x0 = x0;
            this.x1 = x1;
        } else {
            this.x0 = x1;
            this.x1 = x0;
        }
    
        if(y0 <= y1) {
            this.y0 = y0;
            this.y1 = y1;
        } else {
            this.y0 = y1;
            this.y1 = y0;
        }
    }
    
    @Override
    public float x0() { return x0; }
    @Override
    public float y0() { return y0; }
    
    @Override
    public float x1() { return x1; }
    @Override
    public float y1() { return y1; }
    
    @Override
    public Bounds2 copy() {
        return new Bounds2(this);
    }
    
    @Override
    public String toString() {
        return Bounds2.toString(this);
    }
    
    public static String toString(Box b) {
        return "[" +
            b.x0() + ", " + b.y0() + ", " +
            b.x1() + ", " + b.y1() + "]";
    }
    
    public static Bounds2 fromString(String s) {
        return Bounds2.fromString(new Bounds2(), s);
    }
    
    protected static <B extends Bounds2> B fromString(B b, String s) {
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
    
            String[] t = parse(s, "x0", "y0", "x1", "y1");
            b.x0 = stringToFloat(t[0]);
            b.y0 = stringToFloat(t[1]);
            b.x1 = stringToFloat(t[2]);
            b.y1 = stringToFloat(t[3]);
        }
        return b;
    }
    
    public static class Mutable extends Bounds2 implements Bounds.Mutable {
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
            Vector xy0,
            Vector xy1
        ) {
            super(xy0, xy1);
        }
        
        public Mutable(
            float x0, float y0, Vector xy1
        ) {
            super(x0, y0, xy1);
        }
        
        public Mutable(
            Vector xy0, float x1, float y1
        ) {
            super(xy0, x1, y1);
        }
        
        public Mutable(
            float x0, float y0,
            float x1, float y1
        ) {
            super(x0, y0, x1, y1);
        }
        
        @Override
        public Bounds2.Mutable x0(float x0) {
            if(x0 <= x1)
                this.x0 = x0;
            else {
                this.x0 = x1;
                this.x1 = x0;
            }
            return this;
        }
    
        @Override
        public Bounds2.Mutable y0(float y0) {
            if(y0 <= y1)
                this.y0 = y0;
            else {
                this.y0 = y1;
                this.y1 = y0;
            }
            return this;
        }
    
        @Override
        public Bounds2.Mutable x1(float x1) {
            if(x1 >= x0)
                this.x1 = x1;
            else {
                this.x1 = x0;
                this.x0 = x1;
            }
            return this;
        }
    
        @Override
        public Bounds2.Mutable y1(float y1) {
            if(y1 >= y0)
                this.y1 = y1;
            else {
                this.y1 = y0;
                this.y0 = y1;
            }
            return this;
        }
        
        public Bounds2.Mutable set(
            Box b
        ) {
            mSet(
                b.x0(), b.y0(),
                b.x1(), b.y1()
            );
            return this;
        }
    
        public Bounds2.Mutable set(
            Vector xy0,
            Vector xy1
        ) {
            mSet(
                xy0.x(), xy0.y(),
                xy1.x(), xy1.y()
            );
            return this;
        }
        
        public Bounds2.Mutable set(
            float x0, float y0, Vector xy1
        ) {
            mSet(x0, y0, xy1.x(), xy1.y());
            return this;
        }
    
        public Bounds2.Mutable set(
            Vector xy0, float x1, float y1
        ) {
            mSet(xy0.x(), xy0.y(), x1, y1);
            return this;
        }
        
        public Bounds2.Mutable set(
            float x0, float y0,
            float x1, float y1
        ) {
            mSet(x0, y0, x1, y1);
            return this;
        }
    
        @Override
        public Bounds2.Mutable copy() {
            return new Bounds2.Mutable(this);
        }
        
        public static Bounds2.Mutable fromString(String s) {
            return Bounds2.fromString(new Bounds2.Mutable(), s);
        }
    }
}
