package pel.math;

import pel.util.Copyable;

import java.io.Serializable;

public interface Box extends Copyable<Box>, Serializable {
    
    public default float x() { return 0f; }
    public default float y() { return 0f; }
    public default float z() { return 0f; }
    
    public default float w() { return 0f; }
    public default float h() { return 0f; }
    public default float d() { return 0f; }
    
    public default float x0() { return 0f; }
    public default float y0() { return 0f; }
    public default float z0() { return 0f; }
    
    public default float x1() { return 0f; }
    public default float y1() { return 0f; }
    public default float z1() { return 0f; }
    
    public default float x2() { return 0f; }
    public default float y2() { return 0f; }
    public default float z2() { return 0f; }
    
    public static interface Mutable extends Box {
        
        public default Mutable x(float x) { return this; }
        public default Mutable y(float y) { return this; }
        public default Mutable z(float z) { return this; }
    
        public default Mutable w(float w) { return this; }
        public default Mutable h(float h) { return this; }
        public default Mutable d(float d) { return this; }
    
        public default Mutable x0(float x0) { return this; }
        public default Mutable y0(float y0) { return this; }
        public default Mutable z0(float z0) { return this; }
    
        public default Mutable x1(float x1) { return this; }
        public default Mutable y1(float y1) { return this; }
        public default Mutable z1(float z1) { return this; }
    
        public default Mutable x2(float x2) { return this; }
        public default Mutable y2(float y2) { return this; }
        public default Mutable z2(float z2) { return this; }
    }
    
    public static boolean includes(Box2 a, Box2   b, boolean edge) {
        return includes(a, b.x0(), b.y0(), b.x1(), b.y1(), edge);
    }
    
    public static boolean includes(Box3 a, Box3   b, boolean edge) {
        return includes(a, b.x0(), b.y0(), b.z0(), b.x1(), b.y1(), b.z1(), edge);
    }
    
    public static boolean includes(Box2 a, Vector b, boolean edge) {
        return includes(a, b.x(), b.y(), b.x(), b.y(), edge);
    }
    
    public static boolean includes(Box3 a, Vector b, boolean edge) {
        return includes(a, b.x(), b.y(), b.z(), b.x(), b.y(), b.z(), edge);
    }
    
    public static boolean includes(Box2 a, float x, float y, boolean edge) {
        return includes(a, x, y, x, y, edge);
    }
    
    public static boolean includes(Box3 a, float x, float y, float z, boolean edge) {
        return includes(a, x, y, z, x, y, z, edge);
    }
    
    public static boolean includes(Box2 a, float x0, float y0, float x1, float y1, boolean edge) {
        if(edge)
            return
                a.x0() <= x0 && a.x1() >= x1 &&
                a.y0() <= y0 && a.y1() >= y1;
        else
            return
                a.x0() <  x0 && a.x1() >  x1 &&
                a.y0() <  y0 && a.y1() >  y1;
    }
    
    public static boolean includes(Box3 a, float x0, float y0, float z0, float x1, float y1, float z1, boolean edge) {
        if(edge)
            return
                a.x0() <= x0 && a.x1() >= x1 &&
                a.y0() <= y0 && a.y1() >= y1 &&
                a.z0() <= z0 && a.z1() >= z1;
        else
            return
                a.x0() <  x0 && a.x1() >  x1 &&
                a.y0() <  y0 && a.y1() >  y1 &&
                a.z0() <  z0 && a.z1() >  z1;
    }
    
    public static boolean excludes(Box2 a, Box2   b, boolean edge) {
        return excludes(a, b.x0(), b.y0(), b.x1(), b.y1(), edge);
    }
    
    public static boolean excludes(Box3 a, Box3   b, boolean edge) {
        return excludes(a, b.x0(), b.y0(), b.z0(), b.x1(), b.y1(), b.z1(), edge);
    }
    
    public static boolean excludes(Box2 a, Vector b, boolean edge) {
        return excludes(a, b.x(), b.y(), b.x(), b.y(), edge);
    }
    
    public static boolean excludes(Box3 a, Vector b, boolean edge) {
        return excludes(a, b.x(), b.y(), b.z(), b.x(), b.y(), b.z(), edge);
    }
    
    public static boolean excludes(Box2 a, float x, float y, boolean edge) {
        return excludes(a, x, y, x, y, edge);
    }
    
    public static boolean excludes(Box3 a, float x, float y, float z, boolean edge) {
        return excludes(a, x, y, z, x, y, z, edge);
    }
    
    public static boolean excludes(Box2 a, float x0, float y0, float x1, float y1, boolean edge) {
        if(edge)
            return
                a.x0() >= x1 || a.x1() <= x0 ||
                a.y0() >= y1 || a.y1() <= y0;
        else
            return
                a.x0() >  x1 || a.x1() <  x0 ||
                a.y0() >  y1 || a.y1() <  y0;
    }
    
    public static boolean excludes(Box3 a, float x0, float y0, float z0, float x1, float y1, float z1, boolean edge) {
        if(edge)
            return
                a.x0() >= x1 || a.x1() <= x0 ||
                a.y0() >= y1 || a.y1() <= y0 ||
                a.z0() >= z1 || a.z1() <= z0;
        else
            return
                a.x0() >  x1 || a.x1() <  x0 ||
                a.y0() >  y1 || a.y1() <  y0 ||
                a.z0() >  z1 || a.z1() <  z0;
    }
    
    public static boolean intersects(Box2 a, Box2 b, boolean edge) {
        return intersects(a, b.x0(), b.y0(), b.x1(), b.y1(), edge);
    }
    
    public static boolean intersects(Box3 a, Box3 b, boolean edge) {
        return intersects(a, b.x0(), b.y0(), b.z0(), b.x1(), b.y1(), b.z1(), edge);
    }
    
    public static boolean intersects(Box2 a, float x0, float y0, float x1, float y1, boolean edge) {
        if(edge)
            return
                a.x0() <= x1 && a.x1() >= x0 &&
                a.y0() <= y1 && a.y1() >= y0;
        else
            return
                a.x0() <  x1 && a.x1() >  x0 &&
                a.y0() <  y1 && a.y1() >  y0;
    }
    
    public static boolean intersects(Box3 a, float x0, float y0, float z0, float x1, float y1, float z1, boolean edge) {
        if(edge)
            return
                a.x0() <= x1 && a.x1() >= x0 &&
                a.y0() <= y1 && a.y1() >= y0 &&
                a.z0() <= z1 && a.z1() >= z0;
        else
            return
                a.x0() <  x1 && a.x1() >  x0 &&
                a.y0() <  y1 && a.y1() >  y0 &&
                a.z0() <  z1 && a.z1() >  z0;
    }
}
