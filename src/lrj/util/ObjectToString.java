package lrj.util;

public interface ObjectToString<OBJECT> {
    public static final ObjectToString<Boolean>
        BOOLEAN = String::valueOf;
    public static final ObjectToString<Integer>
        INTEGER = String::valueOf;
    public static final ObjectToString<Float>
        FLOAT   = String::valueOf;
    
    public String toString(OBJECT t);
    
    public static <OBJECT> String objectToString(ObjectToString<OBJECT> o2s, OBJECT t          ) {
        return objectToString(o2s, t, null);
    }
    
    public static <OBJECT> String objectToString(ObjectToString<OBJECT> o2s, OBJECT t, String s) {
        try {
            return o2s.toString(t);
        } catch(Exception na) {
            return s;
        }
    }
}
