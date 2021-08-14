package lrj.util;

import java.util.Map;

import static lrj.util.ObjectToString.objectToString;
import static lrj.util.StringToObject.stringToObject;

public interface Configurable {
    
    public Map<String, String> getConfiguration();
    
    public default void configure(Object... args) {
        Configurable.configure(getConfiguration(), args);
    }
    
    public default void loadConfiguration(Resource resource) {
        Configurable.loadConfiguration(getConfiguration(), resource);
    }
    
    public default void loadConfiguration(String   resource) {
        Configurable.loadConfiguration(getConfiguration(), resource);
    }
    
    public default void loadConfiguration(Class<?> from, String path) {
        Configurable.loadConfiguration(getConfiguration(), from, path);
    }
    
    public default void loadConfiguration(String   from, String path) {
        Configurable.loadConfiguration(getConfiguration(), from, path);
    }
    
    public default void saveConfiguration(Resource resource) {
        Configurable.saveConfiguration(getConfiguration(), resource);
    }
    
    public default void saveConfiguration(String   resource) {
        Configurable.saveConfiguration(getConfiguration(), resource);
    }
    
    public default String setProperty(Object key, Object val) {
        return Configurable.setProperty(getConfiguration(), key, val);
    }
    
    public default <OBJECT> String setProperty(Object key, ObjectToString<OBJECT> o2s, OBJECT val            ) {
        return Configurable.setProperty(getConfiguration(), key, o2s, val     );
    }
    
    public default <OBJECT> String setProperty(Object key, ObjectToString<OBJECT> o2s, OBJECT val, String alt) {
        return Configurable.setProperty(getConfiguration(), key, o2s, val, alt);
    }
    
    public default String getProperty(Object key            ) {
        return Configurable.getProperty(getConfiguration(), key     );
    }
    
    public default String getProperty(Object key, Object alt) {
        return Configurable.getProperty(getConfiguration(), key, alt);
    }
    
    public default <OBJECT> OBJECT getPropertyAs(Object key, StringToObject<OBJECT> s2o            ) {
        return Configurable.getPropertyAs(getConfiguration(), key, s2o     );
    }
    
    public default <OBJECT> OBJECT getPropertyAs(Object key, StringToObject<OBJECT> s2o, OBJECT alt) {
        return Configurable.getPropertyAs(getConfiguration(), key, s2o, alt);
    }
    
    public static <T extends Map<String, String>> T configure(T map, Object... args) {
        int
            n = args.length & 1,
            m = args.length - n;
        for(int i = 0; i < m; i += 2) {
            int
                a = i + 0,
                b = i + 1;
            setProperty(map, args[a], args[b]);
        }
        return map;
    }
    
    public static String setProperty(Map<String, String> map, Object key, Object val) {
        if(map != null) {
            String
                _key = key != null ? key.toString() : null,
                _val = val != null ? val.toString() : null;
            map.put(_key, _val);
            return        _val ;
        } else
            return        null ;
    }
    
    public static <OBJECT> String setProperty(Map<String, String> map, Object key, ObjectToString<OBJECT> o2s, OBJECT val            ) {
        return setProperty(map, key, objectToString(o2s, val, null));
    }
    
    public static <OBJECT> String setProperty(Map<String, String> map, Object key, ObjectToString<OBJECT> o2s, OBJECT val, String alt) {
        return setProperty(map, key, objectToString(o2s, val, alt ));
    }
    
    public static String getProperty(Map<String, String> map, Object key            ) {
        return getProperty(map, key, null);
    }
    
    public static String getProperty(Map<String, String> map, Object key, Object alt) {
        String
            _key = key != null ? key.toString() : null,
            _alt = alt != null ? alt.toString() : null;
        try {
            String _val = map.get(_key);
            return _val != null ? _val : _alt;
        } catch(Exception na) {
            return                       _alt;
        }
    }
    
    public static <OBJECT> OBJECT getPropertyAs(Map<String, String> map, Object key, StringToObject<OBJECT> s2o            ) {
        return getPropertyAs(map, key, s2o, null);
    }
    
    public static <OBJECT> OBJECT getPropertyAs(Map<String, String> map, Object key, StringToObject<OBJECT> s2o, OBJECT alt) {
        String _key = key != null ? key.toString() : null;
        try {
            String _val = map.get(_key);
            return _val != null ? stringToObject(s2o, _val, alt) : alt;
        } catch(Exception na) {
            return                                                 alt;
        }
    }
    
    public static <T extends Map<String, String>> T loadConfiguration(T map, Resource resource) {
        return Configurable.fromString(map, Resource.readString(resource));
    }
    
    public static <T extends Map<String, String>> T loadConfiguration(T map, String   resource) {
        return Configurable.fromString(map, Resource.readString(resource));
    }
    
    public static <T extends Map<String, String>> T loadConfiguration(T map, Class<?> from, String path) {
        return Configurable.fromString(map, Resource.readString(from, path));
    }
    
    public static <T extends Map<String, String>> T loadConfiguration(T map, String   from, String path) {
        return Configurable.fromString(map, Resource.readString(from, path));
    }
    
    public static <T extends Map<String, String>> void saveConfiguration(T map, Resource resource) {
        Resource.writeString(resource, Configurable.toString(map));
    }
    
    public static <T extends Map<String, String>> void saveConfiguration(T map, String   resource) {
        Resource.writeString(resource, Configurable.toString(map));
    }
    
    public static <T extends Map<String, String>> String toString(T map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((key, val) -> {
            sb.append(key)
                .append(": ")
                .append(val )
                .append("\n");
        });
        return sb.toString();
    }
    
    public static <T extends Map<String, String>> T fromString(T map, String s) {
        if(s != null) {
            String[] t = (s + " ").split("\\n");
            for (String u: t) {
                if((u = u.trim()).startsWith("//"))
                    continue;
                int i = u.indexOf(":");
                if(i >= 0) {
                    String
                        u0 = u.substring(0, i).trim(),
                        u1 = u.substring(++ i).trim();
                    setProperty(map, u0, u1);
                }
            }
        }
        return map;
    }
}
