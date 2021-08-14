package lrj.core;

import lrj.math.Bounds2;
import lrj.math.Box;
import lrj.math.Region2;
import lrj.util.Copyable;
import lrj.util.Utility;

import java.io.Serializable;

import static lrj.util.StringToObject.stringToFloat;

public class Layout implements Copyable<Layout>, Serializable {
    private static final long
        serialVersionUID = 1L;
    public static final Attribute
        X0 = new Attribute(0f), Y0 = new Attribute(0f),
        X1 = new Attribute(0f), Y1 = new Attribute(0f),
        W0 = new Attribute(1f), H0 = new Attribute(1f),
        W1 = new Attribute(0f), H1 = new Attribute(0f),
        W2 = new Attribute(1f), H2 = new Attribute(1f);
    
    protected Attribute.Mutable
        x0, y0,
        x1, y1,
        w0, h0,
        w1, h1,
        w2, h2;
    
    public Layout() {
        // do nothing
    }
    
    public Layout(
        Object w0, Object h0
    ) {
        mSet(
            null, null,
            null, null,
            w0, h0,
            w0, h0,
            w0, h0
        );
    }
    
    public Layout(
        Object x0, Object y0,
        Object x1, Object y1,
        Object w0, Object h0
    ) {
        mSet(
            x0, y0,
            x1, y1,
            w0, h0,
            w0, h0,
            w0, h0
        );
    }
    
    public Layout(
        Object x0, Object y0,
        Object x1, Object y1,
        Object w0, Object h0,
        Object w1, Object h1,
        Object w2, Object h2
    ) {
        mSet(
            x0, y0,
            x1, y1,
            w0, h0,
            w1, h1,
            w2, h2
        );
    }
    
    public Layout(
        Layout l
    ) {
        mSet(
            l.x0, l.y0,
            l.x1, l.y1,
            l.w0, l.h0,
            l.w1, l.h1,
            l.w2, l.h2
        );
    }
    
    public Attribute x0() { return x0 != null ? x0 : X0; }
    public Attribute y0() { return y0 != null ? y0 : Y0; }
    public Attribute x1() { return x1 != null ? x1 : X1; }
    public Attribute y1() { return y1 != null ? y1 : Y1; }
    public Attribute w0() { return w0 != null ? w0 : W0; }
    public Attribute h0() { return h0 != null ? h0 : H0; }
    public Attribute w1() { return w1 != null ? w1 : W1; }
    public Attribute h1() { return h1 != null ? h1 : H1; }
    public Attribute w2() { return w2 != null ? w2 : W2; }
    public Attribute h2() { return h2 != null ? h2 : H2; }
    
    protected void mSet(
        Object x0, Object y0,
        Object x1, Object y1,
        Object w0, Object h0,
        Object w1, Object h1,
        Object w2, Object h2
    ) {
        this.x0 = x0 != null ? new Attribute.Mutable(x0) : null;
        this.y0 = y0 != null ? new Attribute.Mutable(y0) : null;
        this.x1 = x1 != null ? new Attribute.Mutable(x1) : null;
        this.y1 = y1 != null ? new Attribute.Mutable(y1) : null;
        this.w0 = w0 != null ? new Attribute.Mutable(w0) : null;
        this.h0 = h0 != null ? new Attribute.Mutable(h0) : null;
        this.w1 = w1 != null ? new Attribute.Mutable(w1) : null;
        this.h1 = h1 != null ? new Attribute.Mutable(h1) : null;
        this.w2 = w2 != null ? new Attribute.Mutable(w2) : null;
        this.h2 = h2 != null ? new Attribute.Mutable(h2) : null;
    }
    
    @Override
    public Layout copy() {
        return new Layout(this);
    }
    
    @Override
    public String toString() {
        return Layout.toString(this);
    }
    
    public static String toString(Layout l) {
        String s = "{" + (l.x0 != null ? " x0: " + l.x0 : "");
        if(l.y0 != null) s += (s.length() > 1 ? "," : "") + " y0: " + l.y0;
        if(l.x1 != null) s += (s.length() > 1 ? "," : "") + " x1: " + l.x1;
        if(l.y1 != null) s += (s.length() > 1 ? "," : "") + " y1: " + l.y1;
        if(l.w0 != null) s += (s.length() > 1 ? "," : "") + " w0: " + l.w0;
        if(l.h0 != null) s += (s.length() > 1 ? "," : "") + " h0: " + l.h0;
        if(l.w1 != null) s += (s.length() > 1 ? "," : "") + " w1: " + l.w1;
        if(l.h1 != null) s += (s.length() > 1 ? "," : "") + " h1: " + l.h1;
        if(l.w2 != null) s += (s.length() > 1 ? "," : "") + " w2: " + l.w2;
        if(l.h2 != null) s += (s.length() > 1 ? "," : "") + " h2: " + l.h2;
        return s + " }";
    }
    
    public static Layout fromString(String s) {
        return Layout.fromString(new Layout(), s);
    }
    
    protected static <L extends Layout> L fromString(L l, String s) {
        if(l != null && s != null) {
            int
                i = s.indexOf("{"),
                j = s.indexOf("}");
            if (i >= 0 || j >= 0) {
                if (j > i)
                    s = s.substring(++i, j);
                else
                    s = s.substring(++i);
            }
            
            String[] t = Utility.parse( s,
                "x0", "y0", "x1", "y1", "w0",
                "h0", "w1", "h1", "w2", "h2"
            );
            l.x0 = t[0] != null && !isBlank(t[0]) ? Attribute.Mutable.fromString(t[0]) : null;
            l.y0 = t[1] != null && !isBlank(t[1]) ? Attribute.Mutable.fromString(t[1]) : null;
            l.x1 = t[2] != null && !isBlank(t[2]) ? Attribute.Mutable.fromString(t[2]) : null;
            l.y1 = t[3] != null && !isBlank(t[3]) ? Attribute.Mutable.fromString(t[3]) : null;
            l.w0 = t[4] != null && !isBlank(t[4]) ? Attribute.Mutable.fromString(t[4]) : null;
            l.h0 = t[5] != null && !isBlank(t[5]) ? Attribute.Mutable.fromString(t[5]) : null;
            l.w1 = t[6] != null && !isBlank(t[6]) ? Attribute.Mutable.fromString(t[6]) : null;
            l.h1 = t[7] != null && !isBlank(t[7]) ? Attribute.Mutable.fromString(t[7]) : null;
            l.w2 = t[8] != null && !isBlank(t[8]) ? Attribute.Mutable.fromString(t[8]) : null;
            l.h2 = t[9] != null && !isBlank(t[9]) ? Attribute.Mutable.fromString(t[9]) : null;
        }
        return l;
    }

    protected static boolean isBlank(String s) {
        return s.trim().equals("");
    }
    
    public static class Mutable extends Layout {
        private static final long
            serialVersionUID = 1L;
        
        public Mutable() {
            super();
        }
        
        public Mutable(
            Layout l
        ) {
            super(l);
        }
        
        @Override
        public Attribute.Mutable x0() { return x0; }
        @Override
        public Attribute.Mutable y0() { return y0; }
        @Override
        public Attribute.Mutable x1() { return x1; }
        @Override
        public Attribute.Mutable y1() { return y1; }
        @Override
        public Attribute.Mutable w0() { return w0; }
        @Override
        public Attribute.Mutable h0() { return h0; }
        @Override
        public Attribute.Mutable w1() { return w1; }
        @Override
        public Attribute.Mutable h1() { return h1; }
        @Override
        public Attribute.Mutable w2() { return w2; }
        @Override
        public Attribute.Mutable h2() { return h2; }
        
        public Mutable set(
            Object w0, Object h0
        ) {
            mSet(
                null, null,
                null, null,
                w0, h0,
                w0, h0,
                w0, h0
            );
            return this;
        }
    
        public Mutable set(
            Object x0, Object y0,
            Object x1, Object y1,
            Object w0, Object h0
        ) {
            mSet(
                x0, y0,
                x1, y1,
                w0, h0,
                w0, h0,
                w0, h0
            );
            return this;
        }
    
        public Mutable set(
            Object x0, Object y0,
            Object x1, Object y1,
            Object w0, Object h0,
            Object w1, Object h1,
            Object w2, Object h2
        ) {
            mSet(
                x0, y0,
                x1, y1,
                w0, h0,
                w1, h1,
                w2, h2
            );
            return this;
        }
        
        public Mutable set(
            Layout l
        ) {
            mSet(
                l.x0, l.y0,
                l.x1, l.y1,
                l.w0, l.h0,
                l.w1, l.h1,
                l.w2, l.h2
            );
            return this;
        }
    
        @Override
        public Mutable copy() {
            return new Mutable(this);
        }
        
        public static Mutable fromString(String s) {
            return Layout.fromString(new Mutable(), s);
        }
    }
    
    public static class Attribute implements Copyable<Attribute>, Serializable {
        private static final long
            serialVersionUID = 1L;
        protected float
            x, w, h, u;
        
        public Attribute() {
            // do nothing
        }
        
        public Attribute(Object a) {
            mSet(a);
        }
        
        public Attribute(String s) {
            mSet(s);
        }
        
        public Attribute(Number a) {
            mSet(a);
        }
    
        public Attribute(float...  a) {
            mSet(a);
        }
        
        public Attribute(Number... a) {
            mSet(a);
        }
    
        public Attribute(Attribute a) {
            x = a.x();
            w = a.w();
            h = a.h();
            u = a.u();
        }
        
        protected void mSet(Object a) {
            if(a instanceof String)
                mSet((String) a);
            else if(a instanceof Number)
                mSet((Number) a);
            else if(a instanceof float[] )
                mSet((float[] )  a);
            else if(a instanceof Number[])
                mSet((Number[])  a);
            else if(a instanceof Attribute)
                mSet((Attribute) a);
        }
        
        protected void mSet(String s) {
            fromString(this, s);
        }
        
        protected void mSet(Number a) {
            x = a.floatValue();
        }
    
        protected void mSet(float[]   a) {
            switch(a.length) {
                default:
                case 4: u = a[3];
                case 3: h = a[2];
                        w = a[1];
                        x = a[0];
                    break;
                case 2: u = a[1];
                case 1: x = a[0];
                case 0:
            }
        }
        
        protected void mSet(Number[]  a) {
            switch(a.length) {
                default:
                case 4: u = a[3].floatValue();
                case 3: h = a[2].floatValue();
                        w = a[1].floatValue();
                        x = a[0].floatValue();
                    break;
                case 2: u = a[1].floatValue();
                case 1: x = a[0].floatValue();
                case 0:
            }
        }
        
        protected void mSet(Attribute a) {
            x = a.x();
            w = a.w();
            h = a.h();
            u = a.u();
        }
        
        public float x() { return x; } // relative to axis
        public float w() { return w; } // relative to w
        public float h() { return h; } // relative to h
        public float u() { return u; } // absolute
    
        @Override
        public Attribute copy() {
            return new Attribute(this);
        }
    
        @Override
        public String toString() {
            return Attribute.toString(this);
        }
    
        public static String toString(Attribute a) {
            String s = "";
            if(a.x() != 0) s += a.x();
            if(a.w() != 0) s += (s.length() > 0 && a.w() >= 0 ? "+" : "") + a.w() + "w";
            if(a.h() != 0) s += (s.length() > 0 && a.h() >= 0 ? "+" : "") + a.h() + "h";
            if(a.u() != 0) s += (s.length() > 0 && a.u() >= 0 ? "+" : "") + a.u() + "u";
            if(s.length() == 0) s += a.x();
            return s;
        }
        
        public static Attribute fromString(String s) {
            return Attribute.fromString(new Attribute(), s);
        }
        
        protected static <A extends Attribute> A fromString(A a, String s) {
            if(a != null && s != null && (s = s.trim()).length() > 0) {
                String
                    word = "";
                float
                    x = 0f,
                    w = 0f,
                    h = 0f,
                    u = 0f,
                    sign = 1f;
                
                for(int i = 0; i < s.length(); i ++) {
                    char c = s.charAt(i);
                    switch(c) {
                        case ' ':
                        case '\t':
                        case '\n':
                        case '\r':
                        case '+':
                            if(word.length() > 0) {
                                x += sign * stringToFloat(word);
                                word = "";
                                sign = 1f;
                            }
                        break;
                        case '-':
                            if(word.length() > 0) {
                                x += sign * stringToFloat(word);
                                word = "";
                                sign = 1f;
                            }
                            sign *= -1f;
                            break;
                        case 'w':
                            w += word.length() > 0 ? sign * stringToFloat(word) : sign;
                            word = "";
                            sign = 1f;
                            break;
                        case 'h':
                            h += word.length() > 0 ? sign * stringToFloat(word) : sign;
                            word = "";
                            sign = 1f;
                            break;
                        case 'u':
                            u += word.length() > 0 ? sign * stringToFloat(word) : sign;
                            word = "";
                            sign = 1f;
                            break;
                        default:
                            word += c;
                    }
                }
                if(word.length() > 0)
                    x += sign * stringToFloat(word);
                
                a.x = x;
                a.w = w;
                a.h = h;
                a.u = u;
            }
            return a;
        }
        
        public static class Mutable extends Attribute {
            private static final long
                serialVersionUID = 1L;
            
            public Mutable() {
                super();
            }
            
            public Mutable(Object a) {
                super(a);
            }
            
            public Mutable(String s) {
                super(s);
            }
            
            public Mutable(Number a) {
                super(a);
            }
            
            public Mutable(float...  a) {
                super(a);
            }
            
            public Mutable(Number... a) {
                super(a);
            }
            
            public Mutable(Attribute a) {
                super(a);
            }
            
            public float x(float x) { return this.x = x; }
            public float w(float w) { return this.w = w; }
            public float h(float h) { return this.h = h; }
            public float u(float u) { return this.u = u; }
            
            public Mutable set(Object a) {
                mSet(a);
                return this;
            }
    
            public Mutable set(String s) {
                mSet(s);
                return this;
            }
            
            public Mutable set(Number a) {
                mSet(a);
                return this;
            }
    
            public Mutable set(float...  a) {
                mSet(a);
                return this;
            }
    
            public Mutable set(Number... a) {
                mSet(a);
                return this;
            }
    
            public Mutable set(Attribute a) {
                mSet(a);
                return this;
            }
    
            @Override
            public Mutable copy() {
                return new Mutable(this);
            }
            
            public static Mutable fromString(String s) {
                return Attribute.fromString(new Mutable(), s);
            }
        }
    }
    
    public static Region2 regionOf(Layout l, Box src) {
        float[] layout = of(l,
            src.x(), src.y(),
            src.w(), src.h()
        );
        return new Region2(
            layout[0], layout[1],
            layout[2], layout[3]
        );
    }
    
    public static Region2.Mutable regionOf(Layout l, Box src, Region2.Mutable dst) {
        float[] layout = of(l,
            src.x(), src.y(),
            src.w(), src.h()
        );
        return dst.set(
            layout[0], layout[1],
            layout[2], layout[3]
        );
    }
    
    public static Bounds2 boundsOf(Layout l, Box src) {
        float[] layout = of(l,
            src.x(), src.y(),
            src.w(), src.h()
        );
        return new Bounds2(
            layout[0], layout[1],
            layout[4], layout[5]
        );
    }
    
    public static Bounds2.Mutable boundsOf(Layout l, Box src, Bounds2.Mutable dst) {
        float[] layout = Layout.of(l,
            src.x(), src.y(),
            src.w(), src.h()
        );
        return dst.set(
            layout[0], layout[1],
            layout[4], layout[5]
        );
    }
    
    public static float[] of(
        Layout l,
        float x, float y,
        float w, float h
    ) {
        float
            w1 = dx(l.w1(), w, h),
            h1 = dy(l.h1(), w, h),
            
            w2 = dx(l.w2(), w, h),
            h2 = dy(l.h2(), w, h),
            
            w0 = dx(l.w0(), w, h, w1, w2),
            h0 = dy(l.h0(), w, h, h1, h2),
    
            x0 = dx(l.x0(), w, h),
            y0 = dy(l.y0(), w, h),
            
            x1 = dx(l.x1(), w0, h0),
            y1 = dy(l.y1(), w0, h0);
        
        return new float[] {
            x + x0 - x1,      // x, x0
            y + y0 - y1,      // y, y0
            w0,               // w
            h0,               // h
            x + x0 - x1 + w0, //    x1
            y + y0 - y1 + h0  //    y1
        };
    }
    
    // compute value of x attribute
    protected static float dx(Attribute a, float w0, float h0) {
        return (a.x() * w0) + (a.w() * w0) + (a.h() * h0) + a.u();
    }
    
    // compute value of y attribute
    protected static float dy(Attribute a, float w0, float h0) {
        return (a.x() * h0) + (a.w() * w0) + (a.h() * h0) + a.u();
    }
    
    // compute and clamp value of x attribute
    protected static float dx(Attribute a, float w0, float h0, float w1, float w2) {
        return clamp(dx(a, w0, h0), w1, w2);
    }
    
    // compute and clamp value of y attribute
    protected static float dy(Attribute a, float w0, float h0, float h1, float h2) {
        return clamp(dy(a, w0, h0), h1, h2);
    }
    
    protected static float clamp(float x, float a, float b) {
        if(x <= a) return a;
        if(x >= b) return b;
        return x;
    }
}
