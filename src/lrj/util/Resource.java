package lrj.util;

import java.io.*;
import java.util.Objects;

public class Resource {
    protected Class<?>
        from;
    protected String
        path;
    
    public Resource(Resource resource) {
        this.from = resource.from;
        this.path = resource.path;
    }
    
    public Resource(String   resource) {
        String[] u = (resource + " ").split(":");
        String
            from = u.length > 1 ? u[0].strip() : null,
            path = u.length > 1 ? u[1].strip() : u[0].strip();
        try {
            if(from != null) {
                this.path = path;
                this.from = Class.forName(from);
            } else {
                this.path = path;
                this.from = null;
            }
        } catch(Exception na) {
            Debug.warn("jasper.util.Resource.fromString", na);
        }
    }
    
    public Resource(Class<?> from, String path) {
        this.from = from;
        this.path = path;
    }
    
    public Resource(String   from, String path) {
        try {
            if(from != null) {
                this.path = path;
                this.from = Class.forName(from);
            } else {
                this.path = path;
                this.from = null;
            }
        } catch(Exception na) {
            Debug.warn("jasper.util.Resource.fromString", na);
        }
    }
    
    public Class<?> from() {
        return from;
    }
    
    public String   path() {
        return path;
    }
    
    public InputStream  newInputStream()  {
        return newInputStream(this);
    }
    
    public OutputStream newOutputStream() {
        return newOutputStream(this);
    }
    
    public byte[] readBytes() {
        return readBytes(this);
    }
    
    public <T> T  readObject() {
        return readObject(this);
    }
    
    public String readString() {
        return readString(this);
    }
    
    public void writeBytes(byte[] b) {
        writeBytes(this, b);
    }
    
    public <T> void writeObject(T t) {
        writeObject(this, t);
    }
    
    public     void writeString(Object o) {
        writeString(this, o);
    }
    
    @Override
    public String toString() {
        return Resource.toString(this);
    }
    
    public static String toString(Resource resource) {
        return Resource.toString(
            resource.from,
            resource.path
        );
    }
    
    public static String toString(Class<?> from, String path) {
        if(from != null)
            return from.getName() + ":" + path;
        else
            return                        path;
    }
    
    public static String toString(String   from, String path) {
        if(from != null)
            return from + ":" + path;
        else
            return              path;
    }
    
    public static Resource fromString(String s) {
        return new Resource(s);
    }
    
    public static boolean exists(Resource resource) {
        if(resource.from != null)
            return resource.from.getResource(resource.path) != null;
        else
            return new File(resource.path).exists();
    }
    
    public static boolean exists(String   resource) {
        return exists(new Resource(resource));
    }
    
    public static boolean exists(Class<?> from, String path) {
        return exists(new Resource(from, path));
    }
    
    public static boolean exists(String   from, String path) {
        return exists(new Resource(from, path));
    }
    
    public static InputStream newInputStream(Resource resource) {
        if(resource.from != null)
            return resource.from.getResourceAsStream(resource.path);
        else try {
            return new               FileInputStream(resource.path);
        } catch(Exception na) {
            Debug.warn(new Object() { }, "Unable to open resource '" + resource + "'");
            return null;
        }
    }
    
    public static InputStream newInputStream(String   resource) {
        return newInputStream(new Resource(resource));
    }
    
    public static InputStream newInputStream(Class<?> from, String path) {
        return newInputStream(new Resource(from, path));
    }
    
    public static InputStream newInputStream(String   from, String path) {
        return newInputStream(new Resource(from, path));
    }
    
    public static OutputStream newOutputStream(Resource resource) {
        if(resource.from != null)
            Debug.warn(new Object() { }, "Unable to open resource '" + resource + "'");
        else try {
            File file = new File(resource.path);
            if(!file.exists()) {
                if (file.getParentFile() != null)
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            return new FileOutputStream(file);
        } catch(Exception na) {
            Debug.warn(new Object() { }, "Unable to open resource '" + resource + "'");
        }
        return null;
    }
    
    public static OutputStream newOutputStream(String   resource) {
        return newOutputStream(new Resource(resource));
    }
    
    public static byte[] readBytes(InputStream from, String resource) {
        try(BufferedInputStream in = new BufferedInputStream(from)) {
            return in.readAllBytes();
        } catch (Exception na) {
            Debug.warn(new Object() { }, "Unable to read bytes from resource '" + resource + "'");
        }
        return null;
    }
    
    public static byte[] readBytes(Resource resource) {
        return readBytes(newInputStream(resource), toString(resource));
    }
    
    public static byte[] readBytes(String   resource) {
        return readBytes(new Resource(resource));
    }
    
    public static byte[] readBytes(Class<?> from, String path) {
        return readBytes(newInputStream(from, path), toString(from, path));
    }
    
    public static byte[] readBytes(String   from, String path) {
        return readBytes(newInputStream(from, path), toString(from, path));
    }
    
    public static void writeBytes(Resource resource, byte[] b) {
        writeBytes(newOutputStream(resource), b, toString(resource));
    }
    
    public static void writeBytes(String   resource, byte[] b) {
        writeBytes(new Resource(resource), b);
    }
    
    public static <T> T readObject(Resource resource) {
        return readObject(newInputStream(resource), toString(resource));
    }
    
    public static <T> T readObject(String   resource) {
        return readObject(new Resource(resource));
    }
    
    public static <T> T readObject(Class<?> from, String path) {
        return readObject(newInputStream(from, path), toString(from, path));
    }
    
    public static <T> T readObject(String   from, String path) {
        return readObject(newInputStream(from, path), toString(from, path));
    }
    
    public static <T> void writeObject(Resource resource, T t) {
        writeObject(newOutputStream(resource), t, toString(resource));
    }
    
    public static <T> void writeObject(String   resource, T t) {
        writeObject(new Resource(resource), t);
    }
    
    public static String readString(Resource resource) {
        return readString(newInputStream(resource), toString(resource));
    }
    
    public static String readString(String   resource) {
        return readString(new Resource(resource));
    }
    
    public static String readString(Class<?> from, String path) {
        return readString(newInputStream(from, path), toString(from, path));
    }
    
    public static String readString(String   from, String path) {
        return readString(newInputStream(from, path), toString(from, path));
    }
    
    public static void writeString(Resource resource, Object o) {
        writeString(newOutputStream(resource), o, toString(resource));
    }
    
    public static void writeString(String   resource, Object o) {
        writeString(new Resource(resource), o);
    }
    
    public static void writeBytes(OutputStream to, byte[] b, String resource) {
        try(BufferedOutputStream out = new BufferedOutputStream(to)) {
            out.write(b);
            out.flush( );
        } catch (Exception na) {
            Debug.warn(new Object() { }, "Unable to write bytes to resource '" + resource + "'");
        }
    }
    
    public static <T> T readObject(InputStream from, String resource) {
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(from))) {
            return Unsafe.cast(in.readObject());
        } catch (Exception na) {
            Debug.warn(new Object() { }, "Unable to read from resource '" + resource + "'");
        }
        return null;
    }
    
    public static <T> void writeObject(OutputStream to, T t, String resource) {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(to))) {
            out.writeObject(t);
            out.flush      ( );
        } catch (Exception na) {
            Debug.warn(new Object() { }, "Unable to write to resource '" + resource + "'");
        }
    }
    
    public static String readString(InputStream from, String resource) {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(from))) {
            StringBuilder sb = new StringBuilder();
            int c;
            while((c = in.read()) != -1)
                sb.append((char)c);
            return sb.toString();
        } catch(Exception na) {
            Debug.warn(new Object() { }, "Unable to read string from resource '" + resource + "'");
        }
        return null;
    }
    
    public static void writeString(OutputStream to, Object o, String resource) {
        try(BufferedWriter out = new BufferedWriter(new OutputStreamWriter(to))) {
            String s = Objects.toString(o);
            out.write(s);
            out.flush( );
        } catch(Exception na) {
            Debug.warn(new Object() { }, "Unable to write string to resource '" + resource + "'");
        }
    }
}
