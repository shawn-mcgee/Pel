package lrj.math;

public interface Region extends Box {
    @Override
    default float x0() { return Math.min(x(), x() + w()); }
    @Override
    default float y0() { return Math.min(y(), y() + h()); }
    @Override
    default float z0() { return Math.min(z(), z() + d()); }
    
    @Override
    default float x1() { return Math.max(x(), x() + w()); }
    @Override
    default float y1() { return Math.max(y(), y() + h()); }
    @Override
    default float z1() { return Math.max(z(), z() + d()); }
    
    @Override
    default float x2() { return x() + w() / 2; }
    @Override
    default float y2() { return y() + h() / 2; }
    @Override
    default float z2() { return z() + d() / 2; }
    
    public static interface Mutable extends Region, Box.Mutable {
        @Override
        default Region.Mutable x0(float x0) {
            w(x1() - x0);
            x(       x0);
            return this;
        }
    
        @Override
        default Region.Mutable y0(float y0) {
            h(y1() - y0);
            y(       y0);
            return this;
        }
    
        @Override
        default Region.Mutable z0(float z0) {
            d(z1() - z0);
            z(       z0);
            return this;
        }
    
        @Override
        default Region.Mutable x1(float x1) {
            w(x0() - x1);
            x(       x1);
            return this;
        }
    
        @Override
        default Region.Mutable y1(float y1) {
            h(y0() - y1);
            y(       y1);
            return this;
        }
    
        @Override
        default Region.Mutable z1(float z1) {
            d(z0() - z1);
            z(       z1);
            return this;
        }
    
        @Override
        default Region.Mutable x2(float x2) {
            x(x() + x2 - x2());
            return this;
        }
    
        @Override
        default Region.Mutable y2(float y2) {
            y(y() + y2 - y2());
            return this;
        }
    
        @Override
        default Region.Mutable z2(float z2) {
            z(z() + z2 - z2());
            return this;
        }
    }
}
