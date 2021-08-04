package lrj.math;

import static lrj.util.StringToObject.stringToFloat;
import static lrj.util.Utility.parse;

public class Vector2 implements Vector {
    private static final long
        serialVersionUID = 1L;
    protected float
        x, y;
    
    public Vector2() {
        // do nothing
    }
    
    public Vector2(Vector xy) {
        x = xy.x();
        y = xy.y();
    }
    
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
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
    public final int n() {
        return 2;
    }
    
    @Override
    public Vector2 copy() {
        return new Vector2(this);
    }
    
    @Override
    public String toString() {
        return Vector2.toString(this);
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof Vector2 && Vector.equals(this, (Vector2) o, EPSILON);
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
        return "<" + v.x() + ", " + v.y() + ">";
    }
    
    public static Vector2 fromString(String s) {
        return Vector2.fromString(new Vector2(), s);
    }
    
    protected static <V extends Vector2> V fromString(V v, String s) {
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
            
            String[] t = parse(s, "x|r", "y|g");
            v.y = stringToFloat(t[Y]);
            v.x = stringToFloat(t[X]);
        }
        return v;
    }
    
    public static class Mutable extends Vector2 implements Vector.Mutable {
        private static final long
            serialVersionUID = 1L;
        
        public Mutable() {
            super();
        }
        
        public Mutable(Vector xy) {
            super(xy);
        }
        
        public Mutable(float x, float y) {
            super(x, y);
        }
        
        @Override
        public Vector2.Mutable x(float x) {
            this.x = x;
            return this;
        }
        
        @Override
        public Vector2.Mutable y(float y) {
            this.y = y;
            return this;
        }
        
        public Vector2.Mutable set(Vector xy) {
            x = xy.x();
            y = xy.y();
            return this;
        }
        
        public Vector2.Mutable set(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }
        
        @Override
        public Vector2.Mutable copy() {
            return new Vector2.Mutable(this);
        }
        
        public static Vector2.Mutable fromString(String s) {
            return Vector2.fromString(new Vector2.Mutable(), s);
        }
    }
}
