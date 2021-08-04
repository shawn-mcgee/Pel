package lrj.math;

import static lrj.util.StringToObject.stringToFloat;
import static lrj.util.Utility.parse;

public class Vector3 implements Vector {
    private static final long
        serialVersionUID = 1L;
    protected float
        x, y, z;
    
    public Vector3() {
        // do nothing
    }
    
    public Vector3(Vector xyz) {
        x = xyz.x();
        y = xyz.y();
        z = xyz.z();
    }
    
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(Vector2 xy, float z) {
        this.x = xy.x();
        this.y = xy.y();
        this.z = z;
    }
    
    public Vector3(float x, Vector2 yz) {
        this.x = x;
        this.y = yz.x();
        this.z = yz.y();
    }
    
    @Override
    public float x() {
        return x;
    }
    
    @Override
    public float y() {
        return y;
    }
    
    @Override
    public float z() {
        return z;
    }
    
    @Override
    public final int n() {
        return 3;
    }
    
    @Override
    public Vector3 copy() {
        return new Vector3(this);
    }
    
    @Override
    public String toString() {
        return Vector3.toString(this);
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof Vector3 && Vector.equals(this, (Vector3) o, EPSILON);
    }
    
    public boolean equals(Vector2 b) {
        return Vector.equals(this, b, EPSILON);
    }
    
    public boolean equals(Vector3 b) {
        return Vector.equals(this, b, EPSILON);
    }
    
    public boolean equals(Vector4 b) {
        return Vector.equals(this, b, EPSILON);
    }
    
    public static String toString(Vector v) {
        return "<" + v.x() + ", " + v.y() + ", " + v.z() + ">";
    }
    
    public static Vector3 fromString(String s) {
        return Vector3.fromString(new Vector3(), s);
    }
    
    protected static <V extends Vector3> V fromString(V v, String s) {
        if(v != null && s != null) {
            int
                i = s.indexOf("<"),
                j = s.indexOf(">");
            if (i >= 0 || j >= 0) {
                if (j > i)
                    s = s.substring(++ i, j);
                else
                    s = s.substring(++ i   );
            }
            
            String[] t = parse(s, "x|r", "y|g", "z|b");
            v.z = stringToFloat(t[Z]);
            v.y = stringToFloat(t[Y]);
            v.x = stringToFloat(t[X]);
        }
        return v;
    }
    
    public static class Mutable extends Vector3 implements Vector.Mutable {
        private static final long
            serialVersionUID = 1L;
        
        public Mutable() {
            super();
        }
        
        public Mutable(Vector xyz) {
            super(xyz);
        }
        
        public Mutable(Vector2 xy, float z) {
            super(xy, z);
        }
        
        public Mutable(float x, Vector2 yz) {
            super(x, yz);
        }
        
        public Mutable(float x, float y, float z) {
            super(x, y, z);
        }
        
        @Override
        public Vector3.Mutable x(float x) {
            this.x = x;
            return this;
        }
        
        @Override
        public Vector3.Mutable y(float y) {
            this.y = y;
            return this;
        }
        
        @Override
        public Vector3.Mutable z(float z) {
            this.z = z;
            return this;
        }
        
        public Vector3.Mutable set(Vector xyz) {
            x = xyz.x();
            y = xyz.y();
            z = xyz.z();
            return this;
        }
        
        public Vector3.Mutable set(Vector2 xy, float z) {
            this.x = xy.x();
            this.y = xy.y();
            this.z = z;
            return this;
        }
        
        public Vector3.Mutable set(float x, Vector2 yz) {
            this.x = x;
            this.y = yz.x();
            this.z = yz.y();
            return this;
        }
        
        public Vector3.Mutable set(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }
        
        @Override
        public Vector3.Mutable copy() {
            return new Vector3.Mutable(this);
        }
        
        public static Vector3.Mutable fromString(String s) {
            return Vector3.fromString(new Vector3.Mutable(), s);
        }
    }
}
