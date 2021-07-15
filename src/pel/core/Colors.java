package pel.core;

import pel.math.Vector;
import pel.math.Vector3;
import pel.math.Vector4;

public final class Colors {
    
    public static int r(int argb) {
        return (argb >> 16) & 0xff;
    }
    
    public static int g(int argb) {
        return (argb >>  8) & 0xff;
    }
    
    public static int b(int argb) {
        return argb & 0xff;
    }
    
    public static int a(int argb) {
        return (argb >> 24) & 0xff;
    }
    
    public static java.awt.Color color3i(Vector color) {
        return color3i((int)color.x(), (int)color.y(), (int)color.z());
    }
    
    public static java.awt.Color color4i(Vector color) {
        return color4i((int)color.x(), (int)color.y(), (int)color.z(), (int)color.w());
    }
    
    public static java.awt.Color color3f(Vector color) {
        return color3f(color.x(), color.y(), color.z());
    }
    
    public static java.awt.Color color4f(Vector color) {
        return color4f(color.x(), color.y(), color.z(), color.w());
    }
    
    public static java.awt.Color color3i(int color) {
        return color3i(r(color), g(color), b(color));
    }
    
    public static java.awt.Color color4i(int color) {
        return color4i(r(color), g(color), b(color), a(color));
    }
    
    public static java.awt.Color color3i(int r, int g, int b) {
        return new java.awt.Color(r, g, b);
    }
    
    public static java.awt.Color color4i(int r, int g, int b, int a) {
        return new java.awt.Color(r, g, b, a);
    }
    
    public static java.awt.Color color3f(float r, float g, float b) {
        return new java.awt.Color(r, g, b);
    }
    
    public static java.awt.Color color4f(float r, float g, float b, float a) {
        return new java.awt.Color(r, g, b, a);
    }
    
    public static Vector3 color3i(java.awt.Color color) {
        int argb = color.getRGB();
        return new Vector3(r(argb), g(argb), b(argb));
    }
    
    public static Vector4 color4i(java.awt.Color color) {
        int argb = color.getRGB();
        return new Vector4(r(argb), g(argb), b(argb), a(argb));
    }
    
    public static Vector3 color3f(java.awt.Color color) {
        float[] rgba = color.getRGBComponents(null);
        return new Vector3(rgba[0], rgba[1], rgba[2]);
    }
    
    public static Vector4 color4f(java.awt.Color color) {
        float[] rgba = color.getRGBComponents(null);
        return new Vector4(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
}
