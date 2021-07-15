package pel.util;

import java.io.Serializable;

public class Version implements Comparable<Version>, Serializable {
    private static final long
        serialVersionUID = 1L;
    
    public final String
        string;
    public final int
        major,
        minor,
        patch;
    
    public Version(
        String string,
        int major,
        int minor,
        int patch
    ) {
        this.string = string;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
    
    @Override
    public String toString() {
        return string +
            " " + major +
            "." + minor +
            "." + patch;
    }
    
    @Override
    public int compareTo(Version o) {
        int k;
        if ((k = major - o.major) != 0)
            return k;
        if ((k = minor - o.minor) != 0)
            return k;
        if ((k = patch - o.patch) != 0)
            return k;
        return 0;
    }
}
