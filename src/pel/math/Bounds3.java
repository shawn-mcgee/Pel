package pel.math;

import static pel.util.StringToObject.stringToFloat;
import static pel.util.Utility.parse;

public class Bounds3 implements Box2, Bounds {
    private static final long
        serialVersionUID = 1L;
    protected float
        x0, y0, z0,
        x1, y1, z1;
    
    public Bounds3() {
        // do nothing
    }
    
    public Bounds3(
        Box b
    ) {
        mSet(
            b.x0(), b.y0(), b.z0(),
            b.x1(), b.y1(), b.z1()
        );
    }
    
    public Bounds3(
        Vector xyz0,
        Vector xyz1
    ) {
        mSet(
            xyz0.x(), xyz0.y(), xyz0.z(),
            xyz1.x(), xyz1.y(), xyz1.z()
        );
    }
    
    public Bounds3(
        float x0, float y0, float z0, Vector xyz1
    ) {
        mSet(x0, y0, z0, xyz1.x(), xyz1.y(), xyz1.z());
    }
    
    public Bounds3(
        Vector xyz0, float x1, float y1, float z1
    ) {
        mSet(xyz0.x(), xyz0.y(), xyz0.z(), x1, y1, z1);
    }
    
    public Bounds3(
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        mSet(x0, y0, z0, x1, y1, z1);
    }
    
    protected void mSet(
        float x0, float y0, float z0,
        float x1, float y1, float z1
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
    
        if(z0 <= z1) {
            this.z0 = z0;
            this.z1 = z1;
        } else {
            this.z0 = z1;
            this.z1 = z0;
        }
    }
    
    @Override
    public float x0() { return x0; }
    @Override
    public float y0() { return y0; }
    @Override
    public float z0() { return z0; }
    
    @Override
    public float x1() { return x1; }
    @Override
    public float y1() { return y1; }
    @Override
    public float z1() { return z1; }
    
    @Override
    public Bounds3 copy() {
        return new Bounds3(this);
    }
    
    @Override
    public String toString() {
        return Bounds3.toString(this);
    }
    
    public static String toString(Box b) {
        return "[" +
            b.x0() + ", " + b.y0() + ", " + b.z0() + ", " +
            b.x1() + ", " + b.y1() + ", " + b.z1() + "]";
    }
    
    public static Bounds3 fromString(String s) {
        return Bounds3.fromString(new Bounds3(), s);
    }
    
    protected static <B extends Bounds3> B fromString(B b, String s) {
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
    
            String[] t = parse(s, "x0", "y0", "z0", "x1", "y1", "z1");
            b.x0 = stringToFloat(t[0]);
            b.y0 = stringToFloat(t[1]);
            b.z0 = stringToFloat(t[2]);
            b.x1 = stringToFloat(t[2]);
            b.y1 = stringToFloat(t[3]);
            b.z1 = stringToFloat(t[6]);
        }
        return b;
    }
    
    public static class Mutable extends Bounds3 implements Bounds.Mutable {
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
            Vector xyz0,
            Vector xyz1
        ) {
            super(xyz0, xyz1);
        }
        
        public Mutable(
            float x0, float y0, float z0, Vector xyz1
        ) {
            super(x0, y0, z0, xyz1);
        }
        
        public Mutable(
            Vector xyz0, float x1, float y1, float z1
        ) {
            super(xyz0, x1, y1, z1);
        }
        
        public Mutable(
            float x0, float y0, float z0,
            float x1, float y1, float z1
        ) {
            super(x0, y0, z0, x1, y1, z1);
        }
        
        @Override
        public Bounds3.Mutable x0(float x0) {
            if(x0 <= x1)
                this.x0 = x0;
            else {
                this.x0 = x1;
                this.x1 = x0;
            }
            return this;
        }
    
        @Override
        public Bounds3.Mutable y0(float y0) {
            if(y0 <= y1)
                this.y0 = y0;
            else {
                this.y0 = y1;
                this.y1 = y0;
            }
            return this;
        }
    
        @Override
        public Bounds3.Mutable z0(float z0) {
            if(z0 <= z1)
                this.z0 = z0;
            else {
                this.z0 = z1;
                this.z1 = z0;
            }
            return this;
        }
    
        @Override
        public Bounds3.Mutable x1(float x1) {
            if(x1 >= x0)
                this.x1 = x1;
            else {
                this.x1 = x0;
                this.x0 = x1;
            }
            return this;
        }
    
        @Override
        public Bounds3.Mutable y1(float y1) {
            if(y1 >= y0)
                this.y1 = y1;
            else {
                this.y1 = y0;
                this.y0 = y1;
            }
            return this;
        }
    
        @Override
        public Bounds3.Mutable z1(float z1) {
            if(z1 >= z0)
                this.z1 = z1;
            else {
                this.z1 = z0;
                this.z0 = z1;
            }
            return this;
        }
        
        public Bounds3.Mutable set(
            Box b
        ) {
            mSet(
                b.x0(), b.y0(), b.z0(),
                b.x1(), b.y1(), b.z1()
            );
            return this;
        }
    
        public Bounds3.Mutable set(
            Vector xyz0,
            Vector xyz1
        ) {
            mSet(
                xyz0.x(), xyz0.y(), xyz0.z(),
                xyz1.x(), xyz1.y(), xyz1.z()
            );
            return this;
        }
        
        public Bounds3.Mutable set(
            float x0, float y0, float z0, Vector xyz1
        ) {
            mSet(x0, y0, z0, xyz1.x(), xyz1.y(), xyz1.z());
            return this;
        }
    
        public Bounds3.Mutable set(
            Vector xyz0, float x1, float y1, float z1
        ) {
            mSet(xyz0.x(), xyz0.y(), xyz0.z(), x1, y1, z1);
            return this;
        }
        
        public Bounds3.Mutable set(
            float x0, float y0, float z0,
            float x1, float y1, float z1
        ) {
            mSet(x0, y0, z0, x1, y1, z1);
            return this;
        }
    
        @Override
        public Bounds3.Mutable copy() {
            return new Bounds3.Mutable(this);
        }
        
        public static Bounds3.Mutable fromString(String s) {
            return Bounds3.fromString(new Bounds3.Mutable(), s);
        }
    }
}
