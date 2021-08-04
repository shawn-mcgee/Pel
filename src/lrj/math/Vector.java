package lrj.math;

import lrj.util.Copyable;

import java.io.Serializable;

public interface Vector extends Copyable<Vector>, Serializable {
    public static final float
        EPSILON = .001f;
    public static final int
        X = 0,
        Y = 1,
        Z = 2,
        W = 3;
    
    public default float x() { return 0f; }
    public default float y() { return 0f; }
    public default float z() { return 0f; }
    public default float w() { return 0f; }
    public default int n() { return 0; }
    
    public static interface Mutable extends Vector {
        public default Mutable x(float x) { return this; }
        public default Mutable y(float y) { return this; }
        public default Mutable z(float z) { return this; }
        public default Mutable w(float w) { return this; }
    }
    
    public static boolean equals(Vector2 a, Vector2 b, float e) {
        return equals(a.x(), a.y(), b.x(), b.y(), e);
    }
    
    public static boolean equals(Vector2 a, Vector3 b, float e) {
        return equals(a.x(), a.y(), 0f, b.x(), b.y(), b.z(), e);
    }
    
    public static boolean equals(Vector3 a, Vector2 b, float e) {
        return equals(a.x(), a.y(), a.z(), b.x(), b.y(), 0f, e);
    }
    
    public static boolean equals(Vector3 a, Vector3 b, float e) {
        return equals(a.x(), a.y(), a.z(), b.x(), b.y(), b.z(), e);
    }
    
    public static boolean equals(Vector2 a, Vector4 b, float e) {
        return equals(a.x(), a.y(), 0f, 0f, b.x(), b.y(), b.z(), b.w(), e);
    }
    
    public static boolean equals(Vector4 a, Vector2 b, float e) {
        return equals(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), 0f, 0f, e);
    }
    
    public static boolean equals(Vector3 a, Vector4 b, float e) {
        return equals(a.x(), a.y(), a.z(), 0f, b.x(), b.y(), b.z(), b.w(), e);
    }
    
    public static boolean equals(Vector4 a, Vector3 b, float e) {
        return equals(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), 0f, e);
    }
    
    public static boolean equals(Vector4 a, Vector4 b, float e) {
        return equals(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w(), e);
    }
    
    public static boolean equals(float x0, float y0, float x1, float y1, float e) {
        return
            (x0 >= x1 ? x0 - x1 : x1 - x0) < e &&
            (y0 >= y1 ? y0 - y1 : y1 - y0) < e;
    }
    
    public static boolean equals(float x0, float y0, float z0, float x1, float y1, float z1, float e) {
        return
            (x0 >= x1 ? x0 - x1 : x1 - x0) < e &&
            (y0 >= y1 ? y0 - y1 : y1 - y0) < e &&
            (z0 >= z1 ? z0 - z1 : z1 - z0) < e;
    }
    
    public static boolean equals(float x0, float y0, float z0, float w0, float x1, float y1, float z1, float w1, float e) {
        return
            (x0 >= x1 ? x0 - x1 : x1 - x0) < e &&
            (y0 >= y1 ? y0 - y1 : y1 - y0) < e &&
            (z0 >= z1 ? z0 - z1 : z1 - z0) < e &&
            (w0 >= w1 ? w0 - w1 : w1 - w0) < e;
    }
    
    public static Vector2 add(Vector2 a, Vector2 b) {
        return Vector.add(a.x(), a.y(), b.x(), b.y());
    }
    
    public static Vector3 add(Vector2 a, Vector3 b) {
        return Vector.add(a.x(), a.y(), 0f, b.x(), b.y(), b.z());
    }
    
    public static Vector3 add(Vector3 a, Vector2 b) {
        return Vector.add(a.x(), a.y(), a.z(), b.x(), b.y(), 0f);
    }
    
    public static Vector3 add(Vector3 a, Vector3 b) {
        return Vector.add(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }
    
    public static Vector4 add(Vector2 a, Vector4 b) {
        return Vector.add(a.x(), a.y(), 0f, 0f, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 add(Vector4 a, Vector2 b) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), 0f, 0f);
    }
    
    public static Vector4 add(Vector3 a, Vector4 b) {
        return Vector.add(a.x(), a.y(), a.z(), 0f, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 add(Vector4 a, Vector3 b) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), 0f);
    }
    
    public static Vector4 add(Vector4 a, Vector4 b) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2 add(Vector2 a, float b) {
        return Vector.add(a.x(), a.y(), b, b);
    }
    
    public static Vector2 add(float a, Vector2 b) {
        return Vector.add(a, a, b.x(), b.y());
    }
    
    public static Vector2 add(Vector2 a, float x1, float y1) {
        return Vector.add(a.x(), a.y(), x1, y1);
    }
    
    public static Vector2 add(float x0, float y0, Vector2 b) {
        return Vector.add(x0, y0, b.x(), b.y());
    }
    
    public static Vector3 add(Vector3 a, float b) {
        return Vector.add(a.x(), a.y(), a.z(), b, b, b);
    }
    
    public static Vector3 add(float a, Vector3 b) {
        return Vector.add(a, a, a, b.x(), b.y(), b.z());
    }
    
    public static Vector3 add(Vector3 a, float x1, float y1, float z1) {
        return Vector.add(a.x(), a.y(), a.z(), x1, y1, z1);
    }
    
    public static Vector3 add(float x0, float y0, float z0, Vector3 b) {
        return Vector.add(x0, y0, z0, b.x(), b.y(), b.z());
    }
    
    public static Vector4 add(Vector4 a, float b) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), b, b, b, b);
    }
    
    public static Vector4 add(float a, Vector4 b) {
        return Vector.add(a, a, a, a, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 add(Vector4 a, float x1, float y1, float z1, float w1) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1);
    }
    
    public static Vector4 add(float x0, float y0, float z0, float w0, Vector4 b) {
        return Vector.add(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2.Mutable add(Vector2 a, Vector2 b, Vector2.Mutable m) {
        return Vector.add(a.x(), a.y(), b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable add(Vector2 a, Vector3 b, Vector3.Mutable m) {
        return Vector.add(a.x(), a.y(), 0f, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector3.Mutable add(Vector3 a, Vector2 b, Vector3.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), b.x(), b.y(), 0f, m);
    }
    
    public static Vector3.Mutable add(Vector3 a, Vector3 b, Vector3.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable add(Vector2 a, Vector4 b, Vector4.Mutable m) {
        return Vector.add(a.x(), a.y(), 0f, 0f, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable add(Vector4 a, Vector2 b, Vector4.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), 0f, 0f, m);
    }
    
    public static Vector4.Mutable add(Vector3 a, Vector4 b, Vector4.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), 0f, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable add(Vector4 a, Vector3 b, Vector4.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), 0f, m);
    }
    
    public static Vector4.Mutable add(Vector4 a, Vector4 b, Vector4.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2.Mutable add(Vector2 a, float b, Vector2.Mutable m) {
        return Vector.add(a.x(), a.y(), b, b, m);
    }
    
    public static Vector2.Mutable add(float a, Vector2 b, Vector2.Mutable m) {
        return Vector.add(a, a, b.x(), b.y(), m);
    }
    
    public static Vector2.Mutable add(Vector2 a, float x1, float y1, Vector2.Mutable m) {
        return Vector.add(a.x(), a.y(), x1, y1, m);
    }
    
    public static Vector2.Mutable add(float x0, float y0, Vector2 b, Vector2.Mutable m) {
        return Vector.add(x0, y0, b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable add(Vector3 a, float b, Vector3.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), b, b, b, m);
    }
    
    public static Vector3.Mutable add(float a, Vector3 b, Vector3.Mutable m) {
        return Vector.add(a, a, a, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector3.Mutable add(Vector3 a, float x1, float y1, float z1, Vector3.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), x1, y1, z1, m);
    }
    
    public static Vector3.Mutable add(float x0, float y0, float z0, Vector3 b, Vector3.Mutable m) {
        return Vector.add(x0, y0, z0, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable add(Vector4 a, float b, Vector4.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), b, b, b, b, m);
    }
    
    public static Vector4.Mutable add(float a, Vector4 b, Vector4.Mutable m) {
        return Vector.add(a, a, a, a, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable add(Vector4 a, float x1, float y1, float z1, float w1, Vector4.Mutable m) {
        return Vector.add(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1, m);
    }
    
    public static Vector4.Mutable add(float x0, float y0, float z0, float w0, Vector4 b, Vector4.Mutable m) {
        return Vector.add(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2 add(
        float x0, float y0,
        float x1, float y1
    ) {
        return new Vector2(x0 + x1, y0 + y1);
    }
    
    public static Vector3 add(
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        return new Vector3(x0 + x1, y0 + y1, z0 + z1);
    }
    
    public static Vector4 add(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1
    ) {
        return new Vector4(x0 + x1, y0 + y1, z0 + z1, w0 + w1);
    }
    
    public static Vector2.Mutable add(
        float x0, float y0,
        float x1, float y1,
        Vector2.Mutable m
    ) {
        m.x(x0 + x1);
        m.y(y0 + y1);
        return m;
    }
    
    public static Vector3.Mutable add(
        float x0, float y0, float z0,
        float x1, float y1, float z1,
        Vector3.Mutable m
    ) {
        m.x(x0 + x1);
        m.y(y0 + y1);
        m.z(z0 + z1);
        return m;
    }
    
    public static Vector4.Mutable add(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1,
        Vector4.Mutable m
    ) {
        m.x(x0 + x1);
        m.y(y0 + y1);
        m.z(z0 + z1);
        m.w(w0 + w1);
        return m;
    }
    
    public static Vector2 sub(Vector2 a, Vector2 b) {
        return Vector.sub(a.x(), a.y(), b.x(), b.y());
    }
    
    public static Vector3 sub(Vector2 a, Vector3 b) {
        return Vector.sub(a.x(), a.y(), 0f, b.x(), b.y(), b.z());
    }
    
    public static Vector3 sub(Vector3 a, Vector2 b) {
        return Vector.sub(a.x(), a.y(), a.z(), b.x(), b.y(), 0f);
    }
    
    public static Vector3 sub(Vector3 a, Vector3 b) {
        return Vector.sub(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }
    
    public static Vector4 sub(Vector2 a, Vector4 b) {
        return Vector.sub(a.x(), a.y(), 0f, 0f, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 sub(Vector4 a, Vector2 b) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), 0f, 0f);
    }
    
    public static Vector4 sub(Vector3 a, Vector4 b) {
        return Vector.sub(a.x(), a.y(), a.z(), 0f, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 sub(Vector4 a, Vector3 b) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), 0f);
    }
    
    public static Vector4 sub(Vector4 a, Vector4 b) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2 sub(Vector2 a, float b) {
        return Vector.sub(a.x(), a.y(), b, b);
    }
    
    public static Vector2 sub(float a, Vector2 b) {
        return Vector.sub(a, a, b.x(), b.y());
    }
    
    public static Vector2 sub(Vector2 a, float x1, float y1) {
        return Vector.sub(a.x(), a.y(), x1, y1);
    }
    
    public static Vector2 sub(float x0, float y0, Vector2 b) {
        return Vector.sub(x0, y0, b.x(), b.y());
    }
    
    public static Vector3 sub(Vector3 a, float b) {
        return Vector.sub(a.x(), a.y(), a.z(), b, b, b);
    }
    
    public static Vector3 sub(float a, Vector3 b) {
        return Vector.sub(a, a, a, b.x(), b.y(), b.z());
    }
    
    public static Vector3 sub(Vector3 a, float x1, float y1, float z1) {
        return Vector.sub(a.x(), a.y(), a.z(), x1, y1, z1);
    }
    
    public static Vector3 sub(float x0, float y0, float z0, Vector3 b) {
        return Vector.sub(x0, y0, z0, b.x(), b.y(), b.z());
    }
    
    public static Vector4 sub(Vector4 a, float b) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), b, b, b, b);
    }
    
    public static Vector4 sub(float a, Vector4 b) {
        return Vector.sub(a, a, a, a, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 sub(Vector4 a, float x1, float y1, float z1, float w1) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1);
    }
    
    public static Vector4 sub(float x0, float y0, float z0, float w0, Vector4 b) {
        return Vector.sub(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2.Mutable sub(Vector2 a, Vector2 b, Vector2.Mutable m) {
        return Vector.sub(a.x(), a.y(), b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable sub(Vector2 a, Vector3 b, Vector3.Mutable m) {
        return Vector.sub(a.x(), a.y(), 0f, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector3.Mutable sub(Vector3 a, Vector2 b, Vector3.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), b.x(), b.y(), 0f, m);
    }
    
    public static Vector3.Mutable sub(Vector3 a, Vector3 b, Vector3.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable sub(Vector2 a, Vector4 b, Vector4.Mutable m) {
        return Vector.sub(a.x(), a.y(), 0f, 0f, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable sub(Vector4 a, Vector2 b, Vector4.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), 0f, 0f, m);
    }
    
    public static Vector4.Mutable sub(Vector3 a, Vector4 b, Vector4.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), 0f, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable sub(Vector4 a, Vector3 b, Vector4.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), 0f, m);
    }
    
    public static Vector4.Mutable sub(Vector4 a, Vector4 b, Vector4.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2.Mutable sub(Vector2 a, float b, Vector2.Mutable m) {
        return Vector.sub(a.x(), a.y(), b, b, m);
    }
    
    public static Vector2.Mutable sub(float a, Vector2 b, Vector2.Mutable m) {
        return Vector.sub(a, a, b.x(), b.y(), m);
    }
    
    public static Vector2.Mutable sub(Vector2 a, float x1, float y1, Vector2.Mutable m) {
        return Vector.sub(a.x(), a.y(), x1, y1, m);
    }
    
    public static Vector2.Mutable sub(float x0, float y0, Vector2 b, Vector2.Mutable m) {
        return Vector.sub(x0, y0, b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable sub(Vector3 a, float b, Vector3.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), b, b, b, m);
    }
    
    public static Vector3.Mutable sub(float a, Vector3 b, Vector3.Mutable m) {
        return Vector.sub(a, a, a, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector3.Mutable sub(Vector3 a, float x1, float y1, float z1, Vector3.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), x1, y1, z1, m);
    }
    
    public static Vector3.Mutable sub(float x0, float y0, float z0, Vector3 b, Vector3.Mutable m) {
        return Vector.sub(x0, y0, z0, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable sub(Vector4 a, float b, Vector4.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), b, b, b, b, m);
    }
    
    public static Vector4.Mutable sub(float a, Vector4 b, Vector4.Mutable m) {
        return Vector.sub(a, a, a, a, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable sub(Vector4 a, float x1, float y1, float z1, float w1, Vector4.Mutable m) {
        return Vector.sub(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1, m);
    }
    
    public static Vector4.Mutable sub(float x0, float y0, float z0, float w0, Vector4 b, Vector4.Mutable m) {
        return Vector.sub(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2 sub(
        float x0, float y0,
        float x1, float y1
    ) {
        return new Vector2(x0 - x1, y0 - y1);
    }
    
    public static Vector3 sub(
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        return new Vector3(x0 - x1, y0 - y1, z0 - z1);
    }
    
    public static Vector4 sub(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1
    ) {
        return new Vector4(x0 - x1, y0 - y1, z0 - z1, w0 - w1);
    }
    
    public static Vector2.Mutable sub(
        float x0, float y0,
        float x1, float y1,
        Vector2.Mutable m
    ) {
        m.x(x0 - x1);
        m.y(y0 - y1);
        return m;
    }
    
    public static Vector3.Mutable sub(
        float x0, float y0, float z0,
        float x1, float y1, float z1,
        Vector3.Mutable m
    ) {
        m.x(x0 - x1);
        m.y(y0 - y1);
        m.z(z0 - z1);
        return m;
    }
    
    public static Vector4.Mutable sub(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1,
        Vector4.Mutable m
    ) {
        m.x(x0 - x1);
        m.y(y0 - y1);
        m.z(z0 - z1);
        m.w(w0 - w1);
        return m;
    }
    
    public static Vector2 mul(Vector2 a, Vector2 b) {
        return Vector.mul(a.x(), a.y(), b.x(), b.y());
    }
    
    public static Vector3 mul(Vector3 a, Vector3 b) {
        return Vector.mul(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }
    
    public static Vector4 mul(Vector4 a, Vector4 b) {
        return Vector.mul(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2 mul(Vector2 a, float b) {
        return Vector.mul(a.x(), a.y(), b, b);
    }
    
    public static Vector2 mul(float a, Vector2 b) {
        return Vector.mul(a, a, b.x(), b.y());
    }
    
    public static Vector2 mul(Vector2 a, float x1, float y1) {
        return Vector.mul(a.x(), a.y(), x1, y1);
    }
    
    public static Vector2 mul(float x0, float y0, Vector2 b) {
        return Vector.mul(x0, y0, b.x(), b.y());
    }
    
    public static Vector3 mul(Vector3 a, float b) {
        return Vector.mul(a.x(), a.y(), a.z(), b, b, b);
    }
    
    public static Vector3 mul(float a, Vector3 b) {
        return Vector.mul(a, a, a, b.x(), b.y(), b.z());
    }
    
    public static Vector3 mul(Vector3 a, float x1, float y1, float z1) {
        return Vector.mul(a.x(), a.y(), a.z(), x1, y1, z1);
    }
    
    public static Vector3 mul(float x0, float y0, float z0, Vector3 b) {
        return Vector.mul(x0, y0, z0, b.x(), b.y(), b.z());
    }
    
    public static Vector4 mul(Vector4 a, float b) {
        return Vector.mul(a.x(), a.y(), a.z(), a.w(), b, b, b, b);
    }
    
    public static Vector4 mul(float a, Vector4 b) {
        return Vector.mul(a, a, a, a, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 mul(Vector4 a, float x1, float y1, float z1, float w1) {
        return Vector.mul(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1);
    }
    
    public static Vector4 mul(float x0, float y0, float z0, float w0, Vector4 b) {
        return Vector.mul(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2.Mutable mul(Vector2 a, Vector2 b, Vector2.Mutable m) {
        return Vector.mul(a.x(), a.y(), b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable mul(Vector3 a, Vector3 b, Vector3.Mutable m) {
        return Vector.mul(a.x(), a.y(), a.z(), b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable mul(Vector4 a, Vector4 b, Vector4.Mutable m) {
        return Vector.mul(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2.Mutable mul(Vector2 a, float b, Vector2.Mutable m) {
        return Vector.mul(a.x(), a.y(), b, b, m);
    }
    
    public static Vector2.Mutable mul(float a, Vector2 b, Vector2.Mutable m) {
        return Vector.mul(a, a, b.x(), b.y(), m);
    }
    
    public static Vector2.Mutable mul(Vector2 a, float x1, float y1, Vector2.Mutable m) {
        return Vector.mul(a.x(), a.y(), x1, y1, m);
    }
    
    public static Vector2.Mutable mul(float x0, float y0, Vector2 b, Vector2.Mutable m) {
        return Vector.mul(x0, y0, b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable mul(Vector3 a, float b, Vector3.Mutable m) {
        return Vector.mul(a.x(), a.y(), a.z(), b, b, b, m);
    }
    
    public static Vector3.Mutable mul(float a, Vector3 b, Vector3.Mutable m) {
        return Vector.mul(a, a, a, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector3.Mutable mul(Vector3 a, float x1, float y1, float z1, Vector3.Mutable m) {
        return Vector.mul(a.x(), a.y(), a.z(), x1, y1, z1, m);
    }
    
    public static Vector3.Mutable mul(float x0, float y0, float z0, Vector3 b, Vector3.Mutable m) {
        return Vector.mul(x0, y0, z0, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable mul(Vector4 a, float b, Vector4.Mutable m) {
        return Vector.mul(a.x(), a.y(), a.z(), a.w(), b, b, b, b, m);
    }
    
    public static Vector4.Mutable mul(float a, Vector4 b, Vector4.Mutable m) {
        return Vector.mul(a, a, a, a, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable mul(Vector4 a, float x1, float y1, float z1, float w1, Vector4.Mutable m) {
        return Vector.mul(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1, m);
    }
    
    public static Vector4.Mutable mul(float x0, float y0, float z0, float w0, Vector4 b, Vector4.Mutable m) {
        return Vector.mul(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2 mul(
        float x0, float y0,
        float x1, float y1
    ) {
        return new Vector2(x0 * x1, y0 * y1);
    }
    
    public static Vector3 mul(
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        return new Vector3(x0 * x1, y0 * y1, z0 * z1);
    }
    
    public static Vector4 mul(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1
    ) {
        return new Vector4(x0 * x1, y0 * y1, z0 * z1, w0 * w1);
    }
    
    public static Vector2.Mutable mul(
        float x0, float y0,
        float x1, float y1,
        Vector2.Mutable m
    ) {
        m.x(x0 * x1);
        m.y(y0 * y1);
        return m;
    }
    
    public static Vector3.Mutable mul(
        float x0, float y0, float z0,
        float x1, float y1, float z1,
        Vector3.Mutable m
    ) {
        m.x(x0 * x1);
        m.y(y0 * y1);
        m.z(z0 * z1);
        return m;
    }
    
    public static Vector4.Mutable mul(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1,
        Vector4.Mutable m
    ) {
        m.x(x0 * x1);
        m.y(y0 * y1);
        m.z(z0 * z1);
        m.w(w0 * w1);
        return m;
    }
    
    public static Vector2 div(Vector2 a, Vector2 b) {
        return Vector.div(a.x(), a.y(), b.x(), b.y());
    }
    
    public static Vector3 div(Vector3 a, Vector3 b) {
        return Vector.div(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }
    
    public static Vector4 div(Vector4 a, Vector4 b) {
        return Vector.div(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2 div(Vector2 a, float b) {
        return Vector.div(a.x(), a.y(), b, b);
    }
    
    public static Vector2 div(float a, Vector2 b) {
        return Vector.div(a, a, b.x(), b.y());
    }
    
    public static Vector2 div(Vector2 a, float x1, float y1) {
        return Vector.div(a.x(), a.y(), x1, y1);
    }
    
    public static Vector2 div(float x0, float y0, Vector2 b) {
        return Vector.div(x0, y0, b.x(), b.y());
    }
    
    public static Vector3 div(Vector3 a, float b) {
        return Vector.div(a.x(), a.y(), a.z(), b, b, b);
    }
    
    public static Vector3 div(float a, Vector3 b) {
        return Vector.div(a, a, a, b.x(), b.y(), b.z());
    }
    
    public static Vector3 div(Vector3 a, float x1, float y1, float z1) {
        return Vector.div(a.x(), a.y(), a.z(), x1, y1, z1);
    }
    
    public static Vector3 div(float x0, float y0, float z0, Vector3 b) {
        return Vector.div(x0, y0, z0, b.x(), b.y(), b.z());
    }
    
    public static Vector4 div(Vector4 a, float b) {
        return Vector.div(a.x(), a.y(), a.z(), a.w(), b, b, b, b);
    }
    
    public static Vector4 div(float a, Vector4 b) {
        return Vector.div(a, a, a, a, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 div(Vector4 a, float x1, float y1, float z1, float w1) {
        return Vector.div(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1);
    }
    
    public static Vector4 div(float x0, float y0, float z0, float w0, Vector4 b) {
        return Vector.div(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2.Mutable div(Vector2 a, Vector2 b, Vector2.Mutable m) {
        return Vector.div(a.x(), a.y(), b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable div(Vector3 a, Vector3 b, Vector3.Mutable m) {
        return Vector.div(a.x(), a.y(), a.z(), b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable div(Vector4 a, Vector4 b, Vector4.Mutable m) {
        return Vector.div(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2.Mutable div(Vector2 a, float b, Vector2.Mutable m) {
        return Vector.div(a.x(), a.y(), b, b, m);
    }
    
    public static Vector2.Mutable div(float a, Vector2 b, Vector2.Mutable m) {
        return Vector.div(a, a, b.x(), b.y(), m);
    }
    
    public static Vector2.Mutable div(Vector2 a, float x1, float y1, Vector2.Mutable m) {
        return Vector.div(a.x(), a.y(), x1, y1, m);
    }
    
    public static Vector2.Mutable div(float x0, float y0, Vector2 b, Vector2.Mutable m) {
        return Vector.div(x0, y0, b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable div(Vector3 a, float b, Vector3.Mutable m) {
        return Vector.div(a.x(), a.y(), a.z(), b, b, b, m);
    }
    
    public static Vector3.Mutable div(float a, Vector3 b, Vector3.Mutable m) {
        return Vector.div(a, a, a, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector3.Mutable div(Vector3 a, float x1, float y1, float z1, Vector3.Mutable m) {
        return Vector.div(a.x(), a.y(), a.z(), x1, y1, z1, m);
    }
    
    public static Vector3.Mutable div(float x0, float y0, float z0, Vector3 b, Vector3.Mutable m) {
        return Vector.div(x0, y0, z0, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable div(Vector4 a, float b, Vector4.Mutable m) {
        return Vector.div(a.x(), a.y(), a.z(), a.w(), b, b, b, b, m);
    }
    
    public static Vector4.Mutable div(float a, Vector4 b, Vector4.Mutable m) {
        return Vector.div(a, a, a, a, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable div(Vector4 a, float x1, float y1, float z1, float w1, Vector4.Mutable m) {
        return Vector.div(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1, m);
    }
    
    public static Vector4.Mutable div(float x0, float y0, float z0, float w0, Vector4 b, Vector4.Mutable m) {
        return Vector.div(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2 div(
        float x0, float y0,
        float x1, float y1
    ) {
        return new Vector2(x0 / x1, y0 / y1);
    }
    
    public static Vector3 div(
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        return new Vector3(x0 / x1, y0 / y1, z0 / z1);
    }
    
    public static Vector4 div(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1
    ) {
        return new Vector4(x0 / x1, y0 / y1, z0 / z1, w0 / w1);
    }
    
    public static Vector2.Mutable div(
        float x0, float y0,
        float x1, float y1,
        Vector2.Mutable m
    ) {
        m.x(x0 / x1);
        m.y(y0 / y1);
        return m;
    }
    
    public static Vector3.Mutable div(
        float x0, float y0, float z0,
        float x1, float y1, float z1,
        Vector3.Mutable m
    ) {
        m.x(x0 / x1);
        m.y(y0 / y1);
        m.z(z0 / z1);
        return m;
    }
    
    public static Vector4.Mutable div(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1,
        Vector4.Mutable m
    ) {
        m.x(x0 / x1);
        m.y(y0 / y1);
        m.z(z0 / z1);
        m.w(w0 / w1);
        return m;
    }
    
    public static Vector2 mod(Vector2 a, Vector2 b) {
        return Vector.mod(a.x(), a.y(), b.x(), b.y());
    }
    
    public static Vector3 mod(Vector3 a, Vector3 b) {
        return Vector.mod(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }
    
    public static Vector4 mod(Vector4 a, Vector4 b) {
        return Vector.mod(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2 mod(Vector2 a, float b) {
        return Vector.mod(a.x(), a.y(), b, b);
    }
    
    public static Vector2 mod(float a, Vector2 b) {
        return Vector.mod(a, a, b.x(), b.y());
    }
    
    public static Vector2 mod(Vector2 a, float x1, float y1) {
        return Vector.mod(a.x(), a.y(), x1, y1);
    }
    
    public static Vector2 mod(float x0, float y0, Vector2 b) {
        return Vector.mod(x0, y0, b.x(), b.y());
    }
    
    public static Vector3 mod(Vector3 a, float b) {
        return Vector.mod(a.x(), a.y(), a.z(), b, b, b);
    }
    
    public static Vector3 mod(float a, Vector3 b) {
        return Vector.mod(a, a, a, b.x(), b.y(), b.z());
    }
    
    public static Vector3 mod(Vector3 a, float x1, float y1, float z1) {
        return Vector.mod(a.x(), a.y(), a.z(), x1, y1, z1);
    }
    
    public static Vector3 mod(float x0, float y0, float z0, Vector3 b) {
        return Vector.mod(x0, y0, z0, b.x(), b.y(), b.z());
    }
    
    public static Vector4 mod(Vector4 a, float b) {
        return Vector.mod(a.x(), a.y(), a.z(), a.w(), b, b, b, b);
    }
    
    public static Vector4 mod(float a, Vector4 b) {
        return Vector.mod(a, a, a, a, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector4 mod(Vector4 a, float x1, float y1, float z1, float w1) {
        return Vector.mod(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1);
    }
    
    public static Vector4 mod(float x0, float y0, float z0, float w0, Vector4 b) {
        return Vector.mod(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w());
    }
    
    public static Vector2.Mutable mod(Vector2 a, Vector2 b, Vector2.Mutable m) {
        return Vector.mod(a.x(), a.y(), b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable mod(Vector3 a, Vector3 b, Vector3.Mutable m) {
        return Vector.mod(a.x(), a.y(), a.z(), b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable mod(Vector4 a, Vector4 b, Vector4.Mutable m) {
        return Vector.mod(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2.Mutable mod(Vector2 a, float b, Vector2.Mutable m) {
        return Vector.mod(a.x(), a.y(), b, b, m);
    }
    
    public static Vector2.Mutable mod(float a, Vector2 b, Vector2.Mutable m) {
        return Vector.mod(a, a, b.x(), b.y(), m);
    }
    
    public static Vector2.Mutable mod(Vector2 a, float x1, float y1, Vector2.Mutable m) {
        return Vector.mod(a.x(), a.y(), x1, y1, m);
    }
    
    public static Vector2.Mutable mod(float x0, float y0, Vector2 b, Vector2.Mutable m) {
        return Vector.mod(x0, y0, b.x(), b.y(), m);
    }
    
    public static Vector3.Mutable mod(Vector3 a, float b, Vector3.Mutable m) {
        return Vector.mod(a.x(), a.y(), a.z(), b, b, b, m);
    }
    
    public static Vector3.Mutable mod(float a, Vector3 b, Vector3.Mutable m) {
        return Vector.mod(a, a, a, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector3.Mutable mod(Vector3 a, float x1, float y1, float z1, Vector3.Mutable m) {
        return Vector.mod(a.x(), a.y(), a.z(), x1, y1, z1, m);
    }
    
    public static Vector3.Mutable mod(float x0, float y0, float z0, Vector3 b, Vector3.Mutable m) {
        return Vector.mod(x0, y0, z0, b.x(), b.y(), b.z(), m);
    }
    
    public static Vector4.Mutable mod(Vector4 a, float b, Vector4.Mutable m) {
        return Vector.mod(a.x(), a.y(), a.z(), a.w(), b, b, b, b, m);
    }
    
    public static Vector4.Mutable mod(float a, Vector4 b, Vector4.Mutable m) {
        return Vector.mod(a, a, a, a, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector4.Mutable mod(Vector4 a, float x1, float y1, float z1, float w1, Vector4.Mutable m) {
        return Vector.mod(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1, m);
    }
    
    public static Vector4.Mutable mod(float x0, float y0, float z0, float w0, Vector4 b, Vector4.Mutable m) {
        return Vector.mod(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w(), m);
    }
    
    public static Vector2 mod(
        float x0, float y0,
        float x1, float y1
    ) {
        return new Vector2(x0 % x1, y0 % y1);
    }
    
    public static Vector3 mod(
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        return new Vector3(x0 % x1, y0 % y1, z0 % z1);
    }
    
    public static Vector4 mod(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1
    ) {
        return new Vector4(x0 % x1, y0 % y1, z0 % z1, w0 % w1);
    }
    
    public static Vector2.Mutable mod(
        float x0, float y0,
        float x1, float y1,
        Vector2.Mutable m
    ) {
        m.x(x0 % x1);
        m.y(y0 % y1);
        return m;
    }
    
    public static Vector3.Mutable mod(
        float x0, float y0, float z0,
        float x1, float y1, float z1,
        Vector3.Mutable m
    ) {
        m.x(x0 % x1);
        m.y(y0 % y1);
        m.z(z0 % z1);
        return m;
    }
    
    public static Vector4.Mutable mod(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1,
        Vector4.Mutable m
    ) {
        m.x(x0 % x1);
        m.y(y0 % y1);
        m.z(z0 % z1);
        m.w(w0 % w1);
        return m;
    }
    
    public static float dot(Vector2 a) {
        return a.x() * a.x() + a.y() * a.y();
    }
    
    public static float dot(Vector3 a) {
        return a.x() * a.x() + a.y() * a.y() + a.z() * a.z();
    }
    
    public static float dot(Vector4 a) {
        return a.x() * a.x() + a.y() * a.y() + a.z() * a.z() + a.w() * a.w();
    }
    
    public static float dot(Vector2 a, Vector2 b) {
        return a.x() * b.x() + a.y() * b.y();
    }
    
    public static float dot(Vector3 a, Vector3 b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }
    
    public static float dot(Vector4 a, Vector4 b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z() + a.w() * b.w();
    }
    
    public static float dot(float x, float y) {
        return x * x + y * y;
    }
    
    public static float dot(float x, float y, float z) {
        return x * x + y * y + z * z;
    }
    
    public static float dot(float x, float y, float z, float w) {
        return x * x + y * y + z * z + w * w;
    }
    
    public static Vector3 hom(Vector2 a) {
        return new Vector3(a, 1f);
    }
    
    public static Vector4 hom(Vector3 a) {
        return new Vector4(a, 1f);
    }
    
    public static float mag(Vector2 a) {
        return (float) Math.sqrt(dot(a));
    }
    
    public static float mag(Vector3 a) {
        return (float) Math.sqrt(dot(a));
    }
    
    public static float mag(Vector4 a) {
        return (float) Math.sqrt(dot(a));
    }
    
    public static float mag(float x, float y) {
        return (float) Math.sqrt(dot(x, y));
    }
    
    public static float mag(float x, float y, float z) {
        return (float) Math.sqrt(dot(x, y, z));
    }
    
    public static float mag(float x, float y, float z, float w) {
        return (float) Math.sqrt(dot(x, y, z, w));
    }
    
    public static float dst(Vector2 a, Vector2 b) {
        return Vector.dst(a.x(), a.y(), b.x(), b.y());
    }
    
    public static float dst(Vector2 a, Vector3 b) {
        return Vector.dst(a.x(), a.y(), 0f, b.x(), b.y(), b.z());
    }
    
    public static float dst(Vector3 a, Vector2 b) {
        return Vector.dst(a.x(), a.y(), a.z(), b.x(), b.y(), 0f);
    }
    
    public static float dst(Vector3 a, Vector3 b) {
        return Vector.dst(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }
    
    public static float dst(Vector2 a, Vector4 b) {
        return Vector.dst(a.x(), a.y(), 0f, 0f, b.x(), b.y(), b.z(), b.w());
    }
    
    public static float dst(Vector4 a, Vector2 b) {
        return Vector.dst(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), 0f, 0f);
    }
    
    public static float dst(Vector3 a, Vector4 b) {
        return Vector.dst(a.x(), a.y(), a.z(), 0f, b.x(), b.y(), b.z(), b.w());
    }
    
    public static float dst(Vector4 a, Vector3 b) {
        return Vector.dst(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), 0f);
    }
    
    public static float dst(Vector4 a, Vector4 b) {
        return Vector.dst(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w());
    }
    
    public static float dst(Vector2 a, float b) {
        return Vector.dst(a.x(), a.y(), b, b);
    }
    
    public static float dst(float a, Vector2 b) {
        return Vector.dst(a, a, b.x(), b.y());
    }
    
    public static float dst(Vector2 a, float x1, float y1) {
        return Vector.dst(a.x(), a.y(), x1, y1);
    }
    
    public static float dst(float x0, float y0, Vector2 b) {
        return Vector.dst(x0, y0, b.x(), b.y());
    }
    
    public static float dst(Vector3 a, float b) {
        return Vector.dst(a.x(), a.y(), a.z(), b, b, b);
    }
    
    public static float dst(float a, Vector3 b) {
        return Vector.dst(a, a, a, b.x(), b.y(), b.z());
    }
    
    public static float dst(Vector3 a, float x1, float y1, float z1) {
        return Vector.dst(a.x(), a.y(), a.z(), x1, y1, z1);
    }
    
    public static float dst(float x0, float y0, float z0, Vector3 b) {
        return Vector.dst(x0, y0, z0, b.x(), b.y(), b.z());
    }
    
    public static float dst(Vector4 a, float b) {
        return Vector.dst(a.x(), a.y(), a.z(), a.w(), b, b, b, b);
    }
    
    public static float dst(float a, Vector4 b) {
        return Vector.dst(a, a, a, a, b.x(), b.y(), b.z(), b.w());
    }
    
    public static float dst(Vector4 a, float x1, float y1, float z1, float w1) {
        return Vector.dst(a.x(), a.y(), a.z(), a.w(), x1, y1, z1, w1);
    }
    
    public static float dst(float x0, float y0, float z0, float w0, Vector4 b) {
        return Vector.dst(x0, y0, z0, w0, b.x(), b.y(), b.z(), b.w());
    }
    
    public static float dst(
        float x0, float y0,
        float x1, float y1
    ) {
        return mag(x0 - x1, y0 - y1);
    }
    
    public static float dst(
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        return mag(x0 - x1, y0 - y1, z0 - z1);
    }
    
    public static float dst(
        float x0, float y0, float z0, float w0,
        float x1, float y1, float z1, float w1
    ) {
        return mag(x0 - x1, y0 - y1, z0 - z1, w0 - w1);
    }
    
    public static Vector2 normalize(Vector2 a) {
        return div(a, mag(a));
    }
    
    public static Vector3 normalize(Vector3 a) {
        return div(a, mag(a));
    }
    
    public static Vector4 normalize(Vector4 a) {
        return div(a, mag(a));
    }
    
    public static Vector2.Mutable normalize(Vector2 a, Vector2.Mutable m) {
        return div(a, mag(a), m);
    }
    
    public static Vector3.Mutable normalize(Vector3 a, Vector3.Mutable m) {
        return div(a, mag(a), m);
    }
    
    public static Vector4.Mutable normalize(Vector4 a, Vector4.Mutable m) {
        return div(a, mag(a), m);
    }
    
    public default Vector2 xx() {
        return new Vector2(x(), x());
    }
    
    public default Vector2 xy() {
        return new Vector2(x(), y());
    }
    
    public default Vector2 yx() {
        return new Vector2(y(), x());
    }
    
    public default Vector2 yy() {
        return new Vector2(y(), y());
    }
    
    public default Vector3 xxx() {
        return new Vector3(x(), x(), x());
    }
    
    public default Vector3 xxy() {
        return new Vector3(x(), x(), y());
    }
    
    public default Vector3 xxz() {
        return new Vector3(x(), x(), z());
    }
    
    public default Vector3 xyx() {
        return new Vector3(x(), y(), x());
    }
    
    public default Vector3 xyy() {
        return new Vector3(x(), y(), y());
    }
    
    public default Vector3 xyz() {
        return new Vector3(x(), y(), z());
    }
    
    public default Vector3 xzx() {
        return new Vector3(x(), z(), x());
    }
    
    public default Vector3 xzy() {
        return new Vector3(x(), z(), y());
    }
    
    public default Vector3 xzz() {
        return new Vector3(x(), z(), z());
    }
    
    public default Vector3 yxx() {
        return new Vector3(y(), x(), x());
    }
    
    public default Vector3 yxy() {
        return new Vector3(y(), x(), y());
    }
    
    public default Vector3 yxz() {
        return new Vector3(y(), x(), z());
    }
    
    public default Vector3 yyx() {
        return new Vector3(y(), y(), x());
    }
    
    public default Vector3 yyy() {
        return new Vector3(y(), y(), y());
    }
    
    public default Vector3 yyz() {
        return new Vector3(y(), y(), z());
    }
    
    public default Vector3 yzx() {
        return new Vector3(y(), z(), x());
    }
    
    public default Vector3 yzy() {
        return new Vector3(y(), z(), y());
    }
    
    public default Vector3 yzz() {
        return new Vector3(y(), z(), z());
    }
    
    public default Vector3 zxx() {
        return new Vector3(z(), x(), x());
    }
    
    public default Vector3 zxy() {
        return new Vector3(z(), x(), y());
    }
    
    public default Vector3 zxz() {
        return new Vector3(z(), x(), z());
    }
    
    public default Vector3 zyx() {
        return new Vector3(z(), y(), x());
    }
    
    public default Vector3 zyy() {
        return new Vector3(z(), y(), y());
    }
    
    public default Vector3 zyz() {
        return new Vector3(z(), y(), z());
    }
    
    public default Vector3 zzx() {
        return new Vector3(z(), z(), x());
    }
    
    public default Vector3 zzy() {
        return new Vector3(z(), z(), y());
    }
    
    public default Vector3 zzz() {
        return new Vector3(z(), z(), z());
    }
    
    public default Vector4 xxxx() {
        return new Vector4(x(), x(), x(), x());
    }
    
    public default Vector4 xxxy() {
        return new Vector4(x(), x(), x(), y());
    }
    
    public default Vector4 xxxz() {
        return new Vector4(x(), x(), x(), z());
    }
    
    public default Vector4 xxxw() {
        return new Vector4(x(), x(), x(), w());
    }
    
    public default Vector4 xxyx() {
        return new Vector4(x(), x(), y(), x());
    }
    
    public default Vector4 xxyy() {
        return new Vector4(x(), x(), y(), y());
    }
    
    public default Vector4 xxyz() {
        return new Vector4(x(), x(), y(), z());
    }
    
    public default Vector4 xxyw() {
        return new Vector4(x(), x(), y(), w());
    }
    
    public default Vector4 xxzx() {
        return new Vector4(x(), x(), z(), x());
    }
    
    public default Vector4 xxzy() {
        return new Vector4(x(), x(), z(), y());
    }
    
    public default Vector4 xxzz() {
        return new Vector4(x(), x(), z(), z());
    }
    
    public default Vector4 xxzw() {
        return new Vector4(x(), x(), z(), w());
    }
    
    public default Vector4 xxwx() {
        return new Vector4(x(), x(), w(), x());
    }
    
    public default Vector4 xxwy() {
        return new Vector4(x(), x(), w(), y());
    }
    
    public default Vector4 xxwz() {
        return new Vector4(x(), x(), w(), z());
    }
    
    public default Vector4 xxww() {
        return new Vector4(x(), x(), w(), w());
    }
    
    public default Vector4 xyxx() {
        return new Vector4(x(), y(), x(), x());
    }
    
    public default Vector4 xyxy() {
        return new Vector4(x(), y(), x(), y());
    }
    
    public default Vector4 xyxz() {
        return new Vector4(x(), y(), x(), z());
    }
    
    public default Vector4 xyxw() {
        return new Vector4(x(), y(), x(), w());
    }
    
    public default Vector4 xyyx() {
        return new Vector4(x(), y(), y(), x());
    }
    
    public default Vector4 xyyy() {
        return new Vector4(x(), y(), y(), y());
    }
    
    public default Vector4 xyyz() {
        return new Vector4(x(), y(), y(), z());
    }
    
    public default Vector4 xyyw() {
        return new Vector4(x(), y(), y(), w());
    }
    
    public default Vector4 xyzx() {
        return new Vector4(x(), y(), z(), x());
    }
    
    public default Vector4 xyzy() {
        return new Vector4(x(), y(), z(), y());
    }
    
    public default Vector4 xyzz() {
        return new Vector4(x(), y(), z(), z());
    }
    
    public default Vector4 xyzw() {
        return new Vector4(x(), y(), z(), w());
    }
    
    public default Vector4 xywx() {
        return new Vector4(x(), y(), w(), x());
    }
    
    public default Vector4 xywy() {
        return new Vector4(x(), y(), w(), y());
    }
    
    public default Vector4 xywz() {
        return new Vector4(x(), y(), w(), z());
    }
    
    public default Vector4 xyww() {
        return new Vector4(x(), y(), w(), w());
    }
    
    public default Vector4 xzxx() {
        return new Vector4(x(), z(), x(), x());
    }
    
    public default Vector4 xzxy() {
        return new Vector4(x(), z(), x(), y());
    }
    
    public default Vector4 xzxz() {
        return new Vector4(x(), z(), x(), z());
    }
    
    public default Vector4 xzxw() {
        return new Vector4(x(), z(), x(), w());
    }
    
    public default Vector4 xzyx() {
        return new Vector4(x(), z(), y(), x());
    }
    
    public default Vector4 xzyy() {
        return new Vector4(x(), z(), y(), y());
    }
    
    public default Vector4 xzyz() {
        return new Vector4(x(), z(), y(), z());
    }
    
    public default Vector4 xzyw() {
        return new Vector4(x(), z(), y(), w());
    }
    
    public default Vector4 xzzx() {
        return new Vector4(x(), z(), z(), x());
    }
    
    public default Vector4 xzzy() {
        return new Vector4(x(), z(), z(), y());
    }
    
    public default Vector4 xzzz() {
        return new Vector4(x(), z(), z(), z());
    }
    
    public default Vector4 xzzw() {
        return new Vector4(x(), z(), z(), w());
    }
    
    public default Vector4 xzwx() {
        return new Vector4(x(), z(), w(), x());
    }
    
    public default Vector4 xzwy() {
        return new Vector4(x(), z(), w(), y());
    }
    
    public default Vector4 xzwz() {
        return new Vector4(x(), z(), w(), z());
    }
    
    public default Vector4 xzww() {
        return new Vector4(x(), z(), w(), w());
    }
    
    public default Vector4 xwxx() {
        return new Vector4(x(), w(), x(), x());
    }
    
    public default Vector4 xwxy() {
        return new Vector4(x(), w(), x(), y());
    }
    
    public default Vector4 xwxz() {
        return new Vector4(x(), w(), x(), z());
    }
    
    public default Vector4 xwxw() {
        return new Vector4(x(), w(), x(), w());
    }
    
    public default Vector4 xwyx() {
        return new Vector4(x(), w(), y(), x());
    }
    
    public default Vector4 xwyy() {
        return new Vector4(x(), w(), y(), y());
    }
    
    public default Vector4 xwyz() {
        return new Vector4(x(), w(), y(), z());
    }
    
    public default Vector4 xwyw() {
        return new Vector4(x(), w(), y(), w());
    }
    
    public default Vector4 xwzx() {
        return new Vector4(x(), w(), z(), x());
    }
    
    public default Vector4 xwzy() {
        return new Vector4(x(), w(), z(), y());
    }
    
    public default Vector4 xwzz() {
        return new Vector4(x(), w(), z(), z());
    }
    
    public default Vector4 xwzw() {
        return new Vector4(x(), w(), z(), w());
    }
    
    public default Vector4 xwwx() {
        return new Vector4(x(), w(), w(), x());
    }
    
    public default Vector4 xwwy() {
        return new Vector4(x(), w(), w(), y());
    }
    
    public default Vector4 xwwz() {
        return new Vector4(x(), w(), w(), z());
    }
    
    public default Vector4 xwww() {
        return new Vector4(x(), w(), w(), w());
    }
    
    public default Vector4 yxxx() {
        return new Vector4(y(), x(), x(), x());
    }
    
    public default Vector4 yxxy() {
        return new Vector4(y(), x(), x(), y());
    }
    
    public default Vector4 yxxz() {
        return new Vector4(y(), x(), x(), z());
    }
    
    public default Vector4 yxxw() {
        return new Vector4(y(), x(), x(), w());
    }
    
    public default Vector4 yxyx() {
        return new Vector4(y(), x(), y(), x());
    }
    
    public default Vector4 yxyy() {
        return new Vector4(y(), x(), y(), y());
    }
    
    public default Vector4 yxyz() {
        return new Vector4(y(), x(), y(), z());
    }
    
    public default Vector4 yxyw() {
        return new Vector4(y(), x(), y(), w());
    }
    
    public default Vector4 yxzx() {
        return new Vector4(y(), x(), z(), x());
    }
    
    public default Vector4 yxzy() {
        return new Vector4(y(), x(), z(), y());
    }
    
    public default Vector4 yxzz() {
        return new Vector4(y(), x(), z(), z());
    }
    
    public default Vector4 yxzw() {
        return new Vector4(y(), x(), z(), w());
    }
    
    public default Vector4 yxwx() {
        return new Vector4(y(), x(), w(), x());
    }
    
    public default Vector4 yxwy() {
        return new Vector4(y(), x(), w(), y());
    }
    
    public default Vector4 yxwz() {
        return new Vector4(y(), x(), w(), z());
    }
    
    public default Vector4 yxww() {
        return new Vector4(y(), x(), w(), w());
    }
    
    public default Vector4 yyxx() {
        return new Vector4(y(), y(), x(), x());
    }
    
    public default Vector4 yyxy() {
        return new Vector4(y(), y(), x(), y());
    }
    
    public default Vector4 yyxz() {
        return new Vector4(y(), y(), x(), z());
    }
    
    public default Vector4 yyxw() {
        return new Vector4(y(), y(), x(), w());
    }
    
    public default Vector4 yyyx() {
        return new Vector4(y(), y(), y(), x());
    }
    
    public default Vector4 yyyy() {
        return new Vector4(y(), y(), y(), y());
    }
    
    public default Vector4 yyyz() {
        return new Vector4(y(), y(), y(), z());
    }
    
    public default Vector4 yyyw() {
        return new Vector4(y(), y(), y(), w());
    }
    
    public default Vector4 yyzx() {
        return new Vector4(y(), y(), z(), x());
    }
    
    public default Vector4 yyzy() {
        return new Vector4(y(), y(), z(), y());
    }
    
    public default Vector4 yyzz() {
        return new Vector4(y(), y(), z(), z());
    }
    
    public default Vector4 yyzw() {
        return new Vector4(y(), y(), z(), w());
    }
    
    public default Vector4 yywx() {
        return new Vector4(y(), y(), w(), x());
    }
    
    public default Vector4 yywy() {
        return new Vector4(y(), y(), w(), y());
    }
    
    public default Vector4 yywz() {
        return new Vector4(y(), y(), w(), z());
    }
    
    public default Vector4 yyww() {
        return new Vector4(y(), y(), w(), w());
    }
    
    public default Vector4 yzxx() {
        return new Vector4(y(), z(), x(), x());
    }
    
    public default Vector4 yzxy() {
        return new Vector4(y(), z(), x(), y());
    }
    
    public default Vector4 yzxz() {
        return new Vector4(y(), z(), x(), z());
    }
    
    public default Vector4 yzxw() {
        return new Vector4(y(), z(), x(), w());
    }
    
    public default Vector4 yzyx() {
        return new Vector4(y(), z(), y(), x());
    }
    
    public default Vector4 yzyy() {
        return new Vector4(y(), z(), y(), y());
    }
    
    public default Vector4 yzyz() {
        return new Vector4(y(), z(), y(), z());
    }
    
    public default Vector4 yzyw() {
        return new Vector4(y(), z(), y(), w());
    }
    
    public default Vector4 yzzx() {
        return new Vector4(y(), z(), z(), x());
    }
    
    public default Vector4 yzzy() {
        return new Vector4(y(), z(), z(), y());
    }
    
    public default Vector4 yzzz() {
        return new Vector4(y(), z(), z(), z());
    }
    
    public default Vector4 yzzw() {
        return new Vector4(y(), z(), z(), w());
    }
    
    public default Vector4 yzwx() {
        return new Vector4(y(), z(), w(), x());
    }
    
    public default Vector4 yzwy() {
        return new Vector4(y(), z(), w(), y());
    }
    
    public default Vector4 yzwz() {
        return new Vector4(y(), z(), w(), z());
    }
    
    public default Vector4 yzww() {
        return new Vector4(y(), z(), w(), w());
    }
    
    public default Vector4 ywxx() {
        return new Vector4(y(), w(), x(), x());
    }
    
    public default Vector4 ywxy() {
        return new Vector4(y(), w(), x(), y());
    }
    
    public default Vector4 ywxz() {
        return new Vector4(y(), w(), x(), z());
    }
    
    public default Vector4 ywxw() {
        return new Vector4(y(), w(), x(), w());
    }
    
    public default Vector4 ywyx() {
        return new Vector4(y(), w(), y(), x());
    }
    
    public default Vector4 ywyy() {
        return new Vector4(y(), w(), y(), y());
    }
    
    public default Vector4 ywyz() {
        return new Vector4(y(), w(), y(), z());
    }
    
    public default Vector4 ywyw() {
        return new Vector4(y(), w(), y(), w());
    }
    
    public default Vector4 ywzx() {
        return new Vector4(y(), w(), z(), x());
    }
    
    public default Vector4 ywzy() {
        return new Vector4(y(), w(), z(), y());
    }
    
    public default Vector4 ywzz() {
        return new Vector4(y(), w(), z(), z());
    }
    
    public default Vector4 ywzw() {
        return new Vector4(y(), w(), z(), w());
    }
    
    public default Vector4 ywwx() {
        return new Vector4(y(), w(), w(), x());
    }
    
    public default Vector4 ywwy() {
        return new Vector4(y(), w(), w(), y());
    }
    
    public default Vector4 ywwz() {
        return new Vector4(y(), w(), w(), z());
    }
    
    public default Vector4 ywww() {
        return new Vector4(y(), w(), w(), w());
    }
    
    public default Vector4 zxxx() {
        return new Vector4(z(), x(), x(), x());
    }
    
    public default Vector4 zxxy() {
        return new Vector4(z(), x(), x(), y());
    }
    
    public default Vector4 zxxz() {
        return new Vector4(z(), x(), x(), z());
    }
    
    public default Vector4 zxxw() {
        return new Vector4(z(), x(), x(), w());
    }
    
    public default Vector4 zxyx() {
        return new Vector4(z(), x(), y(), x());
    }
    
    public default Vector4 zxyy() {
        return new Vector4(z(), x(), y(), y());
    }
    
    public default Vector4 zxyz() {
        return new Vector4(z(), x(), y(), z());
    }
    
    public default Vector4 zxyw() {
        return new Vector4(z(), x(), y(), w());
    }
    
    public default Vector4 zxzx() {
        return new Vector4(z(), x(), z(), x());
    }
    
    public default Vector4 zxzy() {
        return new Vector4(z(), x(), z(), y());
    }
    
    public default Vector4 zxzz() {
        return new Vector4(z(), x(), z(), z());
    }
    
    public default Vector4 zxzw() {
        return new Vector4(z(), x(), z(), w());
    }
    
    public default Vector4 zxwx() {
        return new Vector4(z(), x(), w(), x());
    }
    
    public default Vector4 zxwy() {
        return new Vector4(z(), x(), w(), y());
    }
    
    public default Vector4 zxwz() {
        return new Vector4(z(), x(), w(), z());
    }
    
    public default Vector4 zxww() {
        return new Vector4(z(), x(), w(), w());
    }
    
    public default Vector4 zyxx() {
        return new Vector4(z(), y(), x(), x());
    }
    
    public default Vector4 zyxy() {
        return new Vector4(z(), y(), x(), y());
    }
    
    public default Vector4 zyxz() {
        return new Vector4(z(), y(), x(), z());
    }
    
    public default Vector4 zyxw() {
        return new Vector4(z(), y(), x(), w());
    }
    
    public default Vector4 zyyx() {
        return new Vector4(z(), y(), y(), x());
    }
    
    public default Vector4 zyyy() {
        return new Vector4(z(), y(), y(), y());
    }
    
    public default Vector4 zyyz() {
        return new Vector4(z(), y(), y(), z());
    }
    
    public default Vector4 zyyw() {
        return new Vector4(z(), y(), y(), w());
    }
    
    public default Vector4 zyzx() {
        return new Vector4(z(), y(), z(), x());
    }
    
    public default Vector4 zyzy() {
        return new Vector4(z(), y(), z(), y());
    }
    
    public default Vector4 zyzz() {
        return new Vector4(z(), y(), z(), z());
    }
    
    public default Vector4 zyzw() {
        return new Vector4(z(), y(), z(), w());
    }
    
    public default Vector4 zywx() {
        return new Vector4(z(), y(), w(), x());
    }
    
    public default Vector4 zywy() {
        return new Vector4(z(), y(), w(), y());
    }
    
    public default Vector4 zywz() {
        return new Vector4(z(), y(), w(), z());
    }
    
    public default Vector4 zyww() {
        return new Vector4(z(), y(), w(), w());
    }
    
    public default Vector4 zzxx() {
        return new Vector4(z(), z(), x(), x());
    }
    
    public default Vector4 zzxy() {
        return new Vector4(z(), z(), x(), y());
    }
    
    public default Vector4 zzxz() {
        return new Vector4(z(), z(), x(), z());
    }
    
    public default Vector4 zzxw() {
        return new Vector4(z(), z(), x(), w());
    }
    
    public default Vector4 zzyx() {
        return new Vector4(z(), z(), y(), x());
    }
    
    public default Vector4 zzyy() {
        return new Vector4(z(), z(), y(), y());
    }
    
    public default Vector4 zzyz() {
        return new Vector4(z(), z(), y(), z());
    }
    
    public default Vector4 zzyw() {
        return new Vector4(z(), z(), y(), w());
    }
    
    public default Vector4 zzzx() {
        return new Vector4(z(), z(), z(), x());
    }
    
    public default Vector4 zzzy() {
        return new Vector4(z(), z(), z(), y());
    }
    
    public default Vector4 zzzz() {
        return new Vector4(z(), z(), z(), z());
    }
    
    public default Vector4 zzzw() {
        return new Vector4(z(), z(), z(), w());
    }
    
    public default Vector4 zzwx() {
        return new Vector4(z(), z(), w(), x());
    }
    
    public default Vector4 zzwy() {
        return new Vector4(z(), z(), w(), y());
    }
    
    public default Vector4 zzwz() {
        return new Vector4(z(), z(), w(), z());
    }
    
    public default Vector4 zzww() {
        return new Vector4(z(), z(), w(), w());
    }
    
    public default Vector4 zwxx() {
        return new Vector4(z(), w(), x(), x());
    }
    
    public default Vector4 zwxy() {
        return new Vector4(z(), w(), x(), y());
    }
    
    public default Vector4 zwxz() {
        return new Vector4(z(), w(), x(), z());
    }
    
    public default Vector4 zwxw() {
        return new Vector4(z(), w(), x(), w());
    }
    
    public default Vector4 zwyx() {
        return new Vector4(z(), w(), y(), x());
    }
    
    public default Vector4 zwyy() {
        return new Vector4(z(), w(), y(), y());
    }
    
    public default Vector4 zwyz() {
        return new Vector4(z(), w(), y(), z());
    }
    
    public default Vector4 zwyw() {
        return new Vector4(z(), w(), y(), w());
    }
    
    public default Vector4 zwzx() {
        return new Vector4(z(), w(), z(), x());
    }
    
    public default Vector4 zwzy() {
        return new Vector4(z(), w(), z(), y());
    }
    
    public default Vector4 zwzz() {
        return new Vector4(z(), w(), z(), z());
    }
    
    public default Vector4 zwzw() {
        return new Vector4(z(), w(), z(), w());
    }
    
    public default Vector4 zwwx() {
        return new Vector4(z(), w(), w(), x());
    }
    
    public default Vector4 zwwy() {
        return new Vector4(z(), w(), w(), y());
    }
    
    public default Vector4 zwwz() {
        return new Vector4(z(), w(), w(), z());
    }
    
    public default Vector4 zwww() {
        return new Vector4(z(), w(), w(), w());
    }
    
    public default Vector4 wxxx() {
        return new Vector4(w(), x(), x(), x());
    }
    
    public default Vector4 wxxy() {
        return new Vector4(w(), x(), x(), y());
    }
    
    public default Vector4 wxxz() {
        return new Vector4(w(), x(), x(), z());
    }
    
    public default Vector4 wxxw() {
        return new Vector4(w(), x(), x(), w());
    }
    
    public default Vector4 wxyx() {
        return new Vector4(w(), x(), y(), x());
    }
    
    public default Vector4 wxyy() {
        return new Vector4(w(), x(), y(), y());
    }
    
    public default Vector4 wxyz() {
        return new Vector4(w(), x(), y(), z());
    }
    
    public default Vector4 wxyw() {
        return new Vector4(w(), x(), y(), w());
    }
    
    public default Vector4 wxzx() {
        return new Vector4(w(), x(), z(), x());
    }
    
    public default Vector4 wxzy() {
        return new Vector4(w(), x(), z(), y());
    }
    
    public default Vector4 wxzz() {
        return new Vector4(w(), x(), z(), z());
    }
    
    public default Vector4 wxzw() {
        return new Vector4(w(), x(), z(), w());
    }
    
    public default Vector4 wxwx() {
        return new Vector4(w(), x(), w(), x());
    }
    
    public default Vector4 wxwy() {
        return new Vector4(w(), x(), w(), y());
    }
    
    public default Vector4 wxwz() {
        return new Vector4(w(), x(), w(), z());
    }
    
    public default Vector4 wxww() {
        return new Vector4(w(), x(), w(), w());
    }
    
    public default Vector4 wyxx() {
        return new Vector4(w(), y(), x(), x());
    }
    
    public default Vector4 wyxy() {
        return new Vector4(w(), y(), x(), y());
    }
    
    public default Vector4 wyxz() {
        return new Vector4(w(), y(), x(), z());
    }
    
    public default Vector4 wyxw() {
        return new Vector4(w(), y(), x(), w());
    }
    
    public default Vector4 wyyx() {
        return new Vector4(w(), y(), y(), x());
    }
    
    public default Vector4 wyyy() {
        return new Vector4(w(), y(), y(), y());
    }
    
    public default Vector4 wyyz() {
        return new Vector4(w(), y(), y(), z());
    }
    
    public default Vector4 wyyw() {
        return new Vector4(w(), y(), y(), w());
    }
    
    public default Vector4 wyzx() {
        return new Vector4(w(), y(), z(), x());
    }
    
    public default Vector4 wyzy() {
        return new Vector4(w(), y(), z(), y());
    }
    
    public default Vector4 wyzz() {
        return new Vector4(w(), y(), z(), z());
    }
    
    public default Vector4 wyzw() {
        return new Vector4(w(), y(), z(), w());
    }
    
    public default Vector4 wywx() {
        return new Vector4(w(), y(), w(), x());
    }
    
    public default Vector4 wywy() {
        return new Vector4(w(), y(), w(), y());
    }
    
    public default Vector4 wywz() {
        return new Vector4(w(), y(), w(), z());
    }
    
    public default Vector4 wyww() {
        return new Vector4(w(), y(), w(), w());
    }
    
    public default Vector4 wzxx() {
        return new Vector4(w(), z(), x(), x());
    }
    
    public default Vector4 wzxy() {
        return new Vector4(w(), z(), x(), y());
    }
    
    public default Vector4 wzxz() {
        return new Vector4(w(), z(), x(), z());
    }
    
    public default Vector4 wzxw() {
        return new Vector4(w(), z(), x(), w());
    }
    
    public default Vector4 wzyx() {
        return new Vector4(w(), z(), y(), x());
    }
    
    public default Vector4 wzyy() {
        return new Vector4(w(), z(), y(), y());
    }
    
    public default Vector4 wzyz() {
        return new Vector4(w(), z(), y(), z());
    }
    
    public default Vector4 wzyw() {
        return new Vector4(w(), z(), y(), w());
    }
    
    public default Vector4 wzzx() {
        return new Vector4(w(), z(), z(), x());
    }
    
    public default Vector4 wzzy() {
        return new Vector4(w(), z(), z(), y());
    }
    
    public default Vector4 wzzz() {
        return new Vector4(w(), z(), z(), z());
    }
    
    public default Vector4 wzzw() {
        return new Vector4(w(), z(), z(), w());
    }
    
    public default Vector4 wzwx() {
        return new Vector4(w(), z(), w(), x());
    }
    
    public default Vector4 wzwy() {
        return new Vector4(w(), z(), w(), y());
    }
    
    public default Vector4 wzwz() {
        return new Vector4(w(), z(), w(), z());
    }
    
    public default Vector4 wzww() {
        return new Vector4(w(), z(), w(), w());
    }
    
    public default Vector4 wwxx() {
        return new Vector4(w(), w(), x(), x());
    }
    
    public default Vector4 wwxy() {
        return new Vector4(w(), w(), x(), y());
    }
    
    public default Vector4 wwxz() {
        return new Vector4(w(), w(), x(), z());
    }
    
    public default Vector4 wwxw() {
        return new Vector4(w(), w(), x(), w());
    }
    
    public default Vector4 wwyx() {
        return new Vector4(w(), w(), y(), x());
    }
    
    public default Vector4 wwyy() {
        return new Vector4(w(), w(), y(), y());
    }
    
    public default Vector4 wwyz() {
        return new Vector4(w(), w(), y(), z());
    }
    
    public default Vector4 wwyw() {
        return new Vector4(w(), w(), y(), w());
    }
    
    public default Vector4 wwzx() {
        return new Vector4(w(), w(), z(), x());
    }
    
    public default Vector4 wwzy() {
        return new Vector4(w(), w(), z(), y());
    }
    
    public default Vector4 wwzz() {
        return new Vector4(w(), w(), z(), z());
    }
    
    public default Vector4 wwzw() {
        return new Vector4(w(), w(), z(), w());
    }
    
    public default Vector4 wwwx() {
        return new Vector4(w(), w(), w(), x());
    }
    
    public default Vector4 wwwy() {
        return new Vector4(w(), w(), w(), y());
    }
    
    public default Vector4 wwwz() {
        return new Vector4(w(), w(), w(), z());
    }
    
    public default Vector4 wwww() {
        return new Vector4(w(), w(), w(), w());
    }
}
