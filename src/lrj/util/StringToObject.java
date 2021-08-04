package lrj.util;

public interface StringToObject<OBJECT> {
    public static final StringToObject<Boolean>
        BOOLEAN = StringToObject::stringToBoolean;
    public static final StringToObject<Integer>
        INTEGER = StringToObject::stringToInteger;
    public static final StringToObject<Float>
        FLOAT   = StringToObject::stringToFloat;
    
    public OBJECT toObject(String s);
    
    public static Byte stringToByte(String s        ) {
        return stringToByte(s, (byte)0);
    }
    
    public static Byte stringToByte(String s, Byte b) {
        try {
            return Byte.valueOf(s);
        } catch(Exception na) {
            return b;
        }
    }
    
    public static Boolean stringToBoolean(String s           ) {
        return stringToBoolean(s, false);
    }
    
    public static Boolean stringToBoolean(String s, Boolean b) {
        try {
            return Boolean.valueOf(s);
        } catch(Exception na) {
            return b;
        }
    }
    
    public static Integer stringToInteger(String s           ) {
        return stringToInteger(s, 0);
    }
    
    public static Integer stringToInteger(String s, Integer i) {
        try {
            return Integer.valueOf(s);
        } catch(Exception na) {
            return i;
        }
    }
    
    public static Long stringToLong(String s        ) {
        return stringToLong(s, 0L);
    }
    
    public static Long stringToLong(String s, Long l) {
        try {
            return Long.valueOf(s);
        } catch(Exception na) {
            return l;
        }
    }
    
    public static Float stringToFloat(String s         ) {
        return stringToFloat(s, 0f);
    }
    
    public static Float stringToFloat(String s, Float f) {
        try {
            return Float.valueOf(s);
        } catch(Exception na) {
            return f;
        }
    }
    
    public static <OBJECT> OBJECT stringToObject(StringToObject<OBJECT> s2o, String s          ) {
        return stringToObject(s2o, s, null);
    }
    
    public static <OBJECT> OBJECT stringToObject(StringToObject<OBJECT> s2o, String s, OBJECT t) {
        try {
            return s2o.toObject(s);
        } catch(Exception na) {
            return t;
        }
    }
}
