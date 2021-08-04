package lrj.math;

public interface Box2 extends Box {
    public default Vector2  xy() { return new Vector2( x(),  y()); }
    public default Vector2  wh() { return new Vector2( w(),  h()); }
    public default Vector2 xy0() { return new Vector2(x0(), y0()); }
    public default Vector2 xy1() { return new Vector2(x1(), y1()); }
    public default Vector2 xy2() { return new Vector2(x2(), y2()); }
    
    @Override
    public abstract Box2 copy();
    
    public default boolean includes(Box2   b, boolean edge) {
        return Box.includes(this, b, edge);
    }
    
    public default boolean includes(Vector b, boolean edge) {
        return Box.includes(this, b, edge);
    }
    
    public default boolean includes(float x, float y, boolean edge) {
        return Box.includes(this, x, y, edge);
    }
    
    public default boolean excludes(Box2   b, boolean edge) {
        return Box.excludes(this, b, edge);
    }
    
    public default boolean excludes(Vector b, boolean edge) {
        return Box.excludes(this, b, edge);
    }
    
    public default boolean excludes(float x, float y, boolean edge) {
        return Box.excludes(this, x, y, edge);
    }
    
    public default boolean intersects(Box2 b, boolean edge) {
        return Box.intersects(this, b, edge);
    }
    
    public static interface Mutable extends Box2, Box.Mutable {
        public default Box2.Mutable  xy(float  x, float  y) { x(x).y(y); return this; }
        public default Box2.Mutable  wh(float  w, float  h) { w(w).h(h); return this; }
        public default Box2.Mutable xy0(float x0, float y0) { x0(x0).y0(y0); return this; }
        public default Box2.Mutable xy1(float x1, float y1) { x1(x1).y1(y1); return this; }
        public default Box2.Mutable xy2(float x2, float y2) { x2(x2).y2(y2); return this; }
        
        @Override
        public abstract Box2.Mutable copy();
    }
}
