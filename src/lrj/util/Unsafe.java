package lrj.util;

public final class Unsafe {
    
    private Unsafe() {
        // do nothing
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o               ) {
        return (T) o;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o, Class<T> type) {
        return (T) o;
    }
}
